   package reload.Forwarding.Ping;

   import java.io.*;
   import reload.Common.*;
   
   public class PingAns{
   
   
      private long response_id;
      private long time;
   
      
      
      public PingAns (long response_id, long time){
      
         this.response_id = response_id;
         this.time = time;
      
      }
      
      public PingAns (byte[] data) throws IOException{
      
         response_id = Utils.toLong(data, 0);
         time = Utils.toLong(data, 8);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(response_id));
         baos.write(Utils.toByte(time));
         
         return baos.toByteArray();
      
      }
   
      public long getResponseId(){
      
         return response_id;
      
      }
   
      public long getTime(){
      
         return time;
      
      }
   
   }