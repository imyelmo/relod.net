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
   package reload.Forwarding.Task;
		
 
   public enum TaskType {
   
      UNDEFINED("undefined",  	 (byte)-1),
      none("None",  					  (byte)0),
      ping("Ping", 					  (byte)1),
      attach_route("Attach Route", (byte)2),
      app_attach("Application Attach", (byte)3);
   
      
      private final String name;
      private final byte value;
      
      private TaskType(String name, byte value) {
      
         this.name = name;
         this.value = value;
      }  
      
      public static TaskType valueOf(int value) {
      
         TaskType type = UNDEFINED;
         switch (value) {
            case 0:
               type = none;
               break;
            case 1:
               type = ping;
               break;
            case 2:
               type = attach_route;
               break;
            case 3:
               type = app_attach;
               break;
         }
         
         return type;
      }
      
      public byte getValue(){
      
         return value;
      
      }
   	
   }