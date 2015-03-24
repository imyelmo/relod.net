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
   
   public class StoreReq{
   
   
      private ResourceId resource;
      private byte replica_number;
      
      private int kind_length;
      private StoreKindData[] kind_data; //<0..2^32-1>
    
      
      public StoreReq (ResourceId resource, byte replica_number, StoreKindData[] kind_data) throws Exception{
      
         this.resource = resource;
         this.replica_number = replica_number;
         this.kind_data = kind_data;
         
         for (int i=0; i<kind_data.length; i++)
            kind_length += kind_data[i].getBytes().length;
            
         if(kind_length > Math.pow(2, 32)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StoreReq (byte[] data) throws Exception{
      
         resource = new ResourceId(data, false); // Lower level
         int offset = resource.getBytes().length;
         
         replica_number = data[offset];
         offset++;
         
         kind_length = Utils.toInt(data, offset);
         offset += 4;
         
         data = Utils.cutArray(data, kind_length, offset);
         offset = 0;
      	
         int num = Algorithm.counter(4, data, 12);
      		
         kind_data = new StoreKindData[num];
      	
         for (int i=0; i<num; i++){
         
            kind_data[i] = new StoreKindData(Utils.cutArray(data, offset));
            offset += kind_data[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(resource.getBytes());
         baos.write(replica_number);
         baos.write(Utils.toByte(kind_length));
         
         for (int i=0; i<kind_data.length; i++)
            baos.write(kind_data[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public ResourceId getResource(){
      
         return resource;
      
      }
   
      public byte getReplicaNumber(){
      
         return replica_number;
      
      }
   	
      public StoreKindData[] getKindData(){
      
         return kind_data;
      
      }
   
   }