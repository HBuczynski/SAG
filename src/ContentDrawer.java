import javax.swing.*;
import java.awt.*;

public class ContentDrawer {
    private static final int CONTENT_PANE_WIDTH = 914; //LEFT_PANE_WIDTH + 300
    private static final int CONTENT_PANE_HEIGHT = 714; //LEFT_PANE_HEIGHT
    private static final int LEFT_PANE_WIDTH = 614; //m%99
    private static final int LEFT_PANE_HEIGHT = 714; //m%99
    private static final int BUTTON_H = 100;
    private static final Font timesBold16 = new Font("Times New Roman", Font.BOLD, 16);
    private static final Font timesBold36 = new Font("Times New Roman", Font.BOLD, 36);
    private Frame frame;
    private static final Color backgroundColor = new Color(230,255,255);
    private JButton renderMazeButton;
    private JPanel leftPane;
    private JPanel rightPane;
    private MazePanel mazePanel;


    public ContentDrawer(JFrame frame){
        this.frame = frame;
        this.renderMazeButton = new JButton("RENDER MAZE");

        setButtonParametres(renderMazeButton);

        renderMazeButton.addActionListener(e -> {
            renderNewMaze();
        });
    }

    private void setButtonParametres(JButton button){
        button.setMaximumSize(new Dimension(LEFT_PANE_WIDTH,BUTTON_H));
        button.setFont(timesBold36);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void drawContent(){
        setLeftPanel(new JPanel(), getMazePanel());
        setRightPanel(new JPanel());
        frame.add(getContentPane(leftPane,rightPane));
    }

    private JPanel getContentPane(JPanel leftPane, JPanel rightPane){
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.setPreferredSize(new Dimension(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT));

        rightPane.setBackground(backgroundColor);
        leftPane.setBackground(backgroundColor);

        contentPane.add(leftPane);
        contentPane.add(rightPane);

        return contentPane;
    }

    private void setLeftPanel(JPanel leftPanel, MazePanel mazePanel){
        this.leftPane = leftPanel;
        leftPane.setLayout(new BoxLayout(leftPane,BoxLayout.Y_AXIS));
        leftPane.setPreferredSize(new Dimension(LEFT_PANE_WIDTH,LEFT_PANE_HEIGHT));
        leftPane.setFont(timesBold16);

        leftPane.add(renderMazeButton);
        leftPane.add(mazePanel);
    }

    private void setRightPanel(JPanel rightPanel){
        this.rightPane = rightPanel;
        rightPane.setLayout(new BoxLayout(rightPane,BoxLayout.Y_AXIS));
        rightPane.setPreferredSize(new Dimension(CONTENT_PANE_WIDTH-LEFT_PANE_WIDTH, CONTENT_PANE_HEIGHT));
        rightPane.setFont(timesBold16);

        rightPane.add(new Button("Action 1"));
        rightPane.add(new Button("Action 2"));
        rightPane.add(new Button("Action 3"));
        rightPane.add(new Button("Action 4"));

    }

    private MazePanel getMazePanel(){
        MazeGenerator generator = new MazeGenerator();
        mazePanel = new MazePanel(generator.generateMaze());
        mazePanel.setPreferredSize(new Dimension(LEFT_PANE_WIDTH,LEFT_PANE_HEIGHT-BUTTON_H));
        mazePanel.setBackground(backgroundColor);
        return mazePanel;
    }

    private void renderNewMaze(){
        leftPane.remove(mazePanel);
        leftPane.add(getMazePanel());
        frame.revalidate();
    }
}
