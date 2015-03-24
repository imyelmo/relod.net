   package reload.Common;

   import java.util.UUID;
   import java.io.*;
   import java.math.BigInteger;
   import reload.Message.Forwarding.DestinationType;
   import reload.Common.Exception.*;
	
   public class NodeId extends Id{
   
      
      private int length;
         
      
      public NodeId(byte[] data, boolean upper) throws Exception{ //128 to 160 bit
      
         XML configuration = new XML();
         length = configuration.getNodeIDLength();
         
         if(length < 16 || length > 20)
            throw new WrongLengthReloadException();
      
         if(upper){
            
            if(data.length != length)
               throw new WrongLengthReloadException();
               
            id = data;
            
         }
         
         else
            id = Utils.cutArray(data, length, 0);
         
         type = DestinationType.node; // Extended
      
      }
      
      public NodeId(BigInteger data) throws Exception{ //128 to 160 bit, positive BigInteger ONLY
      
      
         if(data.signum()==-1) 
            throw new NumberFormatException("Negative BigInteger not allowed.");
      
      
         XML configuration = new XML();
         length = configuration.getNodeIDLength();
      
         if(length < 16 || length > 20)
            throw new WrongLengthReloadException();
            
         if((float)(data.bitLength())/8 > length)
            throw new WrongLengthReloadException();
                
      
         id = new byte[length]; //16
         byte[] temp = data.toByteArray();
         
      
         if(temp.length == length+1){ //17
         
            if (temp[0] != 0)
               throw new NumberFormatException("Fatal Error."); // Should not occur.
         
            for (int i=length-1; i>=0; i--) //15
               id[i] = temp[i+1];
         }
         
         else{
         
            for (int i=length-1, j=temp.length-1; j>=0; i--, j--) //15
               id[i] = temp[j];
         }
         
         type = DestinationType.node; // Extended
      
      }
      
      public NodeId(int data) throws Exception{ // For testing
      
         this(BigInteger.valueOf(data)); 
      
      }
      
      public NodeId() throws Exception{ //Empty Node-ID
      
         this(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, true);
      	 
      }
      
      public NodeId(UUID data) throws Exception{ // Supposing 128 bits and upper call
      
         XML configuration = new XML();
         length = configuration.getNodeIDLength();
      
         if(length != 16)
            throw new WrongLengthReloadException();
      
         id = Utils.toByte(data); 
         
         type = DestinationType.node; // Extended
      
      }
      
      public NodeId sum(int num) throws Exception{
      
         NodeId node = new NodeId(id, true);
         node.add(num);
         return node;
      
      }
      
      public boolean isEmpty(){
               
         if(Utils.equals(id, new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}))
            return true;
         
         else
            return false;
      	 
      }
      
      public boolean isWildcard(){ // Wildcard node-ID
               
         if(Utils.equals(id, ones))
            return true;
         
         else
            return false;
      	 
      }
      
      public void setOnes(){
      
         id = ones;
      
      }
      
      public static int getLength() throws Exception{
      
         XML configuration = new XML();
         int length = configuration.getNodeIDLength();
      
         return length;
      
      }
      
      public byte[] getBytes(){
      
         return id;
      
      }
   
   }