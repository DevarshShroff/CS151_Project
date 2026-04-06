// plays the game one round at a time so JavaFX can drive the flow
public class GUIGameController {

    private final Player human;
    private final Player computer;
    private final IRules rules;
    private final Scoreboard scoreboard;
    private final int totalRounds;
    private int currentRound;

    public GUIGameController(Player human, Player computer, IRules rules, int totalRounds) {
        this.human      = human;
        this.computer   = computer;
        this.rules      = rules;
        this.scoreboard = new Scoreboard();
        this.totalRounds = totalRounds;
        this.currentRound = 1;
    }

    // plays one round and returns the result for the view to display
    public RoundResult playRound(Move humanMove) {
        Move computerMove = computer.getMove();

        // grab the ML prediction so we can show it in the GUI
        Move prediction = null;
        if (computer instanceof ComputerPlayer) {
            prediction = ((ComputerPlayer) computer).getLastPrediction();
        }

        int result = rules.decideWinner(humanMove, computerMove);
        scoreboard.recordResult(result);

        // let the ML model learn from this round
        human.updateHistory(humanMove, computerMove);
        computer.updateHistory(computerMove, humanMove);

        RoundResult roundResult = new RoundResult(
                currentRound, humanMove, computerMove, prediction, result,
                scoreboard.getP1Wins(), scoreboard.getP2Wins(), scoreboard.getDraws()
        );

        currentRound++;
        return roundResult;
    }

    public boolean isGameOver() {
        return currentRound > totalRounds;
    }

    public int getCurrentRound() { return currentRound; }
    public int getTotalRounds()  { return totalRounds;  }

    public void saveData() {
        computer.saveData();
    }
}