import java.awt.*;

public class AntPositionInformCommand extends Command
{
    private Point newPosition;
    private Point oldPosition;
    private int distance;

    public AntPositionInformCommand()
    {
        super(CommandCode.ANT_POSITION_INFORM);
        newPosition = new Point();
        oldPosition = new Point();
        this.newPosition.x = 9999999;
        this.newPosition.y = 9999999;
        this.oldPosition.x = 9999999;
        this.oldPosition.y = 9999999;

        this.distance = 1;
    }

    public Point getNewPosition()
    {
        return newPosition;
    }

    public void setNewPosition(Point point)
    {

        this.newPosition = point;
    }

    public Point getOldPosition()
    {
        return oldPosition;
    }

    public void setOldPosition(Point point)
    {

        this.oldPosition = point;
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

