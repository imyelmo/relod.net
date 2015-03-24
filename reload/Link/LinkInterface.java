/*******************************************************************************
 *    <relod.net: GPLv3 beta software implementing RELOAD - draft-ietf-p2psip-base-26 >
 *    Copyright (C) <2013>  <Marcos Lopez-Samaniego, Isaias Martinez-Yelmo, Roberto Gonzalez-Sanchez> Contact: isaias.martinezy@uah.es
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software Foundation,
 *    Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *******************************************************************************/
   package reload.Link;
   
   import java.io.*;
   import java.net.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Link.TCP.*;
 
   public class LinkInterface{
   
      private TCPClient linkC;
      private TCPServer linkS;
      private boolean server;
   	
      private boolean tls; //Not implemented. For TLS or DTLS
   
   
   /**
   * LinkInterface class is the interface of the Link module.
   * @author Malosa 
   * @version 0.1
   */
      public LinkInterface(String overlay, IpAddressPort ip) throws Exception{ // Client
      
         server = false;
      
         if(overlay.equals("TLS"))
            throw new UnimplementedReloadException("TLS");
                  
         else if(overlay.equals("DTLS"))
            throw new UnimplementedReloadException("DTLS");
                  
         else if(overlay.equals("TCP")){
            linkC = new TCPClient(ip);
         }
         else
            throw new WrongTypeReloadException("Only TLS, DTLS and TCP types are allowed in the Internet Transport Layer. Check the configuration XML file.");
      
      }
      
   /**
    * Establishes the Link module.
    * @param overlay the overlay link protocol: TLS, DTLS or TCP
    * @param port the port used by RELOAD
    */ 
      public LinkInterface(String overlay, int port) throws Exception{ // Server
      
         server = true;
      
         if(overlay.equals("TLS"))
            throw new UnimplementedReloadException("TLS");
                  
         else if(overlay.equals("DTLS"))
            throw new UnimplementedReloadException("DTLS");
                  
         else if(overlay.equals("TCP")){
            linkS = new TCPServer(port);
         }
         else
            throw new WrongTypeReloadException("Only TLS, DTLS and TCP types are allowed in the Internet Transport Layer. Check the configuration XML file.");
      
      }
   
   /**
    * Sends a message to the network (only for client).
    * @param data the message (a whole RELOAD structure)
    */     
      public void send(byte[] data) throws Exception{
      
         if(!server)
            linkC.send(data);
         else
            throw new ReloadException("No send(data) on server.");
      
      }
   
   /**
    * Sends a message to the network (only for server).
    * @param data the message (a whole RELOAD structure)
    * @param num the link number
    */        
      public void send(int num, byte[] data) throws Exception{
      
         if(server)
            linkS.send(num, data);
         else
            throw new ReloadException("No send(data, out) on client.");
      
      }
   
   /**
    * Receives a message, it blocks until it's received (only for client).
    */   	
      public byte[] receive() throws Exception{
      
         if(!server){
            byte[] ret = linkC.receive();
            
               
            return ret;
         }
         else
            throw new ReloadException("No receive() on server.");
      
      }
   
   /**
    * Receives a message, it blocks until it's received (only for server).
    * @param num the link number
    */   	         
      public byte[] receive(int num) throws Exception{
      
         if(server){
            byte[] ret = linkS.receive(num);
   
            
            return ret;
         }
         else
            throw new ReloadException("No receive(int) on server.");
      
      }
   
   /**
    * Closes connection (only for client).
    */	
      public void close() throws IOException{
      
         if(server)
            linkS.close();
         else
            linkC.close();
      
      }
      
   /**
    * Closes connection (only for server).
    * @param num the link number
    */	   
      public void close(int num) throws Exception{
      
         if(server)
            linkS.close(num);
         else
            throw new ReloadException("No close(int) on client.");
      
      }
      
   /**
    * Adds bootstrap node (server only).
    * @param node the Node-ID to be added
    * @param num the link number
    */  
      public void addBootstrapNode(NodeId node, int num) throws ReloadException{
      
         if(server)
            linkS.addBootstrapNode(node, num);
         else
            throw new ReloadException("No addBootstrap on client.");
      
      }
   
   /**
    * Returns the link number of a bootstrap node (server only).
    * @param node the Node-ID to search
    * @return the link number (-1 if not found)
    */   
      public int getBootstrapNumLink(NodeId node) throws ReloadException{
      
         if(server)
            return linkS.getBootstrapNumLink(node);
         else
            throw new ReloadException("No getBootstrap on client.");
      
      }
   
   /**
    * Returns if bootstrap node exists (server only).
    * @param node the Node-ID to search
    * @return if it was found
    */   
      public boolean bootstrapIsConnected(NodeId node) throws ReloadException{
      
         if(server)
            return linkS.bootstrapIsConnected(node);
         else
            throw new ReloadException("No deleteBootstrap on client.");
      
      }
   
   /**
    * Deletes bootstrap node (server only).
    * @param num the link number
    * @return if it could be deleted
    */	
      public boolean deleteBootstrap(int num) throws Exception{
      
         if(server)
            return linkS.deleteBootstrap(num);
         else
            throw new ReloadException("No deleteBootstrap on client.");
      
      }
     
   /**
    * Gets remote Internet address from server (client only).
    * @return the Internet address
    */ 
      public InetAddress getInetAddress() throws ReloadException{
      
         if(!server)
            return linkC.getInetAddress();
         else
            throw new ReloadException("No getInetAddress() on server.");
      
      }
   
   /**
    * Gets remote Internet address from client (server only).
    * @param num the link number (it selects client)
    * @return the Internet address
    */
      public InetAddress getInetAddress(int num) throws ReloadException{
      
         if(server)
            return linkS.getInetAddress(num);
         else
            throw new ReloadException("No getInetAddress(num) on client.");
      
      }
   
   /**
    * Server waits for a new connection (it blocks until a client stablishes the connection).
    * @return the link number for the new connection
    */	   
      public int newConnection() throws Exception{
      
         return linkS.newConnection();
      
      }
   	
   }