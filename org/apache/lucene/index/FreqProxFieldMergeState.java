/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ final class FreqProxFieldMergeState
/*     */ {
/*     */   final FreqProxTermsWriterPerField field;
/*     */   final int numPostings;
/*     */   final CharBlockPool charPool;
/*     */   final RawPostingList[] postings;
/*     */   private FreqProxTermsWriter.PostingList p;
/*     */   char[] text;
/*     */   int textOffset;
/*  39 */   private int postingUpto = -1;
/*     */ 
/*  41 */   final ByteSliceReader freq = new ByteSliceReader();
/*  42 */   final ByteSliceReader prox = new ByteSliceReader();
/*     */   int docID;
/*     */   int termFreq;
/*     */ 
/*     */   public FreqProxFieldMergeState(FreqProxTermsWriterPerField field)
/*     */   {
/*  48 */     this.field = field;
/*  49 */     this.charPool = field.perThread.termsHashPerThread.charPool;
/*  50 */     this.numPostings = field.termsHashPerField.numPostings;
/*  51 */     this.postings = field.termsHashPerField.sortPostings();
/*     */   }
/*     */ 
/*     */   boolean nextTerm() throws IOException {
/*  55 */     this.postingUpto += 1;
/*  56 */     if (this.postingUpto == this.numPostings) {
/*  57 */       return false;
/*     */     }
/*  59 */     this.p = ((FreqProxTermsWriter.PostingList)this.postings[this.postingUpto]);
/*  60 */     this.docID = 0;
/*     */ 
/*  62 */     this.text = this.charPool.buffers[(this.p.textStart >> 14)];
/*  63 */     this.textOffset = (this.p.textStart & 0x3FFF);
/*     */ 
/*  65 */     this.field.termsHashPerField.initReader(this.freq, this.p, 0);
/*  66 */     if (!this.field.fieldInfo.omitTermFreqAndPositions) {
/*  67 */       this.field.termsHashPerField.initReader(this.prox, this.p, 1);
/*     */     }
/*     */ 
/*  70 */     boolean result = nextDoc();
/*  71 */     assert (result);
/*     */ 
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean nextDoc() throws IOException {
/*  77 */     if (this.freq.eof()) {
/*  78 */       if (this.p.lastDocCode != -1)
/*     */       {
/*  80 */         this.docID = this.p.lastDocID;
/*  81 */         if (!this.field.omitTermFreqAndPositions)
/*  82 */           this.termFreq = this.p.docFreq;
/*  83 */         this.p.lastDocCode = -1;
/*  84 */         return true;
/*     */       }
/*     */ 
/*  87 */       return false;
/*     */     }
/*     */ 
/*  90 */     int code = this.freq.readVInt();
/*  91 */     if (this.field.omitTermFreqAndPositions) {
/*  92 */       this.docID += code;
/*     */     } else {
/*  94 */       this.docID += (code >>> 1);
/*  95 */       if ((code & 0x1) != 0)
/*  96 */         this.termFreq = 1;
/*     */       else {
/*  98 */         this.termFreq = this.freq.readVInt();
/*     */       }
/*     */     }
/* 101 */     assert (this.docID != this.p.lastDocID);
/*     */ 
/* 103 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FreqProxFieldMergeState
 * JD-Core Version:    0.6.2
 */