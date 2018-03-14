import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    int[][] maze;

    public MazePanel (int[][] maze){
        this.maze = maze;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.translate(10,10);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                Color newColor;

                switch (maze[i][j]) {
                    case 1:
                        newColor = Color.BLACK;
                        break;

                    case 0:
                        newColor = Color.WHITE;
                        break;

                    default:
                        newColor = Color.YELLOW;
                }
                g.setColor(newColor);
                g.fillRect(6 * j, 6 * i, 6, 6);

                //grid edges
                //g.setColor(Color.black);

                g.drawRect(6 * j, 6 * i, 6, 6);
            }
        }
    }

}
