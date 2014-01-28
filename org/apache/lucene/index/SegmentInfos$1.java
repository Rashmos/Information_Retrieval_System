/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.store.Directory;
/*     */ 
/*     */ class SegmentInfos$1 extends SegmentInfos.FindSegmentsFile
/*     */ {
/*     */   SegmentInfos$1(SegmentInfos paramSegmentInfos, Directory x0)
/*     */     throws IOException
/*     */   {
/* 311 */     super(x0); } 
/* 312 */   protected Object doBody(String segmentFileName) throws CorruptIndexException, IOException { this.this$0.read(this.directory, segmentFileName);
/* 313 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentInfos.1
 * JD-Core Version:    0.6.2
 */