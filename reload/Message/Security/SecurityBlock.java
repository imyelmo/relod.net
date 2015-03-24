   package reload.Message.Security;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
		
   public class SecurityBlock{
   
      private short cert_length;
      private GenericCertificate[] certificates; //16-bit
      private Signature signature;
      
   
      
      public SecurityBlock (GenericCertificate[] certificates, Signature signature) throws Exception{
      
         this.certificates = certificates;
         this.signature = signature;
         
         for (int i=0; i<certificates.length; i++)
            cert_length += certificates[i].getBytes().length;
         
         if(cert_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      }
      
      public SecurityBlock (byte[] data) throws Exception{
         
         cert_length = Utils.toShort(data, 0);
      	
         byte[] data_cert = Utils.cutArray(data, cert_length, 2);
         int offset = 0;
        
         int num = Algorithm.counter(2, data_cert, 1);
      	
         certificates = new GenericCertificate[num];
         
         for (int i=0; i<num; i++){
         
            certificates[i] = new GenericCertificate(Utils.cutArray(data_cert, offset));
            offset += certificates[i].getBytes().length;
         }
         
         signature = new Signature(Utils.cutArray(data, 2+offset));
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(cert_length));
      	
         for (int i=0; i<certificates.length; i++)
            baos.write(certificates[i].getBytes());
            
         baos.write(signature.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public Signature getSignature(){
      
         return signature;
      
      }
   
   }