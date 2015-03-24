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
 
   public class ResourceKindList{
   
      private ArrayList<ResourceId> resource_id;
      private ArrayList<KindId> data;
      
   		
      public ResourceKindList(){
      
         resource_id = new ArrayList<ResourceId>();
         data = new ArrayList<KindId>();
      
      }
      
      public synchronized void add(ResourceId rid, KindId kid) throws ReloadException{
      
         if(!contains(kid, rid)){
            resource_id.add(rid);
            data.add(kid);
         }
      
      }
      
      public KindId[] getKinds(ResourceId rid) throws ReloadException{
      
         ArrayList<KindId> ret = new ArrayList<KindId>();
         
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
            
            else
               ret.add(data.get(pos));
               
         }
               
         return ret.toArray(new KindId[0]);
               
      }
      
      public synchronized void delete(ResourceId rid) throws ReloadException{
      
         for(int index=0, pos=0; true; index=pos){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               break;
            
            resource_id.remove(pos);
            data.remove(pos);
               
         }
         
      }
      
      public synchronized boolean delete(KindId kid, ResourceId rid) throws ReloadException{  
      		
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               return false;
               
            if(data.get(pos).getId() == kid.getId()){
               resource_id.remove(pos);
               data.remove(pos);
               return true;
            }
               	
         }
         
      }
      
      public boolean contains(KindId kid, ResourceId rid) throws ReloadException{
      
         for(int index=0, pos=0; true; index=pos+1){
         
            pos = resourceIndexOf(rid, index);
            
            if(pos == -1)
               return false;
            
            if(data.get(pos).getId() == kid.getId())
               return true;
               
         }
      
      }
      
      public boolean contains(ResourceId rid) throws ReloadException{
      
         if(resourceIndexOf(rid, 0) == -1)
            return false;
         
         else
            return true;	
      
      }
      
      public ResourceId[] getResources() throws ReloadException{
         
         ArrayList<ResourceId> list = new ArrayList<ResourceId>();
      
         for(int i=0; i<resource_id.size(); i++){
            if(!list.contains(resource_id.get(i)))
               list.add(resource_id.get(i));
            
         }
         
         return list.toArray(new ResourceId[0]);
      
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