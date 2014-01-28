/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class LogByteSizeMergePolicy extends LogMergePolicy
/*    */ {
/*    */   public static final double DEFAULT_MIN_MERGE_MB = 1.6D;
/*    */   public static final double DEFAULT_MAX_MERGE_MB = 9.223372036854776E+18D;
/*    */ 
/*    */   public LogByteSizeMergePolicy(IndexWriter writer)
/*    */   {
/* 34 */     super(writer);
/* 35 */     this.minMergeSize = 1677721L;
/* 36 */     this.maxMergeSize = 9223372036854775807L;
/*    */   }
/*    */ 
/*    */   protected long size(SegmentInfo info) throws IOException {
/* 40 */     return sizeBytes(info);
/*    */   }
/*    */ 
/*    */   public void setMaxMergeMB(double mb)
/*    */   {
/* 55 */     this.maxMergeSize = (()(mb * 1024.0D * 1024.0D));
/*    */   }
/*    */ 
/*    */   public double getMaxMergeMB()
/*    */   {
/* 63 */     return this.maxMergeSize / 1024.0D / 1024.0D;
/*    */   }
/*    */ 
/*    */   public void setMinMergeMB(double mb)
/*    */   {
/* 76 */     this.minMergeSize = (()(mb * 1024.0D * 1024.0D));
/*    */   }
/*    */ 
/*    */   public double getMinMergeMB()
/*    */   {
/* 83 */     return this.minMergeSize / 1024.0D / 1024.0D;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.LogByteSizeMergePolicy
 * JD-Core Version:    0.6.2
 */