   package reload.Common.Error;
	
   import reload.Common.*;
   import java.util.*;


   public class ErrorUnknownKind extends ReloadError {
   
      public ErrorUnknownKind(String kinds) {
      
         super(kinds);
      
      }
   
   }