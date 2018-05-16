import java.awt.*;

public class AntPositionInformCommand extends Command
{

    public static final int ILLEGAL_POSITION = Integer.MAX_VALUE;

    private Point newPosition;
    private Point oldPosition;
    private int distance;

    public AntPositionInformCommand()
    {
        super(CommandCode.ANT_POSITION_INFORM);
        newPosition = new Point();
        oldPosition = new Point();
        this.newPosition.x = ILLEGAL_POSITION;
        this.newPosition.y = ILLEGAL_POSITION;
        this.oldPosition.x = ILLEGAL_POSITION;
        this.oldPosition.y = ILLEGAL_POSITION;

        this.distance = 1;
    }

    public AntPositionInformCommand(int posX, int posY)
    {
        super(CommandCode.ANT_DISABLED_INFORM);
        oldPosition = new Point();
        this.oldPosition.x = posX;
        this.oldPosition.y = posY;
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

