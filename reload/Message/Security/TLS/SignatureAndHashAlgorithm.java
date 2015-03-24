   package reload.Message.Security.TLS;
	
   import java.io.*;
   
   
   public class SignatureAndHashAlgorithm{
   
      private HashAlgorithm hash;
      private SignatureAlgorithm signature;
           
   
      
      public SignatureAndHashAlgorithm (HashAlgorithm hash, SignatureAlgorithm signature){
      
         this.hash = hash;
         this.signature = signature;
         
      }
      
      public SignatureAndHashAlgorithm (byte[] data){
      
         hash = HashAlgorithm.valueOf(data[0]);
         signature = SignatureAlgorithm.valueOf(data[1]);
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(hash.getBytes());
         baos.write(signature.getBytes());
         
         return baos.toByteArray();
      
      }
   
   }