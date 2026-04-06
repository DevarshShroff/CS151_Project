public interface ComputerChoiceAlgorithm {
    Move getMove();

    default void updateHistory(Move myMove, Move opponentMove) {
        // random algorithms don't use history
    }

    default void saveData() {
        // most algorithms have nothing to save
    }
    
    default Move getLastPrediction() {
        return null;
    }
}
