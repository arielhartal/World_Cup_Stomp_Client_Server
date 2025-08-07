
#include <string>
#include <map>


class clientFrame {
public:
    // Constructor
    clientFrame(std::string& command, std::map<std::string, std::string>& headers, std::string& body) :
        command_(command), headers_(headers), body_(body) {}

    // Accessor methods
    const std::string& get_command() const { return command_; }
    const std::map<std::string, std::string>& get_headers() const { return headers_; }
    const std::string& get_body() const { return body_; }

private:
    std::string command_;
    std::map<std::string, std::string> headers_;
    std::string body_;
};