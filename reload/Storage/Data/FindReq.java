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
   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class FindReq{
   
   
      private ResourceId resource;
      
      private byte kinds_length;
      private KindId[] kinds; //<0..2^8-1>
    
      
      public FindReq (ResourceId resource, byte replica_number, KindId[] kinds) throws Exception{
      
         this.resource = resource;
         this.kinds = kinds;
         
         for (int i=0; i<kinds.length; i++)
            kinds_length += kinds[i].getBytes().length;
            
         if(kinds_length > Math.pow(2, 8)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public FindReq (byte[] data) throws Exception{
      
         resource = new ResourceId(data, false); //Lower levers
         int offset = resource.getBytes().length;
         
         kinds_length = data[offset];
         offset++;
         
         data = Utils.cutArray(data, kinds_length, offset);
         offset = 0;
         
         if (data.length % 4 != 0)
            throw new WrongLengthReloadException();
      	
         int num = data.length / 4;
      	
         kinds = new KindId[num];
      	
         for (int i=0; i<num; i++){
         
            kinds[i] = new KindId(Utils.cutArray(data, offset));
            offset += 4;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(resource.getBytes());
         baos.write(kinds_length);
      
         for (int i=0; i<kinds.length; i++)
            baos.write(kinds[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public ResourceId getResource(){
         
         return resource;
      	
      }
   
      public KindId[] getKinds(){
      
         return kinds;
      
      }
   
   }