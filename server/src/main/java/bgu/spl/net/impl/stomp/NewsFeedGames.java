package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NewsFeedGames {

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> topicSubscribers;

    public NewsFeedGames()
    {
        topicSubscribers = new ConcurrentHashMap<String, ConcurrentLinkedQueue<User>>();
    }

    private static class Singleton
    {
        private static NewsFeedGames instance = new NewsFeedGames();
    }

    public static NewsFeedGames getInstance()
    {
        return Singleton.instance;
    }


    public void addSubscriberToTopic (String topic, User user)
    {
        if(topicSubscribers.containsKey(topic))
        {        
            ConcurrentLinkedQueue<User> subscribers = topicSubscribers.get(topic);
            subscribers.add(user);
        }

        else
        {
            ConcurrentLinkedQueue<User> subscribers = new ConcurrentLinkedQueue<User>();
            subscribers.add(user);
            topicSubscribers.put(topic, subscribers);
        } 
    }


    public void removeSubscriberFromTopic (String topic, User user)
    {
        if(topicSubscribers.containsKey(topic))
        {        
            ConcurrentLinkedQueue<User> subscribers = topicSubscribers.get(topic);
            subscribers.remove(user);
        }
    }

    public ConcurrentLinkedQueue<User> getSubscribersByTopic (String topic)
    {

        return topicSubscribers.get(topic);
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> getTopicSubscribers()
    {
        return topicSubscribers;
    }

    
}
