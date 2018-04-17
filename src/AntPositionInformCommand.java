import java.awt.*;

public class AntPositionInformCommand extends Command
{
    private Point newPosition;
    private boolean pheromone;

    public AntPositionInformCommand()
    {
        super(CommandCode.ANT_POSITION_INFORM);

        this.newPosition.x = 9999999;
        this.newPosition.y = 9999999;

        pheromone = false;
    }

    public Point getNewPosition()
    {

        return newPosition;
    }

    public void setNewPosition(Point point)
    {

        this.newPosition = point;
    }

    public boolean pheromoneExist()
    {

        return pheromone;
    }

    public void leavePheromone(boolean state)
    {

        this.pheromone = state;
    }
}

