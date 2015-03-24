package reload.Common;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import java.text.*;
import java.math.BigInteger;
import reload.Common.Exception.*;	
  
public class XML{

   private Document doc;
   private File file;
   public final String path = "reload/overlay.xml";
   
	
   private String xmlns;
   private String xmlns_ext;
   private String xmlns_chord;
   private String instance_name;
   private short sequence;
   private Date expiration;
   private String topology_plugin;
   private byte node_id_length = 16;
   private String[] root_cert; 
   private String[] enrollment_server;
   private String digest;
   private boolean self_signed_permitted;
   private IpAddressPort[] bootstrap_node;
   private int turn_density;
   private IpAddressPort[] multicast_bootstrap;
   private boolean clients_permitted;
   private boolean no_ice;
   private int chord_update_interval;
   private int chord_ping_interval;
   private boolean chord_reactive;
   private String shared_secret;
   private int max_message_size;
   private byte initial_ttl = 100;
   private int overlay_reliability_timer;
   private String overlay_link_protocol;
   private long configuration_signer;
   private long[] kind_signer;
   private long[] bad_node;
	


 
/**
 * Constructor que inicializa los atributos.
 */
   public XML() throws Exception{
      
   	
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      
      boolean error = false;
      
      try{
      
         file = new File(path);
      
         if(!file.exists()){
           
            doc = docBuilder.newDocument();
            create();
            dumpXML();
         		
         }
         
         else
            doc = docBuilder.parse(path);
               
         set();
      
      
      }
      catch(IndexOutOfBoundsException ioobe){
      
      } 
      catch(IOException ioe){
            
      }
      catch(NullPointerException npe){
         System.err.println("Wrong XML file.");
         error=true; 
      }
      catch(SAXParseException saxpe){
         System.err.println("Creating a new XML with default values.");
         doc = docBuilder.newDocument();
         create();
         dumpXML();
         set();
      }
      catch(ParserConfigurationException pce){
            
      }
   }
   
