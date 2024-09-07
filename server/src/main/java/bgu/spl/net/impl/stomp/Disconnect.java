package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImp;

public class Disconnect extends Frame {

    private User user;
    private String receiptId;
    private String msg;

    public Disconnect(User user, int connectionId, String receiptId, String msg)
    {
        this.connectionId = connectionId;
        this.user = user;
        this.receiptId = receiptId;
        this.msg = msg;
        execute();
    }


    @Override
    public void execute() {
        ConnectionsImp<Object> connectionsImp = ConnectionsImp.getInstance();
        String msgToSend = "";
        

        if(user.getMySubscriptions().size() != 0)
        {
            NewsFeedGames newsFeedGames = NewsFeedGames.getInstance();

            for(Object[] channel : user.getMySubscriptions())
            {
                newsFeedGames.removeSubscriberFromTopic((String) channel[0], user);
            }
        }

        if(user.getStatus() != User.Status.LOGIN || user == null)
        {
            msgToSend = buildNotConnectedError();
            connectionsImp.send(connectionId, msgToSend);
        }

        else
        {
            user.setStatus(User.Status.LOGOUT);
            msgToSend = buildFrameReceipt();
            connectionsImp.send(connectionId, msgToSend);
            connectionsImp.disconnect(connectionId);
        }
        
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
    
    
    
    public String buildNotConnectedError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: user already logged off"  
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
                    + "message: user already logged off"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;
    } 
    
}
