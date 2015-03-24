   package reload.Message.Security;
	
	/* Constructor:
   SignerIdentityType type = SignerIdentityType.valueOf(1);
   SignerIdentityType type = SignerIdentityType.cert_hash;
   */
 
   public enum SignerIdentityType {
      UNDEFINED(				  "undefined",        		 "",								 (byte)-1),
      reservedSignerIdentity("reservedsigneridentity", "Reserved Signer Identity", (byte)0),
      cert_hash(    			  "certhash", 				    "Certificate Hash",  	     (byte)1),
      cert_hash_node_id(     "certhashnodeid",   		 "Certificate Hash NodeId",  (byte)2),
      none(    				  "none", 						 "None",  						  (byte)3);
   
      
      final String name;    // not the standard signature algorithm name
                            // except the UNDEFINED, other names are defined
                            // by TLS 1.2 protocol
      final String standardName; // the standard MessageDigest algorithm name
      final byte value;
      
   	
      private SignerIdentityType(String name, String standardName, byte value) {
         this.name = name;
         this.standardName = standardName;
         this.value = value;
      }
      
      public static SignerIdentityType valueOf(int value) {
         SignerIdentityType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedSignerIdentity;
               break;
            case 1:
               type = cert_hash;
               break;
            case 2:
               type = cert_hash_node_id;
               break;
            case 3:
               type = none;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }