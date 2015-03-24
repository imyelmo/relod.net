   package reload.Topology.Plugins;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ProbeReq{
   
      private byte probe_length;
      private ProbeInformationType[] requested_info; //8-bit
      
      
      public ProbeReq (ProbeInformationType[] requested_info) throws Exception{
      
         this.requested_info = requested_info;
         
         probe_length = (byte)requested_info.length;
         
         if(requested_info.length > Math.pow(2, 8)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public ProbeReq (byte[] data){
      
         probe_length = data[0];
         
         data = Utils.cutArray(data, probe_length, 1);
      
         requested_info = new ProbeInformationType[probe_length];
      
         for (int i=0; i<probe_length; i++)
            requested_info[i] = ProbeInformationType.valueOf(data[i]);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
       
         baos.write(probe_length); 
      	
         for (int i=0; i<probe_length; i++)
            baos.write(requested_info[i].getBytes());
         
         return baos.toByteArray();
      
      }
      
      public ProbeInformationType[] getRequestedInfo(){
      
         return requested_info;
      
      }
      
   }