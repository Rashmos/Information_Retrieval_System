/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import org.apache.lucene.index.Payload;
/*    */ import org.apache.lucene.util.ArrayUtil;
/*    */ 
/*    */ public class IntegerEncoder extends AbstractEncoder
/*    */   implements PayloadEncoder
/*    */ {
/*    */   public Payload encode(char[] buffer, int offset, int length)
/*    */   {
/* 32 */     Payload result = new Payload();
/* 33 */     int payload = ArrayUtil.parseInt(buffer, offset, length);
/* 34 */     byte[] bytes = PayloadHelper.encodeInt(payload);
/* 35 */     result.setData(bytes);
/* 36 */     return result;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.IntegerEncoder
 * JD-Core Version:    0.6.2
 */