   package reload.Storage;
   
   import reload.Message.*;
   import reload.Storage.Task.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*;
   
   public class StorageThread extends Thread {
   
   
      private ArrayDeque<StorageTask> queue; 
      private boolean empty;
      private DataStructure[][] ds;
   
   
      public StorageThread(){
      
         queue = new ArrayDeque<StorageTask>();
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
            
               StorageTask task = queue.getFirst();
               TaskType type = task.getType();
            
               synchronized(Module.si){
               
                  switch(type.getValue()){
                  
                     case 0:
                        throw new WrongTypeReloadException("Undefined Task");
                     case 1:
                        JoiningStoreTask stor = (JoiningStoreTask)task;
                        stor.start();  
                        break;
                     case 2:
                        AppStoreTask store = (AppStoreTask)task;
                        store.start();  
                        break;
                     case 3:
                        AppFetchTask fetch = (AppFetchTask)task;
                        ds = fetch.start();  
                        Module.si.notify();
                        break;
                     default:
                        throw new WrongTypeReloadException();
                  }
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
            
      public void add(StorageTask task){
      
         queue.add(task);
      
      }
      
      public StorageTask getFirst(){
      
         return queue.getFirst();
      
      }
      
      public DataStructure[][] getDataStructure(){
      
         return ds;
      
      }
      
      public void setMessage(Message message){
      
         queue.getFirst().setMessage(message);
      
      }
         
   }

