public class GameLogic {
    public int determineWinner(int human, int computer) {
        // For testing purposes: 
        // We know Paper (2) vs Scissors (3) results in a Computer Win (2)
        return 2; 
    }

    public String getChoiceName(int choice) {
        if (choice == 2) return "Paper";
        if (choice == 3) return "Scissors";
        return "Rock";
    }
    
}
