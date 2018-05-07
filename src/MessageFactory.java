import jade.lang.acl.ACLMessage;

public class MessageFactory {
    static ACLMessage createInformativeMessage() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology("ANTS");

        return message;
    }

    static ACLMessage createInformativeMessageMaze() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology("CHANGED_MAZE");

        return message;
    }
}
