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
   package reload.Forwarding.Ping;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class PingReq{
   
   
      private Opaque padding; //<0..2^16-1>
      
      private byte[] def_data = new byte[]{0,0,0,0,0,0,0,0};
   
   
      public PingReq () throws ReloadException{ //upper call [llamada de los niveles superiores]
      
         padding = new Opaque(16, def_data);
      
      }   
      
      public PingReq (byte[] data, boolean upper) throws ReloadException{ //upper call [llamada de los niveles superiores]
      
         if(upper)
            padding = new Opaque(16, data);
         
         else
            padding = new Opaque(16, data, 0);
      
      }
      
      public byte[] getBytes() throws IOException{
            
         return padding.getBytes();
      
      }
      
      public int getPaddingLength(){
      
         return padding.getContent().length;
      
      }
      
   }