import java.util.Scanner;

public class HumanPlayer {
    private final Scanner scanner;

    public HumanPlayer() {
        this.scanner = new Scanner(System.in);
    }

    public int getMove(int round) {
        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.print("Enter your choice (1=rock, 2=paper, 3=scissors): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > 3) {
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // discard bad input
            }
        }
        return choice;
    }
}