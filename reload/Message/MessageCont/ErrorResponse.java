   package reload.Message.MessageCont;
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
 
   public class ErrorResponse{
   
      private short error_code;
      private Opaque error_info; //<0..2^16-1>
      
      public ErrorResponse (short error_code, byte[] error_info) throws ReloadException{
      
         this.error_code = error_code;
         this.error_info = new Opaque(16, error_info);
      
      }
      
      public ErrorResponse (short error_code) throws ReloadException{ // error_info is optional
      
         this.error_code = error_code;
         this.error_info = new Opaque(16, new byte[0]);
      
      }
      
      public ErrorResponse (byte[] data) throws ReloadException{
      
         this.error_code = Utils.toShort(data, 0);
         this.error_info = new Opaque(16, data, 2);
      
      }
   	
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(error_code));
         baos.write(error_info.getBytes());
      
         return baos.toByteArray();
      
      }
   
      public short getErrorCode(){
      
         return error_code;
      
      }
      
      public byte[] getErrorInfo() throws IOException{
      
         return error_info.getContent();
      
      }
   
   }