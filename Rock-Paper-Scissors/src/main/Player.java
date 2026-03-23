public interface Player {

    /**
     * Retrieves the next move from the player.
     * @return A valid Move enum (ROCK, PAPER, or SCISSORS)
     */
    Move getMove();

    /**
     * Retrieves the display name of the player.
     * @return The player's name
     */
    String getName();

    /**
     * Receives both moves after a round is completed.
     * @param myMove The move this player just played
     * @param opponentMove The move the opponent just played
     */
    default void updateHistory(Move myMove, Move opponentMove) {
        // Most players do not track history.
    }

    /**
     * Persists any learned data at the end of a game.
     * Only meaningful for ML-based players; others do nothing.
     */
    default void saveData() {
        // Random and human players have nothing to save.
    }
}
