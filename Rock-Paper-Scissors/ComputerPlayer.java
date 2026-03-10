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
