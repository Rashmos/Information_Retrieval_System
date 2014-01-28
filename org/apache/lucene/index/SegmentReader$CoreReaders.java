/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexInput;
/*     */ 
/*     */ final class SegmentReader$CoreReaders
/*     */ {
/*  83 */   private final SegmentReader.Ref ref = new SegmentReader.Ref();
/*     */   final String segment;
/*     */   final FieldInfos fieldInfos;
/*     */   final IndexInput freqStream;
/*     */   final IndexInput proxStream;
/*     */   final TermInfosReader tisNoIndex;
/*     */   final Directory dir;
/*     */   final Directory cfsDir;
/*     */   final int readBufferSize;
/*     */   final int termsIndexDivisor;
/*     */   private final SegmentReader origInstance;
/*     */   TermInfosReader tis;
/*     */   FieldsReader fieldsReaderOrig;
/*     */   TermVectorsReader termVectorsReaderOrig;
/*     */   CompoundFileReader cfsReader;
/*     */   CompoundFileReader storeCFSReader;
/*     */ 
/*     */   SegmentReader$CoreReaders(SegmentReader origInstance, Directory dir, SegmentInfo si, int readBufferSize, int termsIndexDivisor)
/*     */     throws IOException
/*     */   {
/* 105 */     this.segment = si.name;
/* 106 */     this.readBufferSize = readBufferSize;
/* 107 */     this.dir = dir;
/*     */ 
/* 109 */     boolean success = false;
/*     */     try
/*     */     {
/* 112 */       Directory dir0 = dir;
/* 113 */       if (si.getUseCompoundFile()) {
/* 114 */         this.cfsReader = new CompoundFileReader(dir, this.segment + "." + "cfs", readBufferSize);
/* 115 */         dir0 = this.cfsReader;
/*     */       }
/* 117 */       this.cfsDir = dir0;
/*     */ 
/* 119 */       this.fieldInfos = new FieldInfos(this.cfsDir, this.segment + "." + "fnm");
/*     */ 
/* 121 */       this.termsIndexDivisor = termsIndexDivisor;
/* 122 */       TermInfosReader reader = new TermInfosReader(this.cfsDir, this.segment, this.fieldInfos, readBufferSize, termsIndexDivisor);
/* 123 */       if (termsIndexDivisor == -1) {
/* 124 */         this.tisNoIndex = reader;
/*     */       } else {
/* 126 */         this.tis = reader;
/* 127 */         this.tisNoIndex = null;
/*     */       }
/*     */ 
/* 132 */       this.freqStream = this.cfsDir.openInput(this.segment + "." + "frq", readBufferSize);
/*     */ 
/* 134 */       if (this.fieldInfos.hasProx())
/* 135 */         this.proxStream = this.cfsDir.openInput(this.segment + "." + "prx", readBufferSize);
/*     */       else {
/* 137 */         this.proxStream = null;
/*     */       }
/* 139 */       success = true;
/*     */     } finally {
/* 141 */       if (!success) {
/* 142 */         decRef();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 150 */     this.origInstance = origInstance;
/*     */   }
/*     */ 
/*     */   synchronized TermVectorsReader getTermVectorsReaderOrig() {
/* 154 */     return this.termVectorsReaderOrig;
/*     */   }
/*     */ 
/*     */   synchronized FieldsReader getFieldsReaderOrig() {
/* 158 */     return this.fieldsReaderOrig;
/*     */   }
/*     */ 
/*     */   synchronized void incRef() {
/* 162 */     this.ref.incRef();
/*     */   }
/*     */ 
/*     */   synchronized Directory getCFSReader() {
/* 166 */     return this.cfsReader;
/*     */   }
/*     */ 
/*     */   synchronized TermInfosReader getTermsReader() {
/* 170 */     if (this.tis != null) {
/* 171 */       return this.tis;
/*     */     }
/* 173 */     return this.tisNoIndex;
/*     */   }
/*     */ 
/*     */   synchronized boolean termsIndexIsLoaded()
/*     */   {
/* 178 */     return this.tis != null;
/*     */   }
/*     */ 
/*     */   synchronized void loadTermsIndex(SegmentInfo si, int termsIndexDivisor)
/*     */     throws IOException
/*     */   {
/* 187 */     if (this.tis == null)
/*     */     {
/*     */       Directory dir0;
/*     */       Directory dir0;
/* 189 */       if (si.getUseCompoundFile())
/*     */       {
/* 194 */         if (this.cfsReader == null) {
/* 195 */           this.cfsReader = new CompoundFileReader(this.dir, this.segment + "." + "cfs", this.readBufferSize);
/*     */         }
/* 197 */         dir0 = this.cfsReader;
/*     */       } else {
/* 199 */         dir0 = this.dir;
/*     */       }
/*     */ 
/* 202 */       this.tis = new TermInfosReader(dir0, this.segment, this.fieldInfos, this.readBufferSize, termsIndexDivisor);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void decRef() throws IOException
/*     */   {
/* 208 */     if (this.ref.decRef() == 0)
/*     */     {
/* 211 */       if (this.tis != null) {
/* 212 */         this.tis.close();
/*     */ 
/* 214 */         this.tis = null;
/*     */       }
/*     */ 
/* 217 */       if (this.tisNoIndex != null) {
/* 218 */         this.tisNoIndex.close();
/*     */       }
/*     */ 
/* 221 */       if (this.freqStream != null) {
/* 222 */         this.freqStream.close();
/*     */       }
/*     */ 
/* 225 */       if (this.proxStream != null) {
/* 226 */         this.proxStream.close();
/*     */       }
/*     */ 
/* 229 */       if (this.termVectorsReaderOrig != null) {
/* 230 */         this.termVectorsReaderOrig.close();
/*     */       }
/*     */ 
/* 233 */       if (this.fieldsReaderOrig != null) {
/* 234 */         this.fieldsReaderOrig.close();
/*     */       }
/*     */ 
/* 237 */       if (this.cfsReader != null) {
/* 238 */         this.cfsReader.close();
/*     */       }
/*     */ 
/* 241 */       if (this.storeCFSReader != null) {
/* 242 */         this.storeCFSReader.close();
/*     */       }
/*     */ 
/* 246 */       if (this.origInstance != null)
/* 247 */         FieldCache.DEFAULT.purge(this.origInstance);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void openDocStores(SegmentInfo si)
/*     */     throws IOException
/*     */   {
/* 254 */     assert (si.name.equals(this.segment));
/*     */ 
/* 256 */     if (this.fieldsReaderOrig == null)
/*     */     {
/*     */       Directory storeDir;
/* 258 */       if (si.getDocStoreOffset() != -1) {
/* 259 */         if (si.getDocStoreIsCompoundFile()) {
/* 260 */           assert (this.storeCFSReader == null);
/* 261 */           this.storeCFSReader = new CompoundFileReader(this.dir, si.getDocStoreSegment() + "." + "cfx", this.readBufferSize);
/*     */ 
/* 264 */           Directory storeDir = this.storeCFSReader;
/* 265 */           if ((!$assertionsDisabled) && (storeDir == null)) throw new AssertionError(); 
/*     */         }
/* 267 */         else { Directory storeDir = this.dir;
/* 268 */           if ((!$assertionsDisabled) && (storeDir == null)) throw new AssertionError(); 
/*     */         }
/*     */       }
/* 270 */       else if (si.getUseCompoundFile())
/*     */       {
/* 274 */         if (this.cfsReader == null) {
/* 275 */           this.cfsReader = new CompoundFileReader(this.dir, this.segment + "." + "cfs", this.readBufferSize);
/*     */         }
/* 277 */         Directory storeDir = this.cfsReader;
/* 278 */         if ((!$assertionsDisabled) && (storeDir == null)) throw new AssertionError(); 
/*     */       }
/* 280 */       else { storeDir = this.dir;
/* 281 */         assert (storeDir != null);
/*     */       }
/*     */       String storesSegment;
/*     */       String storesSegment;
/* 285 */       if (si.getDocStoreOffset() != -1)
/* 286 */         storesSegment = si.getDocStoreSegment();
/*     */       else {
/* 288 */         storesSegment = this.segment;
/*     */       }
/*     */ 
/* 291 */       this.fieldsReaderOrig = new FieldsReader(storeDir, storesSegment, this.fieldInfos, this.readBufferSize, si.getDocStoreOffset(), si.docCount);
/*     */ 
/* 295 */       if ((si.getDocStoreOffset() == -1) && (this.fieldsReaderOrig.size() != si.docCount)) {
/* 296 */         throw new CorruptIndexException("doc counts differ for segment " + this.segment + ": fieldsReader shows " + this.fieldsReaderOrig.size() + " but segmentInfo shows " + si.docCount);
/*     */       }
/*     */ 
/* 299 */       if (this.fieldInfos.hasVectors())
/* 300 */         this.termVectorsReaderOrig = new TermVectorsReader(storeDir, storesSegment, this.fieldInfos, this.readBufferSize, si.getDocStoreOffset(), si.docCount);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentReader.CoreReaders
 * JD-Core Version:    0.6.2
 */