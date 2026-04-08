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
    private static final int N = 6;
    private static final String DATA_FILE = "ml_frequency_data.txt";

    private final Random random;
    private final List<Move> recentChoices;
    private final Map<String, Integer> sequenceCounts;

    private final MLFrequencyDataStore dataStore;

    // what we predicted the human would play last round (null if we guessed randomly)
    private Move lastPrediction = null;

    public MLComputerPlayer() {
        random = new Random();
        recentChoices = new ArrayList<>();
        dataStore = new MLFrequencyDataStore(DATA_FILE);
        sequenceCounts = dataStore.load();
    }

    @Override
    public Move getMove() {
        if (recentChoices.size() < N - 1) {
            lastPrediction = null;
            return getRandomMove();
        }
        return getMachineLearningMove();
    }

    @Override
    public Move getLastPrediction() {
        return lastPrediction;
    }

    @Override
    public void updateHistory(Move myMove, Move opponentMove) {
        // add human's move first so we can predict it next time
        Move humanMove = opponentMove;
        Move computerMove = myMove;
        
        appendChoice(humanMove);
        appendChoice(computerMove);
        recordCurrentSequence();
    }

    @Override
    public void saveData() {
        dataStore.save(sequenceCounts);
    }

    private Move getMachineLearningMove() {
        String prefix = getLastNMinusOneChoices();

        int rockCount     = sequenceCounts.getOrDefault(prefix + "R", 0);
        int paperCount    = sequenceCounts.getOrDefault(prefix + "P", 0);
        int scissorsCount = sequenceCounts.getOrDefault(prefix + "S", 0);

        int maxCount = Math.max(rockCount, Math.max(paperCount, scissorsCount));

        if (maxCount == 0) {
            lastPrediction = null;
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

        lastPrediction = predictedHumanMove;
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

    private Move getRandomMove() {
        int value = random.nextInt(3);
        if (value == 0)      return Move.ROCK;
        else if (value == 1) return Move.PAPER;
        else                 return Move.SCISSORS;
    }

    private Move getCounterMove(Move predictedHumanMove) {
        if (predictedHumanMove == Move.ROCK)  return Move.PAPER;
        if (predictedHumanMove == Move.PAPER) return Move.SCISSORS;
        else                                   return Move.ROCK;
    }

    private char moveToLetter(Move move) {
        if (move == Move.ROCK)  return 'R';
        if (move == Move.PAPER) return 'P';
        else                    return 'S';
    }

    private String convertSequenceToString(List<Move> sequence) {
        StringBuilder builder = new StringBuilder();
        for (Move move : sequence) {
            builder.append(moveToLetter(move));
        }
        return builder.toString();
    }
}
