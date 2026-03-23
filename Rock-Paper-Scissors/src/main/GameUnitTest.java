public class GameUnitTest {

    public static void main(String[] args) {
        boolean testFailed = false;

        // ================================================================
        // SCOREBOARD TESTS (testing the actual Scoreboard class)
        // ================================================================

        // --- TEST 1: Scoreboard records a P1 win correctly ---
        {
            Scoreboard sb = new Scoreboard();
            sb.recordResult(1);
            if (sb.getP1Wins() != 1 || sb.getP2Wins() != 0 || sb.getDraws() != 0) {
                System.err.println("TEST 1 FAILED: P1 win not recorded. State: "
                    + sb.getP1Wins() + "/" + sb.getP2Wins() + "/" + sb.getDraws());
                testFailed = true;
            }
        }

        // --- TEST 2: Scoreboard records a P2 win correctly ---
        {
            Scoreboard sb = new Scoreboard();
            sb.recordResult(2);
            if (sb.getP1Wins() != 0 || sb.getP2Wins() != 1 || sb.getDraws() != 0) {
                System.err.println("TEST 2 FAILED: P2 win not recorded. State: "
                    + sb.getP1Wins() + "/" + sb.getP2Wins() + "/" + sb.getDraws());
                testFailed = true;
            }
        }

        // --- TEST 3: Scoreboard records a draw correctly ---
        {
            Scoreboard sb = new Scoreboard();
            sb.recordResult(0);
            if (sb.getP1Wins() != 0 || sb.getP2Wins() != 0 || sb.getDraws() != 1) {
                System.err.println("TEST 3 FAILED: Draw not recorded. State: "
                    + sb.getP1Wins() + "/" + sb.getP2Wins() + "/" + sb.getDraws());
                testFailed = true;
            }
        }

        // --- TEST 4: Scoreboard sum integrity over multiple results ---
        {
            Scoreboard sb = new Scoreboard();
            sb.recordResult(1);
            sb.recordResult(2);
            sb.recordResult(0);
            sb.recordResult(1);
            int total = sb.getP1Wins() + sb.getP2Wins() + sb.getDraws();
            if (total != 4 || sb.getP1Wins() != 2 || sb.getP2Wins() != 1 || sb.getDraws() != 1) {
                System.err.println("TEST 4 FAILED: Score integrity check failed. P1="
                    + sb.getP1Wins() + " P2=" + sb.getP2Wins() + " D=" + sb.getDraws());
                testFailed = true;
            }
        }

        // ================================================================
        // CLASSICRULES (GameLogic) TESTS — all 9 move combinations
        // ================================================================
        ClassicRules rules = new ClassicRules();

        // --- TEST 5: Rock vs Rock = Draw ---
        {
            int result = rules.decideWinner(Move.ROCK, Move.ROCK);
            if (result != 0) {
                System.err.println("TEST 5 FAILED: Rock vs Rock should be Draw (0), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 6: Paper vs Paper = Draw ---
        {
            int result = rules.decideWinner(Move.PAPER, Move.PAPER);
            if (result != 0) {
                System.err.println("TEST 6 FAILED: Paper vs Paper should be Draw (0), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 7: Scissors vs Scissors = Draw ---
        {
            int result = rules.decideWinner(Move.SCISSORS, Move.SCISSORS);
            if (result != 0) {
                System.err.println("TEST 7 FAILED: Scissors vs Scissors should be Draw (0), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 8: Rock vs Scissors = P1 Win ---
        {
            int result = rules.decideWinner(Move.ROCK, Move.SCISSORS);
            if (result != 1) {
                System.err.println("TEST 8 FAILED: Rock vs Scissors should be P1 Win (1), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 9: Paper vs Rock = P1 Win ---
        {
            int result = rules.decideWinner(Move.PAPER, Move.ROCK);
            if (result != 1) {
                System.err.println("TEST 9 FAILED: Paper vs Rock should be P1 Win (1), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 10: Scissors vs Paper = P1 Win ---
        {
            int result = rules.decideWinner(Move.SCISSORS, Move.PAPER);
            if (result != 1) {
                System.err.println("TEST 10 FAILED: Scissors vs Paper should be P1 Win (1), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 11: Scissors vs Rock = P2 Win ---
        {
            int result = rules.decideWinner(Move.SCISSORS, Move.ROCK);
            if (result != 2) {
                System.err.println("TEST 11 FAILED: Scissors vs Rock should be P2 Win (2), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 12: Rock vs Paper = P2 Win ---
        {
            int result = rules.decideWinner(Move.ROCK, Move.PAPER);
            if (result != 2) {
                System.err.println("TEST 12 FAILED: Rock vs Paper should be P2 Win (2), got " + result);
                testFailed = true;
            }
        }

        // --- TEST 13: Paper vs Scissors = P2 Win ---
        {
            int result = rules.decideWinner(Move.PAPER, Move.SCISSORS);
            if (result != 2) {
                System.err.println("TEST 13 FAILED: Paper vs Scissors should be P2 Win (2), got " + result);
                testFailed = true;
            }
        }

        // ================================================================
        // RANDOMCOMPUTERPLAYER TESTS
        // ================================================================

        // --- TEST 14: RandomComputerPlayer always returns a valid Move ---
        {
            RandomComputerPlayer cpu = new RandomComputerPlayer();
            boolean allValid = true;
            for (int i = 0; i < 100; i++) {
                Move m = cpu.getMove();
                if (m != Move.ROCK && m != Move.PAPER && m != Move.SCISSORS) {
                    allValid = false;
                    break;
                }
            }
            if (!allValid) {
                System.err.println("TEST 14 FAILED: RandomComputerPlayer returned an invalid Move.");
                testFailed = true;
            }
        }

        // ================================================================
        // HUMANPLAYER TESTS (using ByteArrayInputStream to simulate input)
        // ================================================================

        // --- TEST 15: HumanPlayer valid input "2" returns PAPER ---
        {
            java.io.InputStream originalIn = System.in;
            try {
                System.setIn(new java.io.ByteArrayInputStream("2\n".getBytes()));
                HumanPlayer human = new HumanPlayer("Tester", new java.util.Scanner(System.in));
                Move move = human.getMove();
                if (move != Move.PAPER) {
                    System.err.println("TEST 15 FAILED: Expected PAPER, got " + move);
                    testFailed = true;
                }
            } finally {
                System.setIn(originalIn);
            }
        }

        // --- TEST 16: HumanPlayer out-of-range then valid "9\n1\n" returns ROCK ---
        {
            java.io.InputStream originalIn = System.in;
            try {
                System.setIn(new java.io.ByteArrayInputStream("9\n1\n".getBytes()));
                HumanPlayer human = new HumanPlayer("Tester", new java.util.Scanner(System.in));
                Move move = human.getMove();
                if (move != Move.ROCK) {
                    System.err.println("TEST 16 FAILED: Expected ROCK after re-prompt, got " + move);
                    testFailed = true;
                }
            } finally {
                System.setIn(originalIn);
            }
        }

        // --- TEST 17: HumanPlayer non-numeric then valid "abc\n3\n" returns SCISSORS ---
        {
            java.io.InputStream originalIn = System.in;
            try {
                System.setIn(new java.io.ByteArrayInputStream("abc\n3\n".getBytes()));
                HumanPlayer human = new HumanPlayer("Tester", new java.util.Scanner(System.in));
                Move move = human.getMove();
                if (move != Move.SCISSORS) {
                    System.err.println("TEST 17 FAILED: Expected SCISSORS after bad input, got " + move);
                    testFailed = true;
                }
            } finally {
                System.setIn(originalIn);
            }
        }

        // --- TEST 18: MLComputerPlayer returns valid move with no history ---
        {
            MLComputerPlayer ml = new MLComputerPlayer();
            Move m = ml.getMove();
            if (m != Move.ROCK && m != Move.PAPER && m != Move.SCISSORS) {
                System.err.println("TEST 18 FAILED: MLComputerPlayer returned invalid move with no history.");
                testFailed = true;
            }
        }

        // --- TEST 19: MLComputerPlayer returns valid move with insufficient history (< N-1) ---
        {
            MLComputerPlayer ml = new MLComputerPlayer();
            ml.updateHistory(Move.ROCK);
            ml.updateHistory(Move.ROCK);
            ml.updateHistory(Move.ROCK);
            // Only 3 moves fed; need N-1=4 before prediction kicks in
            Move m = ml.getMove();
            if (m != Move.ROCK && m != Move.PAPER && m != Move.SCISSORS) {
                System.err.println("TEST 19 FAILED: MLComputerPlayer returned invalid move with insufficient history.");
                testFailed = true;
            }
        }

        // --- TEST 20: MLComputerPlayer learns always-ROCK pattern and counters with PAPER ---
        {
            MLComputerPlayer ml = new MLComputerPlayer();
            // Feed N full windows of ROCK so the frequency table is populated.
            // updateHistory() needs N moves to record a sequence, and getMove()
            // needs N-1 in the deque. We feed enough to build strong signal.
            for (int i = 0; i < 20; i++) {
                ml.updateHistory(Move.ROCK);
            }
            // Now the deque has the last N=5 moves (all ROCK), and the prefix
            // ROCK,ROCK,ROCK,ROCK → ROCK is heavily recorded.
            Move counter = ml.getMove();
            if (counter != Move.PAPER) {
                System.err.println("TEST 20 FAILED: Expected PAPER to counter always-ROCK pattern, got " + counter);
                testFailed = true;
            }
        }

        // --- TEST 21: MLComputerPlayer learns always-SCISSORS pattern and counters with ROCK ---
        {
            MLComputerPlayer ml = new MLComputerPlayer();
            for (int i = 0; i < 20; i++) {
            ml.updateHistory(Move.SCISSORS);
            }
            Move counter = ml.getMove();
            if (counter != Move.ROCK) {
                System.err.println("TEST 21 FAILED: Expected ROCK to counter always-SCISSORS pattern, got " + counter);
                testFailed = true;
            }
        }

        // --- TEST 22: MLComputerPlayer learns alternating ROCK/PAPER pattern ---
        {
            MLComputerPlayer ml = new MLComputerPlayer();
            Move[] pattern = {Move.ROCK, Move.PAPER, Move.ROCK, Move.PAPER, Move.ROCK,
                            Move.PAPER, Move.ROCK, Move.PAPER, Move.ROCK, Move.PAPER,
                            Move.ROCK, Move.PAPER, Move.ROCK, Move.PAPER, Move.ROCK,
                            Move.PAPER, Move.ROCK, Move.PAPER, Move.ROCK};
            // Feed all but the last move — the deque ends with ROCK,PAPER,ROCK,PAPER
            for (Move m : pattern) {
                ml.updateHistory(m);
            }
            // The prefix ROCK,PAPER,ROCK,PAPER strongly predicts PAPER next,
            // so the ML engine should return SCISSORS to beat it.
            Move counter = ml.getMove();
            if (counter != Move.SCISSORS) {
                System.err.println("TEST 22 FAILED: Expected SCISSORS to counter alternating pattern, got " + counter);
                testFailed = true;
            }
        }

        // ================================================================
        // FINAL REPORT
        // ================================================================
        if (testFailed) {
            System.err.println("\nUNIT TEST STATUS: FAILED");
            System.exit(1);
        }
        // Reaching here with no output means all 17 tests passed.
    }
}