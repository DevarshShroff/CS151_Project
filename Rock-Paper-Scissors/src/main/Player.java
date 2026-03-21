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
     * Receives the opponent's move after a round is completed.
     * * @param opponentMove The move the opponent just played
     */
    default void updateHistory(Move opponentMove) { 
        // MLComputerPlayer will override this to store the move and learn.
    }
}