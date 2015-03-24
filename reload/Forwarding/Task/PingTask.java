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
   package reload.Forwarding.Task;
   
   import reload.Message.*;
   import reload.Forwarding.*;
   import reload.Forwarding.Ping.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*;   


   public class PingTask extends ForwardingTask{
   
      private Id dest;
      private int numLink;
      private boolean client;
      
      private boolean hasNumLink;
      private boolean allNodes;
   	
      public PingTask(ForwardingThread thread, Id dest, int numLink, boolean client){
      
         super(thread);
      	
         type = TaskType.ping;
         
         this.dest = dest;
         this.numLink = numLink;
         this.client = client;
         
         hasNumLink = true;
         allNodes = false;
            
      }  
      
      public PingTask(ForwardingThread thread, NodeId dest){
      
         super(thread);
      	
         type = TaskType.ping;
         
         this.dest = dest;
         
         hasNumLink = false;
         allNodes = false;
            
      }  
      
      public PingTask(ForwardingThread thread){ // All nodes
      
         super(thread);
      	
         type = TaskType.ping;
         
         this.dest = dest;
         
         hasNumLink = false;
         allNodes = true;
            
      } 
      
      public void start() throws Exception{
      
         if(allNodes){
         
            ArrayList<NodeId> node = Module.tpi.routingTable.getNodes();
            
            for(int i=0; i<node.size(); i++)
            
               send_receive(node.get(i));
         }
         
         else
         
            send_receive(dest);
         
      }
      
      private void send_receive(Id destination) throws Exception{
            
         PingReq req = new PingReq();
      	
         byte[] msg_body = req.getBytes();
         
         short code = 23; // PingReq
         
         if(hasNumLink)
            Module.msg.send(msg_body, code, destination, numLink, client);
         else
            Module.msg.send(msg_body, code, destination);
               
         Message mes = receive();
            
         code = mes.getMessageCode();
      		
         if (code != 24) // PingAns
            throw new WrongPacketReloadException(24);
         
      	
         msg_body = mes.getMessageBody();
      	
         PingAns ans = new PingAns(msg_body);
         
         System.out.println("Ping to " + destination.print() + " OK.");
         
         long response_id = ans.getResponseId();
         
         long time = ans.getTime();
      
      }
   	
   	
   	
   }

