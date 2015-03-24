   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ArrayEntryMeta{
   
   
      private int index;
      private MetaData value;
   
      
      
      public ArrayEntryMeta (int index, MetaData value){
      
         this.index = index;
         this.value = value;
      
      }
      
      public ArrayEntryMeta (byte[] data) throws ReloadException{
      
         index = Utils.toInt(data, 0);
         value = new MetaData(Utils.cutArray(data, 4));
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(index));
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
   
      public int getIndex(){
         
         return index;
      	
      }
      	
      public MetaData getValue(){
      
         return value;
      
      }
   	
   }