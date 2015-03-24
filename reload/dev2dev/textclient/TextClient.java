   package reload.dev2dev.textclient;

   import java.awt.event.*;
   import java.net.*;
   import java.text.ParseException;
   import java.util.*;

   import javax.sip.*;
   import javax.sip.address.*;
   import javax.sip.header.*;
   import javax.sip.header.ToHeader;
   import javax.sip.header.ViaHeader;
   import javax.sip.message.*;
   import javax.swing.*;

   public class TextClient 
   extends JFrame
   implements MessageProcessor
   {
      private SipLayer sipLayer;
    
      private JTextField fromAddress;
      private JLabel fromLbl;
      private JLabel receivedLbl;
      private JTextArea receivedMessages;
      private JScrollPane receivedScrollPane;
      private JButton sendBtn;
      private JLabel sendLbl;
      private JTextField sendMessages;
      private JTextField toAddress;
      private JLabel toLbl;
      
      private final String from; // = "sip:marcos@192.168.1.1:9999";
      private final String to; // = "sip:isabel@192.168.1.70:9999";
   
   
   
      public TextClient(SipLayer sip, String from, String to){
      
         super();
         
         this.from = from;
         this.to = to;	
      	
         sipLayer = sip;
         initWindow();
      }
    
      private void initWindow() {
         receivedLbl = new JLabel();
         sendLbl = new JLabel();
         sendMessages = new JTextField();
         receivedScrollPane = new JScrollPane();
         receivedMessages = new JTextArea();

         sendBtn = new JButton();
      
         getContentPane().setLayout(null);
      
         setTitle("TextClient");
         
         addWindowListener(
               new WindowAdapter() {
                  public void windowClosing(WindowEvent evt) {
                     sipLayer.close();
                  }
               });
      
         receivedLbl.setText("Received Messages:");
         receivedLbl.setAlignmentY(0.0F);
         receivedLbl.setPreferredSize(new java.awt.Dimension(25, 100));
         getContentPane().add(receivedLbl);
         receivedLbl.setBounds(5, 0, 136, 20);
      
         sendLbl.setText("Send Message:");
         getContentPane().add(sendLbl);
         sendLbl.setBounds(5, 150, 90, 20);
      
         getContentPane().add(sendMessages);
         sendMessages.setBounds(5, 170, 270, 20);
      
         receivedMessages.setAlignmentX(0.0F);
         receivedMessages.setEditable(false);
         receivedMessages.setLineWrap(true);
         receivedMessages.setWrapStyleWord(true);
         receivedScrollPane.setViewportView(receivedMessages);
         receivedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      
         getContentPane().add(receivedScrollPane);
         receivedScrollPane.setBounds(5, 20, 270, 130);

      
         sendBtn.setText("Send");
         sendBtn.addActionListener(
               new ActionListener() {
                  public void actionPerformed(ActionEvent evt) {
                     sendBtnActionPerformed(evt);
                  }
               });
      
         getContentPane().add(sendBtn);
         sendBtn.setBounds(200, 200, 75, 25);
      
         java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
         setBounds((screenSize.width-288)/2, (screenSize.height-310)/2, 295, 275);
      }
   
      private void sendBtnActionPerformed(ActionEvent evt) {
      
         try
         {
            String message = this.sendMessages.getText();
            sipLayer.sendMessage(to, message);
         } 
            catch (Throwable e)
            {
               e.printStackTrace();
               this.receivedMessages.append("ERROR sending message: " + e.getMessage() + "\n");
            }
        			
      }
   
      public void processMessage(String sender, String message)
      {
         this.receivedMessages.append("From " +
                sender + ": " + message + "\n");
      }
   
      public void processError(String errorMessage)
      {
         this.receivedMessages.append("ERROR: " +
                errorMessage + "\n");
      }
    
      public void processInfo(String infoMessage)
      {
         this.receivedMessages.append(
                infoMessage + "\n");
      }
   }
