import javax.swing.*;
import java.awt.*;

public class ContentDrawer {
    private static final int CONTENT_PANE_WIDTH = 614; //LEFT_PANE_WIDTH + 300
    private static final int CONTENT_PANE_HEIGHT = 614; //LEFT_PANE_HEIGHT
    private static final int PANE_WIDTH = 594; //m%99
    private static final int PANE_HEIGHT = 594; //m%99
    private static final Color backgroundColor = new Color(230,255,255);
    private  JFrame frame;
    MazePanel mazePanel;
    JPanel jMazePanel;


    public ContentDrawer(){
    }

    public void drawContent(){
        this.frame= new JFrame("SAG_Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(getContentPane());

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

    }

    private JPanel getContentPane(){
        jMazePanel = new JPanel();
        jMazePanel.setLayout(new BoxLayout(jMazePanel, BoxLayout.Y_AXIS));
        jMazePanel.setPreferredSize(new Dimension(PANE_WIDTH,PANE_HEIGHT));
        int[][] emptyMaze = new int[1][1];
        emptyMaze[0][0]=0;
        jMazePanel.add(drawMazePanel(emptyMaze));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.setPreferredSize(new Dimension(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT));

        jMazePanel.setBackground(backgroundColor);
        contentPane.add(jMazePanel);

        return contentPane;
    }

    public MazePanel drawMazePanel(int[][] generatedMaze){
        mazePanel = new MazePanel(generatedMaze);
        mazePanel.setPreferredSize(new Dimension(PANE_WIDTH,PANE_HEIGHT));
        mazePanel.setBackground(backgroundColor);
        return mazePanel;
    }

    public void redrawMaze(int[][] newMaze){
        jMazePanel.remove(mazePanel);
        jMazePanel.add(drawMazePanel(newMaze));
        frame.revalidate();
    }
}
