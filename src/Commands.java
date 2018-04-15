import java.io.Serializable;

public class Commands  implements Serializable
{
    enum CommandCode
    {
        MAZE_GENERATOR_REQUEST {
            @Override
            public String toString() {
                return "MAZE_GENERATOR_REQUEST";
            }
        },

        ALLEY_REQUEST{
            @Override
            public String toString() {
                return "ALLEY_REQUEST";
            }
        },

        ANT_POSITION{
            @Override
            public String toString() {
                return "ANT_POSITION";
            }
        },
    }

    private CommandCode code;

    public Commands(CommandCode code)
    {
        this.code = code;
    }

    public CommandCode getCommandCode()
    {
        return code;
    }
}
