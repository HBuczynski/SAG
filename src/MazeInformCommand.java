public class MazeInformCommand extends Command {
    private MazeField[][] maze;

    public MazeInformCommand() {
        super(CommandCode.MAZE_INFORM);
    }

    public MazeField[][] getMazeValues() {
        return this.maze;
    }

    public void setMazeValues(MazeField[][] maze) {
        this.maze = maze;
    }
}
