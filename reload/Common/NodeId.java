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

   import java.util.UUID;
   import java.io.*;
   import java.math.BigInteger;
   import reload.Message.Forwarding.DestinationType;
   import reload.Common.Exception.*;
	
   public class NodeId extends Id{
   
      
      private int length;
         
      
      public NodeId(byte[] data, boolean upper) throws Exception{ //128 to 160 bit
      
         XML configuration = new XML();
         length = configuration.getNodeIDLength();
         
         if(length < 16 || length > 20)
            throw new WrongLengthReloadException();
      
         if(upper){
            
            if(data.length != length)
               throw new WrongLengthReloadException();
               
            id = data;
            
         }
         
         else
            id = Utils.cutArray(data, length, 0);
         
         type = DestinationType.node; // Extended
      
      }
      
      public NodeId(BigInteger data) throws Exception{ //128 to 160 bit, positive BigInteger ONLY
      
      
         if(data.signum()==-1) 
            throw new NumberFormatException("Negative BigInteger not allowed.");
      
      
         XML configuration = new XML();
         length = configuration.getNodeIDLength();
      
         if(length < 16 || length > 20)
            throw new WrongLengthReloadException();
            
         if((float)(data.bitLength())/8 > length)
            throw new WrongLengthReloadException();
                
      
         id = new byte[length]; //16
         byte[] temp = data.toByteArray();
         
      
         if(temp.length == length+1){ //17
         
            if (temp[0] != 0)
               throw new NumberFormatException("Fatal Error."); // Should not occur.
         
            for (int i=length-1; i>=0; i--) //15
               id[i] = temp[i+1];
         }
         
         else{
         
            for (int i=length-1, j=temp.length-1; j>=0; i--, j--) //15
               id[i] = temp[j];
         }
         
         type = DestinationType.node; // Extended
      
      }
      
      public NodeId(int data) throws Exception{ // For testing
      
         this(BigInteger.valueOf(data)); 
      
      }
      
      public NodeId() throws Exception{ //Empty Node-ID
      
         this(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, true);
      	 
      }
      
      public NodeId(UUID data) throws Exception{ // Supposing 128 bits and upper call
      
         XML configuration = new XML();
         length = configuration.getNodeIDLength();
      
         if(length != 16)
            throw new WrongLengthReloadException();
      
         id = Utils.toByte(data); 
         
         type = DestinationType.node; // Extended
      
      }
      
      public NodeId sum(int num) throws Exception{
      
         NodeId node = new NodeId(id, true);
         node.add(num);
         return node;
      
      }
      
      public boolean isEmpty(){
               
         if(Utils.equals(id, new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}))
            return true;
         
         else
            return false;
      	 
      }
      
      public boolean isWildcard(){ // Wildcard node-ID
               
         if(Utils.equals(id, ones))
            return true;
         
         else
            return false;
      	 
      }
      
      public void setOnes(){
      
         id = ones;
      
      }
      
      public static int getLength() throws Exception{
      
         XML configuration = new XML();
         int length = configuration.getNodeIDLength();
      
         return length;
      
      }
      
      public byte[] getBytes(){
      
         return id;
      
      }
   
   }