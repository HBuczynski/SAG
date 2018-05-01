
public class AntNeighbourhoodInformCommand extends Command {
    private MazeField[] maze;

    public AntNeighbourhoodInformCommand() {
        super(CommandCode.ANT_NEIGHBORHOOD_INFORM);
    }

    public MazeField[] getMazeValues() {
        return this.maze;
    }

    public void setMazeValues(MazeField[][] maze) {
        this.maze = maze;
    }
}