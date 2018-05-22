import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import javax.swing.*;

public class MazeDrawingAgent extends Agent implements SetAntCountListener, SetWallsCountListener, SetMazeSizeListener, SetEvaporationCoeffListener{
    private DFAgentDescription[] result;
    private MazeField[][] maze;
    private ContentDrawer drawer;
    private DFAgentDescription mazeManagerTemplate;
    private int exitAntCounter;
    private int otherAnts;

    private int walls;
    private JLabel countedAndOut;
    private JLabel countedAndExists;

    private OnExitAntCounterChanged onExitAntCounterListener;

    public void setup() {
        drawer = new ContentDrawer();
        drawer.drawContent();
        drawer.setAntCountListener(this);
        drawer.setWallsCountListener(this);
        drawer.setEvaporationCoeffListener(this);
        drawer.setMazeSizeListener(this);
        drawer.setDrawingAgent(this);


        exitAntCounter = 0;
        otherAnts = 0;

        mazeManagerTemplate = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("maze");
        mazeManagerTemplate.addServices(sd);

        addBehaviour(new TickerBehaviour(this, 200) {
            @Override
            protected void onTick() {
                try {
                    result = DFService.search(MazeDrawingAgent.this, mazeManagerTemplate);

                    Command mazeRequest = new Command(Command.CommandCode.MAZE_REQUEST);

                    for (DFAgentDescription agent : result) {
                        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        message.addReceiver(agent.getName());

                        try {
                            message.setContentObject(mazeRequest);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        //System.out.println(Command.CommandCode.MAZE_REQUEST.toString());
                        send(message);
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }

                ACLMessage receivedMessage = blockingReceive(MessageTemplateFactory.createInformTemplate());
                try {
                    Command cmd = (Command) receivedMessage.getContentObject();

                    switch (cmd.getCommandCode()) {
                        case MAZE_INFORM:
                            MazeInformCommand mazeGeneratorCmd = (MazeInformCommand) receivedMessage.getContentObject();
                            maze = mazeGeneratorCmd.getMazeValues();
                            drawer.redrawMaze(maze,mazeGeneratorCmd.getRectDim());
                            //System.out.println(Command.CommandCode.MAZE_INFORM.toString());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    result = DFService.search(MazeDrawingAgent.this, mazeManagerTemplate);

                    Command mazeRequest = new Command(Command.CommandCode.MAZE_REQUEST);

                    for (DFAgentDescription agent : result) {
                        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        message.addReceiver(agent.getName());

                        try {
                            message.setContentObject(mazeRequest);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        //System.out.println(Command.CommandCode.MAZE_REQUEST.toString());
                        send(message);
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }

                ACLMessage receivedMessage = blockingReceive(MessageTemplateFactory.createInformTemplate());
                try {
                    Command cmd = (Command) receivedMessage.getContentObject();

                    switch (cmd.getCommandCode()) {
                        case MAZE_INFORM:
                            MazeInformCommand mazeGeneratorCmd = (MazeInformCommand) receivedMessage.getContentObject();
                            maze = mazeGeneratorCmd.getMazeValues();
                            drawer.redrawMaze(maze,mazeGeneratorCmd.getRectDim());
                            //System.out.println(Command.CommandCode.MAZE_INFORM.toString());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //set labels with an informations about added / removed walls
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

                ACLMessage receivedMessage = receive(MessageTemplateFactory.createInformTemplateWallsValue());

                try{
                    if (receivedMessage != null) {
                        Command cmd = (Command) receivedMessage.getContentObject();
                        switch (cmd.getCommandCode()) {
                            case CURRENT_WALLS_INFORM: {
                                CurrentWallsInformCommand command = (CurrentWallsInformCommand) receivedMessage.getContentObject();

                                int currentWallsValue = command.getWallsCount();
                                int diff;
                                if(walls>currentWallsValue){
                                    diff = walls-currentWallsValue;
                                    countedAndOut.setText("Dodano " +diff+ " ruchomych ścianek.");
                                    countedAndExists.setText("W labiryncie jest " + walls + " ruchomych ścianek.");
                                } else if (walls<currentWallsValue){
                                    diff = currentWallsValue - walls;
                                    countedAndOut.setText("Usunięto " +diff+ " ruchomych ścianek.");
                                    countedAndExists.setText("W labiryncie jest " + walls + " ruchomych ścianek.");
                                } else {
                                    countedAndExists.setText("");
                                    countedAndOut.setText("");
                                }
                                break;
                            }
                        }
                    }
                }catch  (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Ant Exit
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMessage = receive(MessageTemplateFactory.createInformTemplateMaze());

                if (receivedMessage != null) {
                    String command = receivedMessage.getContent();

                    switch (command) {
                        case "MAZE_CHANGED_ANT_EXIT": {
                            exitAntCounter++;
                            otherAnts--;

                           onExitAntCounterListener.onExitAntValueChanged(exitAntCounter,otherAnts);
                            break;
                        }

                    }
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMessage = receive();

                if (receivedMessage != null) {
                    String command = receivedMessage.getContent();

                    switch (command) {
                        case "MAZE_CHANGED_ANT_EXIT": {
                            exitAntCounter++;
                            otherAnts--;
                          onExitAntCounterListener.onExitAntValueChanged(exitAntCounter,otherAnts);
                            break;
                        }

                    }
                }
            }
        });
    }

    protected void takeDown() {

    }

    @Override
    public void onAntCountChanged(int count, JLabel countedAndOut, JLabel countedAndExists) {
        DFAgentDescription[] antsResult;

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription ant = new ServiceDescription();
        ant.setType("ant");
        ant.setName("single_ant");//AID
        template.addServices(ant);

       // otherAnts = count;
       // exitAntCounter = 0;

        try {
            antsResult = DFService.search(this, template);

            ContainerController container = getContainerController();
            String agentName;

            if (antsResult.length > count) {
                //unregister ants
//                otherAnts = count;
//                countedAndExists.setText("W labiryncie jest " + otherAnts + " mrówek.");
//
//                exitAntCounter = antsResult.length - count;
//                countedAndOut.setText("Usunięto " + exitAntCounter + " mrówek.");

                int antsToUnregister = antsResult.length - count;
                for (int j = 0; j < antsToUnregister; j++) {
                    agentName = antsResult[antsResult.length-1-j].getName().getLocalName();
                    AgentController agent = container.getAgent(agentName);

                    agent.kill();
                }
            }

            if (antsResult.length < count) {
                //register new ants
//                otherAnts = count;
//                countedAndExists.setText("W labiryncie jest " + otherAnts + " mrówek.");
//                exitAntCounter = count-antsResult.length;
//                countedAndOut.setText("Dodano " + exitAntCounter + " mrówek.");

                int antsToRegister = count - antsResult.length;
                for (int j = 0; j < antsToRegister; j++) {
                    agentName = "Ant" + (antsResult.length+j);
                    AgentController agentController;
                    agentController = container.createNewAgent(agentName, "AntAgent", null);
                    agentController.start();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWallsCountChanged(int walls, JLabel countedAndOut, JLabel countedAndExists) {
        this.walls = walls;
        this.countedAndExists = countedAndExists;
        this.countedAndOut = countedAndOut;

        try {
            result = DFService.search(MazeDrawingAgent.this, mazeManagerTemplate);
            for (DFAgentDescription agent : result) {

                ACLMessage message = MessageFactory.createInformativeMessageWalls();
                message.addReceiver(agent.getName());

                System.out.println("Wysylam scianki");

                Command dynamicWallsInformCommand = new DynamicWallsInformCommand(walls);
                try {
                    message.setContentObject(dynamicWallsInformCommand);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                send(message);
                System.out.println(dynamicWallsInformCommand.getCommandCode().toString());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMazeSizeUp() {
        try {
            result = DFService.search(MazeDrawingAgent.this, mazeManagerTemplate);
            for (DFAgentDescription agent : result) {
                ACLMessage message = MessageFactory.createInformativeMessageMaze();
                message.addReceiver(agent.getName());

                MazeChangedInformCommand mazeChangedInformCommand = new MazeChangedInformCommand(Command.CommandCode.MAZE_CHANGED_UP_INFORM);
                message.setContent(mazeChangedInformCommand.getCommandCode().toString());
                send(message);
                System.out.println(mazeChangedInformCommand.getCommandCode().toString());

            }

        } catch (FIPAException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMazeSizeDown() {
        try {
            result = DFService.search(MazeDrawingAgent.this, mazeManagerTemplate);
            for (DFAgentDescription agent : result) {
                ACLMessage message = MessageFactory.createInformativeMessageMaze();
                message.addReceiver(agent.getName());

                MazeChangedInformCommand mazeChangedInformCommand = new MazeChangedInformCommand(Command.CommandCode.MAZE_CHANGED_DOWN_INFORM);
                message.setContent(mazeChangedInformCommand.getCommandCode().toString());
                send(message);
                System.out.println(mazeChangedInformCommand.getCommandCode().toString());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEvaporationCoeffChange(double coeff) {
        if(coeff > 0.0 && coeff < 1.0) {
            MazeField.EVAPORATION_COEFF = coeff;
        }
        System.out.println(MazeField.EVAPORATION_COEFF);
    }

    public OnExitAntCounterChanged getOnExitAntCounterListener() {
        return onExitAntCounterListener;
    }

    public void setOnExitAntCounterListener(OnExitAntCounterChanged onExitAntCounterListener) {
        this.onExitAntCounterListener = onExitAntCounterListener;
    }
}
