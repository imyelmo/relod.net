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
   package reload.Link;
   
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.security.MessageDigest; 

 
   public class FramedMessage{
   
   
      private FramedMessageType type;
      
      private int sequence;
      private Opaque message; // 24-bit  
   
      private int ack_sequence; 
      private int received;
   
   
      
      public FramedMessage (int sequence, byte[] message) throws ReloadException{
      
         type = FramedMessageType.data;
         this.sequence = sequence;
         this.message = new Opaque(24, message);
         
      }
      
      public FramedMessage (int ack_sequence, int received){
      
         type = FramedMessageType.ack;
         this.ack_sequence = ack_sequence;
         this.received = received;
         
      }
      
      public FramedMessage (byte[] data) throws ReloadException{
      
         type = FramedMessageType.valueOf(data[0]);
         
         switch(type.getBytes()){
         
            case (byte)128:
               sequence = Utils.toInt(data, 1);
               message = new Opaque(24, data, 5);
               break;
            	
            case (byte)129:
               ack_sequence = Utils.toInt(data, 1);
               received = Utils.toInt(data, 5);
               break;
               
            default:
               throw new WrongTypeReloadException();
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
      
         switch(type.getBytes()){
         
            case (byte)128:
               baos.write(Utils.toByte(sequence));
               baos.write(message.getBytes());
               break;
            	
            case (byte)129:
               baos.write(Utils.toByte(ack_sequence));
               baos.write(Utils.toByte(received));
               break;
               
            default:
               throw new WrongTypeReloadException();
         
         }
         
         return baos.toByteArray();
         
      }
      
      public FramedMessageType getType(){
      
         return type;
      
      }
      
      public int getSequence() throws ReloadException{
      
         if(type != FramedMessageType.data)
            throw new WrongTypeReloadException();
      
         return sequence;
      
      }
   	
   	
      public byte[] getMessage() throws ReloadException{
      
         if(type != FramedMessageType.data)
            throw new WrongTypeReloadException();
      
         return message.getContent();
      
      }	
   	
      public int getAckSequence() throws ReloadException{
      
         if(type != FramedMessageType.ack)
            throw new WrongTypeReloadException();
      
         return ack_sequence;
      
      }
   	
      public int getReceived() throws ReloadException{
      
         if(type != FramedMessageType.ack)
            throw new WrongTypeReloadException();
            
         return received;
      
      }
   
   }