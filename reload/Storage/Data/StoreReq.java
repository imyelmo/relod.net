   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class StoreReq{
   
   
      private ResourceId resource;
      private byte replica_number;
      
      private int kind_length;
      private StoreKindData[] kind_data; //<0..2^32-1>
    
      
      public StoreReq (ResourceId resource, byte replica_number, StoreKindData[] kind_data) throws Exception{
      
         this.resource = resource;
         this.replica_number = replica_number;
         this.kind_data = kind_data;
         
         for (int i=0; i<kind_data.length; i++)
            kind_length += kind_data[i].getBytes().length;
            
         if(kind_length > Math.pow(2, 32)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StoreReq (byte[] data) throws Exception{
      
         resource = new ResourceId(data, false); // Lower level
         int offset = resource.getBytes().length;
         
         replica_number = data[offset];
         offset++;
         
         kind_length = Utils.toInt(data, offset);
         offset += 4;
         
         data = Utils.cutArray(data, kind_length, offset);
         offset = 0;
      	
         int num = Algorithm.counter(4, data, 12);
      		
         kind_data = new StoreKindData[num];
      	
         for (int i=0; i<num; i++){
         
            kind_data[i] = new StoreKindData(Utils.cutArray(data, offset));
            offset += kind_data[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(resource.getBytes());
         baos.write(replica_number);
         baos.write(Utils.toByte(kind_length));
         
         for (int i=0; i<kind_data.length; i++)
            baos.write(kind_data[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public ResourceId getResource(){
      
         return resource;
      
      }
   
      public byte getReplicaNumber(){
      
         return replica_number;
      
      }
   	
      public StoreKindData[] getKindData(){
      
         return kind_data;
      
      }
   
   }