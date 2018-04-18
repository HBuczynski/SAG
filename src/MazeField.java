import java.io.Serializable;

public class MazeField implements Serializable {

    public enum FieldCode {
        WALL, PHEROMONE, EXIT, MOBILE_WALL, ANT, ALLEY
    }

    private FieldCode value;

    MazeField(FieldCode value)
    {
        this.value = value;
    }

    public FieldCode getValue()
    {
        return value;
    }

    public void setValue(FieldCode value)
    {
        this.value = value;
    }
}


