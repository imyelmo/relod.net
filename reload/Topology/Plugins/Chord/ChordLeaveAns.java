  
   package reload.Topology.Plugins.Chord;
   
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.io.*;
   
   public class ChordLeaveAns{
   
      
      
      public ChordLeaveAns (){
      
      
      }
      
      public ChordLeaveAns (byte[] data) throws ReloadException{
      
         if (data.length != 0)
            throw new WrongLengthReloadException("Wrong LeaveAns packet.");
      
      }
      
      public byte[] getBytes(){
      
         return new byte[0];
      
      }
   
   }