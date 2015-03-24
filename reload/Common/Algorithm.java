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
   
   import reload.Forwarding.Request.CandType;
   import reload.Common.Exception.*;
  
   public class Algorithm{
   
      public static int counter(int size, byte[] data, int sumPosition) throws ReloadException{
      
      
         if(data.length==0)
            return 0;
            
         if(size != 1 && size !=2 && size !=4 && size !=8 && size !=16 && size !=32)
            throw new ReloadException("Incorrect size. Only 1, 2 and 4 bytes are allowed. Only 8, 16 and 32 bits are allowed.");
            
         if(size>7)
            size=size/8;
      
      
         int count=0;    
         int position = sumPosition;
        
         for(boolean salir=false; !salir; count++){
            
            if(size==4)
               position += Utils.toInt(data, position)+size;
               
            if(size==2)
               position += Utils.toShort(data, position)+size;
         
            if(size==1)
               position += data[position]+size;
               
            if(position == data.length)
               salir=true;
            
            position += sumPosition;    
            	
         }
         
         return count;
         
      }
      
   
      public static int AttachCounter(byte[] data){
      
      
         if(data.length==0)
            return 0;
            
         int position=0;
            
      
         int count=0;
      
         for(boolean salir=false; !salir; count++){
            
            position += data[position+1]+2; // IPAddresssPort
            
            position++; //OverleyLinkType
         	
            position += data[position]+1; // Opaque foundation
            
            position += 4; // int priority
            
            CandType type = CandType.valueOf(data[position]);
            
            position++; // CandType
         	
            if (type == CandType.relay)
               position += data[position+1]+2; // IPAddresssPort
               
            short ext_length = Utils.toShort(data, position);	//short
            position += 2;
         	
         	
            position += ext_length; // IceExtension[]
            
            if(position == data.length)
               salir=true;
            	
         }
         
         return count;
      
      }
   }