/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import org.apache.lucene.index.Payload;
/*    */ 
/*    */ public abstract class AbstractEncoder
/*    */   implements PayloadEncoder
/*    */ {
/*    */   public Payload encode(char[] buffer)
/*    */   {
/* 29 */     return encode(buffer, 0, buffer.length);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.AbstractEncoder
 * JD-Core Version:    0.6.2
 */