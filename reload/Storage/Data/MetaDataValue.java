   package reload.Storage.Data;

   import java.io.*;
   import java.util.ArrayList;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Storage.Data.*;
   
   public class MetaDataValue{
   
   	
      private DataModel dataModel;
   	
      private MetaData single_value_entry;
      
      private ArrayEntryMeta array_entry;
      
      private DictionaryEntryMeta dictionary_entry;
    
      
      public MetaDataValue (MetaData single_value_entry){
      
      
         dataModel = DataModel.single_value;
         
         this.single_value_entry = single_value_entry;
      
      }
      
      public MetaDataValue (ArrayEntryMeta array_entry){
      
         dataModel = DataModel.array;
      
         this.array_entry=array_entry;
      
      }
      
      public MetaDataValue (DictionaryEntryMeta dictionary_entry){
      
         dataModel = DataModel.dictionary;
         
         this.dictionary_entry = dictionary_entry;
      
      }
      
      public MetaDataValue (byte[] data, DataModel dataModel) throws Exception{
      
      
         switch(dataModel.getBytes()){
            case 1:
               single_value_entry = new MetaData(data);
               break;
            case 2:
               array_entry = new ArrayEntryMeta(data);
               break;
            case 3:
               dictionary_entry = new DictionaryEntryMeta(data);   	
               break;
            default:
               throw new WrongTypeReloadException();	
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         switch(dataModel.getBytes()){
         
            case 1:
               baos.write(single_value_entry.getBytes());
               break;
            case 2:
               baos.write(array_entry.getBytes());
               break;
            case 3:
               baos.write(dictionary_entry.getBytes());
               break; 
            default:
               throw new WrongTypeReloadException();
         }
      
         return baos.toByteArray();
      
      }
   
      public DataModel getDataModel(){
         
         return dataModel;
      	
      }
   	
      public MetaData getSingleValueEntry(){
      
         return single_value_entry;
      
      }
      
      public ArrayEntryMeta getArrayEntry(){
      
         return array_entry;
      
      }
      
      public DictionaryEntryMeta getDictionaryEntry(){
      
         return dictionary_entry;
      
      }
      
   }