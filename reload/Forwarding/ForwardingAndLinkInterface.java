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
   
   import reload.Message.Message;
   import reload.Message.Forwarding.DestinationType;
   import reload.Common.*;
   import reload.Common.Error.*;
   import reload.Common.Exception.*;
   import reload.Forwarding.Request.*;
   import reload.Forwarding.Ping.*;
   import reload.Forwarding.Config.*;
   import reload.Forwarding.Task.*;
   import reload.Link.*;
   import reload.dev2dev.*;
   import java.util.*;
   import java.net.InetAddress;

/**
* ForwardingAndLinkInterface class is the interface of the Forwarding & Link Management module.
* @author Malosa 
* @version 0.1
*/

   public class ForwardingAndLinkInterface{
   
      private int port; // TCP port for THIS node
      private boolean first; // This is the first node in the overlay
   	
      private ForwardingCheck fc;
      private XML configuration;
      private ForwardingThread thread;
      
      private LinkInterface[] client;
      private LinkInterface server;
      
      private String olp; // Overlay Link Protocol
   	
      public SendData sendData;
      public boolean updateIntialization; // Flag for receiving first update from AP
      public boolean joinIntialization; // Flag for receiving update after join from AP
      
   	
   /**
    * Establishes the Forwarding & Link Management module.
    * @param port the port used by RELOAD
    * @param first if this is the first node to initialize in the overlay
    */    
      public ForwardingAndLinkInterface(int port, boolean first) throws Exception{
      
         this.port = port;
         this.first = first;
      
         fc = new ForwardingCheck(configuration, first);
         configuration = new XML();
         thread = new ForwardingThread();
         
         client = new LinkInterface[134];
         
         olp = configuration.getOverlayLinkProtocol();
         
         sendData = new SendData();
         updateIntialization = false;
         joinIntialization = false;
      
      }
   
   /**
    * Initiates the Forwarding & Link Management module. It is called after the constructor.
    * @return true if it could start and connect to the Admitting Peer
    */    
      public boolean start() throws Exception{
                    
         Initialization ini = new Initialization(fc, configuration, olp, first);
         NodeId AP = ini.initiate();
      	
         if(AP == null)
            return false;
            
         server = new LinkInterface(olp, port);
      
         createThreads();
         
         ForwardingLoop loop = new ForwardingLoop(fc, server);
         loop.start();
         
         if(!first){
         
            synchronized(this){
            
               wait();
               
               Thread.currentThread().sleep(50);
               Module.tpi.createJoin(AP);
            
               joinIntialization = true;
            
               wait();
            
               Thread.currentThread().sleep(50); // We wait so UpdateAns is sent first
               Module.tpi.createUpdate();	// ALL NODES
            
               wait();
            
            }
            
         }
         
         return true;
      
      }
      
      private void createThreads() throws Exception{
         
         if(!first)
            createClientThread(0); // Client thread numLink=0 (AP link)
      	
         Module.falm.getForwardingThread().start(); // Task thread
         Module.tpi.getTopologyThread().start(); // Task thread
         Module.si.getStorageThread().start(); // Task thread
      
      }
   
   /**
    * Creates a new Client thread (ReloadThread).
    * @param numLink the link number
    */     
      public void createClientThread(int numLink) throws Exception{
      
         ReloadThread ct = new ReloadThread(fc, client[numLink], Module.tpi.connectionTable.getNode(numLink, true));
         ct.start();
      
      }
      
   /**
    * It forms a Client connection, adding a new entry in the connection table and creating a new LinkInterface object.
    * @param id the NodeId of the peer
    * @param ip the IP address of the peer
    * @return connection number or -1 if it failed
    */  
      public int createConnection(NodeId id, IpAddressPort ip) throws Exception{
      
         int numLink;
           
         try{
         
            numLink = Module.tpi.connectionTable.addEntry(id, ip, true);
         	
            client[numLink] = new LinkInterface(olp, ip);
             
         }
            catch (java.net.ConnectException ce){
               System.err.println("Failed to create conection to node "+ip.getAddress()+":"+ip.getPort()+".");
               return -1;
            }
            catch (java.net.SocketException se){
               System.err.println("Failed to create conection to node "+ip.getAddress()+":"+ip.getPort()+".");
               return -1;
            }
               
         return numLink;
      		
      }
   
   /**
    * Method to be called from Message Transport when it receives an Attach Request message.
    * @param msg_body Attach Request message to decode
    * @param node peer which the message comes from (not the creator, just the last hop)
    * @return the response (Attach Answer) to the request
    */     
      public byte[] attach_req(byte[] msg_body, NodeId node) throws Exception{
            
         AttachReqAns req = new AttachReqAns(msg_body);
         
         IpAddressPort IP = req.getCandidates(0).getAddressPort(); //If public address, 0
       
                   
         AttachReqAns ans = new AttachReqAns(false, new IpAddressPort(Nat.myIP, port)); // We do not ask for the routing table
      
      
         if(!Module.tpi.connectionTable.isDirectlyConnected(node)){
         
            int num = Module.tpi.connectionTable.addEntry(node, IP, false);
         
            if(req.getSendUpdate())
               Module.tpi.createUpdate(node, num, false, (byte)3, true); // full
         }
         
         else
            if(req.getSendUpdate())
               Module.tpi.createUpdate(node, (byte)3);
                 
         return ans.getBytes();      
      		   
      }
   
   /**
    * Method to be called from Message Transport when it receives a Ping Request message.
    * @param msg_body Ping Request message to decode
    * @return the response (Ping Answer) to the request
    */   
      public byte[] ping_req(byte[] msg_body) throws Exception{
      
         PingReq req = new PingReq(msg_body, false); // Lower level
       
         int paddingLength = req.getPaddingLength(); // Not used
         
         long response_id = new Random().nextLong();
         
         long time = 0; // Pending
      	
         PingAns ans = new PingAns(response_id, time);
      	
         return ans.getBytes();
      
      }
   
   /**
    * Method to be called from Message Transport when it receives an App Attach Request message.
    * @param msg_body Attach Request message to decode
    * @param ID peer which the message comes from (not the creator, just the last hop)
    * @return the response (App Attach Answer) to the request
    */  
      public byte[] app_attach_req(byte[] msg_body, NodeId ID) throws Exception{
      
         AppAttachReq req = new AppAttachReq(msg_body);
      	 
         IpAddressPort ip_port = req.getCandidates(0).getAddressPort(); //If public address, 0
         
         short application  = req.getApplication();
         if(false)
            throw new ErrorForbidden(); // No application is supported
         if(application != 5060)
            throw new UnimplementedReloadException("Other applications than SIP");
            
         InetAddress toAddr = ip_port.getAddress();
         short toPort = ip_port.getPort();
         
         InetAddress fromAddr = InetAddress.getLocalHost();
         short fromPort = Module.falm.getPort();
         
         TextClientInterface tci = new TextClientInterface(fromAddr, toAddr, fromPort, toPort, Nat.uri);
         
         tci.start();
      
      
         AppAttachAns ans = new AppAttachAns(new IpAddressPort(Nat.myIP, port), application);
      
         return ans.getBytes();      
      		   
      }
   
   /**
    * Method to be called from Message Transport when it receives a Config Update Request message.
    * @param msg_body Config Update Request message to decode
    * @return the response (Config Update Answer) to the request
    */
      public byte[] config_update_req(byte[] msg_body) throws Exception{
      
         ConfigUpdateReq req = new ConfigUpdateReq(msg_body, false); // Lower level
       
         ConfigUpdateType type = req.getType();
         
         byte[] config_data;
         KindDescription[] kind;
      
         if(type == ConfigUpdateType.config)
            config_data = req.getConfigData();
            

         
         else if(type == ConfigUpdateType.kind)
            kind = req.getKinds();
            
         
         else
            throw new WrongTypeReloadException();   
      	
         ConfigUpdateAns ans = new ConfigUpdateAns();
      	
         return ans.getBytes();
      
      }
   
   /**
    * Creates an Attach Task.
    * @param node1 array of nodes to be attached
    * @param node2 array of nodes to be attached
    * @param initialization true if node is initializating
    */     
      public void createAttachRoute(NodeId[] node1, NodeId[] node2, boolean initialization){
         
         AttachRouteTask task = new AttachRouteTask(thread, node1, node2, initialization);
         
         synchronized(thread){
         
            boolean empty = thread.isEmpty();
            
            thread.add(task);
            
            if(empty)
               thread.notify();
              
         }
               
      }
   
   /**
    * Creates an App Attach Task.
    * @param node NodeId to connect to
    * @param application the app code
    */     
      public void createAppAttach(NodeId node, short application){
         
         AppAttachTask task = new AppAttachTask(thread, node, application);
         
         synchronized(thread){
         
            boolean empty = thread.isEmpty();
            
            thread.add(task);
            
            if(empty)
               thread.notify();
              
         }
               
      }
   
   /**
    * Creates an Attach Task.
    * @param node1 array of nodes to be attached
    * @param node2 array of nodes to be attached
    * @param node3 array of nodes to be attached
    * @param initialization true if node is initializating
    */      
      public void createAttachRoute(NodeId[] node1, NodeId[] node2, NodeId[] node3, boolean initialization){
         
         AttachRouteTask task = new AttachRouteTask(thread, node1, node2, node3, initialization);
         
         synchronized(thread){
         
            boolean empty = thread.isEmpty();
            
            thread.add(task);
            
            if(empty)
               thread.notify();
              
         }
               
      }
   
   /**
    * Creates an Attach Task.
    * @param fingers array of fingers to be attached
    * @param initialization true if node is initializating
    */   
      public void createAttachRoute(NodeId[] fingers, boolean initialization){ // Fingers
         
         AttachRouteTask task = new AttachRouteTask(thread, fingers, initialization);
         
         synchronized(thread){
         
            boolean empty = thread.isEmpty();
            
            thread.add(task);
            
            if(empty)
               thread.notify();
              
         }
               
      }
   
   /**
    * Creates an Attach Task for fingers (create connections at 180�, 90�, etc).
    */      
      public void createAttachFingersRoute(){ // Fingers
         
         AttachRouteTask task = new AttachRouteTask(thread);
         
         synchronized(thread){
         
            boolean empty = thread.isEmpty();
            
            thread.add(task);
            
            if(empty)
               thread.notify();
              
         }
               
      }
   
   /**
    * Sends a message to the network.
    * @param data_msg the message (a whole RELOAD structure)
    * @param num_link the link number
    * @param cli true if connection is client, false if is server
    */     
      public void send(boolean cli, int num_link, byte[] data_msg) throws Exception{
      
         if(cli)
            client[num_link].send(data_msg);
         else
            server.send(num_link, data_msg);
      
      }
   
   /**
    * Sends a message to the network.
    * @param message a Message structure
    */    
      public void send(Message message) throws Exception{
      
         Id FdestID = message.getDestinationId()[0]; // NodeId or ResourceId
         DestinationType type = FdestID.getType();
         
         int num_link;
         boolean Bclient;
      	
         if(type == DestinationType.node && Module.tpi.connectionTable.isDirectlyConnected((NodeId)FdestID)){
            Bclient = Module.tpi.connectionTable.isClient(FdestID);
            num_link = Module.tpi.connectionTable.getNumLink(FdestID, Bclient);		
         }			
         
         else{
            NodeId nextHop = Module.tpi.route(FdestID);
            Bclient = Module.tpi.connectionTable.isClient(nextHop);
            num_link = Module.tpi.connectionTable.getNumLink(nextHop, Bclient);    
         }
         
         if(type == DestinationType.node && server.bootstrapIsConnected((NodeId)FdestID)){
            Bclient = false;
            num_link = server.getBootstrapNumLink((NodeId)FdestID);		
         }   
              
         byte[] data_msg = message.getBytes();
         
         if(Bclient)
            client[num_link].send(data_msg);
         else
            server.send(num_link, data_msg);
            
      }
   
   /**
    * Creates a ping attach (ping will be done to all nodes directly connected).
    */     
      public void pingAllNodes(){
      
         PingTask task = new PingTask(thread);
         
         synchronized(thread){
         
            boolean empty = thread.isEmpty();
            
            thread.add(task);
            
            if(empty)
               thread.notify();
              
         }
      
      }
   
   /**
    * Closes the specified connection.
    * @param numLink the link number
    * @param cli true if connection is client, false if is server
    */     
      public void close(int numLink, boolean cli) throws Exception{
         
         if(cli){
            NodeId node = Module.tpi.connectionTable.getNode(numLink, cli);
            Module.tpi.connectionTable.delete(numLink, cli);
            Module.tpi.routingTable.delete(node);
            client[numLink].close();
         }
         
         else{
         
            if(numLink < 1000){
               NodeId node = Module.tpi.connectionTable.getNode(numLink, cli);
               Module.tpi.connectionTable.delete(numLink, cli);
               Module.tpi.routingTable.delete(node);  
            }
            
            else
               server.deleteBootstrap(numLink);
         
            server.close(numLink);
         }
            
      }
      
      public void closeServer() throws java.io.IOException{
      
         server.close();
      
      }
   
   /**
    * Returns port.
    * @return the TCP or UDP port
    */     
      public short getPort(){
      
         return (short)port;
      
      }
   
   /**
    * Returns configuration.
    * @return the XML configuration data
    */     
      public XML getConfiguration(){
      
         return configuration;
      
      }
   
   /**
    * Returns the forwarding thread.
    * @return the ForwardingThread object
    */     
      public ForwardingThread getForwardingThread(){
      
         return thread;
      
      }
      
   }