   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class StoreKindResponse{
   
      private KindId kind;
      private long generation_counter;
      
      private short replicas_length;
      private NodeId[] replicas; //<0..2^16-1>
    
      
      public StoreKindResponse (KindId kind, long generation_counter, NodeId[] replicas) throws Exception{
      
         this.kind = kind;
         this.generation_counter = generation_counter;
         this.replicas = replicas;
         
         for (int i=0; i<replicas.length; i++)
            replicas_length += replicas[i].getBytes().length;
            
         if(replicas_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StoreKindResponse (byte[] data) throws Exception{
      
         kind = new KindId(data); // 32-bit
         generation_counter = Utils.toLong(data, 4);
         replicas_length = Utils.toShort(data, 12);
      	
         data = Utils.cutArray(data, replicas_length, 14);
         
         if (data.length % NodeId.getLength() != 0)
            throw new WrongLengthReloadException();
         
         int num = data.length / NodeId.getLength();
         
         int offset=0;
           
         replicas = new NodeId[num];
      	
         for (int i=0; i<num; i++){
         
            replicas[i] = new NodeId(Utils.cutArray(data, offset), false);
            offset += replicas[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(kind.getBytes());
         baos.write(Utils.toByte(generation_counter));
         baos.write(Utils.toByte(replicas_length));
      
         for (int i=0; i<replicas.length; i++)
            baos.write(replicas[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public KindId getKind(){
        
         return kind; 
      	
      }
      	
      public long getGenerationCounter(){
      
         return generation_counter;
      
      }
      
      public NodeId[] getReplicas(){
      
         return replicas;
      
      }
      
   }