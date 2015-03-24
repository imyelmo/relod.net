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
   package reload.dev2dev;
   
   import reload.dev2dev.textclient.*;
   import java.net.*;
   import static java.lang.System.err;


   public class TextClientInterface{
   
      private InetAddress fromAddr;
      private InetAddress toAddr;
      private short fromPort;
      private short toPort;
      private String fromUser;
      private String toUser;
      
      private TextClient tc;
      
   	private String name; // Your own SIP name
      
   
      public TextClientInterface(InetAddress fromAddr, InetAddress toAddr, short fromPort, short toPort, String name){
      
         this.fromAddr = fromAddr;
         this.toAddr = toAddr;
         this.fromPort = fromPort;
         this.toPort = toPort;
         this.name = name;
      
      }
      
      public void start(){
      
         try{
         
            String fromIp = fromAddr.getHostAddress();
            String toIp = toAddr.getHostAddress();  
         	
            String from = "sip:usr@"+fromIp+":"+fromPort;
            String to = "sip:usr@"+toIp+":"+toPort;
         
            SipLayer sipLayer = new SipLayer(fromIp, fromPort, name);
            TextClient tc = new TextClient(sipLayer, from, to);
            sipLayer.setMessageProcessor(tc);
         
            tc.setVisible(true);
         } 
            catch (Throwable ex){
               System.err.println("Problem initializing the SIP stack.");
               ex.printStackTrace();
               
              /* Experimental */ 

              final Throwable[] suppressedExceptions = ex.getSuppressed();
         	  final int numSuppressed = suppressedExceptions.length;
         	  if (numSuppressed > 0)
         	  {
         		  err.println("tThere are " + numSuppressed + " suppressed exceptions:");
     	    	  for (final Throwable exception : suppressedExceptions)
     	    	  {
     	    		  err.println("tt" + exception.toString());
     	    	  }
         	  }
               
               /* Experimental */
         	  
               return;
            }
      
      
      
      }
   
   }
