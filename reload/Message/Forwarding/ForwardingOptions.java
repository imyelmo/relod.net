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
   package reload.Message.Forwarding;
	
   import java.io.*;
   import reload.Common.*;
   
   public class ForwardingOptions{
   
   
      private ForwardingOptionsType type;
      private byte flags;
      private short length;
      
   
      
      public ForwardingOptions(ForwardingOptionsType type, byte flags){
      
         this.type = type;
         this.flags = flags;
         this.length = 0;
      
      }
      
      public ForwardingOptions(byte[] data){
      
         type = ForwardingOptionsType.valueOf(data[0]);
         flags = data[1];
         length = Utils.toShort(data, 2);
      
      }
   
         
      public byte[] getBytes() throws IOException{
      
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(flags);
         baos.write(Utils.toByte(length));
         
         switch(type.getBytes()){
            case 0:
            /* This type may be extended */
               break;
         }
      
         return baos.toByteArray();
      
      }   
      
      public ForwardingOptionsType getType(){
      
         return type;
      
      }
      
      public byte getFlags(){
      
         return flags;
      
      }
      
   }