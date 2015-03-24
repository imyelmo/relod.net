   package reload.Topology.Plugins;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ProbeInformation{
   
      private ProbeInformationType type;
      private byte length;
      private ProbeInformationData value;
      
      public ProbeInformation (ProbeInformationType type, ProbeInformationData value) throws Exception{
      
         this.type = type;
         this.value = value;
         
         length = (byte)value.getBytes().length;
      
      }
      
      public ProbeInformation (byte[] data) throws ReloadException{
      
         type = ProbeInformationType.valueOf(data[0]);
         length = data[1];
         value = new ProbeInformationData(Utils.cutArray(data, length, 3), type);
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(length);
         baos.write(value.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public ProbeInformationType getType(){
      
         return type;
      
      }
   	
      public ProbeInformationData getData(){
      
         return value;
      
      }
   
   }