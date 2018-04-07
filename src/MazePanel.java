import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private PossibleValues[][] maze;
    private int size;

    public MazePanel (PossibleValues[][] maze, int sizeValue){
        this.maze = maze;
        this.size = sizeValue;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.translate(10,10);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                Color newColor;

                switch (maze[i][j]) {
                    case WALL:
                        newColor = Color.BLACK;
                        break;

                    case ALLEY:
                        newColor = Color.WHITE;
                        break;

                    case EXIT:
                        newColor = Color.WHITE;
                        break;

                    case MOBILE_WALL:
                        newColor = Color.YELLOW;
                        break;

                    case ANT:
                        newColor = Color.GREEN;
                        break;

                    default:
                        newColor = Color.YELLOW;
                }
                g.setColor(newColor);
                g.fillRect(size * j, size * i, size, size);
                g.drawRect(size * j, size * i, size, size);
            }
        }
    }

}
