import java.util.Random;

public class RandomComputerPlayer implements ComputerChoiceAlgorithm {
    private final Random random = new Random();

    @Override
    public Move getMove() {
        int val = random.nextInt(3);

        switch (val) {
            case 0:
                return Move.ROCK;
            case 1:
                return Move.PAPER;
            default:
                return Move.SCISSORS;
        }
    }

    @Override
    public void updateHistory(Move myMove, Move opponentMove) {
        // Random player does not use history.
    }

    @Override
    public void saveData() {
        // Random player has nothing to persist.
    }
}
