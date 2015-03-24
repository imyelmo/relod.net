   package reload.Topology.Plugins.Chord.Task;
		
 
   public enum ChordTaskType {
   
      UNDEFINED(	"undefined",	  (byte)-1),
      none(			"None",				(byte)0),
      update(		"Update",			(byte)1),
      leave(		"Leave",				(byte)2),
      join(			"Join",				(byte)3);
   
      
      private final String name;
      private final byte value;
      
      private ChordTaskType(String name, byte value) {
      
         this.name = name;
         this.value = value;
      }  
      
      public static ChordTaskType valueOf(int value) {
      
         ChordTaskType type = UNDEFINED;
         switch (value) {
            case 0:
               type = none;
               break;
            case 1:
               type = update;
               break;
            case 2:
               type = leave;
               break;
            case 3:
               type = join;
               break;
         }
         
         return type;
      }
      
      public byte getValue(){
      
         return value;
      
      }
   	
   }