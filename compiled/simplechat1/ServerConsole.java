import common.ChatIF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerConsole implements ChatIF {

    EchoServer server;

    public ServerConsole(EchoServer server)
    {
        this.server = server;
    }

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the client's message handler.
     */
    public void accept(){

        try{
            BufferedReader fromConsole =
                    new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true)
            {
                //get message from user input
                message = fromConsole.readLine();

                if(message.startsWith("#")){
                    handleCommand(message);
                }
                else
                    //send the message to the client like normal
                    server.handleMessageServer("SERVER msg> " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
            }
    }

    /**
     * this method handles server commands
     *
     * @param msg
     */
    public void handleCommand(String msg) throws IOException {

        if(msg.startsWith("#setport"))
        {
            if(!server.isListening())
            {
                String[] messages = msg.split(" "); //split by space

                //check if the second part of the message is a valid number
                if(messages.length > 1 && messages[1].matches("\\d+"))
                {
                    int port = Integer.parseInt(messages[1]);
                    server.setPort(port);
                    System.out.println("Port set to: " + port);
                } else {
                    System.out.println("Error setting port.");
                }
            }
            else{
                System.out.println("Server must be closed to set port.");
            }
        }
        else{
            switch(msg){
                case "#close":
                    System.out.println("Server has stopped listening for new clients. Disconnecting all clients. ");
                    server.close();
                    break;

                case "#stop":
                    System.out.println("Server has stopped listening for new clients");
                    server.stopListening();
                    break;

                case "#quit":
                    System.out.println("Server is now quitting...");
                    server.close();
                    System.exit(0); //terminate server
                    break;

                case "#getport":
                    int port = server.getPort();
                    System.out.println("Server port: " + port);
                    break;

                case "#start":
                    if(!server.isListening()){
                        System.out.println("Server is starting up...");
                        server.listen();
                        break;
                    }
                    else
                        System.out.println("Server must be closed to start up again.");
                    break;

                default:
                    System.out.println("Unknown command: " + msg);
                    break;


            }
        }

    }

    @Override
    public void display(String message) {
    System.out.println(message);
    }

    public static void main(String[] args){
        int port = EchoServer.DEFAULT_PORT;

        try{
            EchoServer server = new EchoServer(port);
            ServerConsole console = new ServerConsole(server);

            server.listen();
            console.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
