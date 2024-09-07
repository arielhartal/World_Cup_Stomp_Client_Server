package bgu.spl.net.impl.stomp;


import bgu.spl.net.srv.ConnectionsImp;

public class FrameHandler{

    private int connectionId;
    private User sender;
    private ConnectionsImp<Object> connectionsImp;




    public void process(Object message) {
        connectionsImp = ConnectionsImp.getInstance();
        String msg = message.toString();
        String[] lines = msg.split("\n");
        String[] parts = lines[0].split(" ");
        String command = parts[0];
        System.out.println(msg);
        switch(command){
            case("CONNECT"):
                handleConnect(msg);
                break;
            case("SEND"):
                handleSend(msg);
                break;
            case("SUBSCRIBE"):
                handleSubscribe(msg);
                break;
            case("UNSUBSCRIBE"):
                handleUnsubscribe(msg);
                break;
            case("DISCONNECT"):
                handleDisconnect(msg);
                break;
            
        
        }

        
    }

    

    private void handleDisconnect(String msg) {
        String[] lines = msg.split("\n");
        String[] parts = lines[0].split(" ");
        String receiptId = "-1";
        Frame frm;

        for(int i = 1; i < lines.length; i++)
        {
            parts = lines[i].split(":");

            String key = parts[0];
            String value = parts[1];

            switch(key)
            {
                case("receipt"):
                    receiptId = value;
                    break;    
                                
            }
        }

        if(receiptId == "-1")
        {
            handleMalFormedFrameError(msg, "receipt" ,receiptId);
            return;
        }
        frm = new Disconnect(sender, connectionId, receiptId, msg);
        return;

        
    }

    

    private void handleUnsubscribe(String msg) {
        String[] lines = msg.split("\n");
        String[] parts = lines[0].split(" ");
        int subscriptionId = -2;
        String receiptId = "-1";
        Frame frm;

        for(int i = 1; i < lines.length; i++)
        {
            parts = lines[i].split(":");

            String key = parts[0];
            String value = parts[1];

            switch(key)
            {

                case("id"):
                    subscriptionId = Integer.parseInt(value);
                    break;
                
                case("receipt"):
                    receiptId = value;
                    break;    
                    
            }

            
        }
         if(subscriptionId == -2)
        {
            handleMalFormedFrameError(msg, "subscription" ,receiptId);
            return;
        }

        frm = new Unsubscribe(connectionId, subscriptionId, receiptId, msg, this);
        return;
                
        
    }


    private void handleSubscribe(String msg) {
        String[] lines = msg.split("\n");
        String[] parts = lines[0].split(" ");
        String destination = "";
        int subscriptionId = -1;
        String receiptId = "-1";
        Frame frm;
        for(int i = 1; i < lines.length; i++)
        {
            parts = lines[i].split(":");
            String key = parts[0];
            String value = parts[1];
            

            switch(key)
            {
                case("destination"):
                    destination = value.substring(1);
                    break;

                case("id"):
                    subscriptionId = Integer.parseInt(value);
                    break;
                
                case("receipt"):
                    receiptId = value;
                    break;    
            }
        }

        if(destination == "")
        {
            handleMalFormedFrameError(msg, "destination" ,receiptId);
            return;
        }

        if(subscriptionId == -1)
        {
            handleMalFormedFrameError(msg, "subscription" ,receiptId);
            return;
        }


        frm = new Subscribe(destination, connectionId, subscriptionId, receiptId, msg, this);
        return;
                                  
        
    }

   

