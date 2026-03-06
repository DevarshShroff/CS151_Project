public class GameController {
    public static void main(String[] args) {
        GameUnitTest.main(null);

        // Initialize Components
        HumanPlayer human = new HumanPlayer();
        ComputerPlayer computer = new ComputerPlayer();
        GameLogic DecisionMaker = new GameLogic();

        // Scoreboard variables
        int humanWins = 0;
        int computerWins = 0;
        int draws = 0;
        final int totalRounds = 20;

        for (int i = 1; i <= totalRounds; i++) {
            System.out.println("Round " + i + " - Choose (1=rock, 2=paper, 3=scissors): " );
            int humanMove = human.getMove(i);
            int computerMove = computer.getMove();

            int result = DecisionMaker.determineWinner(humanMove, computerMove);

            // Print choices
            System.out.print("You chose " + DecisionMaker.getChoiceName(humanMove) + ". ");
            System.out.print("The computer chose " + DecisionMaker.getChoiceName(computerMove) + ". ");

            // Update Scoreboard and Print Result
            if (result == 0) {
                draws++;
                System.out.println("Draw!");
            } else if (result == 1) {
                humanWins++;
                System.out.println("Human Wins!");
            } else {
                computerWins++;
                System.out.println("Computer Wins!");
            }

            // Print current score
            System.out.printf("Score: Human:%d Computer:%d Draws=%d%n%n", 
                               humanWins, computerWins, draws);
        }

        System.out.println("------- Game Over --------");
        System.out.println("Final Score - You: " + humanWins + " | Computer: " + computerWins);
    }
}