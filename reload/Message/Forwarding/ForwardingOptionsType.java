   package reload.Message.Forwarding;
	
	/* Constructor:
   ForwardingOptionsType type = DestinationType.valueOf(1);
   ForwardingOptionsType = DestinationType.XXXX;
   */
 
   public enum ForwardingOptionsType {
      UNDEFINED(				"undefined", 			 (byte)-1),
      reservedForwarding(  "reservedForwarding",  (byte)0);
   
   
      
      final String name;
      final byte value;
      
      private ForwardingOptionsType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static ForwardingOptionsType valueOf(int value) {
         ForwardingOptionsType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedForwarding;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }