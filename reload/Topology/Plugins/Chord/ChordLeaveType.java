
   package reload.Topology.Plugins.Chord;

	/* Constructor:
   LeaveType type = LeaveType.valueOf(1);
   LeaveType type = LeaveType.from_succ;
   */
 
   public enum ChordLeaveType {
      UNDEFINED("undefined",			(byte)-1),
      reserved( "reserved", 			(byte)0),
      from_succ("from successors",	(byte)1),
      from_pred("from predecessors",(byte)2);
   
      
      final String name;
      final byte value;
      
      private ChordLeaveType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static ChordLeaveType valueOf(int value) {
         ChordLeaveType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reserved;
               break;
            case 1:
               type = from_succ;
               break;
            case 2:
               type = from_pred;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }