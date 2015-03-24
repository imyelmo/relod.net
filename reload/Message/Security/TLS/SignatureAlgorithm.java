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
   
