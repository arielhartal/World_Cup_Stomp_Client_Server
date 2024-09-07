#pragma once

#include "../include/ConnectionHandler.h"
#include "../include/clientFrame.h"
#include "user.h"

// TODO: implement the STOMP protocol
class StompProtocol
{
    private:
        ConnectionHandler& connectionHandler;
        User& current_user;
        bool shouldTerminate = false;
    public:      
        StompProtocol(ConnectionHandler &connectionHandler, User &current_user);
        void execute();
        std::tuple<std::string, std::map<std::string, std::string>, std::string> parse_frame(std::string& frame);
        const std::string build_frame(std::string command, std::map<std::string, std::string>& headers, std::string& body);
        std::string buildBodyOfSend(std::string& user_name, std::string& event_name, std::string& team_a_name, std::string& team_b_name, std::string& team_a_updates, std::string& team_b_updates, int event_time, std::string& event_discription);
    
};
