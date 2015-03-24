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