   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   
	
   public class DictionaryEntryMeta{
   
   
      private DictionaryKey key;
      private MetaData value;
   
      
      
      public DictionaryEntryMeta (DictionaryKey key, MetaData value){
      
         this.key = key;
         this.value = value;
      
      }
      
      public DictionaryEntryMeta (byte[] data) throws Exception{
      
         key = new DictionaryKey(data, false);
         int length = key.getBytes().length;
         value = new MetaData(Utils.cutArray(data, length));
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(key.getBytes());
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
   
      public DictionaryKey getKey(){
         
         return key;
      	
      }
      	
      public MetaData getValue(){
      
         return value;
      
      }
      
   }