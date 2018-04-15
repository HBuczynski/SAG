public class AlleyRequestCommand extends Commands {
    private PossibleValues[][] maze;

    public AlleyRequestCommand() {
        super(CommandCode.ALLEY_REQUEST);
    }

    public PossibleValues[][] getMazeValues() {
        return this.maze;
    }

    public void setMazeValues(PossibleValues[][] maze) {
        this.maze = maze;
    }
}
