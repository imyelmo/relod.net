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

   package reload.Topology.Plugins.Chord;

	/* Constructor:
   ChordUpdateType type = ChordUpdateType.valueOf(1);
   ChordUpdateType type = ChordUpdateType.peer_ready;
   */
 
   public enum ChordUpdateType {
      UNDEFINED( "undefined",  (byte)-1),
      reserved(  "reserved", 	 (byte)0),
      peer_ready("peer ready", (byte)1),
      neighbors( "neighbours", (byte)2),
      full(		  "full",		 (byte)3);
   
      
      final String name;
      final byte value;
      
      private ChordUpdateType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static ChordUpdateType valueOf(int value) {
         ChordUpdateType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reserved;
               break;
            case 1:
               type = peer_ready;
               break;
            case 2:
               type = neighbors;
               break;
            case 3:
               type = full;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }