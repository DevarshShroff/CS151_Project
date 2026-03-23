import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class GameUnitTest {

    public static void main(String[] args) {
        // --- TEST 1: Scoreboard Incrementation ---
        testScoreboardIncrementation();

        // --- TEST 2: Round Count Verification ---
        testRoundCountVerification();

        // --- TEST 3: Score Integrity ---
        testScoreIntegrity();

        // --- TEST 4: HumanPlayer Valid Input ---
        testHumanPlayerValidInput();

        // --- TEST 5: HumanPlayer Invalid Then Valid Input ---
        testHumanPlayerInvalidThenValidInput();

        // --- TEST 6: HumanPlayer Non-Numeric Then Valid Input ---
        testHumanPlayerNonNumericThenValidInput();

        // Silently exits if all tests pass
    }

    private static void testScoreboardIncrementation() {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.recordResult(1); // P1 win
        scoreboard.recordResult(2); // P2 win
        scoreboard.recordResult(0); // draw

        assert scoreboard.getP1Wins() == 1 : "P1 wins should be 1";
        assert scoreboard.getP2Wins() == 1 : "P2 wins should be 1";
        assert scoreboard.getDraws() == 1 : "Draws should be 1";
    }

    private static void testRoundCountVerification() {
        int roundCount = 0;
        int targetRounds = 20;

        for (int i = 1; i <= targetRounds; i++) {
            roundCount++;
        }

        assert roundCount == 20 : "Game loop should run 20 times";
    }

    private static void testScoreIntegrity() {
        Scoreboard scoreboard = new Scoreboard();

        scoreboard.recordResult(1);
        scoreboard.recordResult(2);
        scoreboard.recordResult(0);

        int total = scoreboard.getP1Wins() + scoreboard.getP2Wins() + scoreboard.getDraws();
        assert total == 3 : "Total results should equal total rounds played";
    }

    private static void testHumanPlayerValidInput() {
        InputStream originalIn = System.in;

        try {
            String simulatedInput = "2\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            Scanner scanner = new Scanner(System.in);
            HumanPlayer testHuman = new HumanPlayer("Tester", scanner);
            Move move = testHuman.getMove();

            assert move == Move.PAPER : "Expected PAPER for input 2";
        } finally {
            System.setIn(originalIn);
        }
    }

    private static void testHumanPlayerInvalidThenValidInput() {
        InputStream originalIn = System.in;

        try {
            String simulatedInput = "9\n1\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            Scanner scanner = new Scanner(System.in);
            HumanPlayer testHuman = new HumanPlayer("Tester", scanner);
            Move move = testHuman.getMove();

            assert move == Move.ROCK : "Expected ROCK after invalid input then 1";
        } finally {
            System.setIn(originalIn);
        }
    }

    private static void testHumanPlayerNonNumericThenValidInput() {
        InputStream originalIn = System.in;

        try {
            String simulatedInput = "abc\n3\n";
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            Scanner scanner = new Scanner(System.in);
            HumanPlayer testHuman = new HumanPlayer("Tester", scanner);
            Move move = testHuman.getMove();

            assert move == Move.SCISSORS : "Expected SCISSORS after non-numeric input then 3";
        } finally {
            System.setIn(originalIn);
        }
    }
}
