/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ class ParallelArrayTermVectorMapper extends TermVectorMapper
/*     */ {
/*     */   private String[] terms;
/*     */   private int[] termFreqs;
/*     */   private int[][] positions;
/*     */   private TermVectorOffsetInfo[][] offsets;
/*     */   private int currentPosition;
/*     */   private boolean storingOffsets;
/*     */   private boolean storingPositions;
/*     */   private String field;
/*     */ 
/*     */   public void setExpectations(String field, int numTerms, boolean storeOffsets, boolean storePositions)
/*     */   {
/* 558 */     this.field = field;
/* 559 */     this.terms = new String[numTerms];
/* 560 */     this.termFreqs = new int[numTerms];
/* 561 */     this.storingOffsets = storeOffsets;
/* 562 */     this.storingPositions = storePositions;
/* 563 */     if (storePositions)
/* 564 */       this.positions = new int[numTerms][];
/* 565 */     if (storeOffsets)
/* 566 */       this.offsets = new TermVectorOffsetInfo[numTerms][];
/*     */   }
/*     */ 
/*     */   public void map(String term, int frequency, TermVectorOffsetInfo[] offsets, int[] positions)
/*     */   {
/* 571 */     this.terms[this.currentPosition] = term;
/* 572 */     this.termFreqs[this.currentPosition] = frequency;
/* 573 */     if (this.storingOffsets)
/*     */     {
/* 575 */       this.offsets[this.currentPosition] = offsets;
/*     */     }
/* 577 */     if (this.storingPositions)
/*     */     {
/* 579 */       this.positions[this.currentPosition] = positions;
/*     */     }
/* 581 */     this.currentPosition += 1;
/*     */   }
/*     */ 
/*     */   public TermFreqVector materializeVector()
/*     */   {
/* 589 */     SegmentTermVector tv = null;
/* 590 */     if ((this.field != null) && (this.terms != null)) {
/* 591 */       if ((this.storingPositions) || (this.storingOffsets))
/* 592 */         tv = new SegmentTermPositionVector(this.field, this.terms, this.termFreqs, this.positions, this.offsets);
/*     */       else {
/* 594 */         tv = new SegmentTermVector(this.field, this.terms, this.termFreqs);
/*     */       }
/*     */     }
/* 597 */     return tv;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.ParallelArrayTermVectorMapper
 * JD-Core Version:    0.6.2
 */