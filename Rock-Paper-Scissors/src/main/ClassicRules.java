public class ClassicRules implements IRules {

    @Override
    public int decideWinner(Move p1, Move p2) {
        if (p1 == p2) {
            return 0; // tie
        }

        switch (p1) {
            case ROCK:
                return (p2 == Move.SCISSORS) ? 1 : 2;
            case PAPER:
                return (p2 == Move.ROCK) ? 1 : 2;
            case SCISSORS:
                return (p2 == Move.PAPER) ? 1 : 2;
            default:
                return 0;
        }
    }
}
