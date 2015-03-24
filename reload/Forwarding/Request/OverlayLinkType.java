   package reload.Forwarding.Request;

	/* Constructor:
   OverlayLinkType type = OverlayLinkType.valueOf(1);
   OverlayLinkType type = OverlayLinkType.DTLS_UDP_SR;
   */
 
   public enum OverlayLinkType {
      UNDEFINED(				"undefined", 			 	  (byte)-1),
      reservedOverlayLink(	"reserved Overlay Link",	(byte)0),
      DTLS_UDP_SR(			"DTLS-UDP-SR", 			 	(byte)1),
      DTLS_UDP_SR_NO_ICE(	"DTLS-UDP-SR-NO-ICE",		(byte)3),
      TLS_TCP_FH_NO_ICE(	"TLS-TCP-FH-NO-ICE",			(byte)4);
   
   
      
      final String name;
      final byte value;
      
      private OverlayLinkType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static OverlayLinkType valueOf(int value) {
         OverlayLinkType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedOverlayLink;
               break;
            case 1:
               type = DTLS_UDP_SR;
               break;
            case 3:
               type = DTLS_UDP_SR_NO_ICE;
               break;
            case 4:
               type = TLS_TCP_FH_NO_ICE;      
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }