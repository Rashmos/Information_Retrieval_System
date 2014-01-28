/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class LogDocMergePolicy extends LogMergePolicy
/*    */ {
/*    */   public static final int DEFAULT_MIN_MERGE_DOCS = 1000;
/*    */ 
/*    */   public LogDocMergePolicy(IndexWriter writer)
/*    */   {
/* 32 */     super(writer);
/* 33 */     this.minMergeSize = 1000L;
/*    */ 
/* 37 */     this.maxMergeSize = 9223372036854775807L;
/*    */   }
/*    */ 
/*    */   protected long size(SegmentInfo info) throws IOException {
/* 41 */     return sizeDocs(info);
/*    */   }
/*    */ 
/*    */   public void setMinMergeDocs(int minMergeDocs)
/*    */   {
/* 54 */     this.minMergeSize = minMergeDocs;
/*    */   }
/*    */ 
/*    */   public int getMinMergeDocs()
/*    */   {
/* 61 */     return (int)this.minMergeSize;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.LogDocMergePolicy
 * JD-Core Version:    0.6.2
 */