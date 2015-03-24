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
   import java.net.*;
   import reload.*;
   
   public class IPv4AddrPort{
   
      private int addr;
      private short port;
      
   
      
      public IPv4AddrPort (int addr, short port){
      
         this.addr = addr;
         this.port = port;
         
      }
      
      public IPv4AddrPort (int b0, int b1, int b2, int b3, int port){
      
         byte b[] = new byte[4];
         b[0] = (byte)b0;
         b[1] = (byte)b1;
         b[2] = (byte)b2;
         b[3] = (byte)b3;
      
         this.addr = Utils.toInt(b, 0);
         this.port = (short)port;
         
      }
      
      public IPv4AddrPort (String addr, int port) throws UnknownHostException{
      
         this((Inet4Address)InetAddress.getByName(addr), port);
         
      }
      
      public IPv4AddrPort (InetAddress addr, int port) throws UnknownHostException{
      
         this((Inet4Address)addr, port);
         
      }
      
      public IPv4AddrPort (Inet4Address addr, int port){
      
         this.addr = Utils.toInt(addr.getAddress(), 0);
         this.port = (short)port;
         
      }
      
      public IPv4AddrPort (byte[] data){
      
         addr = Utils.toInt(data, 0);
         port = Utils.toShort(data, 4);
         
      }
      
      public IPv4AddrPort (byte[] data, int port){
      
         addr = Utils.toInt(data, 0);
         port = (short)port;
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(addr));
         baos.write(Utils.toByte(port));
         
         return baos.toByteArray();
      
      }
      
      public byte[] getAddressBytes() throws IOException{
      
         return Utils.toByte(addr);
      
      }
      
      public byte[] getPortBytes() throws IOException{
        
         return Utils.toByte(port);
      
      }
   
   
   }