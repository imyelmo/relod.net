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
   package reload.Message.Security;

   import java.io.*;
   import reload.Message.Security.TLS.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
	
   public class Signature{
   
      private SignatureAndHashAlgorithm algorithm;
      private SignerIdentity identity;
      private Opaque signature_value; //<0..2^16-1>
        
      public Signature (SignatureAndHashAlgorithm algorithm, SignerIdentity identity, byte[] signature_value) throws ReloadException{
      
         this.algorithm = algorithm;
         this.identity = identity;
         this.signature_value = new Opaque(16, signature_value);
      
      }
      
      public Signature (byte[] data) throws Exception{
         
         algorithm = new SignatureAndHashAlgorithm(data); // Size: 2 bytes
         identity = new SignerIdentity(Utils.cutArray(data, 2));
         
         int lengthIdentity = identity.getBytes().length;
         signature_value = new Opaque(16, data, lengthIdentity+2);
      
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(algorithm.getBytes());
         baos.write(identity.getBytes());
         baos.write(signature_value.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public SignerIdentity getIdentity(){
      
         return identity;
      
      }
   
   
   }