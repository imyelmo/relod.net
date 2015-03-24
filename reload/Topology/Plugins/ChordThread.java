package reload.Topology.Plugins;

import reload.Message.*;
import reload.Topology.Plugins.Chord.Task.*;
import reload.Common.*;
import reload.Common.Exception.*;

import java.util.*;


public class ChordThread extends Thread {


   private ArrayDeque<ChordTask> queue;
   
   private boolean empty;



   public ChordThread(){
   
      queue = new ArrayDeque<ChordTask>();
      
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
         
            ChordTask task = queue.getFirst();
            ChordTaskType type = task.getType();
         
            switch(type.getValue()){
            
               case 0:
                  throw new WrongTypeReloadException("Undefined Task");
               case 1:
                  ChordUpdateTask update = (ChordUpdateTask)task;
                  update.start();  
                  break;
               case 2:
                  ChordLeaveTask leave = (ChordLeaveTask)task;
                  leave.start(); 
                  break;
               case 3:
                  ChordJoinTask join = (ChordJoinTask)task;
                  join.start(); 
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
   
   public void add(ChordTask task){
   
      queue.add(task);
   
   }
   
   public ChordTask getFirst(){
   
      return queue.getFirst();
   
   }
   
   public void setMessage(Message message){
   
      queue.getFirst().setMessage(message);
   
   }
      
}

