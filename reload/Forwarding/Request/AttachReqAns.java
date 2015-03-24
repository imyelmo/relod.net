   package reload.Forwarding.Request;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class AttachReqAns{
   
      private Opaque ufrag; //<0..2^8-1>
      private Opaque password; //<0..2^8-1>
      private Opaque role; //<0..2^8-1>
      
      private short cand_length;
      private IceCandidate[] candidates; //<0..2^16-1>
      private boolean send_update;
   
      
      public AttachReqAns (boolean send_update, IpAddressPort addr_port) throws Exception{ // Empty, NAT not supported
      
         ufrag = new Opaque(8, new byte[0]);
         password = new Opaque(8, new byte[0]);
         role = new Opaque(8, new byte[0]);
         
         cand_length = 0;
         
         candidates = new IceCandidate[1];
         candidates[0] = new IceCandidate(addr_port, OverlayLinkType.UNDEFINED, new byte[0], 0, new IceExtension[0]);
         this.send_update = send_update;
         
         for (int i=0; i<candidates.length; i++)
            cand_length += candidates[i].getBytes().length;
         
         if(cand_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      
      }
      
      public AttachReqAns (byte[] ufrag, byte[] password, byte[] role, IceCandidate[] candidates, boolean send_update) throws Exception{
      
         this.ufrag = new Opaque(8, ufrag);
         this.password = new Opaque(8, password);
         this.role = new Opaque(8, role);
         
         this.candidates = candidates;
         this.send_update = send_update;
         
         for (int i=0; i<candidates.length; i++)
            cand_length += candidates[i].getBytes().length;
         
         if(cand_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      
      }
      
      public AttachReqAns (byte[] data) throws Exception{
         
         ufrag = new Opaque(8, data, 0);
         int offset = ufrag.getBytes().length;
         
         password = new Opaque(8, data, offset);
         offset += password.getBytes().length;
         
         role = new Opaque(8, data, offset);
         offset += role.getBytes().length;
         
         cand_length = Utils.toShort(data, offset);
         offset += 2;
         
         data = Utils.cutArray(data, cand_length+1, offset);
         offset = 0;
      
         int num = Algorithm.AttachCounter(Utils.cutArray(data, cand_length, 0));
      		
         candidates = new IceCandidate[num];
               	
         for (int i=0; i<num; i++){
         
            candidates[i] = new IceCandidate(Utils.cutArray(data, offset));
            offset += candidates[i].getBytes().length;
         }
         
      	
         send_update = Utils.toBoolean(data[offset]);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(ufrag.getBytes());
         baos.write(password.getBytes());
         baos.write(role.getBytes());
         baos.write(Utils.toByte(cand_length));
      
         for (int i=0; i<candidates.length; i++)
            baos.write(candidates[i].getBytes());
         
         baos.write(Utils.toByte(send_update));
         
         return baos.toByteArray();
      
      }
      
      public boolean getSendUpdate(){
      
         return send_update;
      
      }
      
      public IceCandidate[] getCandidates(){ //If public, only one Address
      
         return candidates;
      
      }
      
      public IceCandidate getCandidates(int i){ //If public, only one Address
      
         return candidates[i];
      
      }
   
   }