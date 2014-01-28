/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexOutput;
/*     */ import org.apache.lucene.store.RAMOutputStream;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ 
/*     */ final class TermVectorsTermsWriter extends TermsHashConsumer
/*     */ {
/*     */   final DocumentsWriter docWriter;
/*     */   TermVectorsWriter termVectorsWriter;
/*  33 */   PerDoc[] docFreeList = new PerDoc[1];
/*     */   int freeCount;
/*     */   IndexOutput tvx;
/*     */   IndexOutput tvd;
/*     */   IndexOutput tvf;
/*     */   int lastDocID;
/*     */   int allocCount;
/*     */ 
/*     */   public TermVectorsTermsWriter(DocumentsWriter docWriter)
/*     */   {
/*  41 */     this.docWriter = docWriter;
/*     */   }
/*     */ 
/*     */   public TermsHashConsumerPerThread addThread(TermsHashPerThread termsHashPerThread)
/*     */   {
/*  46 */     return new TermVectorsTermsWriterPerThread(termsHashPerThread, this);
/*     */   }
/*     */ 
/*     */   void createPostings(RawPostingList[] postings, int start, int count)
/*     */   {
/*  51 */     int end = start + count;
/*  52 */     for (int i = start; i < end; i++)
/*  53 */       postings[i] = new PostingList();
/*     */   }
/*     */ 
/*     */   synchronized void flush(Map<TermsHashConsumerPerThread, Collection<TermsHashConsumerPerField>> threadsAndFields, SegmentWriteState state)
/*     */     throws IOException
/*     */   {
/*  59 */     if (this.tvx != null)
/*     */     {
/*  61 */       if (state.numDocsInStore > 0)
/*     */       {
/*  64 */         fill(state.numDocsInStore - this.docWriter.getDocStoreOffset());
/*     */       }
/*  66 */       this.tvx.flush();
/*  67 */       this.tvd.flush();
/*  68 */       this.tvf.flush();
/*     */     }
/*     */ 
/*  71 */     for (Map.Entry entry : threadsAndFields.entrySet()) {
/*  72 */       for (TermsHashConsumerPerField field : (Collection)entry.getValue()) {
/*  73 */         TermVectorsTermsWriterPerField perField = (TermVectorsTermsWriterPerField)field;
/*  74 */         perField.termsHashPerField.reset();
/*  75 */         perField.shrinkHash();
/*     */       }
/*     */ 
/*  78 */       TermVectorsTermsWriterPerThread perThread = (TermVectorsTermsWriterPerThread)entry.getKey();
/*  79 */       perThread.termsHashPerThread.reset(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void closeDocStore(SegmentWriteState state) throws IOException
/*     */   {
/*  85 */     if (this.tvx != null)
/*     */     {
/*  88 */       fill(state.numDocsInStore - this.docWriter.getDocStoreOffset());
/*  89 */       this.tvx.close();
/*  90 */       this.tvf.close();
/*  91 */       this.tvd.close();
/*  92 */       this.tvx = null;
/*  93 */       assert (state.docStoreSegmentName != null);
/*  94 */       String fileName = state.docStoreSegmentName + "." + "tvx";
/*  95 */       if (4L + state.numDocsInStore * 16L != state.directory.fileLength(fileName)) {
/*  96 */         throw new RuntimeException("after flush: tvx size mismatch: " + state.numDocsInStore + " docs vs " + state.directory.fileLength(fileName) + " length in bytes of " + fileName + " file exists?=" + state.directory.fileExists(fileName));
/*     */       }
/*  98 */       state.flushedFiles.add(state.docStoreSegmentName + "." + "tvx");
/*  99 */       state.flushedFiles.add(state.docStoreSegmentName + "." + "tvf");
/* 100 */       state.flushedFiles.add(state.docStoreSegmentName + "." + "tvd");
/*     */ 
/* 102 */       this.docWriter.removeOpenFile(state.docStoreSegmentName + "." + "tvx");
/* 103 */       this.docWriter.removeOpenFile(state.docStoreSegmentName + "." + "tvf");
/* 104 */       this.docWriter.removeOpenFile(state.docStoreSegmentName + "." + "tvd");
/*     */ 
/* 106 */       this.lastDocID = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized PerDoc getPerDoc()
/*     */   {
/* 113 */     if (this.freeCount == 0) {
/* 114 */       this.allocCount += 1;
/* 115 */       if (this.allocCount > this.docFreeList.length)
/*     */       {
/* 119 */         assert (this.allocCount == 1 + this.docFreeList.length);
/* 120 */         this.docFreeList = new PerDoc[ArrayUtil.getNextSize(this.allocCount)];
/*     */       }
/* 122 */       return new PerDoc();
/*     */     }
/* 124 */     return this.docFreeList[(--this.freeCount)];
/*     */   }
/*     */ 
/*     */   void fill(int docID)
/*     */     throws IOException
/*     */   {
/* 130 */     int docStoreOffset = this.docWriter.getDocStoreOffset();
/* 131 */     int end = docID + docStoreOffset;
/* 132 */     if (this.lastDocID < end) {
/* 133 */       long tvfPosition = this.tvf.getFilePointer();
/* 134 */       while (this.lastDocID < end) {
/* 135 */         this.tvx.writeLong(this.tvd.getFilePointer());
/* 136 */         this.tvd.writeVInt(0);
/* 137 */         this.tvx.writeLong(tvfPosition);
/* 138 */         this.lastDocID += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void initTermVectorsWriter() throws IOException {
/* 144 */     if (this.tvx == null)
/*     */     {
/* 146 */       String docStoreSegment = this.docWriter.getDocStoreSegment();
/*     */ 
/* 148 */       if (docStoreSegment == null) {
/* 149 */         return;
/*     */       }
/* 151 */       assert (docStoreSegment != null);
/*     */ 
/* 157 */       this.tvx = this.docWriter.directory.createOutput(docStoreSegment + "." + "tvx");
/* 158 */       this.tvd = this.docWriter.directory.createOutput(docStoreSegment + "." + "tvd");
/* 159 */       this.tvf = this.docWriter.directory.createOutput(docStoreSegment + "." + "tvf");
/*     */ 
/* 161 */       this.tvx.writeInt(4);
/* 162 */       this.tvd.writeInt(4);
/* 163 */       this.tvf.writeInt(4);
/*     */ 
/* 165 */       this.docWriter.addOpenFile(docStoreSegment + "." + "tvx");
/* 166 */       this.docWriter.addOpenFile(docStoreSegment + "." + "tvf");
/* 167 */       this.docWriter.addOpenFile(docStoreSegment + "." + "tvd");
/*     */ 
/* 169 */       this.lastDocID = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void finishDocument(PerDoc perDoc) throws IOException
/*     */   {
/* 175 */     assert (this.docWriter.writer.testPoint("TermVectorsTermsWriter.finishDocument start"));
/*     */ 
/* 177 */     initTermVectorsWriter();
/*     */ 
/* 179 */     fill(perDoc.docID);
/*     */ 
/* 182 */     this.tvx.writeLong(this.tvd.getFilePointer());
/* 183 */     this.tvx.writeLong(this.tvf.getFilePointer());
/* 184 */     this.tvd.writeVInt(perDoc.numVectorFields);
/* 185 */     if (perDoc.numVectorFields > 0) {
/* 186 */       for (int i = 0; i < perDoc.numVectorFields; i++)
/* 187 */         this.tvd.writeVInt(perDoc.fieldNumbers[i]);
/* 188 */       assert (0L == perDoc.fieldPointers[0]);
/* 189 */       long lastPos = perDoc.fieldPointers[0];
/* 190 */       for (int i = 1; i < perDoc.numVectorFields; i++) {
/* 191 */         long pos = perDoc.fieldPointers[i];
/* 192 */         this.tvd.writeVLong(pos - lastPos);
/* 193 */         lastPos = pos;
/*     */       }
/* 195 */       perDoc.perDocTvf.writeTo(this.tvf);
/* 196 */       perDoc.numVectorFields = 0;
/*     */     }
/*     */ 
/* 199 */     assert (this.lastDocID == perDoc.docID + this.docWriter.getDocStoreOffset());
/*     */ 
/* 201 */     this.lastDocID += 1;
/*     */ 
/* 203 */     perDoc.reset();
/* 204 */     free(perDoc);
/* 205 */     assert (this.docWriter.writer.testPoint("TermVectorsTermsWriter.finishDocument end"));
/*     */   }
/*     */ 
/*     */   public boolean freeRAM()
/*     */   {
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   public void abort()
/*     */   {
/* 216 */     if (this.tvx != null) {
/*     */       try {
/* 218 */         this.tvx.close();
/*     */       } catch (Throwable t) {
/*     */       }
/* 221 */       this.tvx = null;
/*     */     }
/* 223 */     if (this.tvd != null) {
/*     */       try {
/* 225 */         this.tvd.close();
/*     */       } catch (Throwable t) {
/*     */       }
/* 228 */       this.tvd = null;
/*     */     }
/* 230 */     if (this.tvf != null) {
/*     */       try {
/* 232 */         this.tvf.close();
/*     */       } catch (Throwable t) {
/*     */       }
/* 235 */       this.tvf = null;
/*     */     }
/* 237 */     this.lastDocID = 0;
/*     */   }
/*     */ 
/*     */   synchronized void free(PerDoc doc) {
/* 241 */     assert (this.freeCount < this.docFreeList.length);
/* 242 */     this.docFreeList[(this.freeCount++)] = doc;
/*     */   }
/*     */ 
/*     */   int bytesPerPosting()
/*     */   {
/* 296 */     return 32;
/*     */   }
/*     */ 
/*     */   static final class PostingList extends RawPostingList
/*     */   {
/*     */     int freq;
/*     */     int lastOffset;
/*     */     int lastPosition;
/*     */   }
/*     */ 
/*     */   class PerDoc extends DocumentsWriter.DocWriter
/*     */   {
/* 247 */     final DocumentsWriter.PerDocBuffer buffer = TermVectorsTermsWriter.this.docWriter.newPerDocBuffer();
/* 248 */     RAMOutputStream perDocTvf = new RAMOutputStream(this.buffer);
/*     */     int numVectorFields;
/* 252 */     int[] fieldNumbers = new int[1];
/* 253 */     long[] fieldPointers = new long[1];
/*     */ 
/*     */     PerDoc() {  } 
/* 256 */     void reset() { this.perDocTvf.reset();
/* 257 */       this.buffer.recycle();
/* 258 */       this.numVectorFields = 0;
/*     */     }
/*     */ 
/*     */     void abort()
/*     */     {
/* 263 */       reset();
/* 264 */       TermVectorsTermsWriter.this.free(this);
/*     */     }
/*     */ 
/*     */     void addField(int fieldNumber) {
/* 268 */       if (this.numVectorFields == this.fieldNumbers.length) {
/* 269 */         this.fieldNumbers = ArrayUtil.grow(this.fieldNumbers);
/* 270 */         this.fieldPointers = ArrayUtil.grow(this.fieldPointers);
/*     */       }
/* 272 */       this.fieldNumbers[this.numVectorFields] = fieldNumber;
/* 273 */       this.fieldPointers[this.numVectorFields] = this.perDocTvf.getFilePointer();
/* 274 */       this.numVectorFields += 1;
/*     */     }
/*     */ 
/*     */     public long sizeInBytes()
/*     */     {
/* 279 */       return this.buffer.getSizeInBytes();
/*     */     }
/*     */ 
/*     */     public void finish() throws IOException
/*     */     {
/* 284 */       TermVectorsTermsWriter.this.finishDocument(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermVectorsTermsWriter
 * JD-Core Version:    0.6.2
 */