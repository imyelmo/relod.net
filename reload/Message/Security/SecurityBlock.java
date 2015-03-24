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
		
   public class SecurityBlock{
   
      private short cert_length;
      private GenericCertificate[] certificates; //16-bit
      private Signature signature;
      
   
      
      public SecurityBlock (GenericCertificate[] certificates, Signature signature) throws Exception{
      
         this.certificates = certificates;
         this.signature = signature;
         
         for (int i=0; i<certificates.length; i++)
            cert_length += certificates[i].getBytes().length;
         
         if(cert_length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException(); 
      }
      
      public SecurityBlock (byte[] data) throws Exception{
         
         cert_length = Utils.toShort(data, 0);
      	
         byte[] data_cert = Utils.cutArray(data, cert_length, 2);
         int offset = 0;
        
         int num = Algorithm.counter(2, data_cert, 1);
      	
         certificates = new GenericCertificate[num];
         
         for (int i=0; i<num; i++){
         
            certificates[i] = new GenericCertificate(Utils.cutArray(data_cert, offset));
            offset += certificates[i].getBytes().length;
         }
         
         signature = new Signature(Utils.cutArray(data, 2+offset));
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(cert_length));
      	
         for (int i=0; i<certificates.length; i++)
            baos.write(certificates[i].getBytes());
            
         baos.write(signature.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public Signature getSignature(){
      
         return signature;
      
      }
   
   }