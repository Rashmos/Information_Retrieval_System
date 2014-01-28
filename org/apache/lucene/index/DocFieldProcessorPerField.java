/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import org.apache.lucene.document.Fieldable;
/*    */ 
/*    */ final class DocFieldProcessorPerField
/*    */ {
/*    */   final DocFieldConsumerPerField consumer;
/*    */   final FieldInfo fieldInfo;
/*    */   DocFieldProcessorPerField next;
/* 32 */   int lastGen = -1;
/*    */   int fieldCount;
/* 35 */   Fieldable[] fields = new Fieldable[1];
/*    */ 
/*    */   public DocFieldProcessorPerField(DocFieldProcessorPerThread perThread, FieldInfo fieldInfo) {
/* 38 */     this.consumer = perThread.consumer.addField(fieldInfo);
/* 39 */     this.fieldInfo = fieldInfo;
/*    */   }
/*    */ 
/*    */   public void abort() {
/* 43 */     this.consumer.abort();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldProcessorPerField
 * JD-Core Version:    0.6.2
 */