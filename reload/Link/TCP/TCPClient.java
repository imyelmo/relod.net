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