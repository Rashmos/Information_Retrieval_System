/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class LogMergePolicy extends MergePolicy
/*     */ {
/*     */   public static final double LEVEL_LOG_SPAN = 0.75D;
/*     */   public static final int DEFAULT_MERGE_FACTOR = 10;
/*     */   public static final int DEFAULT_MAX_MERGE_DOCS = 2147483647;
/*  57 */   private int mergeFactor = 10;
/*     */   long minMergeSize;
/*     */   long maxMergeSize;
/*  61 */   int maxMergeDocs = 2147483647;
/*     */ 
/*  64 */   protected boolean calibrateSizeByDeletes = false;
/*     */ 
/*  66 */   private boolean useCompoundFile = true;
/*  67 */   private boolean useCompoundDocStore = true;
/*     */ 
/*     */   public LogMergePolicy(IndexWriter writer) {
/*  70 */     super(writer);
/*     */   }
/*     */ 
/*     */   protected boolean verbose() {
/*  74 */     return (this.writer != null) && (this.writer.verbose());
/*     */   }
/*     */ 
/*     */   private void message(String message) {
/*  78 */     if (verbose())
/*  79 */       this.writer.message("LMP: " + message);
/*     */   }
/*     */ 
/*     */   public int getMergeFactor()
/*     */   {
/*  86 */     return this.mergeFactor;
/*     */   }
/*     */ 
/*     */   public void setMergeFactor(int mergeFactor)
/*     */   {
/*  99 */     if (mergeFactor < 2)
/* 100 */       throw new IllegalArgumentException("mergeFactor cannot be less than 2");
/* 101 */     this.mergeFactor = mergeFactor;
/*     */   }
/*     */ 
/*     */   public boolean useCompoundFile(SegmentInfos infos, SegmentInfo info)
/*     */   {
/* 107 */     return this.useCompoundFile;
/*     */   }
/*     */ 
/*     */   public void setUseCompoundFile(boolean useCompoundFile)
/*     */   {
/* 113 */     this.useCompoundFile = useCompoundFile;
/*     */   }
/*     */ 
/*     */   public boolean getUseCompoundFile()
/*     */   {
/* 120 */     return this.useCompoundFile;
/*     */   }
/*     */ 
/*     */   public boolean useCompoundDocStore(SegmentInfos infos)
/*     */   {
/* 126 */     return this.useCompoundDocStore;
/*     */   }
/*     */ 
/*     */   public void setUseCompoundDocStore(boolean useCompoundDocStore)
/*     */   {
/* 133 */     this.useCompoundDocStore = useCompoundDocStore;
/*     */   }
/*     */ 
/*     */   public boolean getUseCompoundDocStore()
/*     */   {
/* 141 */     return this.useCompoundDocStore;
/*     */   }
/*     */ 
/*     */   public void setCalibrateSizeByDeletes(boolean calibrateSizeByDeletes)
/*     */   {
/* 147 */     this.calibrateSizeByDeletes = calibrateSizeByDeletes;
/*     */   }
/*     */ 
/*     */   public boolean getCalibrateSizeByDeletes()
/*     */   {
/* 153 */     return this.calibrateSizeByDeletes;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */   }
/*     */ 
/*     */   protected abstract long size(SegmentInfo paramSegmentInfo) throws IOException;
/*     */ 
/*     */   protected long sizeDocs(SegmentInfo info) throws IOException {
/* 162 */     if (this.calibrateSizeByDeletes) {
/* 163 */       int delCount = this.writer.numDeletedDocs(info);
/* 164 */       return info.docCount - delCount;
/*     */     }
/* 166 */     return info.docCount;
/*     */   }
/*     */ 
/*     */   protected long sizeBytes(SegmentInfo info) throws IOException
/*     */   {
/* 171 */     long byteSize = info.sizeInBytes();
/* 172 */     if (this.calibrateSizeByDeletes) {
/* 173 */       int delCount = this.writer.numDeletedDocs(info);
/* 174 */       float delRatio = info.docCount <= 0 ? 0.0F : delCount / info.docCount;
/* 175 */       return info.docCount <= 0 ? byteSize : ()((float)byteSize * (1.0F - delRatio));
/*     */     }
/* 177 */     return byteSize;
/*     */   }
/*     */ 
/*     */   private boolean isOptimized(SegmentInfos infos, int maxNumSegments, Set<SegmentInfo> segmentsToOptimize) throws IOException
/*     */   {
/* 182 */     int numSegments = infos.size();
/* 183 */     int numToOptimize = 0;
/* 184 */     SegmentInfo optimizeInfo = null;
/* 185 */     for (int i = 0; (i < numSegments) && (numToOptimize <= maxNumSegments); i++) {
/* 186 */       SegmentInfo info = infos.info(i);
/* 187 */       if (segmentsToOptimize.contains(info)) {
/* 188 */         numToOptimize++;
/* 189 */         optimizeInfo = info;
/*     */       }
/*     */     }
/*     */ 
/* 193 */     return (numToOptimize <= maxNumSegments) && ((numToOptimize != 1) || (isOptimized(optimizeInfo)));
/*     */   }
/*     */ 
/*     */   private boolean isOptimized(SegmentInfo info)
/*     */     throws IOException
/*     */   {
/* 202 */     boolean hasDeletions = this.writer.numDeletedDocs(info) > 0;
/* 203 */     return (!hasDeletions) && (!info.hasSeparateNorms()) && (info.dir == this.writer.getDirectory()) && (info.getUseCompoundFile() == this.useCompoundFile);
/*     */   }
/*     */ 
/*     */   public MergePolicy.MergeSpecification findMergesForOptimize(SegmentInfos infos, int maxNumSegments, Set<SegmentInfo> segmentsToOptimize)
/*     */     throws IOException
/*     */   {
/* 222 */     assert (maxNumSegments > 0);
/*     */     MergePolicy.MergeSpecification spec;
/*     */     MergePolicy.MergeSpecification spec;
/* 224 */     if (!isOptimized(infos, maxNumSegments, segmentsToOptimize))
/*     */     {
/* 229 */       int last = infos.size();
/* 230 */       while (last > 0) {
/* 231 */         SegmentInfo info = infos.info(--last);
/* 232 */         if (segmentsToOptimize.contains(info)) {
/* 233 */           last++;
/* 234 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 238 */       if (last > 0)
/*     */       {
/* 240 */         MergePolicy.MergeSpecification spec = new MergePolicy.MergeSpecification();
/*     */ 
/* 244 */         while (last - maxNumSegments + 1 >= this.mergeFactor) {
/* 245 */           spec.add(new MergePolicy.OneMerge(infos.range(last - this.mergeFactor, last), this.useCompoundFile));
/* 246 */           last -= this.mergeFactor;
/*     */         }
/*     */ 
/* 251 */         if (0 == spec.merges.size())
/* 252 */           if (maxNumSegments == 1)
/*     */           {
/* 256 */             if ((last > 1) || (!isOptimized(infos.info(0))))
/* 257 */               spec.add(new MergePolicy.OneMerge(infos.range(0, last), this.useCompoundFile));
/* 258 */           } else if (last > maxNumSegments)
/*     */           {
/* 269 */             int finalMergeSize = last - maxNumSegments + 1;
/*     */ 
/* 272 */             long bestSize = 0L;
/* 273 */             int bestStart = 0;
/*     */ 
/* 275 */             for (int i = 0; i < last - finalMergeSize + 1; i++) {
/* 276 */               long sumSize = 0L;
/* 277 */               for (int j = 0; j < finalMergeSize; j++)
/* 278 */                 sumSize += size(infos.info(j + i));
/* 279 */               if ((i == 0) || ((sumSize < 2L * size(infos.info(i - 1))) && (sumSize < bestSize))) {
/* 280 */                 bestStart = i;
/* 281 */                 bestSize = sumSize;
/*     */               }
/*     */             }
/*     */ 
/* 285 */             spec.add(new MergePolicy.OneMerge(infos.range(bestStart, bestStart + finalMergeSize), this.useCompoundFile));
/*     */           }
/*     */       }
/*     */       else
/*     */       {
/* 290 */         spec = null;
/*     */       }
/*     */     } else { spec = null; }
/*     */ 
/* 294 */     return spec;
/*     */   }
/*     */ 
/*     */   public MergePolicy.MergeSpecification findMergesToExpungeDeletes(SegmentInfos segmentInfos)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 305 */     int numSegments = segmentInfos.size();
/*     */ 
/* 307 */     if (verbose()) {
/* 308 */       message("findMergesToExpungeDeletes: " + numSegments + " segments");
/*     */     }
/* 310 */     MergePolicy.MergeSpecification spec = new MergePolicy.MergeSpecification();
/* 311 */     int firstSegmentWithDeletions = -1;
/* 312 */     for (int i = 0; i < numSegments; i++) {
/* 313 */       SegmentInfo info = segmentInfos.info(i);
/* 314 */       int delCount = this.writer.numDeletedDocs(info);
/* 315 */       if (delCount > 0) {
/* 316 */         if (verbose())
/* 317 */           message("  segment " + info.name + " has deletions");
/* 318 */         if (firstSegmentWithDeletions == -1) {
/* 319 */           firstSegmentWithDeletions = i;
/* 320 */         } else if (i - firstSegmentWithDeletions == this.mergeFactor)
/*     */         {
/* 323 */           if (verbose())
/* 324 */             message("  add merge " + firstSegmentWithDeletions + " to " + (i - 1) + " inclusive");
/* 325 */           spec.add(new MergePolicy.OneMerge(segmentInfos.range(firstSegmentWithDeletions, i), this.useCompoundFile));
/* 326 */           firstSegmentWithDeletions = i;
/*     */         }
/* 328 */       } else if (firstSegmentWithDeletions != -1)
/*     */       {
/* 332 */         if (verbose())
/* 333 */           message("  add merge " + firstSegmentWithDeletions + " to " + (i - 1) + " inclusive");
/* 334 */         spec.add(new MergePolicy.OneMerge(segmentInfos.range(firstSegmentWithDeletions, i), this.useCompoundFile));
/* 335 */         firstSegmentWithDeletions = -1;
/*     */       }
/*     */     }
/*     */ 
/* 339 */     if (firstSegmentWithDeletions != -1) {
/* 340 */       if (verbose())
/* 341 */         message("  add merge " + firstSegmentWithDeletions + " to " + (numSegments - 1) + " inclusive");
/* 342 */       spec.add(new MergePolicy.OneMerge(segmentInfos.range(firstSegmentWithDeletions, numSegments), this.useCompoundFile));
/*     */     }
/*     */ 
/* 345 */     return spec;
/*     */   }
/*     */ 
/*     */   public MergePolicy.MergeSpecification findMerges(SegmentInfos infos)
/*     */     throws IOException
/*     */   {
/* 358 */     int numSegments = infos.size();
/* 359 */     if (verbose()) {
/* 360 */       message("findMerges: " + numSegments + " segments");
/*     */     }
/*     */ 
/* 364 */     float[] levels = new float[numSegments];
/* 365 */     float norm = (float)Math.log(this.mergeFactor);
/*     */ 
/* 367 */     for (int i = 0; i < numSegments; i++) {
/* 368 */       SegmentInfo info = infos.info(i);
/* 369 */       long size = size(info);
/*     */ 
/* 372 */       if (size < 1L)
/* 373 */         size = 1L;
/* 374 */       levels[i] = ((float)Math.log(size) / norm);
/*     */     }
/*     */     float levelFloor;
/*     */     float levelFloor;
/* 378 */     if (this.minMergeSize <= 0L)
/* 379 */       levelFloor = 0.0F;
/*     */     else {
/* 381 */       levelFloor = (float)(Math.log(this.minMergeSize) / norm);
/*     */     }
/*     */ 
/* 390 */     MergePolicy.MergeSpecification spec = null;
/*     */ 
/* 392 */     int start = 0;
/* 393 */     while (start < numSegments)
/*     */     {
/* 397 */       float maxLevel = levels[start];
/* 398 */       for (int i = 1 + start; i < numSegments; i++) {
/* 399 */         float level = levels[i];
/* 400 */         if (level > maxLevel)
/* 401 */           maxLevel = level;
/*     */       }
/*     */       float levelBottom;
/*     */       float levelBottom;
/* 407 */       if (maxLevel < levelFloor)
/*     */       {
/* 409 */         levelBottom = -1.0F;
/*     */       } else {
/* 411 */         levelBottom = (float)(maxLevel - 0.75D);
/*     */ 
/* 414 */         if ((levelBottom < levelFloor) && (maxLevel >= levelFloor)) {
/* 415 */           levelBottom = levelFloor;
/*     */         }
/*     */       }
/* 418 */       int upto = numSegments - 1;
/* 419 */       while ((upto >= start) && 
/* 420 */         (levels[upto] < levelBottom))
/*     */       {
/* 423 */         upto--;
/*     */       }
/* 425 */       if (verbose()) {
/* 426 */         message("  level " + levelBottom + " to " + maxLevel + ": " + (1 + upto - start) + " segments");
/*     */       }
/*     */ 
/* 429 */       int end = start + this.mergeFactor;
/* 430 */       while (end <= 1 + upto) {
/* 431 */         boolean anyTooLarge = false;
/* 432 */         for (int i = start; i < end; i++) {
/* 433 */           SegmentInfo info = infos.info(i);
/* 434 */           anyTooLarge |= ((size(info) >= this.maxMergeSize) || (sizeDocs(info) >= this.maxMergeDocs));
/*     */         }
/*     */ 
/* 437 */         if (!anyTooLarge) {
/* 438 */           if (spec == null)
/* 439 */             spec = new MergePolicy.MergeSpecification();
/* 440 */           if (verbose())
/* 441 */             message("    " + start + " to " + end + ": add this merge");
/* 442 */           spec.add(new MergePolicy.OneMerge(infos.range(start, end), this.useCompoundFile));
/* 443 */         } else if (verbose()) {
/* 444 */           message("    " + start + " to " + end + ": contains segment over maxMergeSize or maxMergeDocs; skipping");
/*     */         }
/* 446 */         start = end;
/* 447 */         end = start + this.mergeFactor;
/*     */       }
/*     */ 
/* 450 */       start = 1 + upto;
/*     */     }
/*     */ 
/* 453 */     return spec;
/*     */   }
/*     */ 
/*     */   public void setMaxMergeDocs(int maxMergeDocs)
/*     */   {
/* 472 */     this.maxMergeDocs = maxMergeDocs;
/*     */   }
/*     */ 
/*     */   public int getMaxMergeDocs()
/*     */   {
/* 479 */     return this.maxMergeDocs;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.LogMergePolicy
 * JD-Core Version:    0.6.2
 */