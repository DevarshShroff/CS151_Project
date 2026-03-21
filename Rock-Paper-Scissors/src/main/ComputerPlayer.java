import java.util.Random;

public class ComputerPlayer {
    private final Random random;

    public ComputerPlayer() {
        this.random = new Random();
    }
    
    public int getMove() {
        return random.nextInt(3) + 1;
    }
}
// This is to replaced by RandomComputerPlayer and MLComputerPlayer 
// implementing the Player Interface  


//Please Delete this file!