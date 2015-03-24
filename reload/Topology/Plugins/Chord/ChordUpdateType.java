
   package reload.Topology.Plugins.Chord;

	/* Constructor:
   ChordUpdateType type = ChordUpdateType.valueOf(1);
   ChordUpdateType type = ChordUpdateType.peer_ready;
   */
 
   public enum ChordUpdateType {
      UNDEFINED( "undefined",  (byte)-1),
      reserved(  "reserved", 	 (byte)0),
      peer_ready("peer ready", (byte)1),
      neighbors( "neighbours", (byte)2),
      full(		  "full",		 (byte)3);
   
      
      final String name;
      final byte value;
      
      private ChordUpdateType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static ChordUpdateType valueOf(int value) {
         ChordUpdateType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reserved;
               break;
            case 1:
               type = peer_ready;
               break;
            case 2:
               type = neighbors;
               break;
            case 3:
               type = full;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }