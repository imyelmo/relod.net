package reload.Message;

import java.util.*;
import java.io.*;
import reload.Common.*;
import reload.Common.Error.*;
import reload.Common.Exception.*;
import java.security.MessageDigest; 
import reload.Message.Forwarding.*;
import reload.Message.MessageCont.*;
import reload.Message.Security.*;
import reload.Message.Security.TLS.*;

public class Message {

/**
* Message class is a Message object in the Message Transport module.
* @author Malosa 
* @version 0.1
*/
   private MessageStructure message;
      
/**
 * Creates a new message.
 * @param message_body the message body field from the RELOAD message
 * @param dest destination ID (ResourceId array or NodeId array)
 * @param src the node signes the message
 * @param code the message code field from the RELOAD message
 * @param ttl time to life field from the RELOAD message
 */ 
   public Message(byte[] message_body, Id[] dest, NodeId src, short code, byte ttl) throws Exception{
   
   
      XML configuration = new XML();
   
      byte[] overl = SHA1_hash(configuration.getOverlay());
      int overlay = Utils.toInt(overl, 16); // 4 last bytes
      
      short configuration_sequence = configuration.getSequence();
      
      byte initial_ttl = configuration.getTTL();
      if(ttl == 0) // New message
         ttl = initial_ttl;
      if (ttl > initial_ttl)
         throw new ReloadError("Error_TTL_Exceeded"); 
   
   	
      int fragment = 0xC0000000; //No fragments
      int max_response_length = 0;
      
   	
      Destination destination_list[] = new Destination[dest.length];
   	
      for(int i=0; i<dest.length; i++){
         
         DestinationType type = dest[i].getType();
         DestinationData destination_data = new DestinationData(dest[i]);
         destination_list[i] = new Destination(type, destination_data);
      }
   	
      ForwardingHeader fh = new ForwardingHeader(overlay, configuration_sequence, ttl, fragment, max_response_length, destination_list);
      
   		
      MessageContents mc = new MessageContents(code, message_body);
      
      String[] cert = configuration.getRootCert();
      GenericCertificate[] gca = new GenericCertificate[cert.length];
   	
      for(int i=0; i<cert.length; i++)
         gca[i] = new GenericCertificate(CertificateType.X509, cert[i]);
   	
      SignerIdentity si = new SignerIdentity(SignerIdentityType.cert_hash, new SignerIdentityValue(HashAlgorithm.sha1, src.getBytes(), SignerIdentityType.cert_hash));
   	
      SignatureAndHashAlgorithm saha = new SignatureAndHashAlgorithm(HashAlgorithm.sha1, SignatureAlgorithm.rsa);
   		
      Signature s = new Signature(saha, si, new byte[0]);
   
      SecurityBlock sb = new SecurityBlock(gca, s);
      
      message = new MessageStructure(fh, mc, sb);
         
   }

/**
 * Creates a new message.
 * @param message_body the message body field from the RELOAD message
 * @param dest destination ID (ResourceId or NodeId)
 * @param src the node signes the message
 * @param code the message code field from the RELOAD message
 * @param ttl time to life field from the RELOAD message
 */    
   public Message(byte[] message_body, Id dest, NodeId src, short code, byte ttl) throws Exception{
   
      this(message_body, new Id[]{dest}, src, code, ttl);
   
   }

/**
 * Creates a new message.
 * @param message_body the message body field from the RELOAD message
 * @param dest destination ID (ResourceId array or NodeId array)
 * @param src the node signes the message
 * @param code the message code field from the RELOAD message
 */     
   public Message(byte[] message_body, Id[] dest, NodeId src, short code) throws Exception{ 
   
      this (message_body, dest, src, code, (byte)0);
   
   }

/**
 * Creates a new message.
 * @param message_body the message body field from the RELOAD message
 * @param dest destination ID (ResourceId or NodeId)
 * @param src the node signes the message
 * @param code the message code field from the RELOAD message
 */      
   public Message(byte[] message_body, Id dest, NodeId src, short code) throws Exception{
   
      this(message_body, new Id[]{dest}, src, code, (byte)0);
   
   }
   
   /*public Message(byte[] message_body, ResourceId dest, NodeId src, short code) throws Exception{ 
   
      this (message_body, dest, src, code, (byte)0);
   
   }*/

/**
 * Creates a new message (decodes the byte array).
 * @param data the byte array to decode
 */      
   public Message(byte[] data) throws Exception{
   
      message = new MessageStructure(data);
   
   }
   
