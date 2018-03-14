import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MazeGenerator {
    private static final int MAZE_HEIGHT = 99;
    private static final int MAZE_WIDTH = 99;
    int[][] maze;

    public int[][] generateMaze() {
        maze = new int[MAZE_HEIGHT][MAZE_WIDTH];

        // Initialize
        for (int i = 0; i < MAZE_HEIGHT; i++)
            for (int j = 0; j < MAZE_WIDTH; j++)
                maze[i][j] = 1;

        Random rand = new Random();
        // r for row、c for column
        // Generate random r
        int r = rand.nextInt(MAZE_HEIGHT);
        while (r % 2 == 0) {
            r = rand.nextInt(MAZE_HEIGHT);
        }
        // Generate random c
        int c = rand.nextInt(MAZE_WIDTH);
        while (c % 2 == 0) {
            c = rand.nextInt(MAZE_WIDTH);
        }
        // Starting cell
        maze[r][c] = 0;

        //　Allocate the maze with recursive method
        recursion(r, c);

        //select exits
        createExits(maze);

        return maze;
    }

    private void recursion(int r, int c) {
        // 4 random directions
        int[] randDirs = generateRandomDirections();

        // Examine each direction
        for (int i = 0; i < randDirs.length; i++) {

            switch(randDirs[i]){
                case 1: // Up
                    //　Whether 2 cells up is out or not
                    if (r-2 <= 0)
                        continue;
                    if (maze[r - 2][c] != 0) {
                        maze[r-2][c] = 0;
                        maze[r-1][c] = 0;
                        recursion(r - 2, c);
                    }
                    break;
                case 2: // Right
                    // Whether 2 cells to the right is out or not
                    if (c + 2 >= MAZE_WIDTH - 1)
                        continue;
                    if (maze[r][c + 2] != 0) {
                        maze[r][c + 2] = 0;
                        maze[r][c + 1] = 0;
                        recursion(r, c + 2);
                    }
                    break;
                case 3: // Down
                    // Whether 2 cells down is out or not
                    if (r + 2 >= MAZE_HEIGHT - 1)
                        continue;
                    if (maze[r + 2][c] != 0) {
                        maze[r+2][c] = 0;
                        maze[r+1][c] = 0;
                        recursion(r + 2 , c);
                    }
                    break;
                case 4: // Left
                    // Whether 2 cells to the left is out or not
                    if (c - 2 <= 0)
                        continue;

                    if (maze[r][c - 2] != 0) {
                        maze[r][c - 2] = 0;
                        maze[r][c - 1] = 0;
                        recursion(r, c - 2);
                    }
                    break;
            }
        }

    }

    /**
     * Generate an array with random directions 1-4
     * @return Array containing 4 directions in random order
     */
    private int[] generateRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            randoms.add(i + 1);
        Collections.shuffle(randoms);

        int [] tab = new int[4];
        for (int i = 0; i<randoms.size(); i++){
            tab[i]=randoms.get(i);
        }
        return tab;
    }

    private void createExits(int[][] maze){
        int MIN_SPACE = 5;

        for(int i = 10; i < MAZE_HEIGHT - 12;i++) {
            //1 BOTTOM
            if(maze[maze.length-2][i] == 0 && maze[maze.length-2][i+1] == 0 && maze[maze.length-2][i+2] == 0){
                maze[maze.length-1][i] = 0;
                maze[maze.length-1][i+1] = 0;
                i+=MIN_SPACE;
                //continue;
            }
            //2 RIGHT
            if(maze[i][maze.length-2] == 0 && maze[i+1][maze.length-2] == 0 && maze[i+2][maze.length-2] == 0){
                maze[i][maze.length-1] = 0;
                maze[i+1][maze.length-1] = 0;
                i+=MIN_SPACE;
                // continue;
            }

            // 3 TOP
            if(maze[1][i] == 0 && maze[1][i+1] == 0 && maze[1][i+2] == 0){
                maze[0][i] = 0;
                maze[0][i+1] = 0;
                i+=MIN_SPACE;
                //continue;
            }

            //4 LEFT
            if(maze[i][1]== 0 && maze[i+1][1] == 0 && maze[i+2][1] == 0) {
                maze[i][0]=0;
                maze[i+1][0]=0;
                i+=MIN_SPACE;
            }
        }

    }
}
