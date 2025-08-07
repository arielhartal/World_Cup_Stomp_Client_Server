package bgu.spl.net.api;

import bgu.spl.net.impl.stomp.FrameHandler;
import bgu.spl.net.impl.stomp.NewsFeedGames;
import bgu.spl.net.impl.stomp.allUsers;
import bgu.spl.net.srv.Connections;

public class StompProtocol<T> implements MessagingProtocol<T> 
{
    private FrameHandler frameHandler;
    private boolean shouldTerminate;
    private Connections<Object> connections;
    private int connectionId;
    private allUsers users;
    private NewsFeedGames games;

    public StompProtocol(allUsers users, NewsFeedGames games)
    {
        this.users = new allUsers();
        this.games = new NewsFeedGames();
        this.frameHandler = new FrameHandler();

    }

    @Override
    public void start(int connectionId, Connections<Object> connections) {
       this.connectionId = connectionId;
       this.connections = connections;
       this.frameHandler.setConnectionId(connectionId);
    }

    @Override
    public Object process(Object msg) {
        frameHandler.process(msg);
        return null;
    }



    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void terminate(){
        shouldTerminate = true;
    }



}
