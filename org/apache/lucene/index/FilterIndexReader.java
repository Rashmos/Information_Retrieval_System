/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.apache.lucene.document.Document;
/*     */ import org.apache.lucene.document.FieldSelector;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.store.Directory;
/*     */ 
/*     */ public class FilterIndexReader extends IndexReader
/*     */ {
/*     */   protected IndexReader in;
/*     */ 
/*     */   public FilterIndexReader(IndexReader in)
/*     */   {
/* 110 */     this.in = in;
/*     */   }
/*     */ 
/*     */   public Directory directory()
/*     */   {
/* 115 */     return this.in.directory();
/*     */   }
/*     */ 
/*     */   public TermFreqVector[] getTermFreqVectors(int docNumber)
/*     */     throws IOException
/*     */   {
/* 121 */     ensureOpen();
/* 122 */     return this.in.getTermFreqVectors(docNumber);
/*     */   }
/*     */ 
/*     */   public TermFreqVector getTermFreqVector(int docNumber, String field)
/*     */     throws IOException
/*     */   {
/* 128 */     ensureOpen();
/* 129 */     return this.in.getTermFreqVector(docNumber, field);
/*     */   }
/*     */ 
/*     */   public void getTermFreqVector(int docNumber, String field, TermVectorMapper mapper)
/*     */     throws IOException
/*     */   {
/* 135 */     ensureOpen();
/* 136 */     this.in.getTermFreqVector(docNumber, field, mapper);
/*     */   }
/*     */ 
/*     */   public void getTermFreqVector(int docNumber, TermVectorMapper mapper)
/*     */     throws IOException
/*     */   {
/* 142 */     ensureOpen();
/* 143 */     this.in.getTermFreqVector(docNumber, mapper);
/*     */   }
/*     */ 
/*     */   public int numDocs()
/*     */   {
/* 149 */     return this.in.numDocs();
/*     */   }
/*     */ 
/*     */   public int maxDoc()
/*     */   {
/* 155 */     return this.in.maxDoc();
/*     */   }
/*     */ 
/*     */   public Document document(int n, FieldSelector fieldSelector) throws CorruptIndexException, IOException
/*     */   {
/* 160 */     ensureOpen();
/* 161 */     return this.in.document(n, fieldSelector);
/*     */   }
/*     */ 
/*     */   public boolean isDeleted(int n)
/*     */   {
/* 167 */     return this.in.isDeleted(n);
/*     */   }
/*     */ 
/*     */   public boolean hasDeletions()
/*     */   {
/* 173 */     return this.in.hasDeletions();
/*     */   }
/*     */ 
/*     */   protected void doUndeleteAll() throws CorruptIndexException, IOException {
/* 177 */     this.in.undeleteAll();
/*     */   }
/*     */ 
/*     */   public boolean hasNorms(String field) throws IOException {
/* 181 */     ensureOpen();
/* 182 */     return this.in.hasNorms(field);
/*     */   }
/*     */ 
/*     */   public byte[] norms(String f) throws IOException
/*     */   {
/* 187 */     ensureOpen();
/* 188 */     return this.in.norms(f);
/*     */   }
/*     */ 
/*     */   public void norms(String f, byte[] bytes, int offset) throws IOException
/*     */   {
/* 193 */     ensureOpen();
/* 194 */     this.in.norms(f, bytes, offset);
/*     */   }
/*     */ 
/*     */   protected void doSetNorm(int d, String f, byte b) throws CorruptIndexException, IOException
/*     */   {
/* 199 */     this.in.setNorm(d, f, b);
/*     */   }
/*     */ 
/*     */   public TermEnum terms() throws IOException
/*     */   {
/* 204 */     ensureOpen();
/* 205 */     return this.in.terms();
/*     */   }
/*     */ 
/*     */   public TermEnum terms(Term t) throws IOException
/*     */   {
/* 210 */     ensureOpen();
/* 211 */     return this.in.terms(t);
/*     */   }
/*     */ 
/*     */   public int docFreq(Term t) throws IOException
/*     */   {
/* 216 */     ensureOpen();
/* 217 */     return this.in.docFreq(t);
/*     */   }
/*     */ 
/*     */   public TermDocs termDocs() throws IOException
/*     */   {
/* 222 */     ensureOpen();
/* 223 */     return this.in.termDocs();
/*     */   }
/*     */ 
/*     */   public TermDocs termDocs(Term term) throws IOException
/*     */   {
/* 228 */     ensureOpen();
/* 229 */     return this.in.termDocs(term);
/*     */   }
/*     */ 
/*     */   public TermPositions termPositions() throws IOException
/*     */   {
/* 234 */     ensureOpen();
/* 235 */     return this.in.termPositions();
/*     */   }
/*     */ 
/*     */   protected void doDelete(int n) throws CorruptIndexException, IOException {
/* 239 */     this.in.deleteDocument(n);
/*     */   }
/*     */   protected void doCommit(Map<String, String> commitUserData) throws IOException {
/* 242 */     this.in.commit(commitUserData);
/*     */   }
/*     */ 
/*     */   protected void doClose() throws IOException {
/* 246 */     this.in.close();
/*     */ 
/* 251 */     FieldCache.DEFAULT.purge(this);
/*     */   }
/*     */ 
/*     */   public Collection<String> getFieldNames(IndexReader.FieldOption fieldNames)
/*     */   {
/* 257 */     ensureOpen();
/* 258 */     return this.in.getFieldNames(fieldNames);
/*     */   }
/*     */ 
/*     */   public long getVersion()
/*     */   {
/* 263 */     ensureOpen();
/* 264 */     return this.in.getVersion();
/*     */   }
/*     */ 
/*     */   public boolean isCurrent() throws CorruptIndexException, IOException
/*     */   {
/* 269 */     ensureOpen();
/* 270 */     return this.in.isCurrent();
/*     */   }
/*     */ 
/*     */   public boolean isOptimized()
/*     */   {
/* 275 */     ensureOpen();
/* 276 */     return this.in.isOptimized();
/*     */   }
/*     */ 
/*     */   public IndexReader[] getSequentialSubReaders()
/*     */   {
/* 281 */     return this.in.getSequentialSubReaders();
/*     */   }
/*     */ 
/*     */   public Object getFieldCacheKey()
/*     */   {
/* 289 */     return this.in.getFieldCacheKey();
/*     */   }
/*     */ 
/*     */   public Object getDeletesCacheKey()
/*     */   {
/* 297 */     return this.in.getDeletesCacheKey();
/*     */   }
/*     */ 
/*     */   public static class FilterTermEnum extends TermEnum
/*     */   {
/*     */     protected TermEnum in;
/*     */ 
/*     */     public FilterTermEnum(TermEnum in)
/*     */     {
/*  87 */       this.in = in;
/*     */     }
/*     */     public boolean next() throws IOException {
/*  90 */       return this.in.next();
/*     */     }
/*  92 */     public Term term() { return this.in.term(); } 
/*     */     public int docFreq() {
/*  94 */       return this.in.docFreq();
/*     */     }
/*  96 */     public void close() throws IOException { this.in.close(); }
/*     */ 
/*     */   }
/*     */ 
/*     */   public static class FilterTermPositions extends FilterIndexReader.FilterTermDocs
/*     */     implements TermPositions
/*     */   {
/*     */     public FilterTermPositions(TermPositions in)
/*     */     {
/*  62 */       super();
/*     */     }
/*     */     public int nextPosition() throws IOException {
/*  65 */       return ((TermPositions)this.in).nextPosition();
/*     */     }
/*     */ 
/*     */     public int getPayloadLength() {
/*  69 */       return ((TermPositions)this.in).getPayloadLength();
/*     */     }
/*     */ 
/*     */     public byte[] getPayload(byte[] data, int offset) throws IOException {
/*  73 */       return ((TermPositions)this.in).getPayload(data, offset);
/*     */     }
/*     */ 
/*     */     public boolean isPayloadAvailable()
/*     */     {
/*  79 */       return ((TermPositions)this.in).isPayloadAvailable();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FilterTermDocs
/*     */     implements TermDocs
/*     */   {
/*     */     protected TermDocs in;
/*     */ 
/*     */     public FilterTermDocs(TermDocs in)
/*     */     {
/*  44 */       this.in = in;
/*     */     }
/*  46 */     public void seek(Term term) throws IOException { this.in.seek(term); } 
/*  47 */     public void seek(TermEnum termEnum) throws IOException { this.in.seek(termEnum); } 
/*  48 */     public int doc() { return this.in.doc(); } 
/*  49 */     public int freq() { return this.in.freq(); } 
/*  50 */     public boolean next() throws IOException { return this.in.next(); } 
/*     */     public int read(int[] docs, int[] freqs) throws IOException {
/*  52 */       return this.in.read(docs, freqs);
/*     */     }
/*  54 */     public boolean skipTo(int i) throws IOException { return this.in.skipTo(i); } 
/*  55 */     public void close() throws IOException { this.in.close(); }
/*     */ 
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FilterIndexReader
 * JD-Core Version:    0.6.2
 */