   package reload.Topology.Plugins;
   
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class ProbeInformationData{
   
      private int responsible_ppb;
      private int num_resources;
      private int uptime;
      private ProbeInformationType type;
   	
      public ProbeInformationData (int data, ProbeInformationType type) throws ReloadException{
      
         this.type = type;
      
         switch(type.getBytes()){
         
            case 1:
               responsible_ppb = data;
               break;
            case 2:
               num_resources = data;
               break;
            case 3:
               uptime = data;
               break;
            default:
               throw new WrongTypeReloadException();
           
         }
         
      }
      
      public ProbeInformationData (byte[] data, ProbeInformationType type) throws ReloadException{
      
         this.type = type;
      
         switch(type.getBytes()){
         
            case 1:
               responsible_ppb = Utils.toInt(data, 0);
               break;
            case 2:
               num_resources = Utils.toInt(data, 0);
               break;
            case 3:
               uptime = Utils.toInt(data, 0);
               break;
            default:
               throw new WrongTypeReloadException();
           
         }
         
      }
      
      public byte[] getBytes() throws Exception{
      
         
         switch(type.getBytes()){
         
            case 1:
               return Utils.toByte(responsible_ppb);
            case 2:
               return Utils.toByte(num_resources);
            case 3:
               return Utils.toByte(uptime);
            default:
               throw new WrongTypeReloadException();
           
         }
            
      }
      
      public ProbeInformationType getType(){
      
         return type;
      
      }
      
      public int getResponsiblePPB() throws ReloadException{
      
         if(type == ProbeInformationType.responsible_set)
            return responsible_ppb;
         
         else
            throw new WrongTypeReloadException();
      
      }
   	
      public int getNumResources() throws ReloadException{
      
         if(type == ProbeInformationType.num_resources)
            return num_resources;
         
         else
            throw new WrongTypeReloadException();
      
      }
   	
      public int getUptime() throws ReloadException{
      
         if(type == ProbeInformationType.uptime)
            return uptime;
         
         else
            throw new WrongTypeReloadException();
      
      }
   
   }