
public class ScoreboardTest {
    public static void main(String[] args) {
        testRecordPlayer1Win();
        testRecordPlayer2Win();
        testRecordDraw();
        testFormatScore();
        // Silently exits if all tests pass
    }

    private static void testRecordPlayer1Win() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.recordResult(1);
        assert scoreboard.getP1Wins() == 1 : "Player 1 wins should be 1";
        assert scoreboard.getP2Wins() == 0 : "Player 2 wins should be 0";
        assert scoreboard.getDraws() == 0 : "Draws should be 0";
    }

    private static void testRecordPlayer2Win() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.recordResult(2);
        assert scoreboard.getP1Wins() == 0 : "Player 1 wins should be 0";
        assert scoreboard.getP2Wins() == 1 : "Player 2 wins should be 1";
        assert scoreboard.getDraws() == 0 : "Draws should be 0";
    }

    private static void testRecordDraw() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.recordResult(0);
        assert scoreboard.getP1Wins() == 0 : "Player 1 wins should be 0";
        assert scoreboard.getP2Wins() == 0 : "Player 2 wins should be 0";
        assert scoreboard.getDraws() == 1 : "Draws should be 1";
    }

    private static void testFormatScore() {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.recordResult(1);
        scoreboard.recordResult(1);
        scoreboard.recordResult(2);
        scoreboard.recordResult(0);
        
        String expected = "P1: 2 | P2: 1 | Draws: 1";
        assert expected.equals(scoreboard.formatScore()) : "Score format did not match expected output";
    }
}