package reload.Topology.Plugins;

import reload.Common.*;
import reload.Common.Error.*;
import reload.Common.Exception.*;
import reload.Topology.*;
import reload.Topology.Plugins.Chord.*;
import reload.Topology.Plugins.Chord.Task.*;
import reload.Message.Forwarding.*;
import reload.Storage.*;  
import java.security.MessageDigest; 
import java.net.InetAddress;
import java.util.ArrayList;

/**
* ChordInterface class is the interface of the Chord plugin.
* @author Malosa 
* @version 0.1
*/ 	
public class ChordInterface{

   private ChordRouting routing;
   private ChordThread thread;
   
   public ChordInterface(){
   
      routing = new ChordRouting();
      
      thread = new ChordThread();
   
   }

/**
 * Establishes the Chord plugin.
 */ 
   public byte[] createJoinReq(NodeId node) throws java.io.IOException{
   
      ChordJoinReq send_jr = new ChordJoinReq(node);
      	
      return send_jr.getBytes();
   
   }

/**
 * Gets the next hop when a message is routed to a destination ID.
 * @param thisId this peer Node-ID
 * @param destId the destination ID (Node-ID or Resource-ID)
 * @param list the routing table nodes (candidates to be the next hop)
 * @return next hop's Node-ID
 */    
   public NodeId route(NodeId thisId, Id destId, ArrayList<NodeId> list) throws Exception{
   
      return routing.route(thisId, destId, list);
   
   }
   
   public ResourceId closest(ResourceId[] value, ResourceId ideal){
   
      return routing.closest(value, ideal);
   
   }
   
