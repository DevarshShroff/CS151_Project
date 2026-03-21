public class Scoreboard {
    private int p1Wins;
    private int p2Wins;
    private int draws;

    public Scoreboard() {
        this.p1Wins = 0;
        this.p2Wins = 0;
        this.draws = 0;
    }

    /**
     * Updates the score based on the round's result.
     * @param result 1 if Player 1 wins, 2 if Player 2 wins, 0 for a draw.
     */
    public void recordResult(int result) {
        if (result == 1) {
            p1Wins++;
        } else if (result == 2) {
            p2Wins++;
        } else if (result == 0) {
            draws++;
        } else {
            // Optional: Handle unexpected results safely
            System.err.println("Warning: Invalid result code received.");
        }
    }

    // Getters are included here so your team can easily write Unit Tests 
    public int getP1Wins() {
        return p1Wins;
    }

    public int getP2Wins() {
        return p2Wins;
    }

    public int getDraws() {
        return draws;
    }

    /**
     * Formats the current score for display in the View/Controller.
     */
    public String formatScore() {
        return String.format("P1: %d | P2: %d | Draws: %d", p1Wins, p2Wins, draws);
    }
}