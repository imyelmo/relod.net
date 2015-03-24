   package reload.Forwarding.Config;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class KindDescription{
   
   
      private Opaque data; //<0..2^16-1>
   
      
      
      public KindDescription (byte[] data, boolean upper) throws ReloadException{ // Upper call [llamada de los niveles superiores]
      
         if(upper)
            this.data = new Opaque(16, data);
         
         else
            this.data = new Opaque(16, data, 0);
      
      }
      
      
      public byte[] getBytes() throws IOException{
                  
         return data.getBytes();
      
      }
   
   
   }