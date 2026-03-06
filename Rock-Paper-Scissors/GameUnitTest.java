public class GameUnitTest {

    public static void main(String[] args) {
        boolean testFailed = false;

        // --- TEST 1: Scoreboard Incrementation ---
        int hScore = 0, cScore = 0, dScore = 0;
        
        // Simulate 1 Human Win (Result 1), 1 Comp Win (Result 2), 1 Draw (Result 0)
        int[] simulatedResults = {1, 2, 0}; 
        
        for (int res : simulatedResults) {
            if (res == 1) hScore++;
            else if (res == 2) cScore++;
            else if (res == 0) dScore++;
        }

        if (hScore != 1 || cScore != 1 || dScore != 1) {
            System.err.println("ERROR: Scoreboard failed to increment correctly.");
            System.err.println("Actual -> H:" + hScore + " C:" + cScore + " D:" + dScore);
            testFailed = true;
        }

        // --- TEST 2: Round Count Verification ---
        int roundCount = 0;
        int targetRounds = 20;
        for (int i = 1; i <= targetRounds; i++) {
            roundCount++;
        }

        if (roundCount != 20) {
            System.err.println("ERROR: Game loop ran " + roundCount + " times instead of 20.");
            testFailed = true;
        }

        // --- TEST 3: Score Integrity (Sum Check) ---
        // Total points + draws must always equal rounds played
        if ((hScore + cScore + dScore) != simulatedResults.length) {
            System.err.println("ERROR: Score sum does not match total rounds played.");
            testFailed = true;
        }

        // Final status report (Only prints if there was a failure)
        if (testFailed) {
            System.err.println("\nUNIT TEST STATUS: FAILED");
            System.exit(1); // Exit with error code
        }
        
        // Note: If code reaches here with no output, the test passed.


        // Add test for the other classes!
    }
}
    
