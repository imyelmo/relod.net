   package reload.Forwarding.Request;
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class IceExtension{
   
   
      private Opaque name; //<0..2^16-1>
      private Opaque value; //<0..2^16-1>
      
   	   
      
      public IceExtension (byte[] name, byte[] value) throws ReloadException{
      
         this.name = new Opaque(16, name);
         this.value = new Opaque(16, value);
      
      }
      
      public IceExtension (byte[] data) throws Exception{
      
      
         name = new Opaque(16, data, 0);
         
         int offset = name.getBytes().length;
      
         name = new Opaque(16, data, offset);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(name.getBytes());
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
   
   }