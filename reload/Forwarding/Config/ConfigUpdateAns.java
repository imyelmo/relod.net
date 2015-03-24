   package reload.Forwarding.Config;
   
   import reload.Common.Exception.*;
 
   public class ConfigUpdateAns{
   
      public ConfigUpdateAns (){}
      
      public ConfigUpdateAns (byte[] data) throws ReloadException{
      
         if (data.length != 0)
            throw new WrongLengthReloadException("Wrong ConfigUpdate packet");
         
      }
      
      public byte[] getBytes(){
      
         return new byte[0];
         
      }
   
   }