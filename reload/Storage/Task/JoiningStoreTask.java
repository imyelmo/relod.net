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
   package reload.Storage.Task;
   
   import reload.Message.*;
   import reload.Storage.*;
   import reload.Storage.Data.*;
   import reload.Topology.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   
   import reload.Message.Security.*;
   import reload.Message.Security.TLS.*;
   
   import java.util.*;
   
   public class JoiningStoreTask extends StorageTask{
   
      private NodeId joining;
      private int numLink;
      private boolean client;
      
   	
      public JoiningStoreTask(StorageThread thread, NodeId joining, int numLink, boolean client){
      
         super(thread);
      	
         type = TaskType.joining_store;
         
         this.joining = joining;
         this.numLink = numLink;
         this.client = client;
            
      }  
      
      public Object start() throws Exception{
      
         Thread.currentThread().sleep(50); // We wait so JoinAns is sent first
      
         ArrayList<ResourceId> movingRes = store();
         
         if(movingRes.size() > 0)
            deleteOldData(movingRes);
            
         update();
         
         
         return null;
         
      }
      
      private ArrayList<ResourceId> store() throws Exception{
      
         ResourceId[] storedRes = Module.si.resource_kind.getResources();
         ArrayList<ResourceId> movingRes = new ArrayList<ResourceId>();
       
         for(int i=0; i<storedRes.length; i++){
         
            if(Module.tpi.routingTable.isResponsible(joining, storedRes[i]))
               movingRes.add(storedRes[i]);
         }
         
         for(int i=0; i<movingRes.size(); i++)
            doStore(movingRes.get(i));
            
         return movingRes;
         
      }
      
      private void doStore(ResourceId resource) throws Exception{
            
         StoreReq req = createStoreReq(resource);
         
         byte[] msg_body = req.getBytes();
         
         short code = 7; // StoreReq
         
         Module.msg.send(msg_body, code, joining, numLink, client);
         
               
         Message mes = receive();
            
         code = mes.getMessageCode();
      		
         if (code != 8) // StoreAns
            throw new WrongPacketReloadException(8);
         
         msg_body = mes.getMessageBody();
      	
         StoreAns ans = new StoreAns(msg_body);
         
      	
         StoreKindResponse[] skr = ans.getStoreKindResponses();
      	
         for(int i=0; i<skr.length; i++){
         
            KindId kid = skr[i].getKind();
            
            long counter = skr[i].getGenerationCounter();
               
            NodeId[] replica = skr[i].getReplicas();
            
            for(int j=0; j<replica.length; j++){
               
               NodeId ID = replica[j];
                                		
            }
            
         }
      
      }
      
      private StoreReq createStoreReq(ResourceId res) throws Exception{
      
      
         byte replica = Module.si.resource_rep.getReplica(res);
      
         KindId[] kind = Module.si.resource_kind.getKinds(res);
         
         StoreKindData[] skd = new StoreKindData[kind.length];
         
         for(int i=0; i<kind.length; i++){
         
            DataModel dataModel = kind[i].getDataModel();
            
            SignerIdentity si = new SignerIdentity(SignerIdentityType.cert_hash, new SignerIdentityValue(HashAlgorithm.none, new byte[0], SignerIdentityType.none));
            SignatureAndHashAlgorithm saha = new SignatureAndHashAlgorithm(HashAlgorithm.none, SignatureAlgorithm.anonymous); // Used with nonexistent values
            Signature signature = new Signature(saha, si, new byte[0]);
            
            StoredData[] sd = null;
         
            if(dataModel == DataModel.single_value){
               
               DataStructure ds = Module.si.single.getData(kind[i], res);
               sd = new StoredData[1];
               
               sd[0] = new StoredData(ds, signature);              
            }
               
            else if(dataModel == DataModel.array){
               
               DataStructure[] ds = Module.si.array.getData(kind[i], res);
               sd = new StoredData[ds.length];
               
               for(int j=0; j<sd.length; j++)
                  sd[j] = new StoredData(ds[j], j, signature);                 
            }
               
            else if(dataModel == DataModel.dictionary){
            
               DataStructure[] ds = Module.si.dictionary.getData(kind[i], res);
               sd = new StoredData[ds.length];
               
               for(int j=0; j<sd.length; j++)
                  sd[j] = new StoredData(ds[j], signature); 
            }
            
            else
               throw new WrongTypeReloadException();
         
         
            skd[i] = new StoreKindData(kind[i], Module.si.kind_counter.getGenerationCounter(kind[i], res), sd);
         
         }
      
         return new StoreReq(res, replica, skd);
            
      }
      
      public void deleteOldData(ArrayList<ResourceId> movingRes) throws ReloadException{
      
      
         for(int z=0; z<movingRes.size(); z++){
               
            ResourceId res = movingRes.get(z);
         
                     
            if(Module.si.kind_model.isCommon()){
               DataModel dataModel = Module.si.kind_model.getDataModel();
            
               if(dataModel == DataModel.single_value)
                  Module.si.single.delete(res); // Delete        
               
               else if(dataModel == DataModel.array)
                  Module.si.array.delete(res); // Delete
               
               else if(dataModel == DataModel.dictionary) 
                  Module.si.dictionary.delete(res); // Delete
                  
               else
                  throw new WrongTypeReloadException();		
            }
            
            else{ // No common DataModel
            
               Module.si.single.delete(res); // Delete           
               Module.si.array.delete(res); // Delete    
               Module.si.dictionary.delete(res); // Delete   
            }     
         
         }
      
      
      }
   	
      public void update() throws ReloadException{
      
         Module.tpi.routingTable.addNeighbor(joining);
         
         Module.tpi.createUpdate(joining, (byte)3); // full
      
      }
      
   }

