   package reload.Message;

   import java.util.*;
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Message.Forwarding.*;
   import reload.Message.MessageCont.*;
   import reload.Message.Security.*;
   import reload.Message.Security.TLS.*;

   
   public class MessageStructure {
   
      private ForwardingHeader fh;
      private MessageContents mc;
      private SecurityBlock sb;
   
   
   
      public MessageStructure(ForwardingHeader fh, MessageContents mc, SecurityBlock sb) throws Exception{ //Standard message
      
         this.fh=fh;
         this.mc=mc;
         this.sb=sb;
         
         setSize();
      
      }
      
      public MessageStructure(byte[] data) throws Exception{
      
         fh = new ForwardingHeader(data);
         
         int length = fh.getLength();
         data = Utils.cutArray(data, length, 0);
         
         int fhLength = fh.getBytes().length;
         
         if(fhLength >= length)
            throw new WrongLengthReloadException("Inconsistent length field.");
         
         mc = new MessageContents(Utils.cutArray(data, fhLength));
         
         int mcLength = mc.getBytes().length;
         
         sb = new SecurityBlock(Utils.cutArray(data, fhLength+mcLength));
               
      }
      
      private void setSize() throws Exception{
      
         int sizeFH = fh.getBytes().length;
         int sizeMC = mc.getBytes().length;
         int sizeSB = sb.getBytes().length;
      	
         fh.setLength(sizeFH+sizeMC+sizeSB);
         
      }
          
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(fh.getBytes());
         baos.write(mc.getBytes());
         baos.write(sb.getBytes());
         
         return baos.toByteArray();
      
      }   
      
      public ForwardingHeader getForwardingHeader(){
      
         return fh;
      }
   	
      public MessageContents getMessageContents(){
      
         return mc;
      }
   	
      public SecurityBlock getSecurityBlock(){
      
         return sb;
      }
   
   
   }