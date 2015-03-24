
package reload.Storage;

import reload.Storage.Data.*;
import reload.Storage.Task.*;
import reload.Message.*;
import reload.Message.Forwarding.Destination;	
import reload.Common.*;
import reload.Common.Error.*;
import reload.Common.Exception.*;
import reload.Message.Security.*;
import reload.Message.Security.TLS.*;

import java.util.*;
import java.security.MessageDigest; 

/**
* StorageInterface class is the interface of the Storage module.
* @author Malosa 
* @version 0.1
*/	

public class StorageInterface{

   public KindModelList kind_model;
   public KindCounterList kind_counter;
   public ResourceReplicaList resource_rep;
   public ResourceKindList resource_kind;
   public SingleValueTable single;
   public ArrayTable array;
   public DictionaryTable dictionary;
   
   private HashAlgorithm hash_alg = HashAlgorithm.sha1;
   private StorageThread thread;

/**
 * Establishes the Storage module.
 */  
   public StorageInterface(){  // Common=false
   
      kind_model = new KindModelList();
      kind_counter = new KindCounterList();
      resource_kind = new ResourceKindList();
      resource_rep = new ResourceReplicaList();
      single = new SingleValueTable();
      array = new ArrayTable();
      dictionary = new DictionaryTable();
      
      thread = new StorageThread();
   	
   }

/**
 * Establishes the Storage module, specifying a common DataModel.
 * @param common only this data model will be available
 */     
   public StorageInterface(DataModel common){ //Common=true
   
      this();
      
      kind_model = new KindModelList(common);
   	
   }

/**
 * Stores a dictionary entry in the overlay.
 * @param res the Resource-ID where it will be stored
 * @param replicaDest if a replica, the node-ID when it will be stores
 * @param replicaNum if a replica, the replica_number value (0 if not replica)
 * @param kind the kind-ID for the storage
 * @param data the data to be stored
 * @param key the key of the dictionary entry
 */    
   public void storeDictionary(ResourceId res, NodeId replicaDest, byte replicaNum, KindId kind, Opaque[] data, Opaque[] key) throws Exception{
   
      boolean[] exists = new boolean[key.length];
      
      for(int i=0; i<key.length; i++)
         exists[i] = true;
   
      storeDictionary(res, replicaDest, replicaNum, kind, data, key, exists, 0);
   
   }

/**
 * Stores a dictionary entry in the overlay.
 * @param res the Resource-ID where it will be stored
 * @param replicaDest if a replica, the node-ID when it will be stores
 * @param replicaNum if a replica, the replica_number value (0 if not replica)
 * @param kind the kind-ID for the storage
 * @param data the data to be stored
 * @param key the key of the dictionary entry
 * @param exists if the value exists (false for removing data)
 * @param generationCounter the generation_counter value
 */   
   public void storeDictionary(ResourceId res, NodeId replicaDest, byte replicaNum, KindId kind, Opaque[] data, Opaque[] key, boolean[] exists, long generationCounter) throws Exception{ // Método que almacena (con el hilo principal)
         
      long storage_time = 0; // Pending
      int life_time = 0; // Pending
      
      if(exists.length != key.length)
         throw new WrongLengthReloadException("Exists and key don't have the same length.");
      
      if(replicaNum == 0 && Module.tpi.routingTable.isResponsible(res)){
      
         if(replicaNum == 0){
            if(Module.si.kind_counter.contains(kind, res))
               generationCounter = Module.si.kind_counter.getGenerationCounter(kind, res);
            generationCounter++;     
         }
               
         NodeId[] replica = new NodeId[0];
         
         for(int j=0; j<replica.length; j++)
            storeDictionary(res, replica[j], (byte)(j+1), kind, data, key, exists, generationCounter);
            
         
         Module.si.kind_counter.add(kind, res, generationCounter); // Store
         
         for(int i=0; i<key.length; i++)
            if(!exists[i])
               Module.si.dictionary.add(kind, res, storage_time, life_time, new DictionaryKey(key[i]), new DataValue(false, new byte[0])); // Store dictionary
            else
               Module.si.dictionary.add(kind, res, storage_time, life_time, new DictionaryKey(key[i]), new DataValue(true, data[i])); // Store dictionary
      
         
         Module.si.resource_rep.add(res, replicaNum); // Store
      
      }
      
      else{
      
         SignerIdentity si = new SignerIdentity(SignerIdentityType.cert_hash, new SignerIdentityValue(HashAlgorithm.none, new byte[0], SignerIdentityType.none));
         SignatureAndHashAlgorithm saha = new SignatureAndHashAlgorithm(HashAlgorithm.none, SignatureAlgorithm.anonymous); // Used with nonexistent values
         Signature signature = new Signature(saha, si, new byte[0]);
      
      
         StoredData[] value = new StoredData[key.length];
         
         for(int i=0; i<key.length; i++){
         
            DictionaryEntry de;
            if(!exists[i])
               de = new DictionaryEntry(new DictionaryKey(key[i]), new DataValue(false, new byte[0]));
            else
               de = new DictionaryEntry(new DictionaryKey(key[i]), new DataValue(true, data[i]));
         
            StoredDataValue sdv = new StoredDataValue(de);
            
            value[i] = new StoredData(storage_time, life_time, sdv, signature);
         
         }
      
         StoreKindData[] skd = new StoreKindData[1];
         
         skd[0] = new StoreKindData(kind, generationCounter, value);
      
         StoreReq req = new StoreReq(res, replicaNum, skd);
         
      	
         Destination dest[] = new Destination[1];
      	
         if(replicaNum == 0)
            dest[0] = new Destination(res);
         
         else
            dest[0] = new Destination(replicaDest);
               
       // Create a Task
         createAppStore(dest, req);
      
      }
   
   }

/**
 * Stores a StoredData array in the overlay.
 * @param res the Resource-ID where it will be stored
 * @param replicaDest if a replica, the node-ID when it will be stores
 * @param replicaNum if a replica, the replica_number value (0 if not replica)
 * @param kind the kind-ID for the storage
 * @param value the StoredData to be stored
 * @param generationCounter the generation_counter value
 */  
   public void store(ResourceId res, NodeId replicaDest, byte replicaNum, KindId kind, StoredData[] value, long generationCounter) throws Exception{ // Used to store replicas
   
      if(kind.getDataModel() == DataModel.dictionary){
      
         Opaque[] data = new Opaque[value.length];
         boolean[] exists = new boolean[value.length];
         Opaque[] key = new Opaque[value.length];
      
         for(int i=0; i<value.length; i++){
         
            StoredDataValue std = value[i].getValue();
         
            DictionaryEntry de = std.getDictionary();
         
            DictionaryKey dk = de.getDictionaryKey();
            DataValue dv = de.getDataValue();
         
            
            boolean exist = dv.getExists();
            byte[] dkey = dk.getKey();
            byte[] val;
            if(exist)
               val = dv.getValue();
            else
               val = new byte[0];
            
            data[i] = new Opaque(32, val);
            exists[i] = exist;
            key[i] = new Opaque(16, dkey);
         
         }
      
         storeDictionary(res, replicaDest, replicaNum, kind, data, key, exists, generationCounter);
      }
      
      else
         throw new UnimplementedReloadException("Array/single in Application");
   
   }

/**
 * Fetches some dictionary entries from the overlay.
 * @param res the Resource-ID where it will be fetched
 * @param kind the kind-ID for the fetch
 * @param key the keys of the dictionary entries
 */     
   public DataStructure[] fetchDictionary(ResourceId res, KindId kind, Opaque[] key) throws Exception{
   
      long generation_counter = 0;
      
      DictionaryKey[] dkey = new DictionaryKey[key.length];
      
      for(int i=0; i<key.length; i++)
         dkey[i] = new DictionaryKey(key[i]);
      
   
      if(Module.tpi.routingTable.isResponsible(res)){
         
         return Module.si.dictionary.getData(kind, res, dkey);
      
      }
      
      else{
      
         StoredDataSpecifier[] specifier = new StoredDataSpecifier[1];
      
         specifier[0] = new StoredDataSpecifier(kind, generation_counter, dkey);
      
         FetchReq req = new FetchReq(res, specifier);
      
      
         Destination dest[] = new Destination[1];
      	
         dest[0] = new Destination(res);
         
      // Create a Task
         return createAppFetch(dest, req)[0];
      
      }
   
   }

/**
 * Stores an array entry in the overlay.
 * @param res the Resource-ID where it will be stored
 * @param kind the kind-ID for the storage
 * @param data the data to be stored
 * @param index the index of the array entry
 */    
   public void storeArray(ResourceId res, KindId kind, Opaque[] data, int[] index) throws ReloadException{
   
      throw new UnimplementedReloadException("Array in Application");
   
   }

/**
 * Stores a single entry in the overlay.
 * @param res the Resource-ID where it will be stored
 * @param kind the kind-ID for the storage
 * @param data the data to be stored
 */       
   public void storeSingle(ResourceId res, KindId kind, Opaque data) throws ReloadException{
   
      throw new UnimplementedReloadException("Single in Application");
   
   }
   
