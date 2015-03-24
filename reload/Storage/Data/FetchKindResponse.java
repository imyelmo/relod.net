   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class FetchKindResponse{
   
      private KindId kind;
      private long generation;
      
      private int values_length;
      private StoredData[] values; //<0..2^32-1>
    
      
      public FetchKindResponse (KindId kind, long generation, StoredData[] values) throws Exception{
      
         this.kind = kind;
         this.generation = generation;
         this.values = values;
         
         for (int i=0; i<values.length; i++)
            values_length += values[i].getBytes().length;
            
         if(values_length > Math.pow(2, 32)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public FetchKindResponse (byte[] data) throws Exception{
      
         kind = new KindId(data); // 32-bit
         generation = Utils.toLong(data, 4);
         values_length = Utils.toInt(data, 12);
         
         data = Utils.cutArray(data, values_length, 16);
         int offset = 0;
         
         int num = Algorithm.counter(4, data, 0);
         
         values = new StoredData[num];
      	
         DataModel dataModel = kind.getDataModel();
      
         for (int i=0; i<num; i++){
         
            values[i] = new StoredData(Utils.cutArray(data, offset), dataModel);
            offset += values[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(kind.getBytes());
         baos.write(Utils.toByte(generation));
         baos.write(Utils.toByte(values_length));
      
         for (int i=0; i<values.length; i++)
            baos.write(values[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public KindId getKind(){
      
         return kind;
      
      }
   	
      public long getGeneration(){
      
         return generation;
      
      }
      
      public StoredData[] getValues(){
      
         return values;
      
      }
   
   }