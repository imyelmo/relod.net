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

   package reload.Storage;
	
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Storage.Data.*;
   import java.util.*;

/**
 * DictionaryTable class is a table to store dictionary data for the Storage module.
 * @author Malosa 
 * @version 0.1
 */	
   public class DictionaryTable{
   
      private ArrayList<Integer> kind_id; //indexed
      private ArrayList<ResourceId> resource_id; // indexed
      private ArrayList<Long> storage_time;
      private ArrayList<Integer> life_time;
      private ArrayList<byte[]> dictionary_key; //indexed
      private ArrayList<Boolean> exists;
      private ArrayList<Opaque> value; //32-bit
   
   /**
    * Creates a new empty table.
    */	
      public DictionaryTable(){
      
         kind_id = new ArrayList<Integer>();
         resource_id = new ArrayList<ResourceId>();
         storage_time = new ArrayList<Long>();
         life_time = new ArrayList<Integer>();
         dictionary_key = new ArrayList<byte[]>();
         exists = new ArrayList<Boolean>();
         value = new ArrayList<Opaque>();
        
      }
   
   /**
    * Adds a new row in the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param storage_time the storage time
    * @param life_time the life time
    * @param key the dictionary key
    * @param exists if value exists (false if it was deleted)
    * @param value the value to store
    */    
      public synchronized void add(KindId kid, ResourceId rid, long storage_time, int life_time, DictionaryKey key, boolean exists, byte[] value) throws ReloadException{
      
         delete(kid, rid, key); // If exists, deletes old value, if not, nothing happens
         
         Module.si.resource_kind.add(rid, kid);
      
         this.kind_id.add(kid.getId());
         this.resource_id.add(rid);
         this.storage_time.add(storage_time);
         this.life_time.add(life_time);
         dictionary_key.add(key.getKey());
         this.exists.add(exists);
         this.value.add(new Opaque(32, value));
      
      }
   
   /**
    * Adds a new file in the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param storage_time the storage time
    * @param life_time the life time
    * @param key the dictionary key
    * @param data_value the DataValue to store
    */    
      public synchronized void add(KindId kid, ResourceId rid, long storage_time, int life_time, DictionaryKey key, DataValue data_value) throws ReloadException{
      
         add(kid, rid, storage_time, life_time, key, data_value.getExists(), data_value.getValue());
      
      }
   
   /**
    * Returns some rows from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a DataStructure array containing the rows (each DataStructure object is a row)
    */     
      public DataStructure[] getData(KindId kid, ResourceId rid) throws ReloadException{
      
      
         int[] position = getPositions(kid, rid);
      
         if(position.length == 0)
            return new DataStructure[0];
      
         DataStructure[] ds = new DataStructure[position.length];
         
         DictionaryKey[] d = getInternalDictionaryKey(position);
         long[] s = getInternalStorageTime(position);
         int[] l = getInternalLifeTime(position);
         boolean[] e = getInternalExists(position);
         Opaque[] v = getInternalValue(position);
      	
      	   
         for(int i=0; i<position.length; i++)
            ds[i] = new DataStructure(d[i], s[i], l[i] ,e[i], v[i]);
      	   
         return ds;
      
      }
   
   /**
    * Returns a row from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param key the dictionary key
    * @return a DataStructure object containing the row
    */   
      public DataStructure getData(KindId kid, ResourceId rid, DictionaryKey key) throws ReloadException{
      
      
         DataStructure[] ds = getData(kid, rid);
         
         for(int i=0; i<ds.length; i++){
            if(Utils.equals(ds[i].getDictionaryKey().getKey(), key.getKey()))
               return ds[i];
         }
      	
         return null;
         
      }
   
   /**
    * Returns somes rows from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param key the keys of the values to return
    * @return a DataStructure array containing the rows
    */   
      public DataStructure[] getData(KindId kid, ResourceId rid, DictionaryKey[] key) throws ReloadException{ // Most useful
      
      
         DataStructure[] ds = getData(kid, rid);
         
         if(key.length == 0) // Wildcard fetch: all key-value pairs are returned
            return ds;
         
         ArrayList<DataStructure> ret = new ArrayList<DataStructure>();
         
         for(int i=0; i<key.length; i++){
         
            for(int j=0; j<ds.length; j++){
            
               if(Utils.equals(ds[j].getDictionaryKey().getKey(), key[i].getKey())){
                  ret.add(ds[j]);
                  break;
               }
               if(j == ds.length-1)
                  ret.add(new DataStructure()); // New value with Exists=false   
            }
               
         }
         
         if(ret.size() != key.length)
            throw new WrongLengthReloadException("Internal error.");
      	
         return ret.toArray(new DataStructure[0]);
         
      }
      
      private int[] getPositions(KindId kid, ResourceId rid) throws ReloadException{
      
         ArrayList<Integer> position = new ArrayList<Integer>();
         
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
         	
            if(kind_id.get(pos).intValue() == kid.getId())
               position.add(pos);
               
         }
            
         return toArray(position);
      	
      }
   
   /**
    * Returns the storage times of stored values.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a storage time array
    */    
      public long[] getStorageTime(KindId kid, ResourceId rid) throws ReloadException{
      
         int[] position = getPositions(kid, rid);
         
         if(position.length == 0)
            return new long[0]; 
         
         return getInternalStorageTime(position);
      
      }
      
      private long[] getInternalStorageTime(int[] position){
      
         long[] time = new long[position.length];
      
         for(int i=0; i<position.length; i++)
         
            time[i] = storage_time.get(position[i]);
              
         return time;
               
      }
   
   /**
    * Returns the storage time of a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param key the key of the value
    * @return the storage time
    */ 
      public long getStorageTime(KindId kid, ResourceId rid, DictionaryKey key) throws ReloadException{
      
      
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
         	
            byte[] dk = dictionary_key.get(pos);
         	
            if(Utils.equals(key.getKey(), dk) && kind_id.get(pos).intValue() == kid.getId())
               return storage_time.get(pos);
         
         }
            
         return -1;
      
      }
   
   /**
    * Returns the life time of stored values.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a life time array
    */  
      public int[] getLifeTime(KindId kid, ResourceId rid) throws ReloadException{
      
         int[] position = getPositions(kid, rid);
         
         if(position.length == 0)
            return new int[0]; 
         
         return getInternalLifeTime(position);
      
      }
      
      private int[] getInternalLifeTime(int[] position){
      
         int[] time = new int[position.length];
      
         for(int i=0; i<position.length; i++)
         
            time[i] = life_time.get(position[i]);
            
         return time;
               
      }
   
   /**
    * Returns the life time of a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param key the keys of the value
    * @return the life time
    */   
      public int getLifeTime(KindId kid, ResourceId rid, DictionaryKey key) throws ReloadException{
        
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
         	
            byte[] dk = dictionary_key.get(pos);
         	
            if(Utils.equals(key.getKey(), dk) && kind_id.get(pos).intValue() == kid.getId())
               return life_time.get(pos);
         
         }
            
         return -1;
               
      }
   
   /**
    * Returns the keys of stored values.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a dictionary key array
    */   
      public DictionaryKey[] getDictionaryKey(KindId kid, ResourceId rid) throws ReloadException{
      
         int[] position = getPositions(kid, rid);
         
         if(position.length == 0)
            return new DictionaryKey[0]; 
         
         return getInternalDictionaryKey(position);
      
      }
      
      private DictionaryKey[] getInternalDictionaryKey(int[] position) throws ReloadException{
      
         DictionaryKey[] dkey = new DictionaryKey[position.length];
      
         for(int i=0; i<position.length; i++)
         
            dkey[i] = new DictionaryKey(dictionary_key.get(position[i]), true);
            
         return dkey;
                  
      }
    
   /**
    * Returns if stored values exist.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return true if each vale exists, false if not
    */ 
      public boolean[] getExists(KindId kid, ResourceId rid) throws ReloadException{
      
         int[] position = getPositions(kid, rid);
         
         if(position.length == 0)
            return new boolean[0]; 
         
         return getInternalExists(position);
      
      }
      
      private boolean[] getInternalExists(int[] position){
      
         boolean[] exis = new boolean[position.length];
      
         for(int i=0; i<position.length; i++)
         
            exis[i] = exists.get(position[i]);
            
         return exis;
                  
      }
   
   /**
    * Returns if a stored value exists.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param key the key of the value
    * @return true if exists, false if not
    */    
      public boolean getExists(KindId kid, ResourceId rid, DictionaryKey key) throws ReloadException{
        
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
         	
            byte[] dk = dictionary_key.get(pos);
         	
            if(Utils.equals(key.getKey(), dk) && kind_id.get(pos).intValue() == kid.getId())
               return exists.get(pos);
         
         }
            
         return false;
               
      }
   
   /**
    * Returns stored values.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return the stored values in an Opaque array
    */     
      public Opaque[] getValue(KindId kid, ResourceId rid) throws ReloadException{
      
         int[] position = getPositions(kid, rid);
         
         if(position.length == 0)
            return new Opaque[0]; 
         
         return getInternalValue(position);
      
      }
      
      private Opaque[] getInternalValue(int[] position){
        
         Opaque[] val = new Opaque[position.length];
      
         for(int i=0; i<position.length; i++)
         
            val[i] = value.get(position[i]);
            
         return val;
                 
      }
   
   /**
    * Returns a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param key the key of the value
    * @return the stored value in an Opaque object
    */    
      public Opaque getValue(KindId kid, ResourceId rid, DictionaryKey key) throws ReloadException{
        
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
         	
            byte[] dk = dictionary_key.get(pos);
         	
            if(Utils.equals(key.getKey(), dk) && kind_id.get(pos).intValue() == kid.getId())
               return value.get(pos);
         
         }
            
         return null;
               
      }
   
   /**
    * Deletes a row from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param key the dictionary key
    * @return true if could be deleted
    */  
      public synchronized boolean delete(KindId kid, ResourceId rid, DictionaryKey key) throws ReloadException{ 
      		
         for(int ind=0, pos=0; true; ind=pos+1){
         
            pos = resourceIndexOf(rid, ind);
            
            if(pos == -1)
               return false;
               
            byte[] dk = dictionary_key.get(pos);
         
            if(Utils.equals(key.getKey(), dk) && kind_id.get(pos).intValue() == kid.getId()){
            
               remove(pos);   
               break;
            }
         		
         }
         
         if(!contains(kid, rid)){
            Module.si.resource_kind.delete(kid, rid);
         
            if(!Module.si.resource_kind.contains(rid))
               Module.si.resource_rep.delete(rid);
         }
         
         return true;
               
      }
   
   /**
    * Deletes some rows from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    */ 	    
      public synchronized void delete(KindId kid, ResourceId rid) throws ReloadException{  
      		
         for(int ind=0, pos=0; true; ind=pos+1){
         
            pos = resourceIndexOf(rid, ind);
            
            if(pos == -1)
               break;
               
            if(kind_id.get(pos).intValue() == kid.getId())
               remove(pos--);
               	
         }
         
         Module.si.resource_kind.delete(kid, rid);
      	
         if(!Module.si.resource_kind.contains(rid))
            Module.si.resource_rep.delete(rid);
               
      }
   
   /**
    * Deletes some rows from the table.
    * @param rid the Resource-ID
    */ 	    
      public synchronized void delete(ResourceId rid) throws ReloadException{
      
         for(int ind=0, pos=0; true; ind=pos+1){
         
            pos = resourceIndexOf(rid, ind);
            
            if(pos == -1)
               break;
               
            else
               remove(pos--);
               	
         }
         
         Module.si.resource_kind.delete(rid);
      	
         Module.si.resource_rep.delete(rid);
      
      }
      
      private void remove(int pos){
      
         kind_id.remove(pos);
         resource_id.remove(pos);
         storage_time.remove(pos);
         life_time.remove(pos);
         dictionary_key.remove(pos);
         exists.remove(pos);
         value.remove(pos);
      
      }
   
   /**
    * Returns if the table contains the specified element.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return true if contains, false if not
    */ 	  
      public boolean contains(KindId kid, ResourceId rid) throws ReloadException{
      
         for(int ind=0, pos=0; true; ind=pos+1){
         
            pos = resourceIndexOf(rid, ind);
            
            if(pos == -1)
               return false;
               
            if(kind_id.get(pos).intValue() == kid.getId())
               return true;
               	
         }
      
      }
   
   /**
    * Returns if the table contains the specified element.
    * @param rid the Resource-ID
    * @return true if contains, false if not
    */ 	    
      public boolean contains(ResourceId rid) throws ReloadException{
      
         if(resourceIndexOf(rid, 0) == -1)
            return false;
         
         else
            return true;	
      
      }
   
   /**
    * Returns the index of the first occurrence of the specified Resource-ID in the table, searching forwards from pos, or returns -1 if the element is not found.
    * @param o the Resource-ID
    * @param pos the index to start searching from
    * @return the index
    */ 
      public int resourceIndexOf(ResourceId o, int pos) throws ReloadException{
      
         ListIterator it;
      
         try{
            it = resource_id.listIterator(pos);
         }
            catch(IndexOutOfBoundsException ioobe){
               return -1;
            }
         
         for(int i=0; it.hasNext(); i++){
         
            ResourceId current = (ResourceId)it.next();
         
            if(current.equals(o))
               return pos+i;
         
         }
         
         return -1;
      
      }
   
      public static int[] toArray(ArrayList<Integer> list) {
      
         Integer[] inte = list.toArray(new Integer[0]);
      
         int[] ret = new int[inte.length];
      
         for (int i=0; i<inte.length; i++) 
            ret[i] = inte[i];
         
         return ret;
      
      }
      
      public String print() throws Exception{
      
         String ret = new String();
         int size = value.size();
         
         if(size == 0)
            ret += "<Empty>";
         else
            ret += "#\tKind-ID\tResource-ID\t\t\t\tKey\t\t\t\t\tValue\n"; 
      
         for(int i=0; i<size; i++){
         
            if(exists.get(i) == true){
            
               ret += (i+1) + "\t" + kind_id.get(i) + "\t0x";
               
               byte[] rid = resource_id.get(i).getId();
               for(int k=0; k<rid.length; k++)
                  ret += String.format("%02x", rid[k]);
                  
               ret += "\t0x";
            
               byte[] id = dictionary_key.get(i);
               for(int k=0; k<id.length; k++)
                  ret += String.format("%02x", id[k]);
                              
               ret += "\t0x";
            	
               id = value.get(i).getContent();
               for(int k=0; k<id.length; k++)
                  ret += String.format("%02x", id[k]);
                  
               ret += "\n";
            }
         
         }
      
         return ret;
      
      }
            
   }