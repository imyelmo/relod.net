/*******************************************************************************
 *    <relod.net: GPLv3 beta software implementing RELOAD - draft-ietf-p2psip-base-26 >
 *    Copyright (C) <2013>  <Marcos Lopez-Samaniego, Isaias Martinez-Yelmo, Roberto Gonzalez-Sanchez> Contact: isaias.martinezy@uah.es
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software Foundation,
 *    Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *******************************************************************************/
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