import java.util.*;
import java.io.*;

public class MLComputerPlayer implements Player {

    // N = window size (last N moves form a sequence)
    private static final int N = 5;
    private static final String DATA_FILE = "ml_frequencies.dat";

    // Key: sequence string like "ROCK,PAPER,SCISSORS,ROCK,PAPER"
    // Value: int[3] -> counts for predicted next human move: [ROCK, PAPER, SCISSORS]
    private final Map<String, int[]> frequencies;

    // Circular buffer of the last N moves (alternating: human, computer, human, ...)
    // We track both players' moves in order, as described in the assignment
    private final Deque<Move> history;

    private final Random random;

    public MLComputerPlayer() {
        this.frequencies = new HashMap<>();
        this.history = new ArrayDeque<>();
        this.random = new Random();
        loadFrequencies();
    }

    @Override
    public Move getMove() {
        // If we don't have enough history yet, pick randomly
        if (history.size() < N - 1) {
            return randomMove();
        }

        // Build key from last N-1 moves (the prefix before the human's next move)
        String prefix = buildKey(history);

        // Look for all stored sequences that start with this prefix
        Move bestPrediction = null;
        int bestCount = -1;

        // Check each possible next human move
        for (Move humanPredicted : Move.values()) {
            String fullKey = prefix + "," + humanPredicted.name();
            int[] counts = frequencies.get(fullKey);
            if (counts != null) {
                int total = counts[0] + counts[1] + counts[2];
                if (total > bestCount) {
                    bestCount = total;
                    // Find which next human move is most predicted by this sequence
                    bestPrediction = humanPredicted;
                }
            }
        }

        // Find the most likely human move directly
        Move predictedHumanMove = predictHumanMove(prefix);

        if (predictedHumanMove == null) {
            // No data yet — pick randomly
            return randomMove();
        }

        // Choose the move that beats the predicted human move
        return beats(predictedHumanMove);
    }

    /**
     * Predict what the human will most likely play next, given the current history prefix.
     */
    private Move predictHumanMove(String prefix) {
        int bestCount = 0;
        Move bestMove = null;

        for (Move candidate : Move.values()) {
            String fullKey = prefix + "," + candidate.name();
            int[] counts = frequencies.get(fullKey);
            if (counts != null) {
                // counts[i] stores how many times THIS full sequence occurred
                // We stored a single frequency count under index 0
                int freq = counts[0];
                if (freq > bestCount) {
                    bestCount = freq;
                    bestMove = candidate;
                }
            }
        }
        return bestMove; // null if no data found
    }

    /**
     * Returns the move that beats the given move.
     */
    private Move beats(Move m) {
        switch (m) {
            case ROCK:     return Move.PAPER;
            case PAPER:    return Move.SCISSORS;
            case SCISSORS: return Move.ROCK;
            default:       return randomMove();
        }
    }

    /**
     * Called by GameController after each round with the opponent's (human's) last move.
     * We record both moves into the history buffer and update frequencies.
     */
    @Override
    public void updateHistory(Move opponentMove) {
        // Add the human's move to our sliding window
        history.addLast(opponentMove);

        // Keep window at size N
        while (history.size() > N) {
            history.removeFirst();
        }

        // Once we have a full window, record the full sequence
        if (history.size() == N) {
            // The key is the first N-1 moves; the Nth move is what we're predicting
            List<Move> moveList = new ArrayList<>(history);
            String prefix = buildKeyFromList(moveList.subList(0, N - 1));
            Move lastMove = moveList.get(N - 1);
            String fullKey = prefix + "," + lastMove.name();

            // Increment frequency count
            int[] counts = frequencies.computeIfAbsent(fullKey, k -> new int[]{0});
            counts[0]++;
        }

        saveFrequencies();
    }

    private String buildKey(Deque<Move> deque) {
        // Take first N-1 elements as the prefix
        List<Move> list = new ArrayList<>(deque);
        int end = Math.min(N - 1, list.size());
        return buildKeyFromList(list.subList(0, end));
    }

    private String buildKeyFromList(List<Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(moves.get(i).name());
        }
        return sb.toString();
    }

    private Move randomMove() {
        Move[] moves = Move.values();
        return moves[random.nextInt(moves.length)];
    }

    // --- Persistence: save and load frequency map to/from file ---

    private void saveFrequencies() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, int[]> entry : frequencies.entrySet()) {
                pw.println(entry.getKey() + "|" + entry.getValue()[0]);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not save ML data: " + e.getMessage());
        }
    }

    private void loadFrequencies() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                int sep = line.lastIndexOf('|');
                if (sep < 0) continue;
                String key = line.substring(0, sep);
                int count = Integer.parseInt(line.substring(sep + 1).trim());
                frequencies.put(key, new int[]{count});
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Warning: Could not load ML data: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "CPU (Machine Learning)";
    }
}