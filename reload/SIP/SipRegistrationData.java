   package reload.SIP;

   import java.io.*;
   import reload.Common.Exception.*;
   import reload.Common.Utils;

   import reload.Message.Forwarding.Destination;
   
   public class SipRegistrationData{
   
      private short string_length;
   
      private String uri; //<0..2^16-1>
      
      private String contact_prefs; //<0..2^16-1>
      
      private short destination_length;
      private Destination[] destination_list; //<0..2^16-1>
      
      private SipRegistrationType type;
   
      
      public SipRegistrationData (String uri) throws ReloadException{
      
         this.uri = uri; //new Opaque(16, uri);
         
         string_length = (short)uri.length();
         
         if(string_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
         
         type = SipRegistrationType.sip_registration_uri;
      
      }
      
      public SipRegistrationData (String contact_prefs, Destination[] destination_list) throws Exception{
      
         this.contact_prefs = contact_prefs; //new Opaque(16, contact_prefs);
       
         string_length = (short)contact_prefs.length();
         
         if(string_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      	
         this.destination_list = destination_list;
         
         for (int i=0; i<destination_list.length; i++)
            destination_length += destination_list[i].getBytes().length;
         
         if(destination_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
            
         type = SipRegistrationType.sip_registration_route;
      
      }
      
      public SipRegistrationData (byte[] data, SipRegistrationType type) throws Exception{
      
         this.type = type;
         
         string_length = Utils.toShort(data, 0);
         
         switch(type.getValue()){
         
            case 1:
            
               uri = new String(Utils.cutArray(data, string_length, 2), "US-ASCII"); //new Opaque(16, data, 0);
               break;
            	
            case 2:
            
               contact_prefs = new String(Utils.cutArray(data, string_length, 2), "US-ASCII"); //new Opaque(16, data, 0);
               int offset = string_length+2;
               
               destination_length = Utils.toShort(data, offset);
               
               offset += 2;
                 
               int length_array = 0;
               byte[] curr_data = Utils.cutArray(data, destination_length, offset);
            	  
               for (int sum=0; sum<destination_length; length_array++){
               
                  Destination curr_dest = new Destination(curr_data);
                  sum += curr_dest.getBytes().length;
                  curr_data = Utils.cutArray(curr_data, curr_dest.getBytes().length);
               }
            
               destination_list = new Destination[length_array];
            
               for (int i=0, sum=0; i<length_array; sum+=destination_list[i].getBytes().length, i++)
                  destination_list[i] = new Destination(Utils.cutArray(data, offset+sum));	
                  
               break;      
         		
            default:
               throw new WrongTypeReloadException();	
         
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(string_length));
         
         switch(type.getValue()){
         
            case 1:
               baos.write(uri.getBytes("US-ASCII"));   
               break;
            	
            case 2:
               baos.write(contact_prefs.getBytes("US-ASCII"));
               baos.write(Utils.toByte(destination_length));
               for (int i=0; i<destination_list.length; i++)
                  baos.write(destination_list[i].getBytes());
               break;      
         		
            default:
               throw new WrongTypeReloadException();	
         }
         
         return baos.toByteArray();
      
      }
      
      public SipRegistrationType getType(){
      
         return type;
      
      }
      
      public String getUri() throws ReloadException {
      
         if(type != SipRegistrationType.sip_registration_uri)
            throw new WrongTypeReloadException();	
          
         return uri;
      
      }
      
      public String getContactPrefs() throws ReloadException {
      
         if(type != SipRegistrationType.sip_registration_route)
            throw new WrongTypeReloadException();	
          
         return contact_prefs;
      
      }
      
      public Destination[] getDestinationList() throws ReloadException {
      
         if(type != SipRegistrationType.sip_registration_route)
            throw new WrongTypeReloadException();	
          
         return destination_list;
      
      }
   
   }