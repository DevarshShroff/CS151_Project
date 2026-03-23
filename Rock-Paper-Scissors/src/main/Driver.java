import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        final int NUM_OF_ROUNDS = 20;

        // default algorithm is random unless user specifies otherwise
        Algorithm selectedAlgo = Algorithm.RANDOM;

        // check command-line argument to select algorithm
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-m")) {
                selectedAlgo = Algorithm.MACHINE_LEARNING;
            } else if (args[0].equalsIgnoreCase("-r")) {
                selectedAlgo = Algorithm.RANDOM;
            } else {
                System.out.println("Invalid option.");
                System.out.println("Usage: java Driver -r");
                System.out.println("   or: java Driver -m");
                return;
            }
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        // create players
        Player human = new HumanPlayer(playerName, scanner);
        Player computer = ComputerPlayerFactory.createComputerPlayer(selectedAlgo);

        // set rules and start game
        IRules rules = new ClassicRules();
        GameController game = new GameController(human, computer, rules);

        System.out.println("\nStarting game against " + computer.getName() + "...");
        game.playGame(NUM_OF_ROUNDS);

        scanner.close();
    }
}
