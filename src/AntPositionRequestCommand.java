import java.awt.*;

public class AntPositionRequestCommand extends Commands
{
    private Point newPosition;
    private Point pheromonePosition;

    public AntPositionRequestCommand()
    {
        super(CommandCode.ANT_POSITION);
    }

    public Point getNewPosition()
    {
        return newPosition;
    }

    public void setNewPosition(Point point)
    {
        this.newPosition = point;
    }

    public Point getPheromonePosition()
    {
        return pheromonePosition;
    }

    public void setPheromonePosition(Point point)
    {
        this.pheromonePosition = point;
    }
}

