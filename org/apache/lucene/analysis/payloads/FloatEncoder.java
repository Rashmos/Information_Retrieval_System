/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import org.apache.lucene.index.Payload;
/*    */ 
/*    */ public class FloatEncoder extends AbstractEncoder
/*    */   implements PayloadEncoder
/*    */ {
/*    */   public Payload encode(char[] buffer, int offset, int length)
/*    */   {
/* 31 */     Payload result = new Payload();
/* 32 */     float payload = Float.parseFloat(new String(buffer, offset, length));
/* 33 */     byte[] bytes = PayloadHelper.encodeFloat(payload);
/* 34 */     result.setData(bytes);
/* 35 */     return result;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.FloatEncoder
 * JD-Core Version:    0.6.2
 */