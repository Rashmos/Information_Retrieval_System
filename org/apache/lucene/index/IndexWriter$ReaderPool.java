/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ class IndexWriter$ReaderPool
/*     */ {
/* 423 */   private final Map<SegmentInfo, SegmentReader> readerMap = new HashMap();
/*     */ 
/*     */   IndexWriter$ReaderPool(IndexWriter paramIndexWriter) {
/*     */   }
/*     */   synchronized void clear(SegmentInfos infos) throws IOException {
/* 428 */     if (infos == null) {
/* 429 */       for (Map.Entry ent : this.readerMap.entrySet())
/* 430 */         ((SegmentReader)ent.getValue()).hasChanges = false;
/*     */     }
/*     */     else
/* 433 */       for (SegmentInfo info : infos)
/* 434 */         if (this.readerMap.containsKey(info))
/* 435 */           ((SegmentReader)this.readerMap.get(info)).hasChanges = false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean infoIsLive(SegmentInfo info)
/*     */   {
/* 443 */     int idx = IndexWriter.access$000(this.this$0).indexOf(info);
/* 444 */     assert (idx != -1);
/* 445 */     assert (IndexWriter.access$000(this.this$0).get(idx) == info);
/* 446 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized SegmentInfo mapToLive(SegmentInfo info) {
/* 450 */     int idx = IndexWriter.access$000(this.this$0).indexOf(info);
/* 451 */     if (idx != -1) {
/* 452 */       info = (SegmentInfo)IndexWriter.access$000(this.this$0).get(idx);
/*     */     }
/* 454 */     return info;
/*     */   }
/*     */ 
/*     */   public synchronized void release(SegmentReader sr)
/*     */     throws IOException
/*     */   {
/* 464 */     release(sr, false);
/*     */   }
/*     */ 
/*     */   public synchronized void release(SegmentReader sr, boolean drop)
/*     */     throws IOException
/*     */   {
/* 475 */     boolean pooled = this.readerMap.containsKey(sr.getSegmentInfo());
/*     */ 
/* 477 */     if (!$assertionsDisabled) if (((!pooled ? 1 : 0) | (this.readerMap.get(sr.getSegmentInfo()) == sr ? 1 : 0)) == 0) throw new AssertionError();
/*     */ 
/*     */ 
/* 481 */     sr.decRef();
/*     */ 
/* 483 */     if ((pooled) && ((drop) || ((!IndexWriter.access$100(this.this$0)) && (sr.getRefCount() == 1))))
/*     */     {
/* 487 */       this.readerMap.remove(sr.getSegmentInfo());
/*     */ 
/* 489 */       assert ((!sr.hasChanges) || (Thread.holdsLock(this.this$0)));
/*     */ 
/* 493 */       boolean success = false;
/*     */       try {
/* 495 */         sr.close();
/* 496 */         success = true;
/*     */       } finally {
/* 498 */         if ((!success) && (sr.hasChanges))
/*     */         {
/* 500 */           sr.hasChanges = false;
/*     */           try {
/* 502 */             sr.close();
/*     */           }
/*     */           catch (Throwable ignore)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void close() throws IOException
/*     */   {
/* 514 */     Iterator iter = this.readerMap.entrySet().iterator();
/* 515 */     while (iter.hasNext())
/*     */     {
/* 517 */       Map.Entry ent = (Map.Entry)iter.next();
/*     */ 
/* 519 */       SegmentReader sr = (SegmentReader)ent.getValue();
/* 520 */       if (sr.hasChanges) {
/* 521 */         assert (infoIsLive(sr.getSegmentInfo()));
/* 522 */         sr.startCommit();
/* 523 */         boolean success = false;
/*     */         try {
/* 525 */           sr.doCommit(null);
/* 526 */           success = true;
/*     */         } finally {
/* 528 */           if (!success) {
/* 529 */             sr.rollbackCommit();
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 534 */       iter.remove();
/*     */ 
/* 540 */       sr.decRef();
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void commit()
/*     */     throws IOException
/*     */   {
/* 549 */     for (Map.Entry ent : this.readerMap.entrySet())
/*     */     {
/* 551 */       SegmentReader sr = (SegmentReader)ent.getValue();
/* 552 */       if (sr.hasChanges) {
/* 553 */         assert (infoIsLive(sr.getSegmentInfo()));
/* 554 */         sr.startCommit();
/* 555 */         boolean success = false;
/*     */         try {
/* 557 */           sr.doCommit(null);
/* 558 */           success = true;
/*     */         } finally {
/* 560 */           if (!success)
/* 561 */             sr.rollbackCommit();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized SegmentReader getReadOnlyClone(SegmentInfo info, boolean doOpenStores, int termInfosIndexDivisor)
/*     */     throws IOException
/*     */   {
/* 574 */     SegmentReader sr = get(info, doOpenStores, 1024, termInfosIndexDivisor);
/*     */     try {
/* 576 */       return (SegmentReader)sr.clone(true);
/*     */     } finally {
/* 578 */       sr.decRef();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized SegmentReader get(SegmentInfo info, boolean doOpenStores)
/*     */     throws IOException
/*     */   {
/* 591 */     return get(info, doOpenStores, 1024, IndexWriter.access$200(this.this$0));
/*     */   }
/*     */ 
/*     */   public synchronized SegmentReader get(SegmentInfo info, boolean doOpenStores, int readBufferSize, int termsIndexDivisor)
/*     */     throws IOException
/*     */   {
/* 607 */     if (IndexWriter.access$100(this.this$0)) {
/* 608 */       readBufferSize = 1024;
/*     */     }
/*     */ 
/* 611 */     SegmentReader sr = (SegmentReader)this.readerMap.get(info);
/* 612 */     if (sr == null)
/*     */     {
/* 616 */       sr = SegmentReader.get(false, info.dir, info, readBufferSize, doOpenStores, termsIndexDivisor);
/*     */ 
/* 618 */       if (info.dir == IndexWriter.access$300(this.this$0))
/*     */       {
/* 620 */         this.readerMap.put(info, sr);
/*     */       }
/*     */     } else {
/* 623 */       if (doOpenStores) {
/* 624 */         sr.openDocStores();
/*     */       }
/* 626 */       if ((termsIndexDivisor != -1) && (!sr.termsIndexLoaded()))
/*     */       {
/* 633 */         sr.loadTermsIndex(termsIndexDivisor);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 638 */     if (info.dir == IndexWriter.access$300(this.this$0))
/*     */     {
/* 640 */       sr.incRef();
/*     */     }
/* 642 */     return sr;
/*     */   }
/*     */ 
/*     */   public synchronized SegmentReader getIfExists(SegmentInfo info) throws IOException
/*     */   {
/* 647 */     SegmentReader sr = (SegmentReader)this.readerMap.get(info);
/* 648 */     if (sr != null) {
/* 649 */       sr.incRef();
/*     */     }
/* 651 */     return sr;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexWriter.ReaderPool
 * JD-Core Version:    0.6.2
 */