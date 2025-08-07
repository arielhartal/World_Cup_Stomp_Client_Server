#pragma once

#include <string>
#include <iostream>
#include <map>

class clientFrame
{
private:
    std::string command_;
    std::map<std::string, std::string> headers_;
    std::string body_;

public:
    clientFrame(std::string& command, std::map<std::string, std::string>& headers, std::string& body);
    const std::string& get_command() const;
    const std::map<std::string, std::string>& get_headers() const;
    const std::string& get_body() const;
};

