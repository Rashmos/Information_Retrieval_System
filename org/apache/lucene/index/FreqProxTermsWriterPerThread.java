/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ final class FreqProxTermsWriterPerThread extends TermsHashConsumerPerThread
/*    */ {
/*    */   final TermsHashPerThread termsHashPerThread;
/*    */   final DocumentsWriter.DocState docState;
/*    */ 
/*    */   public FreqProxTermsWriterPerThread(TermsHashPerThread perThread)
/*    */   {
/* 25 */     this.docState = perThread.docState;
/* 26 */     this.termsHashPerThread = perThread;
/*    */   }
/*    */ 
/*    */   public TermsHashConsumerPerField addField(TermsHashPerField termsHashPerField, FieldInfo fieldInfo)
/*    */   {
/* 31 */     return new FreqProxTermsWriterPerField(termsHashPerField, this, fieldInfo);
/*    */   }
/*    */ 
/*    */   void startDocument()
/*    */   {
/*    */   }
/*    */ 
/*    */   DocumentsWriter.DocWriter finishDocument()
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   public void abort()
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FreqProxTermsWriterPerThread
 * JD-Core Version:    0.6.2
 */