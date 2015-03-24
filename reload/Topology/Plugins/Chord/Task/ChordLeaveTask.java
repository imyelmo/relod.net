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
   package reload.Topology.Plugins.Chord.Task;
   
   import reload.Message.*;
   import reload.Topology.Plugins.ChordThread;
   import reload.Topology.Plugins.Chord.*;
   import reload.Common.*;
   import reload.Common.Exception.*;

   public class ChordLeaveTask extends ChordTask{
   
   
      public ChordLeaveTask(ChordThread thread){
      
         super(thread);
         
         type = ChordTaskType.leave;
         
      }
      
      public void start() throws Exception{
         
         NodeId[] pred = Module.tpi.routingTable.getPredecessors().clone();
         NodeId[] succ = Module.tpi.routingTable.getSuccessors().clone();
         NodeId thisId = Module.tpi.routingTable.getThisNode();
      
         for(int i=0; i<pred.length; i++)
            send_receive(pred, ChordLeaveType.from_pred, thisId, i);
            
         for(int i=0; i<succ.length; i++)
            send_receive(succ, ChordLeaveType.from_succ, thisId, i);
            
         Module.falm.closeServer();
         
         System.out.println("Closing...");
      	
         System.exit(0);
         
      }
      
      private void send_receive(NodeId[] peer, ChordLeaveType type, NodeId thisId, int i) throws Exception{
            
         ChordLeaveData data = new ChordLeaveData(peer, type);
         
         ChordLeaveReq req = new ChordLeaveReq(thisId, data);
                  
         byte[] msg_body = req.getBytes();
         
         short code = 17; //LeaveReq
         
         Module.msg.send(msg_body, code, peer[i]);
         
                  
         Message mes = receive();
            
         code = mes.getMessageCode();
         	
         if (code != 18) // LeaveAns
            throw new WrongPacketReloadException(18);
         
         
         msg_body = mes.getMessageBody();
         
         
         ChordLeaveAns ans = new ChordLeaveAns(msg_body);
         
      	
         boolean client = Module.tpi.connectionTable.isClient(peer[i]);
         int num = Module.tpi.connectionTable.getNumLink(peer[i], client);
      	
         Module.falm.close(num, client);
      
      }
      
   }

