import java.util.*;
import java.io.*;

public class MLComputerPlayer implements Player {

    private static final int N = 5;
    private static final String DATA_FILE = "ml_frequencies.dat";

    private final Map<String, int[]> frequencies;
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

        // Predict what the human is most likely to play next
        Move predictedHumanMove = predictHumanMove(prefix);

        if (predictedHumanMove == null) {
            // No data for this sequence yet — pick randomly
            return randomMove();
        }

        // Choose the move that beats the predicted human move
        return beats(predictedHumanMove);
    }

    /**
     * Finds the most frequently observed human move following the given prefix.
     * Returns null if the prefix has never been seen before.
     */
    private Move predictHumanMove(String prefix) {
        int bestCount = 0;
        Move bestMove = null;

        for (Move candidate : Move.values()) {
            String fullKey = prefix + "," + candidate.name();
            int[] counts = frequencies.get(fullKey);
            if (counts != null && counts[0] > bestCount) {
                bestCount = counts[0];
                bestMove = candidate;
            }
        }
        return bestMove;
    }

    /** Returns the move that beats the given move. */
    private Move beats(Move m) {
        switch (m) {
            case ROCK:     return Move.PAPER;
            case PAPER:    return Move.SCISSORS;
            case SCISSORS: return Move.ROCK;
            default:       return randomMove();
        }
    }

    /**
     * Called by GameController after each round with the human's last move.
     * Adds it to the sliding window and updates frequencies.
     */
    @Override
    public void updateHistory(Move opponentMove) {
        history.addLast(opponentMove);

        // Keep window at size N
        while (history.size() > N) {
            history.removeFirst();
        }

        // Once we have a full window, record the sequence
        if (history.size() == N) {
            List<Move> moveList = new ArrayList<>(history);
            String prefix = buildKeyFromList(moveList.subList(0, N - 1));
            Move lastMove = moveList.get(N - 1);
            String fullKey = prefix + "," + lastMove.name();

            // Each entry is a single int[1] frequency counter
            int[] counts = frequencies.computeIfAbsent(fullKey, k -> new int[]{0});
            counts[0]++;
        }

        saveFrequencies();
    }

    private String buildKey(Deque<Move> deque) {
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