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
                return "ANT_NEIGHBORHOOD_REQUEST";
            }
        },

        ANT_NEIGHBORHOOD_INFORM{
            @Override
            public String toString() {
                return "ANT_NEIGHBORHOOD_INFORM";
            }
        },

        ANT_DISABLED_INFORM{
            @Override
            public String toString() {
                return "ANT_DISABLED_INFORM";
            }
        },
        MAZE_CHANGED_UP_INFORM{
            @Override
            public String toString() {
                return "MAZE_CHANGED_UP_INFORM";
            }
        },
        MAZE_CHANGED_DOWN_INFORM{
        @Override
        public String toString() {
            return "MAZE_CHANGED_DOWN_INFORM";
        }
        },
        MAZE_CHANGED_ANT_EXIT{
            @Override
            public String toString() { return "MAZE_CHANGED_ANT_EXIT";}
        },
        DYNAMIC_WALLS_NUMBER_INFORM{
        @Override
        public String toString() {
            return "DYNAMIC_WALLS_NUMBER_INFORM";
            }
        },
        CURRENT_WALLS_INFORM{
            @Override
            public String toString() {
                return "CURRENT_WALLS_INFORM";
            }
        }

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
