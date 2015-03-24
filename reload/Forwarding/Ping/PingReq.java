   package reload.Forwarding.Ping;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class PingReq{
   
   
      private Opaque padding; //<0..2^16-1>
      
      private byte[] def_data = new byte[]{0,0,0,0,0,0,0,0};
   
   
      public PingReq () throws ReloadException{ //upper call [llamada de los niveles superiores]
      
         padding = new Opaque(16, def_data);
      
      }   
      
      public PingReq (byte[] data, boolean upper) throws ReloadException{ //upper call [llamada de los niveles superiores]
      
         if(upper)
            padding = new Opaque(16, data);
         
         else
            padding = new Opaque(16, data, 0);
      
      }
      
      public byte[] getBytes() throws IOException{
            
         return padding.getBytes();
      
      }
      
      public int getPaddingLength(){
      
         return padding.getContent().length;
      
      }
      
   }