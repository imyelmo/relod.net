   package reload.Storage.Data;

   import java.io.*;
   import java.util.ArrayList;
   import reload.Common.*;
   import reload.Common.Exception.*;	
   
   public class StoredDataSpecifier{
   
   
      private KindId kind;
      private long generation;
      private short length;
   	
      private DataModel dataModel;
   	
      private ArrayRange[] indices; //<0..2^16-1>
      
      private DictionaryKey[] keys; //<0..2^16-1>
    
      
      public StoredDataSpecifier (KindId kind, long generation){
      
         this.kind = kind;
         this.generation = generation;
         length = 0;
         
         dataModel = DataModel.single_value;
      
      }
      
      public StoredDataSpecifier (KindId kind, long generation, ArrayRange[] indices) throws ReloadException{
      
         this(kind, generation);
         
         dataModel = DataModel.array;
         
         this.indices=indices;
      	
         length = (short)(8*indices.length);
      		
         if(length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StoredDataSpecifier (KindId kind, long generation, DictionaryKey[] keys) throws Exception{
      
         this(kind, generation);
      
         dataModel = DataModel.dictionary;
         
         this.keys = keys;
      
         for (int i=0; i<keys.length; i++)
            length += keys[i].getBytes().length;
            
         if(length > Math.pow(2, 16)-1)
            throw new WrongLengthReloadException();
      
      }
      
      public StoredDataSpecifier (byte[] data) throws Exception{
      
         kind = new KindId(data); // 32-bit
         generation = Utils.toLong(data, 4);
         
         length = Utils.toShort(data, 12);
            
         dataModel = kind.getDataModel();
         
         data = Utils.cutArray(data, length, 14);
         int offset=0;
      
         switch(dataModel.getBytes()){
         
            case 2:
            
               if (length % 8 != 0)
                  throw new WrongLengthReloadException();
            
               int num = length/8;
               indices = new ArrayRange[num];
               
               for (int i=0; i<num; i++){
                  indices[i] = new ArrayRange(Utils.cutArray(data, offset));
                  offset += 8;
               }
               break;
               
            case 3:
               
               num = Algorithm.counter(2, data, 0);
            
               keys = new DictionaryKey[num];
            
               for (int i=0; i<num; i++){
               
                  keys[i] = new DictionaryKey(Utils.cutArray(data, offset), false);
                  offset += keys[i].getBytes().length;
               }
         		
         }
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(kind.getBytes());
         baos.write(Utils.toByte(generation));
         baos.write(Utils.toByte(length));
         
         switch(dataModel.getBytes()){
         
            case 1:
               break;
            case 2:
               for (int i=0; i<indices.length; i++)
                  baos.write(indices[i].getBytes());
               break;
            case 3:
               for (int i=0; i<keys.length; i++)
                  baos.write(keys[i].getBytes());
               break; 
            default:
               throw new WrongTypeReloadException();
         }
      
         return baos.toByteArray();
      
      }
   	   
      public KindId getKind(){
      
         return kind;
      
      }
      
      public long getGeneration(){
      
         return generation;
      
      }	
      
      public DataModel getDataModel(){
      
         return dataModel;
      
      }
   	
      public ArrayRange[] getIndices(){
      
         return indices;
      
      }
      
      public DictionaryKey[] getKeys(){
      
         return keys;
      
      }
   
   }