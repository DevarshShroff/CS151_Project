public class GameLogic {
    // Make a IRules Interface for this Class and Make 
    // this as ClasicRules() implementing the IRules 

    public int determineWinner(int human, int computer) {
        // For testing purposes: 
        // We know Paper (2) vs Scissors (3) results in a Computer Win (2)
        if(human == computer){
            return 0;
        }

        if((human == 1 && computer == 3) ||
           (human == 2 && computer == 1) ||
           (human == 3 && computer == 2)){
            return 1;
           }

        return 2;
    }

    public String getChoiceName(int choice) {
        if (choice == 2) return "Paper";
        if (choice == 3) return "Scissors";
        return "Rock";
    }

}
