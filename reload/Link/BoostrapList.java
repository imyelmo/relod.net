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
   package reload.Link;
   
   import java.util.*;
   import java.net.InetAddress;
   import reload.Common.*;
	
   public class BoostrapList{
   
      private ArrayList<byte[]> address; // IpAddressPort
      private ArrayList<byte[]> node;
      private ArrayList<Integer> num_link; 
      
      public BoostrapList(){
      
         address = new ArrayList<byte[]>();
         node = new ArrayList<byte[]>();
         num_link = new ArrayList<Integer>();
      
      }
      
      public synchronized int addEntry(InetAddress addr) throws Exception{
      
         IpAddressPort ip = new IpAddressPort(addr, 0);
      
         byte[] onlyAddress = ip.getAddressBytes();
         int num = getFirstNumber();
         
         address.add(onlyAddress);
         node.add(null);
         num_link.add(num);
            
         return num;
      
      }
      
      public synchronized void addNode(NodeId node, int num){
      
         int pos = num_link.indexOf(num);
      
         this.node.set(pos, node.getBytes());
      
      }
      
      public int getNumLink(InetAddress addr) throws Exception{
      
         IpAddressPort ip = new IpAddressPort(addr, 0);
      
         byte[] Bip = ip.getAddressBytes();
      
         int pos = addrIndexOf(Bip);
         
         if(pos == -1)
            return -1;
         else
            return num_link.get(pos);
      
      } 
      
      public int getNumLink(NodeId id){
      
         byte[] Bid = id.getId();
      
         int pos = nodeIndexOf(Bid);
         
         if(pos == -1)
            return -1;
         else
            return num_link.get(pos);
      
      }
      
      public boolean exists(NodeId id){
      
         byte[] Bid = id.getId();
      
         int pos = nodeIndexOf(Bid);
         
         if(pos == -1)
            return false;
         else
            return true;
      
      }
         
      public synchronized boolean delete(int num){
      
         int del_num = -1; 
      
         del_num = num_link.indexOf(num);
             
         if(del_num == -1)
            return false;
             
         try{
            remove(del_num); 
         }
            catch(IndexOutOfBoundsException e){
               return false;
            }
         
         return true;
      
      }
      
      private void remove(int num){
      
         address.remove(num);
         node.remove(num);
         num_link.remove(num);
      
      }
      
      public synchronized IpAddressPort getAddressPort(int num) throws Exception{
         
         int pos = num_link.indexOf(num);
         
         if(pos == -1)
            return null;
         else
            return new IpAddressPort(address.get(pos), 0); // No remote port		
             
      }
      
      private synchronized int addrIndexOf(byte[] addr){
                      
         for (int i=0; i<address.size(); i++){
               
            if (Utils.equals(addr, address.get(i)))
               return i;
               
         }
        
         return -1;
      
      }
      
      private synchronized int nodeIndexOf(byte[] nod){
                      
         for (int i=0; i<node.size(); i++){
               
            if (Utils.equals(nod, node.get(i)))
               return i;
               
         }
        
         return -1;
      
      }
      
      private int getFirstNumber(){
      
         if(num_link.isEmpty())
            return 0;
         
         for (int i=0; true; i++){ // We are looking for i
            
            for (int j=0; j<num_link.size(); j++){
               
               if (num_link.get(j) == i)
                  break;
                  
               if(j == num_link.size()-1) // Last iteration
                  return i;
               
            }
            
         }
      
      }
      
   }