  
   package reload.Topology.Plugins.Chord;
   
   import reload.Common.*;
   import reload.Common.Exception.*;
   import java.io.*;
   
   public class ChordJoinAns{

   
      public ChordJoinAns (){
      
      
      }
      
      public ChordJoinAns (byte[] data) throws ReloadException{
      
         if (data.length != 0)
            throw new WrongLengthReloadException("Wrong JoinAns packet.");
      
      }
      
      public byte[] getBytes(){
      
         return new byte[0];
      
      }
   
   }