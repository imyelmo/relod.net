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