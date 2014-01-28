/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ 
/*    */ abstract class TermsHashConsumer
/*    */ {
/*    */   FieldInfos fieldInfos;
/*    */ 
/*    */   abstract int bytesPerPosting();
/*    */ 
/*    */   abstract void createPostings(RawPostingList[] paramArrayOfRawPostingList, int paramInt1, int paramInt2);
/*    */ 
/*    */   abstract TermsHashConsumerPerThread addThread(TermsHashPerThread paramTermsHashPerThread);
/*    */ 
/*    */   abstract void flush(Map<TermsHashConsumerPerThread, Collection<TermsHashConsumerPerField>> paramMap, SegmentWriteState paramSegmentWriteState)
/*    */     throws IOException;
/*    */ 
/*    */   abstract void abort();
/*    */ 
/*    */   abstract void closeDocStore(SegmentWriteState paramSegmentWriteState)
/*    */     throws IOException;
/*    */ 
/*    */   void setFieldInfos(FieldInfos fieldInfos)
/*    */   {
/* 35 */     this.fieldInfos = fieldInfos;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermsHashConsumer
 * JD-Core Version:    0.6.2
 */