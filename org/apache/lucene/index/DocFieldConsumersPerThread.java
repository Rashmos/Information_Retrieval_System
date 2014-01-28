/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ final class DocFieldConsumersPerThread extends DocFieldConsumerPerThread
/*    */ {
/*    */   final DocFieldConsumerPerThread one;
/*    */   final DocFieldConsumerPerThread two;
/*    */   final DocFieldConsumers parent;
/*    */   final DocumentsWriter.DocState docState;
/*    */ 
/*    */   public DocFieldConsumersPerThread(DocFieldProcessorPerThread docFieldProcessorPerThread, DocFieldConsumers parent, DocFieldConsumerPerThread one, DocFieldConsumerPerThread two)
/*    */   {
/* 31 */     this.parent = parent;
/* 32 */     this.one = one;
/* 33 */     this.two = two;
/* 34 */     this.docState = docFieldProcessorPerThread.docState;
/*    */   }
/*    */ 
/*    */   public void startDocument() throws IOException
/*    */   {
/* 39 */     this.one.startDocument();
/* 40 */     this.two.startDocument();
/*    */   }
/*    */ 
/*    */   public void abort()
/*    */   {
/*    */     try {
/* 46 */       this.one.abort();
/*    */     } finally {
/* 48 */       this.two.abort();
/*    */     }
/*    */   }
/*    */ 
/*    */   public DocumentsWriter.DocWriter finishDocument() throws IOException
/*    */   {
/* 54 */     DocumentsWriter.DocWriter oneDoc = this.one.finishDocument();
/* 55 */     DocumentsWriter.DocWriter twoDoc = this.two.finishDocument();
/* 56 */     if (oneDoc == null)
/* 57 */       return twoDoc;
/* 58 */     if (twoDoc == null) {
/* 59 */       return oneDoc;
/*    */     }
/* 61 */     DocFieldConsumers.PerDoc both = this.parent.getPerDoc();
/* 62 */     both.docID = this.docState.docID;
/* 63 */     assert (oneDoc.docID == this.docState.docID);
/* 64 */     assert (twoDoc.docID == this.docState.docID);
/* 65 */     both.one = oneDoc;
/* 66 */     both.two = twoDoc;
/* 67 */     return both;
/*    */   }
/*    */ 
/*    */   public DocFieldConsumerPerField addField(FieldInfo fi)
/*    */   {
/* 73 */     return new DocFieldConsumersPerField(this, this.one.addField(fi), this.two.addField(fi));
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldConsumersPerThread
 * JD-Core Version:    0.6.2
 */