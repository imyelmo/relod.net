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
   package reload.Topology.Plugins.Chord;
   
   import reload.Common.*;
   import java.util.*;
   import java.io.IOException;
   import java.net.InetAddress;
   
/**
* ChordConnectionTable class is the Connection Table in the RELOAD-Chord protocol.
* It stores mapping between Node-IDs and IP Addresses.
* @author Malosa 
* @version 0.1
*/	
   public class ChordConnectionTable{
   
      private ArrayList<byte[]> nodeC; // NodeId, node=true
      private ArrayList<byte[]> addressC; // IpAddressPort, node=false
      private ArrayList<Integer> num_linkC; 
      
      private ArrayList<byte[]> nodeS; // NodeId, node=true
      private ArrayList<byte[]> addressS; // IpAddressPort, node=false
      private ArrayList<Integer> num_linkS; 
   
   
   /**
    * Creates a new empty table.
    */  
      public ChordConnectionTable(){
      
         nodeC = new ArrayList<byte[]>();
         addressC = new ArrayList<byte[]>();
         num_linkC = new ArrayList<Integer>();
         
         nodeS = new ArrayList<byte[]>();
         addressS = new ArrayList<byte[]>();
         num_linkS = new ArrayList<Integer>();
      
      }
   
   /**
    * Adds a new row to the table.
    * @param node the node to be added
    * @param address the IP address and port to be added
    * @param client true if connection is client, false if is server
    * @return the connection number (link number)
    */   
      public synchronized int addEntry(NodeId node, IpAddressPort address, boolean client) throws Exception{
      
         byte[] Bnode = node.getBytes(); // Is equal to getId()
         byte[] Baddress = address.getBytes();
         byte[] onlyAddress = address.getAddressBytes();
         int num = getFirstNumber(client);
         
         if(client){
            nodeC.add(Bnode);
            addressC.add(Baddress);
            num_linkC.add(num);
         }
         
         else{ // Server
            nodeS.add(Bnode);
            addressS.add(onlyAddress);
            num_linkS.add(num);
         }
         
         return num;
      
      }
   
   /**
    * Returns the link number of a stored ID.
    * @param id the Node-ID we are looking for
    * @param client true if connection is client, false if is server
    * @return connection number (link number)
    */     
      public int getNumLink(Id id, boolean client){
      
         if(id == null)
            return -1;
      
         byte[] Bid = id.getId();
      
         int pos = indexOf(true, client, Bid);
         
         if(pos == -1)
            return -1;
         
         if(client)
            return num_linkC.get(pos);
         else
            return num_linkS.get(pos);
      
      }
   
   /**
    * Returns the link number of a stored ID. For server only.
    * @param addr the IP address we are looking for
    * @return connection number (link number)
    */   
      public int getNumLink(InetAddress addr) throws Exception{ // For SERVER Only
      
         IpAddressPort ip = new IpAddressPort(addr, 0);
      
         byte[] Bip = ip.getAddressBytes();
      
         int pos = indexOf(false, false, Bip);
         
         if(pos == -1)
            return -1;
         else
            return num_linkS.get(pos);
      
      }
   
   /**
    * Returns the link number of a stored ID. For client only.
    * @param ip the IP address and port we are looking for
    * @return connection number (link number)
    */    
      public int getNumLink(IpAddressPort ip) throws IOException{ // For CLIENT only. Not used (at the moment)
      
         byte[] Bip = ip.getBytes();
      
         int pos = indexOf(false, true, Bip);
         
         if(pos == -1)
            return -1;
         else
            return num_linkC.get(pos);
      
      }
      
   /**
    * Returns if the connection of a stored Node-ID is client or server.
    * @param id the Node-ID we are asking for
    * @return true if client, false if server
    */      
      public boolean isClient(Id id){
      
         byte[] Bid = id.getId();
      
         int pos = indexOf(true, true, Bid);
         
         if(pos == -1)
            return false;
         else
            return true;
      
      }
   
   /**
    * Returns if the connection of a stored Node-ID is client or server.
    * @param id the Node-ID we are asking for
    * @return true if server, false if client
    */ 	
      public boolean isServer(Id id){
      
         byte[] Bid = id.getId();
      
         int pos = indexOf(true, false, Bid);
         
         if(pos == -1)
            return false;
         else
            return true;
      
      }
      
      private boolean exists(Id id){
      
         byte[] Bid = id.getId();
      
         int server = indexOf(true, false, Bid);
         int client = indexOf(true, true, Bid);
         
         if(server == -1 && client == -1)
            return false;
         else
            return true;
      
      }
   
   /**
    * Returns the Node-ID of a stored connection.
    * @param num the connection number (link number)
    * @param client true if connection is client, false if is server
    * @return the Node-ID
    */ 	
      public synchronized NodeId getNode(int num, boolean client) throws Exception{
      
      
         if(client){
         
            int pos = num_linkC.indexOf(num);
            if(pos != -1)
               return new NodeId(nodeC.get(pos), true); // Upper
            else 
               return null;   
         }
         	
         else{
         		
            int pos = num_linkS.indexOf(num);
            if(pos != -1)
               return new NodeId(nodeS.get(pos), true); // Upper
            else 
               return null;			
         }
      
      }
   
   /**
    * Deletes a row from the table.
    * @param node the Node-ID to be deleted
    * @return true if could be deleted
    */ 	   
      public boolean delete(NodeId node){
      
         boolean client = isClient(node);
      
         return delete(node, client);
      
      }
     
      public synchronized boolean delete(NodeId node, boolean client){
      
         byte[] Bid = node.getId();
      
         int pos = indexOf(true, client, Bid);      
           
         if(pos == -1)
            return false;
             
         try{
            remove(pos, client); 
         }
            catch(IndexOutOfBoundsException e){
               return false;
            }
         
         return true;
      
      }
   
   /**
    * Deletes a row from the table.
    * @param num the connection number (link number)
    * @param client true if connection is client, false if is server
    * @return true if could be deleted
    */    
      public synchronized boolean delete(int num, boolean client){
      
         int del_num = -1; 
      
         if(client)   
            del_num = num_linkC.indexOf(num);
         	   
         else
            del_num = num_linkS.indexOf(num);
            
           
         if(del_num == -1)
            return false;
             
         try{
            remove(del_num, client); 
         }
            catch(IndexOutOfBoundsException e){
               return false;
            }
         
         return true;
      
      }
      
      private void remove(int num, boolean client) throws IndexOutOfBoundsException{
      
         if(client){
         
            nodeC.remove(num);
            addressC.remove(num);
            num_linkC.remove(num);
         }
         
         else{
         
            nodeS.remove(num);
            addressS.remove(num);
            num_linkS.remove(num);
         }
      
      }
   
   /**
    * Returns the IP address and port of a stored connection.
    * @param num the connection number (link number)
    * @param client true if connection is client, false if is server
    * @return the IP address and port (client) or the IP address only (server)
    */ 	    
      public synchronized IpAddressPort getAddressPort(int num, boolean client) throws Exception{
         
      	
         if(client){
         
            int pos = num_linkC.indexOf(num);
            if(pos != -1)
               return new IpAddressPort(addressC.get(pos));
            else 
               return null;   
         }
         	
         else{ // Server
         		
            int pos = num_linkS.indexOf(num);
            if(pos != -1)
               return new IpAddressPort(addressS.get(pos), 0); // No remote port when I am acting server
            else 
               return null;			
         }
            
      }
   
   /**
    * Returns the number of client or server links that exist in the connection table.
    * @param client true for the client links number, false for the server links number
    * @return the number of links
    */     
      public int getNumLinks(boolean client){
      
         if (client)
            return nodeC.size();
         
         else
            return nodeS.size();
      
      }
   
   /**
    * Returns if a Node-ID exists in the connection table.
    * @param node the Node-ID
    * @return true if exists, false if not
    */       
      public boolean isDirectlyConnected(NodeId node){
      
         return exists(node);
      
      }
      
   	// Separar en dos: nodeIndexOf y addrIndexOf
      private synchronized int indexOf(boolean node, boolean client, byte[] data){
      
         if(client){
         
            if(node){
            
               for (int i=0; i<nodeC.size(); i++){
               
                  if (Utils.equals(data, nodeC.get(i)))
                     return i;
               
               }
            
            }
            else{
            
               for (int i=0; i<addressC.size(); i++){
               
                  if (Utils.equals(data, addressC.get(i)))
                     return i;
               
               }
            
            }
         
         }
         
         else{
         
            if(node){
            
               for (int i=0; i<nodeS.size(); i++){
               
                  if (Utils.equals(data, nodeS.get(i)))
                     return i;
               
               }
            
            }
            
            else{
            
               for (int i=0; i<addressS.size(); i++){
               
                  if (Utils.equals(data, addressS.get(i)))
                     return i;
               
               }
            
            }
         
         }
         
         return -1;
      
      }
      
      private int getFirstNumber(boolean client){
      
         if (client){
         
            if(num_linkC.isEmpty())
               return 0;
         
            for (int i=0; true; i++){ // We are looking for i
            
               for (int j=0; j<num_linkC.size(); j++){ // Recorremos todos y cada uno de los valores de la lista
               
                  if (num_linkC.get(j) == i) // Nuestro valor i ya existe en la lista, probamos el siguiente (i++)
                     break;
                  
                  if(j == num_linkC.size()-1) // Last iteration. Si la lista es recorrida sin haber encontrado la posici�n i, i es la posici�n deseada
                     return i;
               
               }
            
            }
         
         }
         
         else{
         
            if(num_linkS.isEmpty())
               return 0;
         
            for (int i=0; true; i++){ // We are looking for i
            
               for (int j=0; j<num_linkS.size(); j++){
               
                  if (num_linkS.get(j) == i)
                     break;
                  
                  if(j == num_linkS.size()-1) // Last iteration
                     return i;
               
               }
            
            }
         
         }
      
      }
      
   }