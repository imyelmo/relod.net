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
   package reload.Message.MessageCont;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class MessageExtension{
   
      private MessageExtensionType type;
      private boolean critical;
      private Opaque extension_contents; //<0..2^32-1>
      
   
      
      public MessageExtension (MessageExtensionType type, boolean critical, byte[] extension_contents) throws ReloadException{
      
         this.type=type;
         this.critical=critical;
         this.extension_contents = new Opaque(32, extension_contents);
         
      }
    
      public MessageExtension (byte[] data) throws ReloadException{
      
         type = MessageExtensionType.valueOf(Utils.toShort(data, 0));
         critical = Utils.toBoolean(data[2]);
         extension_contents = new Opaque(32, data, 3);
         
      }
     
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(Utils.toByte(critical));
         baos.write(extension_contents.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public MessageExtensionType getType(){
      
         return type;
      
      }
      
      public boolean getCritical(){
      
         return critical;
      
      }
      
      public byte[] getExtensionContents() throws IOException{
      
         return extension_contents.getContent();
      
      }
   
   }