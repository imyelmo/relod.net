   package reload.Link.TCP;
   
   import java.io.*;
   import java.net.*;
   import reload.Common.*;

 
   public class TCP{
   
          
      public void send(byte[] data, DataOutputStream out) throws IOException{
         out.write(data);
      
      }
   	
      public byte[] receive(DataInputStream in) throws IOException{
         	
      
         while(true){
         
            byte R = in.readByte();
            
            if(R != -46)
               System.err.println("Received packet is not RELOAD.");
         
            while(R != -46) //0xd2
               R = in.readByte();
            
            byte E = in.readByte();
            if(E != 69) //0x45
               continue;  
         
            byte L = in.readByte();
            if(L != 76) //0x4c
               continue;
            
            byte O = in.readByte();
            if(O != 79) //0x4f
               continue;
            
            break;
         
         }
      
         int overlay = in.readInt();
      
         short configuration_sequence = in.readShort();
      
         byte version = in.readByte();
      
         byte ttl = in.readByte();
       
         int fragment = in.readInt();
       
         int length = in.readInt();
      
      
         byte[] data_temp = new byte[length-20];
            
         in.readFully(data_temp);
         
         byte[] data = new byte[length];
               
         byte[] relo_tokenB = Utils.toByte(0xd2454c4f); //relo_token
         for (int i=0; i<4; i++)
            data[i] = relo_tokenB[i];
            
         byte[] overlayB = Utils.toByte(overlay);
         for (int i=4; i<8; i++)
            data[i] = overlayB[i-4];
            
         byte[] configuration_sequenceB = Utils.toByte(configuration_sequence);
         for (int i=8; i<10; i++)
            data[i] = configuration_sequenceB[i-8];
            
         data[10] = version;
      		
         data[11] = ttl;
         
         byte[] fragmentB = Utils.toByte(fragment);
         for (int i=12; i<16; i++)
            data[i] = fragmentB[i-12];
            
         byte[] lengthB = Utils.toByte(length);
         for (int i=16; i<20; i++)
            data[i] = lengthB[i-16];
            
         for (int i=20; i<length; i++)
            data[i] = data_temp[i-20];
            	
         return data;
      
      }
      
   }