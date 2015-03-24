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
   package reload.Message.Forwarding;

/*    public class DestinationType {
      public static final byte reserved = ((byte)0);
      public static final byte node = ((byte)1);
      public static final byte resource = ((byte)2);
      public static final byte compressed = ((byte)3);
      }
		*/
		
	/* Constructor:
   DestinationType type = DestinationType.valueOf(1);
   DestinationType type = DestinationType.node;
   */
 
   public enum DestinationType {
      UNDEFINED( "undefined", 		 (byte)-1),
      reserved(  "reserved",  	 	  (byte)0),
      node(		  "node", 			 	  (byte)1),
      resource(  "resource",  	 	  (byte)2),
      opaque_id_type("opaque id type",(byte)3);
   
      
      final String name;
      final byte value;
      
      private DestinationType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static DestinationType valueOf(int value) {
         DestinationType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reserved;
               break;
            case 1:
               type = node;
               break;
            case 2:
               type = resource;
               break;
            case 3:
               type = opaque_id_type;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }