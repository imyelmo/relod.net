   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   
   public class DictionaryEntry{
   
   
      private DictionaryKey key;
      private DataValue value;
   
      
      
      public DictionaryEntry (DictionaryKey key, DataValue value){
      
         this.key = key;
         this.value = value;
      
      }
      
      public DictionaryEntry (byte[] data) throws Exception{
      
         key = new DictionaryKey(data, false);
         int length = key.getBytes().length;
         value = new DataValue(Utils.cutArray(data, length));
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(key.getBytes());
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
   
         public DictionaryKey getDictionaryKey(){
      
         return key;
      
      }
   
      public DataValue getDataValue(){
      
         return value;
      
      }
      
   }