   package reload.Topology.Plugins.Chord.Task;
   
   import reload.Message.*;
   import reload.Topology.Plugins.ChordThread;
   import reload.Topology.Plugins.Chord.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*;

   public class ChordUpdateTask extends ChordTask{
   
   
      private NodeId dest;
      private int numLink;
      private boolean client; 
      private ChordUpdateType updateType; 
      private boolean sendData;  //Send data when link is up 
      
      private boolean all_peer;
   	
   	
      public ChordUpdateTask(ChordThread thread, NodeId dest, int numLink, boolean client, ChordUpdateType updateType){
      
         super(thread);
      	
         type = ChordTaskType.update;
         
         this.dest = dest;
         this.numLink = numLink;
         this.client = client;
         this.updateType = updateType;
         sendData = true;
         
         all_peer = false;
            
      }
      
      public ChordUpdateTask(ChordThread thread, NodeId dest, ChordUpdateType updateType){
      
         super(thread);
      	
         type = ChordTaskType.update;
         
         this.dest = dest;
         this.updateType = updateType;
         sendData = false;
         
         all_peer = false;
            
      }
      
      public ChordUpdateTask(ChordThread thread){
      
         super(thread);
      	
         type = ChordTaskType.update;
         
         sendData = false;
                  
         all_peer = true;
            
      }	
      
      public void start() throws Exception{
      
         int this_uptime = 0; // Pending
      
         if(!all_peer)
            send_receive(dest, this_uptime, updateType);
         
         else{
         
            ArrayList<NodeId> node = Module.tpi.routingTable.getNodes();
         
            for(int i=0; i<node.size(); i++)
               send_receive(node.get(i), this_uptime, ChordUpdateType.peer_ready);
         }
         
         if(all_peer) // Inicialization
            synchronized(Module.falm){
               Module.falm.notify();
            }
         
      }
      
      public void send_receive(NodeId node, int this_uptime, ChordUpdateType Utype) throws Exception{
            	
         NodeId[] fingers = null;
         NodeId[] predecessors = null;
         NodeId[] successors = null;
      
         switch(Utype.getBytes()){
         
            case 3:
               fingers = Module.tpi.routingTable.getFingersForSending();
            case 2:
               predecessors = Module.tpi.routingTable.getPredecessors();
               successors = Module.tpi.routingTable.getSuccessors(); 
            case 1:
               break;    
            default:
               throw new WrongTypeReloadException();
         }
         
         ChordUpdateReq req = null;
         
         switch(Utype.getBytes()){
         
            case 1:
               req = new ChordUpdateReq(this_uptime);
               break;
            case 2:
               req = new ChordUpdateReq(this_uptime, predecessors, successors);
               break;
            case 3:
               req = new ChordUpdateReq(this_uptime, predecessors, successors, fingers);
               break;    
         }
         
         byte[] msg_body = req.getBytes();
         
         short code = 19; //UpdateReq
      
         if(!sendData)
            Module.msg.send(msg_body, code, node);
         else{
            Message msg = new Message(msg_body, node, Module.tpi.routingTable.getThisNode(), code);
            byte[] data_msg = msg.getBytes();
            Module.falm.sendData.add(numLink, client, data_msg);
         }  
               
         Message mes = receive();
            
         code = mes.getMessageCode();
      		
         if (code != 20) // UpdateAns
            throw new WrongPacketReloadException(20);
         
      	
         msg_body = mes.getMessageBody();
      	
         ChordUpdateAns ans = new ChordUpdateAns(msg_body);
         
         boolean response = ans.getResponse();
               	
      }
      
   }

