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
    private PossibleValues[][] maze;


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
                    for (DFAgentDescription agent : result) {
                        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        message.addReceiver(agent.getName());
                        message.setContent("maze_request");
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
                receivedMessage = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

                if (receivedMessage != null) {
                    maze = getMazeFromString(receivedMessage.getContent());
                   drawer.redrawMaze(maze);
                } else {
                    block();
                }
            }
        });
    }

    protected void takeDown() {
    }

    private PossibleValues[][] getMazeFromString(String mazeString) {
        PossibleValues[][] mazeV;
        String[] lines = mazeString.split("\n");
        mazeV = new PossibleValues[lines.length][lines.length];
        for (int i = 0; i < lines.length; i++) {
            //i-line
            String[] line = lines[i].split("\t");
            for (int j = 0; j < line.length; j++) {
                mazeV[i][j] = PossibleValues.valueOf(line[j]);
            }
        }
        return mazeV;
    }

}
