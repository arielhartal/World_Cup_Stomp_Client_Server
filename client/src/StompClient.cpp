#include <stdlib.h>
#include "../include/ConnectionHandler.h"
#include "../include/StompProtocol.h"
#include "../include/user.h"
#include "../include/event.h"
#include "../include/game.h"
#include <thread>
#include <boost/algorithm/string.hpp>
#include <boost/lexical_cast.hpp>
#include <string.h>
#include <clientFrame.h>
#include <iostream>
#include <fstream>
using namespace std;






int main(int argc, char *argv[]) {

	std::string command;
	
	getline(cin, command);
	std::vector<std::string> args;
	
	boost::split(args, command, boost::is_any_of(" "));
	if(args.size() < 4)
	{
		std::cerr << "login {host:port} {username} {password}" << std::endl;
		return -1;
	}
	std::string username1 = args[2];
	std::string passcode = args[3];
	std::vector<std::string> hostport;
	boost::split(hostport, args[1], boost::is_any_of(":"));
	std::string frame;
	


   	std::string host = hostport[0];
    short port = stoi(hostport[1]);
	
    ConnectionHandler  connectionHandler(host , port);
	
    if (!connectionHandler.connect()) { 
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
	
	
	std::map<int, std::string> subId_channel;
	std::map<int, std::string> receiptId_command;

	User current_user = User(username1, 0, 0, false, subId_channel, receiptId_command);
	StompProtocol stompProtocol(connectionHandler, current_user);
	std::thread thread(std::bind(&StompProtocol::execute, &stompProtocol));

	Game current_game = Game();

	std::map<std::string, std::string> headers;
	headers.insert(std::pair<std::string, std::string>("accept-version", "1.2"));
	headers.insert(std::pair<std::string, std::string>("host", "stomp.cs.bgu.ac.il"));
	headers.insert(std::pair<std::string, std::string>("login", username1));
	headers.insert(std::pair<std::string, std::string>("passcode", passcode));
	std::string body;
	frame = stompProtocol.build_frame("CONNECT", headers, body);
	connectionHandler.sendLine(frame);

	
	
	std::string user_name = current_user.get_username();
    while (1) {
		
		
		
		std::string command;
		getline(cin, command);
		std::vector<std::string> args;
		boost::split(args, command, boost::is_any_of(" "));
		
		std::string frame;

		int receiptId = current_user.get_receipt_id();
		int subscriptionId = current_user.get_subscription_id();
		if(args.at(0) == "login")
		{
			std::string username1 = args[2];
			std::string passcode = args[3];
			std::string frame;
			if(!current_user.get_is_active())
			{
				  if (!connectionHandler.connect()) { 
						std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
						return 1;
					}
			}

			std::map<std::string, std::string> headers;
			headers.insert(std::pair<std::string, std::string>("accept-version", "1.2"));
			headers.insert(std::pair<std::string, std::string>("host", "stomp.cs.bgu.ac.il"));
			headers.insert(std::pair<std::string, std::string>("login", username1));
			headers.insert(std::pair<std::string, std::string>("passcode", passcode));
			std::string body;
			frame = stompProtocol.build_frame("CONNECT", headers, body);
			current_user.add_to_receiptId_command(receiptId, args.at(0));
			current_user.increment_receipt_id();
			connectionHandler.sendLine(frame);
			
		}

		if(args.at(0) == "join")
		{
			std::string destination = args[1];
			string sub_id = boost::lexical_cast<string>(subscriptionId);
			string receipt_id = boost::lexical_cast<string>(receiptId);
			
			frame = "SUBSCRIBE\ndestination:/" + destination + "\n" +					
					"id:" + sub_id  + "\n" 
					"receipt:" +  receipt_id + "\n" +
					"\0";
			

			current_user.add_to_receiptId_command(receiptId, args.at(0));
			current_user.add_to_subscriptionId_channel(subscriptionId, destination);
			current_user.increment_receipt_id();
		}

		if(args.at(0) == "exit")
		{
			int game_id = -1;
			std::string destination = args[1];
			for(auto const& sub_id_channel : current_user.get_subId_channel())
			{
				if(sub_id_channel.second == destination)
				{
					game_id = sub_id_channel.first;
				}
			}
			string sub_id = boost::lexical_cast<string>(game_id);
			string receipt_id = boost::lexical_cast<string>(receiptId);
			frame = "UNSUBSCRIBE\nid:" + sub_id  + "\n" +		
					"receipt:" +  receipt_id + "\n" +
					"\0";

			current_user.add_to_receiptId_command(receiptId, args.at(0));
			current_user.increment_receipt_id();
			current_user.remove_from_subscriptionId_channel(game_id);
		}

		if(args.at(0) == "logout")
		{
			string r = boost::lexical_cast<string>(receiptId);
			std::map<std::string, std::string> headers;
			headers.insert(std::pair<std::string, std::string>("receipt", r));
			std::string body;
			frame = stompProtocol.build_frame("DISCONNECT", headers, body);

			current_user.add_to_receiptId_command(receiptId, args.at(0));
			current_user.increment_receipt_id();
		}

		if(args.at(0) == "report")
		{
			current_user.add_to_receiptId_command(receiptId, args.at(0));
			current_user.increment_receipt_id();

			std::string json_path = args.at(1);
			struct names_and_events output = ::parseEventsFile(json_path);

			std::string game_name = output.team_a_name+"_"+output.team_b_name;
			std::pair<std::string, std::string> user_game = {user_name, game_name};
			std::map<std::pair<std::string,std::string>, std::vector<Event>> user_game_to_events;

			current_game.add_to_user_game_events(user_game, output.events);
			std::map<std::string, std::string> headers;
			headers.insert(std::pair<std::string, std::string>("destination", "/" + game_name));

			for(Event event : output.events)
			{
				std::string event_name = event.get_name();
				std::string event_discription = event.get_discription();
				std::string team_a_updates;
				std::string team_b_updates;
				for(auto const& update : event.get_team_a_updates())
				{
					team_a_updates += update.first + ":" + update.second + "\n";
				}
				for(auto const& update : event.get_team_b_updates())
				{
					team_b_updates += update.first + ":" + update.second + "\n";
				}

				std::string body = stompProtocol.buildBodyOfSend(user_name, event_name, output.team_a_name, output.team_b_name, team_a_updates, team_b_updates, event.get_time(), event_discription);
				frame = stompProtocol.build_frame("SEND", headers, body);
			}
			
		}



		if(args.at(0) == "summary")
		{
			std::string team_a;
			std::string team_b;
			std::vector<std::string> game_name;
			boost::split(game_name, args.at(1), boost::is_any_of("_"));
			team_a = game_name.at(0);
			team_b = game_name.at(1);
			std::string user = args.at(2);
			std::string output;

			std::map<std::pair<std::string, std::string>, std::vector<Event>> user_game_to_events = current_game.get_user_game_to_events();
			std::pair<std::string, std::string> user_game = {user, team_a + "_" + team_b};
			std::vector<Event> events = user_game_to_events.at(user_game);
			std::ofstream file(args.at(3));
			std::string game_event_reports = "";
			if(file.good())
			{
				for(Event event : events)
				{
					std::string team_a_updates;
					std::string team_b_updates;

					for(auto const& update : event.get_team_a_updates())
					{
						team_a_updates += update.first + ":" + update.second + "\n";
					}

					for(auto const& update : event.get_team_b_updates())
					{
						team_b_updates += update.first + ":" + update.second + "\n";
					}
					game_event_reports += 	boost::lexical_cast<std::string> (event.get_time()) + " - " + event.get_name() + "\n" +
											event.get_discription() + "\n";

					std::string team_stats;
					team_stats += team_a + " stats: \n"
									+ team_a_updates + "\n" +
									team_b + " stats: \n"
									+ team_b_updates + "\n";
					output =  team_a + " vs " + team_b + "\n" + "Game stats:\n" + team_stats +"Game event reports:\n" + game_event_reports;
									


				}
				file.write(output.c_str(), output.length());
				
			}
			else
			{
				std::cout << "File is corrupted" << std::endl;
			}


		}
		
		if(args.at(0) != "summary" && args.at(0) != "login")
		{
			connectionHandler.sendLine(frame); 
		}
		try{thread.detach();}
		catch(exception &e){}

	
	}
	return 0;
};







