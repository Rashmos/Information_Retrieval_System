/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.RAMOutputStream;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ 
/*     */ final class StoredFieldsWriter
/*     */ {
/*     */   FieldsWriter fieldsWriter;
/*     */   final DocumentsWriter docWriter;
/*     */   final FieldInfos fieldInfos;
/*     */   int lastDocID;
/*  32 */   PerDoc[] docFreeList = new PerDoc[1];
/*     */   int freeCount;
/*     */   int allocCount;
/*     */ 
/*     */   public StoredFieldsWriter(DocumentsWriter docWriter, FieldInfos fieldInfos)
/*     */   {
/*  36 */     this.docWriter = docWriter;
/*  37 */     this.fieldInfos = fieldInfos;
/*     */   }
/*     */ 
/*     */   public StoredFieldsWriterPerThread addThread(DocumentsWriter.DocState docState) throws IOException {
/*  41 */     return new StoredFieldsWriterPerThread(docState, this);
/*     */   }
/*     */ 
/*     */   public synchronized void flush(SegmentWriteState state) throws IOException
/*     */   {
/*  46 */     if (state.numDocsInStore > 0)
/*     */     {
/*  50 */       initFieldsWriter();
/*     */ 
/*  54 */       fill(state.numDocsInStore - this.docWriter.getDocStoreOffset());
/*     */     }
/*     */ 
/*  57 */     if (this.fieldsWriter != null)
/*  58 */       this.fieldsWriter.flush();
/*     */   }
/*     */ 
/*     */   private void initFieldsWriter() throws IOException {
/*  62 */     if (this.fieldsWriter == null) {
/*  63 */       String docStoreSegment = this.docWriter.getDocStoreSegment();
/*  64 */       if (docStoreSegment != null) {
/*  65 */         assert (docStoreSegment != null);
/*  66 */         this.fieldsWriter = new FieldsWriter(this.docWriter.directory, docStoreSegment, this.fieldInfos);
/*     */ 
/*  69 */         this.docWriter.addOpenFile(docStoreSegment + "." + "fdt");
/*  70 */         this.docWriter.addOpenFile(docStoreSegment + "." + "fdx");
/*  71 */         this.lastDocID = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void closeDocStore(SegmentWriteState state) throws IOException {
/*  77 */     int inc = state.numDocsInStore - this.lastDocID;
/*  78 */     if (inc > 0) {
/*  79 */       initFieldsWriter();
/*  80 */       fill(state.numDocsInStore - this.docWriter.getDocStoreOffset());
/*     */     }
/*     */ 
/*  83 */     if (this.fieldsWriter != null) {
/*  84 */       this.fieldsWriter.close();
/*  85 */       this.fieldsWriter = null;
/*  86 */       this.lastDocID = 0;
/*  87 */       assert (state.docStoreSegmentName != null);
/*  88 */       state.flushedFiles.add(state.docStoreSegmentName + "." + "fdt");
/*  89 */       state.flushedFiles.add(state.docStoreSegmentName + "." + "fdx");
/*     */ 
/*  91 */       state.docWriter.removeOpenFile(state.docStoreSegmentName + "." + "fdt");
/*  92 */       state.docWriter.removeOpenFile(state.docStoreSegmentName + "." + "fdx");
/*     */ 
/*  94 */       String fileName = state.docStoreSegmentName + "." + "fdx";
/*     */ 
/*  96 */       if (4L + state.numDocsInStore * 8L != state.directory.fileLength(fileName))
/*  97 */         throw new RuntimeException("after flush: fdx size mismatch: " + state.numDocsInStore + " docs vs " + state.directory.fileLength(fileName) + " length in bytes of " + fileName + " file exists?=" + state.directory.fileExists(fileName));
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized PerDoc getPerDoc()
/*     */   {
/* 104 */     if (this.freeCount == 0) {
/* 105 */       this.allocCount += 1;
/* 106 */       if (this.allocCount > this.docFreeList.length)
/*     */       {
/* 110 */         assert (this.allocCount == 1 + this.docFreeList.length);
/* 111 */         this.docFreeList = new PerDoc[ArrayUtil.getNextSize(this.allocCount)];
/*     */       }
/* 113 */       return new PerDoc();
/*     */     }
/* 115 */     return this.docFreeList[(--this.freeCount)];
/*     */   }
/*     */ 
/*     */   synchronized void abort() {
/* 119 */     if (this.fieldsWriter != null) {
/*     */       try {
/* 121 */         this.fieldsWriter.close();
/*     */       } catch (Throwable t) {
/*     */       }
/* 124 */       this.fieldsWriter = null;
/* 125 */       this.lastDocID = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   void fill(int docID) throws IOException
/*     */   {
/* 131 */     int docStoreOffset = this.docWriter.getDocStoreOffset();
/*     */ 
/* 135 */     int end = docID + docStoreOffset;
/* 136 */     while (this.lastDocID < end) {
/* 137 */       this.fieldsWriter.skipDocument();
/* 138 */       this.lastDocID += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void finishDocument(PerDoc perDoc) throws IOException {
/* 143 */     assert (this.docWriter.writer.testPoint("StoredFieldsWriter.finishDocument start"));
/* 144 */     initFieldsWriter();
/*     */ 
/* 146 */     fill(perDoc.docID);
/*     */ 
/* 149 */     this.fieldsWriter.flushDocument(perDoc.numStoredFields, perDoc.fdt);
/* 150 */     this.lastDocID += 1;
/* 151 */     perDoc.reset();
/* 152 */     free(perDoc);
/* 153 */     assert (this.docWriter.writer.testPoint("StoredFieldsWriter.finishDocument end"));
/*     */   }
/*     */ 
/*     */   public boolean freeRAM() {
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   synchronized void free(PerDoc perDoc) {
/* 161 */     assert (this.freeCount < this.docFreeList.length);
/* 162 */     assert (0 == perDoc.numStoredFields);
/* 163 */     assert (0L == perDoc.fdt.length());
/* 164 */     assert (0L == perDoc.fdt.getFilePointer());
/* 165 */     this.docFreeList[(this.freeCount++)] = perDoc;
/*     */   }
/* 169 */   class PerDoc extends DocumentsWriter.DocWriter { final DocumentsWriter.PerDocBuffer buffer = StoredFieldsWriter.this.docWriter.newPerDocBuffer();
/* 170 */     RAMOutputStream fdt = new RAMOutputStream(this.buffer);
/*     */     int numStoredFields;
/*     */ 
/*     */     PerDoc() {  } 
/* 174 */     void reset() { this.fdt.reset();
/* 175 */       this.buffer.recycle();
/* 176 */       this.numStoredFields = 0;
/*     */     }
/*     */ 
/*     */     void abort()
/*     */     {
/* 181 */       reset();
/* 182 */       StoredFieldsWriter.this.free(this);
/*     */     }
/*     */ 
/*     */     public long sizeInBytes()
/*     */     {
/* 187 */       return this.buffer.getSizeInBytes();
/*     */     }
/*     */ 
/*     */     public void finish() throws IOException
/*     */     {
/* 192 */       StoredFieldsWriter.this.finishDocument(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.StoredFieldsWriter
 * JD-Core Version:    0.6.2
 */