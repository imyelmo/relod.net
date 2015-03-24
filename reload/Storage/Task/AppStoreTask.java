   package reload.Storage.Task;
   
   import reload.Message.*;
   import reload.Message.Forwarding.Destination;	
   import reload.Storage.*;
   import reload.Storage.Data.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*;
   
   public class AppStoreTask extends StorageTask{
   
      private Destination[] destination;
      private StoreReq req;
      
   	
      public AppStoreTask(StorageThread thread, Destination[] destination, StoreReq req){
      
         super(thread);
      	
         type = TaskType.app_store;
         
         this.destination = destination;
         this.req = req;
            
      }  
      
      public Object start() throws Exception{
      
         Id[] id = new Id[destination.length];
      
         try{
         
            for(int i=0; i<destination.length; i++)
               id[i] = destination[i].getDestinationData().getId();
         }
            catch(WrongTypeReloadException e){
            
               throw new UnimplementedReloadException("Opaque ID type");
            
            }
         
               
         byte[] msg_body = req.getBytes();
         
         short code = 7; // StoreReq
         
         Module.msg.send(msg_body, code, id);
         
               
         Message mes = receive();
            
         code = mes.getMessageCode();
      		
         if (code != 8) // StoreAns
            throw new WrongPacketReloadException(8);
         
         msg_body = mes.getMessageBody();
      	
         StoreAns ans = new StoreAns(msg_body);
         
      	
         StoreKindResponse[] skr = ans.getStoreKindResponses();
      	
         for(int i=0; i<skr.length; i++){
         
            KindId kid = skr[i].getKind();
            
            long counter = skr[i].getGenerationCounter();
               
            NodeId[] replica = skr[i].getReplicas();
            
            for(int j=0; j<replica.length; j++){
               
               NodeId ID = replica[j];
                                		
            }
            
         }
               
         return null;
      
      }
      
   }

