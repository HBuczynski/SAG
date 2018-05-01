import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.*;

public class AntAgent extends Agent {
    private int coordinateX, coordinateY, distance;

    private string mazeManagerId;

    public void setCoordinateX(int coordX) {
        coordinateX = coordX;
    }

    public void setCoordinateY(int coordY) {
        coordinateY = coordY;
    }

    public void setDistance(int dist) {
        distance = dist;
    }

    public int getCoordinateX() {
        return coordX;
    }

    public int getCoordinateY() {
        return coordY;
    }

    public int getDistance() {
        return distance;
    }



    public void setup(){
        //1. ask where ant could be placed

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
               // ACLMessage message = new ACLMessage(ACLMessage.QUERY_IF);
               // message.setConversationId("FirstLocationQuestion");
                setCoordinateX(1);
                setCoordinateY(1);
                System.out.println("Ant placed");
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
                    result = DFService.search(AntAgent.this, template);

                    Command antRequest = new Command(Command.CommandCode.ANT_NEIGHBOURHOOD_REQUEST);

                    for (DFAgentDescription agent : result) {
                        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        message.addReceiver(agent.getName());
                        try
                        {
                            message.setContentObject(antRequest);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        System.out.println(Command.CommandCode.ANT_NEIGHBOURHOOD_REQUEST.toString());
                        send(message);
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
                ACLMessage receivedMessage = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                AntPositionInformCommand positionInformCommand = new AntPositionInformCommand();
                if (receivedMessage != null) {

                    try {
                        Command cmd = (Command) receivedMessage.getContentObject();

                        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                        message.addReceiver(receivedMessage.getSender());

                        switch (cmd.getCommandCode()) {
                            case ANT_NEIGHBOURHOOD_INFORM:
                                MazeField[] maze = cmd.getMazeValues();
                                boolean isExit = setNextMove(maze);
                                Point newPosition;
                                newPosition.x = this.coordinateX;
                                newPosition.y = this.coordinateY;
                                positionInformCommand.setNewPosition(newPosition);
                                positionInformCommand.setDistance(this.distance);

                                try
                                {
                                    message.setContentObject(positionInformCommand);
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                System.out.println(Command.CommandCode.ANT_POSITION_INFORM.toString());
                                send(message);
                                if(isExit){
                                    return;
                                }
                                break;
                        }
                    }
                }
                else{
                    block();
                }
            });

        }


    }
    public boolean setNextMove(MazeField[] maze){
        for(int i = 0; i < maze.length(); ++i){
            if(maze[i].getValue() == MazeField.FieldCode.EXIT){
                makeMove(i);
                return;
            }
        }
        double sumPheromonePower = 0;
        double [] distribution  = new double[maze.length()];
        for(int i = 0; i < maze.length(); ++i){
            sumPheromonePower+=maze[i].getPheromonePower();
            if(i != 0)
                distribution[i] = distribution[i-1] + maze[i].getPheromonePower();
            else
                distribution[i] = maze[i].getPheromonePower();
        }
        for(element:distribution){
            element = element/sumPheromonePower;
        }
        Random generator = new Random();
        double random = nextDouble();
        int chosenIndex = maze.length()-1;
        for(int i = 0; i < distribution.length() - 1; ++i){
            if(random < distribution[i] && distribution[i] != distribution[i+1]) {
                makeMove(i);
                break;
            }
        }
    }

    public void makeMove(int fieldIndex){
        switch (fieldIndex){
            case 0:
                this.coordinateX += 1;
                break;
            case 1:
                this.coordinateY += 1;
                break;
            case 2:
                this.coordinateX -= 1;
                break;
            case 3:
                this.coordinateY -= 1;
                break;
        }
        ++this.distance;
    }
}
