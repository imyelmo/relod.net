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

import java.io.*;
import reload.Common.*;
import reload.Common.Exception.*;

public class Destination{

   private DestinationType type;
   private byte length;
   private DestinationData destination_data; // If type's top bit = 0
   
   short opaque_id; // If type's top bit = 1
	
   boolean opaque; // Top bit = 1
   
   
   public Destination (DestinationType type, DestinationData destination_data) throws Exception{
   
      this.type = type;
      this.length = (byte)destination_data.getBytes().length;
      this.destination_data = destination_data;
      opaque = false;
   
   }
   
   public Destination (Id id) throws Exception{
   
      type = id.getType();
      destination_data = new DestinationData(id);
      
      this.length = (byte)destination_data.getBytes().length;
      opaque = false;
   
   }
   
   public Destination (short opaque_id) throws IOException{
   
      length = 2;
   	
      if(opaque_id < 0)
         this.opaque_id = opaque_id;
      
      else
         this.opaque_id = (short)(opaque_id | 0x8000);
   	
      opaque = true;
    
   }
   
	
   public Destination (byte[] data) throws Exception{ 
   
      if(data[0] < 0){ // If top bit = 1, 16-bit integer (short)
      
         opaque = true;
      
         
         if(data.length != 2)
            throw new WrongLengthReloadException("Compressed opaque id does not equal 2 bytes length.");        
              	
         opaque_id = Utils.toShort(data, 0);
      }
      	
      else{ // If top bit = 0
      
         opaque = false;
         type = DestinationType.valueOf(data[0]);      
         length = data[1];
         destination_data = new DestinationData(Utils.cutArray(data, length, 2), type);
      	
      }
   
   }
   
   public byte[] getBytes() throws Exception{
   
   
      if(opaque){
       
         return Utils.toByte(opaque_id);
      	
      }
      
      else{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
         baos.write(type.getBytes());
         baos.write(length);
         baos.write(destination_data.getBytes());
      
         return baos.toByteArray();
      }
   
   }   
   
   public DestinationType getType(){
   
      return type;
   
   }
   
   public byte getLength(){
   
      return length;
   
   }
   
   public NodeId getNodeId() throws ReloadException{
   
      return getDestinationData().getNodeId();
   
   }
   
   public ResourceId getResourceId() throws ReloadException{
   
      return getDestinationData().getResourceId();
   
   }
   
   public DestinationData getDestinationData() throws ReloadException{
   
      if(!opaque)
         return destination_data;
      else
         throw new WrongTypeReloadException("Destination is opaque. No DestionationData here.");
   		
   }
   
   public short getOpaqueId() throws ReloadException{
   
      if(opaque)
         return opaque_id;
      else
         throw new WrongTypeReloadException("Destination is not opaque.");
   
   }

}