import java.util.Random;

public class RandomComputerPlayer implements Player {
    private final Random random = new Random();
    
    @Override
    public Move getMove() {
        // Logic for replacing ComputerPlayer
        int val = random.nextInt(3);
        switch (val) {
            case 0: return Move.ROCK;
            case 1: return Move.PAPER;
            default: return Move.SCISSORS;
    }
    
    @Override 
    public String getName() { 
        return "Computer (Random)"; 
    }
}
