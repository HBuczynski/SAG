import java.awt.*;

public class AntPositionInformCommand extends Commands
{
    private Point newPosition;
    private Point pheromonePosition;

    public AntPositionInformCommand()
    {
        super(CommandCode.ANT_POSITION);

        this.newPosition.x = 9999999;
        this.newPosition.y = 9999999;

        this.pheromonePosition.x = 9999999;
        this.pheromonePosition.y = 9999999;
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

