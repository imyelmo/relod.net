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

   public class ChordJoinTask extends ChordTask{
   
      private NodeId node;
   
      public ChordJoinTask(ChordThread thread, NodeId node){
      
         super(thread);
         
         type = ChordTaskType.join;
         
         this.node = node;
         
      }
      
      public void start() throws Exception{
         
         send_receive(node);
         
      }
      
      private void send_receive(NodeId node) throws Exception{
               
         ChordJoinReq req = new ChordJoinReq(Module.tpi.routingTable.getThisNode());
                  
         byte[] msg_body = req.getBytes();
         
         short code = 15; //JoinReq
         
         Module.msg.send(msg_body, code, node);
         
               	
         Message mes = receive();
            
         code = mes.getMessageCode();
         	
         if (code != 16) // JoinAns
            throw new WrongPacketReloadException(16);
         
         
         msg_body = mes.getMessageBody();
            	
         ChordJoinAns join = new ChordJoinAns(msg_body);
      
      }
      
   }

