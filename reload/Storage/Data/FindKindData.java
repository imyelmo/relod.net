   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
	
   
   public class FindKindData{
   
   
      private KindId kind;
      private ResourceId closest;
    
      
      public FindKindData (KindId kind, ResourceId closest){
      
         this.kind = kind;
         this.closest = closest;
      
      }
      
      public FindKindData (byte[] data) throws Exception{
      
         kind = new KindId(data); // 32-bit	
         closest = new ResourceId(Utils.cutArray(data, 4), false);          	
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(kind.getBytes());
         baos.write(closest.getBytes());
         
         return baos.toByteArray();
      
      }
   
      public KindId getKind(){
      
         return kind;
      
      }
      
      public ResourceId getClosest(){
      
         return closest; 
      
      }
   
   }