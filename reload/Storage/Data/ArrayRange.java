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
	
   
   public class ArrayRange{
   
   
      private int first;
      private int last;
    
      
      public ArrayRange (int first, int last){
      
         this.first = first;
         this.last = last;
      
      }
      
      public ArrayRange (byte[] data) {
      
         first = Utils.toInt(data, 0);
         last = Utils.toInt(data, 4);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(first));
         baos.write(Utils.toByte(last));
         
         return baos.toByteArray();
      
      }
   
      public int getFirst(){
      
         return first;
      
      }
   
      public int getLast(){
      
         return last;
      
      }
   
   }