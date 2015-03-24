   package reload.Topology;
   
   import reload.Message.*;
   import reload.Topology.Plugins.ChordThread;
   import reload.Common.*;
   
	// This is a fake thread
   public class TopologyThread{
   
   // If Chord
      ChordThread thread;
      
      public TopologyThread(){
      
         thread = Module.tpi.getTopologyThread();
      
      }
         
      public void setMessageAndNotify(Message message){
      
         synchronized(thread){
         
            thread.setMessage(message);
         
            thread.notify();
         
         }
      
      }
         
   }

