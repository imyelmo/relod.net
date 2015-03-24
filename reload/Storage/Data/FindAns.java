   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class FindAns{
   
   
      private short results_length;
      private FindKindData[] results; //<0..2^16-1>
    
      
      public FindAns (FindKindData[] results) throws Exception{
      
         this.results = results;
         
         for (int i=0; i<results.length; i++)
            results_length += results[i].getBytes().length;
            
         if(results_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public FindAns (byte[] data) throws Exception{
      
      
         results_length = Utils.toShort(data, 0);
         
         data = Utils.cutArray(data, results_length, 2);
         int offset = 0;
         
         int num = Algorithm.counter(1, data, 4);	
      
         results = new FindKindData[num];
      
         for (int i=0; i<num; i++){
         
            results[i] = new FindKindData(Utils.cutArray(data, offset));
            offset += results[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(results_length));
      
         for (int i=0; i<results.length; i++)
            baos.write(results[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public FindKindData[] getResults(){
      
         return results;
      
      }
   
   }