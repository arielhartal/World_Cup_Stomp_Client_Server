package bgu.spl.net.impl.stomp;


import bgu.spl.net.srv.ConnectionsImp;

public class Subscribe extends Frame{

    private int connectionId;
    private String topic;
    private int subscriptionId;
    private String receiptId;
    private String msg;
    private FrameHandler frameHandler;

    public Subscribe(String topic, int connectionId, int subscriptionId, String receiptId, String msg, FrameHandler frameHandler)
    {
        this.topic = topic;
        this.connectionId = connectionId;
        this.subscriptionId = subscriptionId;
        this.frameHandler = frameHandler;
        this.receiptId = receiptId;
        this.msg = msg;
        execute();
    }

    

    @Override
    public void execute() {
        String msgToSend = "";
        NewsFeedGames newsFeedGames = NewsFeedGames.getInstance();
        
        ConnectionsImp<Object> connectionsImp = ConnectionsImp.getInstance();

        newsFeedGames.addSubscriberToTopic(topic, frameHandler.getSender());
        frameHandler.getSender().addSubscription(topic, subscriptionId);
        msgToSend = buildFrameReceipt();
        System.out.println("Joined channel " + topic);
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
    
    public String buildAlreadySubbedError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: already subscribed to channel"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
            return msgToSend;
        }

        msgToSend = "ERROR\n"
                    + "receipt-id: " + receiptId 
                    + "\n"
                    + "message: already subscribed to channel"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;
    }
    
}
