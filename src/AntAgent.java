import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AntAgent extends Agent {
    private int coordinateX, coordinateY;

    public void setup(){
        //1. ask where ant could be placed

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage message = new ACLMessage(ACLMessage.QUERY_IF);
                message.setConversationId("FirstLocationQuestion");

            }
        });


    }


}
