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
