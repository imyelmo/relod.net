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
   DataModel type = DataModel.valueOf(1);
   DataModel type = DataModel.single_value;
   */
 
   public enum DataModel {
      UNDEFINED(   "undefined",	  (byte)-1),
      single_value("single value",	(byte)1),
      array(		 "array",			(byte)2),
      dictionary(  "dictionary",		(byte)3);
   
      
      final String name;
      final byte value;
      
      private DataModel(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static DataModel valueOf(int value) {
         DataModel type = UNDEFINED;
         switch (value) {
            case 1:
               type = single_value;
               break;
            case 2:
               type = array;
               break;
            case 3:
               type = dictionary;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }