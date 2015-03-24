   package reload.Message.Security;
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class GenericCertificate{
   
      private CertificateType type;
      private Opaque certificate; //16-bit   
   
      
      public GenericCertificate (CertificateType type, byte[] certificate) throws ReloadException{
      
         this.type = type;
         this.certificate = new Opaque (16, certificate);
         
      }
      
      public GenericCertificate (CertificateType type, String certificate) throws Exception{
      
         this(type, certificate.getBytes("US-ASCII"));
      
      }
   	
      
      public GenericCertificate (byte[] data) throws ReloadException{
      
         type = CertificateType.valueOf(data[0]);
         certificate = new Opaque (16, data, 1);
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(certificate.getBytes());
         
         return baos.toByteArray();
      
      }
   
   
   }