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
package reload.Forwarding;

import reload.Message.*;
import reload.Forwarding.Request.*;
import reload.Link.*;
import reload.Topology.*;
import reload.Common.*;
import reload.Common.Exception.*;
import java.net.InetAddress;

public class Initialization{

   private ForwardingCheck fc;
   private XML configuration;
   private String olp;
   
   private LinkInterface bootstrapConn;
   private boolean first;
   
	
   public Initialization (ForwardingCheck fc, XML configuration, String olp, boolean first){
   
      this.fc = fc;
      this.configuration = configuration;
      this.olp = olp;
      this.first = first;
   
   }


   public NodeId initiate() throws Exception{
         
      InetAddress address = InetAddress.getLocalHost();
   
      NodeId thisNodeId = (NodeId)Module.tpi.generateNewId(address, true);
      
      System.out.println("My Node-ID: " + thisNodeId.print());

      
      Module.tpi.routingTable.setThisNode(thisNodeId);
   
      if(first)
         return new NodeId();
      
      if(attachReqAP()){
      
         NodeId AP = attachAnsAP();
         bootstrapConn.close(); // Closes bootstrap connection
         Module.falm.updateIntialization = true; // Ready to receive update
         return AP;
      }
      
      else
         return null;
      
   }
   
   private boolean attachReqAP() throws Exception{
   
   
      IpAddressPort[] bootstrap = configuration.getBootstrapNode();
      
      byte[] nodeIDbytes = Module.tpi.routingTable.getThisNode().getId();	
      ResourceId ApRId = new ResourceId(nodeIDbytes, true);	
      ApRId.add(1); // Admitting Peer Resource-ID
   	
      try{
      
         boolean exit = false;
      
         for (int i=0; !exit; i++){
         
         
            try{
               bootstrapConn = new LinkInterface(olp, bootstrap[i]);  
            }
            catch (java.net.ConnectException ce){
               System.err.println("Failed to Attach to boostrap node "+bootstrap[i].getAddress()+":"+bootstrap[i].getPort()+".");
               continue;
            }
            catch (java.net.SocketException se){
               System.err.println("Failed to Attach to boostrap node "+bootstrap[i].getAddress()+":"+bootstrap[i].getPort()+".");
               continue;
            }
            
            exit = true;
            Nat.setMyIp(bootstrap[i]);
         
         }
      
      }
      catch (ArrayIndexOutOfBoundsException aioobe){
            
         System.err.println("Failed to Attach. No boostrap node available.");
         return false;  
         	
      }
         
   		         
      AttachReqAns req = new AttachReqAns(true, new IpAddressPort(Nat.myIP, Module.falm.getPort())); // We ask for the routing table
   	
      byte[] msg_body = req.getBytes();
      
      short code = 3; // AttachReq
      
      byte[] data_msg = new Message(msg_body, ApRId, Module.tpi.routingTable.getThisNode(), code).getBytes();
         
      bootstrapConn.send(data_msg);
      
      return true;
   		
   }
   
   private NodeId attachAnsAP() throws Exception{
   
      byte[] response = bootstrapConn.receive();
      Message message = new Message(response); 
   	
      if(!fc.checkMessage(message) || message.getMessageCode() != 4)
         throw new WrongPacketReloadException(4);
      
      NodeId ApNId = message.getSourceNodeId();
      
      
      byte[] msg_body = message.getMessageBody();
      AttachReqAns ans = new AttachReqAns(msg_body);
      
      IpAddressPort IpAp = ans.getCandidates(0).getAddressPort(); //If public addresses, 0
      
      Module.falm.createConnection(ApNId, IpAp);
      
      return ApNId;
   
   }
   
}