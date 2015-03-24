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

