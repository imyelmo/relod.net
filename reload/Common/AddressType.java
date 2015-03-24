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

   package reload.Common;

	/* Constructor:
   AddressType type = AddressType.valueOf(1);
   AddressType type = AddressType.ipv4_address;
   */
 
   public enum AddressType {
      UNDEFINED(	 "undefined",        (byte)-1),
      reservedAddr("reserved address", (byte)0),
      ipv4_address("ipv4 address", 	   (byte)1),
      ipv6_address("ipv6 address",	   (byte)2);
   
      
      final String name;
      final byte value;
      
      private AddressType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static AddressType valueOf(int value) {
         AddressType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedAddr;
               break;
            case 1:
               type = ipv4_address;
               break;
            case 2:
               type = ipv6_address;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }