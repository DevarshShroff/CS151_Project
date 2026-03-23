import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner scanner;
    private final String name;

    public HumanPlayer(String name, Scanner scanner) {
        this.scanner = scanner;
        this.name = name;
    }

    @Override
    public Move getMove() {
        while (true) {
            System.out.print("Enter your choice (1=rock, 2=paper, 3=scissors): ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1: return Move.ROCK;
                    case 2: return Move.PAPER;
                    case 3: return Move.SCISSORS;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // discard bad token
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}