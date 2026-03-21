public class RandomComputerPlayer implements Player {
    @Override
    public Move getMove() {
        // TODO: Implement random move logic here (from Computer Player)
        return Move.PAPER; // Stub
    }
    
    @Override 
    public String getName() { 
        return "Computer (Random)"; 
    }
}