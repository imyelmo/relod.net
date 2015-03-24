   package reload.Message.Security.TLS;
   
	 
   /* Constructor:
   SignatureAlgorithm alg = SignatureAlgorithm.valueOf(1);
   SignatureAlgorithm alg = SignatureAlgorithm.rsa;
   */

   public enum SignatureAlgorithm {
      UNDEFINED("undefined", (byte)-1),
      anonymous("anonymous",  (byte)0),
      rsa(            "RSA",  (byte)1),
      dsa(            "DSA",  (byte)2),
      ecdsa(        "ECDSA",  (byte)3);
      
      final String name;    // except the UNDEFINED, other names are defined               
      final byte value;		 // by TLS 1.2 protocol
      
      private SignatureAlgorithm(String name, byte value) {
      
         this.name = name;
         this.value = value;
      }
      
      static SignatureAlgorithm valueOf(int value) {
      
         SignatureAlgorithm sign = UNDEFINED;
         switch (value) {
            case 0:
               sign = anonymous;
               break;
            case 1:
               sign = rsa;
               break;
            case 2:
               sign = dsa;
               break;
            case 3:
               sign = ecdsa;
               break;
         }
         
         return sign;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	   
   }
   
