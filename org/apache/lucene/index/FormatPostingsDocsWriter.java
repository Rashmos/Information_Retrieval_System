/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexOutput;
/*     */ import org.apache.lucene.util.UnicodeUtil;
/*     */ import org.apache.lucene.util.UnicodeUtil.UTF8Result;
/*     */ 
/*     */ final class FormatPostingsDocsWriter extends FormatPostingsDocsConsumer
/*     */ {
/*     */   final IndexOutput out;
/*     */   final FormatPostingsTermsWriter parent;
/*     */   final FormatPostingsPositionsWriter posWriter;
/*     */   final DefaultSkipListWriter skipListWriter;
/*     */   final int skipInterval;
/*     */   final int totalNumDocs;
/*     */   boolean omitTermFreqAndPositions;
/*     */   boolean storePayloads;
/*     */   long freqStart;
/*     */   FieldInfo fieldInfo;
/*     */   int lastDocID;
/*     */   int df;
/*  99 */   private final TermInfo termInfo = new TermInfo();
/* 100 */   final UnicodeUtil.UTF8Result utf8 = new UnicodeUtil.UTF8Result();
/*     */ 
/*     */   FormatPostingsDocsWriter(SegmentWriteState state, FormatPostingsTermsWriter parent)
/*     */     throws IOException
/*     */   {
/*  44 */     this.parent = parent;
/*  45 */     String fileName = IndexFileNames.segmentFileName(parent.parent.segment, "frq");
/*  46 */     state.flushedFiles.add(fileName);
/*  47 */     this.out = parent.parent.dir.createOutput(fileName);
/*  48 */     this.totalNumDocs = parent.parent.totalNumDocs;
/*     */ 
/*  51 */     this.skipInterval = parent.parent.termsOut.skipInterval;
/*  52 */     this.skipListWriter = parent.parent.skipListWriter;
/*  53 */     this.skipListWriter.setFreqOutput(this.out);
/*     */ 
/*  55 */     this.posWriter = new FormatPostingsPositionsWriter(state, this);
/*     */   }
/*     */ 
/*     */   void setField(FieldInfo fieldInfo) {
/*  59 */     this.fieldInfo = fieldInfo;
/*  60 */     this.omitTermFreqAndPositions = fieldInfo.omitTermFreqAndPositions;
/*  61 */     this.storePayloads = fieldInfo.storePayloads;
/*  62 */     this.posWriter.setField(fieldInfo);
/*     */   }
/*     */ 
/*     */   FormatPostingsPositionsConsumer addDoc(int docID, int termDocFreq)
/*     */     throws IOException
/*     */   {
/*  73 */     int delta = docID - this.lastDocID;
/*     */ 
/*  75 */     if ((docID < 0) || ((this.df > 0) && (delta <= 0))) {
/*  76 */       throw new CorruptIndexException("docs out of order (" + docID + " <= " + this.lastDocID + " )");
/*     */     }
/*  78 */     if (++this.df % this.skipInterval == 0)
/*     */     {
/*  80 */       this.skipListWriter.setSkipData(this.lastDocID, this.storePayloads, this.posWriter.lastPayloadLength);
/*  81 */       this.skipListWriter.bufferSkip(this.df);
/*     */     }
/*     */ 
/*  84 */     assert (docID < this.totalNumDocs) : ("docID=" + docID + " totalNumDocs=" + this.totalNumDocs);
/*     */ 
/*  86 */     this.lastDocID = docID;
/*  87 */     if (this.omitTermFreqAndPositions) {
/*  88 */       this.out.writeVInt(delta);
/*  89 */     } else if (1 == termDocFreq) {
/*  90 */       this.out.writeVInt(delta << 1 | 0x1);
/*     */     } else {
/*  92 */       this.out.writeVInt(delta << 1);
/*  93 */       this.out.writeVInt(termDocFreq);
/*     */     }
/*     */ 
/*  96 */     return this.posWriter;
/*     */   }
/*     */ 
/*     */   void finish()
/*     */     throws IOException
/*     */   {
/* 105 */     long skipPointer = this.skipListWriter.writeSkip(this.out);
/*     */ 
/* 109 */     this.termInfo.set(this.df, this.parent.freqStart, this.parent.proxStart, (int)(skipPointer - this.parent.freqStart));
/*     */ 
/* 112 */     UnicodeUtil.UTF16toUTF8(this.parent.currentTerm, this.parent.currentTermStart, this.utf8);
/*     */ 
/* 114 */     if (this.df > 0) {
/* 115 */       this.parent.termsOut.add(this.fieldInfo.number, this.utf8.result, this.utf8.length, this.termInfo);
/*     */     }
/*     */ 
/* 121 */     this.lastDocID = 0;
/* 122 */     this.df = 0;
/*     */   }
/*     */ 
/*     */   void close() throws IOException {
/* 126 */     this.out.close();
/* 127 */     this.posWriter.close();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FormatPostingsDocsWriter
 * JD-Core Version:    0.6.2
 */