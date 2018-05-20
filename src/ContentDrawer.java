import javax.swing.*;
import java.awt.*;

public class ContentDrawer {

    private static final int CONTENT_PANE_WIDTH = 515; // 495+20
    private static final int CONTENT_PANE_HEIGHT = 745;// 10+495+200+10  <10+180+10>

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
    private JLabel countedAndExists, countedAndOut;


    public ContentDrawer(){
        walls = 0;
        ants = 0;
        coeff = 0.9;
        countedAndExists = new JLabel(" ");
        countedAndOut = new JLabel(" ");
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
        contentPane.setPreferredSize(new Dimension(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT));

        jMazePanel.setBackground(backgroundColor);

        contentPane.add(jMazePanel);
        contentPane.add(drawCustomOptions());

        return contentPane;
    }


    private JPanel drawCustomOptions(){
        JPanel customOptionsPanel = new JPanel();
        customOptionsPanel.setPreferredSize(new Dimension(MAZE_PANE_DIM, 180));
        customOptionsPanel.setMinimumSize(new Dimension(MAZE_PANE_DIM, 180));
       // customOptionsPanel.setLayout(new BoxLayout(customOptionsPanel,BoxLayout.Y_AXIS));
        customOptionsPanel.setBackground(backgroundColor);

        JLabel emptyLabel = new JLabel(" ");
        emptyLabel.setMaximumSize(new Dimension(MAZE_PANE_DIM/4,30));

        Box verticalBox = Box.createVerticalBox();

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.setPreferredSize(new Dimension(MAZE_PANE_DIM/2,30));

        //ilosc mrowek + okienko i przycisk
        JLabel label1 = new JLabel("Ilość mrówek");
        label1.setMaximumSize(new Dimension(220,30));
        horizontalBox.add(label1);
        horizontalBox.add(drawHorizontalPanel(0));
        verticalBox.add(horizontalBox);

        //ilosc ruchomych scianek + okienko i przycisk
        horizontalBox = Box.createHorizontalBox();
        JLabel label2 = new JLabel("Ilość ruchomych ścianek");
        label2.setMaximumSize(new Dimension(220,30));
        horizontalBox.add(label2);
        horizontalBox.add(drawHorizontalPanel(1));
        verticalBox.add(horizontalBox);

        //empty line
        horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(emptyLabel);
        verticalBox.add(horizontalBox);

        horizontalBox = Box.createHorizontalBox();
        JLabel label3 = new JLabel("Współczynnik wyparowania (0<wsp<1)");
        horizontalBox.add(label3);
        JLabel emptyLabel2 = new JLabel(" ");
        emptyLabel2.setMaximumSize(new Dimension(MAZE_PANE_DIM/3,30));
        horizontalBox.add(emptyLabel2);
        //wpolczynnik wyparowania
        verticalBox.add(horizontalBox);
        verticalBox.add(drawHorizontalPanel(2));

        //zmiana rozmiaru lab
        horizontalBox = Box.createHorizontalBox();
        JLabel label4 = new JLabel("Zmiana rozmiaru labiryntu");
        horizontalBox.add(label4);
        horizontalBox.add(emptyLabel);
        horizontalBox.add(emptyLabel);

        verticalBox.add(horizontalBox);
        verticalBox.add(drawMazeSizeButtonsPanel());

        //Mrowki pozostale
        verticalBox.add(countedAndExists);

        //Mrowki opuszajace labirynt
        verticalBox.add(countedAndOut);

        customOptionsPanel.setBackground(backgroundColor);
        customOptionsPanel.add(verticalBox);
        return customOptionsPanel;
    }

    private Box drawHorizontalPanel(int option){
        Box horizontalPanel = Box.createHorizontalBox();
        horizontalPanel.setPreferredSize(new Dimension(MAZE_PANE_DIM/4,30));

        horizontalPanel.setBackground(backgroundColor);

        switch (option){
            case 0:
                JTextField textField1 = new JTextField();
                JButton button1 = new JButton("OK");
                 button1.addActionListener(e -> {
                     String stringValue = textField1.getText().trim();
                     if(!stringValue.isEmpty()){
                             ants = Integer.parseInt(stringValue);
                             antCountListener.onAntCountChanged(ants,countedAndOut,countedAndExists);

                     }
                     textField1.setText("");
                 });

                 textField1.setMaximumSize(new Dimension(50,30));
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
                            wallsCountListener.onWallsCountChanged(walls,countedAndOut,countedAndExists);
                        }
                        textField2.setText("");
                    }
                });


                textField2.setMaximumSize(new Dimension(50,30));
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

                textField3.setMaximumSize(new Dimension(80,30));
                button3.setPreferredSize(new Dimension(80,30));
                horizontalPanel.add(textField3);
                horizontalPanel.add(button3);
                break;
        }
        return horizontalPanel;
    }


    private Box drawMazeSizeButtonsPanel(){
        Box panel = Box.createHorizontalBox();
        panel.setBackground(backgroundColor);
        panel.setPreferredSize(new Dimension(MAZE_PANE_DIM/2,30));

        JButton button = new JButton("+");
        button.setPreferredSize(new Dimension(30, 30));
        button.addActionListener(e -> mazeSizeListener.onMazeSizeUp());

        JButton button1 = new JButton("-");
        button1.setPreferredSize(new Dimension(30, 30));
        button1.addActionListener(e-> mazeSizeListener.onMazeSizeDown());

        panel.add(button);
        panel.add(button1);

        return panel;
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
