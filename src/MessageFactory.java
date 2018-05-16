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

    static ACLMessage createInformativeMessageWalls() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology("DYNAMIC_WALLS_NUMBER");

        return message;
    }

    static ACLMessage createInformativeMessageAntExit() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology("MAZE_CHANGED_ANT_EXIT");

        return message;
    }

    static ACLMessage createInformativeMessageWallsValue(){

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setOntology("LAST_WALLS_VALUE");

        return message;
    }
}
