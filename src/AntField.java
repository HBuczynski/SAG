import java.awt.*;

public class AntField extends MazeField{

    private int id;

    AntField()
    {
        super(FieldCode.ANT);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}