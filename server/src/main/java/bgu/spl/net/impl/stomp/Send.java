package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.net.srv.ConnectionsImp;

public class Send extends Frame{

    private int connectionId;
    private int subscriptionId;
    private String bodyMsg;
    private String receiptId;
    private String destination;
    private String topic;
    private String msg;
    private FrameHandler frameHandler;

    public Send(String bodyMsg, int connectionId, String receiptId, String destination, String msg, FrameHandler frameHandler)
    {
        this.connectionId = connectionId;
        this.bodyMsg = bodyMsg;
        this.receiptId = receiptId;
        this.destination = destination;
        topic = destination;
        this.msg = msg;
        this.frameHandler = frameHandler;
        execute();
    }


    @Override
    public void execute() {

        String msgToSend = "";
        NewsFeedGames newsFeedGames = NewsFeedGames.getInstance();
        
        ConnectionsImp<Object> connectionsImp = ConnectionsImp.getInstance();
        ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> topicSubscribers = newsFeedGames.getTopicSubscribers();
        ConcurrentLinkedQueue<User> subscribersByTopic = newsFeedGames.getSubscribersByTopic(topic);
        
        if(subscribersByTopic == null)
        {
            msgToSend = buildNoExistsError();
            connectionsImp.send(connectionId, msgToSend);
            return;
        }

        if(!subscribersByTopic.contains(frameHandler.getSender()))
        {
            msgToSend = buildNotSubscribedError();
            connectionsImp.send(connectionId, msgToSend);
            return;
        }
    


        if(!topicSubscribers.containsKey(destination))
        {
            msgToSend = buildNoExistsError();
            connectionsImp.send(connectionId, msgToSend);
            return;
        }

        msgToSend = buildMessageFrame();
        connectionsImp.send(destination, msgToSend);

        
    }



    public String buildMessageFrame ()
    {
        String msgToSend = "";

        msgToSend = "MESSAGE\n"
                    + "subscription:" + subscriptionId + "\n"
                    + "message-id:" + "\n"
                    + "destination:" + destination + "\n"
                    + bodyMsg + "\n"
                    + "\u0000";
        return msgToSend;
    }

    public String buildNotSubscribedError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: not subscribed to the channel: " + topic  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
            
            return msgToSend;
        }

        msgToSend = "ERROR\n"
                        + "receipt-id: " + receiptId + "\n"
                        + "message: not subscribed to the channel: " + topic  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";

        return msgToSend;
    }

    public String buildNoExistsError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "receipt-id: " + receiptId + "\n"
                        + "message: the channel: " + topic + " doesn't exists"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
            return msgToSend;
        }

        msgToSend = "ERROR\n"
                    + "message: the channel: " + topic + " doesn't exists"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;
    }
    
    public String buildNoSubsError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "receipt-id: " + receiptId + "\n"
                        + "message: the channel has no subs"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
            return msgToSend;
        }

        msgToSend = "ERROR\n"
                    + "message: the channel has no subs"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;
    }

}

    

