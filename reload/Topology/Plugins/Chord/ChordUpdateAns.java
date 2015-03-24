   package reload.Topology.Plugins.Chord;
	
   import java.io.*;
   import reload.Common.*;
	
   
   public class ChordUpdateAns{
   
      private boolean response;
   	
      
      public ChordUpdateAns (boolean response){
      
         this.response = response;
         
      }
         
      public ChordUpdateAns (byte[] data) throws Exception{
      
         response = Utils.toBoolean(data[0]);
                  
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(response));
                     
         return baos.toByteArray();
      
      }
      
      public boolean getResponse(){
      
         return response;
      
      }
     
   }