   private void create(){
            
      Element root = doc.createElement("overlay");
         	
      root.setAttribute("xmlns", "urn:ietf:params:xml:ns:p2p:config-base");
      root.setAttribute("xmlns:ext", "urn:ietf:params:xml:ns:p2p:config-ext1");
      root.setAttribute("xmlns:chord", "urn:ietf:params:xml:ns:p2p:config-chord");
            
      doc.appendChild(root);
            
      Element rootElem1 = doc.createElement("configuration");
         	
      rootElem1.setAttribute("instance-name", "overlay.example.org");
      rootElem1.setAttribute("sequence", "22");
      rootElem1.setAttribute("expiration", "2002-10-10T07:00:00Z");
      rootElem1.setAttribute("ext:ext-example", "stuff");
         
      root.appendChild(rootElem1);
            
      Element rootElemChild1 = doc.createElement("topology-plugin");
      Element rootElemChild2 = doc.createElement("node-id-length");
      Element rootElemChild3 = doc.createElement("root-cert");
      Element rootElemChild4 = doc.createElement("root-cert");
      Element rootElemChild5 = doc.createElement("enrollment-server");
      Element rootElemChild6 = doc.createElement("enrollment-server");
         	
         	
         	
      Text text1 = doc.createTextNode(" CHORD-RELOAD ");
      rootElemChild1.appendChild(text1);
            
      Text text2 = doc.createTextNode("16");
      rootElemChild2.appendChild(text2);
            
      Text text3 = doc.createTextNode("MIIDJDCCAo2gAwIBAgIBADANBgkqhkiG9w0BAQUFADBwMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTERMA8GA1UEBxMIU2FuIEpvc2UxDjAMBgNVBAoTBXNpcGl0MSkwJwYDVQQLEyBTaXBpdCBUZXN0IENlcnRpZmljYXRlIEF1dGhvcml0eTAeFw0wMzA3MTgxMjIxNTJaFw0xMzA3MTUxMjIxNTJaMHAxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMREwDwYDVQQHEwhTYW4gSm9zZTEOMAwGA1UEChMFc2lwaXQxKTAnBgNVBAsTIFNpcGl0IFRlc3QgQ2VydGlmaWNhdGUgQXV0aG9yaXR5MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDIh6DkcUDLDyK9BEUxkud+nJ4xrCVGKfgjHm6XaSuHiEtnfELHM+9WymzkBNzZpJu30yzsxwfKoIKugdNUrD4N3viCicwcN35LgP/KnbN34cavXHr4ZlqxH+OdKB3hQTpQa38A7YXdaoz6goW2ft5Mi74z03GNKP/G9BoKOGd5QIDAQABo4HNMIHKMB0GA1UdDgQWBBRrRhcU6pR2JYBUbhNU2qHjVBShtjCBmgYDVR0jBIGSMIGPgBRrRhcU6pR2JYBUbhNU2qHjVBShtqF0pHIwcDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExETAPBgNVBAcTCFNhbiBKb3NlMQ4wDAYDVQQKEwVzaXBpdDEpMCcGA1UECxMgU2lwaXQgVGVzdCBDZXJ0aWZpY2F0ZSBBdXRob3JpdHmCAQAwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQCWbRvv1ZGTRXxbH8/EqkdSCzSoUPrs+rQqR0xdQac9wNY/nlZbkR3OqAezG6Sfmklvf+DOg5RxQq/+Y6I03LRepc7KeVDpaplMFGnpfKsibETMipwzayNQQgUf4cKBiF+65Ue7hZuDJa2EMv8qW4twEhGDYclpFU9YozyS1OhvUg==");
      rootElemChild3.appendChild(text3);
            
      Text text4 = doc.createTextNode("YmFkIGNlcnQK");
      rootElemChild4.appendChild(text4);
            
      Text text5 = doc.createTextNode("https://example.org");
      rootElemChild5.appendChild(text5);
            
      Text text6 = doc.createTextNode("https://example.net");
      rootElemChild6.appendChild(text6);
         
         	
      rootElem1.appendChild(rootElemChild1);
      rootElem1.appendChild(rootElemChild2);
      rootElem1.appendChild(rootElemChild3);
      rootElem1.appendChild(rootElemChild4);
      rootElem1.appendChild(rootElemChild5);
      rootElem1.appendChild(rootElemChild6);
         	
      Element rootElemChild7 = doc.createElement("self-signed-permitted");
      rootElemChild7.setAttribute("digest", "sha1");
   	
      Text text7 = doc.createTextNode("false");
      rootElemChild7.appendChild(text7);
         	
      rootElem1.appendChild(rootElemChild7);
            
      Element rootElemChild8 = doc.createElement("bootstrap-node");
      Element rootElemChild9 = doc.createElement("bootstrap-node");
      Element rootElemChild10 = doc.createElement("bootstrap-node");
      Element rootElemChild11 = doc.createElement("turn-density");
      Element rootElemChild12 = doc.createElement("multicast-bootstrap");
      Element rootElemChild13 = doc.createElement("multicast-bootstrap");
         	
      rootElemChild8.setAttribute("address", "192.0.0.1");
      rootElemChild8.setAttribute("port", "6084");
         
      rootElemChild9.setAttribute("address", "192.0.2.2");
      rootElemChild9.setAttribute("port", "6084");
         	
      rootElemChild10.setAttribute("address", "2001:DB8::1");
      rootElemChild10.setAttribute("port", "6084");
            
      rootElem1.appendChild(rootElemChild8);
      rootElem1.appendChild(rootElemChild9);
      rootElem1.appendChild(rootElemChild10);
            
      Text text11 = doc.createTextNode(" 20 ");
      rootElemChild11.appendChild(text11);
            
      rootElem1.appendChild(rootElemChild11);
            
      rootElemChild12.setAttribute("address", "192.0.0.1");
         
      rootElemChild13.setAttribute("address", "233.252.0.1");
      rootElemChild13.setAttribute("port", "6084");
            
      rootElem1.appendChild(rootElemChild12);
      rootElem1.appendChild(rootElemChild13);
            
         	
      Element rootElemChild14 = doc.createElement("clients-permitted");
      Element rootElemChild15 = doc.createElement("no-ice");
      Element rootElemChild16 = doc.createElement("chord:chord-update-interval");
      Element rootElemChild17 = doc.createElement("chord:chord-ping-interval");
      Element rootElemChild18 = doc.createElement("chord:chord-reactive");
      Element rootElemChild19 = doc.createElement("shared-secret");
      Element rootElemChild20 = doc.createElement("max-message-size");
      Element rootElemChild21 = doc.createElement("initial-ttl");
      Element rootElemChild22 = doc.createElement("overlay-reliability-timer");
      Element rootElemChild23 = doc.createElement("overlay-link-protocol");
      Element rootElemChild24 = doc.createElement("configuration-signer");
      Element rootElemChild25 = doc.createElement("kind-signer");
      Element rootElemChild26 = doc.createElement("kind-signer");
      Element rootElemChild27 = doc.createElement("bad-node");
      Element rootElemChild28 = doc.createElement("bad-node");
      Element rootElemChild29 = doc.createElement("ext:example-extension");
      Element rootElemChild30 = doc.createElement("mandatory-extension");
            
      rootElemChild14.appendChild(doc.createTextNode(" false "));
      rootElemChild15.appendChild(doc.createTextNode(" false "));
      rootElemChild16.appendChild(doc.createTextNode("400"));
      rootElemChild17.appendChild(doc.createTextNode("30"));
      rootElemChild18.appendChild(doc.createTextNode(" true "));
      rootElemChild19.appendChild(doc.createTextNode(" password "));
      rootElemChild20.appendChild(doc.createTextNode("4000"));
      rootElemChild21.appendChild(doc.createTextNode(" 30 "));
      rootElemChild22.appendChild(doc.createTextNode(" 3000 "));
      rootElemChild23.appendChild(doc.createTextNode("TLS"));
      rootElemChild24.appendChild(doc.createTextNode("47112162e84c69ba"));
      rootElemChild25.appendChild(doc.createTextNode(" 47112162e84c69ba "));
      rootElemChild26.appendChild(doc.createTextNode(" 6eba45d31a900c06 "));
      rootElemChild27.appendChild(doc.createTextNode(" 6ebc45d31a900c06 "));
      rootElemChild28.appendChild(doc.createTextNode(" 6ebc45d31a900ca6 "));
      rootElemChild29.appendChild(doc.createTextNode(" foo "));
      rootElemChild30.appendChild(doc.createTextNode("urn:ietf:params:xml:ns:p2p:config-ext1"));
         	
      rootElem1.appendChild(rootElemChild14);
      rootElem1.appendChild(rootElemChild15);
      rootElem1.appendChild(rootElemChild16);
      rootElem1.appendChild(rootElemChild17);
      rootElem1.appendChild(rootElemChild18);
      rootElem1.appendChild(rootElemChild19);
      rootElem1.appendChild(rootElemChild20);
      rootElem1.appendChild(rootElemChild21);
      rootElem1.appendChild(rootElemChild22);
      rootElem1.appendChild(rootElemChild23);
      rootElem1.appendChild(rootElemChild24);
      rootElem1.appendChild(rootElemChild25);
      rootElem1.appendChild(rootElemChild26);
      rootElem1.appendChild(rootElemChild27);
      rootElem1.appendChild(rootElemChild28);
      rootElem1.appendChild(rootElemChild29);
      rootElem1.appendChild(rootElemChild30);
            
         	
      Element rootElemChild31 = doc.createElement("required-kinds");
            
      rootElem1.appendChild(rootElemChild31);
         	
      Element rootElemChild31o1 = doc.createElement("kind-block");
      rootElemChild31.appendChild(rootElemChild31o1);
           
      Element rootElemChild31o1o1 = doc.createElement("kind");
      Element rootElemChild31o1o2 = doc.createElement("kind-signature");
            
      rootElemChild31o1o1.setAttribute("name", "SIP-REGISTRATION");
      rootElemChild31o1o2.appendChild(doc.createTextNode("VGhpcyBpcyBub3QgcmlnaHQhCg=="));
         	
      rootElemChild31o1.appendChild(rootElemChild31o1o1);
      rootElemChild31o1.appendChild(rootElemChild31o1o2);
            
      Element rootElemChild31o1o1o1 = doc.createElement("data-model");
      Element rootElemChild31o1o1o2 = doc.createElement("access-control");
      Element rootElemChild31o1o1o3 = doc.createElement("max-count");
      Element rootElemChild31o1o1o4 = doc.createElement("max-size");
            
      rootElemChild31o1o1o1.appendChild(doc.createTextNode("SINGLE"));
      rootElemChild31o1o1o2.appendChild(doc.createTextNode("USER-MATCH"));
      rootElemChild31o1o1o3.appendChild(doc.createTextNode("1"));
      rootElemChild31o1o1o4.appendChild(doc.createTextNode("100"));
            
      rootElemChild31o1o1.appendChild(rootElemChild31o1o1o1);
      rootElemChild31o1o1.appendChild(rootElemChild31o1o1o2);
      rootElemChild31o1o1.appendChild(rootElemChild31o1o1o3);
      rootElemChild31o1o1.appendChild(rootElemChild31o1o1o4);
         
         
      Element rootElemChild31o2 = doc.createElement("kind-block");
      rootElemChild31.appendChild(rootElemChild31o2);
            
      Element rootElemChild31o2o1 = doc.createElement("kind");
      Element rootElemChild31o2o2 = doc.createElement("kind-signature");
            
      rootElemChild31o2o1.setAttribute("id", "2000");
      rootElemChild31o2o2.appendChild(doc.createTextNode("VGhpcyBpcyBub3QgcmlnaHQhCg=="));
         	
      rootElemChild31o2.appendChild(rootElemChild31o1o1);
      rootElemChild31o2.appendChild(rootElemChild31o1o2);
            
      Element rootElemChild31o2o1o1 = doc.createElement("data-model");
      Element rootElemChild31o2o1o2 = doc.createElement("access-control");
      Element rootElemChild31o2o1o3 = doc.createElement("max-node-multiple");
      Element rootElemChild31o2o1o4 = doc.createElement("max-count");
      Element rootElemChild31o2o1o5 = doc.createElement("max-size");
      Element rootElemChild31o2o1o6 = doc.createElement("ext:example-kind-extension");
            
      rootElemChild31o2o1o1.appendChild(doc.createTextNode("ARRAY"));
      rootElemChild31o2o1o2.appendChild(doc.createTextNode("NODE-MULTIPLE"));
      rootElemChild31o2o1o3.appendChild(doc.createTextNode("3"));
      rootElemChild31o2o1o4.appendChild(doc.createTextNode("22"));
      rootElemChild31o2o1o5.appendChild(doc.createTextNode("4"));
      rootElemChild31o2o1o6.appendChild(doc.createTextNode(" 1 "));
            
      rootElemChild31o1o1.appendChild(rootElemChild31o2o1o1);
      rootElemChild31o1o1.appendChild(rootElemChild31o2o1o2);
      rootElemChild31o1o1.appendChild(rootElemChild31o2o1o3);
      rootElemChild31o1o1.appendChild(rootElemChild31o2o1o4);
      rootElemChild31o1o1.appendChild(rootElemChild31o2o1o5);
      rootElemChild31o1o1.appendChild(rootElemChild31o2o1o6);
            
         	
      Element rootElem2 = doc.createElement("signature");
      rootElem2.appendChild(doc.createTextNode(" VGhpcyBpcyBub3QgcmlnaHQhCg== "));
      root.appendChild(rootElem2);
            
      Element rootElem3 = doc.createElement("configuration");
      rootElem3.setAttribute("instance-name", "other.example.net");
      root.appendChild(rootElem3);
            
      Element rootElem4 = doc.createElement("signature");
      rootElem4.appendChild(doc.createTextNode(" VGhpcyBpcyBub3QgcmlnaHQhCg== "));
      root.appendChild(rootElem4);
      
   }
   
