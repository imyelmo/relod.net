   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class StatAns{
   
   
      private int kind_length;
      private StatKindResponse[] kind_responses; //<0..2^32-1>
    
      
      public StatAns (StatKindResponse[] kind_responses) throws Exception{
      
         this.kind_responses = kind_responses;
         
         for (int i=0; i<kind_responses.length; i++)
            kind_length += kind_responses[i].getBytes().length;
      		
         if(kind_length > Math.pow(2, 32)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StatAns (byte[] data) throws Exception{
      
         
         kind_length = Utils.toInt(data, 0);
         
         data = Utils.cutArray(data, kind_length, 4);
         int offset = 0;
         
         int num = Algorithm.counter(4, data, 12);	
      
         kind_responses = new StatKindResponse[num];
      
         for (int i=0; i<num; i++){
         
            kind_responses[i] = new StatKindResponse(Utils.cutArray(data, offset));
            offset += kind_responses[i].getBytes().length;
         }
      	
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(kind_length));
      
         for (int i=0; i<kind_responses.length; i++)
            baos.write(kind_responses[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public StatKindResponse[] getKindResponses(){
      
         return kind_responses;
      
      }
   
   }