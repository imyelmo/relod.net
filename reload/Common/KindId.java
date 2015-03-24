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
   package reload.Common;

   import java.io.*;
   import reload.Common.*;
   import reload.Storage.*;  
   
   public class KindId{
   
      private int data;
   	
      
      public KindId(int data){
      
         this.data = data;
                  
      }
      
      public KindId(byte[] data){
      
         this.data = Utils.toInt(data, 0);
               
      }
      
      public byte[] getBytes() throws IOException{
            
         return Utils.toByte(data);
      
      }
      
      public int getId(){
      
         return data;
      
      }
      
      public DataModel getDataModel(){
        
         return Module.si.kind_model.getDataModel(data);
        
      }
   
   }