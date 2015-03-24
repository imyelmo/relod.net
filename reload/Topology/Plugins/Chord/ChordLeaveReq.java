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