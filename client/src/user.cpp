#include "../include/user.h"
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <sstream>
#include "user.h"

User::User(std::string username, int receipt_id, int subscription_id, bool is_active, std::map<int, std::string> subId_channel, std::map<int, std::string> receiptId_command)
    : username(username), receipt_id(0), subscription_id(0), is_active(is_active), subId_channel(subId_channel), receiptId_command (receiptId_command)
{
}



const std::string &User::get_username() const
{
    return this->username;
}

const int &User::get_receipt_id() const
{
    return this->receipt_id;
}

const int &User::get_subscription_id() const
{
    return this->subscription_id;
}

const bool &User::get_is_active() const
{
    return this->is_active;
}

const std::map<int, std::string> &User::get_subId_channel() const
{
    return this->subId_channel;
}

const std::map<int, std::string> &User::get_receiptId_command() const
{
    return this->receiptId_command;
}
void User::set_username(std::string &username)
{
    this->username = username;
}
void User::increment_receipt_id()
{
    this->receipt_id += 1;
}

void User::increment_subscription_id()
{
    this->subscription_id += 1;
}
void User::set_is_active(bool is_active)
{
    this->is_active = is_active;
}

void User::add_to_receiptId_command(int &receiptId, std::string& command)
{
    this->receiptId_command.insert(std::pair<int, std::string>(receiptId, command));
}

void User::add_to_subscriptionId_channel(int &subscriptionId, std::string& channel)
{
    this->subId_channel.insert(std::pair<int, std::string>(subscriptionId, channel));
}

void User::remove_from_receiptId_command(int &receiptId)
{
    this->receiptId_command.erase(receipt_id);
}

void User::remove_from_subscriptionId_channel(int &subscriptionId)
{
    this->subId_channel.erase(subscription_id);
}
