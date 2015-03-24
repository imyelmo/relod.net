   package reload.Topology.Plugins;

   import java.io.*;
   import reload.Common.*;
	import reload.Common.Exception.*;
   
   public class ProbeAns{
   
   
      private short probe_length;
      private ProbeInformation[] probe_info; //<0..2^16-1>
         
      
      public ProbeAns (ProbeInformation probe_info[]) throws Exception{
      
         this.probe_info = probe_info;
         
         for (int i=0; i<probe_info.length; i++)
            probe_length += probe_info[i].getBytes().length;
            
         if(probe_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public ProbeAns (byte[] data) throws Exception{
      
         probe_length = Utils.toShort(data, 0);
         
         Utils.cutArray(data, probe_length, 2);
         int offset = 0;
         
         int num = Algorithm.counter(1, data, 1);
      
         probe_info = new ProbeInformation[num];
      
         for (int i=0; i<num; i++){
         
            probe_info[i] = new ProbeInformation(Utils.cutArray(data, offset));
            offset += probe_info[i].getBytes().length;
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(probe_length));
         
         for (int i=0; i<probe_info.length; i++)
            baos.write(probe_info[i].getBytes());
         
         
         return baos.toByteArray();
      
      }
   
      public ProbeInformation[] getProbeInfo(){
      
         return probe_info;
      
      }
   
   }