    private void handleSend(String msg) {
        String[] lines = msg.split("\n");
        String[] parts = lines[0].split(" ");
        String destination = "";
        String receiptId = "-1";
        String bodyMsg = "";
        Frame frm;

        int emptyLine = -1;
        boolean foundLine = false;

        for(int i = 1; i < lines.length && !foundLine; i++)
        {
            if(lines[i].equals(""))
            {
              emptyLine = i;  
              foundLine = true;
              break;
            }

            parts = lines[i].split(":");
            String key = parts[0];
            String value = parts[1];

            

            switch(key)
            {
                case("destination"):
                    destination = value.substring(1);
                    break;
                
                case("receipt"):
                    receiptId = value;
                    break;    

                
            }
        }

        boolean lastLine = false;

        for(int j = emptyLine; j < lines.length && !lastLine; j++)
        {
            if(lines[j] == "\u0000")
            {
                lastLine = true;
            }

            bodyMsg += lines[j] + "\n";
        }


        if(destination == "")
        {
            handleMalFormedFrameError(msg, "destination" ,receiptId);
            return;
        }

        frm = new Send(bodyMsg, connectionId, receiptId, destination, msg, this);
        return;
    }

    private void handleConnect(String msg) {
        String[] lines = msg.split("\n");
        String[] parts = lines[0].split(" ");
        String login = "";
        String passcode = "";
        String receiptId = "-1";
        String version = "";
        String host = "";
        Frame frm;
        for(int i = 1; i < lines.length; i++)
        {
            parts = lines[i].split(":");

            String key = parts[0];
            String value = parts[1];

            switch(key)
            {
                case("accept-version"):
                     if(!value.equals("1.2"))
                    {
                        handleWrongAcceptVersionError(msg, receiptId);
                        return;
                    }
                    version = value;
                    break;

                case("host"):
                    if(!value.equals("stomp.cs.bgu.ac.il"))
                    {
                        handleWrongHostError(msg, receiptId);
                        return;
                    }
                    host = value;
                    break;

                case("login"):
                    
                    login = value;
                    break;

                case("passcode"):
                    passcode = value;
                    break;
                
                case("receipt"):
                    receiptId = value;
                    break;    
            }
        }
        if(login == "")
        {
            handleMalFormedFrameError(msg, "login" ,receiptId);
            return;
        }

        if(passcode == "")
        {
            handleMalFormedFrameError(msg, "passcode" ,receiptId);
            return;
        }

        if(host == "")
        {
            handleMalFormedFrameError(msg, "host" ,receiptId);
            return;
        }

        if(version == "")
        {
            handleMalFormedFrameError(msg, "accept-version" ,receiptId);
            return;
        }

        frm = new Connect(login, passcode, connectionId, receiptId, msg, this);
        return;               
        
    }


    private void handleMalFormedFrameError(String msg, String header, String receiptId) {

        String msgToSend = "";
        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: malformed frame received"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "Did not contain a " + header + " header,\n"
                        + "which is REQUIRED for message propagation."
                        + "\u0000";
        }
        else
        {
            msgToSend = "ERROR\n"
                        + "receipt-id: " + receiptId
                        + "message: malformed frame received"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "Did not contain a " + header + " header,\n"
                        + "which is REQUIRED for message propagation."
                        + "\u0000";
        }

        connectionsImp.send(connectionId, msgToSend);


    }

   


    private void handleWrongHostError(String msg, String receiptId) {
        String msgToSend = "";
        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: invalid host"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
        }
        else
        {
            msgToSend = "ERROR\n"
                        + "receipt-id: " + receiptId
                        + "message: invalid host"   
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
        }

        connectionsImp.send(connectionId, msgToSend);
    }

    private void handleWrongAcceptVersionError(String msg, String receiptId) {
        String msgToSend = "";
        if(receiptId == "-1")
        {
            msgToSend = "ERROR\n"
                        + "message: accept-version invalid"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
        }
        else
        {
            msgToSend = "ERROR\n"
                        + "receipt-id: " + receiptId
                        + "message: accept-version invaild"  
                        + "\n"
                        + "The message: \n"
                        + "-----\n"
                        + msg + "\n"
                        + "-----\n"
                        + "\u0000";
        }

        connectionsImp.send(connectionId, msgToSend);
    }

    public void setSender(User sender)
    {
        this.sender = sender;
    }

    public User getSender()
    {
        return this.sender;
    }

    public void setConnectionId(int connectionId)
    {
        this.connectionId = connectionId;
    }




}