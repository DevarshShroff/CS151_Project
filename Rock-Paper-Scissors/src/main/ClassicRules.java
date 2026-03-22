public class ClassicRules implements IRules {
    // Formerly GameLogic, now ClassicRules class
    // Now implements IRules interface for game logic

    @Override
    public int determineWinner(Move p1, Move p2) {
        // For testing purposes: 
        // We know Paper (2) vs Scissors (3) results in a Computer Win (2)
        if(p1 == p2){
            return 0;
        }

        if((p1 == Move.ROCK && p2 == Move.SCISSORS) ||
           (p1 == Move.PAPER && p2 == Move.ROCK) ||
           (p1 == Move.SCISSORS && p2 == Move.PAPER)){
            return 1;
           }

        return 2;
    }

}
