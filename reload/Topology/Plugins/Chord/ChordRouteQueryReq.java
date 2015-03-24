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
   package reload.Topology.Plugins.Chord;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Message.Forwarding.Destination;
   
   public class ChordRouteQueryReq{
   
      private boolean send_update;
      private Destination destination;
      private Opaque data; //16-bit   
      
      public ChordRouteQueryReq (boolean send_update, Destination destination, byte[] data) throws ReloadException{
      
         this.send_update = send_update;
         this.destination = destination;
         this.data = new Opaque(16, data);
      
      }
      
      public ChordRouteQueryReq (byte[] data) throws Exception{
      
         send_update = Utils.toBoolean(data);
         destination = new Destination(Utils.cutArray(data, 1));
         int length = destination.getBytes().length;
         this.data = new Opaque(16, data, 1+length);
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(send_update));
         baos.write(destination.getBytes());
         baos.write(data.getBytes());
      
         
         return baos.toByteArray();
      
      }
      
      public boolean getSendUpdate(){
      
         return send_update;
      
      }
   	
      public Destination getDestination(){
      
         return destination;
      
      }
   	
      public byte[] getData() throws IOException{
      
         return data.getContent();
      
      }
   
   
   }