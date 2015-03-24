   package reload.Forwarding.Task;
   
   import reload.Message.*;
   import reload.Forwarding.*;
   import reload.Common.*;
   import reload.Common.Exception.*;

   public class ForwardingTask{
   
         
      private Message receivedMessage;
      
      private ForwardingThread thread;
      
      protected TaskType type;
      
   	
      public ForwardingTask(ForwardingThread thread){
      
         this.thread = thread;
         
         type = type.none;
            
      }  
      
      public void start() throws Exception{
            
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

