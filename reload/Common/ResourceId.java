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

   import java.util.Random;
   import java.math.BigInteger;
   import java.io.ByteArrayOutputStream;
   import reload.Message.Forwarding.DestinationType;
   import reload.Common.Exception.*;
   
	// Should be variable-length but it is a 128-bit number
  
   public class ResourceId extends Id{
   
      private static byte length = 16; // For Chord
   	
      //public final static byte[] ones = {(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF};
   
   
      public ResourceId(byte[] data, boolean upper) throws WrongLengthReloadException{
      
         if(upper){
         
            if(data.length != 16) // Chord
               throw new WrongLengthReloadException();
          
            id = data;
            
         }
         
         else{
         
            id = Utils.cutArray(data, data[0], 1);
            length = data[0];
            
            if(length != 16) // Chord
               throw new WrongLengthReloadException();
            
         }
         
         type = DestinationType.resource; // Extended
      
      }
      
      public ResourceId (BigInteger id) throws Exception{
      
         if(id.signum()==-1) 
            throw new NumberFormatException("Negative BigInteger not allowed.");
            
         if (id.bitLength() > 128)
            throw new WrongLengthReloadException();
      
         this.id = toByteArray(id);
         
         type = DestinationType.resource; // Extended
         
      }
      
      public ResourceId (long id) throws Exception{
      
         this(BigInteger.valueOf(id));
         
      }
      
      public ResourceId() { // Random
      
         id = toByteArray(new BigInteger(128, new Random()));
         
         type = DestinationType.resource; // Extended
      
      }
   
   // Not equal to getId(), getBytes() returns length and id, while getId() returns id only
      public byte[] getBytes() throws java.io.IOException{
      
         //return id;
         
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(length);
         baos.write(id);
         
         return baos.toByteArray();
      	
      }
   
   }