   package reload.Link.TCP;
   
   import java.io.*;
   import java.net.*;
   import java.util.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Link.BoostrapList;
 
   public class TCPServer extends TCP{
   
   
      private ServerSocket sc;
      
      private Socket[] socket;
      private DataOutputStream[] out;
      private DataInputStream[] in;
      
      private Socket[] bootSocket;
      private DataOutputStream[] bootOut;
      private DataInputStream[] bootIn;
      
      private BoostrapList bootstrap;
   
      
      public TCPServer(int port) throws IOException{
      	
         sc = new ServerSocket(port);  
      
         socket = new Socket[134];
         out = new DataOutputStream[134];
         in = new DataInputStream[134];
         
         bootSocket = new Socket[128];
         bootOut = new DataOutputStream[128];
         bootIn = new DataInputStream[128];
         
         bootstrap = new BoostrapList();
         
      }
      
      public int newConnection() throws Exception{
      
         try{
            Socket socket = sc.accept();
            
            InetAddress remoteIP = ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress();
         
            int num = Module.tpi.connectionTable.getNumLink(remoteIP);
         
            if (num == -1){

            
               num = bootstrap.addEntry(remoteIP);
                        
               addBootstrap(num, socket);
            
               return num+1000;
            }
         
            add(num, socket);
         
            return num;
         
         }
            catch(java.net.SocketException se){
               return -1;
            }
      
      }
      
      private synchronized void add(int position, Socket socket) throws IOException{
       
         
         DataOutputStream out = new DataOutputStream(socket.getOutputStream());
         DataInputStream in = new DataInputStream(socket.getInputStream());
      
         this.socket[position] = socket;
         this.out[position] = out;
         this.in[position] = in;
      
      }
      
      private synchronized void addBootstrap(int position, Socket socket) throws IOException{
       
         
         DataOutputStream out = new DataOutputStream(socket.getOutputStream());
         DataInputStream in = new DataInputStream(socket.getInputStream());
      
         this.bootSocket[position] = socket;
         this.bootOut[position] = out;
         this.bootIn[position] = in;
      
      }
      
      public void send(int num, byte[] data) throws IOException{
      
         if(num < 1000)
            send(data, out[num]); // Super
         else
            send(data, bootOut[num-1000]);
      
      }
   	
      public byte[] receive(int num) throws IOException{
      
         if(num < 1000)
            return receive(in[num]); // Super
         else
            return receive(bootIn[num-1000]);
      
      }
         
      public void close() throws IOException{
          
         sc.close();
      
      }
      
      public void close(int num) throws IOException{
       
         if(num < 1000){
            in[num].close();
            out[num].close();
            socket[num].close();
         }
         else{
            bootIn[num-1000].close();
            bootOut[num-1000].close();
            bootSocket[num-1000].close();
         }
      
      }
      
      public boolean deleteBootstrap(int num) throws Exception{
       
         if(num < 1000)
            throw new ReloadException("Wrong bootstrap connection num.");
         
         else
            return bootstrap.delete(num-1000);      
      }
      
      public void addBootstrapNode(NodeId node, int num) throws ReloadException{
       
         if(num < 1000)
            throw new ReloadException("Wrong bootstrap connection num.");
         
         else
            bootstrap.addNode(node, num-1000);      
      }
      
      public boolean bootstrapIsConnected(NodeId node){
       
         return bootstrap.exists(node);
               
      }
      
      public int getBootstrapNumLink(NodeId node){
       
         return bootstrap.getNumLink(node)+1000;
               
      }
      
      public InetAddress getInetAddress(int num){
      
         if(num < 1000){
            InetSocketAddress isa = (InetSocketAddress)socket[num].getRemoteSocketAddress(); 
            return isa.getAddress(); 
         }
         
         else{
            InetSocketAddress isa = (InetSocketAddress)bootSocket[num-1000].getRemoteSocketAddress(); 
            return isa.getAddress();
         }
      
      }
   		
   }