/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class DocFieldProcessor extends DocConsumer
/*    */ {
/*    */   final DocumentsWriter docWriter;
/* 37 */   final FieldInfos fieldInfos = new FieldInfos();
/*    */   final DocFieldConsumer consumer;
/*    */   final StoredFieldsWriter fieldsWriter;
/*    */ 
/*    */   public DocFieldProcessor(DocumentsWriter docWriter, DocFieldConsumer consumer)
/*    */   {
/* 42 */     this.docWriter = docWriter;
/* 43 */     this.consumer = consumer;
/* 44 */     consumer.setFieldInfos(this.fieldInfos);
/* 45 */     this.fieldsWriter = new StoredFieldsWriter(docWriter, this.fieldInfos);
/*    */   }
/*    */ 
/*    */   public void closeDocStore(SegmentWriteState state) throws IOException
/*    */   {
/* 50 */     this.consumer.closeDocStore(state);
/* 51 */     this.fieldsWriter.closeDocStore(state);
/*    */   }
/*    */ 
/*    */   public void flush(Collection<DocConsumerPerThread> threads, SegmentWriteState state)
/*    */     throws IOException
/*    */   {
/* 57 */     Map childThreadsAndFields = new HashMap();
/* 58 */     for (DocConsumerPerThread thread : threads) {
/* 59 */       DocFieldProcessorPerThread perThread = (DocFieldProcessorPerThread)thread;
/* 60 */       childThreadsAndFields.put(perThread.consumer, perThread.fields());
/* 61 */       perThread.trimFields(state);
/*    */     }
/* 63 */     this.fieldsWriter.flush(state);
/* 64 */     this.consumer.flush(childThreadsAndFields, state);
/*    */ 
/* 70 */     String fileName = state.segmentFileName("fnm");
/* 71 */     this.fieldInfos.write(state.directory, fileName);
/* 72 */     state.flushedFiles.add(fileName);
/*    */   }
/*    */ 
/*    */   public void abort()
/*    */   {
/* 77 */     this.fieldsWriter.abort();
/* 78 */     this.consumer.abort();
/*    */   }
/*    */ 
/*    */   public boolean freeRAM()
/*    */   {
/* 83 */     return this.consumer.freeRAM();
/*    */   }
/*    */ 
/*    */   public DocConsumerPerThread addThread(DocumentsWriterThreadState threadState) throws IOException
/*    */   {
/* 88 */     return new DocFieldProcessorPerThread(threadState, this);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldProcessor
 * JD-Core Version:    0.6.2
 */