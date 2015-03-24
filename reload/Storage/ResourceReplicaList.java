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
 
   public class ResourceReplicaList{
   
      private ArrayList<ResourceId> resource_id;
      private ArrayList<Byte> replica;
      
   	
      public ResourceReplicaList(){
      
         resource_id = new ArrayList<ResourceId>();
         replica = new ArrayList<Byte>();
      
      }
      
      public synchronized void add(ResourceId id, byte replica) throws ReloadException{
      
         if(contains(id))
            delete(id);
                
         resource_id.add(id);
         this.replica.add(replica);
      
      }
      
      public byte getReplica(ResourceId rid) throws ReloadException{
        
         int pos = resourceIndexOf(rid);
            
         if(pos == -1)
            return -1; 
               
         else 
            return replica.get(pos);
               
      }
      
      public ResourceId[] getResources() throws ReloadException{
         
         return resource_id.toArray(new ResourceId[0]);
      
      }
      
      public synchronized boolean delete(ResourceId rid){
      
         int pos = resource_id.indexOf(rid.getId());
            
         if(pos == -1)
            return false; 
               
         resource_id.remove(pos);
         replica.remove(pos);
         
         return true;
      
      }
      
      public boolean contains(ResourceId rid) throws ReloadException{
      
         if(resourceIndexOf(rid) == -1)
            return false;
         
         else
            return true;	
      
      }
      
      public int resourceIndexOf(ResourceId o) throws ReloadException{
      
         int pos = 0;
      
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
      
      public String print() throws Exception{
      
         String ret = new String();
         int size = resource_id.size();
         
         if(size == 0)
            ret += "<Empty>";
         else
            ret += "#\tResource-ID\t\t\t\tReplica\n"; 
      
         for(int i=0; i<size; i++){
         
            ret += (i+1) + "\t0x";
               
            byte[] rid = resource_id.get(i).getId();
            for(int k=0; k<rid.length; k++)
               ret += String.format("%02x", rid[k]);
                  
            ret += "\t0x";
            
            byte rep = replica.get(i);
            ret += rep;
               
            ret += "\n";
            
         
         }
      
         return ret;
      
      }
         
   }