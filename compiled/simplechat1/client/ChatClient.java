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
    if(msg.toString().startsWith("#"))
    {
      handleCommand(msg.toString());
    }
  }


  public void handleCommand(String msg)
  {
    if(msg.equals("#quit"))
    {
      quit(); //quits chat client
    }
    else if(msg.equals("#logoff"))
    {
      try{
        connectionClosed();
      }
      catch(Exception e)
      {
        System.out.println("Error: " + e);
      }
    }
    else if(msg.equals("#setport"))
    {
    String[] messages = msg.split(" ");
    this.setPort(Integer.parseInt(messages[1]));
    System.out.println("Port Number Changed to: " + messages[1]);

  }
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
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
