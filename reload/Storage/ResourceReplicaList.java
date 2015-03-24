	
   package reload.Storage;
	
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*;
 
   public class ResourceReplicaList{
   
      private ArrayList<ResourceId> resource_id;
      private ArrayList<Byte> replica;
      
   	
      public ResourceReplicaList(){
      
         resource_id = new ArrayList<ResourceId>();
         replica = new ArrayList<Byte>();
      
      }
      
      public synchronized void add(ResourceId id, byte replica) throws ReloadException{
      
         if(contains(id))
            delete(id);
                
         resource_id.add(id);
         this.replica.add(replica);
      
      }
      
      public byte getReplica(ResourceId rid) throws ReloadException{
        
         int pos = resourceIndexOf(rid);
            
         if(pos == -1)
            return -1; 
               
         else 
            return replica.get(pos);
               
      }
      
      public ResourceId[] getResources() throws ReloadException{
         
         return resource_id.toArray(new ResourceId[0]);
      
      }
      
      public synchronized boolean delete(ResourceId rid){
      
         int pos = resource_id.indexOf(rid.getId());
            
         if(pos == -1)
            return false; 
               
         resource_id.remove(pos);
         replica.remove(pos);
         
         return true;
      
      }
      
      public boolean contains(ResourceId rid) throws ReloadException{
      
         if(resourceIndexOf(rid) == -1)
            return false;
         
         else
            return true;	
      
      }
      
      public int resourceIndexOf(ResourceId o) throws ReloadException{
      
         int pos = 0;
      
         ListIterator it;
      
         try{
            it = resource_id.listIterator(pos);
         }
            catch(IndexOutOfBoundsException ioobe){
               return -1;
            }
         
         for(int i=0; it.hasNext(); i++){
         
            ResourceId current = (ResourceId)it.next();
         
            if(current.equals(o))
               return pos+i;
         
         }
         
         return -1;
      
      }
      
      public String print() throws Exception{
      
         String ret = new String();
         int size = resource_id.size();
         
         if(size == 0)
            ret += "<Empty>";
         else
            ret += "#\tResource-ID\t\t\t\tReplica\n"; 
      
         for(int i=0; i<size; i++){
         
            ret += (i+1) + "\t0x";
               
            byte[] rid = resource_id.get(i).getId();
            for(int k=0; k<rid.length; k++)
               ret += String.format("%02x", rid[k]);
                  
            ret += "\t0x";
            
            byte rep = replica.get(i);
            ret += rep;
               
            ret += "\n";
            
         
         }
      
         return ret;
      
      }
         
   }