import javax.swing.*;
import java.awt.*;

public class ContentDrawer {
    private int RECTANGLE_SIZE = 6;
    private int MAZE_LENGHT;

    private static final int CONTENT_PANE_WIDTH = 614; // 594+20
    private static final int CONTENT_PANE_HEIGHT = 714;// 10+594+10 + 80 <10+60+10>

    private static final int MAZE_PANE_WIDTH = 594; //m%99 = 6
    private static final int MAZE_PANE_HEIGHT = 594; //m%99


    private static final Color backgroundColor = new Color(230,255,255);
    private  JFrame frame;
    private MazePanel mazePanel;
    private JPanel jMazePanel;


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
        jMazePanel.setPreferredSize(new Dimension(MAZE_PANE_WIDTH, MAZE_PANE_HEIGHT));
        MazeField[][] emptyMaze = new MazeField[1][1];
        emptyMaze[0][0] =  new MazeField(MazeField.FieldCode.ALLEY);
        jMazePanel.add(drawMazePanel(emptyMaze));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(new Dimension(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT));

        jMazePanel.setBackground(backgroundColor);

        contentPane.add(jMazePanel);
        contentPane.add(drawCustomOptions());

        return contentPane;
    }


    private JPanel drawCustomOptions(){
        JPanel customOptionsPanel = new JPanel();
        customOptionsPanel.setPreferredSize(new Dimension(MAZE_PANE_WIDTH, 80));

        customOptionsPanel.add(new JLabel("Ilość mrówek"));
        customOptionsPanel.add(drawHorizontalPanel(0));
        customOptionsPanel.add(new JLabel("Ilość ruchomych ścianek"));
        customOptionsPanel.add(drawHorizontalPanel(1));
        customOptionsPanel.add(new JLabel("Zmiana rozmiaru labiryntu"));
        customOptionsPanel.add(drawButtonsHorizontalPanel());
        customOptionsPanel.setBackground(backgroundColor);
        return customOptionsPanel;
    }

    private JPanel drawHorizontalPanel(int option){
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setPreferredSize(new Dimension(MAZE_PANE_WIDTH/4,30));
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel,BoxLayout.X_AXIS));
        JTextField textField1 = new JTextField();
        JButton button1 = new JButton("OK");
        horizontalPanel.add(textField1);
        horizontalPanel.add(button1);
        horizontalPanel.setBackground(backgroundColor);

        return horizontalPanel;
    }

    private JPanel drawButtonsHorizontalPanel(){
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setBackground(backgroundColor);
        horizontalPanel.setPreferredSize(new Dimension(MAZE_PANE_WIDTH/4,30));
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel,BoxLayout.X_AXIS));
        JButton button = new JButton("+");
        JButton button1 = new JButton("-");
        horizontalPanel.add(button);
        horizontalPanel.add(button1);

        return horizontalPanel;
    }


    private MazePanel drawMazePanel(MazeField[][] generatedMaze){
        mazePanel = new MazePanel(generatedMaze, RECTANGLE_SIZE);
        mazePanel.setPreferredSize(new Dimension(MAZE_PANE_WIDTH, MAZE_PANE_HEIGHT));
        mazePanel.setBackground(backgroundColor);
        return mazePanel;
    }

    public void redrawMaze(MazeField[][] newMaze){
        jMazePanel.remove(mazePanel);
        jMazePanel.add(drawMazePanel(newMaze));
        frame.revalidate();
    }
}
