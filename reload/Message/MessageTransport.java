   package reload.Message;

   import java.util.*;
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Error.*;
   import reload.Common.Exception.*;
   import reload.Forwarding.*;
   import reload.Topology.TopologyPluginInterface;
   
   import reload.Forwarding.Request.*;
   import reload.Storage.*;
   import reload.Topology.*;

   import reload.Message.MessageCont.*;
   	
   import java.net.InetAddress;   
   
/**
* MessageTransport class is the interface of the MessageTransport module.
* @author Malosa 
* @version 0.1
*/   
   public class MessageTransport {
   
      private int port;
      private boolean first;
   
   /**
    * Establishes the Message Transport module.
    * @param port the port used by RELOAD
    * @param first if this is the first node to initialize in the overlay
    */      
      public MessageTransport(int port, boolean first) throws Exception{ 
      
         this.port = port;
         this.first = first;
      
         Module.tpi = new TopologyPluginInterface();
         
         Module.falm = new ForwardingAndLinkInterface(port, first);
      
      }
   
   /**
    * Initiates the Message Transport module. It is called after the constructor.
    * @return true if it could start and connect to the Admitting Peer
    */     
      public boolean start() throws Exception{ // New node in the overlay
      
         return Module.falm.start();
         
      }
   
   /**
    * Sends a message.
    * @param message a Message structure
    */      
      public void send(Message message) throws Exception{
         
         Module.falm.send(message);
      
      }
   
   /**
    * Sends a message.
    * @param msg_body the message body (MESSAGE BODY field from the RELOAD structure)
    * @param code the the message code (MESSAGE CODE field from the RELOAD structure)
    * @param dest the the destination array (ResourceId[] or NodeId[] to form the DESTINATION LIST)
    */ 
      public void send(byte[] msg_body, short code, Id[] dest) throws Exception{
      
         Message msg = new Message(msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
            
         Module.falm.send(msg);
         
      }
   
   /**
    * Sends a message.
    * @param msg_body the message body (MESSAGE BODY field from the RELOAD structure)
    * @param code the the message code (MESSAGE CODE field from the RELOAD structure)
    * @param dest the the destination (ResourceId or NodeId)
    */     
      public void send(byte[] msg_body, short code, Id dest) throws Exception{
      
         this.send(msg_body, code, new Id[]{dest});
         
      }
   
   /**
    * Sends a message.
    * @param msg_body the message body (MESSAGE BODY field from the RELOAD structure)
    * @param code the the message code (MESSAGE CODE field from the RELOAD structure)
    * @param dest the the destination array (ResourceId[] or NodeId[] to form the DESTINATION LIST)
    * @param numLink the link number
    * @param client true if connection is client, false if is server
    */    
      public void send(byte[] msg_body, short code, Id[] dest, int numLink, boolean client) throws Exception{
      
         Message msg = new Message(msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         
         byte[] message = msg.getBytes();
         
         Module.falm.send(client, numLink, message);
         
      }
   
   /**
    * Sends a message.
    * @param msg_body the message body (MESSAGE BODY field from the RELOAD structure)
    * @param code the the message code (MESSAGE CODE field from the RELOAD structure)
    * @param dest the the destination (ResourceId or NodeId)
    * @param numLink the link number
    * @param client true if connection is client, false if is server
    */      
      public void send(byte[] msg_body, short code, Id dest, int numLink, boolean client) throws Exception{
      
         this.send(msg_body, code, new Id[]{dest}, numLink, client);
         
      }
   
   /**
    * Processes a Request, method to be called from the Forwarding & Link Management module.
    * @param message the Message structure
    * @param peer peer which the message comes from (not the creator, just the last hop)
    */   	  
      public byte[] processReq(Message message, NodeId peer) throws Exception{ 
      
      
         short code = message.getMessageCode();
         
         if(code >= 0x8000 && code <= 0xfffe)
            throw new WrongTypeReloadException("reserved");
      
         switch(code){
            case 1:
               return probe_req(message, peer);
            case 3:
               return attach_req(message, peer);
            case 5:
               throw new WrongTypeReloadException("unused");
            case 7:
               return store_req(message, peer);
            case 9:
               return fetch_req(message, peer);
            case 11:
               throw new WrongTypeReloadException("unused (was remove_req)");
            case 13:
               return find_req(message, peer);
            case 15:
               return join_req(message, peer);
            case 17:
               return leave_req(message, peer);
            case 19:
               return update_req(message, peer);  
            case 21:
               return route_query_req(message, peer);
            case 23:
               return ping_req(message, peer);
            case 25:
               return stat_req(message, peer);
            case 27:
               throw new WrongTypeReloadException("unused (was attachlite_req)");
            case 29:
               return app_attach_req(message, peer);
            case 31:
               throw new WrongTypeReloadException("unused (was app_attachlite_req)");
            case 33:
               return config_update_req(message, peer);
            case 35:
               return exp_a_req(message, peer);
            case 37:
               return exp_b_req(message, peer);
            case (short)0xffff:
               return error(message);
            default:
               throw new WrongTypeReloadException("invalid #" + code);
         }
         
      }
      
   /**
    * Processes an Answer, method to be called from the Forwarding & Link Management module.
    * @param message the Message structure
    */     
      public void processAns(Message message) throws Exception{ 
      
      
         short code = message.getMessageCode();
         
         if(code >= 0x8000 && code <= 0xfffe)
            throw new WrongTypeReloadException("reserved");
      
         switch(code){
            case 2:
            case 16:
            case 18:
            case 20:
            case 22:
               TopologyThread tThread = Module.tpi.getFakeTopologyThread();
               tThread.setMessageAndNotify(message);
               break;
            case 4:
            case 24:
            case 30:
            case 34:
               ForwardingThread fThread = Module.falm.getForwardingThread();
               synchronized(fThread){
                  fThread.setMessage(message);
                  fThread.notify();
               }
               break;
            case 6:
               throw new WrongTypeReloadException("unused");
            case 8:
            case 10:
            case 14:
            case 26:
               StorageThread sThread = Module.si.getStorageThread();
               synchronized(sThread){
                  sThread.setMessage(message);
                  sThread.notify();
               }
               break;
            case 12:
               throw new WrongTypeReloadException("unused (was remove_ans)");
            case 28:
               throw new WrongTypeReloadException("unused (was attachlite_req)");
            case 32:
               throw new WrongTypeReloadException("unused (was app_attachlite_req)");
            case 36:
               exp_a_ans(message);
            case 38:
               exp_b_ans(message);
            case (short)0xffff:
               error(message);
            default:
               throw new WrongTypeReloadException("invalid");
         
         }
         
      }
      
      private byte[] probe_req(Message message, NodeId peer) throws Exception{
               
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.tpi.probe_req(msg_body);      
      		
         short code = 2; // ProbeAns
      	
         NodeId[] via = message.getViaList();
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
   
      private byte[] attach_req(Message message, NodeId peer) throws Exception{
            
         NodeId ID = message.getSourceNodeId();
         
         byte[] msg_body = message.getMessageBody();
         
         byte[] ans_msg_body = Module.falm.attach_req(msg_body, ID);      
      		
         short code = 4; //AttachAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
              
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
         
      }
      
      private byte[] store_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
         
         short code = 0;
         byte[] ans_msg_body = null;
      
         try{
            ans_msg_body = Module.si.store_req(msg_body);  
            code = 8; // store_ans
         }
            catch(ErrorUnknownKind euk){
            
               String mes = euk.getMessage();
               
               String[] kinds = mes.split("-");
               KindId[] unknown = new KindId[kinds.length];
            
               for (int i=0; i<kinds.length; i++)
                  unknown[i] = new KindId(Integer.parseInt(kinds[i]));
               
               KindArray ka = new KindArray(unknown);
               
               ErrorResponse resp = new ErrorResponse((short)12, ka.getBytes()); // Error_Unknown_Kind
               
               ans_msg_body = resp.getBytes();      
               code = (short)0xffff; // Error
               
            }
            
            catch(ErrorForbidden ef){
               
               ErrorResponse resp = new ErrorResponse((short)2); // Error_Forbidden
               
               ans_msg_body = resp.getBytes();      
               code = (short)0xffff; // Error
               
            }
            
            catch(ErrorDataTooOld edto){
               
               ErrorResponse resp = new ErrorResponse((short)9); // Error_Data_Too_Old
               
               ans_msg_body = resp.getBytes();      
               code = (short)0xffff; // Error
               
            }
            
            catch(ErrorGenerationCounterTooLow egctl){
            
               String mes = egctl.getMessage();
                  
               ErrorResponse resp = new ErrorResponse((short)5, mes.getBytes()); // Error_Generation_Counter_Too_Low
               
               ans_msg_body = resp.getBytes();      
               code = (short)0xffff; // Error
               
            }
            
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
      
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
             
      }
      
      private byte[] fetch_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.si.fetch_req(msg_body);      
      		
         short code = 10; // FetchAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
         
      }
      
      private byte[] find_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = null;
         short code = 0;
         
         try{
            ans_msg_body = Module.si.find_req(msg_body);      
            code = 14; // FindAns
         }
            catch(ErrorNotFound enf){
               
               ErrorResponse resp = new ErrorResponse((short)3); // Error_Not_Found
               
               ans_msg_body = resp.getBytes();      
               code = (short)0xffff; // Error
               
            }
            
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] join_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.tpi.join_req(msg_body, message.getSourceNodeId());    
               		
         short code = 16; // JoinAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] leave_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.tpi.leave_req(msg_body, message.getSourceNodeId());      
      		
         short code = 18; // LeaveAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] update_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.tpi.update_req(msg_body, message.getSourceNodeId());      
      		
         short code = 20; // UpdateAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] route_query_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.tpi.route_query_req(msg_body);      
      		
         short code = 22; // RouteQueryAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] ping_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.falm.ping_req(msg_body);      
      		
         short code = 24; // PingAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] stat_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.si.stat_req(msg_body);      
      		
         short code = 26; // StatAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] app_attach_req(Message message, NodeId peer) throws Exception{
      
         NodeId ID = message.getSourceNodeId();
      
         byte[] msg_body = message.getMessageBody();
         
         short code = 0;
         
         byte[] ans_msg_body;
      
         try{
            ans_msg_body = Module.falm.app_attach_req(msg_body, ID);    
            code = 30; // AppAttachAns 
         }
            catch(ErrorForbidden ef){
               
               ErrorResponse resp = new ErrorResponse((short)2); // Error_Forbidden
               
               ans_msg_body = resp.getBytes();      
               code = (short)0xffff; // Error
               
            }
            
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
      		       
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] config_update_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = Module.falm.config_update_req(msg_body);      
      		
         short code = 34; // ConfigUpdateAns
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] exp_a_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = new byte[0];      
      		
         short code = 36; // Ans
      
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] exp_a_ans(Message message) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         return null;
               
      }
      
      private byte[] exp_b_req(Message message, NodeId peer) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         byte[] ans_msg_body = new byte[0];      
      		
         short code = 38; // Ans
         
         NodeId[] via = message.getViaList(); 
         NodeId[] dest = createRoute(via, peer);
               
         Message msg = new Message(ans_msg_body, dest, Module.tpi.routingTable.getThisNode(), code);
         return msg.getBytes();
               
      }
      
      private byte[] exp_b_ans(Message message) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
        
         return null;
               
      }
      
      private byte[] error(Message message) throws Exception{
      
         byte[] msg_body = message.getMessageBody();
      
         ErrorResponse resp = new ErrorResponse(msg_body); // Error_Unknown_Kind
         
         decodeError(resp.getErrorCode(), resp.getErrorInfo());
      
         throw new UnimplementedReloadException("Errors");
               
      }   
   
      public void decodeError(short error_code, byte[] error_info) throws Exception{ // set to private
      
      
         if(error_code >= 0x8000 && error_code <= 0xfffe)
            throw new WrongTypeReloadException("reserved");
      
         switch(error_code){
            case 1:
               throw new WrongTypeReloadException("Unused");
            case 2:
               error_Forbidden(error_info);
               break;
            case 3:
               error_Not_Found(error_info);
               break;
            case 4:
               error_Request_Timeout(error_info);
               break;
            case 5:
               error_Generation_Counter_Too_Low(error_info);
               break;
            case 6:
               error_Incompatible_with_Overlay(error_info);
               break;
            case 7:
               error_Unsupported_Forwarding_Option(error_info);
               break;
            case 8:
               error_Data_Too_Large(error_info);
               break;
            case 9:
               error_Data_Too_Old(error_info);
               break;
            case 10:
               error_TTL_Exceeded(error_info);
               break;
            case 11:
               error_Message_Too_Large(error_info);
               break;
            case 12:
               error_Unknown_Kind(error_info);
               break;
            case 13:
               error_Unknown_Extension(error_info);
               break;
            case 14:
               error_Response_Too_Large(error_info);
               break;
            case 15:
               error_Config_Too_Old(error_info);
               break;
            case 16:
               error_Config_Too_New(error_info);
               break;
            case 17:
               error_In_Progress(error_info);
               break;
            case 18:
               error_Exp_A(error_info);
               break;
            case 19:
               error_Exp_B(error_info);
               break;
            case 20:
               error_Invalid_Message(error_info);
               break;        
            default:
               throw new WrongTypeReloadException("invalid");
         
         }
         
      }
      
      public void error_Forbidden(byte[] error_info){ // set all to private
      
      // Gestionar error
      
      }
      
      public void error_Not_Found(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Request_Timeout(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Generation_Counter_Too_Low(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Incompatible_with_Overlay(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Unsupported_Forwarding_Option(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Data_Too_Large(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Data_Too_Old(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_TTL_Exceeded(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Message_Too_Large(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Unknown_Kind(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Unknown_Extension(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Response_Too_Large(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Config_Too_Old(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Config_Too_New(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_In_Progress(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Exp_A(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Exp_B(byte[] error_info){
      
      // Gestionar error
      
      }
      
      public void error_Invalid_Message(byte[] error_info){
      
      // Gestionar error
      
      }
   	
      private NodeId[] createRoute(NodeId[] via, NodeId peer){
      
         NodeId[] route = new NodeId[via.length+1];
         
         route[0] = peer;
      	
         for(int i=1, j=via.length-1; i<route.length; i++, j--)
            route[i] = via[j];
      
         return route;
      
      }
      
      public byte[] createStores(NodeId src) throws Exception{
      
         return null;
               
      }
   
   }