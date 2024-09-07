package bgu.spl.net.impl.stomp;


import bgu.spl.net.api.StompMessageEncoderDecoder;
import bgu.spl.net.api.StompProtocol;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
          allUsers users = new allUsers();
          NewsFeedGames games = new NewsFeedGames(); 
 
            int port = Integer.parseInt(args[0]);
            String server = args[1];

             if(server == "tpc")
            {
              Server.threadPerClient(
                port, //port
                () -> new StompProtocol<>(users, games), //protocol factory
                StompMessageEncoderDecoder::new //message encoder decoder factory
              ).serve();
            }
            else if(server == "reactor")
            {
               Server.reactor(
                       Runtime.getRuntime().availableProcessors(),
                         port, //port
                       () -> new StompProtocol<>(users, games), //protocol factory
                       StompMessageEncoderDecoder::new //message encoder decoder factory
               ).serve();
            }


    }
}
