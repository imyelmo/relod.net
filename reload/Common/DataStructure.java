
   package reload.Common;
	
   import reload.Storage.Data.DictionaryKey;	
 
   public class DataStructure{
   
      private DictionaryKey key; // For Dictionary only
      private long storage_time;
      private int life_time;
      private boolean exists;
      private Opaque value;
   	
      private boolean dictionary;
   
   	
      public DataStructure(DictionaryKey key, long storage_time, int life_time, boolean exists, Opaque value){
      
         this.key = key;
         this.storage_time = storage_time;
         this.life_time = life_time;
         this.exists = exists;
         this.value = value;
         
         dictionary = true;
        
      }
      
      public DataStructure(long storage_time, int life_time, boolean exists, Opaque value){
      
         this.storage_time = storage_time;
         this.life_time = life_time;
         this.exists = exists;
         this.value = value;
         
         dictionary = false;
        
      }
      
      public DataStructure(){  // Empty, exists=false
      
      }
      
      public DictionaryKey getDictionaryKey(){
      
         return key;
      
      }
   
      public long getStorageTime(){
      
         return storage_time;
               
      }
      
      public int getLifeTime(){
      
            
         return life_time;
               
      }
      
      public boolean getExists(){
      
            
         return exists;
                    
      }
      
      public Opaque getValue(){
            
         return value;
               
      }
      
      public boolean isDictionary(){
      
         return dictionary;
      
      }
      
      public String print(){
      
         String ret = new String();
      
         if(dictionary){
            byte[] id = key.getKey();
            ret +="0x";
            for(int k=0; k<id.length; k++)
               ret += String.format("%02x", id[k]);
         }
         
         ret += "\t";
      	
         byte[] id = value.getContent();
         ret +="0x";
         for(int k=0; k<id.length; k++)
            ret += String.format("%02x", id[k]);
      
         ret += "\n";
      
         return ret;
      
      }
         
   }