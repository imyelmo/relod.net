   package reload.Common;

   import java.io.*;
   import reload.Common.Exception.*;
   
   public class KindArray{
   
      private byte kind_length;
      private KindId[] kind_array; //<0..2^8-1>
    
      
      public KindArray (KindId[] kind_array) throws Exception{
      
         this.kind_array = kind_array;
         
         for (int i=0; i<kind_array.length; i++)
            kind_length += kind_array[i].getBytes().length;
            
         if(kind_length > Math.pow(2, 8)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public KindArray (byte[] data) throws Exception{
      
         kind_length = data[0];
         
         data = Utils.cutArray(data, kind_length, 1);
         
         int offset = 0;
         
         int num = data.length / 4;	//32-bit, 4 bytes
      
         kind_array = new KindId[num];
      
         for (int i=0; i<num; i++){
         
            kind_array[i] = new KindId(Utils.cutArray(data, offset));
            offset += kind_array[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(kind_length));
      
         for (int i=0; i<kind_array.length; i++)
            baos.write(kind_array[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
   
   }