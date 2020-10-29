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

  /*
   * String loginId is the display name of the client
  */
  String loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param loginId The name of the client
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginId, String host, int port, ChatIF clientUI){
    super(host, port);  
    try{
        this.loginId = loginId;
        this.clientUI = clientUI;
        openConnection();
        sendToServer("#login " + loginId);
        System.out.println(loginId + " has logged on.");
    } catch(IOException e){
      System.out.println("Cannot open connection.  Awaiting command.");
    }
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
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
      if(message.charAt(0)=='#'){
        String[] arr = message.split(" ");

        switch (arr[0]){
          case "#quit":
            try{
              System.out.println("Quitting.");
              sendToServer(this.loginId + " has disconnected.");
              quit();
            } catch (IOException e){
              System.exit(0);
            }
            break;
          case "#logoff":
            try{
              System.out.println("Logging off.");
              sendToServer(this.loginId + " has disconnected.");
              closeConnection();
            }catch(Exception e){
              System.out.println("There was an error closing the server.");
            }
            break;
          case "#sethost":
            if(this.isConnected()){
              System.out.println("You are already connected to a server. REQUEST FAILED.");
            }else{
              super.setHost(arr[1]);
              System.out.println("The host has been set to: " + this.getHost());
            }
            break;
          case "#setport":
            if(this.isConnected()){
              System.out.println("You are already connected to a server. REQUEST FAILED.");
            }else{
              super.setPort(Integer.parseInt(arr[1]));
              System.out.println("The port has been set to: " + this.getPort());
            }
            break;
          case "#login":
            if(this.isConnected()){
              System.out.println("You are already connected to a server. REQUEST FAILED.");
            }else{
              try{
                this.openConnection();
                sendToServer("#login " + arr[1]);
                System.out.println("You are connected.");
              }catch (IOException e){
                System.out.println("Connection failed.");
              }
            }
            break;
          case "#gethost":
            System.out.println("The host is: " + this.getHost());
            break;
          case "#getport":
            System.out.println("The port is: " + this.getPort());
            break;
          default:
            System.out.println("Command not recognized.");
        }
      }else{
        try{
          sendToServer(message);
        } catch(IOException e){
            clientUI.display("Could not send message to server.  Terminating client.");
            quit();
        }
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

  @Override
  public void connectionException(Exception exception){
    connectionClosed();
    //quit();
  }

  @Override
  public void connectionClosed(){
    clientUI.display("Connection with server closed.");
  }

  // protected void connectionEstablished(){
  //   try{
  //     sendToServer("#login " + loginId);
  //   }catch (IOException e){
  //     quit();
  //   }
  // }!!!

}
//End of ChatClient class
