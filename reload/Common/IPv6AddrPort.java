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
import java.util.UUID;
import reload.*;

public class IPv6AddrPort{

   private UUID addr; //128-bit integer
   private short port;
   

   
   public IPv6AddrPort (UUID addr, short port){
   
      this.addr = addr;
      this.port = port;
      
   }
	     
   public IPv6AddrPort (Inet6Address addr, int port){
   
      this(Utils.toUUID(addr.getAddress(), 0), (short)port);
               
   }
   
   public IPv6AddrPort (InetAddress addr, int port) throws UnknownHostException{
   
      this((Inet6Address)addr, port);
      
   }
   
   public IPv6AddrPort (String address, int port) throws UnknownHostException{
   
      this((Inet6Address)InetAddress.getByName(address), port);
      
   }
   
   public IPv6AddrPort (byte[] data){
   
      addr = Utils.toUUID(data, 0);
      port = Utils.toShort(data, 16);
      
   }
   
   public IPv6AddrPort (byte[] data, int port){
   
      addr = Utils.toUUID(data, 0);
      port = (short)port;
      
   }
   
   public byte[] getBytes() throws IOException{
   
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
      baos.write(Utils.toByte(addr.getMostSignificantBits()));
      baos.write(Utils.toByte(addr.getLeastSignificantBits()));
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