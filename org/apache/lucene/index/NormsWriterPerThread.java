/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ final class NormsWriterPerThread extends InvertedDocEndConsumerPerThread
/*    */ {
/*    */   final NormsWriter normsWriter;
/*    */   final DocumentsWriter.DocState docState;
/*    */ 
/*    */   public NormsWriterPerThread(DocInverterPerThread docInverterPerThread, NormsWriter normsWriter)
/*    */   {
/* 25 */     this.normsWriter = normsWriter;
/* 26 */     this.docState = docInverterPerThread.docState;
/*    */   }
/*    */ 
/*    */   InvertedDocEndConsumerPerField addField(DocInverterPerField docInverterPerField, FieldInfo fieldInfo)
/*    */   {
/* 31 */     return new NormsWriterPerField(docInverterPerField, this, fieldInfo);
/*    */   }
/*    */   void abort() {
/*    */   }
/*    */ 
/*    */   void startDocument() {
/*    */   }
/*    */ 
/*    */   void finishDocument() {
/*    */   }
/*    */ 
/*    */   boolean freeRAM() {
/* 43 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.NormsWriterPerThread
 * JD-Core Version:    0.6.2
 */