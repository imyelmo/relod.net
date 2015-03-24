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
   LeaveType type = LeaveType.valueOf(1);
   LeaveType type = LeaveType.from_succ;
   */
 
   public enum ChordLeaveType {
      UNDEFINED("undefined",			(byte)-1),
      reserved( "reserved", 			(byte)0),
      from_succ("from successors",	(byte)1),
      from_pred("from predecessors",(byte)2);
   
      
      final String name;
      final byte value;
      
      private ChordLeaveType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static ChordLeaveType valueOf(int value) {
         ChordLeaveType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reserved;
               break;
            case 1:
               type = from_succ;
               break;
            case 2:
               type = from_pred;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }