   package reload.Message.MessageCont;
   
   import reload.Common.Utils;
   
	
	/* Constructor:
   MessageExtensionType type = MessageExtensionType.valueOf(0);
   MessageExtensionType type = MessageExtensionType.reservedMessagesExtension;
   */
 
   public enum MessageExtensionType {
      UNDEFINED(				  	  "undefined",      		 	     (short)-1),
      reservedMessagesExtension("reserved messages extension", (short)0);
   
      
      final String name;
      final short value;
      
      private MessageExtensionType(String name, short value) {
      
         this.name = name;
         this.value = value;
      }
      
      
      public static MessageExtensionType valueOf(int value) {
      
         MessageExtensionType type = UNDEFINED;
         
         switch (value) {
            case 0:
               type = reservedMessagesExtension;
               break;
         }
         
         return type;
      }
      
      public short getValue(){
      
         return value;
      
      }
      
      public byte[] getBytes(){
      
         return Utils.toByte(value);
      
      }
   	
   }