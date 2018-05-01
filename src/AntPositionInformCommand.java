import java.awt.*;

public class AntPositionInformCommand extends Command
{
    private Point newPosition;
    private int distance;

    public AntPositionInformCommand()
    {
        super(CommandCode.ANT_POSITION_INFORM);

        this.newPosition.x = 9999999;
        this.newPosition.y = 9999999;

        this.distance = 0;
    }

    public Point getNewPosition()
    {

        return newPosition;
    }

    public void setNewPosition(Point point)
    {

        this.newPosition = point;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int dist)
    {
        this.distance = dist;
    }
}

