import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MazePanel extends JPanel {
    private MazeField[][] maze;
    private int size;

    private BufferedImage image;

    public MazePanel(MazeField[][] maze, int sizeValue) {
        this.maze = maze;
        this.size = sizeValue;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.translate(10, 10);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                Color newColor;

                switch (maze[i][j].getValue()) {
                    case WALL:
                        newColor = Color.BLACK;
                        drawRect(g,newColor,i,j);
                        break;

                    case ALLEY:
                        double pheromone = maze[i][j].getPheromonePower();
                        int coeff = (int)(255*(1-pheromone/MazeField.MAX_PHEROMONE_POWER));
                        newColor = new Color(255, coeff, coeff);
                        drawRect(g,newColor,i,j);
                        break;

                    case EXIT:
                        newColor = Color.RED;
                        drawRect(g,newColor,i,j);
                        break;

                    case MOBILE_WALL:
                        newColor = Color.YELLOW;
                        drawRect(g,newColor,i,j);
                        break;

                    case ANT:
                        //drawIcon(g,i,j);
                        newColor = new Color(102, 0, 0);
                        drawCirc(g,newColor,i,j);
                        break;

                    default:
                        newColor = Color.YELLOW;
                        drawRect(g,newColor,i,j);
                }
            }
        }
    }

    private void drawRect(Graphics g, Color c, int i, int j){
        g.setColor(c);
        g.fillRect(size * j, size * i, size, size);
        g.drawRect(size * j, size * i, size, size);
    }

    private void drawCirc(Graphics g, Color c, int i, int j){
        g.setColor(c);
        g.fillOval(size * j, size * i, size, size);
        g.drawOval(size * j, size * i, size, size);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
