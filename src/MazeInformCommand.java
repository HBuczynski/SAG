public class MazeInformCommand extends Command {
    private MazeField[][] maze;
    private int rectDim;

    public MazeInformCommand() {
        super(CommandCode.MAZE_INFORM);
    }

    public MazeField[][] getMazeValues() {
        return this.maze;
    }

    public int getRectDim(){ return this.rectDim; }

    public void setMazeValues(MazeField[][] maze, int rectDim) {
        this.maze = maze;
        this.rectDim = rectDim;
    }
}
