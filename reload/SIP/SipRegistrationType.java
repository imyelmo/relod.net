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