   private byte[] SHA1_hash(byte[] text) throws Exception{
   
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      md.update(text);
      return md.digest();
   
   }

/**
 * Adds a node at the end of the via list.
 * @param node the node to be added
 */     
   public void addVia(NodeId node) throws Exception{
   
      Destination[] d = message.getForwardingHeader().getViaList();
   
      Destination[] newD = new Destination[d.length+1];
   	
      for(int i=0; i<d.length; i++)
         newD[i] = d[i];
         
   		
      DestinationType type = DestinationType.node;
      DestinationData destination_data = new DestinationData(node);
   
         
      newD[d.length] = new Destination(type, destination_data);
         
      message.getForwardingHeader().setViaList(newD);
   
   }

/**
 * Deletes the first ID from the destination list.
 */   
   public void deleteFirstId() throws Exception{
   
      Destination[] d = message.getForwardingHeader().getDestinationList();
      
      Destination[] newD = new Destination[d.length-1];
   	
      for(int i=0; i<newD.length; i++)
         newD[i] = d[i+1];
         
      message.getForwardingHeader().setDestinationList(newD);
   
   }

/**
 * Decrements TTL.
 * @return false if TTL is zero and cannot be decremented 
 */    
   public boolean decTTL(){
   
      byte ttl = message.getForwardingHeader().getTTL();
      
      if(ttl == 0)
         return false;
      
      message.getForwardingHeader().setTTL((byte)(ttl-1));
   	
      return true;
   
   }

/**
 * Resturns the whole message in a byte array.
 * @return the byte array
 */     
   public byte[] getBytes() throws Exception{
   
      return message.getBytes();
   
   }   

/**
 * Returns the destination list.
 * @return the destination list (ResourceId array or NodeId array) 
 */     
   public Id[] getDestinationId() throws ReloadException{ // Id is NodeId or ResourceId
      
      Destination[] d = message.getForwardingHeader().getDestinationList();
      Id[] id = new Id[d.length];
      
      for (int i=0; i<d.length; i++){
         if(d[i].getType() == DestinationType.node)
            id[i] = d[i].getDestinationData().getNodeId();
         else if (d[i].getType() == DestinationType.resource)
            id[i] = d[i].getDestinationData().getResourceId();
         else
            throw new UnimplementedReloadException("Opaque ID Type");
      }
      
      return id;
   
   }

/**
 * Returns the message body in a byte array.
 * @return the message body 
 */      
   public byte[] getMessageBody() throws IOException{
   
      return message.getMessageContents().getMessageBody();
   
   }

/**
 * Returns the message code.
 * @return the message code 
 */    
   public short getMessageCode(){
   
      return message.getMessageContents().getMessageCode();
   
   }

/**
 * Returns if message is an Answer.
 * @return true is message is an Answer 
 */    
   public boolean isAns(){
   
      return (message.getMessageContents().getMessageCode()%2) == 0;
   
   }

/**
 * Returns the TTL from RELOAD message.
 * @return the TTL 
 */ 	
   public byte getTTL(){
   
      return message.getForwardingHeader().getTTL();
   
   }

/**
 * Returns the source Node-ID (node that signed the message).
 * @return the source Node-ID 
 */    
   public NodeId getSourceNodeId() throws Exception{
   
      SignerIdentityType type = message.getSecurityBlock().getSignature().getIdentity().getType();
      
      if(type==SignerIdentityType.cert_hash || type==SignerIdentityType.cert_hash_node_id){
      
         byte[] certificate_node = message.getSecurityBlock().getSignature().getIdentity().getValue().getCertificateHash();
         return new NodeId(certificate_node, true); // Upper
      }
      
      else
         return null;
   
   }
   
   public MessageStructure getStructure(){
   
      return message;
   
   }

/**
 * Returns the via list.
 * @return the vis list 
 */    
   public NodeId[] getViaList() throws ReloadException{
   
   
      Destination[] destination = message.getForwardingHeader().getViaList();
      
      NodeId[] ret = new NodeId[destination.length];
      		
      for(int i=0; i<destination.length; i++){
      
         DestinationType type = destination[i].getType();
      
         if(type != DestinationType.node)
            throw new WrongTypeReloadException("Via list contains NodeId only.");
            
         DestinationData destination_data = destination[i].getDestinationData();
         
         ret[i] = destination_data.getNodeId();
         
      }
      
      return ret;
   
   }

}