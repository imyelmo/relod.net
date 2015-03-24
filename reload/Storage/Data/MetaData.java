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
   import reload.Message.Security.TLS.HashAlgorithm;
   
   public class MetaData{
   
      private boolean exists;
      private int value_length;
      private HashAlgorithm hash_algorithm;
      private Opaque hash_value; //<0..255>
      
      public MetaData (boolean exists, HashAlgorithm hash_algorithm, byte[] hash_value) throws ReloadException{
      
         this.exists = exists;
         this.hash_algorithm = hash_algorithm;
         this.hash_value = new Opaque(8, hash_value);
         
         value_length = hash_value.length + 1;
      
      }
      
      public MetaData (byte[] data) throws ReloadException{
      
         exists = Utils.toBoolean(data[0]);
         value_length = Utils.toInt(data, 1);
         hash_algorithm = HashAlgorithm.valueOf(data[5]);
         hash_value = new Opaque(8, data, 6);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(exists));
         baos.write(Utils.toByte(value_length));
         baos.write(hash_algorithm.getBytes());
         baos.write(hash_value.getBytes());
         
         return baos.toByteArray();
      
      }
   
      public boolean getExists(){
         
         return exists;
      	
      }
     
      public HashAlgorithm getHashAlgorithm(){
      
         return hash_algorithm;
      
      }
   	
      public byte[] getHashValue(){
      
         return hash_value.getContent();
      
      }
      
   }