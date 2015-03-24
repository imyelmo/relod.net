package reload.Forwarding.Task;

import reload.Message.*;
import reload.Forwarding.*;
import reload.Forwarding.Request.*;
import reload.Common.*;
import reload.Common.Exception.*;
import java.net.InetAddress;

public class AttachRouteTask extends ForwardingTask{

   private NodeId dest;
   private NodeId[] nodes1;
   private NodeId[] nodes2;
   private NodeId[] nodes3;
   
   private boolean fingers;
   
   private boolean initialization; // Special case for initialization
	
	
   public AttachRouteTask(ForwardingThread thread, NodeId[] nodes1, NodeId[] nodes2, boolean initialization){
   
      super(thread);
   	
      type = TaskType.attach_route;
      
      this.nodes1 = nodes1;
      this.nodes2 = nodes2;
      nodes3 = null;
      
      fingers = false;
      
      this.initialization = initialization;
         
   }  
   
   public AttachRouteTask(ForwardingThread thread, NodeId[] nodes1, NodeId[] nodes2, NodeId[] nodes3, boolean initialization){
   
      this(thread, nodes1, nodes2, initialization);
   	
      this.nodes3 = nodes3;
         
   } 
   
   public AttachRouteTask(ForwardingThread thread, NodeId[] fingers, boolean initialization){
   
      super(thread);
   	
      type = TaskType.attach_route;
      
      nodes1 = fingers;
      nodes2 = null;
      nodes3 = null;
      
      this.fingers = true;
      
      this.initialization = initialization;
         
   }
   
   public AttachRouteTask(ForwardingThread thread){ // Fingers
   
      super(thread);
   	
      type = TaskType.attach_route;
      
      nodes1 = null;
      nodes2 = null;
      nodes3 = null;
      
      this.fingers = true;
      
      initialization = false;
         
   }
   
   public void start() throws Exception{
   
   
      if(fingers){
      
         if(nodes1 == null){
            
            for (int i=Module.tpi.routingTable.getCurrentFingerLength(); i<Module.tpi.routingTable.getFingerLength(); i++){
            
               ResourceId finger = Module.tpi.distanceFinger(i);
               NodeId dest = send_receive(finger);
               
               if(dest != null){
               
                  if(!initialization)
                     Module.tpi.createUpdate(dest, (byte)1); // peer_ready    
                  Module.tpi.routingTable.setFinger(dest, i);
               }
               else
                  break;
            
            }
         
         }
         
         else{
         
            for(int i=0; i<nodes1.length; i++)
            
               if(send_receive(nodes1[i]) != null){
                  if(!initialization)
                     Module.tpi.createUpdate(nodes1[i], (byte)1); // peer_ready
                  Module.tpi.routingTable.setFinger(nodes1[i], i);
               }
            
            
         }
      }
      
      else{
      
         for(int i=0; i<nodes1.length; i++){
         
            if(Module.tpi.routingTable.isPossibleNeighbor(nodes1[i]))
               if(send_receive(nodes1[i]) != null){ 
                  if(!initialization)
                     Module.tpi.createUpdate(nodes1[i], (byte)1); // peer_ready
                  Module.tpi.routingTable.addNeighbor(nodes1[i]);
               }
         
         }
      
         for(int i=0; i<nodes2.length; i++){
         
            if(Module.tpi.routingTable.isPossibleNeighbor(nodes2[i]))
               if(send_receive(nodes2[i]) != null){
                  if(!initialization)
                     Module.tpi.createUpdate(nodes2[i], (byte)1); // peer_ready
                  Module.tpi.routingTable.addNeighbor(nodes2[i]);
               }
         
         }
      
      
         if(nodes3 != null)
         
            for(int i=0; i<nodes3.length; i++){
            
               if(Module.tpi.routingTable.isPossibleNeighbor(nodes3[i]))
                  if(send_receive(nodes3[i]) != null){
                     if(!initialization)
                        Module.tpi.createUpdate(nodes3[i], (byte)1); // peer_ready
                     Module.tpi.routingTable.addNeighbor(nodes3[i]);
                  }
            
            }
         
      }
      
      if(initialization)
         synchronized(Module.falm){
            Module.falm.notify();
         }
      
   }
   
   public NodeId send_receive(Id dest) throws Exception{
   
         
   
      AttachReqAns req = new AttachReqAns(false, new IpAddressPort(Nat.myIP, Module.falm.getPort())); // We do not ask for the routing table
   	
      byte[] msg_body = req.getBytes();
      
      short code = 3; // AttachReq
      
      Module.msg.send(msg_body, code, dest);
      
   
      Message mes = receive();
         
      code = mes.getMessageCode();
   		
      if (code != 4) // AttachAns
         throw new WrongPacketReloadException(4);
      
   	
      NodeId node = mes.getSourceNodeId();
      
   	
      msg_body = mes.getMessageBody();
   	
      AttachReqAns ans = new AttachReqAns(msg_body);
      
      IpAddressPort IPdest = ans.getCandidates(0).getAddressPort(); //If public addresses, 0
      
      if(Module.tpi.connectionTable.isDirectlyConnected(node)){
         if(Module.tpi.routingTable.exists(node)){
            return null;
         }
            
         else{
            return node;
         }
         
      }
   
      int connect_num = Module.falm.createConnection(node, IPdest);
       
      if(connect_num == -1)
         return null; // false
      
      Module.falm.createClientThread(connect_num);
      return node;
   	
   }
   
}

