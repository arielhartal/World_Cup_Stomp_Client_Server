package bgu.spl.net.impl.stomp;

import java.util.LinkedList;


public class User {

    public enum Status {NULL, REGISTER, LOGIN, LOGOUT}


    private String login;
    private String passcode;
    private int connectionId;
    private Status status;
    private LinkedList<Object[]> subscriptions; 



    public User (String login, String passcode)
    {
        this.login = login;
        this.status = Status.REGISTER;
        this.passcode = passcode;
        this.subscriptions = new LinkedList<>();

    }


    
    public LinkedList<Object[]> getMySubscriptions()
    {
        return subscriptions;
    }

    public void addSubscription(String topic, int id)
    {
        Object [] subscription = new Object [2];
        subscription[0] = topic;
        subscription[1] = id;
        subscriptions.add(subscription);
    }

    public void removeSubscription(String topic, int id)
    {
        Object [] subscription = new Object [2];
        subscription[0] = topic;
        subscription[1] = id;
        subscriptions.remove(subscription);
    }


    public void setPasscode(String passcode)
    {
        this.passcode = passcode;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public void setConnectionId(int connectionId)
    {
        this.connectionId = connectionId;
    }

    public int getConnectionId()
    {
        return connectionId;
    }

    public Status getStatus()
    {
        return this.status;
    }

    public String getLogin()
    {
        return login;
    }

    public String getPasscode()
    {
        return passcode;
    }


    public boolean login(String passcode)
    {
        boolean success = false;
        
        if(this.passcode == passcode && status != Status.LOGIN)
        {
            this.status = Status.LOGIN;
            success = true;
        }

        return success;
    }

    public boolean logout()
    {
        boolean success = false;
        
        if(status != Status.LOGOUT)
        {
            this.status = Status.LOGOUT;
            success = true;
        }

        return success;
    }


}
