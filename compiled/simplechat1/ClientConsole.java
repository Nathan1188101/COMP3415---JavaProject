// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import client.*;
import common.*;

import javax.xml.bind.annotation.XmlType;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client = new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        //get message from user input
        message = fromConsole.readLine();
        //send the message to the client like normal
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args The host to connect to.
   */
  public static void main(String[] args) throws IOException {

    //outputting a message to the user to enter the port number to connect to, otherwise it's not obvious to the user what to do.
    System.out.println("Client started. Enter the port number to connect to (Default port number is 5555): ");

    String host = "";
    //int port = 0;  //The port number

    //scanner for user input for port number.
    Scanner scanner = new Scanner(System.in); //main purpose of scanner is to parse primitive types and strings using regular expressions, but it can also be used to read input from the user in the console.
    int port = scanner.nextInt(); //Port to connect to

    try
    {
      host = args[0]; //host name from command line
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ClientConsole chat= new ClientConsole(host, port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