   /*public void addStoreReq(Id[] dest, ResourceId res, int num, boolean client, boolean last, byte usage) throws Exception{
   
            
   }*/

/**
 * Method to be called from Message Transport when it receives a Store Request message.
 * @param msg_body Store Request message to decode
 * @return the response (Store Answer) to the request
 */   
   public byte[] store_req(byte[] msg_body) throws Exception{
   
   
      boolean error_unknown_kind = false;
      boolean error_generation_counter_too_low = false;
   
      StoreReq req = new StoreReq(msg_body);
      
      ResourceId res = req.getResource();
      byte replicaNum = req.getReplicaNumber();
      
      if(Module.tpi.routingTable.isResponsible(res)){
         
         if(replicaNum != 0)
            throw new ErrorForbidden();
      }
      else{
         
         if(replicaNum == 0)
            throw new ErrorForbidden();
            
         else{
         	// Check that the request sender is consistent with being the responsible node (i.e., that the receiving peer does not know of a better node) and, if not, reject the request.
            if(false) // (!nodeId.sign.isPlausible())
               throw new ErrorForbidden();
         }
      }
   
      StoreKindData[] store = req.getKindData();
      
      StoreKindResponse[] skr = new StoreKindResponse[store.length];
    
   
      KindId[] kind = new KindId[store.length];  // For storing  
      
      ArrayList<KindId> unknown = new ArrayList<KindId>();
      
   	
      NodeId[] replica = new NodeId[0];

   		
      for(int i=0; i<store.length; i++){
      
         KindId kid = store[i].getKind();
      
         if(kind_model.contains(kid)){
             
            if(false) // (!kid.isAuthorized(res))
               throw new ErrorForbidden();
             
            if(replicaNum == 0){  
               if(false) // (!store_req.signature.isAuthorized(kid, res))
                  throw new ErrorForbidden();
            }
         
            long counter = store[i].getGenerationCounter();
            
            if(replicaNum == 0 && counter != 0 && Module.si.kind_counter.contains(kid, res))
               if(counter < Module.si.kind_counter.getGenerationCounter(kid, res))
                  error_generation_counter_too_low = true;
                  
            if(replicaNum != 0 && counter == 0)
               error_generation_counter_too_low = true;
         	
            StoredData[] value = store[i].getValues();
            
         
            for(int j=0; j<value.length; j++){
            
               long st = value[j].getStorageTime();
               StoredDataValue stored = value[j].getValue();
               Signature signature = value[j].getSignature();
            
            
               if(false) // (!signature.isValid())
                  throw new ErrorForbidden();
                  
            		
            // All this code is only for checking storage_time
               DataModel dm = kid.getDataModel();
               long time = 0;
            
               if(dm == DataModel.single_value)
                  time = Module.si.single.getStorageTime(kid, res);
                  
               if(dm == DataModel.array){
                  int index = stored.getArray().getIndex();
                  time = Module.si.array.getStorageTime(kid, res, index);
               }
                  
               if(dm == DataModel.dictionary){
                  DictionaryKey key = stored.getDictionary().getDictionaryKey();
                  time = Module.si.dictionary.getStorageTime(kid, res, key);
               }
               
               if(st < time)
                  throw new ErrorDataTooOld();
            		
            }
         
            skr[i] = new StoreKindResponse(kid, counter, replica);
         
         }
         
         else{
            unknown.add(kid);
            error_unknown_kind = true;
         }
                 
      }
      
      if(error_unknown_kind){
      
         KindId[] unknownkind = unknown.toArray(new KindId[0]);
         String s = new String();
         for(int i=0; i<unknownkind.length; i++)
            s = s.concat(unknownkind[i].getId()+"-");
      
         throw new ErrorUnknownKind(s);
      }
      
      if(error_generation_counter_too_low){
      
         StoreAns ans = new StoreAns(skr);
         String s = new String(ans.getBytes());
      
         throw new ErrorGenerationCounterTooLow(s);
      }
      
      
      for(int i=0; i<store.length; i++){
         
         kind[i] = store[i].getKind();
         long counter = store[i].getGenerationCounter();
         if(replicaNum == 0){
            if(Module.si.kind_counter.contains(kind[i], res))
               counter = Module.si.kind_counter.getGenerationCounter(kind[i], res);
            counter++;     
         }
         StoredData[] value = store[i].getValues();
         
      	
         for(int j=0; j<replica.length; j++)
            store(res, replica[j], (byte)(j+1), kind[i], value, counter);
            
         Module.si.kind_counter.add(kind[i], res, counter); // Store
            
         for(int j=0; j<value.length; j++){
            
            long st = value[j].getStorageTime();
            int lt = value[j].getLifeTime();
            StoredDataValue stored = value[j].getValue();
            	
            DataModel dm = kind[i].getDataModel();
            
            if(dm == DataModel.single_value)
               Module.si.single.add(kind[i], res, st, lt, stored.getSingleValue()); // Store single value
                  
            if(dm == DataModel.array){
               ArrayEntry ae = stored.getArray();
               Module.si.array.add(kind[i], res, st, lt, ae.getIndex(), ae.getDataValue()); // Store array
            }
                  
            if(dm == DataModel.dictionary){
               DictionaryEntry de = stored.getDictionary();
               Module.si.dictionary.add(kind[i], res, st, lt, de.getDictionaryKey(), de.getDataValue()); // Store dictionary
            }
            
         }
         
      }
         
      Module.si.resource_rep.add(res, replicaNum); // Store
        
                 
      StoreAns ans = new StoreAns(skr);
      
      return ans.getBytes();      
      	
   }
   
/**
 * Method to be called from Message Transport when it receives a Fetch Request message.
 * @param msg_body Fetch Request message to decode
 * @return the response (Fetch Answer) to the request
 */     
   public byte[] fetch_req(byte[] msg_body) throws Exception{
   
   
      FetchReq req = new FetchReq(msg_body);
      
      ResourceId res = req.getResource();
      StoredDataSpecifier[] specifier = req.getSpecifiers();
      
      FetchKindResponse[] fkr = new FetchKindResponse[specifier.length];
   
      
      for(int i=0; i<specifier.length; i++){
      
         KindId kind = specifier[i].getKind();
         long generation = specifier[i].getGeneration();
         
         StoredData[] retData = null;
         
         SignerIdentity si = new SignerIdentity(SignerIdentityType.cert_hash, new SignerIdentityValue(HashAlgorithm.none, new byte[0], SignerIdentityType.none));
         SignatureAndHashAlgorithm saha = new SignatureAndHashAlgorithm(HashAlgorithm.none, SignatureAlgorithm.anonymous); // Used with nonexistent values
         Signature signature = new Signature(saha, si, new byte[0]);
         
      
         if( (generation != kind_counter.getGenerationCounter(kind, res) || generation == 0) && isStored(res, kind) ){
         
            
            if(specifier[i].getDataModel() == DataModel.single_value){
            
               DataStructure data = single.getData(kind, res);
               
               retData = new StoredData[1];
               retData[0] = new StoredData(data, signature);
            
            }
            
            else if(specifier[i].getDataModel() == DataModel.array){
            
               ArrayRange[] range = specifier[i].getIndices();
               int[] index = Utils.rangeToIndexes(range);
            
               DataStructure[] data = array.getData(kind, res, index);
               
               retData = new StoredData[data.length];
               for(int j=0; j<data.length; j++)
                  retData[j] = new StoredData(data[j], index[j], signature);
            
            }
            
            else if(specifier[i].getDataModel() == DataModel.dictionary){
            
               DictionaryKey[] key = specifier[i].getKeys();
               DataStructure[] data = dictionary.getData(kind, res, key);
               
               retData = new StoredData[data.length];
               for(int j=0; j<data.length; j++)
                  retData[j] = new StoredData(data[j], signature);
            
            }
            
            else
               throw new WrongTypeReloadException();
         
         }
         
         else
         
            retData = new StoredData[0];
         
         
         fkr[i] = new FetchKindResponse(kind, specifier[i].getGeneration(), retData);
      
      }
   
         
      FetchAns ans = new FetchAns(fkr);
      
      return ans.getBytes();      
      	
   }
   
/**
 * Method to be called from Message Transport when it receives a Find Request message.
 * @param msg_body Find Request message to decode
 * @return the response (Find Answer) to the request
 */    
   public byte[] find_req(byte[] msg_body) throws Exception{
   
   
      FindReq req = new FindReq(msg_body);
      
      ResourceId res = req.getResource();
      KindId[] kind = req.getKinds();
      
      FindKindData[] fkd = new FindKindData[kind.length];
      
      if(!Module.tpi.routingTable.isResponsible(res))
         throw new ErrorNotFound();
      
   	
      for(int i=0; i<kind.length; i++){
            
         if(resource_kind.contains(kind[i], res))
            fkd[i] = new FindKindData(kind[i], res);
         
         else
            fkd[i] = new FindKindData(kind[i], new ResourceId(0));
         
      }
      
      FindAns ans = new FindAns(fkd);
      
      return ans.getBytes();      
      	
   }
   
/**
 * Method to be called from Message Transport when it receives a Stat Request message.
 * @param msg_body Stat Request message to decode
 * @return the response (Stat Answer) to the request
 */   
   public byte[] stat_req(byte[] msg_body) throws Exception{
   
   
      StatReq req = new StatReq(msg_body);
      
      ResourceId res = req.getResource();
      StoredDataSpecifier[] specifier = req.getSpecifiers();
      
      StatKindResponse[] skr = new StatKindResponse[specifier.length];
   
      
      for(int i=0; i<specifier.length; i++){
      
         KindId kind = specifier[i].getKind();
         long generation = specifier[i].getGeneration();
         
         StoredMetaData[] retData = null;
         
      
         if( (generation != kind_counter.getGenerationCounter(kind, res) || generation == 0) && isStored(res, kind) ){
         
         
            if(specifier[i].getDataModel() == DataModel.single_value){
            
               DataStructure data = single.getData(kind, res);
               
               retData = new StoredMetaData[1];
               retData[0] = new StoredMetaData(data);
            
            }
            
            else if(specifier[i].getDataModel() == DataModel.array){
            
               ArrayRange[] range = specifier[i].getIndices();
               int[] index = Utils.rangeToIndexes(range);
            
               DataStructure[] data = array.getData(kind, res, index);
               
               retData = new StoredMetaData[data.length];
               for(int j=0; j<data.length; j++)
                  retData[j] = new StoredMetaData(data[j], index[j]);
            
            }
            
            else if(specifier[i].getDataModel() == DataModel.dictionary){
            
               DictionaryKey[] key = specifier[i].getKeys();
               DataStructure[] data = dictionary.getData(kind, res, key);
               
               retData = new StoredMetaData[data.length];
               for(int j=0; j<data.length; j++)
                  retData[j] = new StoredMetaData(data[j]);
            
            }
            
            else
               throw new WrongTypeReloadException();
         
         }
         
         else{
         
            retData = new StoredMetaData[0];
         }
         
         skr[i] = new StatKindResponse(kind, specifier[i].getGeneration(), retData);
      
      }
   
      
      StatAns ans = new StatAns(skr);
      
      return ans.getBytes();      
      	
   }
   
