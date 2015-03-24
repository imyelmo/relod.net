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
	
   package reload.Forwarding;
	
   import reload.Common.*;
   import java.util.*;
   import reload.Common.Exception.*;
 
   public class SendData{
   
      private ArrayList<Integer> num_link;
      private ArrayList<Boolean> client;
      private ArrayList<byte[]> data;
      
   	
      public SendData(){
      
         num_link = new ArrayList<Integer>();
         client = new ArrayList<Boolean>();
         data = new ArrayList<byte[]>();
      
      }
      
      public synchronized void add(int num_link, boolean client, byte[] data){
         
         this.num_link.add(num_link);
         this.client.add(client);
         this.data.add(data);
         
      }
      
      public byte[] getData(int num, boolean client) throws ReloadException{
        
         int pos1 = num_link.indexOf(num);
         int pos2 = num_link.lastIndexOf(num);
            
         if(pos1 == -1)
            return null; 
            
         if(pos1 == pos2){
            boolean cli = this.client.get(pos1);
            
            if(cli==client)
               return data.get(pos1);
            else
               return null;
         }
      
         if(this.client.get(pos1) == client)
            return data.get(pos1);
         else if (this.client.get(pos2) == client)
            return data.get(pos2);
         else
            throw new ReloadException("Incorrect data, duplicate boolean value.");
      	               
      }
        
      public synchronized boolean delete(int num, boolean client) throws ReloadException{
      
         int pos1 = num_link.indexOf(num);
         int pos2 = num_link.lastIndexOf(num);
            
         if(pos1 == -1)
            return false; 
            
         if(pos1 == pos2){
            boolean cli = this.client.get(pos1);
            
            if(cli==client){
               remove(pos1);
               return true;
            }
            else
               return false;
         }
         
         if(this.client.get(pos1) == client)
            remove(pos1);
         else if (this.client.get(pos2) == client)
            remove(pos2);
         else
            throw new ReloadException("Incorrect data, duplicate boolean value.");
            
         return true;
      
      }
      
      public void remove(int pos){
      
         num_link.remove(pos);
         client.remove(pos);
         data.remove(pos);
         
      }
      
      public boolean contains(int num, boolean client) throws ReloadException{
      
         if(getData(num, client) == null)
            return false;
         
         else
            return true;	
      
      }
     
   }