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
