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
   
   public class StoreKindData{
   
      private KindId kind;
      private long generation_counter;
      
      private int values_length;
      private StoredData[] values; //<0..2^32-1>
    
      
      public StoreKindData (KindId kind, long generation_counter, StoredData[] values) throws Exception{
      
         this.kind = kind;
         this.generation_counter = generation_counter;
         this.values = values;
         
         for (int i=0; i<values.length; i++)
            values_length += values[i].getBytes().length;
            
         if(values_length > Math.pow(2, 32)-1)
            throw new WrongLengthReloadException(); 
      
      }
      
      public StoreKindData (byte[] data) throws Exception{
      
         kind = new KindId(data); // 32-bit
         generation_counter = Utils.toLong(data, 4);
         
         values_length = Utils.toInt(data, 12);
         
         data = Utils.cutArray(data, values_length, 16);
         int offset = 0;
         
         int num = Algorithm.counter(4, data, 0);
         
         values = new StoredData[num];
      	
         DataModel dataModel = kind.getDataModel();
      
         for (int i=0; i<num; i++){
         
            values[i] = new StoredData(Utils.cutArray(data, offset), dataModel);
            offset += values[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(kind.getBytes());
         baos.write(Utils.toByte(generation_counter));
         baos.write(Utils.toByte(values_length));
      
         for (int i=0; i<values.length; i++)
            baos.write(values[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public KindId getKind(){
      
         return kind;
      
      }
   	
      public long getGenerationCounter(){
      
         return generation_counter;
      
      }
   	
      public StoredData[] getValues(){
      
         return values;
      
      }
   
   }