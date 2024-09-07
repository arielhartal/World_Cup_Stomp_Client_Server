#include "../include/game.h"
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <sstream>
#include "game.h"

Game::Game(std::map<std::pair<std::string,std::string>, std::vector<Event>> user_game_to_events) : user_game_to_events(user_game_to_events)
{
}

Game::Game()
{
	std::map<std::pair<std::string,std::string>, std::vector<Event>> user_game_to_events;
    this->user_game_to_events = user_game_to_events;
}

const std::map<std::pair<std::string, std::string>, std::vector<Event>> &Game::get_user_game_to_events() const
{
    return this->user_game_to_events;
}

void Game::add_to_user_game_events(std::pair<std::string, std::string> user_game, std::vector<Event>& events)
{
    this->user_game_to_events.insert(std::pair<std::pair<std::string, std::string>, std::vector<Event>>(user_game, events));
}