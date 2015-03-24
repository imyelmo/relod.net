   package reload.dev2dev.textclient;

   import java.net.InetAddress;
   import java.text.ParseException;
   import java.util.ArrayList;
   import java.util.Properties;
   import java.util.TooManyListenersException;

   import javax.sip.DialogTerminatedEvent;
   import javax.sip.IOExceptionEvent;
   import javax.sip.InvalidArgumentException;
   import javax.sip.ListeningPoint;
   import javax.sip.ObjectInUseException;
   import javax.sip.PeerUnavailableException;
   import javax.sip.RequestEvent;
   import javax.sip.ResponseEvent;
   import javax.sip.ServerTransaction;
   import javax.sip.SipException;
   import javax.sip.SipFactory;
   import javax.sip.SipListener;
   import javax.sip.SipProvider;
   import javax.sip.SipStack;
   import javax.sip.TimeoutEvent;
   import javax.sip.TransactionTerminatedEvent;
   import javax.sip.TransportNotSupportedException;
   import javax.sip.address.Address;
   import javax.sip.address.AddressFactory;
   import javax.sip.address.SipURI;
   import javax.sip.header.CSeqHeader;
   import javax.sip.header.CallIdHeader;
   import javax.sip.header.ContactHeader;
   import javax.sip.header.ContentTypeHeader;
   import javax.sip.header.FromHeader;
   import javax.sip.header.HeaderFactory;
   import javax.sip.header.MaxForwardsHeader;
   import javax.sip.header.ToHeader;
   import javax.sip.header.ViaHeader;
   import javax.sip.message.MessageFactory;
   import javax.sip.message.Request;
   import javax.sip.message.Response;

   public class SipLayer implements SipListener {
   
      private MessageProcessor messageProcessor;
   
      private String username;
   
      private SipStack sipStack;
   
      private SipFactory sipFactory;
   
      private AddressFactory addressFactory;
   
      private HeaderFactory headerFactory;
   
      private MessageFactory messageFactory;
   
      private SipProvider sipProvider;
   	
      private String name;
      
      //private ListeningPoint tcp;
      private ListeningPoint udp;
   
    /** Here we initialize the SIP stack. */
      public SipLayer(String ip, int port, String name)
       throws PeerUnavailableException, TransportNotSupportedException,
       InvalidArgumentException, ObjectInUseException,
       TooManyListenersException {
       
       this.name = name;
         setUsername(username);
         sipFactory = SipFactory.getInstance();
         sipFactory.setPathName("gov.nist");
         Properties properties = new Properties();
         properties.setProperty("javax.sip.STACK_NAME", "TextClient");
         properties.setProperty("javax.sip.IP_ADDRESS", ip);
      
      //DEBUGGING: Information will go to files 
      //textclient.log and textclientdebug.log
        /* properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
         properties.setProperty("gov.nist.javax.sip.SERVER_LOG",
            "textclient.txt");
         properties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
            "textclientdebug.log");*/
      
         sipStack = sipFactory.createSipStack(properties);
         headerFactory = sipFactory.createHeaderFactory();
         addressFactory = sipFactory.createAddressFactory();
         messageFactory = sipFactory.createMessageFactory();
      
         //tcp = sipStack.createListeningPoint(ip, port, "tcp");
         udp = sipStack.createListeningPoint(ip, port, "udp");
      
         //sipProvider = sipStack.createSipProvider(tcp);
         //sipProvider.addSipListener(this);
         sipProvider = sipStack.createSipProvider(udp);
         sipProvider.addSipListener(this);
      }
   
    /**
     * This method uses the SIP stack to send a message. 
     */
      public void sendMessage(String to, String message) throws ParseException,
       InvalidArgumentException, SipException {
      
         SipURI from = addressFactory.createSipURI(null, name+ ":" + getPort());
         Address fromNameAddress = addressFactory.createAddress(from);
         fromNameAddress.setDisplayName(getUsername());
         FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
            "textclientv1.0");
      
         String username = to.substring(to.indexOf(":") + 1, to.indexOf("@"));
         String address = to.substring(to.indexOf("@") + 1);
      
         SipURI toAddress = addressFactory.createSipURI(username, address);
         Address toNameAddress = addressFactory.createAddress(toAddress);
         toNameAddress.setDisplayName(username);
         ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);
      
         SipURI requestURI = addressFactory.createSipURI(username, address);
         requestURI.setTransportParam("udp");
      
         ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
         ViaHeader viaHeader = headerFactory.createViaHeader(getHost(),
            getPort(), "udp", "branch1");
         viaHeaders.add(viaHeader);
      
         CallIdHeader callIdHeader = sipProvider.getNewCallId();
      
         CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L,
            Request.MESSAGE);
      
         MaxForwardsHeader maxForwards = headerFactory
            .createMaxForwardsHeader(70);
      
         Request request = messageFactory.createRequest(requestURI,
            Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
            toHeader, viaHeaders, maxForwards);
      
         SipURI contactURI = addressFactory.createSipURI(getUsername(),
            getHost());
         contactURI.setPort(getPort());
         Address contactAddress = addressFactory.createAddress(contactURI);
         contactAddress.setDisplayName(getUsername());
         ContactHeader contactHeader = headerFactory
            .createContactHeader(contactAddress);
         request.addHeader(contactHeader);
      
         ContentTypeHeader contentTypeHeader = headerFactory
            .createContentTypeHeader("text", "plain");
         request.setContent(message, contentTypeHeader);
      
         sipProvider.sendRequest(request);
      }
   
    /** This method is called by the SIP stack when a response arrives. */
      public void processResponse(ResponseEvent evt) {
         Response response = evt.getResponse();
         int status = response.getStatusCode();
      
         if ((status >= 200) && (status < 300)) { //Success!
            messageProcessor.processInfo("--Sent");
            return;
         }
      
         messageProcessor.processError("Previous message not sent: " + status);
      }
   
    /** 
     * This method is called by the SIP stack when a new request arrives. 
     */
      public void processRequest(RequestEvent evt) {
         Request req = evt.getRequest();
      
         String method = req.getMethod();
         if (!method.equals("MESSAGE")) { //bad request type.
            messageProcessor.processError("Bad request type: " + method);
            return;
         }
      
         FromHeader from = (FromHeader) req.getHeader("From");
         messageProcessor.processMessage(from.getAddress().toString(), //Aquí está la clave de todo
            new String(req.getRawContent()));
         Response response = null;
         try { //Reply with OK
            response = messageFactory.createResponse(200, req);
            ToHeader toHeader = (ToHeader) response.getHeader(ToHeader.NAME);
            toHeader.setTag("888"); //This is mandatory as per the spec.
            ServerTransaction st = sipProvider.getNewServerTransaction(req);
            st.sendResponse(response);
         } 
            catch (Throwable e) {
               e.printStackTrace();
               messageProcessor.processError("Can't send OK reply.");
            }
      }
   
    /** 
     * This method is called by the SIP stack when there's no answer 
     * to a message. Note that this is treated differently from an error
     * message. 
     */
      public void processTimeout(TimeoutEvent evt) {
         messageProcessor
            .processError("Previous message not sent: " + "timeout");
      }
   
    /** 
     * This method is called by the SIP stack when there's an asynchronous
     * message transmission error.  
     */
      public void processIOException(IOExceptionEvent evt) {
         messageProcessor.processError("Previous message not sent: "
            + "I/O Exception");
      }
   
    /** 
     * This method is called by the SIP stack when a dialog (session) ends. 
     */
      public void processDialogTerminated(DialogTerminatedEvent evt) {
      }
   
    /** 
     * This method is called by the SIP stack when a transaction ends. 
     */
      public void processTransactionTerminated(TransactionTerminatedEvent evt) {
      }
   
      public String getHost() {
         int port = sipProvider.getListeningPoints()[0].getPort();
         String host = sipStack.getIPAddress();
         return host;
      }
   
      public int getPort() {
         int port = sipProvider.getListeningPoints()[0].getPort();
         return port;
      }
   
      public String getUsername() {
         return username;
      }
   
      public void setUsername(String newUsername) {
         username = newUsername;
      }
   
      public MessageProcessor getMessageProcessor() {
         return messageProcessor;
      }
   
      public void setMessageProcessor(MessageProcessor newMessageProcessor) {
         messageProcessor = newMessageProcessor;
      }
      
      public void close(){
      
         try{
            //sipStack.deleteListeningPoint(tcp);
            sipStack.deleteListeningPoint(udp);
            sipProvider.removeSipListener(this);
         }
            catch (Exception e){
            
               System.err.println("Error on close in Text Client.");
            
            }
      
      }
   
   }
