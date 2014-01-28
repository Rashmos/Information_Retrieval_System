/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ final class DocumentsWriterThreadState
/*    */ {
/* 28 */   boolean isIdle = true;
/* 29 */   int numThreads = 1;
/*    */   boolean doFlushAfter;
/*    */   final DocConsumerPerThread consumer;
/*    */   final DocumentsWriter.DocState docState;
/*    */   final DocumentsWriter docWriter;
/*    */ 
/*    */   public DocumentsWriterThreadState(DocumentsWriter docWriter)
/*    */     throws IOException
/*    */   {
/* 37 */     this.docWriter = docWriter;
/* 38 */     this.docState = new DocumentsWriter.DocState();
/* 39 */     this.docState.maxFieldLength = docWriter.maxFieldLength;
/* 40 */     this.docState.infoStream = docWriter.infoStream;
/* 41 */     this.docState.similarity = docWriter.similarity;
/* 42 */     this.docState.docWriter = docWriter;
/* 43 */     this.consumer = docWriter.consumer.addThread(this);
/*    */   }
/*    */ 
/*    */   void doAfterFlush() {
/* 47 */     this.numThreads = 0;
/* 48 */     this.doFlushAfter = false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocumentsWriterThreadState
 * JD-Core Version:    0.6.2
 */