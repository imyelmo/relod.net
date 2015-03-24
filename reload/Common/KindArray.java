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

   import java.io.*;
   import reload.Common.Exception.*;
   
   public class KindArray{
   
      private byte kind_length;
      private KindId[] kind_array; //<0..2^8-1>
    
      
      public KindArray (KindId[] kind_array) throws Exception{
      
         this.kind_array = kind_array;
         
         for (int i=0; i<kind_array.length; i++)
            kind_length += kind_array[i].getBytes().length;
            
         if(kind_length > Math.pow(2, 8)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public KindArray (byte[] data) throws Exception{
      
         kind_length = data[0];
         
         data = Utils.cutArray(data, kind_length, 1);
         
         int offset = 0;
         
         int num = data.length / 4;	//32-bit, 4 bytes
      
         kind_array = new KindId[num];
      
         for (int i=0; i<num; i++){
         
            kind_array[i] = new KindId(Utils.cutArray(data, offset));
            offset += kind_array[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(kind_length));
      
         for (int i=0; i<kind_array.length; i++)
            baos.write(kind_array[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
   
   }