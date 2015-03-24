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
   import java.io.*;
   
   
   public class ChordLeaveReq{
   
      private NodeId leaving_peer_id;
      private ChordLeaveData leaveData;
   
      
      public ChordLeaveReq (NodeId leaving_peer_id, ChordLeaveData leaveData){
      
         this.leaving_peer_id = leaving_peer_id;
         this.leaveData = leaveData;
      
      }
      
      public ChordLeaveReq (byte[] data) throws Exception{
      
         leaving_peer_id = new NodeId(data, false); // Lower
         int length = leaving_peer_id.getBytes().length;
         leaveData = new ChordLeaveData(Utils.cutArray(data, length));
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
      
         baos.write(leaving_peer_id.getBytes());
         baos.write(leaveData.getBytes());
      	
         return baos.toByteArray();
      
      }
   
      public NodeId getLeavingPeerId(){
      
         return leaving_peer_id;
      
      }
      
      public ChordLeaveData getLeavingData(){
      
         return leaveData;
      
      }
   
   }