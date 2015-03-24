	
   package reload.Forwarding;
	
   import reload.Common.*;
   import java.util.*;
   import reload.Common.Exception.*;
 
   public class SendData{
   
      private ArrayList<Integer> num_link;
      private ArrayList<Boolean> client;
      private ArrayList<byte[]> data;
      
   	
      public SendData(){
      
         num_link = new ArrayList<Integer>();
         client = new ArrayList<Boolean>();
         data = new ArrayList<byte[]>();
      
      }
      
      public synchronized void add(int num_link, boolean client, byte[] data){
         
         this.num_link.add(num_link);
         this.client.add(client);
         this.data.add(data);
         
      }
      
      public byte[] getData(int num, boolean client) throws ReloadException{
        
         int pos1 = num_link.indexOf(num);
         int pos2 = num_link.lastIndexOf(num);
            
         if(pos1 == -1)
            return null; 
            
         if(pos1 == pos2){
            boolean cli = this.client.get(pos1);
            
            if(cli==client)
               return data.get(pos1);
            else
               return null;
         }
      
         if(this.client.get(pos1) == client)
            return data.get(pos1);
         else if (this.client.get(pos2) == client)
            return data.get(pos2);
         else
            throw new ReloadException("Incorrect data, duplicate boolean value.");
      	               
      }
        
      public synchronized boolean delete(int num, boolean client) throws ReloadException{
      
         int pos1 = num_link.indexOf(num);
         int pos2 = num_link.lastIndexOf(num);
            
         if(pos1 == -1)
            return false; 
            
         if(pos1 == pos2){
            boolean cli = this.client.get(pos1);
            
            if(cli==client){
               remove(pos1);
               return true;
            }
            else
               return false;
         }
         
         if(this.client.get(pos1) == client)
            remove(pos1);
         else if (this.client.get(pos2) == client)
            remove(pos2);
         else
            throw new ReloadException("Incorrect data, duplicate boolean value.");
            
         return true;
      
      }
      
      public void remove(int pos){
      
         num_link.remove(pos);
         client.remove(pos);
         data.remove(pos);
         
      }
      
      public boolean contains(int num, boolean client) throws ReloadException{
      
         if(getData(num, client) == null)
            return false;
         
         else
            return true;	
      
      }
     
   }