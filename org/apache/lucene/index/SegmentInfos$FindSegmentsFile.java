/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexInput;
/*     */ import org.apache.lucene.util.ThreadInterruptedException;
/*     */ 
/*     */ public abstract class SegmentInfos$FindSegmentsFile
/*     */ {
/*     */   final Directory directory;
/*     */ 
/*     */   public SegmentInfos$FindSegmentsFile(Directory directory)
/*     */   {
/* 517 */     this.directory = directory;
/*     */   }
/*     */ 
/*     */   public Object run() throws CorruptIndexException, IOException {
/* 521 */     return run(null);
/*     */   }
/*     */ 
/*     */   public Object run(IndexCommit commit) throws CorruptIndexException, IOException {
/* 525 */     if (commit != null) {
/* 526 */       if (this.directory != commit.getDirectory())
/* 527 */         throw new IOException("the specified commit does not match the specified Directory");
/* 528 */       return doBody(commit.getSegmentsFileName());
/*     */     }
/*     */ 
/* 531 */     String segmentFileName = null;
/* 532 */     long lastGen = -1L;
/* 533 */     long gen = 0L;
/* 534 */     int genLookaheadCount = 0;
/* 535 */     IOException exc = null;
/* 536 */     boolean retry = false;
/*     */ 
/* 538 */     int method = 0;
/*     */     while (true)
/*     */     {
/* 557 */       if (0 == method)
/*     */       {
/* 564 */         String[] files = null;
/*     */ 
/* 566 */         long genA = -1L;
/*     */ 
/* 568 */         files = this.directory.listAll();
/*     */ 
/* 570 */         if (files != null) {
/* 571 */           genA = SegmentInfos.getCurrentSegmentGeneration(files);
/*     */         }
/* 573 */         SegmentInfos.access$000("directory listing genA=" + genA);
/*     */ 
/* 580 */         long genB = -1L;
/* 581 */         for (int i = 0; i < SegmentInfos.access$100(); i++) {
/* 582 */           IndexInput genInput = null;
/*     */           try {
/* 584 */             genInput = this.directory.openInput("segments.gen");
/*     */           } catch (FileNotFoundException e) {
/* 586 */             SegmentInfos.access$000("segments.gen open: FileNotFoundException " + e);
/* 587 */             break;
/*     */           } catch (IOException e) {
/* 589 */             SegmentInfos.access$000("segments.gen open: IOException " + e);
/*     */           }
/*     */ 
/* 592 */           if (genInput != null)
/*     */             try {
/* 594 */               int version = genInput.readInt();
/*     */               Object v;
/*     */               String prevSegmentFileName;
/*     */               boolean prevExists;
/*     */               Object v;
/* 595 */               if (version == -2) {
/* 596 */                 long gen0 = genInput.readLong();
/* 597 */                 long gen1 = genInput.readLong();
/* 598 */                 SegmentInfos.access$000("fallback check: " + gen0 + "; " + gen1);
/* 599 */                 if (gen0 == gen1)
/*     */                 {
/* 601 */                   genB = gen0;
/*     */ 
/* 608 */                   genInput.close(); break; } 
/*     */               } } catch (IOException err2) {  } finally { genInput.close(); }
/*     */ 
/*     */           try
/*     */           {
/* 612 */             Thread.sleep(SegmentInfos.access$200());
/*     */           } catch (InterruptedException ie) {
/* 614 */             throw new ThreadInterruptedException(ie);
/*     */           }
/*     */         }
/*     */ 
/* 618 */         SegmentInfos.access$000("segments.gen check: genB=" + genB);
/*     */ 
/* 621 */         if (genA > genB)
/* 622 */           gen = genA;
/*     */         else {
/* 624 */           gen = genB;
/*     */         }
/* 626 */         if (gen == -1L)
/*     */         {
/* 628 */           throw new FileNotFoundException("no segments* file found in " + this.directory + ": files: " + Arrays.toString(files));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 636 */       if ((1 == method) || ((0 == method) && (lastGen == gen) && (retry)))
/*     */       {
/* 638 */         method = 1;
/*     */ 
/* 640 */         if (genLookaheadCount < SegmentInfos.access$300()) {
/* 641 */           gen += 1L;
/* 642 */           genLookaheadCount++;
/* 643 */           SegmentInfos.access$000("look ahead increment gen to " + gen);
/*     */         }
/*     */       }
/*     */ 
/* 647 */       if (lastGen == gen)
/*     */       {
/* 654 */         if (retry)
/*     */         {
/* 659 */           throw exc;
/*     */         }
/* 661 */         retry = true;
/*     */       }
/* 664 */       else if (0 == method)
/*     */       {
/* 667 */         retry = false;
/*     */       }
/*     */ 
/* 670 */       lastGen = gen;
/*     */ 
/* 672 */       segmentFileName = IndexFileNames.fileNameFromGeneration("segments", "", gen);
/*     */       try
/*     */       {
/* 677 */         v = doBody(segmentFileName);
/* 678 */         if (exc != null) {
/* 679 */           SegmentInfos.access$000("success on " + segmentFileName);
/*     */         }
/* 681 */         return v;
/*     */       }
/*     */       catch (IOException err)
/*     */       {
/* 685 */         if (exc == null) {
/* 686 */           exc = err;
/*     */         }
/*     */ 
/* 689 */         SegmentInfos.access$000("primary Exception on '" + segmentFileName + "': " + err + "'; will retry: retry=" + retry + "; gen = " + gen);
/*     */ 
/* 691 */         if ((!retry) && (gen > 1L))
/*     */         {
/* 698 */           prevSegmentFileName = IndexFileNames.fileNameFromGeneration("segments", "", gen - 1L);
/*     */ 
/* 703 */           prevExists = this.directory.fileExists(prevSegmentFileName);
/*     */ 
/* 705 */           if (prevExists) {
/* 706 */             SegmentInfos.access$000("fallback to prior segment file '" + prevSegmentFileName + "'");
/*     */             try {
/* 708 */               v = doBody(prevSegmentFileName);
/* 709 */               if (exc != null) {
/* 710 */                 SegmentInfos.access$000("success on fallback " + prevSegmentFileName);
/*     */               }
/* 712 */               return v;
/*     */             } catch (IOException err2) {
/* 714 */               SegmentInfos.access$000("secondary Exception on '" + prevSegmentFileName + "': " + err2 + "'; will retry");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract Object doBody(String paramString)
/*     */     throws CorruptIndexException, IOException;
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentInfos.FindSegmentsFile
 * JD-Core Version:    0.6.2
 */