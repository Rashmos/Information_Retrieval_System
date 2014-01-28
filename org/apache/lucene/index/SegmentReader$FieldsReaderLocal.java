/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import org.apache.lucene.util.CloseableThreadLocal;
/*     */ 
/*     */ class SegmentReader$FieldsReaderLocal extends CloseableThreadLocal<FieldsReader>
/*     */ {
/*     */   private SegmentReader$FieldsReaderLocal(SegmentReader paramSegmentReader)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected FieldsReader initialValue()
/*     */   {
/* 312 */     return (FieldsReader)this.this$0.core.getFieldsReaderOrig().clone();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentReader.FieldsReaderLocal
 * JD-Core Version:    0.6.2
 */