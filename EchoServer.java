// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
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
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
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
   * This method detects disconnected clients and prints a nice message
   *
   * @param client is the client that disconnected (i hope)
   */
  public void clientDisconnected(ConnectionToClient client) {
    sendToAllClients("A client has disconnected. Have a nice day " + client + "!"); //        ADD A NAME WHEN U GET A NAME!!!
    System.out.println("A client has disconnected. Have a nice day " + client + "!");
  }

  public void clientConnected(ConnectionToClient client) {
    System.out.println("A client has connected. Say hi to " + client + "!");
  }

  public void handleMessageFromServerUI(String message){

    if(message.charAt(0)=='#'){
      String[] arr = message.split(" ");

      switch (arr[0]){
        case "#quit":
          this.sendToAllClients("The server will now quit.");
          System.exit(0);
          break;
        case "#stop":
          this.stopListening();
          this.sendToAllClients("The server has stopped listening.");
          break;
        case "#close":
          try{
            this.sendToAllClients("The server will now closed.");
            this.close();
          } catch(Exception e){
            System.out.println("Unable to close.");
          }
          break;
        case "#setport":
          if(this.isListening() && this.getNumberOfClients()>0){
            System.out.println("The server is on. REQUEST FAILED.");
          }else{
            super.setPort(Integer.parseInt(arr[1]));
          }
          break;
        case "#getport":
          System.out.println("The port is: " + this.getPort());
          break;
        default:
          System.out.println("Command not recognized.");
      }
    }else{
      try{
        this.sendToAllClients("SERVER MSG> " + message);
        System.out.println("SERVER MSG> " + message);
      } catch(Exception e){
          System.out.println("Could not send message to clients.");
      }
    }
  }
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
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
