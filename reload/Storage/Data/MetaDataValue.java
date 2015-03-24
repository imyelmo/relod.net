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
   import java.util.ArrayList;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Storage.Data.*;
   
   public class MetaDataValue{
   
   	
      private DataModel dataModel;
   	
      private MetaData single_value_entry;
      
      private ArrayEntryMeta array_entry;
      
      private DictionaryEntryMeta dictionary_entry;
    
      
      public MetaDataValue (MetaData single_value_entry){
      
      
         dataModel = DataModel.single_value;
         
         this.single_value_entry = single_value_entry;
      
      }
      
      public MetaDataValue (ArrayEntryMeta array_entry){
      
         dataModel = DataModel.array;
      
         this.array_entry=array_entry;
      
      }
      
      public MetaDataValue (DictionaryEntryMeta dictionary_entry){
      
         dataModel = DataModel.dictionary;
         
         this.dictionary_entry = dictionary_entry;
      
      }
      
      public MetaDataValue (byte[] data, DataModel dataModel) throws Exception{
      
      
         switch(dataModel.getBytes()){
            case 1:
               single_value_entry = new MetaData(data);
               break;
            case 2:
               array_entry = new ArrayEntryMeta(data);
               break;
            case 3:
               dictionary_entry = new DictionaryEntryMeta(data);   	
               break;
            default:
               throw new WrongTypeReloadException();	
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         switch(dataModel.getBytes()){
         
            case 1:
               baos.write(single_value_entry.getBytes());
               break;
            case 2:
               baos.write(array_entry.getBytes());
               break;
            case 3:
               baos.write(dictionary_entry.getBytes());
               break; 
            default:
               throw new WrongTypeReloadException();
         }
      
         return baos.toByteArray();
      
      }
   
      public DataModel getDataModel(){
         
         return dataModel;
      	
      }
   	
      public MetaData getSingleValueEntry(){
      
         return single_value_entry;
      
      }
      
      public ArrayEntryMeta getArrayEntry(){
      
         return array_entry;
      
      }
      
      public DictionaryEntryMeta getDictionaryEntry(){
      
         return dictionary_entry;
      
      }
      
   }