import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MessageTemplateFactory {
    static MessageTemplate createInformTemplate() {
        return MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchOntology("ANTS")
        );
    }

    static MessageTemplate createInformTemplateMaze() {
        return MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchOntology("CHANGED_MAZE")
        );
    }
}
