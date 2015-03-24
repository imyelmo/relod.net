   package reload.Topology.Plugins.Chord;
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ChordLeaveData{
   
      private ChordLeaveType type;
      private short pred_length;
      private NodeId[] successors;
      private short succ_length;
      private NodeId[] predecessors;
   	
      
      public ChordLeaveData (NodeId[] node, boolean succ) throws Exception{
      
         if (succ){
            successors = node;
            type = ChordLeaveType.from_succ;
            for (int i=0; i<node.length; i++)
               succ_length += node[i].getBytes().length;
         
            if(succ_length > Math.pow(2, 16)-1)
               throw new WrongLengthReloadException();
               
            pred_length = 0;
            predecessors = new NodeId[0];
         }
         else{
            predecessors = node;
            type = ChordLeaveType.from_pred;
            for (int i=0; i<node.length; i++)
               pred_length += node[i].getBytes().length;
               
            if(pred_length > Math.pow(2, 16)-1)
               throw new WrongLengthReloadException(); 
               
            succ_length = 0;
            successors = new NodeId[0];
         }   
      }
      
      
      public ChordLeaveData (NodeId[] node, ChordLeaveType type) throws Exception{
      
         this.type = type;
      
         switch(type.getBytes()){
            case 1: //Successors
               successors = node;
               for (int i=0; i<node.length; i++)
                  succ_length += node[i].getBytes().length;
            
               if(succ_length > Math.pow(2, 16)-1)
                  throw new WrongLengthReloadException();
                  
               pred_length = 0;
               predecessors = new NodeId[0];
               break;
            case 2: //Predeccesors
               predecessors = node;
               for (int i=0; i<node.length; i++)
                  pred_length += node[i].getBytes().length;
               
               if(pred_length > Math.pow(2, 16)-1)
                  throw new WrongLengthReloadException(); 
               
               succ_length = 0;
               successors = new NodeId[0];
               break;
            default:
               throw new WrongTypeReloadException();
         }
             
      }
      
      public ChordLeaveData (byte[] data) throws Exception{
      
         type = ChordLeaveType.valueOf(data[0]);
         
         switch(type.getBytes()){
         
            case 1:
               succ_length = Utils.toShort(data, 1);
               
               if(succ_length % NodeId.getLength() != 0)
                  throw new WrongLengthReloadException();
                  
               int num_succ = succ_length / NodeId.getLength();	
               successors = new NodeId[num_succ];
               int offset = 3;
               for (int i=0; i<num_succ; i++){
                  successors[i] = new NodeId(Utils.cutArray(data, offset), false); // Lower
                  offset += successors[i].getBytes().length;
               }
               pred_length = 0;
               predecessors = new NodeId[0];
               break;
               
            case 2:
               pred_length = Utils.toShort(data, 1);
               
               if(pred_length % NodeId.getLength() != 0)
                  throw new WrongLengthReloadException();
            	
               int num_pred = pred_length / NodeId.getLength();
               predecessors = new NodeId[num_pred];
               offset = 3;
               for (int i=0; i<num_pred; i++){
                  predecessors[i] = new NodeId(Utils.cutArray(data, offset), false);
                  offset += predecessors[i].getBytes().length;
               }
               succ_length = 0;
               successors = new NodeId[0];
               break;
               
            default:
               throw new WrongTypeReloadException();
         }
      
      
      }
            
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         
         switch(type.getBytes()){
         
            case 1:
               baos.write(Utils.toByte(pred_length));
               for (int i=0; i<successors.length; i++)
                  baos.write(successors[i].getBytes());
               break;
            case 2:
               baos.write(Utils.toByte(succ_length));
               for (int i=0; i<predecessors.length; i++)
                  baos.write(predecessors[i].getBytes());
               break;
           
         }
            
         return baos.toByteArray();
      
      }
      
      public ChordLeaveType getType(){
      
         return type;
      
      }
      
      public NodeId[] getSuccessors(){
      
         return successors;
      
      }
   
      public NodeId[] getPredecessors(){
      
         return predecessors;
      
      }
   
   }