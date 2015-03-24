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
package reload.Forwarding;

import reload.Message.*;
import reload.Link.*;
import reload.Common.*;
import reload.Common.Exception.*;

public class ReloadThread extends Thread {
 
   private ForwardingCheck fc;
   private LinkInterface li;
   private NodeId peer;
   private boolean server;
   
   private int num; // Only if server
   private boolean bootstrap; // Only if server


   public ReloadThread(ForwardingCheck fc, LinkInterface li, NodeId peer){ // Client
   
      this.fc = fc; 
      this.li = li;
      this.peer = peer;  
      server = false;
         
   }
   
   public ReloadThread(ForwardingCheck fc, LinkInterface li){ // Server
         
      this.fc = fc; 
      this.li = li;
      server = true;
         
   }

   public void run() {
   
      try{
      
         if(server){
         
            num = li.newConnection();
            if(num == -1)
               return;
            if(num >= 1000)
               bootstrap = true;
            
            synchronized(this){
               notify(); // Next thread is unlocked
            }
            
            peer = Module.tpi.connectionTable.getNode(num, false);  
         
         }
      
         if(Module.falm.sendData.contains(num, !server)){
         
            byte[] data = Module.falm.sendData.getData(num, !server);
         
            send(data);
         
            Module.falm.sendData.delete(num, !server);
         
         }
      }
      
      catch(Exception e){
            
         e.printStackTrace();
            
      }
   
      
      while(true){
            
         try{
         
            byte[] received = null;
            try{  
               received = receive();
            }
            catch(java.io.EOFException e){
            
               if(bootstrap)
                  li.deleteBootstrap(num);
               else{
                  Module.tpi.connectionTable.delete(peer, !server);
                  Module.tpi.routingTable.delete(peer);
               }
               return;
            }
            catch(java.io.IOException io){

               if(bootstrap)
                  li.deleteBootstrap(num);
               else{
                  Module.tpi.connectionTable.delete(peer, !server);
                  Module.tpi.routingTable.delete(peer);
               }
               return;
            }
               
            Message message = new Message(received);
            
            if(peer == null && bootstrap){
               peer = message.getSourceNodeId();
               
               li.addBootstrapNode(peer, num);
            }
               
         
            if(fc.checkMessage(message, peer)){
            
               if(message.isAns())
               
                  Module.msg.processAns(message);
               
               else{ // isReq
               
                  byte[] response = Module.msg.processReq(message, peer);
               
                  if(num != -1 || (num == -1 && server && Module.tpi.routingTable.getPredecessor(0) == null))
                     send(response);
                  else
                     throw new ReloadException("Mensaje err�neo con num -1");
               
               }
            }
         }
         catch(Exception e){
               
               e.printStackTrace();
               
         }
         
      }
      
   }
   
   private byte[] receive() throws Exception{
   
      if(server)
         return li.receive(num);
      
      else
         return li.receive();
   
   }
	
   private void send(byte[] data) throws Exception{
   
      if(server)
         li.send(num, data);
      
      else
         li.send(data);
   
   }
   
}

