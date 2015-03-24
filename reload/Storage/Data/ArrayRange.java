   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
	
   
   public class ArrayRange{
   
   
      private int first;
      private int last;
    
      
      public ArrayRange (int first, int last){
      
         this.first = first;
         this.last = last;
      
      }
      
      public ArrayRange (byte[] data) {
      
         first = Utils.toInt(data, 0);
         last = Utils.toInt(data, 4);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(first));
         baos.write(Utils.toByte(last));
         
         return baos.toByteArray();
      
      }
   
      public int getFirst(){
      
         return first;
      
      }
   
      public int getLast(){
      
         return last;
      
      }
   
   }