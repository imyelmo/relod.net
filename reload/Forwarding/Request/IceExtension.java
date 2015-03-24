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
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class IceExtension{
   
   
      private Opaque name; //<0..2^16-1>
      private Opaque value; //<0..2^16-1>
      
   	   
      
      public IceExtension (byte[] name, byte[] value) throws ReloadException{
      
         this.name = new Opaque(16, name);
         this.value = new Opaque(16, value);
      
      }
      
      public IceExtension (byte[] data) throws Exception{
      
      
         name = new Opaque(16, data, 0);
         
         int offset = name.getBytes().length;
      
         name = new Opaque(16, data, offset);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(name.getBytes());
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
   
   }