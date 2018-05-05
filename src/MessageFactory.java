import jade.lang.acl.ACLMessage;

public class MessageFactory {
    static ACLMessage createInformativeMessage() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology("ANTS");

        return message;
    }
}
