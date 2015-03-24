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
   package reload.Topology;
   
   import reload.Common.*;
   import reload.Topology.Plugins.Chord.ChordConnectionTable;
   import java.util.*;
   import java.io.IOException;
   import java.net.InetAddress;
   
/**
* ConnectionTable class is the Connection Table for the Topology Plugin module.
* @author Malosa 
* @version 0.1
*/	
   public class ConnectionTable{
   
   
      private ChordConnectionTable chord;
      // private AhotherConnectionTable other_plugin;
   
   /**
    * Creates a new empty table.
    */  
      public ConnectionTable(){
      
         chord = new ChordConnectionTable();
      
      }
   
   /**
    * Adds a new row to the table.
    * @param node the node to be added
    * @param address the IP address and port to be added
    * @param client true if connection is client, false if is server
    * @return the connection number (link number)
    */ 
      public int addEntry(NodeId node, IpAddressPort address, boolean client) throws Exception{
      
         return chord.addEntry(node, address, client);
      
      }
   
   /**
    * Returns the link number of a stored ID.
    * @param id the Node-ID we are looking for
    * @param client true if connection is client, false if is server
    * @return connection number (link number)
    */      
      public int getNumLink(Id id, boolean client){
      
         return chord.getNumLink(id, client);
      
      }
   
   /**
    * Returns the link number of a stored ID. For server only.
    * @param addr the IP address we are looking for
    * @return connection number (link number)
    */    
      public int getNumLink(InetAddress addr) throws Exception{ // For SERVER Only
      
         return chord.getNumLink(addr);
      
      }
     
   /**
    * Returns the link number of a stored ID. For client only.
    * @param ip the IP address and port we are looking for
    * @return connection number (link number)
    */    
      public int getNumLink(IpAddressPort ip) throws IOException{ // For CLIENT only
      
         return chord.getNumLink(ip);
      
      }
   
   /**
    * Returns if the connection of a stored Node-ID is client or server.
    * @param id the Node-ID we are asking for
    * @return true if client, false if server
    */ 
      public boolean isClient(Id id){
      
         return chord.isClient(id);
      
      }
      
   /**
    * Returns if the connection of a stored Node-ID is client or server.
    * @param id the Node-ID we are asking for
    * @return true if server, false if client
    */
      public boolean isServer(Id id){
      
         return chord.isServer(id);
      
      }
   
   /**
    * Returns the Node-ID of a stored connection.
    * @param num the connection number (link number)
    * @param client true if connection is client, false if is server
    * @return the Node-ID
    */ 	 
      public NodeId getNode(int num, boolean client) throws Exception{
      
         return chord.getNode(num, client);
      
      }
    
   /**
    * Deletes a row from the table.
    * @param num the connection number (link number)
    * @param client true if connection is client, false if is server
    * @return true if could be deleted
    */    
      public boolean delete(int num, boolean client){
      
         return chord.delete(num, client);
      
      }
   
   /**
    * Deletes a row from the table.
    * @param node the Node-ID to be deleted
    * @return true if could be deleted
    */    
      public synchronized boolean delete(NodeId node){
         
         return chord.delete(node);
      
      }
   	
      public synchronized boolean delete(NodeId node, boolean client){
         
         return chord.delete(node, client);
      
      }
   
   /**
    * Returns the IP address and port of a stored connection.
    * @param num the connection number (link number)
    * @param client true if connection is client, false if is server
    * @return the IP address and port (client) or the IP address only (server)
    */ 	  
      public IpAddressPort getAddressPort(int num, boolean client) throws Exception{
      
         return chord.getAddressPort(num, client);
      
      }
   
   /**
    * Returns the number of client or server links that exist in the connection table.
    * @param client true for the client links number, false for the server links number
    * @return the number of links
    */     
      public int getNumLinks(boolean client){
      
         return chord.getNumLinks(client);
      
      }
   
   /**
    * Returns if a Node-ID exists in the connection table.
    * @param node the Node-ID
    * @return true if exists, false if not
    */   
      public boolean isDirectlyConnected(NodeId node){
      
         return chord.isDirectlyConnected(node);
      
      }
     
   }