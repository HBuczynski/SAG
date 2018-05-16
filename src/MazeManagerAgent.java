import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MazeManagerAgent extends Agent {

    private int selection;

    private int[] lenghts = {15, 33, 45, 55, 99, 165};
    private int[] rectDims = {33,15,11,9,5,3};
    private MazeField[][] maze;
    private Vector<MazeField> dynamicWalls;

    private AID aid;
    private DFAgentDescription[] result;

    public void setup() {
        aid = new AID();
        dynamicWalls = new Vector<MazeField>();

        this.selection = 2;

        maze = generateMaze(lenghts[selection]);

        DFAgentDescription dfad = new DFAgentDescription();
        dfad.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("maze");
        sd.setName("manager");
        dfad.addServices(sd);

        try {
            DFService.register(this, dfad);
        } catch (FIPAException ex) {
            System.out.println(ex.getMessage());
        }

        addBehaviour(new TickerBehaviour(this, 1500) {
            @Override
            protected void onTick() {
                updatePheromons();
            }
        });

        addBehaviour(new TickerBehaviour(this, 500) {
            @Override
            protected void onTick() {

                if(!dynamicWalls.isEmpty()) {
                    for (MazeField field : dynamicWalls) {
                        Random random = new Random();
                        int isWall = random.nextInt(10)%2;
                        if (field.getValue() == MazeField.FieldCode.MOBILE_WALL && isWall == 1) {
                            field.setValue(MazeField.FieldCode.ALLEY);
                        } else if(field.getValue() == MazeField.FieldCode.ALLEY && isWall == 0) {
                            field.setValue(MazeField.FieldCode.MOBILE_WALL);
                        }
                    }
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMessage = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

                if (receivedMessage != null) {

                    try {
                        Command cmd = (Command) receivedMessage.getContentObject();

                        ACLMessage message = MessageFactory.createInformativeMessage();
                        message.addReceiver(receivedMessage.getSender());

                        switch (cmd.getCommandCode()) {
                            case MAZE_REQUEST:
                                MazeInformCommand command = new MazeInformCommand();
                                command.setMazeValues(maze, rectDims[selection]);

                                try {
                                    message.setContentObject(command);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                //System.out.println(Command.CommandCode.MAZE_REQUEST.toString());
                                send(message);
                                break;

                            case ANT_NEIGHBORHOOD_REQUEST:
                                AntNeighbourhoodRequestCommand antNeighborhoodRequestCommand = (AntNeighbourhoodRequestCommand) receivedMessage.getContentObject();
                                AntNeighbourhoodInformCommand neighbourhoodInformCommand = new AntNeighbourhoodInformCommand();

                                Point antPosition = antNeighborhoodRequestCommand.getCurrentPosition();

                                //Sending information about four neighbouring fields: northern, eastern, southern, western
                                //(in that order):
                                Vector<MazeField> neighbourhood = new Vector<>(4);
                                for (int i = -1; i < 2; i += 2) {
                                    if (antPosition.x + i > 0 && antPosition.x + i < maze.length)
                                        if (maze[antPosition.x + i][antPosition.y].getValue() != MazeField.FieldCode.WALL &&
                                                maze[antPosition.x + i][antPosition.y].getValue() != MazeField.FieldCode.MOBILE_WALL)
                                            neighbourhood.add(maze[antPosition.x + i][antPosition.y]);

                                    if (antPosition.y + i > 0 && antPosition.y + i < maze.length)
                                        if (maze[antPosition.x][antPosition.y + i].getValue() != MazeField.FieldCode.WALL &&
                                                maze[antPosition.x][antPosition.y + i].getValue() != MazeField.FieldCode.MOBILE_WALL)
                                            neighbourhood.add(maze[antPosition.x][antPosition.y + i]);
                                }
                                neighbourhoodInformCommand.setMazeValues(neighbourhood);
                                try {
                                    message.setContentObject(neighbourhoodInformCommand);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                //System.out.println(Command.CommandCode.ANT_NEIGHBORHOOD_REQUEST.toString());
                                send(message);

                                break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMessage = receive(MessageTemplateFactory.createInformTemplate());

                if (receivedMessage != null) {

                    try {
                        Command cmd = (Command) receivedMessage.getContentObject();

                        switch (cmd.getCommandCode()) {
                            case ANT_POSITION_INFORM:
                                AntPositionInformCommand positionInformCommand = (AntPositionInformCommand) receivedMessage.getContentObject();

                                Point newPosition = positionInformCommand.getNewPosition();
                                Point oldPosition = positionInformCommand.getOldPosition();
                                int antDistance = positionInformCommand.getDistance();
                                maze[oldPosition.x][oldPosition.y].setValue(MazeField.FieldCode.ALLEY);
                                maze[newPosition.x][newPosition.y].setValue(MazeField.FieldCode.ANT);
                                double newPheromonePower = maze[newPosition.x][newPosition.y].getPheromonePower() + Math.pow(1.0 / (double) antDistance, 0.25);
                                if(newPheromonePower > MazeField.MAX_PHEROMONE_POWER){
                                    newPheromonePower = MazeField.MAX_PHEROMONE_POWER;
                                }
                                maze[newPosition.x][newPosition.y].setPheromonePower(newPheromonePower);
                                //System.out.println(maze[newPosition.x][newPosition.y].getPheromonePower());
                                //System.out.println(Command.CommandCode.ANT_POSITION_INFORM.toString());
                                break;

                            case ANT_DISABLED_INFORM:
                                AntPositionInformCommand positionInform = (AntPositionInformCommand) receivedMessage.getContentObject();
                                Point oldPos = positionInform.getOldPosition();
                                maze[oldPos.x][oldPos.y].setValue(MazeField.FieldCode.ALLEY);

                                // Sending information to the MazeDrawingAgent
                                try {
                                    result = DFService.search(MazeManagerAgent.this, dfad);
                                    for (DFAgentDescription agent : result) {
                                        ACLMessage message = MessageFactory.createInformativeMessageAntExit();
                                        message.addReceiver(agent.getName());

                                        MazeChangedInformCommand mazeChangedInformCommand = new MazeChangedInformCommand(Command.CommandCode.MAZE_CHANGED_ANT_EXIT);
                                        message.setContent(mazeChangedInformCommand.getCommandCode().toString());
                                        send(message);
                                        //System.out.println(mazeChangedInformCommand.getCommandCode().toString());
                                    }

                                } catch (FIPAException e) {
                                    e.printStackTrace();
                                }

//TODO What does it do?
//                                for (int i = 0; i < 2; i++) {
//                                    for (int j = 0; j < 2; j++) {
//                                        if (maze[oldPos.x + i][oldPos.y + j].getValue() == MazeField.FieldCode.ANT)
//                                            maze[oldPos.x + i][oldPos.y + j].setValue(MazeField.FieldCode.ALLEY);
//                                    }
//                                }

                                System.out.println(Command.CommandCode.ANT_DISABLED_INFORM.toString());
                                break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    block();
                }
            }
        });

        //MazeChanged
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMessage = receive(MessageTemplateFactory.createInformTemplateMaze());

                if (receivedMessage != null) {
                    String command = receivedMessage.getContent();

                    switch (command) {
                        case "MAZE_CHANGED_DOWN_INFORM": {
                            if (selection < 4) selection++;
                            maze = generateMaze(lenghts[selection]);
                            System.out.println("MAZE_CHANGED_DOWN_INFORM");
                            break;
                        }


                        case "MAZE_CHANGED_UP_INFORM": {
                            if (selection > 0) selection -= 1;
                            maze = generateMaze(lenghts[selection]);
                            System.out.println("MAZE_CHANGED_UP_INFORM");
                            break;
                        }
                    }
                }
            }
        });

        //Wall Changed
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMessage = receive(MessageTemplateFactory.createInformTemplateDynamicWalls());

                try {
                    if (receivedMessage != null) {
                        Command cmd = (Command) receivedMessage.getContentObject();

                        switch (cmd.getCommandCode()) {
                            case DYNAMIC_WALLS_NUMBER_INFORM: {

                                DynamicWallsInformCommand dynamicCommand = (DynamicWallsInformCommand) receivedMessage.getContentObject();
                                int wallNumber = dynamicCommand.getWallsCount();

                                if(dynamicWalls.size() < wallNumber)
                                {
                                    addDynamicWalls(wallNumber - dynamicWalls.size());
                                }
                                else
                                {
                                    while ((dynamicWalls.size() > wallNumber))
                                    {
                                        MazeField lastField = dynamicWalls.lastElement();
                                        lastField.setValue(MazeField.FieldCode.ALLEY);
                                        dynamicWalls.remove(lastField);
                                    }
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void addDynamicWalls(int count)
    {
        while(count > 0)
        {
            Random rand = new Random();

            int r = rand.nextInt(maze.length-2) + 1;
            int c = rand.nextInt(maze.length-2) + 1;

            if(maze[r][c].getValue() == MazeField.FieldCode.WALL) {

                maze[r][c].setValue(MazeField.FieldCode.MOBILE_WALL);
                dynamicWalls.add(maze[r][c]);
                count--;
            }
        }
    }

    public void takeDown() {
        try {
            DFService.deregister(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MazeField[][] generateMaze(int size) {
        maze = new MazeField[size][size];

        // Initialize
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                maze[i][j] = new MazeField(MazeField.FieldCode.WALL);
                maze[i][j].setCoordinateX(i);
                maze[i][j].setCoordinateY(j);
            }

        Random rand = new Random();
        // r for row、c for column
        // Generate random r
        int r = rand.nextInt(size);
        while (r % 2 == 0) {
            r = rand.nextInt(size);
        }
        // Generate random c
        int c = rand.nextInt(size);
        while (c % 2 == 0) {
            c = rand.nextInt(size);
        }
        // Starting cell
        maze[r][c] = new MazeField(MazeField.FieldCode.ALLEY);

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

            switch (randDirs[i]) {
                case 1: // Up
                    //　Whether 2 cells up is out or not
                    if (r - 2 <= 0)
                        continue;
                    if (maze[r - 2][c].getValue() != MazeField.FieldCode.ALLEY) {
                        maze[r - 2][c].setValue(MazeField.FieldCode.ALLEY);
                        maze[r - 1][c].setValue(MazeField.FieldCode.ALLEY);
                        maze[r - 2][c].setPheromonePower(0.1);
                        maze[r - 1][c].setPheromonePower(0.1);
                        recursion(r - 2, c);
                    }
                    break;
                case 2: // Right
                    // Whether 2 cells to the right is out or not
                    if (c + 2 >= maze.length - 1)
                        continue;
                    if (maze[r][c + 2].getValue() != MazeField.FieldCode.ALLEY) {
                        maze[r][c + 2].setValue(MazeField.FieldCode.ALLEY);
                        maze[r][c + 1].setValue(MazeField.FieldCode.ALLEY);
                        maze[r][c + 2].setPheromonePower(0.1);
                        maze[r][c + 1].setPheromonePower(0.1);
                        recursion(r, c + 2);
                    }
                    break;
                case 3: // Down
                    // Whether 2 cells down is out or not
                    if (r + 2 >= maze.length - 1)
                        continue;
                    if (maze[r + 2][c].getValue() != MazeField.FieldCode.ALLEY) {
                        maze[r + 2][c].setValue(MazeField.FieldCode.ALLEY);
                        maze[r + 1][c].setValue(MazeField.FieldCode.ALLEY);
                        maze[r + 2][c].setPheromonePower(0.1);
                        maze[r + 1][c].setPheromonePower(0.1);
                        recursion(r + 2, c);
                    }
                    break;
                case 4: // Left
                    // Whether 2 cells to the left is out or not
                    if (c - 2 <= 0)
                        continue;

                    if (maze[r][c - 2].getValue() != MazeField.FieldCode.ALLEY) {
                        maze[r][c - 2].setValue(MazeField.FieldCode.ALLEY);
                        maze[r][c - 1].setValue(MazeField.FieldCode.ALLEY);
                        maze[r][c - 2].setPheromonePower(0.1);
                        maze[r][c - 1].setPheromonePower(0.1);
                        recursion(r, c - 2);
                    }
                    break;
            }
        }

    }

    /**
     * Generate an array with random directions 1-4
     *
     * @return Array containing 4 directions in random order
     */

    private int[] generateRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            randoms.add(i + 1);
        Collections.shuffle(randoms);

        int[] tab = new int[4];
        for (int i = 0; i < randoms.size(); i++) {
            tab[i] = randoms.get(i);
        }
        return tab;
    }

    private void createExits(MazeField[][] maze) {
        Boolean ok = false;

        while (!ok) {
            Random rand = new Random();

            int randomedColumn = rand.nextInt(lenghts[selection] - 10) + 2;
            randomedColumn += 2;
            if (randomedColumn != maze.length - 1) {
                if (maze[maze.length - 2][randomedColumn].getValue() == MazeField.FieldCode.ALLEY
                        && maze[maze.length - 2][randomedColumn - 1].getValue() == MazeField.FieldCode.ALLEY
                        && maze[maze.length - 2][randomedColumn + 1].getValue() == MazeField.FieldCode.ALLEY) {
                    maze[maze.length - 1][randomedColumn].setValue(MazeField.FieldCode.EXIT);
                    maze[maze.length - 1][randomedColumn - 1].setValue(MazeField.FieldCode.EXIT);
                    ok = true;
                }

            }
        }

    }

    private void updatePheromons() {
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[0].length; ++j) {
                if (maze[i][j].getValue() == MazeField.FieldCode.ANT || maze[i][j].getValue() == MazeField.FieldCode.ALLEY) {
                    maze[i][j].setPheromonePower(maze[i][j].getPheromonePower() * (1 - MazeField.EVAPORATION_COEFF));
                    if (maze[i][j].getPheromonePower() < MazeField.MIN_PHEROMONE_POWER) {
                        maze[i][j].setPheromonePower(MazeField.MIN_PHEROMONE_POWER);
                    }
                }
            }
        }
    }

}
