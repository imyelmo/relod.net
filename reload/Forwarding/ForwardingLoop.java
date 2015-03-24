   package reload.Forwarding;
   
   import reload.Link.LinkInterface;
   

   public class ForwardingLoop extends Thread {
   
      private ForwardingCheck fc;
      private LinkInterface server;
      
   	
      public ForwardingLoop(ForwardingCheck fc, LinkInterface server){
      
         this.fc = fc;
         this.server = server;
      
      }
   
      public void run(){
      
         while(true){
         
            ReloadThread st = new ReloadThread(fc, server);
            st.start();
            
            synchronized(st){
               try{
                  st.wait();
               }
                  catch(InterruptedException e){
                  
                     e.printStackTrace();
                  
                  }
            }
         
         }
      
      }
          
   }

