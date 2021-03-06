import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.awt.Point;

public class AntAgent extends Agent {
    private DFAgentDescription[] result;
    private int coordinateX, coordinateY, previousCoordinateX, previousCoordinateY, distance;
    private AID aid;
    private AMSAgentDescription description;

    private String mazeManagerId;

    public void setCoordinateX(int coordX) {
        previousCoordinateX = coordinateX;
        coordinateX = coordX;
    }

    public void setCoordinateY(int coordY) {
        previousCoordinateY = coordinateY;
        coordinateY = coordY;
    }

    public void setDistance(int dist) {
        distance = dist;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public int getPreviousCoordinateX() {
        return previousCoordinateX;
    }

    public int getPreviousCoordinateY() {
        return previousCoordinateY;
    }

    public int getDistance() {
        return distance;
    }



    public void setup(){
        //0. register ant to make ants counting possible
        aid = new AID();

        DFAgentDescription antDescription = new DFAgentDescription();
        antDescription.setName(getAID());

        ServiceDescription ant = new ServiceDescription();
        ant.setType("ant");
        ant.setName("single_ant");
        antDescription.addServices(ant);

        try {
            DFService.register(this, antDescription);
        }

        catch (FIPAException ex) {
            System.out.println(ex.getMessage());
        }

        //1. ask where ant could be placed
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
               // ACLMessage message = new ACLMessage(ACLMessage.QUERY_IF);
               // message.setConversationId("FirstLocationQuestion");
                setCoordinateX(1);
                setCoordinateY(1);
                setDistance(1);
                //System.out.println("Ant placed");
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

                    AntNeighbourhoodRequestCommand antRequest = new AntNeighbourhoodRequestCommand();
                    Point position = new Point();
                    position.x = getCoordinateX();
                    position.y = getCoordinateY();
                    antRequest.setCurrentPosition(position);
                    for (DFAgentDescription agent : result) {
                        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                        message.addReceiver(agent.getName());
                        try {
                            message.setContentObject(antRequest);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        //System.out.println(Command.CommandCode.ANT_NEIGHBORHOOD_REQUEST.toString());
                        send(message);
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
                ACLMessage receivedMessage = blockingReceive(MessageTemplateFactory.createInformTemplate());
                AntPositionInformCommand positionInformCommand = new AntPositionInformCommand();
                try {
                    Command cmd = (Command) receivedMessage.getContentObject();
                    ACLMessage message = MessageFactory.createInformativeMessage();
                    message.addReceiver(receivedMessage.getSender());

                    switch (cmd.getCommandCode()) {
                        case ANT_NEIGHBORHOOD_INFORM:
                            AntNeighbourhoodInformCommand neighbourhoodInformCommand = (AntNeighbourhoodInformCommand) cmd;
                            Vector<MazeField> maze = neighbourhoodInformCommand.getMazeValues();
                            boolean isExit = setNextMove(maze);
                            if (isExit) {
                                doDelete();
                                return;
                            }
                            Point newPosition = new Point();
                            newPosition.x = getCoordinateX();
                            newPosition.y = getCoordinateY();
                            positionInformCommand.setNewPosition(newPosition);
                            positionInformCommand.setDistance(getDistance());
                            Point oldPosition = new Point();
                            oldPosition.x = getPreviousCoordinateX();
                            oldPosition.y = getPreviousCoordinateY();
                            positionInformCommand.setOldPosition(oldPosition);
                            try {
                                message.setContentObject(positionInformCommand);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                           // System.out.println(Command.CommandCode.ANT_POSITION_INFORM.toString());
                            send(message);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    protected void takeDown() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("maze");
        template.addServices(sd);

        try {
            result = DFService.search(AntAgent.this, template);

            for (DFAgentDescription agent : result) {
                ACLMessage message = MessageFactory.createInformativeMessage();
                message.addReceiver(agent.getName());
                try {
                    AntPositionInformCommand positionInformCommand = new AntPositionInformCommand(getCoordinateX(),getCoordinateY());
                    message.setContentObject(positionInformCommand);
                    send(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (FIPAException e) {
            e.printStackTrace();
        }

        //deregister ant
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public boolean setNextMove(Vector<MazeField> maze){
        for(int i = 0; i < maze.size(); ++i){
            if(maze.get(i).getValue() == MazeField.FieldCode.EXIT){
                return true;
            }
        }
        double sumPheromonePower = 0;
        double [] distribution  = new double[maze.size()];
        for(int i = 0; i < maze.size(); ++i){
            if(maze.get(i).getCoordinateX() != getPreviousCoordinateX() || maze.get(i).getCoordinateY() != getPreviousCoordinateY()){
                sumPheromonePower += maze.get(i).getPheromonePower();
                if(i != 0)
                    distribution[i] = distribution[i-1] + maze.get(i).getPheromonePower();
                else
                    distribution[i] = maze.get(i).getPheromonePower();
            }
            else{
                distribution[i] = 0;
            }
        }
        for(int i = 0; i < distribution.length; ++i){
            distribution[i] /= sumPheromonePower;
        }
        Random generator = new Random();
        double random = generator.nextDouble();
        for(int i = 0; i < distribution.length; ++i){
            if(random < distribution[i]) {
                makeMove(maze.get(i));
                return false;
            }
        }
        setCoordinateX(getCoordinateX());
        setCoordinateY(getCoordinateY());
        ++distance;
        return false;
    }

    public void makeMove(MazeField field){
        setCoordinateY(field.getCoordinateY());
        setCoordinateX(field.getCoordinateX());
        ++distance;
    }
}
