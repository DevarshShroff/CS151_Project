// represents the human in the GUI — move comes from a button click, not getMove()
public class HumanGUIPlayer implements Player {

    private final String name;

    public HumanGUIPlayer(String name) {
        this.name = name;
    }

    @Override
    public Move getMove() {
        throw new UnsupportedOperationException("Use button clicks instead.");
    }

    @Override
    public String getName() {
        return name;
    }
}