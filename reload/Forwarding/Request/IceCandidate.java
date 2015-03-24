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
   package reload.Forwarding.Request;
  
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class IceCandidate{
   
      private IpAddressPort addr_port;
      private OverlayLinkType overlay_link;
      private Opaque foundation; //<0..255>  
      private int priority;
      private CandType type;
      
      private IpAddressPort rel_addr_port;
      
      private short ext_length;
      private IceExtension[] extensions; //<0..2^16-1>
      
   	
      
      public IceCandidate(IpAddressPort addr_port, OverlayLinkType overlay_link, byte[] foundation, int priority, IceExtension[] extensions) throws Exception{
      
         this.addr_port = addr_port;
         this.overlay_link = overlay_link;
         this.foundation = new Opaque(8, foundation);
         this.priority = priority;
      	
         type = CandType.host; // Default type �?
         
         this.extensions = extensions;
         
         for (int i=0; i<extensions.length; i++)
            ext_length += extensions[i].getBytes().length;
         
         if(ext_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      	
      }
      
      public IceCandidate(IpAddressPort addr_port, OverlayLinkType overlay_link, byte[] foundation, int priority, CandType type, IceExtension[] extensions) throws Exception{
      
         this(addr_port,overlay_link, foundation, priority, extensions);
         
         this.type = type;
         
         if (type == CandType.relay)
            throw new WrongTypeReloadException("No IPAddressPort specified (required for CandType relay)");
      
      }
   	
      public IceCandidate(IpAddressPort addr_port, OverlayLinkType overlay_link, byte[] foundation, int priority, IpAddressPort rel_addr_port, IceExtension[] extensions) throws Exception{
      
         this.addr_port = addr_port;
         this.overlay_link = overlay_link;
         this.foundation = new Opaque(8, foundation);
         this.priority = priority;
         
         this.rel_addr_port = rel_addr_port;
      	
         type = CandType.relay;
         
         this.extensions = extensions;
         
         for (int i=0; i<extensions.length; i++)
            ext_length += extensions[i].getBytes().length;
         
         if(ext_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      	
      }
      
      public IceCandidate(byte[] data) throws Exception {
         
         addr_port = new IpAddressPort(data);
         int offset = addr_port.getBytes().length;
         
         overlay_link = OverlayLinkType.valueOf(data[offset]);
         offset++;
         
         foundation = new Opaque(8, data, offset);
         offset += foundation.getBytes().length;
         
         priority = Utils.toInt(data, offset);
         offset += 4;
      
         type = CandType.valueOf(data[offset]);
         offset++;
         
         switch(type.getBytes()){
         
            case 1:
               break;
            case 2:
               break;
            case 3:
               break;
            case 4:
               rel_addr_port = new IpAddressPort(Utils.cutArray(data, offset));
               offset += rel_addr_port.getBytes().length;
         }
      
         ext_length = Utils.toShort(data, offset);
         offset += 2;
         
         byte[] dataExt = Utils.cutArray(data, ext_length, offset);
         offset = 0;
         
         int num = Algorithm.counter(2, dataExt, 0);
         
         num = num/2; // The number of IceExtensions is the half of the Opaques count (an IceExtension structure contains two Opaques)
      	
         extensions = new IceExtension[num];
         
         for (int i=0; i<num; i++){
         
            extensions[i] = new IceExtension(Utils.cutArray(dataExt, offset));
            offset += extensions[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(addr_port.getBytes());
         baos.write(overlay_link.getBytes());
         baos.write(foundation.getBytes());
         baos.write(Utils.toByte(priority));
         baos.write(type.getBytes());
      
         switch(type.getBytes()){
         
            case 1:
               /* Nothing */
               break;
            case 2:
               break;
            case 3:
               break;
            case 4:
               baos.write(rel_addr_port.getBytes());
         
         }
         
         baos.write(Utils.toByte(ext_length));
         
         for (int i=0; i<extensions.length; i++)
            baos.write(extensions[i].getBytes());
      	
         return baos.toByteArray();
      
      }
      
      public IpAddressPort getAddressPort(){
      
         return addr_port;
      
      }
   	    
   }