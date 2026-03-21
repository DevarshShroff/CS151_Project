public class MLComputerPlayer implements Player {

    @Override
    public Move getMove() {
        // TODO: Implement the Machine Learning algorithm (e.g., predicting based on history)
        return Move.SCISSORS; // Stub
    }
    
    @Override 
    public String getName() { 
        return "CPU (Machine Learning)"; 

    // Also Implement updateHistory()
    }
}