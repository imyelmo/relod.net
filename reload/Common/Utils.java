   package reload.Common;

   import java.util.UUID;
   import java.math.BigInteger;
   import reload.Storage.Data.ArrayRange;
   import reload.Common.Exception.*;
  
   public class Utils{
   
      public static byte[] toByte(int value) {
         return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
      }
      
      public static byte[] to3Bytes(int value) {
         return new byte[] {
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
      }
      
      public static byte[] toByte(short value) {
         return new byte[] {
                (byte)(value >>> 8),
                (byte)value};
      }
      
      public static byte[] toByte(long value) {
         return new byte[] {
                (byte)(value >>> 56),
                (byte)(value >>> 48),
                (byte)(value >>> 40),
                (byte)(value >>> 32),
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
      }
      
      public static byte[] toByte(UUID value) {
         return new byte[] {
                (byte)(value.getMostSignificantBits() >>> 56),
                (byte)(value.getMostSignificantBits() >>> 48),
                (byte)(value.getMostSignificantBits() >>> 40),
                (byte)(value.getMostSignificantBits() >>> 32),
                (byte)(value.getMostSignificantBits() >>> 24),
                (byte)(value.getMostSignificantBits() >>> 16),
                (byte)(value.getMostSignificantBits() >>> 8),
                (byte) value.getMostSignificantBits(),
                (byte)(value.getLeastSignificantBits() >>> 56),
                (byte)(value.getLeastSignificantBits() >>> 48),
                (byte)(value.getLeastSignificantBits() >>> 40),
                (byte)(value.getLeastSignificantBits() >>> 32),
                (byte)(value.getLeastSignificantBits() >>> 24),
                (byte)(value.getLeastSignificantBits() >>> 16),
                (byte)(value.getLeastSignificantBits() >>> 8),
                (byte) value.getLeastSignificantBits() };
      }
      
      public static byte toByte(boolean value) {
      
         if(value)
            return (byte)1;
         
         else
            return (byte)0;
      
      }
      
      public static int toInt(byte[] b, int offset) {
      
         int value = 0;
         for (int i = 0; i<4; i++)
            value = (value << 8) + (b[i + offset] & 0xff);
         
         return value;
      
      }
      
      public static int to3Bytes(byte[] b, int offset) {
      
         int value = 0;
         for (int i = 0; i<3; i++)
            value = (value << 8) + (b[i + offset] & 0xff);
         
         return value;
      
      }
      
      public static long toLong(byte[] b, int offset) {
      
         long value = 0;
      	/* http://stackoverflow.com/questions/1026761/how-to-convert-a-byte-array-to-its-numeric-value-java */
         for (int i = 0; i<8; i++)
            value = (value << 8) + (b[i + offset] & 0xff);
      
         
         return value;
      }
      
      public static UUID toUUID(byte[] b, int offset) {
      
         byte[] most = Utils.cutArray(b, 8, offset);
         byte[] least = Utils.cutArray(b, 8, offset+8);
      
         return new UUID(toLong(most, 0), toLong(least, 0));
      }
      
      public static short toShort(byte[] b, int offset) {
      
         short value = 0;
         for (int i = 0; i<2; i++)
            value = (short)((value << 8) + (b[i + offset] & 0xff));
         
         return value;
      }
      
      public static boolean toBoolean(byte b) throws ReloadException{
      
         if (b==1)
            return true;
         
         else if (b==0)
            return false;
         
         else
            throw new NotTrueNorFalseReloadException();
      }
      
      public static boolean toBoolean(byte b[]) throws ReloadException{
      
         return toBoolean(b[0]);
      }
      
      public static byte[] cutArray(byte[] b, int size, int offset) {
      
         byte value[] = new byte[size];
         for (int i=0; i < size; i++) {
            value[i] = b[offset+i];
         }
         
         return value;
      }
   	
      public static byte[] cutArray(byte[] b, int offset){
      
         return cutArray(b, b.length-offset, offset);
      
      }
      
      public static boolean equals(byte[] array1, byte[] array2){
      
         if(array1.length != array2.length)
            return false;
      
         for (int i=0; i<array1.length; i++){
         
            if(array1[i] != array2[i])
               return false;
         
         }
         
         return true;
      
      }
      
      public static int[] rangeToIndexes(ArrayRange[] range){
      
         int total = 0;
         for(int i=0; i<range.length; i++)
            total += (range[i].getLast() - range[i].getFirst() + 1);
         
         int[] res = new int[total];
         int k=0;
      
         for(int i=0; i<range.length; i++){
         
            for(int j=range[i].getFirst(); j<=range[i].getLast(); j++, k++)
               res[k] = j; 
               
         }
         
         return res;
      
      }
   	
   }