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
package reload.Topology.Plugins;

import reload.Message.*;
import reload.Topology.Plugins.Chord.Task.*;
import reload.Common.*;
import reload.Common.Exception.*;

import java.util.*;


public class ChordThread extends Thread {


   private ArrayDeque<ChordTask> queue;
   
   private boolean empty;



   public ChordThread(){
   
      queue = new ArrayDeque<ChordTask>();
      
      empty = true;
         
   }
   
   public void run() {
   
      
      while(true){
            
         synchronized(this){
         
            if(isEmpty()){
            
               try{
                  wait();
               }
               catch(InterruptedException e){
                  
                  e.printStackTrace();
                  
               }
               
            }
         }
         
         try{
         
            ChordTask task = queue.getFirst();
            ChordTaskType type = task.getType();
         
            switch(type.getValue()){
            
               case 0:
                  throw new WrongTypeReloadException("Undefined Task");
               case 1:
                  ChordUpdateTask update = (ChordUpdateTask)task;
                  update.start();  
                  break;
               case 2:
                  ChordLeaveTask leave = (ChordLeaveTask)task;
                  leave.start(); 
                  break;
               case 3:
                  ChordJoinTask join = (ChordJoinTask)task;
                  join.start(); 
                  break;
               default:
                  throw new WrongTypeReloadException();
            }
         
            queue.removeFirst();
         
         }
         catch(Exception e){
                  
            e.printStackTrace();
                  
         }
      	
         
      }
      
   }
	
   public boolean isEmpty(){
   
      return queue.size() == 0;
   
   }
   
   public void add(ChordTask task){
   
      queue.add(task);
   
   }
   
   public ChordTask getFirst(){
   
      return queue.getFirst();
   
   }
   
   public void setMessage(Message message){
   
      queue.getFirst().setMessage(message);
   
   }
      
}

