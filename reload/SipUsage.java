
   package reload;

   import reload.SIP.*;
   import reload.Message.Forwarding.*;	
   import reload.Common.*;	
   import reload.Common.Exception.*;	
   
   import java.util.*;

/**
* SipUsage class is a SIP Usage for the RELOAD protocol.
* @author Malosa 
* @version 0.1
*/	 
   public class SipUsage{
   
      private int port;
      private boolean first;
      private DataModel dictionary;
      private ReloadInterface iface;
      private KindId kind; 
      private NodeId thisNode;
   
   /**
    * Establishes the usage and the RELOAD protocol.
    * @param port the port used by RELOAD
    * @param first if this is the first node to initialize in the overlay
    */     
      public SipUsage(int port, boolean first) throws Exception{
        
         this.port = port;
         this.first = first;
         dictionary = DataModel.dictionary;
         iface = new ReloadInterface(port, first, dictionary);
         kind = new KindId(1);
         
         iface.addKindModel(kind, "SIP-REGISTRATION");
      
      }
   
   /**
    * Initializes the RELOAD protocol with the SIP Usage. It is called after the constructor.
    * @return true if overlay could start
    */    
      public boolean startOverlay() throws Exception{
               
         if(!iface.start())
            return false;
           
         thisNode = Module.tpi.routingTable.getThisNode();
         
         return true;
      
      } 
   
   /**
    * Maps your own Address Of Record (AOR) with your current Node-ID.
    * @param aor the URI of the AOR
    */       
      public void setUri(String aor) throws Exception{
      
         setUri(aor, thisNode);
      
      }
   
   /**
    * Maps an Address Of Record (AOR) with a specified Node-ID.
    * @param aor the URI of the AOR
    */ 
      public void setUri(String aor, NodeId dest) throws Exception{
      
         setUri(aor, dest, dest);
      
      }
      
      public void setUri(String aor, NodeId key, NodeId value) throws Exception{
      
         Destination[] dest = new Destination[1];
         dest[0] = new Destination(DestinationType.node, new DestinationData(value));
      
         String contact_prefs = "(& (sip.audio=TRUE)\n(sip.video=TRUE)\n(sip.actor=msg-taker)\n(sip.automata=TRUE)\nsip.mobility=fixed)\n";
         contact_prefs.concat("| (sip.methods=INVITE) (sip.methods=BYE) (sip.methods=OPTIONS) (sip.methods=ACK) (sip.methods=CANCEL) ))");
      
         SipRegistrationData srd = new SipRegistrationData(contact_prefs, dest);
         
         SipRegistration reg = new SipRegistration(SipRegistrationType.sip_registration_route, srd);
         
         store(aor, reg, key);
                  
      }
      
   /**
    * Maps an Address Of Record (AOR) into another AOR.
    * @param aor the AOR to be redirected from
    * @param myAor the AOR that will be redirected to
    */    
      public void setUriRedirection(String aor, String myAor) throws Exception{
      
         SipRegistrationData srd = new SipRegistrationData(myAor);
         
         SipRegistration reg = new SipRegistration(SipRegistrationType.sip_registration_uri, srd);
         
         store(aor, reg, thisNode);
      
      }
      
      private void store(String aor, SipRegistration reg, NodeId key) throws Exception{
      
         ResourceId resource_aor = Module.tpi.hash(aor);
         
         System.out.println("My resource " + aor + " is stored at Resource-ID: " + resource_aor.print());
         
         Opaque[] dictionaryKey = {new Opaque(16, key.getId())};
         
         Opaque[] data = {new Opaque(32, reg.getBytes())};
      
         iface.storeDictionary(resource_aor, kind, data, dictionaryKey);
      
      }
      
      private Destination fetch(String aor) throws Exception{
      
         ResourceId resource_aor = Module.tpi.hash(aor);
         
         Opaque[] dictionaryKey = new Opaque[0];
      
         DataStructure[] ds = iface.fetchDictionary(resource_aor, kind, dictionaryKey);
         
         System.out.println("Fetch received. Content:");
         
         if(ds.length == 0)
            System.out.println("<Empty>");
      
         SipRegistration[] sr = new SipRegistration[ds.length];
         
         Destination ret = null;
         
         for(int i=0; i<ds.length; i++){
         
            if(ds[i].getExists()){
            
               sr[i] = new SipRegistration(ds[i].getValue().getContent());
               byte[] key = ds[i].getDictionaryKey().getKey();
            
               SipRegistrationData srd = sr[i].getData();
               SipRegistrationType type = sr[i].getType();
            
               if(type == SipRegistrationType.sip_registration_uri){

                  String uri = srd.getUri();
                  System.out.println("URI: " + uri);
                  
                  ret = fetch(uri);
               }
               
               if(type == SipRegistrationType.sip_registration_route){
               
                  Destination[] destination = srd.getDestinationList();
               
                  for(int j=0; j<destination.length; j++){
                  
                     Destination dest = destination[j];
                     NodeId nodeKey = new NodeId(key, true);
                  
                     if(nodeKey.equals(dest.getNodeId()))
                        System.out.println("Route Node-ID: " + dest.getNodeId().print());
                     
                     else
                        System.out.println("Route Node-ID: " + nodeKey.print() + " -> " + dest.getNodeId().print());
                        
                     ret = dest;
                  
                  }
               }
            
            }
            
            else
               System.out.println("<Deleted value>");
               
         }
         
         return ret;
            
      }
      
      private void remove(String aor, NodeId key) throws Exception{
      
         ResourceId resource_aor = Module.tpi.hash(aor);
         
         Opaque[] dictionaryKey = {new Opaque(16, key.getId())};
      
         iface.removeDictionary(resource_aor, kind, dictionaryKey);
      
      }
   
      private void sipAttach(String aor) throws Exception{
      
         Destination dest = fetch(aor);
         
         if(dest != null){
         
            NodeId node = dest.getNodeId();
         
            short app = 5060;
         
            iface.appAttach(node, app);
         }
         
         else
            System.out.println("Could not do App Attach.");
      
      }
   
   /**
    * Initializes the console. This is the last method called.
    */      
      public void startConsole() throws Exception{
      
         boolean exit = false;
      
         while (!exit){

         	System.out.println("Use \"help\" to show the diffente commands");
            System.out.print(">");
         
            Scanner scanner = new Scanner(System.in);
            String read = scanner.nextLine();
            
            if(read.startsWith("fetch ")){
               String aor = read.substring(6);
               fetch(aor);
            }
            
            else if(read.startsWith("remove ")){
               String aor = read.substring(7);
               remove(aor, thisNode);
            }
            
            else if(read.startsWith("sip ")){
               String aor = read.substring(4);
               sipAttach(aor);
            }
            
            else
               if(iface.commonCommand(read)) // If true, exit application
                  exit = true;
         
         }
         
         System.out.println("SIP Exiting...");
      	
      }
      
   }