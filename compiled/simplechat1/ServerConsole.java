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
                //send the message to the client like normal
                server.handleMessageServer("SERVER msg> " + message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
