
   package reload.Link;

	/* Constructor:
   FramedMessageType type = FramedMessageType.valueOf(128);
   FramedMessageType type = FramedMessageType.data;
   */
 
   public enum FramedMessageType {
      UNDEFINED("undefined",	(byte)-1),
      data(		 "data",			(byte)128),
      ack( 		 "ack",			(byte)129);
   
      
      final String name;
      final byte value;
      
      private FramedMessageType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static FramedMessageType valueOf(int value) {
         FramedMessageType type = UNDEFINED;
         switch (value) {
            case 128:
               type = data;
               break;
            case 129:
               type = ack;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }