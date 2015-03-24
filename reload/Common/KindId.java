   package reload.Common;

   import java.io.*;
   import reload.Common.*;
   import reload.Storage.*;  
   
   public class KindId{
   
      private int data;
   	
      
      public KindId(int data){
      
         this.data = data;
                  
      }
      
      public KindId(byte[] data){
      
         this.data = Utils.toInt(data, 0);
               
      }
      
      public byte[] getBytes() throws IOException{
            
         return Utils.toByte(data);
      
      }
      
      public int getId(){
      
         return data;
      
      }
      
      public DataModel getDataModel(){
        
         return Module.si.kind_model.getDataModel(data);
        
      }
   
   }