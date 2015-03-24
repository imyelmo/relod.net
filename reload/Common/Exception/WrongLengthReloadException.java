   package reload.Common.Exception;

   public class WrongLengthReloadException extends ReloadException {
   
   
      public WrongLengthReloadException() {
      
         super("Data length is bigger/smaller than allowed.");
      
      }
      
      public WrongLengthReloadException(String msg) {
      
         super(msg);
      
      }    
   
   }