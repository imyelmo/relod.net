   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class FetchReq{
   
   
      private ResourceId resource;
    
      private short specifiers_length;   
   	
      private StoredDataSpecifier[] specifiers; //<0..2^16-1>
    
      
      public FetchReq (ResourceId resource, StoredDataSpecifier[] specifiers) throws Exception{
      
         this.resource = resource;
         this.specifiers = specifiers;
      
         for (int i=0; i<specifiers.length; i++)
            specifiers_length += specifiers[i].getBytes().length;
      
         if(specifiers_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public FetchReq (byte[] data) throws Exception{
      
         resource = new ResourceId(data, false); // Lower level
         
         int offset = resource.getBytes().length;
         
         specifiers_length = Utils.toShort(data, offset);
         
         data = Utils.cutArray(data, specifiers_length, offset + 2);
         offset = 0;
         
         int num = Algorithm.counter(2, data, 12);	
      		
         specifiers = new StoredDataSpecifier[num];
      	
         for (int i=0; i<num; i++){
         
            specifiers[i] = new StoredDataSpecifier(Utils.cutArray(data, offset));
            offset += specifiers[i].getBytes().length;
         }	
         
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(resource.getBytes());
         baos.write(Utils.toByte(specifiers_length));
      	
         for (int i=0; i<specifiers.length; i++)
            baos.write(specifiers[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public ResourceId getResource(){
         
         return resource;
      	
      }
   	
      public StoredDataSpecifier[] getSpecifiers(){
      
         return specifiers;
      
      }
   	
   }