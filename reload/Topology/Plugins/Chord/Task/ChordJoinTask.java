   package reload.Topology.Plugins.Chord.Task;
   
   import reload.Message.*;
   import reload.Topology.Plugins.ChordThread;
   import reload.Topology.Plugins.Chord.*;
   import reload.Common.*;
   import reload.Common.Exception.*;

   public class ChordJoinTask extends ChordTask{
   
      private NodeId node;
   
      public ChordJoinTask(ChordThread thread, NodeId node){
      
         super(thread);
         
         type = ChordTaskType.join;
         
         this.node = node;
         
      }
      
      public void start() throws Exception{
         
         send_receive(node);
         
      }
      
      private void send_receive(NodeId node) throws Exception{
               
         ChordJoinReq req = new ChordJoinReq(Module.tpi.routingTable.getThisNode());
                  
         byte[] msg_body = req.getBytes();
         
         short code = 15; //JoinReq
         
         Module.msg.send(msg_body, code, node);
         
               	
         Message mes = receive();
            
         code = mes.getMessageCode();
         	
         if (code != 16) // JoinAns
            throw new WrongPacketReloadException(16);
         
         
         msg_body = mes.getMessageBody();
            	
         ChordJoinAns join = new ChordJoinAns(msg_body);
      
      }
      
   }

