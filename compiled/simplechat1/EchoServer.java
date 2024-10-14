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
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String message = msg.toString();

    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);

    //if the message is #quit, close the server (not sure if this is working how I want it too)
//    if(message.equals("#quit")) {
//      try {
//        close(); //close the server
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    }

    //if the message is #stop, stop listening for new connections
    if(message.equals("#stop")) {
      stopListening();
    }

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

    //may need this code for later (or something similar to it)
    //Scanner scanner = new Scanner(System.in);
    //int port = scanner.nextInt(); //Port to connect to

    try
    {
      //may need this code for later (or something similar to it)
      //port = Integer.parseInt(args[0]); //Get port from command line

      port = Integer.parseInt(args[0]);
    }
    catch(Throwable t)
    {
      //port = scanner.nextInt(); //Port to connect to
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
