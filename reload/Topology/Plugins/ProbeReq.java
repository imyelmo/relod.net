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
   package reload.Topology.Plugins;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ProbeReq{
   
      private byte probe_length;
      private ProbeInformationType[] requested_info; //8-bit
      
      
      public ProbeReq (ProbeInformationType[] requested_info) throws Exception{
      
         this.requested_info = requested_info;
         
         probe_length = (byte)requested_info.length;
         
         if(requested_info.length > Math.pow(2, 8)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public ProbeReq (byte[] data){
      
         probe_length = data[0];
         
         data = Utils.cutArray(data, probe_length, 1);
      
         requested_info = new ProbeInformationType[probe_length];
      
         for (int i=0; i<probe_length; i++)
            requested_info[i] = ProbeInformationType.valueOf(data[i]);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
       
         baos.write(probe_length); 
      	
         for (int i=0; i<probe_length; i++)
            baos.write(requested_info[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public ProbeInformationType[] getRequestedInfo(){
      
         return requested_info;
      
      }
      
   }