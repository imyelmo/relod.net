   package reload.Topology.Plugins.Chord.Task;
   
   import reload.Message.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Topology.Plugins.*;

   public class ChordTask{
   
         
      private Message receivedMessage;
      
      private ChordThread thread;
      
      protected ChordTaskType type;
      
   	
      public ChordTask(ChordThread thread){
      
         this.thread = thread;
         
         type = ChordTaskType.none;
            
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
      
      public ChordTaskType getType(){
      
         return type;
      
      }
      
   }

