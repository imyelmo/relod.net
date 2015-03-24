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
   
   public class ProbeAns{
   
   
      private short probe_length;
      private ProbeInformation[] probe_info; //<0..2^16-1>
         
      
      public ProbeAns (ProbeInformation probe_info[]) throws Exception{
      
         this.probe_info = probe_info;
         
         for (int i=0; i<probe_info.length; i++)
            probe_length += probe_info[i].getBytes().length;
            
         if(probe_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public ProbeAns (byte[] data) throws Exception{
      
         probe_length = Utils.toShort(data, 0);
         
         Utils.cutArray(data, probe_length, 2);
         int offset = 0;
         
         int num = Algorithm.counter(1, data, 1);
      
         probe_info = new ProbeInformation[num];
      
         for (int i=0; i<num; i++){
         
            probe_info[i] = new ProbeInformation(Utils.cutArray(data, offset));
            offset += probe_info[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(probe_length));
         
         for (int i=0; i<probe_info.length; i++)
            baos.write(probe_info[i].getBytes());
         
         
         return baos.toByteArray();
      
      }
   
      public ProbeInformation[] getProbeInfo(){
      
         return probe_info;
      
      }
   
   }