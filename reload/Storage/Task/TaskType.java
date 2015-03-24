   package reload.Storage.Task;
		
 
   public enum TaskType {
   
      UNDEFINED(		"undefined", 		  (byte)-1),
      none(				"None",  			   (byte)0),
      joining_store(	"Joining Storage",	(byte)1),
      app_store(		"Application Store", (byte)2),
      app_fetch(		"Application Fetch",	(byte)3);
   
      
      private final String name;
      private final byte value;
      
      private TaskType(String name, byte value) {
      
         this.name = name;
         this.value = value;
      }  
      
      public static TaskType valueOf(int value) {
      
         TaskType type = UNDEFINED;
         switch (value) {
            case 0:
               type = none;
               break;
            case 1:
               type = joining_store;
               break;
            case 2:
               type = app_store;
               break;
            case 3:
               type = app_fetch;
               break;
         }
         
         return type;
      }
      
      public byte getValue(){
      
         return value;
      
      }
   	
   }