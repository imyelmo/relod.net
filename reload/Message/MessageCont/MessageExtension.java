   package reload.Message.MessageCont;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class MessageExtension{
   
      private MessageExtensionType type;
      private boolean critical;
      private Opaque extension_contents; //<0..2^32-1>
      
   
      
      public MessageExtension (MessageExtensionType type, boolean critical, byte[] extension_contents) throws ReloadException{
      
         this.type=type;
         this.critical=critical;
         this.extension_contents = new Opaque(32, extension_contents);
         
      }
    
      public MessageExtension (byte[] data) throws ReloadException{
      
         type = MessageExtensionType.valueOf(Utils.toShort(data, 0));
         critical = Utils.toBoolean(data[2]);
         extension_contents = new Opaque(32, data, 3);
         
      }
     
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(Utils.toByte(critical));
         baos.write(extension_contents.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public MessageExtensionType getType(){
      
         return type;
      
      }
      
      public boolean getCritical(){
      
         return critical;
      
      }
      
      public byte[] getExtensionContents() throws IOException{
      
         return extension_contents.getContent();
      
      }
   
   }