   package reload.Turn;

   import java.io.*;
   import reload.Common.*;
   
   public class TurnServer{
   
   
      private byte iteration;
      private IpAddressPort server_address;
      
   
      
      public TurnServer (byte iteration, IpAddressPort server_address){
      
         this.iteration = iteration;
         this.server_address = server_address;
         
      }
      
      public TurnServer (byte[] data) throws IOException{
      
         iteration = data[0];
         
         server_address = new IpAddressPort(Utils.cutArray(data, 1));
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(iteration);
         baos.write(server_address.getBytes());
         
         return baos.toByteArray();
      
      }
   
   }