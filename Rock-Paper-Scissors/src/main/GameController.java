public class GameController {
    private final Player p1;
    private final Player p2;
    private final IRules rules;
    private final Scoreboard scoreboard;

    // Dependencies are injected here!
    public GameController(Player p1, Player p2, IRules rules) {
        this.p1 = p1;
        this.p2 = p2;
        this.rules = rules;
        this.scoreboard = new Scoreboard();
    }

    public void playGame(int totalRounds) {
        for (int i = 1; i <= totalRounds; i++) {
            System.out.println("Round " + i + " --------------------");
            
            // Get Moves (The HumanPlayer class itself should handle the "1=rock..." and get input (as ENUM Moves))
            Move p1Move = p1.getMove();
            Move p2Move = p2.getMove();

            // Decide Winner
            int result = rules.decideWinner(p1Move, p2Move);

            // Update Scoreboard
            scoreboard.recordResult(result);

            // Print Choices
            System.out.print(p1.getName() + " chose " + p1Move + ". ");
            System.out.print(p2.getName() + " chose " + p2Move + ". \n");

            // Print Round Result (Assuming 0=Draw, 1=P1 Win, 2=P2 Win)
            if (result == 0) {
                System.out.println("Draw!");
            } else if (result == 1) { 
                System.out.println(p1.getName() + " Wins!");
            } else {
                System.out.println(p2.getName() + " Wins!");
            }

            // Feed history to players (for Machine Learning engine)
            p1.updateHistory(p2Move);
            p2.updateHistory(p1Move);

            // Print Current Score
            System.out.println("Current " + scoreboard.formatScore() + "\n");
        }

        // End of Game
        System.out.println("------- Game Over --------");
        System.out.println("Final Standings - " + scoreboard.formatScore());

        p2.saveData();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
