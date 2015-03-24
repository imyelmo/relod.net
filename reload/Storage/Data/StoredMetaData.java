   package reload.Storage.Data;

   import java.io.*;
   import reload.Common.*;
   //import reload.Storage.DataStructure;
	
   
   public class StoredMetaData{
   
   
      private int value_length;
      private long storage_time;
      private int lifetime;
      private MetaDataValue metadata;
    
      
      public StoredMetaData (long storage_time, int lifetime, MetaDataValue metadata) throws Exception{
      
         this.storage_time = storage_time;
         this.lifetime = lifetime;
         this.metadata = metadata;
         
         value_length = 8 + 4 + metadata.getBytes().length;
      
      }
      
      public StoredMetaData (DataStructure data) throws Exception{ // Single & Dictionary
      
         storage_time = data.getStorageTime();
         lifetime = data.getLifeTime();
         if(data.isDictionary()) // Dictionary
            metadata = new MetaDataValue(new DictionaryEntryMeta(data.getDictionaryKey(), new MetaData(data.getExists(), Module.si.getHashAlgorithm(), Module.si.digest(data.getValue().getBytes()))));
         else // Single
            metadata = new MetaDataValue(new MetaData(data.getExists(), Module.si.getHashAlgorithm(), Module.si.digest(data.getValue().getBytes())));
         
         value_length = 8 + 4 + metadata.getBytes().length;
         
      }
      
      public StoredMetaData (DataStructure data, int index) throws Exception{ // Array
      
         storage_time = data.getStorageTime();
         lifetime = data.getLifeTime();
         metadata = new MetaDataValue(new ArrayEntryMeta(index, new MetaData(data.getExists(), Module.si.getHashAlgorithm(), Module.si.digest(data.getValue().getBytes()))));
         
         value_length = 8 + 4 + metadata.getBytes().length;
         
      }
      
      public StoredMetaData (byte[] data, DataModel dataModel) throws Exception{
      
         value_length = Utils.toInt(data, 0);
         
         data = Utils.cutArray(data, value_length, 4);
      	
         storage_time = Utils.toInt(data, 0);
         lifetime = Utils.toInt(data, 8);
         metadata = new MetaDataValue(Utils.cutArray(data, 12), dataModel);
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(value_length));
         baos.write(Utils.toByte(storage_time));
         baos.write(Utils.toByte(lifetime));
         baos.write(metadata.getBytes());
         
         return baos.toByteArray();
      
      }
      
      public int getValueLength(){
      
         return value_length;
      
      }
      
      public long getStorageTime(){
      
         return storage_time;
      
      }
   	
      public int getLifeTime(){
      
         return lifetime; 
      
      }
   	
      public MetaDataValue getMetaData(){
      
         return metadata;
      
      }
   
   }