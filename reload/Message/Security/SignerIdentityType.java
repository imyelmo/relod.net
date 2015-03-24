/*******************************************************************************
 *    <relod.net: GPLv3 beta software implementing RELOAD - draft-ietf-p2psip-base-26 >
 *    Copyright (C) <2013>  <Marcos Lopez-Samaniego, Isaias Martinez-Yelmo, Roberto Gonzalez-Sanchez> Contact: isaias.martinezy@uah.es
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software Foundation,
 *    Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *******************************************************************************/
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