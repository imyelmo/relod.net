   package reload.Message.Security;

   /* public class CertificateType {
      public static final byte X509 = ((byte)0);
      public static final byte OpenPGP = ((byte)1);
   }*/
	
	/* Constructor:
   CertificateType type = CertificateType.valueOf(1);
   CertificateType type = CertificateType.OpenPGP;
   */
 
   public enum CertificateType {
      UNDEFINED("undefined", (byte)-1),
      X509(     "X.509",     (byte)0),
      OpenPGP(  "OpenPGP",   (byte)1);
   
      
      final String name;    // except the UNDEFINED, other names are defined
                            // by TLS 1.2 protocol
      final byte value;
      
      private CertificateType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static CertificateType valueOf(int value) {
         CertificateType type = UNDEFINED;
         switch (value) {
            case 0:
               type = X509;
               break;
            case 1:
               type = OpenPGP;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }
