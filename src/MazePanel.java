import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private MazeField[][] maze;
    private int size;

    public MazePanel (MazeField[][] maze, int sizeValue){
        this.maze = maze;
        this.size = sizeValue;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.translate(10,10);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                Color newColor;

                switch (maze[i][j].getValue()) {
                    case WALL:
                        newColor = Color.BLACK;
                        break;

                    case ALLEY:
                        double pheromone = maze[i][j].getPheromonePower();
                        if(pheromone > 0.75)
                            newColor = new Color(255,200,200);
                        else if(pheromone > 1.0)
                            newColor = new Color(255,160,160);
                        else if(pheromone > 1.2)
                            newColor = new Color(255,100,100);
                        else
                            newColor = new Color(255,255,255);
                        break;

                    case EXIT:
                        newColor = Color.RED;
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
