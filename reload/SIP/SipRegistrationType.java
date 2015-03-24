   package reload.SIP;
		
 
   public enum SipRegistrationType {
      UNDEFINED(				   "undefined",				(byte)-1),
      sip_registration_uri(   "SIP Registration URI",  (byte)1),
      sip_registration_route ("SIP Registration Route",(byte)2);
   
      
      final String name;
      final byte value;
      
      private SipRegistrationType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static SipRegistrationType valueOf(int value) {
         SipRegistrationType type = UNDEFINED;
         switch (value) {
            case 1:
               type = sip_registration_uri;
               break;
            case 2:
               type = sip_registration_route;
               break;
         }
         
         return type;
      }
      
      public byte getValue(){
      
         return value;
      
      }
   	
   }