   package reload.Topology.Plugins.Chord;

   import java.io.*;
   import reload.Common.*;
   import reload.Common.Exception.*;
   import reload.Message.Forwarding.Destination;
   
   public class ChordRouteQueryReq{
   
      private boolean send_update;
      private Destination destination;
      private Opaque data; //16-bit   
      
      public ChordRouteQueryReq (boolean send_update, Destination destination, byte[] data) throws ReloadException{
      
         this.send_update = send_update;
         this.destination = destination;
         this.data = new Opaque(16, data);
      
      }
      
      public ChordRouteQueryReq (byte[] data) throws Exception{
      
         send_update = Utils.toBoolean(data);
         destination = new Destination(Utils.cutArray(data, 1));
         int length = destination.getBytes().length;
         this.data = new Opaque(16, data, 1+length);
      
      }
      
      public byte[] getBytes() throws Exception{
      
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         baos.write(Utils.toByte(send_update));
         baos.write(destination.getBytes());
         baos.write(data.getBytes());
      
         
         return baos.toByteArray();
      
      }
      
      public boolean getSendUpdate(){
      
         return send_update;
      
      }
   	
      public Destination getDestination(){
      
         return destination;
      
      }
   	
      public byte[] getData() throws IOException{
      
         return data.getContent();
      
      }
   
   
   }