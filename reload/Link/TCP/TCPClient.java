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
   package reload.Link.TCP;
   
   import java.io.*;
   import java.net.*;
   import reload.Common.*;

 
   public class TCPClient extends TCP{
   
   
      private Socket socket;
      private DataOutputStream out;
      private DataInputStream in;
    
   
   
      public TCPClient(IpAddressPort ip) throws Exception{
      	
         socket = new Socket(ip.getAddress(), ip.getPort());  
         
         out = new DataOutputStream(socket.getOutputStream());
         in = new DataInputStream(socket.getInputStream());
         
      }
      
      public void send(byte[] data) throws IOException{
      
         send(data, out); // Super
      
      }
   	
      public byte[] receive() throws IOException{
      
         return receive(in); // Super
      
      }
      
      public void close() throws IOException{
      
         in.close();
         out.close();
         socket.close();
      
      }
      
      public InetAddress getInetAddress(){
      
         return socket.getInetAddress();
      
      }
   	
   }