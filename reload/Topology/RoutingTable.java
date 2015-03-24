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
   package reload.Topology;
   
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Topology.Plugins.Chord.ChordRoutingTable;
   import java.util.*;   

/**
* RoutingTable class is the Routing Table for the Topology Plugin module.
* @author Malosa 
* @version 0.1
*/	
   public class RoutingTable{
   
      private ChordRoutingTable chord;
      // private AhotherRoutingTable other_plugin;
   
      /**
    * Creates a new empty table.
    */
      public RoutingTable(){
      
         chord = new ChordRoutingTable();
      
      }
   
   /**
    * Establishes your own Node-ID.
    * @param id the node to be stored
    */   
      public void setThisNode(NodeId id){
      
         chord.setThisNode(id);
      
      }
      
      public boolean setFinger(NodeId id, int pos) throws ReloadException{
      
         return chord.setFinger(id, pos);
      
      }
      
      public boolean isPossibleNeighbor(NodeId node) throws ReloadException{
      
         return chord.isPossibleNeighbor(node);
      
      }
      
      public boolean addNeighborAndReturn(NodeId node) throws ReloadException{
      
         return chord.addNeighborAndReturn(node);
      
      }
      
      public void addNeighbor(NodeId node) throws ReloadException{
      
         chord.addNeighbor(node);
      
      }
      	      
      public boolean addNeighbors(NodeId[] node) throws ReloadException{
      
         return chord.addNeighbors(node);
      
      }
      
      public void addFingers(NodeId[] node) throws ReloadException{
      
         chord.addFingers(node);
      
      }
   
   /**
    * Deletes a node from the Routing Table. The node might be erased from the Finger Table and also from the Neighbor Table. 
    * @param node the node to be deleted
    * @return true if the node was erased
    */    
      public boolean delete(NodeId node) throws ReloadException{
      
         return chord.delete(node);
      
      }
      
   /**
    * Asks if a node is present in the Neighbor Table or in the Finger Table. 
    * @param node the node we are asking for
    * @return true if the node was found in the Routing Table, and also if it is your own Node-ID
    */    
      public boolean exists(NodeId node) throws ReloadException{
      
         return chord.exists(node);
      
      }
   
   /**
    * Checks if this node is responsible for a ID (typically, a Resource-ID). 
    * @param dest the ID from which we want to know the responsibility
    * @return true if the node is responsible
    */     
      public boolean isResponsible(Id dest) throws ReloadException{
      
         return chord.isResponsible(dest);
       	
      }
   
   /**
    * Checks if a joining node is responsible for a Resource-ID. This is only for a special case when a new Node-ID is joining the overlay. It does not work in the general case. 
    * @param resource the Resource-ID from which we want to know the responsibility
    * @param joining the responsible joining Node-ID
    * @return true if the joining node is responsible
    */     
      public boolean isResponsible(NodeId joining, ResourceId resource) throws ReloadException{ // Node is responsible from resource
      
         return chord.isResponsible(joining, resource);
      
      }
      
      public int getSuccessorLength(){
      
         return chord.getSuccessorLength();
        
      }
      
      public int getPredecessorLength(){
      
         return chord.getPredecessorLength();
        
      }
      	
      public int getFingerLength(){
      
         return chord.getFingerLength();
      
      }
      
      public int getCurrentFingerLength(){
      
         return chord.getCurrentFingerLength();
      
      }
      
      public NodeId getSuccessor(int i){
      
         return chord.getSuccessor(i);
      
      }
      
      public NodeId getPredecessor(int i){
      
         return chord.getPredecessor(i);
      
      }
      
      public NodeId getFinger(int i){
      
         return chord.getFinger(i);
      
      }
   
   /**
    * Returns your own Node-ID.
    * @return your own Node-ID
    */ 
      public NodeId getThisNode(){
      
         return chord.getThisNode();
      
      }
      
      public NodeId[] getNeighbors(){
      
         return chord.getNeighbors();
      
      }
        
      public ArrayList<NodeId> getNodes(){
      
         return chord.getNodes();
         
      }
      
      public NodeId[] getPredecessors(){
      
         return chord.getPredecessors();
       
      }  
   	
      public NodeId[] getSuccessors(){
      
         return chord.getSuccessors();
          
      }
      
      public NodeId[] getNodesForReplicas(){
      
         return chord.getTwoSuccessors();
      
      }
   	
      public NodeId[] getFingersForSending(){
      
         return chord.getFingersForSending();
       
      }
      
      public NodeId[] getFingers(){
      
         return chord.getFingers();
       
      }
      
      public String printNeighbors(){
      
         return chord.printNeighbors();
         
      }
      
      public String printFingers(){
      
         return chord.printFingers();
         
      }
      
      public String printId() throws Exception{
      
         return chord.printId();
           
      }
        
   }