   private void set() throws Exception{
   
      Element root = doc.getDocumentElement();
         
      Attr Axmlns = root.getAttributeNode("xmlns");
      xmlns = Axmlns.getValue();
         
      Attr ext = root.getAttributeNode("xmlns:ext");
      xmlns_ext = ext.getValue();
         
      Attr chord = root.getAttributeNode("xmlns:chord");
      xmlns_chord = chord.getValue();
         
      	
      NodeList configurationL = root.getElementsByTagName("configuration");
      Element configuration = (Element)configurationL.item(0);
         
      Attr name = configuration.getAttributeNode("instance-name");   
      instance_name = name.getValue();
         
      Attr seq = configuration.getAttributeNode("sequence");
      String Ssequence = seq.getValue();
      sequence = Short.parseShort(Ssequence);
         
      Attr exp = configuration.getAttributeNode("expiration");
      String Sexpiration = exp.getValue();
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      expiration = formatter.parse(Sexpiration);
         
      NodeList pluginL = configuration.getElementsByTagName("topology-plugin");
      Element plugin = (Element)pluginL.item(0);
      topology_plugin = plugin.getTextContent().trim();
      	
      NodeList nidlL = configuration.getElementsByTagName("node-id-length");
      Element nidl = (Element)nidlL.item(0);
      String node_id = nidl.getTextContent().trim();
      node_id_length = Byte.parseByte(node_id);
         
      NodeList cert = configuration.getElementsByTagName("root-cert");
      root_cert = new String[cert.getLength()];
      for(int i=0; i<cert.getLength(); i++){
         Element rcert = (Element)cert.item(i);
         root_cert[i] = rcert.getTextContent().trim();
      }
      
      NodeList enrollment = configuration.getElementsByTagName("enrollment-server");
      enrollment_server = new String[enrollment.getLength()];
      for(int i=0; i<enrollment.getLength(); i++){
         Element es = (Element)enrollment.item(i);
         enrollment_server[i] = es.getTextContent().trim();
      }
      
      NodeList self_signedL = configuration.getElementsByTagName("self-signed-permitted");
      Element self_signed = (Element)self_signedL.item(0);
      String Sself_signed = self_signed.getTextContent().trim();
      self_signed_permitted = Boolean.valueOf(Sself_signed);
      Attr dig = self_signed.getAttributeNode("digest");
      digest = dig.getValue();
         
      NodeList bootstrap = configuration.getElementsByTagName("bootstrap-node");
      bootstrap_node = new IpAddressPort[bootstrap.getLength()];
      for(int i=0; i<bootstrap.getLength(); i++){
         Element bn = (Element)bootstrap.item(i);
         Attr address = bn.getAttributeNode("address");
         Attr port = bn.getAttributeNode("port");
         String ad = address.getValue();
         int por;
         try{
            por = Integer.parseInt(port.getValue());
         }
         catch (NullPointerException npe){
            por = 6084;
         }
         bootstrap_node[i] = new IpAddressPort(ad, por);
      }
         
      NodeList densityL = configuration.getElementsByTagName("turn-density");
      Element density = (Element)densityL.item(0);
      String Sdensity = density.getTextContent().trim();
      turn_density = Integer.parseInt(Sdensity);
         
      NodeList MbootstrapL = configuration.getElementsByTagName("multicast-bootstrap");
      multicast_bootstrap = new IpAddressPort[MbootstrapL.getLength()];
      for(int i=0; i<MbootstrapL.getLength(); i++){
         Element Mbootstrap = (Element)MbootstrapL.item(i);
         Attr address = Mbootstrap.getAttributeNode("address");
         Attr port = Mbootstrap.getAttributeNode("port");
         String ad = address.getValue();
         int por;  
         try{
            por = Integer.parseInt(port.getValue());
         }
         catch (NullPointerException npe){
            por = 6084;
         }
         multicast_bootstrap[i] = new IpAddressPort(ad, por);
      }
      	
      NodeList clientsL = configuration.getElementsByTagName("clients-permitted");
      Element clients = (Element)clientsL.item(0);
      String Sclients = clients.getTextContent().trim();
      clients_permitted = Boolean.valueOf(Sclients);
      
      NodeList iceL = configuration.getElementsByTagName("no-ice");
      Element ice = (Element)iceL.item(0);
      String Sice = ice.getTextContent().trim();
      no_ice = Boolean.valueOf(Sice);
         
      NodeList cuiL = configuration.getElementsByTagName("chord:chord-update-interval");
      Element cui = (Element)cuiL.item(0);
      String Scui = cui.getTextContent().trim();
      chord_update_interval = Integer.parseInt(Scui);
         
      NodeList cpiL = configuration.getElementsByTagName("chord:chord-ping-interval");
      Element cpi = (Element)cpiL.item(0);
      String Scpi = cpi.getTextContent().trim();
      chord_ping_interval = Integer.parseInt(Scpi);
      	
      NodeList reactiveL = configuration.getElementsByTagName("chord:chord-reactive");
      Element reactive = (Element)reactiveL.item(0);
      String Sreactive = reactive.getTextContent().trim();
      chord_reactive = Boolean.valueOf(Sreactive);
         
      NodeList ssL = configuration.getElementsByTagName("shared-secret");
      Element ss = (Element)ssL.item(0);
      shared_secret = ss.getTextContent().trim();
         
      NodeList mmsL = configuration.getElementsByTagName("max-message-size");
      Element mms = (Element)mmsL.item(0);
      String Smms = mms.getTextContent().trim();
      chord_ping_interval = Integer.parseInt(Smms);
         
      NodeList ttlL = configuration.getElementsByTagName("initial-ttl");
      Element ttl = (Element)ttlL.item(0);
      String Sttl = ttl.getTextContent().trim();
      initial_ttl = Byte.parseByte(Sttl);
         
      NodeList ortL = configuration.getElementsByTagName("overlay-reliability-timer");
      Element ort = (Element)ortL.item(0);
      String Sort = ort.getTextContent().trim();
      overlay_reliability_timer = Integer.parseInt(Sort);
         
      NodeList olpL = configuration.getElementsByTagName("overlay-link-protocol");
      Element olp = (Element)olpL.item(0);
      overlay_link_protocol = olp.getTextContent().trim();
         
      NodeList csL = configuration.getElementsByTagName("configuration-signer");
      Element cs = (Element)csL.item(0);
      String Scs = cs.getTextContent().trim();
      configuration_signer = new BigInteger(Scs, 16).longValue();
      	
      NodeList kind = configuration.getElementsByTagName("kind-signer");
      kind_signer = new long[kind.getLength()];
      for(int i=0; i<kind.getLength(); i++){
         Element ks = (Element)kind.item(i);
         String Sks = ks.getTextContent().trim();
         kind_signer[i] = new BigInteger(Sks, 16).longValue();
      }
         
      NodeList bad = configuration.getElementsByTagName("bad-node");
      bad_node = new long[bad.getLength()];
      for(int i=0; i<bad.getLength(); i++){
         Element bn = (Element)bad.item(i);
         String Sbn = bn.getTextContent().trim();
         bad_node[i] = new BigInteger(Sbn, 16).longValue();
      }
   
   }
   
