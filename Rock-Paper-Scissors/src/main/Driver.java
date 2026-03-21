import java.util.Scanner;


public class Driver {
    public static void main(String[] args) {
        final int NumOFRounds =20;
        // GameUnitTest.main(null);  -> To run unit test 
        Scanner scanner = new Scanner(System.in);

        // 1. Setup Human Player
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        Player human = new HumanPlayer(playerName, scanner);
        
        // 2. Setup Computer Player via Factory
        System.out.println("Choose your opponent:");
        System.out.println("1. Random Engine");
        System.out.println("2. Machine Learning Engine");
        System.out.print("Choice: ");

        int choice = scanner.nextInt();
        Algorithm selectedAlgo = (choice == 2) ? Algorithm.MACHINE_LEARNING : Algorithm.RANDOM;

        Player computer = ComputerPlayerFactory.createComputerPlayer(selectedAlgo);

        IRules rules = new ClassicRules();
        GameController game = new GameController(human, computer, rules);

        System.out.println("\nStarting game against " + computer.getName() + "...");
        game.playGame(NumOFRounds);
        
        scanner.close();
    }
}
