
   package reload.Topology.Plugins;

	/* Constructor:
   ProbeInformationType type = ProbeInformationType.valueOf(1);
   ProbeInformationType type = ProbeInformationType.responsible_set;
   */
 
   public enum ProbeInformationType {
      UNDEFINED(					 "undefined",						 (byte)-1),
      reservedProbeInformation("reserved probe information", (byte)0),
      responsible_set(			 "responsible set",				 (byte)1),
      num_resources(				 "number of resources",			 (byte)2),
      uptime(						 "uptime",							 (byte)3);
   
      
      final String name;
      final byte value;
      
      private ProbeInformationType(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static ProbeInformationType valueOf(int value) {
         ProbeInformationType type = UNDEFINED;
         switch (value) {
            case 0:
               type = reservedProbeInformation;
               break;
            case 1:
               type = responsible_set;
               break;
            case 2:
               type = num_resources;
               break;
            case 3:
               type = uptime;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }