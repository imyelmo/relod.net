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
   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   public class StatAns{
   
   
      private int kind_length;
      private StatKindResponse[] kind_responses; //<0..2^32-1>
    
      
      public StatAns (StatKindResponse[] kind_responses) throws Exception{
      
         this.kind_responses = kind_responses;
         
         for (int i=0; i<kind_responses.length; i++)
            kind_length += kind_responses[i].getBytes().length;
      		
         if(kind_length > Math.pow(2, 32)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StatAns (byte[] data) throws Exception{
      
         
         kind_length = Utils.toInt(data, 0);
         
         data = Utils.cutArray(data, kind_length, 4);
         int offset = 0;
         
         int num = Algorithm.counter(4, data, 12);	
      
         kind_responses = new StatKindResponse[num];
      
         for (int i=0; i<num; i++){
         
            kind_responses[i] = new StatKindResponse(Utils.cutArray(data, offset));
            offset += kind_responses[i].getBytes().length;
         }
      	
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(kind_length));
      
         for (int i=0; i<kind_responses.length; i++)
            baos.write(kind_responses[i].getBytes());
         
         return baos.toByteArray();
      
      }
   
      public StatKindResponse[] getKindResponses(){
      
         return kind_responses;
      
      }
   
   }