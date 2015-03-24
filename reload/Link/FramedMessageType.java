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

   package reload.Link;

	/* Constructor:
   FramedMessageType type = FramedMessageType.valueOf(128);
   FramedMessageType type = FramedMessageType.data;
   */
 
   public enum FramedMessageType {
      UNDEFINED("undefined",	(byte)-1),
      data(		 "data",			(byte)128),
      ack( 		 "ack",			(byte)129);
   
      
      final String name;
      final byte value;
      
      private FramedMessageType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static FramedMessageType valueOf(int value) {
         FramedMessageType type = UNDEFINED;
         switch (value) {
            case 128:
               type = data;
               break;
            case 129:
               type = ack;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }