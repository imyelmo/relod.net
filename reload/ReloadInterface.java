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

package reload;

import reload.Common.*;
import reload.Common.Exception.*;
import reload.Message.MessageTransport;
import reload.Storage.StorageInterface;
import reload.Storage.Data.*;

/**
* ReloadInterface class is the interface of the RELOAD protocol.
* @author Malosa 
* @version 0.1
*/	 
public class ReloadInterface{

   private int port;
   private boolean first;

/**
 * Establishes the RELOAD protocol.
 * @param port the port used by RELOAD
 * @param first if this is the first node to initialize in the overlay
 */    
   public ReloadInterface(int port, boolean first) throws Exception{
   
      this.port = port;
      this.first = first;
   
      Module.si = new StorageInterface();
      
      Module.msg = new MessageTransport(port, first);
   
   }

/**
 * Establishes the RELOAD protocol, specifying a common DataModel.
 * @param port the port used by RELOAD
 * @param first if this is the first node to initialize in the overlay
 * @param dataModel only this data model will be available
 */    
   public ReloadInterface(int port, boolean first, DataModel dataModel) throws Exception{
   
      this.port = port;
      this.first = first;
   
      Module.si = new StorageInterface(dataModel);
      
      Module.msg = new MessageTransport(port, first);
   
   }

/**
 * Initializes the RELOAD protocol. It is called after the constructor.
 * @return true if it could start and connect to the Admitting Peer
 */  
   public boolean start() throws Exception{
   
      return Module.msg.start();
          
   }
   
   public boolean commonCommand(String read) throws Exception{ // true=exit, false=normal
   
      if(read.length() > 4)
         read = read.substring(0, 4);
   
      switch(read.toLowerCase()){
         
         case "":
            break;
         case "hi":
         case "hell":
            System.out.println("Hello!");
            break;
         case "id":
            System.out.println(Module.tpi.routingTable.printId());
            break;
         case "neig":
            System.out.println(Module.tpi.routingTable.printNeighbors());
            break;
         case "fing":
            System.out.println(Module.tpi.routingTable.printFingers());
            break;
         case "dict":
            System.out.println(Module.si.dictionary.print());
            break;
         case "ping":
            Module.falm.pingAllNodes();
            Thread.currentThread().sleep(150);
            break;
         case "attf":
            Module.falm.createAttachFingersRoute();
            break;
         case "help":
             System.out.println("fetch \"uri\": It fetchs the info of the \"uri\"");
             System.out.println("remove \"uri\": It removes the info of the \"uri\". Check source code");
             System.out.println("sip \"uri\": It opens connection with the \"uri\"");
             System.out.println("id: It shows Node-ID");
             System.out.println("neig: It shows neighbours");
             System.out.println("fing: It shows fingers");
             System.out.println("dict: It shows dictionary");
             System.out.println("ping: It pings all nodes");
             System.out.println("attf: It attaches a finger");
             System.out.println("bye: It makes node to leave the overlay");
             System.out.println("help: It shows this information");
             break;
         case "leav":
         case "exit":
         case "quit":
         case "bye":
            Module.tpi.createLeave();
            return true;
         default:
            System.out.println("Unknown command. Use \"help\" to show the diffente commands");
      }
      
      return false;
   
   }

/**
 * Stores a dictionary entry in the overlay.
 * @param resource the Resource-ID where it will be stored
 * @param kind the kind-ID for the storage
 * @param data the data to be stored
 * @param key the key of the dictionary entry
 */  
   public void storeDictionary(ResourceId resource, KindId kind, Opaque[] data, Opaque[] key) throws Exception{
   
      Module.si.storeDictionary(resource, null, (byte)0, kind, data, key);
   
   }

/**
 * Stores an array entry in the overlay.
 * @param resource the Resource-ID where it will be stored
 * @param kind the kind-ID for the storage
 * @param data the data to be stored
 * @param index the index of the array entry
 */     
   public void storeArray(ResourceId resource, KindId kind, Opaque[] data, int[] index) throws ReloadException{
   
      Module.si.storeArray(resource, kind, data, index);
   
   }

/**
 * Stores a single entry in the overlay.
 * @param resource the Resource-ID where it will be stored
 * @param kind the kind-ID for the storage
 * @param data the data to be stored
 */    
   public void storeSingle(ResourceId resource, KindId kind, Opaque data) throws ReloadException{
   
      Module.si.storeSingle(resource, kind, data);
   
   }

/**
 * Fetches some dictionary entries from the overlay.
 * @param resource the Resource-ID where it will be fetched
 * @param kind the kind-ID for the fetch
 * @param key the keys of the dictionary entries
 */     
   public DataStructure[] fetchDictionary(ResourceId resource, KindId kind, Opaque[] key) throws Exception{
   
      return Module.si.fetchDictionary(resource, kind, key);
   
   }

/**
 * Creates an App Attach Task.
 * @param node NodeId to connect to
 * @param application the app code
 */      
   public void appAttach(NodeId node, short application){
   
      Module.falm.createAppAttach(node, application);
   
   }

/**
 * Removes some dictionary entries from the overlay.
 * @param resource the Resource-ID where it will be removed
 * @param kind the kind-ID to remove
 * @param key the keys of the dictionary entries to be removed
 */    
   public void removeDictionary(ResourceId resource, KindId kind, Opaque[] key) throws Exception{
   
      boolean[] exists = new boolean[key.length];
      
      for(int i=0; i<key.length; i++)
         exists[i] = false;
   
      Module.si.storeDictionary(resource, null, (byte)0, kind, new Opaque[0], key, exists, 0);
   
   }

/**
 * Adds a a DataModel to a Kind-ID.
 * @param id the Kind-ID to be added
 * @param name the name for this Kind-ID
 * @param dataModel the DataModel to be added
 */     
   public void addKindModel(KindId id, String name, DataModel dataModel) throws ReloadException{
   
      Module.si.kind_model.add(id, name, dataModel);
   
   }

/**
 * Adds a a DataModel name.
 * @param id the Kind-ID to be added
 * @param name the name for this Kind-ID
 */       
   public void addKindModel(KindId id, String name) throws ReloadException{
   
      Module.si.kind_model.add(id, name);
   
   }

}