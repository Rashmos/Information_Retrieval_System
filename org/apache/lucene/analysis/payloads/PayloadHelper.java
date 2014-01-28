/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ public class PayloadHelper
/*    */ {
/*    */   public static byte[] encodeFloat(float payload)
/*    */   {
/* 27 */     return encodeFloat(payload, new byte[4], 0);
/*    */   }
/*    */ 
/*    */   public static byte[] encodeFloat(float payload, byte[] data, int offset) {
/* 31 */     return encodeInt(Float.floatToIntBits(payload), data, offset);
/*    */   }
/*    */ 
/*    */   public static byte[] encodeInt(int payload) {
/* 35 */     return encodeInt(payload, new byte[4], 0);
/*    */   }
/*    */ 
/*    */   public static byte[] encodeInt(int payload, byte[] data, int offset) {
/* 39 */     data[offset] = ((byte)(payload >> 24));
/* 40 */     data[(offset + 1)] = ((byte)(payload >> 16));
/* 41 */     data[(offset + 2)] = ((byte)(payload >> 8));
/* 42 */     data[(offset + 3)] = ((byte)payload);
/* 43 */     return data;
/*    */   }
/*    */ 
/*    */   public static float decodeFloat(byte[] bytes)
/*    */   {
/* 53 */     return decodeFloat(bytes, 0);
/*    */   }
/*    */ 
/*    */   public static final float decodeFloat(byte[] bytes, int offset)
/*    */   {
/* 67 */     return Float.intBitsToFloat(decodeInt(bytes, offset));
/*    */   }
/*    */ 
/*    */   public static final int decodeInt(byte[] bytes, int offset) {
/* 71 */     return (bytes[offset] & 0xFF) << 24 | (bytes[(offset + 1)] & 0xFF) << 16 | (bytes[(offset + 2)] & 0xFF) << 8 | bytes[(offset + 3)] & 0xFF;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.PayloadHelper
 * JD-Core Version:    0.6.2
 */