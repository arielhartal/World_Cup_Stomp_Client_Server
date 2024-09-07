package bgu.spl.net.impl.stomp;

import bgu.spl.net.srv.ConnectionsImp;

import java.util.concurrent.ConcurrentHashMap;


public class Connect extends Frame {
    
    private String login;
    private String passcode;
    private User sender;
    private int connectionId;
    private String receiptId;
    private FrameHandler frameHandler;
    private String msg;


    public Connect(String login, String passcode, int connectionId, String receiptId, String msg, FrameHandler frameHandler)
    {
        this.login = login;
        this.passcode = passcode;
        this.frameHandler = frameHandler;
        this.connectionId = connectionId;
        this.receiptId = receiptId;
        this.msg = msg;
        execute();
    }


    @Override
    public void execute() {


        ConnectionsImp<Object> connectionsImp = ConnectionsImp.getInstance();
        ConcurrentHashMap <String, User> users = allUsers.getInstance().getUsers();
        String msgToSend = "";        


        
        if(!allUsers.getInstance().isUserRegistered(login))
        {
            sender = new User(login, passcode);
            allUsers.getInstance().addUser(sender);
        }

        else
        {
            sender = users.get(login);

            if(!allUsers.getInstance().authenticatePassword(login, passcode))
            {
                msgToSend = buildWrongPasswordError(); 
                connectionsImp.send(connectionId, msgToSend);
                return;
            }
            

            if(sender.getStatus() == User.Status.LOGIN)
            {
                msgToSend = buildAlreadyLoggedInError();
                connectionsImp.send(connectionId, msgToSend);
                return;
            }
            
        }

        frameHandler.setSender(sender);
        sender.setStatus(User.Status.LOGIN);
        sender.setConnectionId(connectionId);
        msgToSend = buildConnectedReceipt();
        connectionsImp.send(connectionId, msgToSend);



    }

    public String buildConnectedReceipt ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "CONNECTED\n"
                        +"version:1.2\n"
                        + "\n"
                        + "\u0000";
            return msgToSend;
        }

        msgToSend = "CONNECTED\n"
                    +"receipt-id:" + receiptId + "\n" 
                    +"version:1.2\n"
                    + "\n"
                    + "\u0000";
        return msgToSend;
    }
    
    
    public String buildWrongPasswordError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: wrong password"  
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
                    + "message: wrong password"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;
    }
    
    public String buildAlreadyLoggedInError ()
    {
        String msgToSend = "";

        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: already logged in"  
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
                    + "message: already logged in"  
                    + "\n"
                    + "The message: \n"
                    + "-----\n"
                    + msg + "\n"
                    + "-----\n"
                    + "\u0000";
        return msgToSend;

    } 
}
