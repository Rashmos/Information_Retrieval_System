/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import org.apache.lucene.store.Directory;
/*     */ 
/*     */ class SegmentMerger$CheckAbort
/*     */ {
/*     */   private double workCount;
/*     */   private MergePolicy.OneMerge merge;
/*     */   private Directory dir;
/*     */ 
/*     */   public SegmentMerger$CheckAbort(MergePolicy.OneMerge merge, Directory dir)
/*     */   {
/* 753 */     this.merge = merge;
/* 754 */     this.dir = dir;
/*     */   }
/*     */ 
/*     */   public void work(double units)
/*     */     throws MergePolicy.MergeAbortedException
/*     */   {
/* 766 */     this.workCount += units;
/* 767 */     if (this.workCount >= 10000.0D) {
/* 768 */       this.merge.checkAborted(this.dir);
/* 769 */       this.workCount = 0.0D;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentMerger.CheckAbort
 * JD-Core Version:    0.6.2
 */