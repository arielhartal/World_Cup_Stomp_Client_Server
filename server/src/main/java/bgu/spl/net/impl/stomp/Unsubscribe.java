package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.net.srv.ConnectionsImp;

public class Unsubscribe extends Frame {
    private int connectionId;
    private String topic;
    private int subscriptionId;
    private String receiptId;
    private String msg;
    private FrameHandler frameHandler;


    public Unsubscribe( int connectionId, int subscriptionId, String receiptId, String msg, FrameHandler frameHandler)
    {
        this.connectionId = connectionId;
        this.subscriptionId = subscriptionId;
        this.receiptId = receiptId;
        this.msg = msg;
        this.frameHandler = frameHandler;
        execute();
    }


    @Override
    public void execute() {

        String msgToSend = "";
        NewsFeedGames newsFeedGames = NewsFeedGames.getInstance();
        
        ConnectionsImp<Object> connectionsImp = ConnectionsImp.getInstance();
        
        if(frameHandler.getSender().getMySubscriptions().size() == 0)
        {
            msgToSend = buildNotSubbedToAnythingError();
            connectionsImp.send(connectionId, msgToSend);
            return;
        }

        if(subscriptionId == -1)
        {
            msgToSend = buildNotSubbedError();
            connectionsImp.send(connectionId, msgToSend);
            return;
        }
        
        for(Object[] array : frameHandler.getSender().getMySubscriptions())
        {
            if((int) array[1] == subscriptionId)
            {
                topic = (String) array[0];                   
            }
        }
        

        ConcurrentLinkedQueue<User> subscribersByTopic = newsFeedGames.getSubscribersByTopic(topic);
        

        if(!subscribersByTopic.contains(frameHandler.getSender()))
        {
            msgToSend = buildNotSubbedError();
            connectionsImp.send(connectionId, msgToSend);
            return;
        }

        newsFeedGames.removeSubscriberFromTopic(topic, frameHandler.getSender());
        frameHandler.getSender().removeSubscription(topic, subscriptionId);
        msgToSend = buildFrameReceipt();
        System.out.println("Exited channel " + topic);
        connectionsImp.send(connectionId, msgToSend);

        

        
    }


    public String buildFrameReceipt ()
    {
        String msgToSend = "";

        msgToSend = "RECEIPT\n"
                    +"receipt-id:" + receiptId + "\n"
                    + "\n"
                    + "\u0000";
        return msgToSend;
    }  
    

    public String buildNotSubbedError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: user is not subscribed to channel"  
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
                    + "message: user is not subscribed to channel"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;
    }

    public String buildNotSubbedToAnythingError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: user is not subscribed to any channel"  
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
                    + "message: user is not subscribed to any channel"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;
    }


}


