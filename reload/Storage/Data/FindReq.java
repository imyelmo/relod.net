   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class FindReq{
   
   
      private ResourceId resource;
      
      private byte kinds_length;
      private KindId[] kinds; //<0..2^8-1>
    
      
      public FindReq (ResourceId resource, byte replica_number, KindId[] kinds) throws Exception{
      
         this.resource = resource;
         this.kinds = kinds;
         
         for (int i=0; i<kinds.length; i++)
            kinds_length += kinds[i].getBytes().length;
            
         if(kinds_length > Math.pow(2, 8)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public FindReq (byte[] data) throws Exception{
      
         resource = new ResourceId(data, false); //Lower levers
         int offset = resource.getBytes().length;
         
         kinds_length = data[offset];
         offset++;
         
         data = Utils.cutArray(data, kinds_length, offset);
         offset = 0;
         
         if (data.length % 4 != 0)
            throw new WrongLengthReloadException();
      	
         int num = data.length / 4;
      	
         kinds = new KindId[num];
      	
         for (int i=0; i<num; i++){
         
            kinds[i] = new KindId(Utils.cutArray(data, offset));
            offset += 4;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(resource.getBytes());
         baos.write(kinds_length);
      
         for (int i=0; i<kinds.length; i++)
            baos.write(kinds[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public ResourceId getResource(){
         
         return resource;
      	
      }
   
      public KindId[] getKinds(){
      
         return kinds;
      
      }
   
   }