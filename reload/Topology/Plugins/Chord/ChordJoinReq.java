   package reload.Topology.Plugins.Chord;
   
   import reload.Common.*;
   import java.io.*;
   
   public class ChordJoinReq{
   
      private NodeId joining_peer_id;
   
   
      
      public ChordJoinReq (NodeId joining_peer_id){
      
         this.joining_peer_id = joining_peer_id;
      
      }
      
      public ChordJoinReq (byte[] data) throws Exception{
      
         joining_peer_id = new NodeId(data, false); // Lower
      
      }
      
      public byte[] getBytes() throws IOException{
      
         
         return joining_peer_id.getBytes();
      
      }
      
      public NodeId getJoiningPeerId(){
      
         return joining_peer_id;
      
      }
   
   
   }