   public ResourceId distanceFinger(NodeId thisId, int i) throws Exception{
   
      return routing.distanceFinger(thisId, i);
   
   }

/**
 * Method to be called by the Topology Plugin when it receives a Chord Update Request message.
 * @param msg_body Chord Update Request message to decode
 * @param src peer which the message comes from (not the creator, just the last hop)
 * @return the response (Chord Update Answer) to the request
 */	   
   public byte[] update_req(byte[] msg_body, NodeId src) throws Exception{
   
     
      ChordUpdateReq req = new ChordUpdateReq(msg_body);
      
      int uptime = req.getUptime();
      
      ChordUpdateType type = req.getType();
      
      if (!Module.falm.updateIntialization){
      
         if(type == ChordUpdateType.peer_ready){
         
            if(!Module.tpi.routingTable.exists(src))
               if(Module.tpi.connectionTable.isDirectlyConnected(src)){
                  Module.tpi.createUpdate(src, (byte)1); // peer_ready
                  Module.tpi.routingTable.addNeighbor(src);
               }
         
         }
      
         if(type == ChordUpdateType.neighbors){
            NodeId[] predecessors = req.getPredecessors();
            NodeId[] successors = req.getSuccessors();
            Module.falm.createAttachRoute(predecessors, successors, false);
         }
      
         if(type == ChordUpdateType.full){
            NodeId[] predecessors = req.getPredecessors();
            NodeId[] successors = req.getSuccessors();
            NodeId[] fingers = req.getFingers();
            Module.falm.createAttachRoute(predecessors, successors, fingers, false);
         }
      
      } 
      
      else{  // Module.falm.updateIntialization=true
      
         Module.tpi.routingTable.addNeighbor(src); // Admitting Peer ID (AP)
      
         if(type == ChordUpdateType.full){
            NodeId[] predecessors = req.getPredecessors();
            NodeId[] successors = req.getSuccessors();
            Module.falm.createAttachRoute(predecessors, successors, false);
            
            NodeId[] fingers = req.getFingers();
            Module.falm.createAttachRoute(fingers, true); // Fingers only
         }
            
         else if(type == ChordUpdateType.neighbors){
            NodeId[] predecessors = req.getPredecessors();
            NodeId[] successors = req.getSuccessors();
            Module.falm.createAttachRoute(predecessors, successors, true);
            
            Module.falm.createAttachFingersRoute(); // If type is neighbor, make attachs to fingers
         }
          
         else
            throw new WrongTypeReloadException();
      
         Module.falm.updateIntialization = false;
      
      }  
      
      ChordUpdateAns ans = new ChordUpdateAns(true);
      
      if(Module.falm.joinIntialization){ // Special case for initialization
         Module.falm.joinIntialization = false;
         synchronized(Module.falm){
            Module.falm.notify();
         }
      }
   
      return ans.getBytes();  
   
   }

/**
 * Method to be called by the Topology Plugin when it receives a Chord Join Request message.
 * @param msg_body Chord Join Request message to decode
 * @param src peer which the message comes from (not the creator, just the last hop)
 * @return the response (Chord Join Answer) to the request
 */   
   public byte[] join_req(byte[] msg_body, NodeId src) throws Exception{
   
      ChordJoinReq req = new ChordJoinReq(msg_body);
      
      NodeId id = req.getJoiningPeerId();
      
      if(!src.equals(id))
         throw new ErrorForbidden();
            	
      
      boolean client = Module.tpi.connectionTable.isClient(id);
      int numLink = Module.tpi.connectionTable.getNumLink(id, client);
   	
   	
      Module.si.createJoiningStore(id, numLink, client);
   	
   
      ChordJoinAns ans = new ChordJoinAns();
   
      return ans.getBytes();      
            
   }

/**
 * Method to be called by the Topology Plugin when it receives a Chord Leave Request message.
 * @param msg_body Chord Leave Request message to decode
 * @param src peer which the message comes from (not the creator, just the last hop)
 * @return the response (Chord Leave Answer) to the request
 */		    
   public byte[] leave_req(byte[] msg_body, NodeId src) throws Exception{
   
      ChordLeaveReq req = new ChordLeaveReq(msg_body);
      
      NodeId id = req.getLeavingPeerId();
      
      ChordLeaveData data = req.getLeavingData();
   	
      ChordLeaveType type = data.getType();
   	
      if(type == ChordLeaveType.from_succ){
         NodeId[] successor = data.getSuccessors();
      }
   	
      if(type == ChordLeaveType.from_pred){
         NodeId[] predecessor = data.getPredecessors();
      }
      
      if(!src.equals(id))
         throw new ErrorForbidden();
      
      if(!Module.tpi.connectionTable.isDirectlyConnected(id))
         throw new ErrorForbidden();
   		
   		
      Module.tpi.routingTable.delete(id);
         
         
      ChordLeaveAns ans = new ChordLeaveAns();
   
      return ans.getBytes();      
            
   }

/*
 * Method to be called by the Topology Plugin when it receives a Chord Route Query Request message.
 * @param msg_body Chord Route Query Request message to decode
 * @return the response (Chord Route Query Answer) to the request
 */
   public byte[] route_query_req(byte[] msg_body) throws Exception{
   
   
      ChordRouteQueryReq req = new ChordRouteQueryReq(msg_body);
      
      boolean send_update = req.getSendUpdate();
      Destination destination = req.getDestination();
   
      if(send_update){
      
      }
      
      NodeId next_peer = null; 
      
      DestinationData data = destination.getDestinationData();
      DestinationType type = destination.getType();
   	
      if(type == DestinationType.opaque_id_type)
         throw new UnimplementedReloadException("Opaque/compressed List");
      
      else{
         Id dest = data.getId();
         next_peer = Module.tpi.route(dest);
      }
   
      ChordRouteQueryAns ans = new ChordRouteQueryAns(next_peer);
      
      return ans.getBytes();    
            
   }

/**
 * Creates an Update Task.
 * @param dest Node-ID to send the Update to
 * @param numLink the link number
 * @param client true if connection is client, false if is server
 * @param updateType ChordUpdateType for the request
 * @param sendData data will be sent when link is up
 */  	
   public void createUpdate(NodeId dest, int numLink, boolean client, byte updateType, boolean sendData){
      
      ChordUpdateTask task;
      
      if(dest == null)
         task = new ChordUpdateTask(thread);
      
      else{	
      
         if(sendData)
            task = new ChordUpdateTask(thread, dest, numLink, client, ChordUpdateType.valueOf(updateType));
         
         else
            task = new ChordUpdateTask(thread, dest, ChordUpdateType.valueOf(updateType));
         
      }
      
      synchronized(thread){
      
         boolean empty = thread.isEmpty();
         
         thread.add(task);
         
         if(empty)
            thread.notify();
           
      }
            
   }

/**
 * Creates a Join Task.
 * @param dest the Admitting Peer Node-ID
 */  	   
   public void createJoin(NodeId dest){
    
      ChordJoinTask task = new ChordJoinTask(thread, dest);
      
      synchronized(thread){
      
         boolean empty = thread.isEmpty();
         
         thread.add(task);
         
         if(empty)
            thread.notify();
           
      }
            
   }

/**
 * Creates a Leave Task.
 */  		
   public void createLeave(){
    
      ChordLeaveTask task = new ChordLeaveTask(thread);
      
      synchronized(thread){
      
         boolean empty = thread.isEmpty();
         
         thread.add(task);
         
         if(empty)
            thread.notify();
           
      }
            
   }

/**
 * Generates a new Node-ID or Resource-ID from the IP address (used if no enrollment server exists).
 * @param address the IP address (port is not used)
 * @param node true if Node-ID, false if Resource-ID
 */  		
   public Id generateNewId(InetAddress address, boolean node) throws Exception{ // Port is not used
   
      IpAddressPort IpAddr = new IpAddressPort(address, 0);
   
      MessageDigest md = MessageDigest.getInstance(Module.falm.getConfiguration().getDigest()); // sha1 or sha256 (SHA-1 by default)
      md.update(IpAddr.getAddressBytes());
      byte[] ID = md.digest();
      ID = Utils.cutArray(ID, 16, 0);
   
      if(node)
         return new NodeId(ID, true);
      else
         return new ResourceId(ID, true); // Upper
   
   }

/**
 * Calculates the hash of a String, obtaining a Reource-ID with the most significant 128 bits.
 * @param name the String
 * @return the Resource-ID calculated.
 */     
   public ResourceId hash(String name) throws Exception{
   
      MessageDigest md = MessageDigest.getInstance(Module.falm.getConfiguration().getDigest()); // sha1 or sha256 (SHA-1 by default)
      md.update(name.getBytes("US-ASCII"));
      byte[] ID = md.digest();
      ID = Utils.cutArray(ID, 16, 0);
   
      return new ResourceId(ID, true); // Upper
   
   }

/**
 * Returns the Topology Plugin thread.
 * @return the TopologyThread object.
 */     
   public ChordThread getChordThread(){
   
      return thread;
   
   }

}