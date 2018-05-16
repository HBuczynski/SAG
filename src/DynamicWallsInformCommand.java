public class DynamicWallsInformCommand extends Command {
    private int wallsCount;

    public DynamicWallsInformCommand(int number){
        super(CommandCode.DYNAMIC_WALLS_NUMBER_INFORM);
        this.wallsCount = number;
    }

    public int getWallsCount()
    {
        return this.wallsCount;
    }

    public void setWallsCount(int number)
    {
        this.wallsCount = number;
    }
}
