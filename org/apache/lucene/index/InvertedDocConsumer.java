/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ 
/*    */ abstract class InvertedDocConsumer
/*    */ {
/*    */   FieldInfos fieldInfos;
/*    */ 
/*    */   abstract InvertedDocConsumerPerThread addThread(DocInverterPerThread paramDocInverterPerThread);
/*    */ 
/*    */   abstract void abort();
/*    */ 
/*    */   abstract void flush(Map<InvertedDocConsumerPerThread, Collection<InvertedDocConsumerPerField>> paramMap, SegmentWriteState paramSegmentWriteState)
/*    */     throws IOException;
/*    */ 
/*    */   abstract void closeDocStore(SegmentWriteState paramSegmentWriteState)
/*    */     throws IOException;
/*    */ 
/*    */   abstract boolean freeRAM();
/*    */ 
/*    */   void setFieldInfos(FieldInfos fieldInfos)
/*    */   {
/* 45 */     this.fieldInfos = fieldInfos;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.InvertedDocConsumer
 * JD-Core Version:    0.6.2
 */