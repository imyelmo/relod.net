   package reload.Message.MessageCont;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class MessageContents{
   
   
      private short message_code;
      private Opaque message_body; //<0..2^32-1>
      private int extensions_length;
      private MessageExtension[] extensions; //<0..2^32-1>
            
   
      
      public MessageContents (short message_code, byte[] message_body, MessageExtension[] extensions) throws Exception{
      
         this.message_code = message_code;
         this.message_body = new Opaque(32, message_body);
      	
         this.extensions = extensions;
         
         for (int i=0; i<extensions.length; i++)
            extensions_length += extensions[i].getBytes().length;
            
         if(extensions_length > Math.pow(2, 32)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public MessageContents (short message_code, byte[] message_body) throws ReloadException{
      
         this.message_code = message_code;
         this.message_body = new Opaque(32, message_body);
         this.extensions = new MessageExtension[0];
         
         extensions_length = 0;
         
      }
      
      public MessageContents (byte[] data) throws Exception {
      
         message_code = Utils.toShort(data, 0);
         int offset = 2;  
      	
         message_body = new Opaque(32, data, offset);
         offset += message_body.getBytes().length;
        
         extensions_length = Utils.toInt(data, offset);
         offset += 4;
         
         data = Utils.cutArray(data, extensions_length, offset);
         offset = 0;
         
         int num = Algorithm.counter(4, data, 3);
      	
         extensions = new MessageExtension[num];
         
         for (int i=0; i<num; i++){
         
            extensions[i] = new MessageExtension(Utils.cutArray(data, offset));
            offset += extensions[i].getBytes().length;
         }
      
      }
   
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(message_code));
         baos.write(message_body.getBytes());
         baos.write(Utils.toByte(extensions_length));
         
         for (int i=0; i<extensions.length; i++)
            baos.write(extensions[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public short getMessageCode(){
      
         return message_code;
      
      }
   	
      public byte[] getMessageBody() throws IOException{
      
         return message_body.getContent();
      
      }
      
      public MessageExtension[] getExtensions(){
      
         return extensions;
      
      }
   
   }