   package reload.Common;

   import java.util.Random;
   import java.math.BigInteger;
   import java.io.ByteArrayOutputStream;
   import reload.Message.Forwarding.DestinationType;
   import reload.Common.Exception.*;
   
	// Should be variable-length but it is a 128-bit number
  
   public class ResourceId extends Id{
   
      private static byte length = 16; // For Chord
   	
      //public final static byte[] ones = {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
   
   
      public ResourceId(byte[] data, boolean upper) throws WrongLengthReloadException{
      
         if(upper){
         
            if(data.length != 16) // Chord
               throw new WrongLengthReloadException();
          
            id = data;
            
         }
         
         else{
         
            id = Utils.cutArray(data, data[0], 1);
            length = data[0];
            
            if(length != 16) // Chord
               throw new WrongLengthReloadException();
            
         }
         
         type = DestinationType.resource; // Extended
      
      }
      
      public ResourceId (BigInteger id) throws Exception{
      
         if(id.signum()==-1) 
            throw new NumberFormatException("Negative BigInteger not allowed.");
            
         if (id.bitLength() > 128)
            throw new WrongLengthReloadException();
      
         this.id = toByteArray(id);
         
         type = DestinationType.resource; // Extended
         
      }
      
      public ResourceId (long id) throws Exception{
      
         this(BigInteger.valueOf(id));
         
      }
      
      public ResourceId() { // Random
      
         id = toByteArray(new BigInteger(128, new Random()));
         
         type = DestinationType.resource; // Extended
      
      }
   
   // Not equal to getId(), getBytes() returns length and id, while getId() returns id only
      public byte[] getBytes() throws java.io.IOException{
      
         //return id;
         
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(length);
         baos.write(id);
         
         return baos.toByteArray();
      	
      }
   
   }