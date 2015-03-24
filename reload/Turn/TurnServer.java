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
   package reload.Turn;

   import java.io.*;
   import reload.Common.*;
   
   public class TurnServer{
   
   
      private byte iteration;
      private IpAddressPort server_address;
      
   
      
      public TurnServer (byte iteration, IpAddressPort server_address){
      
         this.iteration = iteration;
         this.server_address = server_address;
         
      }
      
      public TurnServer (byte[] data) throws IOException{
      
         iteration = data[0];
         
         server_address = new IpAddressPort(Utils.cutArray(data, 1));
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(iteration);
         baos.write(server_address.getBytes());
         
         return baos.toByteArray();
      
      }
   
   }