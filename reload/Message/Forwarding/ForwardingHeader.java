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
   package reload.Message.Forwarding;

   import java.util.*;
   import java.io.*;
   import reload.Common.*;
   
   public class ForwardingHeader {
   
   
      private int relo_token;
      private int overlay;
      private short configuration_sequence;
      private byte version;
      private byte ttl;
      private int fragment;
      private int length; // Length of the whole message, including this header
      private long transaction_id;
      private int max_response_length;
      private short via_list_length;
      private short destination_list_length;
      private short options_length;
      private Destination[] via_list; // Multiple Destination
      private Destination[] destination_list; // Multiple Destination
      private ForwardingOptions[] options; // Multiple options
      
   
   
   //DESTINATION[] ONLY
      public ForwardingHeader(int overlay, short configuration_sequence, byte ttl, int fragment,
      int max_response_length, Destination[] destination_list) throws Exception{
      
         Random r = new Random();
      
         this.relo_token=0xd2454c4f;
         this.overlay=overlay;
         this.configuration_sequence=configuration_sequence;
         this.version=0xa; // 1.0
         this.ttl=ttl; // 100 normalmente
         this.fragment=fragment;
         this.transaction_id=r.nextLong(); // Random
         this.max_response_length=max_response_length;
      	
         this.destination_list = destination_list;
         for (int i=0; i<destination_list.length; i++)
            destination_list_length += (short)destination_list[i].getBytes().length;
            
         via_list = new Destination[0];
      		
         options = new ForwardingOptions[0];
          
      }
      
   // DESTINATION[] & VIA[]	
      public ForwardingHeader(int overlay, short configuration_sequence, byte ttl, int fragment, int max_response_length,
      Destination[] via_list, Destination[] destination_list) throws Exception {
      
         this(overlay, configuration_sequence, ttl, fragment, max_response_length, destination_list);
      
         this.via_list = via_list;  
         for (int i=0; i<via_list.length; i++)
            via_list_length  += (short)via_list[i].getBytes().length;
      
      }
      
      public ForwardingHeader(byte[] data) throws Exception {
      
         relo_token = Utils.toInt(data, 0);
         overlay = Utils.toInt(data, 4);
         configuration_sequence = Utils.toShort(data, 8);
         version = data[10];
         ttl = data[11];
         fragment = Utils.toInt(data, 12);
         length = Utils.toInt(data, 16);
         transaction_id = Utils.toLong(data, 20);
         max_response_length = Utils.toInt(data, 28);
         via_list_length = Utils.toShort(data, 32);
         destination_list_length = Utils.toShort(data, 34);
         options_length = Utils.toShort(data, 36);
         
         
         int length_array=0;
         byte[] curr_data = Utils.cutArray(data, via_list_length, 38);
            
         for (int sum=0; sum<via_list_length; length_array++){
            Destination curr_via_list = new Destination(curr_data);
            sum += curr_via_list.getBytes().length;
            curr_data = Utils.cutArray(curr_data, curr_via_list.getBytes().length);
         }
            
         via_list = new Destination[length_array];
            
         for (int i=0, sum=0; i<length_array; sum+=via_list[i++].getBytes().length)
            via_list[i] = new Destination(Utils.cutArray(data, 38+sum));	


         length_array=0;
         curr_data = Utils.cutArray(data, destination_list_length, 38+via_list_length);
         for (int sum=0; sum<destination_list_length; length_array++){
            Destination curr_destination_list = new Destination(curr_data);
            sum += curr_destination_list.getBytes().length;
            curr_data = Utils.cutArray(curr_data, curr_destination_list.getBytes().length);
         }
            
         destination_list = new Destination[length_array];
            
         for (int i=0, sum=0; i<length_array; sum+=destination_list[i++].getBytes().length)
            destination_list[i] = new Destination(Utils.cutArray(data, 38+via_list_length+sum));	
         
      	            
         length_array=0;
         curr_data = Utils.cutArray(data, options_length, 38+via_list_length+destination_list_length);
         for (int sum=0; sum<options_length; length_array++){
            ForwardingOptions curr_options = new ForwardingOptions(curr_data);
            sum += curr_options.getBytes().length;
            curr_data = Utils.cutArray(curr_data, curr_options.getBytes().length);
         }
            
         options = new ForwardingOptions[length_array];
            
         for (int i=0, sum=0; i<length_array; sum+=options[i++].getBytes().length)
            options[i] = new ForwardingOptions(Utils.cutArray(data, 38+via_list_length+destination_list_length+sum));	

         
      }
      
      public void setLength(int length){
      
         this.length = length;
      
      }
      
      public int getLength(){
      
         return length;
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(relo_token));
         baos.write(Utils.toByte(overlay));
         baos.write(Utils.toByte(configuration_sequence));
         baos.write(version);
         baos.write(ttl);
         baos.write(Utils.toByte(fragment));
         baos.write(Utils.toByte(length));
         baos.write(Utils.toByte(transaction_id));
         baos.write(Utils.toByte(max_response_length));
         baos.write(Utils.toByte(via_list_length));
         baos.write(Utils.toByte(destination_list_length));
         baos.write(Utils.toByte(options_length));
         
         if(via_list_length > 0){
            for (int i=0; i<via_list.length; i++)
               baos.write(via_list[i].getBytes());
         }
         if(destination_list_length > 0){
            for (int i=0; i<destination_list.length; i++)
               baos.write(destination_list[i].getBytes());
         }
         if(options_length > 0){
            for (int i=0; i<options.length; i++)
               baos.write(options[i].getBytes());
         }
            
         
         
         return baos.toByteArray();
      
      }
   	
      public int getOverlay(){
      
         return overlay;
      
      }	
   	
      public short getConfigurationSequence(){
      
         return configuration_sequence;
      
      }
   	
      public byte getVersion(){
      
         return version;
      
      }
   
      public byte getTTL(){
      
         return ttl;
      
      }
      
      public int getFragment(){
      
         return fragment;
      
      }	
      
      public long getTransactionId(){
      
         return transaction_id;
      
      }
   	
      public int getMaxResponseLength(){
      
         return max_response_length;
      
      }	
      
      public Destination[] getViaList(){
      
         return via_list;
      
      }
   	
      public Destination[] getDestinationList(){
      
         return destination_list;
         
      }
      
      public ForwardingOptions[] getOptions(){
      
         return options;
      
      }
      
      public void setTTL(byte ttl){
      
         this.ttl = ttl;
      
      }
      
      public void setDestinationList(Destination[] destination) throws Exception{
      
         int previuos_length = length;
         int previous_destination_length = destination_list_length;
      
         destination_list = destination;
         
         destination_list_length = 0;
      	
         for (int i=0; i<destination_list.length; i++)
            destination_list_length += (short)destination_list[i].getBytes().length;
            
         int diff = destination_list_length - previous_destination_length;
      		
         length = previuos_length + diff;
      
      }
   	
      public void setViaList(Destination[] via) throws Exception{
      
         int previuos_length = length;
         int previous_via_length = via_list_length;
      
         via_list = via;
         
         via_list_length = 0;
      	
         for (int i=0; i<via_list.length; i++)
            via_list_length  += (short)via_list[i].getBytes().length;
            
         int diff = via_list_length - previous_via_length;
      		
         length = previuos_length + diff;
      
      }
   
   }