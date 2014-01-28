/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.store.Directory;
/*     */ 
/*     */ public abstract class MergePolicy
/*     */   implements Closeable
/*     */ {
/*     */   protected final IndexWriter writer;
/*     */ 
/*     */   public MergePolicy(IndexWriter writer)
/*     */   {
/* 205 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */   public abstract MergeSpecification findMerges(SegmentInfos paramSegmentInfos)
/*     */     throws CorruptIndexException, IOException;
/*     */ 
/*     */   public abstract MergeSpecification findMergesForOptimize(SegmentInfos paramSegmentInfos, int paramInt, Set<SegmentInfo> paramSet)
/*     */     throws CorruptIndexException, IOException;
/*     */ 
/*     */   public abstract MergeSpecification findMergesToExpungeDeletes(SegmentInfos paramSegmentInfos)
/*     */     throws CorruptIndexException, IOException;
/*     */ 
/*     */   public abstract void close();
/*     */ 
/*     */   public abstract boolean useCompoundFile(SegmentInfos paramSegmentInfos, SegmentInfo paramSegmentInfo);
/*     */ 
/*     */   public abstract boolean useCompoundDocStore(SegmentInfos paramSegmentInfos);
/*     */ 
/*     */   public static class MergeAbortedException extends IOException
/*     */   {
/*     */     public MergeAbortedException()
/*     */     {
/* 195 */       super();
/*     */     }
/*     */     public MergeAbortedException(String message) {
/* 198 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MergeException extends RuntimeException
/*     */   {
/*     */     private Directory dir;
/*     */ 
/*     */     public MergeException(String message, Directory dir)
/*     */     {
/* 178 */       super();
/* 179 */       this.dir = dir;
/*     */     }
/*     */ 
/*     */     public MergeException(Throwable exc, Directory dir) {
/* 183 */       super();
/* 184 */       this.dir = dir;
/*     */     }
/*     */ 
/*     */     public Directory getDirectory()
/*     */     {
/* 189 */       return this.dir;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MergeSpecification
/*     */   {
/* 156 */     public List<MergePolicy.OneMerge> merges = new ArrayList();
/*     */ 
/*     */     public void add(MergePolicy.OneMerge merge) {
/* 159 */       this.merges.add(merge);
/*     */     }
/*     */ 
/*     */     public String segString(Directory dir) {
/* 163 */       StringBuilder b = new StringBuilder();
/* 164 */       b.append("MergeSpec:\n");
/* 165 */       int count = this.merges.size();
/* 166 */       for (int i = 0; i < count; i++)
/* 167 */         b.append("  ").append(1 + i).append(": ").append(((MergePolicy.OneMerge)this.merges.get(i)).segString(dir));
/* 168 */       return b.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class OneMerge
/*     */   {
/*     */     SegmentInfo info;
/*     */     boolean mergeDocStores;
/*     */     boolean optimize;
/*     */     boolean increfDone;
/*     */     boolean registerDone;
/*     */     long mergeGen;
/*     */     boolean isExternal;
/*     */     int maxNumSegmentsOptimize;
/*     */     SegmentReader[] readers;
/*     */     SegmentReader[] readersClone;
/*     */     List<String> mergeFiles;
/*     */     final SegmentInfos segments;
/*     */     final boolean useCompoundFile;
/*     */     boolean aborted;
/*     */     Throwable error;
/*     */ 
/*     */     public OneMerge(SegmentInfos segments, boolean useCompoundFile)
/*     */     {
/*  91 */       if (0 == segments.size())
/*  92 */         throw new RuntimeException("segments must include at least one segment");
/*  93 */       this.segments = segments;
/*  94 */       this.useCompoundFile = useCompoundFile;
/*     */     }
/*     */ 
/*     */     synchronized void setException(Throwable error)
/*     */     {
/* 100 */       this.error = error;
/*     */     }
/*     */ 
/*     */     synchronized Throwable getException()
/*     */     {
/* 106 */       return this.error;
/*     */     }
/*     */ 
/*     */     synchronized void abort()
/*     */     {
/* 113 */       this.aborted = true;
/*     */     }
/*     */ 
/*     */     synchronized boolean isAborted()
/*     */     {
/* 118 */       return this.aborted;
/*     */     }
/*     */ 
/*     */     synchronized void checkAborted(Directory dir) throws MergePolicy.MergeAbortedException {
/* 122 */       if (this.aborted)
/* 123 */         throw new MergePolicy.MergeAbortedException("merge is aborted: " + segString(dir));
/*     */     }
/*     */ 
/*     */     String segString(Directory dir) {
/* 127 */       StringBuilder b = new StringBuilder();
/* 128 */       int numSegments = this.segments.size();
/* 129 */       for (int i = 0; i < numSegments; i++) {
/* 130 */         if (i > 0) b.append(' ');
/* 131 */         b.append(this.segments.info(i).segString(dir));
/*     */       }
/* 133 */       if (this.info != null)
/* 134 */         b.append(" into ").append(this.info.name);
/* 135 */       if (this.optimize)
/* 136 */         b.append(" [optimize]");
/* 137 */       if (this.mergeDocStores) {
/* 138 */         b.append(" [mergeDocStores]");
/*     */       }
/* 140 */       return b.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.MergePolicy
 * JD-Core Version:    0.6.2
 */