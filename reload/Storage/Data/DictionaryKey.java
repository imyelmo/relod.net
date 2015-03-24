   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
  
   public class DictionaryKey{
   
      private Opaque id; // 16-bit
   
      public DictionaryKey(byte[] data, boolean upper) throws ReloadException{ // Upper and lower levels
      
         if(upper)
            id = new Opaque(16, data);
            
         else
            id = new Opaque(16, data, 0);
         
      }
      
      public DictionaryKey(Opaque data) throws ReloadException{ // Upper level
      
         id = data;
      
         if(data.getBits() != 16)
            throw new WrongLengthReloadException();
         
      }
   
      public byte[] getBytes() throws IOException{
      
         return id.getBytes();
      
      }
      
      public byte[] getKey(){
      
         return id.getContent();
      
      }
   
   }