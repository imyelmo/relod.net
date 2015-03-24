package reload.Topology;

import java.io.*;
import reload.Common.*;
import reload.Common.Error.*;
import reload.Topology.Plugins.*;
import reload.Topology.Plugins.Chord.Task.*;
import reload.Message.Forwarding.Destination;
import java.net.InetAddress;

/**
* TopologyPluginInterface class is the interface of the TopologyPlugin module.
* @author Malosa 
* @version 0.1
*/   
public class TopologyPluginInterface{

   public RoutingTable routingTable;  
   public ConnectionTable connectionTable; 

   public boolean nodeJoiningFlag;
   
	//private AnotherOne other_plugin;  
   private ChordInterface chord;
   
   private int responsible_ppb=0, num_resources=0, uptime=0; // Pending
   

/**
 * Establishes the Topology Plugin module.
 */      
   public TopologyPluginInterface(){
   
      routingTable = new RoutingTable();
      connectionTable = new ConnectionTable();
   
      chord = new ChordInterface();
      
   }
   
   public Id generateNewId(InetAddress address, boolean node) throws Exception{ // Port is not used. Only for Chord
   
      return chord.generateNewId(address, node);
   
   }
   
   public ResourceId hash(String name) throws Exception{ // Only for Chord
   
      return chord.hash(name);
   
   }

/**
 * Gets the next hop when a message is routed to a destination ID.
 * @param destId the destination ID (Node-ID or Resource-ID)
 * @return next hop's Node-ID
 */  	
   public NodeId route(Id destId) throws Exception{
   
      return chord.route(routingTable.getThisNode(), destId, routingTable.getNodes());
   
   }

/**
 * Gets the next hop when a message is routed to a destination list.
 * @param destId the destination ID list (Node-ID array or Resource-ID array)
 * @return next hop's Node-ID
 */     
   public NodeId route(Id[] destId) throws Exception{
   
      return route(destId[0]);
   
   }
   
   public ResourceId closest(ResourceId[] value, ResourceId ideal){
   
      return chord.closest(value, ideal);
   
   }
   
   public ResourceId distanceFinger(int i) throws Exception{
   
      return chord.distanceFinger(routingTable.getThisNode(), i);
   
   }
  
   public byte[] createJoinReq(NodeId node) throws java.io.IOException{
   
      return chord.createJoinReq(node);
   
   }

/**
 * Method to be called by Message Transport when it receives a Probe Request message.
 * @param msg_body Probe Request message to decode
 * @return the response (Probe Answer) to the request
 */	   
   public byte[] probe_req(byte[] msg_body) throws Exception{
   
   
      ProbeReq req = new ProbeReq(msg_body);
      
      ProbeInformationType[] type = req.getRequestedInfo();
   	
      ProbeInformation[] pi = new ProbeInformation[type.length];
   	
      for(int i=0; i<type.length; i++){
      
         switch(type[i].getBytes()){
         
            case 1:
               pi[i] = new ProbeInformation(ProbeInformationType.responsible_set, new ProbeInformationData(responsible_ppb, ProbeInformationType.responsible_set));
               break;
            case 2:
               pi[i] = new ProbeInformation(ProbeInformationType.num_resources, new ProbeInformationData(num_resources, ProbeInformationType.responsible_set));
               break;
            case 3:
               pi[i] = new ProbeInformation(ProbeInformationType.uptime, new ProbeInformationData(uptime, ProbeInformationType.responsible_set));
               break;
         
         }
      
      }
   	  
           
      ProbeAns ans = new ProbeAns(pi);
   
      return ans.getBytes();      
            
   }
   
/**
 * Method to be called by Message Transport when it receives a Join Request message.
 * @param msg_body Join Request message to decode
 * @param src peer which the message comes from (not the creator, just the last hop)
 * @return the response (Join Answer) to the request
 */	    
   public byte[] join_req(byte[] msg_body, NodeId src) throws Exception{
   
      return chord.join_req(msg_body, src);     
            
   }

/**	
 * Method to be called by Message Transport when it receives a Leave Request message.
 * @param msg_body Leave Request message to decode
 * @param src peer which the message comes from (not the creator, just the last hop)
 * @return the response (Leave Answer) to the request
 */	   
   public byte[] leave_req(byte[] msg_body, NodeId src) throws Exception{
   
      return chord.leave_req(msg_body, src);
            
   }
	
/**
 * Method to be called by Message Transport when it receives a Update Request message.
 * @param msg_body Update Request message to decode
 * @param src peer which the message comes from (not the creator, just the last hop)
 * @return the response (Update Answer) to the request
 */	   
   public byte[] update_req(byte[] msg_body, NodeId src) throws Exception{
   
      return chord.update_req(msg_body, src);
            
   }
   
/**
 * Method to be called by Message Transport when it receives a Route Query Request message.
 * @param msg_body Route Query Request message to decode
 * @return the response (Route Query Answer) to the request
 */	   
   public byte[] route_query_req(byte[] msg_body) throws Exception{
   
      return chord.route_query_req(msg_body);      
            
   }

   public void createUpdate(NodeId dest, int numLink, boolean client, byte updateType, boolean sendData){
   
      chord.createUpdate(dest, numLink, client, updateType, sendData);
   
   }
      
   public void createUpdate(NodeId dest, byte updateType){
   
      createUpdate(dest, -1, false, updateType, false);
            
   }

/**
 * Creates an Update Task. Updates all nodes.
 */   
   public void createUpdate(){  // Updates all nodes
   
      createUpdate(null, -1, false, (byte)-1, false);
            
   }

/**
 * Creates a Join Task.
 * @param dest the Admitting Peer Node-ID
 */  	
   public void createJoin(NodeId dest){
    
      chord.createJoin(dest);
            
   }

/**
 * Creates a Leave Task.
 */  	
   public void createLeave(){
    
      chord.createLeave();
            
   }

/**
 * Returns the Topology Plugin thread.
 * @return the TopologyThread object.
 */     
   public ChordThread getTopologyThread(){
   
      return chord.getChordThread();
   
   }
   
   public TopologyThread getFakeTopologyThread(){
   
      return new TopologyThread();
   
   }
      
}