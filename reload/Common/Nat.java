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

	  
   public class Nat{
   
      public static InetAddress myIP;
      public static String uri;
      
      public static void setMyIp(IpAddressPort IP) throws Exception{
      
         InetAddress remote = IP.getAddress();
         InetAddress internal = InetAddress.getLocalHost();
      
         if(internal.isSiteLocalAddress() && !remote.isSiteLocalAddress() && IP.getType() == AddressType.ipv4_address)
            myIP = (Inet4Address)InetAddress.getByName(getIp());
            
         else
            myIP = internal;
      
      }
      
      public static void setMyIp(boolean external) throws Exception{
      
         if(external)
            myIP = (Inet4Address)InetAddress.getByName(getIp());
            
         else
            myIP = InetAddress.getLocalHost();
      
      }
   	
      public static String getIp() throws Exception {
      
      
         URL whatismyip = new URL("http://checkip.amazonaws.com");
         BufferedReader in = null;
         try {
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
         } 
         finally {
            if (in != null) 
               in.close(); 
         }
         
      }
   
   }
