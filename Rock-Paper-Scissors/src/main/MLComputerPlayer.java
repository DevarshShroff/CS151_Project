import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MLComputerPlayer implements ComputerChoiceAlgorithm {
    private static final int N = 5;
    private static final String DATA_FILE = "ml_frequency_data.txt";

    private final Random random;
    private final List<Move> recentChoices;
    private final Map<String, Integer> sequenceCounts;

    public MLComputerPlayer() {
        random = new Random();
        recentChoices = new ArrayList<>();
        sequenceCounts = new HashMap<>();
        loadFrequencyData();
    }

    @Override
    public Move getMove() {
        if (recentChoices.size() < N - 1) {
            return getRandomMove();
        }

        return getMachineLearningMove();
    }

    @Override
    public void updateHistory(Move myMove, Move opponentMove) {
        // Record moves in the actual round order: human first, computer second.
        // Store frequencies only for windows that end on the human's choice,
        // which is the next event we want to predict.
        appendChoice(opponentMove);
        recordCurrentSequence();
        appendChoice(myMove);
    }

    @Override
    public void saveData() {
        File file = resolveDataFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : sequenceCounts.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save ML data.");
        }
    }

    private void loadFrequencyData() {
        File file = resolveDataFile();

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts.length == 2) {
                    String sequence = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    sequenceCounts.put(sequence, count);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load ML data.");
        }
    }

    private Move getMachineLearningMove() {
        String prefix = getLastNMinusOneChoices();

        int rockCount = sequenceCounts.getOrDefault(prefix + "R", 0);
        int paperCount = sequenceCounts.getOrDefault(prefix + "P", 0);
        int scissorsCount = sequenceCounts.getOrDefault(prefix + "S", 0);

        int maxCount = Math.max(rockCount, Math.max(paperCount, scissorsCount));

        if (maxCount == 0) {
            return getRandomMove();
        }

        Move predictedHumanMove;

        if (rockCount >= paperCount && rockCount >= scissorsCount) {
            predictedHumanMove = Move.ROCK;
        } else if (paperCount >= rockCount && paperCount >= scissorsCount) {
            predictedHumanMove = Move.PAPER;
        } else {
            predictedHumanMove = Move.SCISSORS;
        }

        return getCounterMove(predictedHumanMove);
    }

    private String getLastNMinusOneChoices() {
        StringBuilder builder = new StringBuilder();
        int start = recentChoices.size() - (N - 1);

        for (int i = start; i < recentChoices.size(); i++) {
            builder.append(moveToLetter(recentChoices.get(i)));
        }

        return builder.toString();
    }

    private void appendChoice(Move move) {
        recentChoices.add(move);
        trimHistory();
    }

    private void recordCurrentSequence() {
        if (recentChoices.size() == N) {
            String sequence = convertSequenceToString(recentChoices);
            sequenceCounts.put(sequence, sequenceCounts.getOrDefault(sequence, 0) + 1);
        }
    }

    private void trimHistory() {
        while (recentChoices.size() > N) {
            recentChoices.remove(0);
        }
    }

    private File resolveDataFile() {
        Path projectDataFile = Paths.get("src", "main", DATA_FILE);
        Path localDataFile = Paths.get(DATA_FILE);

        if (projectDataFile.toFile().exists()) {
            return projectDataFile.toFile();
        }

        if (localDataFile.toFile().exists()) {
            return localDataFile.toFile();
        }

        if (projectDataFile.getParent().toFile().exists()) {
            return projectDataFile.toFile();
        }

        return localDataFile.toFile();
    }

    private Move getRandomMove() {
        int value = random.nextInt(3);

        if (value == 0) {
            return Move.ROCK;
        } else if (value == 1) {
            return Move.PAPER;
        } else {
            return Move.SCISSORS;
        }
    }

    private Move getCounterMove(Move predictedHumanMove) {
        if (predictedHumanMove == Move.ROCK) {
            return Move.PAPER;
        } else if (predictedHumanMove == Move.PAPER) {
            return Move.SCISSORS;
        } else {
            return Move.ROCK;
        }
    }

    private char moveToLetter(Move move) {
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

        for (Move move : sequence) {
            builder.append(moveToLetter(move));
        }

        return builder.toString();
    }
}
