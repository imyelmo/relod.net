   package reload.Common;
		
   import reload.Common.Exception.*;
 
   public enum MessageCode {
   
      invalid("Invalid", (short)0),
      probe_req("Probe Request", (short)1),
      probe_ans("Probe Answer", (short)2),
      attach_req("Attach Request", (short)3),
      attach_ans("Attach Answer", (short)4),
      unused("Unused", (short)5),
      store_req("Store Request", (short)7),
      store_ans("Store Answer", (short)8),
      fetch_req("Fetch Request", (short)9),
      fetch_ans("Fetch Answer", (short)10),
      find_req("Find Request", (short)13),
      find_ans("Find Answer", (short)14),
      join_req("Join Request", (short)15),
      join_ans("Join Answer", (short)16),
      leave_req("Leave Request", (short)17),
      leave_ans("Leave Answer", (short)18),
      update_req("Update Request", (short)19),
      update_ans("Update Answer", (short)20),
      route_query_req("Route Query Request", (short)21),
      route_query_ans("Route Query Answer", (short)22),
      ping_req("Ping Request", (short)23),
      ping_ans("Ping Answer", (short)24),
      stat_req("Stat Request", (short)25),
      stat_ans("Stat Answer", (short)26),
      app_attach_req("Application Attach Request", (short)29),
      app_attach_ans("Application Attach Answer", (short)30),
      config_update_req("Configuration Update Request", (short)33),
      config_update_ans("Configuration Update Answer", (short)34),
      exp_a_req("Exp A Request", (short)35),
      exp_a_ans("Exp A Answer", (short)36),
      exp_b_req("Exp B Request", (short)37),
      exp_b_ans("Exp B Answer", (short)38),
      reserved("Reserved", (short)0x8000),
      error("Error", (short)0xffff);
   
      
      private final String name;
      private final short value;
      
      private MessageCode(String name, short value) {
      
         this.name = name;
         this.value = value;
      }  
      
      public static MessageCode valueOf(int value) throws ReloadException{
      
         if(value < 0 || value > 0xffff)
            throw new WrongTypeReloadException("Input value must be a 16-bit positive short number.");
      
         if(value >= 0x8000 && value <= 0xfffe)
            return reserved;
      
         MessageCode code;
         
         switch (value) {
            case 0:
               code = invalid;
               break;
            case 1:
               code = probe_req;
               break;
            case 2:
               code = probe_ans;
               break;
            case 3:
               code = attach_req;
               break;
            case 4:
               code = attach_ans;
               break;     
            case 7:
               code = store_req;
               break;
            case 8:
               code = store_ans;
               break;
            case 9:
               code = fetch_req;
               break;
            case 10:
               code = fetch_ans;
               break;   
            case 13:
               code = find_req;
               break;
            case 14:
               code = find_ans; 
               break;
            case 15:
               code = join_req;
               break;
            case 16:
               code = join_ans;
               break;
            case 17:
               code = leave_req;
               break;
            case 18:
               code = leave_ans;
               break;
            case 19:
               code = update_req;
               break;
            case 20:
               code = update_ans;
               break;
            case 21:
               code = route_query_req;
               break;
            case 22:
               code = route_query_ans;
               break;
            case 23:
               code = ping_req;
               break;
            case 24:
               code = ping_ans;
               break;
            case 25:
               code = stat_req;
               break;
            case 26:
               code = stat_ans;
               break;
            case 29:
               code = app_attach_req;
               break;
            case 30:
               code = app_attach_ans;
               break;
            case 33:
               code = config_update_req;
               break;
            case 34:
               code = config_update_ans;
               break;
            case 35:
               code = exp_a_req;
               break;
            case 36:
               code = exp_a_ans;
               break;
            case 37:
               code = exp_b_req;
               break;
            case 38:
               code = exp_b_ans;
               break;
            case 0xffff:
               code = error;
               break;	
            default:
               code = unused;
         }
         
         return code;
      }
      
      public short getValue(){
      
         return value;
      
      }
      
      public String getName(){
      
         return name;
      
      }
   	
   }