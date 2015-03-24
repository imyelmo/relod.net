   package reload.Forwarding.Task;
   
   import reload.Message.*;
   import reload.Forwarding.*;
   import reload.Forwarding.Request.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.dev2dev.*;
   import java.net.InetAddress;

   public class AppAttachTask extends ForwardingTask{
   
      private NodeId dest;	
      private short application;
   	
      public AppAttachTask(ForwardingThread thread, NodeId dest, short application){
      
         super(thread);
         
         type = TaskType.app_attach;
         
         this.dest = dest;
         this.application = application;
            
      }  
      
      public void start() throws Exception{
      
         IpAddressPort ip_port = send_receive(dest, application);
         
         InetAddress toAddr = ip_port.getAddress();
         short toPort = ip_port.getPort();
         
         InetAddress fromAddr = InetAddress.getLocalHost();
         short fromPort = Module.falm.getPort();
         
         TextClientInterface tci = new TextClientInterface(fromAddr, toAddr, fromPort, toPort, Nat.uri);
         
         tci.start();
                    
      }
      
      public IpAddressPort send_receive(NodeId dest, short application) throws Exception{
      
                  
         AppAttachReq req = new AppAttachReq(new IpAddressPort(Nat.myIP, Module.falm.getPort()), application);
      	
         byte[] msg_body = req.getBytes();
         
         short code = 29; // AppAttachReq
         
         Module.msg.send(msg_body, code, dest);
               
         Message mes = receive();
            
         code = mes.getMessageCode();
      		
         if (code != 30) // AppAttachAns
            throw new WrongPacketReloadException(4);
            
      	
         msg_body = mes.getMessageBody();
      	
         AppAttachAns ans = new AppAttachAns(msg_body);
         
         IpAddressPort IPdest = ans.getCandidates(0).getAddressPort(); //If public addresses, 0
         
         short app = ans.getApplication();
      	
         if(application != app)
            throw new WrongTypeReloadException("Application not match");
      	
         if(application != 5060)
            throw new UnimplementedReloadException("Other applications than SIP");
         
         return IPdest;
      	
      }
      
   }

