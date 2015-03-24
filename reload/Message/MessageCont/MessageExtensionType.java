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
   package reload.Message.MessageCont;
   
   import reload.Common.Utils;
   
	
	/* Constructor:
   MessageExtensionType type = MessageExtensionType.valueOf(0);
   MessageExtensionType type = MessageExtensionType.reservedMessagesExtension;
   */
 
   public enum MessageExtensionType {
      UNDEFINED(				  	  "undefined",      		 	     (short)-1),
      reservedMessagesExtension("reserved messages extension", (short)0);
   
      
      final String name;
      final short value;
      
      private MessageExtensionType(String name, short value) {
      
         this.name = name;
         this.value = value;
      }
      
      
      public static MessageExtensionType valueOf(int value) {
      
         MessageExtensionType type = UNDEFINED;
         
         switch (value) {
            case 0:
               type = reservedMessagesExtension;
               break;
         }
         
         return type;
      }
      
      public short getValue(){
      
         return value;
      
      }
      
      public byte[] getBytes(){
      
         return Utils.toByte(value);
      
      }
   	
   }