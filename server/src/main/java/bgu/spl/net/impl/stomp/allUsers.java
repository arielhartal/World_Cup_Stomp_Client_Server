package bgu.spl.net.impl.stomp;

import java.util.concurrent.ConcurrentHashMap;


public class allUsers {

    private ConcurrentHashMap<String, User> users;
    private ConcurrentHashMap<User, Integer> userToConnectionID;


    public allUsers()
    {
        users = new ConcurrentHashMap<String, User>();
        userToConnectionID = new ConcurrentHashMap<User, Integer>();

    }

    private static class Singleton
    {
        private static allUsers instance = new allUsers();
    }

    public static allUsers getInstance()
    {
        return Singleton.instance;
    }


    public boolean addUser (User user){
        
        boolean success = false;

        if(!users.containsKey(user.getLogin()))
        {
            users.put(user.getLogin(), user);
            success = true;
        }
        return success;
    }

    public ConcurrentHashMap<User, Integer> getUserToConnectionID()
    {
        return userToConnectionID;
    }

    public boolean addUserConnetctionId (User user, int connectionId){
        
        boolean success = false;

        if(!userToConnectionID.containsKey(user))
        {
            userToConnectionID.put(user, connectionId);
            success = true;
        }
        return success;
    }

    public boolean removeUser (User user){
        
        boolean success = false;

        if(users.containsKey(user.getLogin()))
        {
            users.remove(user.getLogin(), user);
            success = true;
        }
        return success;
    }
    

    public boolean login(String login, String passcode)
    {
        boolean success = false;
        User user = users.get(login);
        if(user != null)
        {
            success = user.login(passcode);
        }

        return success;
    }

    public boolean logout(String login)
    {
        boolean success = false;
        User user = users.get(login);
        if(user != null)
        {
            success = user.logout();
        }

        return success;
    }

    public boolean authenticatePassword(String login, String passcode)
    {
        User user = users.get(login);
        
        return passcode.equals((user.getPasscode()));       
    }

    public boolean isUserRegistered(String username)
    {
        return users.containsKey(username);
    }


    public ConcurrentHashMap<String, User> getUsers() {
        return users;
    }
    
}
