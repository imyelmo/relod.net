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