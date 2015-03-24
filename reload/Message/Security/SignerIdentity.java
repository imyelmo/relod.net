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
   import reload.Common.*;
   import reload.Common.Exception.*;
 
   public class SignerIdentity{
   
   
      private SignerIdentityType identity_type;
      private short length;
      private SignerIdentityValue identity;
   
      
      public SignerIdentity (SignerIdentityType identity_type, SignerIdentityValue identity) throws IOException{
      
         this.identity_type = identity_type;
         this.identity = identity;
      	
         length = (short)(identity.getBytes().length);
         
      }
      
      public SignerIdentity (SignerIdentityType identity_type) { //SignerIdentityValue empty
      
         this.identity_type = identity_type;
         length = 0;
         this.identity = new SignerIdentityValue(); //Empty  
      	
      }
      
      public SignerIdentity (byte[] data) throws ReloadException {
      
         identity_type = SignerIdentityType.valueOf(data[0]);
         length = Utils.toShort(data, 1);
         
         if (length > 0)
            identity = new SignerIdentityValue(Utils.cutArray(data, length, 3), identity_type);
            
         else
            identity = new SignerIdentityValue(); //Empty  
      
      }
      
      public byte[] getBytes() throws IOException{
      
         
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(identity_type.getBytes());
         baos.write(Utils.toByte(length));
         baos.write(identity.getBytes());
         
         return baos.toByteArray();
      }
   
      public SignerIdentityType getType(){
      
         return identity_type;
      
      }
      
      public SignerIdentityValue getValue(){
      
         return identity;
      
      }
   
   }