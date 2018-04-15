public class PheromoneField extends MazeField {

    private int antId;
    private int power;

    PheromoneField()
    {
        super(FieldCode.PHEROMONE);
    }

    public void setAntId(int antId)
    {
        this.antId = antId;
    }

    public int getAntId(int id)
    {
        return this.antId;
    }

    public void setPower(int power)
    {
        this.power = power;
    }

    public int getPower()
    {
        return this.power;
    }
}
