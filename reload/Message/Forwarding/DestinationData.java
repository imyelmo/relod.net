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
   package reload.Message.Forwarding;
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class DestinationData{
   
      private NodeId node_id;
      private ResourceId resource_id;
      private Opaque opaque_id; // <0..255>
      
      private DestinationType destination_type;
     
      
      public DestinationData(NodeId node_id){
      
         this.node_id = node_id;
         destination_type = DestinationType.node;
      }
   	
      public DestinationData(ResourceId resource_id){
      
         this.resource_id = resource_id;
         destination_type = DestinationType.resource;
      }
      
      public DestinationData(Id id) throws ReloadException{
      
         destination_type = id.getType();
         
         if(destination_type == DestinationType.node)
            node_id = (NodeId)id;
         
         else if(destination_type == DestinationType.resource)
            resource_id = (ResourceId)id;
            
         else
            throw new WrongTypeReloadException();
      	
      }
   	
      public DestinationData(Opaque opaque_id){
      
         this.opaque_id = opaque_id;
         destination_type = DestinationType.opaque_id_type;
      }
      
      public DestinationData(byte[] data, DestinationType destination_type) throws Exception{
      
         this.destination_type = destination_type;
      
         switch(destination_type.getBytes()){
         
            case 1:
               node_id = new NodeId(data, false); // Lower
               break;
            case 2:
               resource_id = new ResourceId(data, false); // Lower
               break;
            case 3:
               opaque_id = new Opaque(8, data, 0);
               break;
            default:
               throw new WrongTypeReloadException();
         
         }
      }
      
      public byte[] getBytes() throws Exception{
      
         switch(destination_type.getBytes()){
         
            case 1:
               return node_id.getBytes();
            case 2:
               return resource_id.getBytes();
            case 3:
               return opaque_id.getBytes();
            default:
               throw new WrongTypeReloadException();
         
         }
      }
      
      public NodeId getNodeId() throws ReloadException{
      
         if(destination_type == DestinationType.node)
            return node_id;
         else
            throw new WrongTypeReloadException();
      
      }
   	
      public ResourceId getResourceId() throws ReloadException{
      
         if(destination_type == DestinationType.resource)
            return resource_id;
         else
            throw new WrongTypeReloadException();
      
      }
      
      public Id getId() throws ReloadException{
         
         if(destination_type == DestinationType.resource)  
            return resource_id;
      
         if(destination_type == DestinationType.node)
            return node_id;
            
         throw new WrongTypeReloadException();
            
      }
   	
      public byte[] getCompressedId() throws Exception{
      
         if(destination_type == DestinationType.opaque_id_type)
            return opaque_id.getContent();
         else
            throw new WrongTypeReloadException();
      
      }
      
   }