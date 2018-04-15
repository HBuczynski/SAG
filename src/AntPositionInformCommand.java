import java.awt.*;

public class AntPositionInformCommand extends Commands
{
    private Point newPosition;
    private boolean pheromone;

    public AntPositionInformCommand()
    {
        super(CommandCode.ANT_POSITION);

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

    public boolean getPheromonePosition()
    {

        return pheromone;
    }

    public void setPheromonePosition(Boolean state)
    {

        this.pheromone = state;
    }
}