   private boolean isStored(ResourceId rid, KindId kid) throws ReloadException{
   
      KindId[] kind = resource_kind.getKinds(rid);
   
      for(int i=0; i<kind.length; i++)
         if(kind[i].getId() == kid.getId())
            return true;
   
      return false;
   
   }

/**
 * Creates a Joining Store Task.
 * @param dest the destination of the store
 * @param numLink the link number
 * @param client true if connection is client, false if is server
 */    
   public void createJoiningStore(NodeId dest, int numLink, boolean client){  
   	
      JoiningStoreTask task = new JoiningStoreTask(thread, dest, numLink, client);
      
      synchronized(thread){
      
         boolean empty = thread.isEmpty();
      
         thread.add(task);
      
         if(empty)
            thread.notify();
           
      }
      
   }
   
/**
 * Creates a App Store Task.
 * @param dest the destination of the store
 * @param req the Store Request stucture to be send
 */     
   public void createAppStore(Destination[] dest, StoreReq req){  
   	
      AppStoreTask task = new AppStoreTask(thread, dest, req);
      
      synchronized(thread){
      
         boolean empty = thread.isEmpty();
      
         thread.add(task);
      
         if(empty)
            thread.notify();
           
      }
      
   }

/**
 * Creates a App Store Task.
 * @param dest the destination of the store
 * @param req the Fetch Request stucture to be send
 */    
   public DataStructure[][] createAppFetch(Destination[] dest, FetchReq req) throws InterruptedException{  
   	
      AppFetchTask task = new AppFetchTask(thread, dest, req);
      
      synchronized(thread){
      
         boolean empty = thread.isEmpty();
      
         thread.add(task);
      
         if(empty)
            thread.notify();
      }
      	
      synchronized(this){
         this.wait();
      }
   
      return thread.getDataStructure();
   
   }

/**
 * Does a Message Digest.
 * @param input the byte array to do the digest
 */    
   public byte[] digest(byte[] input) throws java.security.NoSuchAlgorithmException{
   
      MessageDigest md = MessageDigest.getInstance(hash_alg.getName());
      md.update(input);
      return md.digest();
      
   }

/**
 * Returns the hash algorithm.
 * @return the HashAlgorith object
 */    
   public HashAlgorithm getHashAlgorithm(){
   
      return hash_alg;
   
   }

/**
 * Returns the storage thread.
 * @return the StorageThread object
 */  	
   public StorageThread getStorageThread(){
   
      return thread;
   
   }

}