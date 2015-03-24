   package reload.Message.Forwarding;

/*    public class DestinationType {
      public static final byte reserved = ((byte)0);
      public static final byte node = ((byte)1);
      public static final byte resource = ((byte)2);
      public static final byte compressed = ((byte)3);
      }
		*/
		
	/* Constructor:
   DestinationType type = DestinationType.valueOf(1);
   DestinationType type = DestinationType.node;
   */
 
   public enum DestinationType {
      UNDEFINED( "undefined", 		 (byte)-1),
      reserved(  "reserved",  	 	  (byte)0),
      node(		  "node", 			 	  (byte)1),
      resource(  "resource",  	 	  (byte)2),
      opaque_id_type("opaque id type",(byte)3);
   
      
      final String name;
      final byte value;
      
      private DestinationType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static DestinationType valueOf(int value) {
         DestinationType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reserved;
               break;
            case 1:
               type = node;
               break;
            case 2:
               type = resource;
               break;
            case 3:
               type = opaque_id_type;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }