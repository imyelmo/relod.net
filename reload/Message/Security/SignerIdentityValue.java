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
 
   public class SignerIdentityValue{
   
   
      private HashAlgorithm hash_alg; // From TLS  
      private Opaque certificate_hash; // 8-bit. Two types
      private SignerIdentityType identity_type;
   
      
      public SignerIdentityValue (HashAlgorithm hash_alg, byte[] certificate_hash, SignerIdentityType identity_type) throws ReloadException{
      
         if(identity_type == SignerIdentityType.cert_hash || identity_type == SignerIdentityType.cert_hash_node_id){
            this.hash_alg = hash_alg;
            this.certificate_hash = new Opaque(8, certificate_hash);   
         }
      
         this.identity_type = identity_type; // Not a part of the Structure
         
      }
      
      public SignerIdentityValue(){
      
         identity_type = SignerIdentityType.none;
         
      }
   	
      public SignerIdentityValue (byte[] data, SignerIdentityType identity_type) throws ReloadException{
      
      
         if(identity_type == SignerIdentityType.cert_hash || identity_type == SignerIdentityType.cert_hash_node_id){
            hash_alg = HashAlgorithm.valueOf(data[0]); 
            certificate_hash = new Opaque(8, data, 1);
         }
      
         this.identity_type = identity_type; // Not a part of the Structure
         
      }
      
      public byte[] getBytes() throws IOException{
      
         byte ret[] = new byte[0]; // Empty
      
         switch(identity_type.getBytes()){
         
            case 1:
            
            // We go to case 2
            	
            case 2:
            
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               baos.write(hash_alg.getBytes());
               baos.write(certificate_hash.getBytes());
               ret = baos.toByteArray();
               break;
            		
            case 3:
            
            /* Empty */   
         
         }
         
         return ret;
         
      }
   
      public byte[] getCertificateHash(){ // Returns cert_hash and also cert_hast_node_id
      
         return certificate_hash.getContent();
      
      }
   
   }