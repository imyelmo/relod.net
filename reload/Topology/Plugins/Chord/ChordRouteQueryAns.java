   package reload.Topology.Plugins.Chord;
   
   import reload.Common.*;
   import java.io.*;
   
   
   public class ChordRouteQueryAns{
   
   
      private NodeId next_peer;
   
      
      public ChordRouteQueryAns (NodeId next_peer){
      
         this.next_peer = next_peer;
      
      }
      
      public ChordRouteQueryAns (byte[] data) throws Exception{
      
         next_peer = new NodeId(data, false); // Lower
      
      }
      
      public byte[] getBytes() throws IOException{
      
         return next_peer.getBytes();
      
      }
   
      public NodeId getNextPeer() throws Exception{
      
         return next_peer;
      
      }
   
   }