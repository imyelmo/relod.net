   package reload.Common.Exception;

   public class UnimplementedReloadException extends ReloadException {
   
   
      public UnimplementedReloadException(String feature) {
      
         super(feature + " not implemented in this version.");
      
      } 
      
   
   }