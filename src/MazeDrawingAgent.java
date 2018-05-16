import jade.core.AID;
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

public class MazeDrawingAgent extends Agent implements SetAntCountListener, SetWallsCountListener, SetMazeSizeListener{
    private DFAgentDescription[] result;
    private MazeField[][] maze;
    private ContentDrawer drawer;
    private DFAgentDescription mazeManagerTemplate;


    public void setup() {
        drawer = new ContentDrawer();
        drawer.drawContent();
        drawer.setAntCountListener(this);
        drawer.setWallsCountListener(this);
        drawer.setMazeSizeListener(this);

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
                        System.out.println(Command.CommandCode.MAZE_REQUEST.toString());
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
                            System.out.println(Command.CommandCode.MAZE_INFORM.toString());
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
                        System.out.println(Command.CommandCode.MAZE_REQUEST.toString());
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
                            System.out.println(Command.CommandCode.MAZE_INFORM.toString());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

 /*       addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                receivedMessage = receive(MessageTemplateFactory.createInformTemplate());

                if (receivedMessage != null) {

                    try{
                        Command cmd = (Command)receivedMessage.getContentObject();

                        switch (cmd.getCommandCode())
                        {
                            case MAZE_INFORM:
                                MazeInformCommand mazeGeneratorCmd = (MazeInformCommand) receivedMessage.getContentObject();
                                maze = mazeGeneratorCmd.getMazeValues();
                                drawer.redrawMaze(maze);
                                System.out.println(Command.CommandCode.MAZE_INFORM.toString());
                        }
                    }
                    catch (Exception ex) {
                       ex.printStackTrace();
                    }

                } else {
                   block();
               }
            }
        });*/
    }

    protected void takeDown() {

    }

    @Override
    public void onAntCountChanged(int count) {
        DFAgentDescription[] antsResult;

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription ant = new ServiceDescription();
        ant.setType("ant");
        ant.setName("single_ant");//AID
        template.addServices(ant);


        try {
            antsResult = DFService.search(this, template);

            ContainerController container = getContainerController();
            String agentName;

            if (antsResult.length > count) {
                //unregister ants
                int antsToUnregister = antsResult.length - count;
                for (int j = 0; j < antsToUnregister; j++) {
                    agentName = antsResult[antsResult.length-1-j].getName().getLocalName();
                    AgentController agent = container.getAgent(agentName);
                    agent.kill();
                }
            }

            if (antsResult.length < count) {
                //register new ants
                int antsToRegister = count - antsResult.length;
                for (int j = 0; j < antsToRegister; j++) {
                    agentName = "Ant" + (antsResult.length+j);
                    AgentController agentController;
                    agentController = container.createNewAgent(agentName, "AntAgent", null);
                    agentController.start();
                }
            }

            //show found ants
            for (int i = 0; i < antsResult.length; i++) {
                AID agentID = antsResult[i].getName();
                System.out.println("FOUND ANT" + " onAntCountChanged: " + agentID + " " + antsResult.length);
                System.out.println(agentID.getLocalName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWallsCountChanged(int walls) {
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


}
