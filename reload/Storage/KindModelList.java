	
   package reload.Storage;
	
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.util.*;
	
   public class KindModelList{
   
      ArrayList<Integer> kind_id;
      ArrayList<String> name;
      ArrayList<DataModel> dataModel;
      
      DataModel commonDM;
      boolean common;
      
   	// If common = two columns
   	// If !common = three columns
      
   	
      public KindModelList(){
      
         kind_id = new ArrayList<Integer>();
         name = new ArrayList<String>();
         dataModel = new ArrayList<DataModel>();
        
         common = false;
      
      }
      
      public KindModelList(DataModel commonDM){
      
         kind_id = new ArrayList<Integer>();
         name = new ArrayList<String>();
      
         this.commonDM = commonDM;
         common = true;
         
      }
      
      public synchronized void add(KindId id, String name, DataModel dataModel) throws ReloadException{
      
         delete(id);
      
         kind_id.add(id.getId());
         this.name.add(name);
         
         if (common){
         
            if(dataModel != commonDM)
               throw new WrongTypeReloadException();
         }
         
         else
            this.dataModel.add(dataModel);
      
      }
      
      public synchronized void add(KindId id, String name) throws ReloadException{
      
         delete(id);
      
         kind_id.add(id.getId());
         this.name.add(name);
         
         if(!common)
            throw new WrongTypeReloadException();
      
      }
         
      public DataModel getDataModel(int kid){ // Call from KindId
      
         if(common)
            return commonDM;
            
        
         int pos = kind_id.indexOf(kid);
            
         if(pos == -1)
            return DataModel.UNDEFINED; 
               
         else 
            return dataModel.get(pos);
               
      }
      
      public DataModel getDataModel(KindId kid){
      
         return getDataModel(kid.getId());
               
      }
      
      public boolean isCommon(){
      
         return common;
               
      }
      
      public DataModel getDataModel() throws ReloadException{
      
         if (!common)
            throw new ReloadException("No KindId specified.");
      
         return commonDM;
               
      }
      
      public String getName(KindId kid){
        
         int pos = kind_id.indexOf(kid.getId());
            
         if(pos == -1)
            return null; 
               
         else 
            return name.get(pos);
               
      }
      
      public boolean contains(KindId kid){
      
         int pos = kind_id.indexOf(kid.getId());
            
         if(pos == -1)
            return false; 
               
         else 
            return true;
      
      }
      
      public synchronized boolean delete(KindId kid){
         
         int pos = kind_id.indexOf(kid.getId());
            
         if(pos == -1)
            return false; 
               
         kind_id.remove(pos);
         name.remove(pos);
         
         if(!common)
            dataModel.remove(pos);
         
         return true;
      
      }
   
   }