   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class DataValue{
   
      private boolean exists;
      private Opaque value; //32-bit
   
      
      public DataValue (boolean exists, byte[] value) throws ReloadException{
      
         this.exists = exists;
         this.value = new Opaque(32, value);
      
      }
      
      public DataValue (boolean exists, Opaque value) throws ReloadException{
      
         this.exists = exists;
         this.value = value;
         
         if(value.getBits() != 32)
            throw new WrongLengthReloadException();
      
      }
      
      public DataValue (byte[] data) throws ReloadException{
      
         exists = Utils.toBoolean(data[0]);
         value = new Opaque(32, data, 1);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(exists));
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
   
      public boolean getExists(){
      
         return exists;
      
      }
   
      public byte[] getValue(){
      
         return value.getContent();
      
      }
      
      public Opaque getOpaqueValue(){
      
         return value;
      
      }
   
   }