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
 * DictionaryTable class is a table to store single value data for the Storage module.
 * @author Malosa 
 * @version 0.1
 */	 
   public class SingleValueTable{
   
      private ArrayList<Integer> kind_id;
      private ArrayList<ResourceId> resource_id;
      private ArrayList<Long> storage_time;
      private ArrayList<Integer> life_time;
      private ArrayList<Boolean> exists;
      private ArrayList<Opaque> value; //32-bit
   
   /**
    * Creates a new empty table.
    */
      public SingleValueTable(){
      
         kind_id = new ArrayList<Integer>();
         resource_id = new ArrayList<ResourceId>();
         storage_time = new ArrayList<Long>();
         life_time = new ArrayList<Integer>();
         exists = new ArrayList<Boolean>();
         value = new ArrayList<Opaque>();
        
      }
   
   /**
    * Adds a new row in the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param storage_time the storage time
    * @param life_time the life time
    * @param exists if value exists (false if it was deleted)
    * @param value the value to store
    */    
      public synchronized void add(KindId kid, ResourceId rid, long storage_time, int life_time, boolean exists, byte[] value) throws ReloadException{
      
         delete(kid, rid); // If exists, deletes old value, if not nothing happens
      
         this.kind_id.add(kid.getId());
         this.resource_id.add(rid);
         this.storage_time.add(storage_time);
         this.life_time.add(life_time);
         this.exists.add(exists);
         this.value.add(new Opaque(32, value));
      
      }
   
   /**
    * Adds a new file in the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @param storage_time the storage time
    * @param life_time the life time
    * @param data_value the DataValue to store
    */     
      public synchronized void add(KindId kid, ResourceId rid, long storage_time, int life_time, DataValue data_value) throws ReloadException{
      
         add(kid, rid, storage_time, life_time, data_value.getExists(), data_value.getValue());
      
      }
   
   /**
    * Returns a row from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return a DataStructure object containing the row
    */    
      public DataStructure getData(KindId kid, ResourceId rid) throws ReloadException{
      
         long s = getStorageTime(kid, rid);
         int l = getLifeTime(kid, rid);
         boolean e = getExists(kid, rid);
         Opaque v = getValue(kid, rid);
      
         return new DataStructure(s, l, e, v);
      
      }
   
   /**
    * Returns the storage time of a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return the storage time
    */      
      public long getStorageTime(KindId kid, ResourceId rid) throws ReloadException{
        
         int pos = indexOf(kid, rid);
            
         if(pos == -1)
            return -1; 
               
         else 
            return storage_time.get(pos);
               
      }
   
   /**
    * Returns the life time of a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return the life time
    */       
      public int getLifeTime(KindId kid, ResourceId rid) throws ReloadException{
        
         int pos = indexOf(kid, rid);
            
         if(pos == -1)
            return -1; 
               
         else 
            return life_time.get(pos);
               
      }
   
   /**
    * Returns if a stored value exists.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return true if exists, false if not
    */     
      public boolean getExists(KindId kid, ResourceId rid) throws ReloadException{
        
         int pos = indexOf(kid, rid);
            
         if(pos == -1)
            return false; 
               
         else 
            return exists.get(pos);
               
      }
      
   /**
    * Returns a stored value.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return the stored value in an Opaque object
    */     
      public Opaque getValue(KindId kid, ResourceId rid) throws ReloadException{
        
         int pos = indexOf(kid, rid);
            
         if(pos == -1)
            return null; 
               
         else 
            return value.get(pos);
               
      }
   
   /**
    * Deletes a row from the table.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return true if could be deleted
    */ 	   
      public synchronized boolean delete(KindId kid, ResourceId rid) throws ReloadException{
      
         int pos = indexOf(kid, rid);
            
         if(pos == -1)
            return false; 
               
         remove(pos);
         
         Module.si.resource_kind.delete(kid, rid);
      	
         if(!Module.si.resource_kind.contains(rid))
            Module.si.resource_rep.delete(rid);
         
         return true;
      
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
         exists.remove(pos);
         value.remove(pos);
         
      }
     
   /**
    * Returns the index of the first occurrence of the specified element in the table, or -1 if the element is not found.
    * @param kid the Kind-ID
    * @param rid the Resource-ID
    * @return the index
    */ 		 
      public int indexOf(KindId kid, ResourceId rid) throws ReloadException{
      
         ListIterator it;
      
         try{
            it = resource_id.listIterator();
         }
            catch(IndexOutOfBoundsException ioobe){
               return -1;
            }
         
         for(int i=0; it.hasNext(); i++){
         
            ResourceId current = (ResourceId)it.next();
         
            if(current.equals(rid))
               if(kind_id.get(i) == kid.getId())
                  return i;
         
         }
         
         return -1;
      
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