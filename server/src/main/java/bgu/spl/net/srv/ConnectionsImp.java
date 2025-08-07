package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.stomp.NewsFeedGames;
import bgu.spl.net.impl.stomp.User;

public class ConnectionsImp<T> implements Connections<T> {

    private HashMap<Integer, MessagingProtocol<T>> protocols;
    private HashMap<Integer, ConnectionHandler<T>> connectionHandlers;
    private int lastConnectionId = -1;


    private static class Singleton
    {
        private static ConnectionsImp<Object> instance = new ConnectionsImp<>();
    }

    public static ConnectionsImp<Object> getInstance()
    {
        return ConnectionsImp.Singleton.instance;
    }

    private ConnectionsImp(){
        this.protocols = new HashMap<Integer, MessagingProtocol<T>>();
        this.connectionHandlers = new HashMap<Integer, ConnectionHandler<T>>();
    }


    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler<T> whereToSend = this.connectionHandlers.get(connectionId);

        if (whereToSend != null)
        {
            whereToSend.send(msg);
        }

        return false;
    }

    @Override
    public void send(String channel, T msg) {
        
        NewsFeedGames newsFeedGames = NewsFeedGames.getInstance();
        ConcurrentLinkedQueue<User> subsToChannel = newsFeedGames.getSubscribersByTopic(channel);
        Iterator<User> iter = subsToChannel.iterator();

        while(iter.hasNext())
        {
            User user = (User) iter.next();
            send(user.getConnectionId(), msg);
        }
        
    }

    @Override
    public void disconnect(int connectionId) {

        try{
            MessagingProtocol<T> protocol = protocols.get(connectionId);
            protocol.shouldTerminate();
        } catch(Exception e){}

        connectionHandlers.remove(connectionId);
        
    }



    public synchronized int add (ConnectionHandler connectionHandler, MessagingProtocol protocol)
    {
        lastConnectionId++;
        protocols.put(lastConnectionId, protocol);
        connectionHandlers.put(lastConnectionId, connectionHandler);
        return lastConnectionId;

    }

   
    
}
