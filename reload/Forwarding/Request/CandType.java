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

   package reload.Forwarding.Request;

	/* Constructor:
   CandType type = CandType.valueOf(1);
   CandType type = CandType.XXXX;
   */
 
   public enum CandType {
      UNDEFINED(		"undefined",		(byte)-1),
      reservedCand(	"reserved Cand",	(byte)0),
      host(				"host", 			 	(byte)1),
      srflx(			"srflx",				(byte)2),
      prflx(			"prflx",				(byte)3),
      relay(			"relay",				(byte)4);
   
   
      
      final String name;
      final byte value;
      
      private CandType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static CandType valueOf(int value) {
         CandType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedCand;
               break;
            case 1:
               type = host;
               break;
            case 2:
               type = srflx;
               break;
            case 3:
               type = prflx;
               break;
            case 4:
               type = relay;      
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }