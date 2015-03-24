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
   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
  
   public class DictionaryKey{
   
      private Opaque id; // 16-bit
   
      public DictionaryKey(byte[] data, boolean upper) throws ReloadException{ // Upper and lower levels
      
         if(upper)
            id = new Opaque(16, data);
            
         else
            id = new Opaque(16, data, 0);
         
      }
      
      public DictionaryKey(Opaque data) throws ReloadException{ // Upper level
      
         id = data;
      
         if(data.getBits() != 16)
            throw new WrongLengthReloadException();
         
      }
   
      public byte[] getBytes() throws IOException{
      
         return id.getBytes();
      
      }
      
      public byte[] getKey(){
      
         return id.getContent();
      
      }
   
   }