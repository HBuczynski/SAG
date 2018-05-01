import java.awt.*;

public class AntNeighbourhoodRequestCommand extends Command {
    private Point currentPosition;

    public AntNeighbourhoodRequestCommand() {

        super(CommandCode.ANT_NEIGHBORHOOD_REQUEST);
        currentPosition = new Point();
        this.currentPosition.x = 9999999;
        this.currentPosition.y = 9999999;
    }

    public Point getCurrentPosition()
    {

        return currentPosition;
    }

    public void setCurrentPosition(Point point)
    {

        this.currentPosition = point;
    }

}
