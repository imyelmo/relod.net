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
   package reload.Message;

   import java.util.*;
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Message.Forwarding.*;
   import reload.Message.MessageCont.*;
   import reload.Message.Security.*;
   import reload.Message.Security.TLS.*;

   
   public class MessageStructure {
   
      private ForwardingHeader fh;
      private MessageContents mc;
      private SecurityBlock sb;
   
   
   
      public MessageStructure(ForwardingHeader fh, MessageContents mc, SecurityBlock sb) throws Exception{ //Standard message
      
         this.fh=fh;
         this.mc=mc;
         this.sb=sb;
         
         setSize();
      
      }
      
      public MessageStructure(byte[] data) throws Exception{
      
         fh = new ForwardingHeader(data);
         
         int length = fh.getLength();
         data = Utils.cutArray(data, length, 0);
         
         int fhLength = fh.getBytes().length;
         
         if(fhLength >= length)
            throw new WrongLengthReloadException("Inconsistent length field.");
         
         mc = new MessageContents(Utils.cutArray(data, fhLength));
         
         int mcLength = mc.getBytes().length;
         
         sb = new SecurityBlock(Utils.cutArray(data, fhLength+mcLength));
               
      }
      
      private void setSize() throws Exception{
      
         int sizeFH = fh.getBytes().length;
         int sizeMC = mc.getBytes().length;
         int sizeSB = sb.getBytes().length;
      	
         fh.setLength(sizeFH+sizeMC+sizeSB);
         
      }
          
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(fh.getBytes());
         baos.write(mc.getBytes());
         baos.write(sb.getBytes());
         
         return baos.toByteArray();
      
      }   
      
      public ForwardingHeader getForwardingHeader(){
      
         return fh;
      }
   	
      public MessageContents getMessageContents(){
      
         return mc;
      }
   	
      public SecurityBlock getSecurityBlock(){
      
         return sb;
      }
   
   
   }