   package reload.Storage.Task;
   
   import reload.Message.*;
   import reload.Message.Forwarding.Destination;	
   import reload.Storage.*;
   import reload.Storage.Data.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*;
   
   public class AppFetchTask extends StorageTask{
   
      private Destination[] destination;
      private FetchReq req;
      
   	
      public AppFetchTask(StorageThread thread, Destination[] destination, FetchReq req){
      
         super(thread);
      	
         type = TaskType.app_fetch;
         
         this.destination = destination;
         this.req = req;
            
      }  
      
      public DataStructure[][] start() throws Exception{
         
         Id[] id = new Id[destination.length];
      
         try{
         
            for(int i=0; i<destination.length; i++)
               id[i] = destination[i].getDestinationData().getId();
         }
            catch(WrongTypeReloadException e){
            
               throw new UnimplementedReloadException("Opaque ID type");
            
            }
         
               
         byte[] msg_body = req.getBytes();
         
         short code = 9; // FetchReq
         
         Module.msg.send(msg_body, code, id);
         
             
         Message mes = receive();
      
         code = mes.getMessageCode();
        
         if (code != 10) // FetchAns
            throw new WrongPacketReloadException(10);
         
         msg_body = mes.getMessageBody();
      	
      
         FetchAns ans = new FetchAns(msg_body);
      
        
         FetchKindResponse[] response = ans.getKindResponses();
      
         DataStructure[][] ds = new DataStructure[response.length][];
      
         
         for(int i=0; i<response.length; i++){
         
            KindId kind = response[i].getKind();
            long generation = response[i].getGeneration();
            
            StoredData[] value = response[i].getValues();
            
            
            switch (kind.getDataModel().getBytes()){
            
               case 1:
                  ds[i] = new DataStructure[1];
                  ds[i][0] = value[0].getData(DataModel.single_value);
                  break;
               case 2:
                  ds[i] = getArrayData(value);
                  break;
               case 3:
                  ds[i] = new DataStructure[value.length];
                  
                  for(int j=0; j<value.length; j++){
                     ds[i][j] = value[j].getData(DataModel.dictionary);
                     //Signature signature = value[j].getSignature();              
                  } 
                  break;
            }
         
         }
               
      
         return ds;
      
      }
      
      private DataStructure[] getArrayData(StoredData[] value) throws ReloadException{
      
         int max = getArrayLength(value);
         
         if(max == -1)
            return null; 
            
         int size = max+1;
         
         DataStructure[] ds = new DataStructure[size];
         
         for(int i=0; i<value.length; i++){
         
            DataStructure current = value[i].getData(DataModel.array);
            int index = value[i].getIndex();
            
            ds[index] = current;
         }
      	
      	   
         return ds;
      
      }
      
      private int getArrayLength(StoredData[] value){ // Real length is return+1
      
         int max = -1;
         
         for(int i=0; i<value.length; i++){
               
            int index = value[i].getIndex();
            
            if(index>max)
               max = index;
         
         }
         
         return max;
      	
      }
      
   }

