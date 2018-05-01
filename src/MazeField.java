import java.io.Serializable;

const static double EVAPORATION_COEFF = 0.2;

public class MazeField implements Serializable {

    public enum FieldCode {
        WALL, EXIT, MOBILE_WALL, ANT, ALLEY
    }

    private FieldCode value;
    private double pheromonePower;

    MazeField(FieldCode value)
    {
        this.value = value;
        this.pheromonePower = 0;
    }

    public FieldCode getValue()
    {
        return value;
    }

    public void setValue(FieldCode value)
    {
        this.value = value;
    }

    public void setPheromonePower(double power)
    {
        this.pheromonePower = power;
    }

    public double getPheromonePower()
    {
        return this.pheromonePower;
    }

    public void evaporatePheromone(){this.pheromonePower *= (1-EVAPORATION_COEFF);}
}


