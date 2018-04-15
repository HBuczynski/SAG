public class AlleyFields extends MazeField {

    private int antId;
    private int power;

    AlleyFields()
    {
        super(FieldCode.ALLEY);
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
