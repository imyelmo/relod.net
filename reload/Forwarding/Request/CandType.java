
   package reload.Forwarding.Request;

	/* Constructor:
   CandType type = CandType.valueOf(1);
   CandType type = CandType.XXXX;
   */
 
   public enum CandType {
      UNDEFINED(		"undefined",		(byte)-1),
      reservedCand(	"reserved Cand",	(byte)0),
      host(				"host", 			 	(byte)1),
      srflx(			"srflx",				(byte)2),
      prflx(			"prflx",				(byte)3),
      relay(			"relay",				(byte)4);
   
   
      
      final String name;
      final byte value;
      
      private CandType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static CandType valueOf(int value) {
         CandType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedCand;
               break;
            case 1:
               type = host;
               break;
            case 2:
               type = srflx;
               break;
            case 3:
               type = prflx;
               break;
            case 4:
               type = relay;      
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }