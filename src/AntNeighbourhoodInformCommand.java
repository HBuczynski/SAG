import java.util.Vector;

public class AntNeighbourhoodInformCommand extends Command {
    private Vector<MazeField> maze;

    public AntNeighbourhoodInformCommand() {
        super(CommandCode.ANT_NEIGHBORHOOD_INFORM);
    }

    public Vector<MazeField> getMazeValues() {
        return this.maze;
    }

    public void setMazeValues(Vector<MazeField> maze) {
        this.maze = maze;
    }
}