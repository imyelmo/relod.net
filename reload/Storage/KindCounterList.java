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
   import java.util.*;
 
   public class KindCounterList{
   
      ArrayList<Integer> kind_id;
      ArrayList<ResourceId> resource_id;
      ArrayList<Long> gen_counter;
      
   	
      public KindCounterList(){
      
         kind_id = new ArrayList<Integer>();
         resource_id = new ArrayList<ResourceId>();
         gen_counter = new ArrayList<Long>();
      
      }
         
      public synchronized void add(KindId kid, ResourceId rid, long gen_counter) throws ReloadException{
      
         delete(kid, rid); // If exists, deletes old value, if not nothing happens
      
         kind_id.add(kid.getId());
         resource_id.add(rid);
         this.gen_counter.add(gen_counter);
      
      }
            
      public long getGenerationCounter(KindId kid, ResourceId rid) throws ReloadException{
            
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               return -1;
         	
            if(kind_id.get(pos).intValue() == kid.getId())
               return gen_counter.get(pos);
         
         }
               
      }
      
      public boolean contains(KindId kid, ResourceId rid) throws ReloadException{
            
         for(int ind=0, pos=0; true; ind=pos+1){
         
            pos = resourceIndexOf(rid, ind);
            
            if(pos == -1)
               return false;
               
            if(kind_id.get(pos).intValue() == kid.getId())
               return true;
               	
         }
      
      }
      
      public synchronized boolean delete(KindId kid, ResourceId rid) throws ReloadException{
            
         for(int ind=0, pos=0; true; ind=pos+1){
         
            pos = resourceIndexOf(rid, ind);
            
            if(pos == -1)
               return false;
               
            if(kind_id.get(pos).intValue() == kid.getId()){
               kind_id.remove(pos);
               resource_id.remove(pos);
               gen_counter.remove(pos);   
               return true;		
            }
               	
         }
               
      
      
      }
      
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