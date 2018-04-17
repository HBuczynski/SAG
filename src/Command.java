import java.io.Serializable;

public class Command implements Serializable
{
    enum CommandCode
    {
        MAZE_REQUEST {
            @Override
            public String toString() {
                return "MAZE_REQUEST";
            }
        },

        MAZE_INFORM {
            @Override
            public String toString() {
                return "MAZE_INFORM";
            }
        },

        ANT_POSITION_INFORM {
            @Override
            public String toString() {
                return "ANT_POSITION_INFORM";
            }
        },

        ANT_NEIGHBORHOOD_REQUEST{
            @Override
            public String toString() {
                return "ANT_NEIGHBORHOOD";
            }
        },

        ANT_NEIGHBORHOOD_INFORM{
            @Override
            public String toString() {
                return "ANT_NEIGHBORHOOD_INFORM";
            }
        },
    }

    private CommandCode code;

    public Command(CommandCode code)
    {
        this.code = code;
    }

    public CommandCode getCommandCode()
    {
        return code;
    }
}
