public class ComputerPlayerFactory {
    
    public static Player createComputerPlayer(Algorithm algo) {
        switch (algo) {
            case MACHINE_LEARNING:
                return new MLComputerPlayer();
                //Implement MLComputerPlayer (MLComputerPlayer.java)
            case RANDOM:
            default:
                return new RandomComputerPlayer();
                //Implement RandomComputerPlayer (RandomComputerPlayer.java)

        }
    }
}

