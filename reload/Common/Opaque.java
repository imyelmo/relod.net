   package reload.Common;
	
   import java.io.*;
   import reload.Common.Exception.*;
 
   public class Opaque{
   
   
      private byte length;
      private short lengthS;
      private int lengthI;
    
      private int bits;
   	
      private byte[] cont;
      
          
      public Opaque(int bits, byte[] data) throws ReloadException{ // Upper
      
         if(bits != 8 && bits != 16 && bits != 24 && bits != 32)
            throw new ReloadException("Incorrect bits length. Only 8, 16, 24 and 32 are allowed.");
            
         if(data.length > Math.pow(2, bits)-1)
            throw new WrongLengthReloadException();
            
         this.bits = bits;
         cont = data;
            
         switch(bits){
            case 8:
               length = (byte)data.length;
               break;
            case 16:
               lengthS = (short)data.length;
               break;
            case 32:
               lengthI = data.length;
               break;
            case 24:
               lengthI = data.length;
         }
              
      }
      
      public Opaque(int bits, byte[] data, int offset) throws ReloadException{ // Lower
      
         if(bits != 8 && bits != 16 && bits != 24 && bits != 32)
            throw new ReloadException("Incorrect bits length. Only 8, 16, 24 and 32 are allowed.");
      		
            
         this.bits = bits;
         
            
         switch(bits){
            case 8:
               length = data[offset];
               cont = Utils.cutArray(data, length, offset+1);
               break;
            case 16:
               lengthS = Utils.toShort(data, offset);
               cont = Utils.cutArray(data, lengthS, offset+2);
               break;
            case 32:
               lengthI = Utils.toInt(data, offset);
               cont = Utils.cutArray(data, lengthI, offset+4);
               break;
            case 24:
               lengthI = Utils.to3Bytes(data, offset);
               cont = Utils.cutArray(data, lengthI, offset+3);
         }
      		
         if(cont.length > Math.pow(2, bits)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         switch(bits){
            case 8:
               baos.write(length);
               break;
            case 16:
               baos.write(Utils.toByte(lengthS));
               break;
            case 32:
               baos.write(Utils.toByte(lengthI));
               break;
            case 24:
               baos.write(Utils.to3Bytes(lengthI));
         }
            
         baos.write(cont);
         
         return baos.toByteArray();
      
      }
      
      public byte[] getContent(){
      
         return cont;
      
      }
      
      public int getBits(){
      
         return bits;
      
      }
      
   }