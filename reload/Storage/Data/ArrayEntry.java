   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ArrayEntry{
   
   
      private int index;
      private DataValue value;
   
      
      
      public ArrayEntry (int index, DataValue value){
      
         this.index = index;
         this.value = value;
      
      }
      
      public ArrayEntry (byte[] data) throws ReloadException{
      
         index = Utils.toInt(data, 0);
         value = new DataValue(Utils.cutArray(data, 4));
      
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
   
      public DataValue getDataValue(){
      
         return value;
      
      }
   
   }