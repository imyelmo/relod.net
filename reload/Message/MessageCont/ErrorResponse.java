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
 
   public class ErrorResponse{
   
      private short error_code;
      private Opaque error_info; //<0..2^16-1>
      
      public ErrorResponse (short error_code, byte[] error_info) throws ReloadException{
      
         this.error_code = error_code;
         this.error_info = new Opaque(16, error_info);
      
      }
      
      public ErrorResponse (short error_code) throws ReloadException{ // error_info is optional
      
         this.error_code = error_code;
         this.error_info = new Opaque(16, new byte[0]);
      
      }
      
      public ErrorResponse (byte[] data) throws ReloadException{
      
         this.error_code = Utils.toShort(data, 0);
         this.error_info = new Opaque(16, data, 2);
      
      }
   	
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(error_code));
         baos.write(error_info.getBytes());
      
         return baos.toByteArray();
      
      }
   
      public short getErrorCode(){
      
         return error_code;
      
      }
      
      public byte[] getErrorInfo() throws IOException{
      
         return error_info.getContent();
      
      }
   
   }