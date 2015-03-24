   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class StoreAns{
   
      private short kind_length;
      private StoreKindResponse[] kind_responses; //<0..2^16-1>
    
      
      public StoreAns (StoreKindResponse[] kind_responses) throws Exception{
      
         this.kind_responses = kind_responses;
         
         for (int i=0; i<kind_responses.length; i++)
            kind_length += kind_responses[i].getBytes().length;
            
         if(kind_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StoreAns (byte[] data) throws Exception{
      
         kind_length = Utils.toShort(data, 0);
         
         data = Utils.cutArray(data, kind_length, 2);
         
         int offset = 0;
         
         int num = Algorithm.counter(2, data, 12);	
      
         kind_responses = new StoreKindResponse[num];
      
         for (int i=0; i<num; i++){
         
            kind_responses[i] = new StoreKindResponse(Utils.cutArray(data, offset));
            offset += kind_responses[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(kind_length));
      
         for (int i=0; i<kind_responses.length; i++)
            baos.write(kind_responses[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public StoreKindResponse[] getStoreKindResponses(){
      
         return kind_responses;
      
      }
   
   }