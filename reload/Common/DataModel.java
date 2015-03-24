   package reload.Common;

	/* Constructor:
   DataModel type = DataModel.valueOf(1);
   DataModel type = DataModel.single_value;
   */
 
   public enum DataModel {
      UNDEFINED(   "undefined",	  (byte)-1),
      single_value("single value",	(byte)1),
      array(		 "array",			(byte)2),
      dictionary(  "dictionary",		(byte)3);
   
      
      final String name;
      final byte value;
      
      private DataModel(String name, byte value) {
         this.name = name;
         this.value = value;
      }
      
      
      public static DataModel valueOf(int value) {
         DataModel type = UNDEFINED;
         switch (value) {
            case 1:
               type = single_value;
               break;
            case 2:
               type = array;
               break;
            case 3:
               type = dictionary;
               break;
         }
         
         return type;
      }
      
      public byte getBytes(){
      
         return value;
      
      }
   	
   }