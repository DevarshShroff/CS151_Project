public class ComputerPlayerFactory {
    public static Player createComputerPlayer(Algorithm algo) {
        switch (algo) {
            case MACHINE_LEARNING:
                return new ComputerPlayer("CPU (Machine Learning)", new MLComputerPlayer());
            case RANDOM:
            default:
                return new ComputerPlayer("Computer (Random)", new RandomComputerPlayer());
        }
    }
}
