   package reload.Message.Forwarding;
	
   import java.io.*;
   import reload.Common.*;
   
   public class ForwardingOptions{
   
   
      private ForwardingOptionsType type;
      private byte flags;
      private short length;
      
   
      
      public ForwardingOptions(ForwardingOptionsType type, byte flags){
      
         this.type = type;
         this.flags = flags;
         this.length = 0;
      
      }
      
      public ForwardingOptions(byte[] data){
      
         type = ForwardingOptionsType.valueOf(data[0]);
         flags = data[1];
         length = Utils.toShort(data, 2);
      
      }
   
         
      public byte[] getBytes() throws IOException{
      
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(flags);
         baos.write(Utils.toByte(length));
         
         switch(type.getBytes()){
            case 0:
            /* This type may be extended */
               break;
         }
      
         return baos.toByteArray();
      
      }   
      
      public ForwardingOptionsType getType(){
      
         return type;
      
      }
      
      public byte getFlags(){
      
         return flags;
      
      }
      
   }