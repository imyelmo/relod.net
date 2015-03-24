
   package reload.Forwarding.Config;

	/* Constructor:
   ConfigUpdateType type = ConfigUpdateType.valueOf(1);
   ConfigUpdateType type = ConfigUpdateType.ipv4_address;
   */
 
   public enum ConfigUpdateType {
      UNDEFINED(	 			"undefined",							(byte)-1),
      reservedConfigUpdate("reserved configuration update",	(byte)0),
      config(					"configuration",						(byte)1),
      kind(						"kind",									(byte)2);
   
      
      final String name;
      final byte value;
      
      private ConfigUpdateType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static ConfigUpdateType valueOf(int value) {
         ConfigUpdateType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedConfigUpdate;
               break;
            case 1:
               type = config;
               break;
            case 2:
               type = kind;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }