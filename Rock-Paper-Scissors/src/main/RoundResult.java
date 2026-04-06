// holds everything that happened in one round so the view can update its labels
public class RoundResult {

    public final int round;
    public final Move humanMove;
    public final Move computerMove;
    public final Move prediction;  // null if the ML model didn't have enough data yet
    public final int result;       // 0 = tie, 1 = human wins, 2 = computer wins
    public final int humanWins;
    public final int computerWins;
    public final int draws;

    public RoundResult(int round, Move humanMove, Move computerMove,
                       Move prediction, int result,
                       int humanWins, int computerWins, int draws) {
        this.round        = round;
        this.humanMove    = humanMove;
        this.computerMove = computerMove;
        this.prediction   = prediction;
        this.result       = result;
        this.humanWins    = humanWins;
        this.computerWins = computerWins;
        this.draws        = draws;
    }

    public String getWinnerText() {
        if (result == 0) return "Tie";
        if (result == 1) return "Human";
        return "Computer";
    }
}