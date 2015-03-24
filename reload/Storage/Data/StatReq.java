   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class StatReq{
   
   
      private ResourceId resource;
      
      private short specifiers_length;
      private StoredDataSpecifier[] specifiers; //<0..2^16-1>
    
      
      public StatReq (ResourceId resource, StoredDataSpecifier[] specifiers) throws Exception{
      
         this.resource = resource;
         this.specifiers = specifiers;
         
         for (int i=0; i<specifiers.length; i++)
            specifiers_length += specifiers[i].getBytes().length;
            
         if(specifiers_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StatReq (byte[] data) throws Exception{
      
      	
         resource = new ResourceId(data, false); // Lower levels
         int offset = resource.getBytes().length;
         
         specifiers_length = Utils.toShort(data, offset);
         offset += 2;
         
         data = Utils.cutArray(data, specifiers_length, offset);
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