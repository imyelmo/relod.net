   package reload.Link;
   
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.security.MessageDigest; 

 
   public class FramedMessage{
   
   
      private FramedMessageType type;
      
      private int sequence;
      private Opaque message; // 24-bit  
   
      private int ack_sequence; 
      private int received;
   
   
      
      public FramedMessage (int sequence, byte[] message) throws ReloadException{
      
         type = FramedMessageType.data;
         this.sequence = sequence;
         this.message = new Opaque(24, message);
         
      }
      
      public FramedMessage (int ack_sequence, int received){
      
         type = FramedMessageType.ack;
         this.ack_sequence = ack_sequence;
         this.received = received;
         
      }
      
      public FramedMessage (byte[] data) throws ReloadException{
      
         type = FramedMessageType.valueOf(data[0]);
         
         switch(type.getBytes()){
         
            case (byte)128:
               sequence = Utils.toInt(data, 1);
               message = new Opaque(24, data, 5);
               break;
            	
            case (byte)129:
               ack_sequence = Utils.toInt(data, 1);
               received = Utils.toInt(data, 5);
               break;
               
            default:
               throw new WrongTypeReloadException();
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
      
         switch(type.getBytes()){
         
            case (byte)128:
               baos.write(Utils.toByte(sequence));
               baos.write(message.getBytes());
               break;
            	
            case (byte)129:
               baos.write(Utils.toByte(ack_sequence));
               baos.write(Utils.toByte(received));
               break;
               
            default:
               throw new WrongTypeReloadException();
         
         }
         
         return baos.toByteArray();
         
      }
      
      public FramedMessageType getType(){
      
         return type;
      
      }
      
      public int getSequence() throws ReloadException{
      
         if(type != FramedMessageType.data)
            throw new WrongTypeReloadException();
      
         return sequence;
      
      }
   	
   	
      public byte[] getMessage() throws ReloadException{
      
         if(type != FramedMessageType.data)
            throw new WrongTypeReloadException();
      
         return message.getContent();
      
      }	
   	
      public int getAckSequence() throws ReloadException{
      
         if(type != FramedMessageType.ack)
            throw new WrongTypeReloadException();
      
         return ack_sequence;
      
      }
   	
      public int getReceived() throws ReloadException{
      
         if(type != FramedMessageType.ack)
            throw new WrongTypeReloadException();
            
         return received;
      
      }
   
   }