/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ final class DocInverter extends DocFieldConsumer
/*    */ {
/*    */   final InvertedDocConsumer consumer;
/*    */   final InvertedDocEndConsumer endConsumer;
/*    */ 
/*    */   public DocInverter(InvertedDocConsumer consumer, InvertedDocEndConsumer endConsumer)
/*    */   {
/* 38 */     this.consumer = consumer;
/* 39 */     this.endConsumer = endConsumer;
/*    */   }
/*    */ 
/*    */   void setFieldInfos(FieldInfos fieldInfos)
/*    */   {
/* 44 */     super.setFieldInfos(fieldInfos);
/* 45 */     this.consumer.setFieldInfos(fieldInfos);
/* 46 */     this.endConsumer.setFieldInfos(fieldInfos);
/*    */   }
/*    */ 
/*    */   void flush(Map<DocFieldConsumerPerThread, Collection<DocFieldConsumerPerField>> threadsAndFields, SegmentWriteState state)
/*    */     throws IOException
/*    */   {
/* 52 */     Map childThreadsAndFields = new HashMap();
/* 53 */     Map endChildThreadsAndFields = new HashMap();
/*    */ 
/* 55 */     for (Map.Entry entry : threadsAndFields.entrySet())
/*    */     {
/* 58 */       DocInverterPerThread perThread = (DocInverterPerThread)entry.getKey();
/*    */ 
/* 60 */       Collection childFields = new HashSet();
/* 61 */       Collection endChildFields = new HashSet();
/* 62 */       for (DocFieldConsumerPerField field : (Collection)entry.getValue()) {
/* 63 */         DocInverterPerField perField = (DocInverterPerField)field;
/* 64 */         childFields.add(perField.consumer);
/* 65 */         endChildFields.add(perField.endConsumer);
/*    */       }
/*    */ 
/* 68 */       childThreadsAndFields.put(perThread.consumer, childFields);
/* 69 */       endChildThreadsAndFields.put(perThread.endConsumer, endChildFields);
/*    */     }
/*    */ 
/* 72 */     this.consumer.flush(childThreadsAndFields, state);
/* 73 */     this.endConsumer.flush(endChildThreadsAndFields, state);
/*    */   }
/*    */ 
/*    */   public void closeDocStore(SegmentWriteState state) throws IOException
/*    */   {
/* 78 */     this.consumer.closeDocStore(state);
/* 79 */     this.endConsumer.closeDocStore(state);
/*    */   }
/*    */ 
/*    */   void abort()
/*    */   {
/* 84 */     this.consumer.abort();
/* 85 */     this.endConsumer.abort();
/*    */   }
/*    */ 
/*    */   public boolean freeRAM()
/*    */   {
/* 90 */     return this.consumer.freeRAM();
/*    */   }
/*    */ 
/*    */   public DocFieldConsumerPerThread addThread(DocFieldProcessorPerThread docFieldProcessorPerThread)
/*    */   {
/* 95 */     return new DocInverterPerThread(docFieldProcessorPerThread, this);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocInverter
 * JD-Core Version:    0.6.2
 */