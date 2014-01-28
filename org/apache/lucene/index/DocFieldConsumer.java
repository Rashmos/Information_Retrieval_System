/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ 
/*    */ abstract class DocFieldConsumer
/*    */ {
/*    */   FieldInfos fieldInfos;
/*    */ 
/*    */   abstract void flush(Map<DocFieldConsumerPerThread, Collection<DocFieldConsumerPerField>> paramMap, SegmentWriteState paramSegmentWriteState)
/*    */     throws IOException;
/*    */ 
/*    */   abstract void closeDocStore(SegmentWriteState paramSegmentWriteState)
/*    */     throws IOException;
/*    */ 
/*    */   abstract void abort();
/*    */ 
/*    */   abstract DocFieldConsumerPerThread addThread(DocFieldProcessorPerThread paramDocFieldProcessorPerThread)
/*    */     throws IOException;
/*    */ 
/*    */   abstract boolean freeRAM();
/*    */ 
/*    */   void setFieldInfos(FieldInfos fieldInfos)
/*    */   {
/* 48 */     this.fieldInfos = fieldInfos;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldConsumer
 * JD-Core Version:    0.6.2
 */