#pragma once

#include <string>
#include <iostream>
#include <map>
#include <vector>

class User
{
private:
    std::string username;
    int receipt_id;
    int subscription_id;
    bool is_active;
    std::map<int, std::string> subId_channel;
    std::map<int, std::string> receiptId_command;

public:
    User(std::string username, int receipt_id, int subscription_id, bool is_active, std::map<int, std::string> subId_channel, std::map<int, std::string> receiptId_command);
    const std::string &get_username() const;
    const int &get_receipt_id() const;
    const int &get_subscription_id() const;
    const bool &get_is_active() const;
    const std::map<int, std::string> &get_subId_channel() const;
    const std::map<int, std::string> &get_receiptId_command() const;
    void set_username(std::string& username);
    void increment_receipt_id();
    void increment_subscription_id();
    void set_is_active(bool is_active);
    void add_to_receiptId_command(int& receiptId, std::string& command);
    void add_to_subscriptionId_channel(int &subscriptionId, std::string& channel);
    void remove_from_receiptId_command(int& receiptId);
    void remove_from_subscriptionId_channel(int &subscriptionId);
};
