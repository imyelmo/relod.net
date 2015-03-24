   package reload.Message.Security;

   import java.io.*;
   import reload.Message.Security.TLS.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
	
   public class Signature{
   
      private SignatureAndHashAlgorithm algorithm;
      private SignerIdentity identity;
      private Opaque signature_value; //<0..2^16-1>
        
      public Signature (SignatureAndHashAlgorithm algorithm, SignerIdentity identity, byte[] signature_value) throws ReloadException{
      
         this.algorithm = algorithm;
         this.identity = identity;
         this.signature_value = new Opaque(16, signature_value);
      
      }
      
      public Signature (byte[] data) throws Exception{
         
         algorithm = new SignatureAndHashAlgorithm(data); // Size: 2 bytes
         identity = new SignerIdentity(Utils.cutArray(data, 2));
         
         int lengthIdentity = identity.getBytes().length;
         signature_value = new Opaque(16, data, lengthIdentity+2);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(algorithm.getBytes());
         baos.write(identity.getBytes());
         baos.write(signature_value.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public SignerIdentity getIdentity(){
      
         return identity;
      
      }
   
   
   }