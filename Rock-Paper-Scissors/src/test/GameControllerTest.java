public class GameControllerTest {

    static class StubPlayer implements Player {
        private final Move fixedMove;

        public StubPlayer(Move fixedMove) {
            this.fixedMove = fixedMove;
        }

        @Override
        public Move getMove() {
            return fixedMove;
        }

        @Override
        public String getName() {
            return "Stub";
        }
    }

    static class StubRules implements IRules {
        private final int fixedResult;

        public StubRules(int fixedResult) {
            this.fixedResult = fixedResult;
        }

        @Override
        public int decideWinner(Move m1, Move m2) {
            return fixedResult;
        }
    }

    public static void main(String[] args) {
        testGameControllerLoopsCorrectly();
        // Silently exits if all tests pass
    }

    private static void testGameControllerLoopsCorrectly() {
        Player p1 = new StubPlayer(Move.ROCK);
        Player p2 = new StubPlayer(Move.SCISSORS);
        IRules rules = new StubRules(1);

        GameController game = new GameController(p1, p2, rules);
        game.playGame(3);

        Scoreboard board = game.getScoreboard();
        assert board.getP1Wins() == 3 : "Game should have looped exactly 3 times, giving P1 3 wins.";
        assert board.getP2Wins() == 0 : "P2 wins should be 0";
        assert board.getDraws() == 0 : "Draws should be 0";
    }
}
