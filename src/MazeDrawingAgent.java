import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MazeDrawingAgent extends Agent {
    private DFAgentDescription[] result;
    private ACLMessage receivedMessage;
    private MazeField[][] maze;


    public void setup() {
        ContentDrawer drawer = new ContentDrawer();
        drawer.drawContent();


        addBehaviour(new TickerBehaviour(this, 1000) {
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

                        try
                        {
                            message.setContentObject(mazeRequest);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        send(message);
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                receivedMessage = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));

                if (receivedMessage != null) {

                    try{
                        Command cmd = (Command)receivedMessage.getContentObject();

                        switch (cmd.getCommandCode())
                        {
                            case MAZE_INFORM:
                                MazeInformCommand mazeGeneratorCmd = (MazeInformCommand) receivedMessage.getContentObject();
                                maze = mazeGeneratorCmd.getMazeValues();
                                drawer.redrawMaze(maze);
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    block();
                }
            }
        });
    }

    protected void takeDown() {
    }
}
