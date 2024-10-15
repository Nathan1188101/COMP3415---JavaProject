// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
      clientUI.display(msg.toString());
  }


  /**
   * This method handles client commands
   * @param msg
   * @throws IOException
   */
  public void handleCommand(String msg) throws IOException {

    //had to do an if statement for setport to handle it correctly
    if(msg.startsWith("#setport"))
    {
      if(!isConnected()){
        //splitting the message by spaces
        String[] messages = msg.split(" ");

        //checking if the second part of the message is a valid number
        if(messages.length > 1 && messages[1].matches("\\d+"))
        {
          int port = Integer.parseInt(messages[1]);
          this.setPort(port);
          //int ports = this.getPort();
          System.out.println("Port number set to: " + port);
          //System.out.println("Port number direct: " + ports);
        }
        else{
          System.out.println("Error setting port");
        }
      }
      else
        System.out.println("Must logoff to set port");

    }
    else if(msg.startsWith("#sethost")){
      if(!isConnected()) {//if logged out/ not connected
        //split the message by spaces
        String[] messages = msg.split(" ");

        if (messages.length > 1 && messages[1].matches("[a-z, A-Z]+")) {
          String hostname = messages[1]; //getting host name from second part of input
          this.setHost(hostname); //setting host name to input
          System.out.println("Host set to: " + hostname);
        } else {
          System.out.println("Error setting host.");
        }
      }
      else
        System.out.println("Must logoff to set host.");
    }
    else{
      switch (msg) {
        case "#quit":
          quit(); //quits chat client
          break;

        case "#logoff":
          try {
            //disconnect from server, but don't quit client
            closeConnection();
            System.out.println("You have logged off from the server.");
          } catch (Exception e) {
            System.out.println("Error: " + e);
          }
          break;

        case "#login":
          openConnection();
          System.out.println("Login Successful");
          break;

        case "#gethost":
          String host = this.getHost();
          System.out.println("Host: " + host);
          break;

        case "#getport":
          int port = this.getPort();
          System.out.println("Port: " + port);
          break;

        default:
          System.out.println("Unknown Command: " + msg);
          break;
      }
    }
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message) throws IOException {

    if(message.startsWith("#"))
    {
      handleCommand(message);
    }
    else
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  /**
   * This method handles losing connection to the server
   */
    @Override //Overriding the connectionClosed method in AbstractClient
    protected void connectionException(Exception exception){
      super.connectionException(exception); //keeping the default behavior from AbstractClient
      System.exit(0); //terminates the client
    }

    /**
     * This method handles closing the connection to the server
     */
    @Override
    protected void connectionClosed(){
      super.connectionClosed(); //keeping the default behavior from AbstractClient
      //System.exit(0); //terminates the client
    }

}
//End of ChatClient class
