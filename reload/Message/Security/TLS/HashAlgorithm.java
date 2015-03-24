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
   HashAlgorithm hash = HashAlgorithm.valueOf(1);
   HashAlgorithm hash = HashAlgorithm.MD5;
   */
 
   public enum HashAlgorithm {
      UNDEFINED("undefined", (byte)-1),
      none(     "none",		   (byte)0),
      md5(      "MD5",  		(byte)1),
      sha1(     "SHA-1",  		(byte)2),
      sha224(   "SHA-224",  	(byte)3),
      sha256(   "SHA-256",  	(byte)4),
      sha384(   "SHA-384",  	(byte)5),
      sha512(   "SHA-512",  	(byte)6);
      
      final String name;    // Except the UNDEFINED, other names are defined
      final byte value;     // by TLS 1.2 protocol
            
      private HashAlgorithm(String name, byte value) {
      
         this.name = name;
         this.value = value;
      }
      
      public static HashAlgorithm valueOf(int value) {
      
         HashAlgorithm hash = UNDEFINED;
         
         switch (value) {
            case 0:
               hash = none;
               break;
            case 1:
               hash = md5;
               break;
            case 2:
               hash = sha1;
               break;
            case 3:
               hash = sha224;
               break;
            case 4:
               hash = sha256;
               break;
            case 5:
               hash = sha384;
               break;
            case 6:
               hash = sha512;
               break;
         }
         
         return hash;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
      
      public String getName(){
      
         return name;
      
      }
   	
   }