   public byte[] getOverlay(){
            
      return instance_name.getBytes();
   
   }
   
   public short getSequence(){
   	
      return sequence;
   
   }
   
   public byte getTTL(){
      
      return initial_ttl;
   
   }
   
   public byte getNodeIDLength(){
      
      return node_id_length;
   
   }
   
   public String getOverlayLinkProtocol(){
      
      return overlay_link_protocol;
   
   }

/**
 * Escribe el archivo XML en el disco.
 */     	
   private void dumpXML() throws Exception {
   
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
   
      Transformer transformer = transformerFactory.newTransformer();
   
      DOMSource source = new DOMSource(doc);
   
      StreamResult result = new StreamResult(file);
      
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(source, result);
   
   }
   
   public IpAddressPort[] getBootstrapNode(){
   
      return bootstrap_node;
   
   }
   
   public String getDigest() throws ReloadException{
   
      if (digest.equalsIgnoreCase("sha1") || digest.equalsIgnoreCase("sha-1"))
         return "SHA-1";
         
      else if (digest.equalsIgnoreCase("sha256") || digest.equalsIgnoreCase("sha-256"))
         return "SHA-256";
      
      else
         throw new WrongTypeReloadException("Only SHA-1 and SHA-256 digest algorithms are supported.");
      
   }
   
   public String[] getRootCert(){
   
      return root_cert;
   
   }
   	
}