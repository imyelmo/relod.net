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