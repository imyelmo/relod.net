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
   import reload.Storage.*;
   import reload.Message.Security.Signature;
	
   public class StoredData{
   
      private int length;
      private long storage_time;
      private int life_time;
      private StoredDataValue value;
      private Signature signature;
      
   
      
      public StoredData (long storage_time, int life_time, StoredDataValue value, Signature signature) throws Exception{
      
         this.storage_time = storage_time;
         this.life_time = life_time;
         this.value = value;
         this.signature = signature;
         
         length = 8 + 4 + value.getBytes().length + signature.getBytes().length;
         
      }
      
      public StoredData (DataStructure data, Signature signature) throws Exception{ // Single & Dictionary
      
      
         storage_time = data.getStorageTime();
         life_time = data.getLifeTime();
         if(data.isDictionary()) // Dictionary
            value = new StoredDataValue(new DictionaryEntry(data.getDictionaryKey(), new DataValue(data.getExists(), data.getValue())));
         else // Single
            value = new StoredDataValue(new DataValue(data.getExists(), data.getValue()));
         this.signature = signature;
         
         length = 8 + 4 + value.getBytes().length + signature.getBytes().length;
         
      }
      
      public StoredData (DataStructure data, int index, Signature signature) throws Exception{ // Array
      
         storage_time = data.getStorageTime();
         life_time = data.getLifeTime();
         value = new StoredDataValue(new ArrayEntry(index, new DataValue(data.getExists(), data.getValue())));
         this.signature = signature;
         
         length = 8 + 4 + value.getBytes().length + signature.getBytes().length;
         
      }
      
      public StoredData (byte[] data, DataModel type) throws Exception{
      
         length = Utils.toInt(data, 0);
         
         data = Utils.cutArray(data, length, 4);
      	
         storage_time = Utils.toLong(data, 0);
         life_time = Utils.toInt(data, 8);
      	
         value = new StoredDataValue (Utils.cutArray(data, 12), type);
         int offset = value.getBytes().length + 12;
         
         signature = new Signature(Utils.cutArray(data, offset));
         
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(length));
         baos.write(Utils.toByte(storage_time));
         baos.write(Utils.toByte(life_time));
         baos.write(value.getBytes());
         baos.write(signature.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public int getLength(){
      
         return length;
      }
   	
      public long getStorageTime(){
      
         return storage_time;
         
      }
   	
      public int getLifeTime(){
      
         return life_time;
         
      }
   	
      public StoredDataValue getValue(){
      
         return value;
         
      }
   	
      public Signature getSignature(){
      
         return signature;
         
      }
      
      public DataStructure getData(DataModel dataModel) throws ReloadException{
      
         switch(dataModel.getBytes()){
         
            case 1:
               return new DataStructure(storage_time, life_time, value.getSingleValue().getExists(), value.getSingleValue().getOpaqueValue());
            case 2:
               return new DataStructure(storage_time, life_time, value.getArray().getDataValue().getExists(), value.getArray().getDataValue().getOpaqueValue());
            case 3:
               return new DataStructure(value.getDictionary().getDictionaryKey(), storage_time, life_time, value.getDictionary().getDataValue().getExists(), value.getDictionary().getDataValue().getOpaqueValue());
            default:
               throw new WrongTypeReloadException();
         }
      
      }
      
      public int getIndex(){
      
         return value.getArray().getIndex();
      
      }
   	
   }