// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import java.util.Scanner;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
    String newMsg;
    Boolean flag = (Boolean) client.getInfo("flag");//getting the flag from the client
    String message = msg.toString();

    //System.out.println("Message received: " + msg + " from " + client);
    //this.sendToAllClients(msg);

    //this is for the first message that the client sends - it must be #login
    // Check if the client is not logged in and the first message is not #login
    if ((flag == null || !flag) && !message.startsWith("#login")) {
      // If this is the first message and it isn't #login, send error and close connection
      try {
        client.sendToClient("Error: The first command must be #login. Connection will be terminated.");
        client.close(); // Terminate the client’s connection
      } catch (IOException e) {
        System.out.println("Error closing client connection.");
      }
      return; // Stop further processing
    }

    //if the message starts with #login, then it's a login message
    if(message.startsWith("#login")) {
      //if user hasn't logged in yet i.e flag is false or null (null is for the first time the user logs in)
      if(flag == null || !flag){
        //we can take the message that was received, identifying it by the start of #login. strip that and take the id left behind
        String loginId = message.substring(6).trim();//trimming the message to get the login id

        //saving the user's login id so that the server can always identify the user
        client.setInfo("loginId", loginId);//setting the login id to the client
        client.setInfo("flag", true);//setting the flag to 1 to indicate that the user has logged in

        System.out.println("Client logged in with: " + loginId);
      }
      else{
        //if the user has already logged in, display this message
        System.out.println("User has already logged in");
        this.sendToAllClients("User has already logged in");
      }

      return; //returning to avoid the message being sent to all clients, since it's a login message

      }


    String loginId = (String) client.getInfo("loginId");//getting the login id from the client

    if(loginId != null){
      //new variable for adding the login id to the message
      newMsg = loginId + ": " + msg;
      System.out.println("Message received from " + loginId + ": " + msg);
    }
    else{
      //if the login id is null, just display the message as is
      newMsg = msg.toString();
      System.out.println("Message received: " + msg + " from " + client);
    }

    //sending the message to all clients
    this.sendToAllClients(newMsg);

    //if the message is #stop, stop listening for new connections
    if(message.equals("#stop")) {
      stopListening();
    }

  }

  /**
   * This method sends a message when a client disconnects from the server
   *
   * I was trying to use clientDisconnected, but it wasn't ever being called when a
   * client was disconnecting. I realized eventually (I think) that the
   * server was detecting disconnects through exceptions. So I ended up using
   * clientException to handle it, and it has seemed to work out.
   *
   * synchronized to avoid concurrency issues
   * @param client the connection with the client.
   */
  @Override
  protected synchronized void clientException(ConnectionToClient client, Throwable exception){
    System.out.println("A client has disconnected from the server: " + client);
  }

  /**
   * This method sends a message to the server when a client connects.
   *
   * synchronized to avoid concurrency issues
   * @param client the connection connected to the client.
   */
  @Override
  protected synchronized void clientConnected(ConnectionToClient client)
  {
    System.out.println("A client has connected to the server: " + client);
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  public void handleMessageServer(String message){
    System.out.println(message); //display message on server console
    sendToAllClients(message);//sending message to all clients
  }

  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]);
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT;// Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try
    {
      sv.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
