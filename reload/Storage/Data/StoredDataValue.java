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
   
   public class StoredDataValue{
   
      private DataValue single_value_entry;
      private ArrayEntry array_entry;
      private DictionaryEntry dictionary_entry;
      private DataModel dataModel;
   	
      
      public StoredDataValue (DataValue single_value_entry){
      
         dataModel = DataModel.single_value;
      
         this.single_value_entry = single_value_entry;
         
      }
      
      public StoredDataValue (ArrayEntry array_entry){
      
         dataModel = DataModel.array;
      
         this.array_entry = array_entry;
         
      }
      
      public StoredDataValue (DictionaryEntry dictionary_entry){
      
         dataModel = DataModel.dictionary;
      
         this.dictionary_entry = dictionary_entry;
         
      }
      
      public StoredDataValue (byte[] data, DataModel type) throws Exception{
      
         dataModel = type;
      
         switch(type.getBytes()){
         
            case 1:
               single_value_entry = new DataValue(data);
               break;
            case 2:
               array_entry = new ArrayEntry(data);
               break;
            case 3:
               dictionary_entry = new DictionaryEntry(data);
               break;
            default:
               throw new WrongTypeReloadException();
           
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         
         switch(dataModel.getBytes()){
         
            case 1:
               return single_value_entry.getBytes();
            case 2:
               return array_entry.getBytes();
            case 3:
               return dictionary_entry.getBytes();
            default:
               throw new WrongTypeReloadException();
           
         }
            
      }
      
      public DataValue getSingleValue(){
      
         return single_value_entry;
      
      }
         
      public ArrayEntry getArray(){
      
         return array_entry;
      
      }
   	      
      public DictionaryEntry getDictionary(){
      
         return dictionary_entry;
      
      }
   
   }