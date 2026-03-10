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
        
        // --- TEST 4: HumanPlayer Valid Input ---
        // Simulate user typing "2" (Paper) into System.in
        java.io.InputStream originalIn = System.in;
        try {
            String simulatedInput = "2\n";
            System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

            HumanPlayer testHuman = new HumanPlayer();
            int move = testHuman.getMove(1);

            if (move != 2) {
                System.err.println("ERROR: HumanPlayer returned " + move + " but expected 2 (Paper).");
                testFailed = true;
            }
        } finally {
            System.setIn(originalIn); // Always restore System.in
        }

        // --- TEST 5: HumanPlayer Invalid Then Valid Input ---
        // Simulate user typing "9" (invalid), then "1" (Rock)
        originalIn = System.in;
        try {
            String simulatedInput = "9\n1\n";
            System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

            HumanPlayer testHuman = new HumanPlayer();
            int move = testHuman.getMove(1);

            if (move != 1) {
                System.err.println("ERROR: HumanPlayer should have re-prompted and returned 1 (Rock), got " + move);
                testFailed = true;
            }
        } finally {
            System.setIn(originalIn);
        }

        // --- TEST 6: HumanPlayer Non-Numeric Then Valid Input ---
        // Simulate user typing "abc" (bad), then "3" (Scissors)
        originalIn = System.in;
        try {
            String simulatedInput = "abc\n3\n";
            System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

            HumanPlayer testHuman = new HumanPlayer();
            int move = testHuman.getMove(1);

            if (move != 3) {
                System.err.println("ERROR: HumanPlayer should have handled bad input and returned 3 (Scissors), got " + move);
                testFailed = true;
            }
        } finally {
            System.setIn(originalIn);
        }

         // Final status report
        if (testFailed) {
            System.err.println("\nUNIT TEST STATUS: FAILED");
            System.exit(1);
        }

        // Note: If code reaches here with no output, the test passed.

        // Add test for the other classes!
    }
}
    
