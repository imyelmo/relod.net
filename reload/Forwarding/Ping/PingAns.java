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
   package reload.Forwarding.Ping;

   import java.io.*;
   import reload.Common.*;
   
   public class PingAns{
   
   
      private long response_id;
      private long time;
   
      
      
      public PingAns (long response_id, long time){
      
         this.response_id = response_id;
         this.time = time;
      
      }
      
      public PingAns (byte[] data) throws IOException{
      
         response_id = Utils.toLong(data, 0);
         time = Utils.toLong(data, 8);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(response_id));
         baos.write(Utils.toByte(time));
         
         return baos.toByteArray();
      
      }
   
      public long getResponseId(){
      
         return response_id;
      
      }
   
      public long getTime(){
      
         return time;
      
      }
   
   }