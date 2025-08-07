#include "../include/StompProtocol.h"
#include "../include/ConnectionHandler.h"
#include "../include/user.h"
#include "../include/event.h"
#include <boost/algorithm/string.hpp>
#include <boost/lexical_cast.hpp>
#include <string>
#include <thread>
#include <clientFrame.h>
#include "StompProtocol.h"
using namespace std;

   
		
	StompProtocol::StompProtocol(ConnectionHandler &connectionHandler, User &current_user) : connectionHandler (connectionHandler), current_user (current_user)
	{};
			
		

	void StompProtocol::execute()
	{
		bool shouldTerminate = false;
		while(!shouldTerminate)
		{
	
			std::string frame; 
			if (!connectionHandler.getLine(frame)) {
				std::cout << "Disconnected. Exiting...\n" << std::endl;
				this->shouldTerminate = true;
				break;
			}

			std::cout << frame << std::endl;

			std::vector<std::string> args;
			boost::split(args, frame, boost::is_any_of("\n"));
			if(args[0] == "MESSAGE")
			{
				return;
				shouldTerminate = true;
			}
			std::tuple<std::string,  std::map<std::string, std::string>, std::string> values_of_frame = parse_frame(frame);
			std::string command = std::get<0>(values_of_frame);
			std::map<std::string, std::string> headers = std::get<1>(values_of_frame);
			std::string body = std::get<2>(values_of_frame);


			if(command == "CONNECTED")
			{
				current_user.set_is_active(true);
				std::cout << "Login Successful" << std::endl;
			}

			if(command == "RECEIPT")
			{

				int receipt_id = std::stoi(headers.at("receipt-id"));
				std::string command_client = current_user.get_receiptId_command().at(receipt_id);

				if(command_client == "join")
				{
					
					current_user.increment_subscription_id();

				}



				if(command_client == "logout")
				{
					current_user.set_is_active(false);
					connectionHandler.close();
					return;
				}
			
 
			}

			if(command == "ERROR")
			{ 

				// login errors
				if(headers.at("message") == " already logged in")
				{
					std::cout << "User already logged in" << std::endl;
				}

				if(headers.at("message") == " wrong password")
				{
					std::cout << "Wrong password" << std::endl;
				}
				
				// join error
				if(headers.at("message") == " already subscribed to channel")
				{
					std::cout << "Already subscribed to this channel" << std::endl;
				}

				// logout error
				if(headers.at("message") == " user already logged off")
				{
					std::cout << "Already subscribed to this channel" << std::endl;
				}

				// exit error
				if(headers.at("message") == " user is not subscribed to channel")
				{
					std::cout << "Can't unsubscribe from a channel you are not subscribe to" << std::endl;
				}

				if(headers.at("message") == " user is not subscribed to any channel")
				{
					std::cout << "Can't unsubscribe from a channel if you are not subscribed to anything" << std::endl;
				}

				
			}

		}
		std::this_thread::yield();
		return;
	}

    std::tuple<std::string, std::map<std::string, std::string>, std::string> StompProtocol::parse_frame(std::string &frame)
    {
		std::string command;
		std::map<std::string, std::string> headers;
		std::string body;
		std::string headers_str;

		int pos = 0;
		int length = frame.length();

		int newline_pos = frame.find('\n', pos);
		command = frame.substr(pos, newline_pos - pos);
		pos = newline_pos + 1;

		while (pos < length) {
			newline_pos = frame.find('\n', pos);

			std::string line = frame.substr(pos, newline_pos - pos);

			if (line.empty()) {
				break;
			}

			headers_str += line + '\n';
			pos = newline_pos + 1;
		}


		std::istringstream ss(headers_str);
		std::string line;
		while (std::getline(ss, line)) {
			size_t colon_pos = line.find(':');
			if (colon_pos == std::string::npos) {
				continue;
			}
			std::string key = line.substr(0, colon_pos);
			std::string value = line.substr(colon_pos+1);
			headers[key] = value;
		}

		body = frame.substr(pos, length - pos);
		return {command, headers, body};
    }

    const std::string StompProtocol::build_frame(std::string command, std::map<std::string, std::string>& headers, std::string& body)
	{

		std::string frame = command + "\n";
		for(auto const& header : headers)
		{
			frame += header.first + ":" + header.second + "\n";
		}

		frame += "\n" + body + "\0";
		return frame;

	}

    std::string StompProtocol::buildBodyOfSend(std::string &user_name, std::string &event_name, std::string &team_a_name, std::string &team_b_name, std::string &team_a_updates, std::string &team_b_updates, int event_time, std::string &event_discription)
    {	


		std::string body;
		body = 	"user: " + user_name + "\n" +
				"team_a: " + team_a_name + "\n" +
				"team_b: " + team_b_name + "\n" +
				"event name: " + event_name + "\n" +
				"time: " + boost::lexical_cast<string>(event_time) + "\n" +
				"general game updates:\n" +
				"team_a_updates:\n" + team_a_updates +
				"team_b_updates:\n" + team_b_updates +
				"description:\n" + event_discription;
		
		return body;


					
	}


