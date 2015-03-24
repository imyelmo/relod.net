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
   package reload.Common.Exception;
   
	import reload.Common.MessageCode;

   public class WrongPacketReloadException extends ReloadException {
   
   
      public WrongPacketReloadException(int code) throws ReloadException{
      
         super(MessageCode.valueOf(code).getName() + " was espected to be received.");
         
      	// Extender con un type que devuelva un string para que quede bonito.
      
      }    
      
      /*public WrongTypeReloadException() {
      
         super("No type has been selected.");
      
      }*/
   
   
   }