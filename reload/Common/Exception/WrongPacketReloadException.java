   package reload.Common.Exception;
   
	import reload.Common.MessageCode;

   public class WrongPacketReloadException extends ReloadException {
   
   
      public WrongPacketReloadException(int code) throws ReloadException{
      
         super(MessageCode.valueOf(code).getName() + " was espected to be received.");
         
      	// Extender con un type que devuelva un string para que quede bonito.
      
      }    
      
      /*public WrongTypeReloadException() {
      
         super("No type has been selected.");
      
      }*/
   
   
   }