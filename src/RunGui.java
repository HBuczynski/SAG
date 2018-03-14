import javax.swing.*;

public class RunGui {

    public static void main(String[] arg ){

        JFrame frame= new JFrame("SAG_Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ContentDrawer contentDrawer = new ContentDrawer(frame);
        contentDrawer.drawContent();

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        System.out.println();
    }

}
