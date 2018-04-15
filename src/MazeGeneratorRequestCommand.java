public class MazeGeneratorRequestCommand extends Commands
{
    private PossibleValues[][] maze;

    public MazeGeneratorRequestCommand()
    {
        super(CommandCode.MAZE_GENERATOR_REQUEST);
    }

    public PossibleValues[][] getMazeValues()
    {
        return this.maze;
    }

    public void setMazeValues(PossibleValues[][] maze)
    {
        this.maze = maze;
    }
}
