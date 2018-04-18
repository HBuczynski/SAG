public class PheromoneField extends MazeField {

    private int power;

    PheromoneField()
    {
        super(FieldCode.PHEROMONE);
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
