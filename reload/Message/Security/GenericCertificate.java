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
   
   public class GenericCertificate{
   
      private CertificateType type;
      private Opaque certificate; //16-bit   
   
      
      public GenericCertificate (CertificateType type, byte[] certificate) throws ReloadException{
      
         this.type = type;
         this.certificate = new Opaque (16, certificate);
         
      }
      
      public GenericCertificate (CertificateType type, String certificate) throws Exception{
      
         this(type, certificate.getBytes("US-ASCII"));
      
      }
   	
      
      public GenericCertificate (byte[] data) throws ReloadException{
      
         type = CertificateType.valueOf(data[0]);
         certificate = new Opaque (16, data, 1);
         
      }
      
      public byte[] getBytes() throws IOException{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(type.getBytes());
         baos.write(certificate.getBytes());
         
         return baos.toByteArray();
      
      }
   
   
   }