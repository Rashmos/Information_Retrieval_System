/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ class SegmentReader$Ref
/*     */ {
/* 317 */   private int refCount = 1;
/*     */ 
/*     */   public String toString()
/*     */   {
/* 321 */     return "refcount: " + this.refCount;
/*     */   }
/*     */ 
/*     */   public synchronized int refCount() {
/* 325 */     return this.refCount;
/*     */   }
/*     */ 
/*     */   public synchronized int incRef() {
/* 329 */     assert (this.refCount > 0);
/* 330 */     this.refCount += 1;
/* 331 */     return this.refCount;
/*     */   }
/*     */ 
/*     */   public synchronized int decRef() {
/* 335 */     assert (this.refCount > 0);
/* 336 */     this.refCount -= 1;
/* 337 */     return this.refCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentReader.Ref
 * JD-Core Version:    0.6.2
 */