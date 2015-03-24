   package reload.Message.Security;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
 
   public class SignerIdentity{
   
   
      private SignerIdentityType identity_type;
      private short length;
      private SignerIdentityValue identity;
   
      
      public SignerIdentity (SignerIdentityType identity_type, SignerIdentityValue identity) throws IOException{
      
         this.identity_type = identity_type;
         this.identity = identity;
      	
         length = (short)(identity.getBytes().length);
         
      }
      
      public SignerIdentity (SignerIdentityType identity_type) { //SignerIdentityValue empty
      
         this.identity_type = identity_type;
         length = 0;
         this.identity = new SignerIdentityValue(); //Empty  
      	
      }
      
      public SignerIdentity (byte[] data) throws ReloadException {
      
         identity_type = SignerIdentityType.valueOf(data[0]);
         length = Utils.toShort(data, 1);
         
         if (length > 0)
            identity = new SignerIdentityValue(Utils.cutArray(data, length, 3), identity_type);
            
         else
            identity = new SignerIdentityValue(); //Empty  
      
      }
      
      public byte[] getBytes() throws IOException{
      
         
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(identity_type.getBytes());
         baos.write(Utils.toByte(length));
         baos.write(identity.getBytes());
         
         return baos.toByteArray();
      }
   
      public SignerIdentityType getType(){
      
         return identity_type;
      
      }
      
      public SignerIdentityValue getValue(){
      
         return identity;
      
      }
   
   }