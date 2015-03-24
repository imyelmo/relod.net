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

   package reload.Common;
	
   import reload.Storage.Data.DictionaryKey;	
 
   public class DataStructure{
   
      private DictionaryKey key; // For Dictionary only
      private long storage_time;
      private int life_time;
      private boolean exists;
      private Opaque value;
   	
      private boolean dictionary;
   
   	
      public DataStructure(DictionaryKey key, long storage_time, int life_time, boolean exists, Opaque value){
      
         this.key = key;
         this.storage_time = storage_time;
         this.life_time = life_time;
         this.exists = exists;
         this.value = value;
         
         dictionary = true;
        
      }
      
      public DataStructure(long storage_time, int life_time, boolean exists, Opaque value){
      
         this.storage_time = storage_time;
         this.life_time = life_time;
         this.exists = exists;
         this.value = value;
         
         dictionary = false;
        
      }
      
      public DataStructure(){  // Empty, exists=false
      
      }
      
      public DictionaryKey getDictionaryKey(){
      
         return key;
      
      }
   
      public long getStorageTime(){
      
         return storage_time;
               
      }
      
      public int getLifeTime(){
      
            
         return life_time;
               
      }
      
      public boolean getExists(){
      
            
         return exists;
                    
      }
      
      public Opaque getValue(){
            
         return value;
               
      }
      
      public boolean isDictionary(){
      
         return dictionary;
      
      }
      
      public String print(){
      
         String ret = new String();
      
         if(dictionary){
            byte[] id = key.getKey();
            ret +="0x";
            for(int k=0; k<id.length; k++)
               ret += String.format("%02x", id[k]);
         }
         
         ret += "\t";
      	
         byte[] id = value.getContent();
         ret +="0x";
         for(int k=0; k<id.length; k++)
            ret += String.format("%02x", id[k]);
      
         ret += "\n";
      
         return ret;
      
      }
         
   }