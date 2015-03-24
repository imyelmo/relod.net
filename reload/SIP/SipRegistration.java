   package reload.SIP;

   import java.io.*;
   import reload.Common.*;
   import reload.Message.Forwarding.Destination;
   
   public class SipRegistration{
   
      private SipRegistrationType type;
      private short length;
      private SipRegistrationData data;
   
      
      public SipRegistration (SipRegistrationType type, SipRegistrationData data) throws Exception{
      
         this.type = type;
         this.data = data;
         length = (short)data.getBytes().length;
      
      }
      
      public SipRegistration (byte[] Bdata) throws Exception{
      
         type = SipRegistrationType.valueOf(Bdata[0]);
         length = Utils.toShort(Bdata, 1);
         data = new SipRegistrationData(Utils.cutArray(Bdata, length, 3), type);
         
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
         baos.write(type.getValue());   
         baos.write(Utils.toByte(length));
         baos.write(data.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public SipRegistrationType getType(){
      
         return type;
      
      }
      
      public SipRegistrationData getData(){
      
         return data;
      
      }
   
   }