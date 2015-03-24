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
   package reload.Topology.Plugins.Chord;
   
   import reload.Common.*;   
   import reload.Common.Exception.*;
   import reload.Topology.*;
   import java.util.ArrayList;
   import java.math.BigInteger;
	
   public class ChordRouting{
   
   
      public NodeId route(NodeId thisId, Id destId, ArrayList<NodeId> list) throws Exception{
      
         BigInteger min = new BigInteger(1, NodeId.ones); // MAX_VALUE
         NodeId best = null;
      
      // We find the min distance from the peer to the destId     	            
         for(int i=0; i<list.size(); i++){
               
            BigInteger distance = list.get(i).distancePos(destId);
                  
            if(distance.compareTo(min) == -1){
               best = list.get(i);
               min = distance;   
            }
                             
         }
                        
      // If my distance to destId is lower than found, we have not a valid node.
         if(thisId.distancePos(destId).compareTo(min) == -1){
         
            BigInteger max = BigInteger.ZERO; 
            
         // We find the max positive distance (the smallest greater than destId)
            for(int i=0; i<list.size(); i++){
                     
               BigInteger distance = list.get(i).distancePos(destId);
                  
               if(distance.compareTo(max) == 1){
                  best = list.get(i);
                  max = distance;   
               }       
               
            }
               
         }
      	
         return best;
      
      }
      
      public ResourceId closest(ResourceId[] value, ResourceId ideal){
      
         BigInteger diff = new BigInteger(1, ResourceId.ones); // MAX_VALUE
         ResourceId best = null;
      
         for(int i=0; i<value.length; i++){
         
            if(ideal.distanceAbs(value[i]).compareTo(diff) == -1){
               diff = ideal.distanceAbs(value[i]);
               best = value[i];   
            }
         
         }
         
         return best;
      
      }
      
      private NodeId closest(Id destID, ArrayList<NodeId> list){
      
         BigInteger diff = new BigInteger(1, NodeId.ones); // MAX_VALUE
         NodeId best = null;
      
         for(int i=0; i<list.size(); i++){
         
            if(destID.distanceAbs(list.get(i)).compareTo(diff) == -1) {
               diff = destID.distanceAbs(list.get(i));
               best = list.get(i);
            }
         
         }
         
         return best;
      
      }  
      
      public ResourceId distanceFinger(NodeId thisNode, int i) throws Exception{
      
         int finger = i+1;
      
         BigInteger dist = BigInteger.valueOf(2);
         dist = dist.pow(128-finger);
         
         byte[] IdBytes = thisNode.getId();
         ResourceId res = new ResourceId(IdBytes, true);
         res.add(dist);
         
         return res;
      
      }
         
   }