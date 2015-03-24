   package reload.Common.Exception;

   public class WrongTypeReloadException extends ReloadException {
   
   
      public WrongTypeReloadException(String message) {
      
         super(message);
      
      }    
      
      public WrongTypeReloadException() {
      
         super("No type has been selected.");
      
      }    
   
   
   }