import javax.swing.*;
import java.awt.*;

public class ContentDrawer {

    private static final int CONTENT_PANE_WIDTH = 515; // 495+20
    private static final int CONTENT_PANE_HEIGHT = 595;// 10+495+10 + 80 <10+60+10>

    private static final int MAZE_PANE_DIM = 495;


    private static final Color backgroundColor = new Color(230,255,255);
    private  JFrame frame;
    private MazePanel mazePanel;
    private JPanel jMazePanel;
    private SetMazeSizeListener mazeSizeListener;
    private SetAntCountListener antCountListener;
    private SetWallsCountListener wallsCountListener;
    private SetEvaporationCoeffListener evaporationCoeffListener;
    private int walls;
    private int ants;
    private double coeff;


    public ContentDrawer(){
        walls = 0;
        ants = 0;
        coeff = 0.9;
    }

    public void drawContent(){
        this.frame = new JFrame("SAG_Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(getContentPane());

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel getContentPane(){
        int RECTANGLE_SIZE = 6;
        jMazePanel = new JPanel();
        jMazePanel.setLayout(new BoxLayout(jMazePanel, BoxLayout.Y_AXIS));
        jMazePanel.setPreferredSize(new Dimension(MAZE_PANE_DIM, MAZE_PANE_DIM));
        MazeField[][] emptyMaze = new MazeField[1][1];
        emptyMaze[0][0] =  new MazeField(MazeField.FieldCode.ALLEY);
        jMazePanel.add(drawMazePanel(emptyMaze,RECTANGLE_SIZE));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(new Dimension(CONTENT_PANE_WIDTH+100, CONTENT_PANE_HEIGHT+100));

        jMazePanel.setBackground(backgroundColor);

        contentPane.add(jMazePanel);
        contentPane.add(drawCustomOptions());

        return contentPane;
    }


    private JPanel drawCustomOptions(){
        JPanel customOptionsPanel = new JPanel();
        customOptionsPanel.setPreferredSize(new Dimension(MAZE_PANE_DIM, 80));
        customOptionsPanel.setMinimumSize(new Dimension(MAZE_PANE_DIM, 80));

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        horizontalBox.add(new JLabel("Ilość mrówek"));
        horizontalBox.add(drawHorizontalPanel(0));
        customOptionsPanel.add(horizontalBox);
        horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        horizontalBox.add(new JLabel("Ilość ruchomych ścianek"));
        horizontalBox.add(drawHorizontalPanel(1));
        customOptionsPanel.add(horizontalBox);
        horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        horizontalBox.add(new JLabel("Zmiana rozmiaru labiryntu"));
        horizontalBox.add(drawButtonsHorizontalPanel());
        customOptionsPanel.add(horizontalBox);
        horizontalBox = Box.createHorizontalBox();
        horizontalBox.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        horizontalBox.add(new JLabel("Wsp. wyparowania (0<wsp<1)"));
        horizontalBox.add(drawHorizontalPanel(2));
        customOptionsPanel.add(horizontalBox);
        customOptionsPanel.setBackground(backgroundColor);
        return customOptionsPanel;
    }

    private JPanel drawHorizontalPanel(int option){
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setPreferredSize(new Dimension(MAZE_PANE_DIM/2,30));
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel,BoxLayout.X_AXIS));

        horizontalPanel.setBackground(backgroundColor);

        switch (option){
            case 0:
                JTextField textField1 = new JTextField();
                JButton button1 = new JButton("OK");
                 button1.addActionListener(e -> {
                     String stringValue = textField1.getText().trim();
                     if(!stringValue.isEmpty()){
                         if(ants != Integer.parseInt(stringValue)){
                             ants = Integer.parseInt(stringValue);
                             antCountListener.onAntCountChanged(ants);
                         }

                     }
                 });

                horizontalPanel.add(textField1);
                horizontalPanel.add(button1);
                break;

            case 1:
                JTextField textField2 = new JTextField();
                JButton button2 = new JButton("OK");
                button2.addActionListener(e -> {

                    String stringValue = textField2.getText().trim();
                    if(!stringValue.isEmpty()){
                        if(walls != Integer.parseInt(stringValue)){
                            walls = Integer.parseInt(stringValue);
                            wallsCountListener.onWallsCountChanged(walls);
                        }
                    }
                });

                horizontalPanel.add(textField2);
                horizontalPanel.add(button2);
                break;

            case 2:
                JTextField textField3 = new JTextField();
                JButton button3 = new JButton("OK");
                button3.addActionListener(e -> {

                    String stringValue = textField3.getText().trim();
                    if(!stringValue.isEmpty()){
                        if(coeff != Double.parseDouble(stringValue)){
                            coeff = Double.parseDouble(stringValue);
                            evaporationCoeffListener.onEvaporationCoeffChange(coeff);
                        }
                    }
                });

                horizontalPanel.add(textField3);
                horizontalPanel.add(button3);
                break;
        }
        return horizontalPanel;
    }

    private JPanel drawButtonsHorizontalPanel(){
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setBackground(backgroundColor);
        horizontalPanel.setPreferredSize(new Dimension(MAZE_PANE_DIM/4,30));
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel,BoxLayout.X_AXIS));
        JButton button = new JButton("+");
        button.setPreferredSize(new Dimension(30, 30));
        button.setMinimumSize(new Dimension(30,30));
        button.addActionListener(e -> mazeSizeListener.onMazeSizeUp());
        JButton button1 = new JButton("-");
        button1.setPreferredSize(new Dimension(30, 30));
        button1.setMinimumSize(new Dimension(30,30));
        button1.addActionListener(e-> mazeSizeListener.onMazeSizeDown());
        horizontalPanel.add(button);
        horizontalPanel.add(button1);

        return horizontalPanel;
    }


    private MazePanel drawMazePanel(MazeField[][] generatedMaze, int rectDim){
        mazePanel = new MazePanel(generatedMaze, rectDim);
        mazePanel.setPreferredSize(new Dimension(MAZE_PANE_DIM, MAZE_PANE_DIM));
        mazePanel.setMinimumSize(new Dimension(MAZE_PANE_DIM, MAZE_PANE_DIM));
        mazePanel.setBackground(backgroundColor);
        return mazePanel;
    }

    public void redrawMaze(MazeField[][] newMaze,int rectDim){
        jMazePanel.remove(mazePanel);
        jMazePanel.add(drawMazePanel(newMaze,rectDim));
        frame.revalidate();
    }

    public SetAntCountListener getAntCountListener() {
        return antCountListener;
    }

    public void setAntCountListener(SetAntCountListener antCountListener) {
        this.antCountListener = antCountListener;
    }

    public SetMazeSizeListener getMazeSizeListener() {
        return mazeSizeListener;
    }

    public void setMazeSizeListener(SetMazeSizeListener mazeSizeListener) {
        this.mazeSizeListener = mazeSizeListener;
    }

    public SetWallsCountListener getWallsCountListener() {
        return wallsCountListener;
    }

    public void setWallsCountListener(SetWallsCountListener wallsCountListener) {
        this.wallsCountListener = wallsCountListener;
    }

    public void setEvaporationCoeffListener(SetEvaporationCoeffListener evaporationCoeffListener) {
        this.evaporationCoeffListener = evaporationCoeffListener;
    }
}
