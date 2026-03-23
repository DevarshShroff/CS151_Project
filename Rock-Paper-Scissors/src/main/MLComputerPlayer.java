import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MLComputerPlayer implements Player {
    private static final int N = 5; // size of sequence
    private static final String DATA_FILE = "ml_frequency_data.txt"; // file to save data

    private final Random random;
    private final List<Move> recentChoices; // last N moves
    private final Map<String, Integer> sequenceCounts; // sequence -> frequency

    private Move currentComputerMove;

    public MLComputerPlayer() {
        random = new Random();
        recentChoices = new ArrayList<>();
        sequenceCounts = new HashMap<>();
        currentComputerMove = null;
        loadFrequencyData(); // load past data
    }

    @Override
    public Move getMove() {
        Move chosenMove;

        // if not enough data, use random
        if (recentChoices.size() < N - 1) {
            chosenMove = getRandomMove();
        } else {
            chosenMove = getMachineLearningMove(); // use ML
        }

        currentComputerMove = chosenMove;

        recentChoices.add(chosenMove); // add computer move
        trimHistory(); // keep size N

        return chosenMove;
    }

    @Override
    public String getName() {
        return "CPU (Machine Learning)";
    }

    @Override
    public void updateHistory(Move opponentMove) {
        recentChoices.add(opponentMove); // add human move
        trimHistory();

        // if we have full sequence, update count
        if (recentChoices.size() == N) {
            String sequence = convertSequenceToString(recentChoices);
            int oldCount = sequenceCounts.getOrDefault(sequence, 0);
            sequenceCounts.put(sequence, oldCount + 1);
        }
    }

    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            // write all sequences to file
            for (Map.Entry<String, Integer> entry : sequenceCounts.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save ML data.");
        }
    }

    private void loadFrequencyData() {
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return; // no file yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts.length == 2) {
                    String sequence = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    sequenceCounts.put(sequence, count); // load into map
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load ML data.");
        }
    }

    private Move getMachineLearningMove() {
        String prefix = getLastNMinusOneChoices(); // last N-1 moves

        // check frequencies
        int rockCount = sequenceCounts.getOrDefault(prefix + "R", 0);
        int paperCount = sequenceCounts.getOrDefault(prefix + "P", 0);
        int scissorsCount = sequenceCounts.getOrDefault(prefix + "S", 0);

        int maxCount = Math.max(rockCount, Math.max(paperCount, scissorsCount));

        // if no data, random
        if (maxCount == 0) {
            return getRandomMove();
        }

        Move predictedHumanMove;

        // predict most frequent move
        if (rockCount >= paperCount && rockCount >= scissorsCount) {
            predictedHumanMove = Move.ROCK;
        } else if (paperCount >= rockCount && paperCount >= scissorsCount) {
            predictedHumanMove = Move.PAPER;
        } else {
            predictedHumanMove = Move.SCISSORS;
        }

        return getCounterMove(predictedHumanMove); // beat it
    }

    private String getLastNMinusOneChoices() {
        StringBuilder builder = new StringBuilder();
        int start = recentChoices.size() - (N - 1);

        // build prefix string
        for (int i = start; i < recentChoices.size(); i++) {
            builder.append(moveToLetter(recentChoices.get(i)));
        }

        return builder.toString();
    }

    private void trimHistory() {
        // keep only last N moves
        while (recentChoices.size() > N) {
            recentChoices.remove(0);
        }
    }

    private Move getRandomMove() {
        int value = random.nextInt(3);

        // random choice
        if (value == 0) {
            return Move.ROCK;
        } else if (value == 1) {
            return Move.PAPER;
        } else {
            return Move.SCISSORS;
        }
    }

    private Move getCounterMove(Move predictedHumanMove) {
        // return move that beats human
        if (predictedHumanMove == Move.ROCK) {
            return Move.PAPER;
        } else if (predictedHumanMove == Move.PAPER) {
            return Move.SCISSORS;
        } else {
            return Move.ROCK;
        }
    }

    private char moveToLetter(Move move) {
        // convert move to char
        if (move == Move.ROCK) {
            return 'R';
        } else if (move == Move.PAPER) {
            return 'P';
        } else {
            return 'S';
        }
    }

    private String convertSequenceToString(List<Move> sequence) {
        StringBuilder builder = new StringBuilder();

        // convert list to string
        for (Move move : sequence) {
            builder.append(moveToLetter(move));
        }

        return builder.toString();
    }
}
