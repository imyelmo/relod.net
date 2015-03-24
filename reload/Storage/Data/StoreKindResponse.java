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
   
   public class StoreKindResponse{
   
      private KindId kind;
      private long generation_counter;
      
      private short replicas_length;
      private NodeId[] replicas; //<0..2^16-1>
    
      
      public StoreKindResponse (KindId kind, long generation_counter, NodeId[] replicas) throws Exception{
      
         this.kind = kind;
         this.generation_counter = generation_counter;
         this.replicas = replicas;
         
         for (int i=0; i<replicas.length; i++)
            replicas_length += replicas[i].getBytes().length;
            
         if(replicas_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StoreKindResponse (byte[] data) throws Exception{
      
         kind = new KindId(data); // 32-bit
         generation_counter = Utils.toLong(data, 4);
         replicas_length = Utils.toShort(data, 12);
      	
         data = Utils.cutArray(data, replicas_length, 14);
         
         if (data.length % NodeId.getLength() != 0)
            throw new WrongLengthReloadException();
         
         int num = data.length / NodeId.getLength();
         
         int offset=0;
           
         replicas = new NodeId[num];
      	
         for (int i=0; i<num; i++){
         
            replicas[i] = new NodeId(Utils.cutArray(data, offset), false);
            offset += replicas[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(kind.getBytes());
         baos.write(Utils.toByte(generation_counter));
         baos.write(Utils.toByte(replicas_length));
      
         for (int i=0; i<replicas.length; i++)
            baos.write(replicas[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public KindId getKind(){
        
         return kind; 
      	
      }
      	
      public long getGenerationCounter(){
      
         return generation_counter;
      
      }
      
      public NodeId[] getReplicas(){
      
         return replicas;
      
      }
      
   }