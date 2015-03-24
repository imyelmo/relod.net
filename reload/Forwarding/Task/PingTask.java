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

