   package reload.Forwarding.Task;
		
 
   public enum TaskType {
   
      UNDEFINED("undefined",  	 (byte)-1),
      none("None",  					  (byte)0),
      ping("Ping", 					  (byte)1),
      attach_route("Attach Route", (byte)2),
      app_attach("Application Attach", (byte)3);
   
      
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
               type = ping;
               break;
            case 2:
               type = attach_route;
               break;
            case 3:
               type = app_attach;
               break;
         }
         
         return type;
      }
      
      public byte getValue(){
      
         return value;
      
      }
   	
   }