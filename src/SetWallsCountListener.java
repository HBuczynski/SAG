import javax.swing.*;

public interface SetWallsCountListener {
    void onWallsCountChanged(int walls, JLabel countedAndOut, JLabel countedAndExists);
}
