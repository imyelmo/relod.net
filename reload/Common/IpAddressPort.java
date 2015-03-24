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
   import reload.Common.Exception.*;
 
   public class IpAddressPort{
   
   
      private AddressType type;
      private byte length;
   
      private IPv4AddrPort v4addr_port;  
      private IPv6AddrPort v6addr_port;
   
   
      
      public IpAddressPort (IPv4AddrPort v4addr_port) throws IOException{
      
         type = AddressType.ipv4_address;
         length = (byte)v4addr_port.getBytes().length;
         this.v4addr_port = v4addr_port;
         
      }
      
      public IpAddressPort (IPv6AddrPort v6addr_port) throws IOException{
      
         type = AddressType.ipv6_address;
         length = (byte)v6addr_port.getBytes().length;
         this.v6addr_port = v6addr_port;
         
      }
      
      public IpAddressPort (String addr, int port) throws Exception{
      
         InetAddress ia = InetAddress.getByName(addr);
         
         if(ia instanceof Inet4Address)
            type = AddressType.ipv4_address;
         if(ia instanceof Inet6Address)
            type = AddressType.ipv6_address;
      	
         
         if(type == AddressType.ipv4_address){
            v4addr_port = new IPv4AddrPort(addr, port);
            length = (byte)v4addr_port.getBytes().length;
         }
         
         else if(type == AddressType.ipv6_address){
            v6addr_port = new IPv6AddrPort(addr, port);
            length = (byte)v6addr_port.getBytes().length;
         }
            
         else
            throw new WrongTypeReloadException();
         
      }
      
      public IpAddressPort (InetAddress addr, int port) throws Exception{
         
         if(addr instanceof Inet4Address)
            type = AddressType.ipv4_address;
         if(addr instanceof Inet6Address)
            type = AddressType.ipv6_address;
      	
         
         if(type == AddressType.ipv4_address){
            v4addr_port = new IPv4AddrPort(addr, port);
            length = (byte)v4addr_port.getBytes().length;
         }
         
         else if(type == AddressType.ipv6_address){
            v6addr_port = new IPv6AddrPort(addr, port);
            length = (byte)v6addr_port.getBytes().length;
         }
            
         else
            throw new WrongTypeReloadException();
         
      }
      
      public IpAddressPort (byte[] data) throws IOException{
      
         type = AddressType.valueOf(data[0]);
         length = data[1];
         
         switch(type.getBytes()){
         
            case 1:
            
               v4addr_port = new IPv4AddrPort(Utils.cutArray(data, 2));
               break;
            	
            case 2:
            
               v6addr_port = new IPv6AddrPort(Utils.cutArray(data, 2));
         }
      
      }
      
      public IpAddressPort (byte[] address, int port) throws Exception{
      
         if(address.length == 4)
            type = AddressType.ipv4_address;
         else if(address.length == 16)
            type = AddressType.ipv6_address;
         else
            throw new WrongTypeReloadException();
      
         
         if(type == AddressType.ipv4_address){
            v4addr_port = new IPv4AddrPort(address, port);
            length = (byte)v4addr_port.getBytes().length;
         }
         
         else if(type == AddressType.ipv6_address){
            v6addr_port = new IPv6AddrPort(address, port);
            length = (byte)v6addr_port.getBytes().length;
         }
            
         else
            throw new WrongTypeReloadException();
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(length);
      
         switch(type.getBytes()){
         
            case 1:
            
               baos.write(v4addr_port.getBytes());
               break;
            	
            case 2:
            
               baos.write(v6addr_port.getBytes());
         
         }
         
         return baos.toByteArray();
         
      }
   
      public byte getLength(){
      
         return length;
      }
      
      public InetAddress getAddress() throws Exception{
      
         if(type == AddressType.ipv4_address)
            return (Inet4Address)InetAddress.getByAddress(Utils.cutArray(v4addr_port.getBytes(), 4, 0));
         
         else if(type == AddressType.ipv6_address)
            return (Inet6Address)InetAddress.getByAddress(Utils.cutArray(v6addr_port.getBytes(), 16, 0));
            
         else
            throw new WrongTypeReloadException();
      
      }
   	
      public short getPort() throws Exception{
      
         if(type == AddressType.ipv4_address)
            return Utils.toShort(v4addr_port.getBytes(), 4);
         
         else if(type == AddressType.ipv6_address)
            return Utils.toShort(v6addr_port.getBytes(), 16);
         
         else
            throw new WrongTypeReloadException();
      
      
      }
      
      public byte[] getAddressBytes() throws Exception{
      
         if(type == AddressType.ipv4_address)
            return v4addr_port.getAddressBytes();
         
         else if(type == AddressType.ipv6_address)
            return v6addr_port.getAddressBytes();
            
         else
            throw new WrongTypeReloadException();
      
      }
      
      public byte[] getPortBytes() throws Exception{
      
         if(type == AddressType.ipv4_address)
            return v4addr_port.getPortBytes();
         
         else if(type == AddressType.ipv6_address)
            return v6addr_port.getPortBytes();
            
         else
            throw new WrongTypeReloadException();
      
      }
   
      public AddressType getType(){
      
         return type;
      
      }
   
   }