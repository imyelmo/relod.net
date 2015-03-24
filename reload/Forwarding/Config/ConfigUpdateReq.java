   package reload.Forwarding.Config;
	
   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
 
   public class ConfigUpdateReq{
   
   
      private ConfigUpdateType type;
      private int length;
   
      private byte[] config_data; // 24 bits  
      
      private KindDescription[] kinds; // 24 bits
   
   
      
      public ConfigUpdateReq (byte[] data, boolean upper) throws Exception{
      
      
         if (upper){
            type = ConfigUpdateType.config;
            length = data.length;
         
            config_data = data;
            
            if(length > Math.pow(2, 24)-1)
               throw new WrongLengthReloadException();
         }
         
         else{
         
            type = ConfigUpdateType.valueOf(data[0]);
            length = Utils.toInt(data, 1);
            
            if(length > Math.pow(2, 24)-1)
               throw new WrongLengthReloadException();
         
            switch(type.getBytes()){
            
               case 1:
                  //config_data = new Opaque(24, data, 5);
                  config_data = Utils.cutArray(data, length, 5);
                  break;
            	
               case 2:
               
                  byte[] kindData = Utils.cutArray(data, length, 5);
               
                  int num = Algorithm.counter(2, kindData, 0);
               
                  kinds = new KindDescription[num];
               
                  for (int offset=0, i=0; i<num; i++){
                  
                     kinds[i] = new KindDescription(Utils.cutArray(kindData, offset), false);
                     offset += kinds[i].getBytes().length;
                  }
            
            }
         
         }
         
      }
      
      public ConfigUpdateReq (KindDescription[] kinds) throws Exception{
      
         type = ConfigUpdateType.kind;
         
         this.kinds = kinds;
         
         for(int i=0; i<kinds.length; i++){
            length += kinds[i].getBytes().length;
         }
         
         if(length > Math.pow(2, 24)-1)
            throw new WrongLengthReloadException(); 
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(Utils.toByte(length));
      
         switch(type.getBytes()){
         
            case 1:
            
               baos.write(config_data);
               break;
            	
            case 2:
            
               for (int i=0; i<kinds.length; i++)
                  baos.write(kinds[i].getBytes());
         
         }
         
         return baos.toByteArray();
         
      }
   
      public ConfigUpdateType getType(){
      
         return type;
      
      }
      
      public byte[] getConfigData(){
      
         return config_data;
      
      }  
      
      public KindDescription[] getKinds(){
      
         return kinds;
      
      }
   
   }