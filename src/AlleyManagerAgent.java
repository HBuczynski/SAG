import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AlleyManagerAgent extends Agent{

    private DFAgentDescription[] result;
    private ACLMessage receivedInformMessage;
    private ACLMessage receivedRequestMessage;
    private PossibleValues[][] maze;

    public void setup() {

        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {

                updatePheromonsValues();


                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("alley");
                template.addServices(sd);

                try {
                    result = DFService.search(AlleyManagerAgent.this, template);
                    for (DFAgentDescription agent : result) {
                        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        message.addReceiver(agent.getName());
                        message.setContent(Commands.CommandCode.MAZE_GENERATOR_REQUEST.toString());
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
                receivedInformMessage = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));

                if (receivedInformMessage != null) {

                    try {
                        Commands cmd = (Commands) receivedInformMessage.getContentObject();

                        switch (cmd.getCommandCode()) {
                            case ANT_POSITION:
                                AntPositionInformCommand alleyRequestCommand = (AntPositionInformCommand) receivedInformMessage.getContentObject();

                                // TO DO
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
                receivedRequestMessage = receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

                if (receivedRequestMessage != null) {

                    try {
                        Commands cmd = (Commands) receivedRequestMessage.getContentObject();

                        switch (cmd.getCommandCode()) {
                            case ALLEY_REQUEST:
                                AlleyRequestCommand alleyRequestCommand = (AlleyRequestCommand) receivedRequestMessage.getContentObject();

                                // TO DO

                            case MAZE_GENERATOR_REQUEST:
                                MazeGeneratorRequestCommand mazeGeneratorCmd = (MazeGeneratorRequestCommand)receivedRequestMessage.getContentObject();
                                maze = mazeGeneratorCmd.getMazeValues();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void updatePheromonsValues()
    {

    }

    protected void takeDown() {
    }
}
