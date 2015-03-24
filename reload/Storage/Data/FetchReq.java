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
   
   public class FetchReq{
   
   
      private ResourceId resource;
    
      private short specifiers_length;   
   	
      private StoredDataSpecifier[] specifiers; //<0..2^16-1>
    
      
      public FetchReq (ResourceId resource, StoredDataSpecifier[] specifiers) throws Exception{
      
         this.resource = resource;
         this.specifiers = specifiers;
      
         for (int i=0; i<specifiers.length; i++)
            specifiers_length += specifiers[i].getBytes().length;
      
         if(specifiers_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public FetchReq (byte[] data) throws Exception{
      
         resource = new ResourceId(data, false); // Lower level
         
         int offset = resource.getBytes().length;
         
         specifiers_length = Utils.toShort(data, offset);
         
         data = Utils.cutArray(data, specifiers_length, offset + 2);
         offset = 0;
         
         int num = Algorithm.counter(2, data, 12);	
      		
         specifiers = new StoredDataSpecifier[num];
      	
         for (int i=0; i<num; i++){
         
            specifiers[i] = new StoredDataSpecifier(Utils.cutArray(data, offset));
            offset += specifiers[i].getBytes().length;
         }	
         
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(resource.getBytes());
         baos.write(Utils.toByte(specifiers_length));
      	
         for (int i=0; i<specifiers.length; i++)
            baos.write(specifiers[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public ResourceId getResource(){
         
         return resource;
      	
      }
   	
      public StoredDataSpecifier[] getSpecifiers(){
      
         return specifiers;
      
      }
   	
   }