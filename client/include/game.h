#pragma once

#include <string>
#include <iostream>
#include <map>
#include <vector>
# include "event.h"

class Game
{
    private:
        std::map<std::pair<std::string,std::string>, std::vector<Event>> user_game_to_events;

    public:
        Game(std::map<std::pair<std::string,std::string>, std::vector<Event>> user_game_to_events);
        Game();
        const std::map<std::pair<std::string,std::string>, std::vector<Event>> &get_user_game_to_events() const;
        void add_to_user_game_events(std::pair<std::string, std::string> user_game, std::vector<Event>& events);
};
