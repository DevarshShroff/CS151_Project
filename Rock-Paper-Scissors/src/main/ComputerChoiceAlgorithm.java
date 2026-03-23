public interface ComputerChoiceAlgorithm {
    Move getMove();

    default void updateHistory(Move myMove, Move opponentMove) {
        // Random algorithms do not need history.
    }

    default void saveData() {
        // Most algorithms have nothing to persist.
    }
}
