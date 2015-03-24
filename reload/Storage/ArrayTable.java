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
 * DictionaryTable class is a table to store array data for the Storage module.
 * @author Malosa 
 * @version 0.1
 */		
   public class ArrayTable{
   
      private ArrayList<Integer> kind_id; //indexed
      private ArrayList<ResourceId> resource_id; // indexed
      private ArrayList<Long> storage_time;
      private ArrayList<Integer> life_time;
      private ArrayList<Integer> index; //indexed
      private ArrayList<Boolean> exists;
      private ArrayList<Opaque> value; //32-bit
   
   /**
    * Creates a new empty table.
    */
      public ArrayTable(){
      
         kind_id = new ArrayList<Integer>();
         resource_id = new ArrayList<ResourceId>();
         storage_time = new ArrayList<Long>();
         life_time = new ArrayList<Integer>();
         index = new ArrayList<Integer>();
         exists = new ArrayList<Boolean>();
         value = new ArrayList<Opaque>();
        
      }
   
   /**
    * Adds a new row in the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param storage_time the storage time
    * @param life_time the life time
    * @param index the index of the array
    * @param exists if value exists (false if it was deleted)
    * @param value the value to store
    */    
      public synchronized void add(KindId kid, ResourceId rid, long storage_time, int life_time, int index, boolean exists, byte[] value) throws ReloadException{
      
         if (index == (int)0xffffffff)
            index = getArrayLength(kid, rid);
      
         delete(kid, rid, index); // If exists, deletes old value, if not nothing happens
      
         this.kind_id.add(kid.getId());
         this.resource_id.add(rid);
         this.storage_time.add(storage_time);
         this.life_time.add(life_time);
         this.index.add(index);
         this.exists.add(exists);
         this.value.add(new Opaque(32, value));
      
      }
   
   /**
    * Adds a new file in the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param storage_time the storage time
    * @param life_time the life time
    * @param index the index of the array
    * @param data_value the DataValue to store
    */       
      public synchronized void add(KindId kid, ResourceId rid, long storage_time, int life_time, int index, DataValue data_value) throws ReloadException{
      
         add(kid, rid, storage_time, life_time, index, data_value.getExists(), data_value.getValue());
      
      }
   
   /**
    * Returns some rows from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a DataStructure array containing the rows (each DataStructure object is a row)
    */    
      public DataStructure[] getData(KindId kid, ResourceId rid) throws ReloadException{
      
         int max = getArrayLength(kid, rid);
         
         if(max == -1)
            return new DataStructure[0];
            
         int size = max+1;
         
         DataStructure[] ds = new DataStructure[size];
      	
         long[] s = getInternalStorageTime(kid, rid, size);
         int[] l = getInternalLifeTime(kid, rid, size);
         boolean[] e = getInternalExists(kid, rid, size);
         Opaque[] v = getInternalValue(kid, rid, size);
         
         for(int i=0; i<size; i++)
            ds[i] = new DataStructure(s[i], l[i] ,e[i], v[i]);
      	   
         return ds;
      
      }
   
   /**
    * Returns a row from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param index the index of the array
    * @param allowNull allows null values 
    * @return a DataStructure object containing the row
    */   
      public DataStructure getData(KindId kid, ResourceId rid, int index, boolean allowNull) throws ReloadException{
      
         DataStructure ds = getData(kid, rid)[index];
      
         if(allowNull && ds.getValue() == null)
            return null;
         else
            return ds;
      
      }
         
   /**
    * Returns somes rows from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param index the indexes of the values to return
    * @return a DataStructure array containing the rows
    */     
      public DataStructure[] getData(KindId kid, ResourceId rid, int[] index) throws ReloadException{ // Most useful
      
         DataStructure[] data = getData(kid, rid);
         
         DataStructure[] ret = new DataStructure[index.length];
         
         for(int i=0; i<ret.length; i++)
            ret[i] = data[index[i]]; 
               
         return ret;
      
      }
      
      private int getArrayLength(KindId kid, ResourceId rid) throws ReloadException{ // Real length is return+1
           
         int max = -1;
         
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
               
            if(kind_id.get(pos).intValue() == kid.getId()){
            
               int ind = this.index.get(pos);
            
               if(ind>max)
                  max = ind;
            }
         
         }
         
         return max;
      	
      }
   
   /**
    * Returns the storage times of stored values.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a storage time array
    */        
      public long[] getStorageTime(KindId kid, ResourceId rid) throws ReloadException{
      
         int max = getArrayLength(kid, rid);
         
         if(max == -1)
            return new long[0]; 
         
         return getInternalStorageTime(kid, rid, max+1);
      
      }
      
      private long[] getInternalStorageTime(KindId kid, ResourceId rid, int size) throws ReloadException{
      
          
         long[] time = new long[size];
      
      
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
         	
            if(kind_id.get(pos).intValue() == kid.getId()){
            
               long st = storage_time.get(pos);
               int ind = this.index.get(pos);
            
               time[ind] = st;
            }
         
         }
            
         return time;
               
      }
   
   /**
    * Returns the storage time of a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param index the index of the value
    * @return the storage time
    */    
      public long getStorageTime(KindId kid, ResourceId rid, int index) throws ReloadException{
      
         return getStorageTime(kid, rid)[index];
      
      }
   
   /**
    * Returns the life time of stored values.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a life time array
    */    
      public int[] getLifeTime(KindId kid, ResourceId rid) throws ReloadException{
      
         int max = getArrayLength(kid, rid);
         
         if(max == -1)
            return new int[0]; 
         
         return getInternalLifeTime(kid, rid, max+1);
      
      }
      
      private int[] getInternalLifeTime(KindId kid, ResourceId rid, int size) throws ReloadException{
      
         
         int[] time = new int[size];
      
      
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
               
            if(kind_id.get(pos).intValue() == kid.getId()){
            
               int lt = life_time.get(pos);
               int ind = this.index.get(pos);
            
               time[ind] = lt;
            }
         
         }
            
         return time;
               
      }
   
   /**
    * Returns the life time of a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param index the index of the value
    * @return the life time
    */    
      public int getLifeTime(KindId kid, ResourceId rid, int index) throws ReloadException{
        
         return getLifeTime(kid, rid)[index];
               
      }
   
   /**
    * Returns if stored values exist.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return true if each vale exists, false if not
    */        
      public boolean[] getExists(KindId kid, ResourceId rid) throws ReloadException{
      
         int max = getArrayLength(kid, rid);
         
         if(max == -1)
            return new boolean[0]; 
         
         return getInternalExists(kid, rid, max+1);
      
      }
      
      private boolean[] getInternalExists(KindId kid, ResourceId rid, int size) throws ReloadException{
      
      	
         boolean[] exis = new boolean[size];
      
      
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
               
            if(kind_id.get(pos).intValue() == kid.getId()){
            
               boolean ex = exists.get(pos);
               int ind = this.index.get(pos);
            
               exis[ind] = ex;
            }
         
         }
            
         return exis;
               
      }
   
   /**
    * Returns if a stored value exists.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param index the index of the value
    * @return true if exists, false if not
    */        
      public boolean getExists(KindId kid, ResourceId rid, int index) throws ReloadException{
        
         return getExists(kid, rid)[index];
               
      }
   
   /**
    * Returns stored values.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return the stored values in an Opaque array
    */      
      public Opaque[] getValue(KindId kid, ResourceId rid) throws ReloadException{
      
         int max = getArrayLength(kid, rid);
         
         if(max == -1)
            return new Opaque[0]; 
         
         return getInternalValue(kid, rid, max+1);
      
      }
   
      private Opaque[] getInternalValue(KindId kid, ResourceId rid, int size) throws ReloadException{
      
         
         Opaque[] val = new Opaque[size];
      
      
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
         	
            if(kind_id.get(pos).intValue() == kid.getId()){
            
               Opaque opaque = value.get(pos);
               int ind = this.index.get(pos);
            
               val[ind] = opaque;
            
            }
         
         }
            
         return val;
               
      }
   
   /**
    * Returns a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param index the index of the value
    * @return the stored value in an Opaque object
    */      
      public Opaque getValue(KindId kid, ResourceId rid, int index) throws ReloadException{
        
         return getValue(kid, rid)[index];
               
      }
   
   /**
    * Deletes a row from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param index the index of the array
    * @return true if could be deleted
    */   
      public synchronized boolean delete(KindId kid, ResourceId rid, int index) throws ReloadException{
          		
         for(int ind=0, pos=0; true; ind=pos+1){
         
            pos = resourceIndexOf(rid, ind);
            
            if(pos == -1)
               return false;
         	
            if(kind_id.get(pos).intValue() == kid.getId() && this.index.get(pos) == index){
            
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
         storage_time.remove(pos);
         life_time.remove(pos);
         index.remove(pos);
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
      
   }