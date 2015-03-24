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
   
   public class ProbeInformation{
   
      private ProbeInformationType type;
      private byte length;
      private ProbeInformationData value;
      
      public ProbeInformation (ProbeInformationType type, ProbeInformationData value) throws Exception{
      
         this.type = type;
         this.value = value;
         
         length = (byte)value.getBytes().length;
      
      }
      
      public ProbeInformation (byte[] data) throws ReloadException{
      
         type = ProbeInformationType.valueOf(data[0]);
         length = data[1];
         value = new ProbeInformationData(Utils.cutArray(data, length, 3), type);
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(length);
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public ProbeInformationType getType(){
      
         return type;
      
      }
   	
      public ProbeInformationData getData(){
      
         return value;
      
      }
   
   }