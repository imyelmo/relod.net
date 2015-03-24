   package reload.Topology.Plugins.Chord;
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ChordUpdateReq{
   
      private int uptime;
      private ChordUpdateType type;
      private short pred_length;
      private NodeId[] predecessors; //<0..2^16-1>
      private short succ_length;
      private NodeId[] successors; //<0..2^16-1>
      private short fing_length;
      private NodeId[] fingers; //<0..2^16-1>
   	
      
      public ChordUpdateReq (int uptime){
      
         this.uptime = uptime;
         type = ChordUpdateType.peer_ready;
         
         pred_length=0;
         succ_length=0;
         fing_length=0;
         
         predecessors = new NodeId[0];
         successors = new NodeId[0];
         fingers = new NodeId[0];
         
      }
      
      public ChordUpdateReq (int uptime, NodeId[] predecessors, NodeId[] successors) throws Exception{
      
         this.predecessors = predecessors;
         this.successors = successors;
         this.uptime = uptime;
         type = ChordUpdateType.neighbors;
         
         for (int i=0; i<predecessors.length; i++)
            pred_length += predecessors[i].getBytes().length;
         
         if(pred_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      
         for (int i=0; i<successors.length; i++)
            succ_length += successors[i].getBytes().length;
         
         if(succ_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
            
         fing_length=0;
         fingers = new NodeId[0];
         
      }
      
      public ChordUpdateReq (int uptime, NodeId[] predecessors, NodeId[] successors, NodeId[] fingers) throws Exception{
      
         this(uptime, predecessors, successors);
         this.fingers = fingers;
         type = ChordUpdateType.full;
         
         for (int i=0; i<fingers.length; i++)
            fing_length += fingers[i].getBytes().length;
         
         if(fing_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
         
      }
      
      public ChordUpdateReq (byte[] data) throws Exception{
      
         uptime = Utils.toInt(data, 0);
         type = ChordUpdateType.valueOf(data[4]);
         
         switch(type.getBytes()){
         
            case 1:
               pred_length=0;
               succ_length=0;
               fing_length=0;
            
               predecessors = new NodeId[0];
               successors = new NodeId[0];
               fingers = new NodeId[0];
               break;
               
            case 2:
               pred_length = Utils.toShort(data, 5);
               
               if(pred_length % NodeId.getLength() != 0)
                  throw new WrongLengthReloadException();
            	
               int num_pred = pred_length / NodeId.getLength();
               predecessors = new NodeId[num_pred];
               
               int offset = 7;
               
               for (int i=0; i<num_pred; i++){
                  predecessors[i] = new NodeId(Utils.cutArray(data, offset), false); // Lower
                  offset += predecessors[i].getBytes().length;
               }
            	
               succ_length = Utils.toShort(data, offset);
               offset += 2;
               
               if(succ_length % NodeId.getLength() != 0)
                  throw new WrongLengthReloadException();
            	
               int num_succ = succ_length / NodeId.getLength();
            	
               successors = new NodeId[num_succ];
            	
               for (int i=0; i<num_succ; i++){
                  successors[i] = new NodeId(Utils.cutArray(data, offset), false); // Lower
                  offset += successors[i].getBytes().length;
               }
            		
               fing_length=0;
               fingers = new NodeId[0];
               break;
               
            case 3:
            
               pred_length = Utils.toShort(data, 5);
               
               if(pred_length % NodeId.getLength() != 0)
                  throw new WrongLengthReloadException();
            	
               num_pred = pred_length / NodeId.getLength();
               predecessors = new NodeId[num_pred];
               
               offset = 7;
               
               for (int i=0; i<num_pred; i++){
                  predecessors[i] = new NodeId(Utils.cutArray(data, offset), false); // Lower
                  offset += predecessors[i].getBytes().length;
               }
            
               succ_length = Utils.toShort(data, offset);
               offset += 2;
               
               if(succ_length % NodeId.getLength() != 0)
                  throw new WrongLengthReloadException();
            	
               num_succ = succ_length / NodeId.getLength();
            	
               successors = new NodeId[num_succ];
            	
               for (int i=0; i<num_succ; i++){
                  successors[i] = new NodeId(Utils.cutArray(data, offset), false); // Lower
                  offset += successors[i].getBytes().length;
               }
            		
               fing_length = Utils.toShort(data, offset);
               offset += 2;
               
               if(fing_length % NodeId.getLength() != 0)
                  throw new WrongLengthReloadException();
            	
               int num_fing = fing_length / NodeId.getLength();
               
               fingers = new NodeId[num_fing];
            	
               for (int i=0; i<num_fing; i++){
                  fingers[i] = new NodeId(Utils.cutArray(data, offset), false); // Lower
                  offset += fingers[i].getBytes().length;
               }
            
               break;
               
            default:
               throw new WrongTypeReloadException();
           
         }
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(uptime));
         baos.write(type.getBytes());
         
         switch(type.getBytes()){
         
            case 1:
               break;
            case 2:
               baos.write(Utils.toByte(pred_length));
               for (int i=0; i<predecessors.length; i++)
                  baos.write(predecessors[i].getBytes());
               baos.write(Utils.toByte(succ_length));
               for (int i=0; i<successors.length; i++)
                  baos.write(successors[i].getBytes());
               break;
            case 3:
               baos.write(Utils.toByte(pred_length));
               for (int i=0; i<predecessors.length; i++)
                  baos.write(predecessors[i].getBytes());
               baos.write(Utils.toByte(succ_length));
               for (int i=0; i<successors.length; i++)
                  baos.write(successors[i].getBytes());
               baos.write(Utils.toByte(fing_length));
               for (int i=0; i<fingers.length; i++)
                  baos.write(fingers[i].getBytes());
               break;
           
         }
            
         return baos.toByteArray();
      
      }
      
      public int getUptime(){
      
         return uptime;
      
      }
      
      public ChordUpdateType getType(){
      
         return type;
      
      }
      
      public NodeId[] getPredecessors(){
      
         return predecessors;
      
      }
   	
      public NodeId[] getSuccessors(){
      
         return successors;
      
      }
   	
      public NodeId[] getFingers(){
      
         return fingers;
      
      }
   
   
   }