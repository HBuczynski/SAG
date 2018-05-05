import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class MazeDrawingAgent extends Agent implements SetAntCountListener {
    private DFAgentDescription[] result;
    private DFAgentDescription[] antsResult;
    private MazeField[][] maze;
    private int antNumber = 0;


    public void setup() {
        ContentDrawer drawer = new ContentDrawer();
        drawer.drawContent();
        drawer.setAntCountListener(this);

        addBehaviour(new TickerBehaviour(this, 200) {
            @Override
            protected void onTick() {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("maze");
                template.addServices(sd);

                try {
                    result = DFService.search(MazeDrawingAgent.this, template);

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
                            drawer.redrawMaze(maze);
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

                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("maze");
                template.addServices(sd);

                try {
                    result = DFService.search(MazeDrawingAgent.this, template);

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
                            drawer.redrawMaze(maze);
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
                for (int j = 0; j < (antsResult.length - count); j++) {
                    agentName = antsResult[j].getName().getLocalName();
                    AgentController agent = container.getAgent(agentName);
                    System.out.println("Got agent " + agentName);
                    agent.kill();
                }
            } else if (antsResult.length < count) {
                //register new ants
                for (int j = 0; j < (count - antsResult.length); j++) {
                    agentName = "Ant" + antNumber;
                    antNumber++;
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


}
