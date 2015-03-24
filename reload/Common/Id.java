   package reload.Common;

   import reload.Message.Forwarding.DestinationType;
   import java.math.BigInteger;
   import reload.Common.Exception.*;
   
   public class Id{
   
      protected byte[] id;
   
      protected DestinationType type;
      
      public final static byte[] ones = {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
   
      	
      public Id(){
      
         type = DestinationType.UNDEFINED;
      
      }
      
      public boolean equals(Id node) throws NotTrueNorFalseReloadException{
      
         if(node==null)
            throw new NotTrueNorFalseReloadException();
      
         byte[] idn = node.getId();
      
         if(id.length != idn.length)
            return false;
      
         for (int i=0; i<id.length; i++){
         
            if(id[i] != idn[i])
               return false;
         
         }
       
         return true;  
      	
      }
      
      public boolean lt(Id node) throws NotTrueNorFalseReloadException{
      
         if(node==null)
            throw new NotTrueNorFalseReloadException();
      
         BigInteger b1 = new BigInteger(1, id);
         BigInteger b2 = new BigInteger(1, node.getId());
         
      	
         if(b1.compareTo(b2) == -1)
            return true;
         
         else
            return false;
      
      }
      
      public boolean lte(Id node) throws NotTrueNorFalseReloadException{
       
      	
         if(lt(node) || equals(node))
            return true;
         
         else
            return false;
      
      }
   	
      public boolean gt(Id node) throws NotTrueNorFalseReloadException{
      
         if(node==null)
            throw new NotTrueNorFalseReloadException();
      
      
         BigInteger b1 = new BigInteger(1, id);
         BigInteger b2 = new BigInteger(1, node.getId());
      
      
         if(b1.compareTo(b2) == 1)
            return true;
         
         else
            return false;
      
      }
      
      public void add(BigInteger num) throws Exception{
      
         if(num.signum()==-1) 
            throw new NumberFormatException("Negative BigInteger not allowed.");
            
         if (num.bitLength() > 128)
            throw new WrongLengthReloadException();
      
         BigInteger b = new BigInteger(1, id);
      
         BigInteger sum = b.add(num);
         
         if(type != DestinationType.node && type != DestinationType.resource)
            throw new WrongTypeReloadException();
         
         if(sum.compareTo(new BigInteger(1,ones)) == 1){
         
            BigInteger bi = sum.subtract(new BigInteger(1,ones)).subtract(BigInteger.ONE);
            id = toByteArray(bi);
               
         }
         else
            id = toByteArray(sum);
         
      }
      
      public void add(int num) throws Exception{
      
         add(BigInteger.valueOf(num));
      
      }
      
   	/* Returns shorter distance in a positive or negative value. Module is the distance (same as distanceAbs), positive signum means for clockwise and negative means against clockwise. */
      public BigInteger distance(Id node){
      
         boolean signum = false; // Positive
      
         BigInteger num1 = new BigInteger(1, id);
         BigInteger num2 = new BigInteger(1, node.getId());
         
         if (num1.compareTo(num2) == 1)
            signum = true; // Negative
      
         BigInteger a = num1.subtract(num2).abs();
         BigInteger b = a.subtract(new BigInteger(1,ones)).abs().add(BigInteger.ONE);
      	
         if (a.compareTo(b) == 1)
            signum = !signum; // Switch
            
         BigInteger res = a.min(b);
            
         if(signum) // Negative
            res = res.negate();
         				      
         return res;
      
      }
      
   	/* Returs shorter distance in absolute value. */
      public BigInteger distanceAbs(Id node){
      
         return distance(node).abs();
      
      }
      
   /* Returs distance always for clockwise, although is not the shorter one. */
      public BigInteger distancePos(Id node){
      
         BigInteger num1 = new BigInteger(1, id);
         BigInteger num2 = new BigInteger(1, node.getId());
         
         BigInteger res = num2.subtract(num1);
         if(res.signum() == -1)
            res = res.add(new BigInteger(1,ones)).add(BigInteger.ONE);
            //res = res.add(BigInteger.valueOf(100));
         				      
         return res;
      
      }
      
      public String print(){
      
         String ret = new String("0x");
      
         for(int i=0; i<id.length; i++)
            ret += String.format("%02x", id[i]);
      
         return ret;
      
      }
      
      public byte[] getId(){
      
         return id;
      
      }
      
      public DestinationType getType(){
      
         return type;
      
      }
      
      protected byte[] toByteArray(BigInteger bi){
      
         byte[] result = new byte[16];
         byte[] temp = bi.toByteArray();
         
         
         if(temp.length == 17){
         
            if (temp[0] != 0)
               throw new NumberFormatException("Fatal Error."); // Should not occur.
         
            for (int i=15; i>=0; i--)
               result[i] = temp[i+1];
         }
         
         else{
         
            for (int i=15, j=temp.length-1; j>=0; i--, j--)
               result[i] = temp[j];
         }
      	
         return result;
      	
      }
   
   }