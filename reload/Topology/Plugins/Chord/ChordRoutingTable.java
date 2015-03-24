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
   import java.util.*;   

/**
* ChordRoutingTable class is the Routing Table in the RELOAD-Chord protocol.
* Neighbor Table and Finger Table are the two components of the Routing Table.
* It also stores your own Node-ID. This table stores NodeId objects.
* @author Malosa 
* @version 0.1
*/
   public class ChordRoutingTable{
   
   // My own Node ID
   
      private NodeId thisNodeId;
   
   
   // Neighbor Table
   
      private NodeId[] successor;
      private NodeId[] predecessor;
   
   
   // Finger Table
   
      private NodeId[] finger;
      		
      		// 0: 1/2
      		// 1: 1/4
      		// 2: 1/8
      		// 3: 1/16
      		// n: 1/2^(n+1)
   
   /**
    * Creates a new empty table.
    */
      public ChordRoutingTable(){
      
         successor = new NodeId[3];
         predecessor = new NodeId[3];
         finger = new NodeId[128];
            
      }
   
   /**
    * Establishes your own Node-ID.
    * @param id the node to be stored
    */   
      public void setThisNode(NodeId id){
      
         thisNodeId = id;
      
      }
   	
      private void setSuccessor(NodeId id, int pos){
      
         successor[pos] = id;
      
      }
      
      private void setPredecessor(NodeId id, int pos){
      
         predecessor[pos] = id;
      
      }
   
   /**
    * Establishes a finger in the Finger Table. It overrides the current value.
    * @param id the node to be stored
    * @param pos the position in the table, from 0 to 127
    * @return true if id does not exist in the Routing Table
    */     
      public boolean setFinger(NodeId id, int pos) throws ReloadException{
      
         if(exists(id))
            return false;
      
         finger[pos] = id;
         
         return true;
      
      }
   
   /**
    * It is used to ask if a node can be added to the Neighbor Table. It does not store at all.
    * @param node the node we are asking about
    * @return true if the node could be added at the present time
    */   
      public boolean isPossibleNeighbor(NodeId node) throws ReloadException{
      
         if(exists(node))
            return false;
            
         if(predecessor.length != getPredecessorLength() || successor.length != getSuccessorLength()) // Table not full
            return true;
                        
         NodeId[] candidate = getNeighborsAndCandidate(node);
      
         ArrayList<NodeId> list = orderPosAlgorithm(candidate);
         
         if(list.get(predecessor.length).equals(node)) // If the middle of the list is node
            return false;
         else
            return true;
      
      }
   
   /**
    * Tries to store a node into the Neighbor Table. It only will be added to the table if it is a better value than the stored nodes. 
    * @param node the node to be stored
    * @return true if the node were added
    */    
      public boolean addNeighborAndReturn(NodeId node) throws ReloadException{
      
         if(exists(node))
            return false;
      
         NodeId[] candidates = getNeighborsAndCandidate(node);
      
         return neighborAlgorithm(candidates, true);
      
      }
   
   /**
    * Tries to store a node into the Neighbor Table. It only will be added to the table if it has a closer distance than the stored nodes. There is not return value. 
    * @param node the node to be stored
    */     
      public void addNeighbor(NodeId node) throws ReloadException{
      
         if(exists(node))
            return;
            
         if(getSuccessorLength() == successor.length && getPredecessorLength() == predecessor.length)
            if(getCurrentFingerLength() != finger.length)
               Module.falm.createAttachFingersRoute();
      
         NodeId[] candidates = getNeighborsAndCandidate(node);
      
         neighborAlgorithm(candidates, false);
      
      }
      	      
      private boolean neighborAlgorithm(NodeId[] candidate, boolean isVoid) throws ReloadException{
      
         NodeId[] succ1 = getSuccessors().clone();
         NodeId[] pred1 = getPredecessors().clone();
      
         ArrayList<NodeId> list = orderAbsAlgorithm(candidate);
      
            	      		
         boolean pred_full = false, succ_full = false;
         ArrayList<NodeId> excess_p = new ArrayList<NodeId>();
         ArrayList<NodeId> excess_s = new ArrayList<NodeId>();
         
         int max;
         if(list.size() > (predecessor.length + successor.length))
            max = predecessor.length;
         else if(list.size()%2 == 0)
            max = list.size()/2;
         else 
            max = list.size()/2 + 1;
          
         int p=0, s=0;
         for(int i=0; i<list.size(); i++){	
         
            if(p == max)
               pred_full = true;
               
            if(s == max)
               succ_full = true;
         
            if(thisNodeId.distance(list.get(i)).signum() == -1 && !pred_full){
               setPredecessor(list.get(i), p);
               p++; 
            }
               
            else if(thisNodeId.distance(list.get(i)).signum() == 1 && !succ_full){
               setSuccessor(list.get(i), s);
               s++;
            }
            
            else if(thisNodeId.distance(list.get(i)).signum() == -1) // Excess
               excess_s.add(list.get(i));
            
            else // Excess
               excess_p.add(list.get(i));
         
         }
         
      	 
         if(excess_p.size() > 0 && !pred_full){
         
            for(int i=excess_p.size()-1; i>=0 && p<max; i--){
               
               setPredecessor(excess_p.get(i), p);
               p++;
            
            }
         
         }
         
         if(excess_s.size() > 0 && !succ_full){
         
            for(int i=excess_s.size()-1; i>=0 && s<max; i--){
            
               setSuccessor(excess_s.get(i), s);
               s++;
            
            }
         
         }
        
         if (isVoid){ 
         
            NodeId[] succ2 = getSuccessors();
            NodeId[] pred2 = getPredecessors();
         
         
            if(equals(pred1, succ1, pred2, succ2))
               return false;
            else
               return true;
               
         }
      		
         return false;
      
      }
      
      private ArrayList<NodeId> orderAbsAlgorithm(NodeId[] candidate){
      
         ArrayList<NodeId> list = new ArrayList<NodeId>();  
      	      
         boolean added;
         
         for(int i=0; i<candidate.length; i++){
         
            added = false;
            
            for(int j=0; j<list.size(); j++){
            
               if(thisNodeId.distanceAbs(candidate[i]).compareTo(thisNodeId.distanceAbs(list.get(j))) == -1){ //candidate[i] < list[j]
               
                  list.add(j, candidate[i]);
                  added = true;
                  break;
               }
            }
            
            if(!added)
               list.add(candidate[i]);
         	
         }
      
         if(list.size() != candidate.length)
            System.out.println("EXCEPTION! " + list.size() + " " + candidate.length);
            
         return list;
            
      }
      
      private ArrayList<NodeId> orderPosAlgorithm(NodeId[] candidate){
      
         ArrayList<NodeId> list = new ArrayList<NodeId>();  
      	      
         boolean added;
         
         for(int i=0; i<candidate.length; i++){
         
            added = false;
            
            for(int j=0; j<list.size(); j++){
            
               if(thisNodeId.distancePos(candidate[i]).compareTo(thisNodeId.distancePos(list.get(j))) == -1){ //candidate[i] < list[j]
               
                  list.add(j, candidate[i]);
                  added = true;
                  break;
               }
            }
            
            if(!added)
               list.add(candidate[i]);
         	
         }
      
         if(list.size() != candidate.length)
            System.err.println("EXCEPTION! " + list.size() + " " + candidate.length);
            
         return list;
            
      }
   
   /**
    * Tries to store some nodes into the Neighbor Table. They only will be added to the table if they have a closer distance than the stored nodes. 
    * @param node the nodes to be stored
    * @return true if all the nodes were added. If false some of the nodes might have been stored
    */     
      public boolean addNeighbors(NodeId[] node) throws ReloadException{
      
         boolean ret = true;
      
         for (int i=0; i<node.length; i++){
         
            if (!addNeighborAndReturn(node[i]))
               ret = false;
         
         }
      
         return ret;      
      }
   
   /**
    * Establishes the whole Finger Table. It overrides the current values. 
    * @param node the nodes to be stored. Array length must be 128
    */    
      public void addFingers(NodeId[] node) throws ReloadException{
      
         if(finger.length != node.length)
            throw new WrongLengthReloadException("Finger table is 128 length.");
      
         for (int i=0; i<node.length; i++){
            if(node != null)
               setFinger(node[i], i);   
         }
       
      }
      
      private boolean equals(NodeId[] n1, NodeId[] n2, NodeId[] m1, NodeId[] m2) throws ReloadException{
         
         if(n1.length != m1.length || n2.length != m2.length)
            return false;
        
         for(int i=0; i<n1.length; i++)
         
            if(!n1[i].equals(m1[i]))
               return false;
        
         for(int i=0; i<n2.length; i++)
         
            if(!n2[i].equals(m2[i]))
               return false;
         
         return true;
      
      }
      
   /**
    * Deletes a node from the Routing Table. The node might be erased from the Finger Table and also from the Neighbor Table. 
    * @param node the node to be deleted
    * @return true if the node was erased
    */      
      public boolean delete(NodeId node) throws ReloadException{
      
         int pos = getFinger(node);
      
         if(pos != -1){
            deleteFinger(pos);
            return true; 
         }
         
         else{
         
            if (deleteSuccessor(node))
               return true;
            if (deletePredecessor(node))
               return true;  
         }
      
         return false;
      
      }
      
      private boolean deleteSuccessor(NodeId node) throws ReloadException{
      
         for(int i=0; i<getSuccessorLength(); i++){
         
            if(successor[i].equals(node)){
               deleteSuccessor(i);
               return true;   
            }
         
         }
         
         return false;
      
      }
      
      private void deleteSuccessor(int pos){
      
         for (int i=pos; i<successor.length-1; i++)
            successor[i] = successor[i+1];
            
         successor[successor.length-1] = null;
      
      }
      
      private boolean deletePredecessor(NodeId node) throws ReloadException{
      
         for(int i=0; i<getPredecessorLength(); i++){
         
            if(predecessor[i].equals(node)){
               deletePredecessor(i);
               return true;
            }
         
         }
         
         return false;
      
      }
      
      private void deletePredecessor(int pos){
      
         for (int i=pos; i<predecessor.length-1; i++)
            predecessor[i] = predecessor[i+1];
            
         predecessor[predecessor.length-1] = null;
      
      }
      
      private void deleteFinger(int pos){
            
         finger[pos] = null;
      
      }
   
   /**
    * Asks if a node is present in the Neighbor Table or in the Finger Table. 
    * @param node the node we are asking for
    * @return true if the node was found in the Routing Table, and also if it is your own Node-ID
    */   
      public boolean exists(NodeId node) throws ReloadException{
      
         if(node.equals(thisNodeId))
            return true;
         
      
         for (int i=0; i<getPredecessorLength(); i++){
         
            if(node.equals(predecessor[i]))
               return true;
         
         }
      
         for (int i=0; i<getSuccessorLength(); i++){
         
            if(node.equals(successor[i]))
               return true;
         
         }
      
         for (int i=0; i<finger.length; i++){
         
            if(finger[i] != null)
               if(node.equals(finger[i]))
                  return true;
         
         }
         
         return false;
      
      }
   
   /**
    * Checks if this node is responsible for a ID (typically, a Resource-ID). 
    * @param resource the ID from which we want to know the responsibility
    * @return true if the node is responsible
    */     
      public boolean isResponsible(Id resource) throws ReloadException{
      
         NodeId thisID = getThisNode();
         NodeId previousID = getPredecessor(0);
         
         if(previousID == null){ 
         
            if(getSuccessor(0) != null) // Two nodes in the overlay
               previousID = getSuccessor(0);
            else								 // Only node in the overlay
               return true;
         }
      
         if(thisID.gt(previousID)){  // If thisID > previousID. If not, comparation is an OR ||
         
            if(resource.gt(previousID) && resource.lte(thisID)) // Responsible ID
               return true;
            
            else // Other ID
               return false;
         
         }
         
         else{ // thisId < previousID
         
            if(resource.gt(previousID) || resource.lte(thisID)) // Responsible ID
               return true;
            
            else // Other ID
               return false;
         
         }
      	
      }
   
   /**
    * Checks if a joining node is responsible for a Resource-ID. This is only for a special case when a new Node-ID is joining the overlay. It does not work in the general case. 
    * @param resource the Resource-ID from which we want to know the responsibility
    * @param joining the responsible joining Node-ID
    * @return true if the joining node is responsible
    */     
      public boolean isResponsible(NodeId joining, ResourceId resource) throws ReloadException{ // Node is responsible from resource
            
         NodeId thisID = getThisNode();
         
         NodeId previousID = getPredecessor(0);
         
         if(previousID == null){ // Only node in overlay
         
            if(getSuccessor(0) != null) // Two nodes in the overlay
               previousID = getSuccessor(0);
            else
               previousID = thisID; // You are your own predecessor
         }
      
         if(joining.gt(previousID)){  // If thisID > previousID. If not, comparation is an OR ||
         
            if(resource.gt(previousID) && resource.lte(joining)) // Responsible ID
               return true;
            
            else // Other ID
               return false;
         
         }
         
         else{ // thisId < previousID
         
            if(resource.gt(previousID) || resource.lte(joining)) // Responsible ID
               return true;
            
            else // Other ID
               return false;
         
         }
              	
      }
      
   /**
    * Returns the number of valid successors stored in the Neighbor Table. 
    * @return the number of successors stored
    */    
      public int getSuccessorLength(){
      
         for(int i=0; i<successor.length; i++){
         
            if(successor[i] == null)
               return i;
         
         }
         
         return successor.length;
      
      }
   
   /**
    * Returns the number of valid predecessors stored in the Neighbor Table. 
    * @return the number of predecessors stored
    */      
      public int getPredecessorLength(){
      
         for(int i=0; i<predecessor.length; i++){
         
            if(predecessor[i] == null)
               return i;
         
         }
         
         return predecessor.length;
      
      }
   
   /**
    * Returns the number of consecutive valid fingers from the Finger Table, starting from 0. 
    * @return the number of consecutive valid fingers
    */     
      public int getCurrentFingerLength(){
      
         for(int i=0; i<finger.length; i++){
         
            if(finger[i] == null)
               return i;
         
         }
         
         return finger.length;
      
      }
   
   /**
    * Returns the capacity of the Finger Table. In Chord, 128.
    * @return the number of fingers the Finger Table can store
    */  	
      public int getFingerLength(){
      
         return finger.length;
      
      }
   
   /**
    * Returns the successor at the specified position.
    * @param i index of the successor to return 
    * @return the successor at the specified position, null if it does not exist
    */    
      public NodeId getSuccessor(int i){
      
         return successor[i];
      
      }
   
   /**
    * Returns the predecessor at the specified position.
    * @param i index of the predecessor to return 
    * @return the predecessor at the specified position, null if it does not exist
    */    
      public NodeId getPredecessor(int i){
      
         return predecessor[i];
      
      }
   
   /**
    * Returns the finger at the specified position.
    * @param i index of the finger to return 
    * @return the finger at the specified position, null if it does not exist
    */    
      public NodeId getFinger(int i){
      
         return finger[i];
      
      }
   
   /**
    * Returns your own Node-ID.
    * @return your own Node-ID
    */ 
      public NodeId getThisNode(){
      
         return thisNodeId;
      
      }
   
   /**
    * Returns all the Neighbor Table, valid IDs only.
    * @return all the Neighbor Table values
    */    
      public NodeId[] getNeighbors(){
      
         int total = getPredecessorLength() + getSuccessorLength();
      
         NodeId[] ret = new NodeId[total];
      
         for (int i=0; i<getPredecessorLength(); i++)
            ret[i] = predecessor[i];
      
         for (int i=0; i<getSuccessorLength(); i++)
            ret[i+getPredecessorLength()] = successor[i];
            
         return ret;
      
      }
      
      private NodeId[] getNeighborsAndCandidate(NodeId candidate){
      
         int total = getPredecessorLength() + getSuccessorLength() + 1;
      
         NodeId[] ret = new NodeId[total];
      
         for (int i=0; i<getPredecessorLength(); i++)
            ret[i] = predecessor[i];
      
         for (int i=0; i<getSuccessorLength(); i++)
            ret[i+getPredecessorLength()] = successor[i];
            
         ret[total-1] = candidate;
            
         return ret;
      
      }
   
   /**
    * Returns all the Routing Table, valid IDs only. Neighbor Table and Finger Table.
    * @return all the Routing Table values
    */     
      public ArrayList<NodeId> getNodes(){ // Potencially not scalable
      
         ArrayList<NodeId> list = new ArrayList<NodeId>();
      
         for(int i=0; i<successor.length; i++){
         
            if(successor[i] != null)
               list.add(successor[i]);
         
         }
         
         for(int i=0; i<predecessor.length; i++){
         
            if(predecessor[i] != null)
               list.add(predecessor[i]);
         
         }
         
         for(int i=0; i<finger.length; i++){
         
            if(finger[i] != null)
               list.add(finger[i]);
         
         }
         
         return list;
      
      }
   
   /**
    * Returns predecessors, valid IDs only.
    * @return all valid predecessors values
    */    
      public NodeId[] getPredecessors(){
      
         if(predecessor[2] != null)
            return predecessor;
          
         int num = getPredecessorLength();
         NodeId[] ret = new NodeId[num];
      	   
         for(int i=0; i<num; i++)
            ret[i] = predecessor[i];
            
         return ret;
      
      }  
   
   /**
    * Returns successors, valid IDs only.
    * @return all valid successors values
    */ 	
      public NodeId[] getSuccessors(){
      
         if(successor[2] != null)
            return successor;
            
         int num = getSuccessorLength();
         NodeId[] ret = new NodeId[num];
      	   
         for(int i=0; i<num; i++)
            ret[i] = successor[i];
            
         return ret;
      
      }
      
   /**
    * Returns two successors, valid IDs only.
    * @return two first successors values
    */ 	
      public NodeId[] getTwoSuccessors(){
      
         if(successor[0] == null)
            return new NodeId[0];
            
         NodeId[] ret;
            
         if(successor[1] != null){
            ret = new NodeId[2];
            ret[1] = successor[1];
         }
         
         else
            ret = new NodeId[1];
      
         ret[0] = successor[0];   
            
         return ret;
      
      }
   
   /**
    * Returns the consecutive valid fingers, starting from 0.
    * @return the consecutive valid fingers
    */ 
      public NodeId[] getFingersForSending(){
      
         int num = getCurrentFingerLength();
         NodeId[] ret = new NodeId[num];
      	   
         for(int i=0; i<num; i++)
            ret[i] = finger[i];
            
         return ret;
      
      }
   
   /**
    * Returns all Finger Table, including null values.
    * @return all Finger Table
    */    
      public NodeId[] getFingers(){
      
         return finger;
      
      }
      
      private int getFinger(NodeId node) throws ReloadException{
      
         for(int i=0; i<finger.length; i++){
         
            if(finger[i] != null)
               if(finger[i].equals(node))
                  return i;
         
         }
         
         return -1;
      
      }
   
   /**
    * Returns a String with the neighbors for printing.
    * @return a String with the neighbors
    */     
      public String printNeighbors(){
      
         String ret = new String();
         
         ret += "Predecessors:\n";
      
         for(int i=0; i<predecessor.length; i++)
         
            if(predecessor[i] != null)
               ret += i + ". " + predecessor[i].print() + "\n";
            
            else
               ret += i + ". None\n";
               
         ret += "\nSuccessors:\n";
               
         for(int i=0; i<successor.length; i++)
         
            if(successor[i] != null)
               ret += i + ". " + successor[i].print() + "\n";
            
            else
               ret += i + ". None\n";
                  
         return ret; 
      
      }
   
   /**
    * Returns a String with the fingers for printing.
    * @return a String with the fingers
    */    
      public String printFingers(){
      
         String ret = new String();
         
         ret += "Fingers:\n";
         
         if(getCurrentFingerLength() == 0)
            ret += "<none>\n";
      
         for(int i=0; i<finger.length; i++)
         
            if(finger[i] != null)
               ret += i + ". " + finger[i].print() + "\n";
            
         return ret; 
      
      }
   
   /**
    * Returns a String for printing with your own Node-ID and the Resources-ID of your responsibility.
    * @return a String with your Node-ID and your Resources-ID
    */     
      public String printId() throws Exception{
      
         String ret = new String();
         
         ret += "Your Node-ID:\n";
         ret += thisNodeId.print() + "\n";
         
         NodeId previousID = getPredecessor(0);
         
         if(previousID == null && getSuccessor(0) != null) // Two nodes in the overlay
            previousID = getSuccessor(0);
      	
         if(previousID != null){    
            ret += "Responsible for Resources-ID from:\n";
            ret += previousID.sum(1).print() + "\n";
            ret += "To:\n";
            ret += thisNodeId.print() + "\n";
         }
         
         else
            ret += "Responsible for the whole overlay.\n";
            
         return ret; 
      
      }
   
   }