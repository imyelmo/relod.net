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

package reload;

import java.net.*;

/**
* Main class contains the main method for the SIP Usage.
* @author Malosa 
* @version 0.1
*/		
public class Main{ // Main for P2P-SIP Usage
   
   private static int port;
   private static boolean first_node_in_overlay;
   private static boolean nat_first;
   private static String sip_uri[];
   private static String sip_uri_redirection_from;
   private static String sip_uri_redirection_to;

/**
 * Main method.
 * @param arg arguments must be empty
 */  
   public static void main(String arg[]) throws Exception{
   
      port = 9999;
      first_node_in_overlay = true;
      nat_first = true;
      
      String IP = InetAddress.getLocalHost().getHostAddress();
      String num = IP.substring(IP.lastIndexOf('.')+1);	
      String uri = "node" + num + "@relod.net";
      
      if (arg.length == 3) {
    	  port = Integer.parseInt(arg[0]);
    	  first_node_in_overlay = Boolean.parseBoolean(arg[1]);
    	  nat_first = Boolean.parseBoolean(arg[2]);
      }
   	
      if (arg.length == 4) {
    	  port = Integer.parseInt(arg[0]);
    	  first_node_in_overlay = Boolean.parseBoolean(arg[1]);
    	  nat_first = Boolean.parseBoolean(arg[2]);
    	  uri = arg[3];
      }
   
      sip_uri = new String[1];
      sip_uri[0] = uri;
   	
      //sip_uri_redirection_from = "marcos@relod.es";
      //sip_uri_redirection_from =uri;
      sip_uri_redirection_to = uri;
      
      reload.Common.Nat.uri = uri;
   
      start();
           
   }
   
   private static void start() throws Exception{
   
      if(first_node_in_overlay)
         reload.Common.Nat.setMyIp(nat_first);
   
      SipUsage usage = new SipUsage(port, first_node_in_overlay);
      
      if (!usage.startOverlay())
         return;
   	
      for (int i=0; i<sip_uri.length; i++)
         usage.setUri(sip_uri[i]);
   	
      if(sip_uri_redirection_from != null)
         usage.setUriRedirection(sip_uri_redirection_from, sip_uri_redirection_to);
            
      usage.startConsole();
      
   }
}