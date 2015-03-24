package reload.Forwarding;

import reload.Message.*;
import reload.Forwarding.Task.*;
import reload.Common.*;
import reload.Common.Exception.*;

import java.util.*;

public class ForwardingThread extends Thread {

   private ArrayDeque<ForwardingTask> queue;
   
   private boolean empty;
      

   public ForwardingThread(){
   
      queue = new ArrayDeque<ForwardingTask>();
      
      empty = true;
         
   }
   
   public void run() {
   
      
      while(true){
            
         synchronized(this){
         
            if(isEmpty()){
            
               try{
                  wait();
               }
               catch(InterruptedException e){
                  
                  e.printStackTrace();
                  
               }
               
            }
         }
         
         try{
         
            ForwardingTask task = queue.getFirst();
            TaskType type = task.getType();
         
            switch(type.getValue()){
            
               case 0:
                  throw new WrongTypeReloadException("Undefined Task");
               case 1:
                  PingTask ping = (PingTask)task;
                  ping.start();  
                  break;
               case 2:
                  AttachRouteTask attach = (AttachRouteTask)task;
                  attach.start();
                  break;
               case 3:
                  AppAttachTask app_attach = (AppAttachTask)task;
                  app_attach.start();
                  break;
               default:
                  throw new WrongTypeReloadException();
            }
         
            queue.removeFirst();
         
         }
         catch(Exception e){
                  
            e.printStackTrace();
                  
         }
      	
         
      }
      
   }
	
   public boolean isEmpty(){
   
      return queue.size() == 0;
   
   }
   
   public void add(ForwardingTask task){
   
      queue.add(task);
   
   }
   
   public ForwardingTask getFirst(){
   
      return queue.getFirst();
   
   }

   public void setMessage(Message message){
   
      queue.getFirst().setMessage(message);
   
   }
      
}

