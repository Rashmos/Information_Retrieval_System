/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.document.Fieldable;
/*    */ 
/*    */ final class DocFieldConsumersPerField extends DocFieldConsumerPerField
/*    */ {
/*    */   final DocFieldConsumerPerField one;
/*    */   final DocFieldConsumerPerField two;
/*    */   final DocFieldConsumersPerThread perThread;
/*    */ 
/*    */   public DocFieldConsumersPerField(DocFieldConsumersPerThread perThread, DocFieldConsumerPerField one, DocFieldConsumerPerField two)
/*    */   {
/* 30 */     this.perThread = perThread;
/* 31 */     this.one = one;
/* 32 */     this.two = two;
/*    */   }
/*    */ 
/*    */   public void processFields(Fieldable[] fields, int count) throws IOException
/*    */   {
/* 37 */     this.one.processFields(fields, count);
/* 38 */     this.two.processFields(fields, count);
/*    */   }
/*    */ 
/*    */   public void abort()
/*    */   {
/*    */     try {
/* 44 */       this.one.abort();
/*    */     } finally {
/* 46 */       this.two.abort();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldConsumersPerField
 * JD-Core Version:    0.6.2
 */