   package reload.Storage.Task;
   
   import reload.Message.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Storage.*;
   
   public class StorageTask{
   
         
      private Message receivedMessage;
      
      private StorageThread thread;
      
      protected TaskType type;
      
   	
      public StorageTask(StorageThread thread){
      
         this.thread = thread;
         
         type = TaskType.none;
            
      }  
      
      public Object start() throws Exception{
            
         throw new ReloadException("No task defined.");
         
      }
      
      protected Message receive() throws InterruptedException{
      
         synchronized(thread){
         
            thread.wait();
         
         }
         
         return receivedMessage;
      
      }
      
      public void setMessage(Message message){
      
         receivedMessage = message;
      
      }
      
      public TaskType getType(){
      
         return type;
      
      }
      
   }

