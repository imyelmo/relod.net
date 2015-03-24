
   package reload.Common;

	/* Constructor:
   AddressType type = AddressType.valueOf(1);
   AddressType type = AddressType.ipv4_address;
   */
 
   public enum AddressType {
      UNDEFINED(	 "undefined",        (byte)-1),
      reservedAddr("reserved address", (byte)0),
      ipv4_address("ipv4 address", 	   (byte)1),
      ipv6_address("ipv6 address",	   (byte)2);
   
      
      final String name;
      final byte value;
      
      private AddressType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static AddressType valueOf(int value) {
         AddressType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedAddr;
               break;
            case 1:
               type = ipv4_address;
               break;
            case 2:
               type = ipv6_address;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }