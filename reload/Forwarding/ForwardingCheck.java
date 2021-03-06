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
   import reload.Message.Forwarding.DestinationType;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*; 
   import java.net.InetAddress;
   
   public class ForwardingCheck{
   
      private XML configuration;
      private boolean first; 	
   	
      public ForwardingCheck(XML configuration, boolean first) throws Exception{ // New connection
            
         this.configuration = configuration;
         this.first = first;
      
      }
   	
      public boolean checkMessage(Message message, NodeId peer) throws Exception{
            
         Id[] destID = message.getDestinationId(); // NodeId or ResourceId
      	
         NodeId thisID = Module.tpi.routingTable.getThisNode();
         NodeId previousID = Module.tpi.routingTable.getPredecessor(0);
         
      	
         if(thisID == null){ // First message received
         
            if(destID.length == 1 && destID[0].getType() == DestinationType.node && !((NodeId)destID[0]).isWildcard())
               return true;
            else
               return false; // Silently DROPPED
         }
         
      	
         if(previousID == null && Module.tpi.routingTable.getSuccessor(0) != null) // Two nodes in the overlay
            previousID = Module.tpi.routingTable.getSuccessor(0);
      
      
         if(previousID == null){ // Not yet part of the Overlay or only node in the Overlay
         
            if(destID.length == 1) 
               return checkResponsibleId(destID, thisID, message, null);
            else
               return false; // Silently DROPPED
         }
      
      
         if(Module.tpi.routingTable.isResponsible(destID[0])) // Responsible ID
            return checkResponsibleId(destID, thisID, message, peer);
            
         else // Other ID
            return checkOtherId(destID, thisID, message, peer);
      	
      	
      }
      
      public boolean checkMessage(Message message) throws Exception{ // Only usable if the peer is joining
      
         NodeId thisID = Module.tpi.routingTable.getThisNode();
         NodeId previousID = Module.tpi.routingTable.getPredecessor(0);
      
         if(thisID == null || previousID == null)
            return checkMessage(message, null);
         else
            throw new WrongTypeReloadException();
      	
      }
      
      private boolean checkResponsibleId(Id[] destID, NodeId thisID, Message message, NodeId peer) throws Exception{
      
         if(destID[0].getType() == DestinationType.resource){ // ResourceID
            
            if(destID.length == 1)
               return true;
            else
               return false; // Silently DROPPED
            
         }
            
         else if(destID[0].getType() == DestinationType.node){ // NodeID
            
            if(((NodeId)destID[0]).isWildcard())
               return true;
            
            
            if(destID[0].equals(thisID)){  
               
               if(destID.length == 1)
                  return true;
                  
               else{ // length != 1
                  
                  message.deleteFirstId();
                  if(message.decTTL()){
                     message.addVia(peer);
                     Module.falm.send(message);
                  }
                  else
                  {}// TTL ERROR MESSAGE
                     
                  return false;
                     	
               }
               
            }
                  
            else // Not equals this ID
               
               return false; // Silently DROPPED UNLESS CLIENT.
            
         }
            
         else
            throw new WrongTypeReloadException();
         
      }
      
      private boolean checkOtherId(Id[] destID, NodeId thisID, Message message, NodeId peer) throws Exception{
      
         if(destID[0].getType() == DestinationType.node || destID[0].getType() == DestinationType.resource){ // NodeID or ResourceID
                                            
            if(message.decTTL()){
               message.addVia(peer);
               Module.falm.send(message);
            }
            else
            {}// TTL error message
                     
            return false;
                    	  
         }
            
         else if(destID[0].getType() == DestinationType.opaque_id_type)
            throw new UnimplementedReloadException("Compressed Via List.");
            
            
         else 
            throw new WrongTypeReloadException();
      
      }
      
   }