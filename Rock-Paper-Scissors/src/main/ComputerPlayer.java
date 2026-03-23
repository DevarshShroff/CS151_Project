public class ComputerPlayer implements Player {
    private final String name;
    private final ComputerChoiceAlgorithm algorithm;

    public ComputerPlayer(String name, ComputerChoiceAlgorithm algorithm) {
        this.name = name;
        this.algorithm = algorithm;
    }

    @Override
    public Move getMove() {
        return algorithm.getMove();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateHistory(Move myMove, Move opponentMove) {
        algorithm.updateHistory(myMove, opponentMove);
    }

    @Override
    public void saveData() {
        algorithm.saveData();
    }
}
