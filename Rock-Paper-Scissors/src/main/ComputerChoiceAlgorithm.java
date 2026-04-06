public interface ComputerChoiceAlgorithm {
    Move getMove();

    default void updateHistory(Move myMove, Move opponentMove) {
        // random algorithms don't use history
    }

    default void saveData() {
        // most algorithms have nothing to save
    }

    // returns what the ML model predicted the human would play
    // returns null for random since there's no prediction
    default Move getLastPrediction() {
        return null;
    }
}
