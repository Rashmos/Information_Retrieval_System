/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.lucene.document.Document;
/*     */ import org.apache.lucene.document.FieldSelector;
/*     */ import org.apache.lucene.search.DefaultSimilarity;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ 
/*     */ public class MultiReader extends IndexReader
/*     */   implements Cloneable
/*     */ {
/*     */   protected IndexReader[] subReaders;
/*     */   private int[] starts;
/*     */   private boolean[] decrefOnClose;
/*  40 */   private Map<String, byte[]> normsCache = new HashMap();
/*  41 */   private int maxDoc = 0;
/*  42 */   private int numDocs = -1;
/*  43 */   private boolean hasDeletions = false;
/*     */ 
/*     */   public MultiReader(IndexReader[] subReaders)
/*     */   {
/*  54 */     initialize(subReaders, true);
/*     */   }
/*     */ 
/*     */   public MultiReader(IndexReader[] subReaders, boolean closeSubReaders)
/*     */   {
/*  67 */     initialize(subReaders, closeSubReaders);
/*     */   }
/*     */ 
/*     */   private void initialize(IndexReader[] subReaders, boolean closeSubReaders) {
/*  71 */     this.subReaders = ((IndexReader[])subReaders.clone());
/*  72 */     this.starts = new int[subReaders.length + 1];
/*  73 */     this.decrefOnClose = new boolean[subReaders.length];
/*  74 */     for (int i = 0; i < subReaders.length; i++) {
/*  75 */       this.starts[i] = this.maxDoc;
/*  76 */       this.maxDoc += subReaders[i].maxDoc();
/*     */ 
/*  78 */       if (!closeSubReaders) {
/*  79 */         subReaders[i].incRef();
/*  80 */         this.decrefOnClose[i] = true;
/*     */       } else {
/*  82 */         this.decrefOnClose[i] = false;
/*     */       }
/*     */ 
/*  85 */       if (subReaders[i].hasDeletions())
/*  86 */         this.hasDeletions = true;
/*     */     }
/*  88 */     this.starts[subReaders.length] = this.maxDoc;
/*     */   }
/*     */ 
/*     */   public synchronized IndexReader reopen()
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 112 */     return doReopen(false);
/*     */   }
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 127 */       return doReopen(true);
/*     */     } catch (Exception ex) {
/* 129 */       throw new RuntimeException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected IndexReader doReopen(boolean doClone)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 142 */     ensureOpen();
/*     */ 
/* 144 */     boolean reopened = false;
/* 145 */     IndexReader[] newSubReaders = new IndexReader[this.subReaders.length];
/*     */ 
/* 147 */     boolean success = false;
/*     */     try {
/* 149 */       for (int i = 0; i < this.subReaders.length; i++) {
/* 150 */         if (doClone)
/* 151 */           newSubReaders[i] = ((IndexReader)this.subReaders[i].clone());
/*     */         else {
/* 153 */           newSubReaders[i] = this.subReaders[i].reopen();
/*     */         }
/*     */ 
/* 156 */         if (newSubReaders[i] != this.subReaders[i]) {
/* 157 */           reopened = true;
/*     */         }
/*     */       }
/* 160 */       success = true;
/*     */     } finally {
/* 162 */       if ((!success) && (reopened)) {
/* 163 */         for (int i = 0; i < newSubReaders.length; i++) {
/* 164 */           if (newSubReaders[i] != this.subReaders[i]) {
/*     */             try {
/* 166 */               newSubReaders[i].close();
/*     */             }
/*     */             catch (IOException ignore)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 175 */     if (reopened) {
/* 176 */       boolean[] newDecrefOnClose = new boolean[this.subReaders.length];
/* 177 */       for (int i = 0; i < this.subReaders.length; i++) {
/* 178 */         if (newSubReaders[i] == this.subReaders[i]) {
/* 179 */           newSubReaders[i].incRef();
/* 180 */           newDecrefOnClose[i] = true;
/*     */         }
/*     */       }
/* 183 */       MultiReader mr = new MultiReader(newSubReaders);
/* 184 */       mr.decrefOnClose = newDecrefOnClose;
/* 185 */       return mr;
/*     */     }
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   public TermFreqVector[] getTermFreqVectors(int n)
/*     */     throws IOException
/*     */   {
/* 193 */     ensureOpen();
/* 194 */     int i = readerIndex(n);
/* 195 */     return this.subReaders[i].getTermFreqVectors(n - this.starts[i]);
/*     */   }
/*     */ 
/*     */   public TermFreqVector getTermFreqVector(int n, String field)
/*     */     throws IOException
/*     */   {
/* 201 */     ensureOpen();
/* 202 */     int i = readerIndex(n);
/* 203 */     return this.subReaders[i].getTermFreqVector(n - this.starts[i], field);
/*     */   }
/*     */ 
/*     */   public void getTermFreqVector(int docNumber, String field, TermVectorMapper mapper)
/*     */     throws IOException
/*     */   {
/* 209 */     ensureOpen();
/* 210 */     int i = readerIndex(docNumber);
/* 211 */     this.subReaders[i].getTermFreqVector(docNumber - this.starts[i], field, mapper);
/*     */   }
/*     */ 
/*     */   public void getTermFreqVector(int docNumber, TermVectorMapper mapper) throws IOException
/*     */   {
/* 216 */     ensureOpen();
/* 217 */     int i = readerIndex(docNumber);
/* 218 */     this.subReaders[i].getTermFreqVector(docNumber - this.starts[i], mapper);
/*     */   }
/*     */ 
/*     */   public boolean isOptimized()
/*     */   {
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   public int numDocs()
/*     */   {
/* 231 */     if (this.numDocs == -1) {
/* 232 */       int n = 0;
/* 233 */       for (int i = 0; i < this.subReaders.length; i++)
/* 234 */         n += this.subReaders[i].numDocs();
/* 235 */       this.numDocs = n;
/*     */     }
/* 237 */     return this.numDocs;
/*     */   }
/*     */ 
/*     */   public int maxDoc()
/*     */   {
/* 243 */     return this.maxDoc;
/*     */   }
/*     */ 
/*     */   public Document document(int n, FieldSelector fieldSelector)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 249 */     ensureOpen();
/* 250 */     int i = readerIndex(n);
/* 251 */     return this.subReaders[i].document(n - this.starts[i], fieldSelector);
/*     */   }
/*     */ 
/*     */   public boolean isDeleted(int n)
/*     */   {
/* 257 */     int i = readerIndex(n);
/* 258 */     return this.subReaders[i].isDeleted(n - this.starts[i]);
/*     */   }
/*     */ 
/*     */   public boolean hasDeletions()
/*     */   {
/* 264 */     return this.hasDeletions;
/*     */   }
/*     */ 
/*     */   protected void doDelete(int n) throws CorruptIndexException, IOException
/*     */   {
/* 269 */     this.numDocs = -1;
/* 270 */     int i = readerIndex(n);
/* 271 */     this.subReaders[i].deleteDocument(n - this.starts[i]);
/* 272 */     this.hasDeletions = true;
/*     */   }
/*     */ 
/*     */   protected void doUndeleteAll() throws CorruptIndexException, IOException
/*     */   {
/* 277 */     for (int i = 0; i < this.subReaders.length; i++) {
/* 278 */       this.subReaders[i].undeleteAll();
/*     */     }
/* 280 */     this.hasDeletions = false;
/* 281 */     this.numDocs = -1;
/*     */   }
/*     */ 
/*     */   private int readerIndex(int n) {
/* 285 */     return DirectoryReader.readerIndex(n, this.starts, this.subReaders.length);
/*     */   }
/*     */ 
/*     */   public boolean hasNorms(String field) throws IOException
/*     */   {
/* 290 */     ensureOpen();
/* 291 */     for (int i = 0; i < this.subReaders.length; i++) {
/* 292 */       if (this.subReaders[i].hasNorms(field)) return true;
/*     */     }
/* 294 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized byte[] norms(String field) throws IOException
/*     */   {
/* 299 */     ensureOpen();
/* 300 */     byte[] bytes = (byte[])this.normsCache.get(field);
/* 301 */     if (bytes != null)
/* 302 */       return bytes;
/* 303 */     if (!hasNorms(field)) {
/* 304 */       return null;
/*     */     }
/* 306 */     bytes = new byte[maxDoc()];
/* 307 */     for (int i = 0; i < this.subReaders.length; i++)
/* 308 */       this.subReaders[i].norms(field, bytes, this.starts[i]);
/* 309 */     this.normsCache.put(field, bytes);
/* 310 */     return bytes;
/*     */   }
/*     */ 
/*     */   public synchronized void norms(String field, byte[] result, int offset)
/*     */     throws IOException
/*     */   {
/* 316 */     ensureOpen();
/* 317 */     byte[] bytes = (byte[])this.normsCache.get(field);
/* 318 */     for (int i = 0; i < this.subReaders.length; i++) {
/* 319 */       this.subReaders[i].norms(field, result, offset + this.starts[i]);
/*     */     }
/* 321 */     if ((bytes == null) && (!hasNorms(field)))
/* 322 */       Arrays.fill(result, offset, result.length, DefaultSimilarity.encodeNorm(1.0F));
/* 323 */     else if (bytes != null)
/* 324 */       System.arraycopy(bytes, 0, result, offset, maxDoc());
/*     */     else
/* 326 */       for (int i = 0; i < this.subReaders.length; i++)
/* 327 */         this.subReaders[i].norms(field, result, offset + this.starts[i]);
/*     */   }
/*     */ 
/*     */   protected void doSetNorm(int n, String field, byte value)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 335 */     synchronized (this.normsCache) {
/* 336 */       this.normsCache.remove(field);
/*     */     }
/* 338 */     int i = readerIndex(n);
/* 339 */     this.subReaders[i].setNorm(n - this.starts[i], field, value);
/*     */   }
/*     */ 
/*     */   public TermEnum terms() throws IOException
/*     */   {
/* 344 */     ensureOpen();
/* 345 */     return new DirectoryReader.MultiTermEnum(this, this.subReaders, this.starts, null);
/*     */   }
/*     */ 
/*     */   public TermEnum terms(Term term) throws IOException
/*     */   {
/* 350 */     ensureOpen();
/* 351 */     return new DirectoryReader.MultiTermEnum(this, this.subReaders, this.starts, term);
/*     */   }
/*     */ 
/*     */   public int docFreq(Term t) throws IOException
/*     */   {
/* 356 */     ensureOpen();
/* 357 */     int total = 0;
/* 358 */     for (int i = 0; i < this.subReaders.length; i++)
/* 359 */       total += this.subReaders[i].docFreq(t);
/* 360 */     return total;
/*     */   }
/*     */ 
/*     */   public TermDocs termDocs() throws IOException
/*     */   {
/* 365 */     ensureOpen();
/* 366 */     return new DirectoryReader.MultiTermDocs(this, this.subReaders, this.starts);
/*     */   }
/*     */ 
/*     */   public TermPositions termPositions() throws IOException
/*     */   {
/* 371 */     ensureOpen();
/* 372 */     return new DirectoryReader.MultiTermPositions(this, this.subReaders, this.starts);
/*     */   }
/*     */ 
/*     */   protected void doCommit(Map<String, String> commitUserData) throws IOException
/*     */   {
/* 377 */     for (int i = 0; i < this.subReaders.length; i++)
/* 378 */       this.subReaders[i].commit(commitUserData);
/*     */   }
/*     */ 
/*     */   protected synchronized void doClose() throws IOException
/*     */   {
/* 383 */     for (int i = 0; i < this.subReaders.length; i++) {
/* 384 */       if (this.decrefOnClose[i] != 0)
/* 385 */         this.subReaders[i].decRef();
/*     */       else {
/* 387 */         this.subReaders[i].close();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 394 */     FieldCache.DEFAULT.purge(this);
/*     */   }
/*     */ 
/*     */   public Collection<String> getFieldNames(IndexReader.FieldOption fieldNames)
/*     */   {
/* 399 */     ensureOpen();
/* 400 */     return DirectoryReader.getFieldNames(fieldNames, this.subReaders);
/*     */   }
/*     */ 
/*     */   public boolean isCurrent()
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 408 */     for (int i = 0; i < this.subReaders.length; i++) {
/* 409 */       if (!this.subReaders[i].isCurrent()) {
/* 410 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 415 */     return true;
/*     */   }
/*     */ 
/*     */   public long getVersion()
/*     */   {
/* 423 */     throw new UnsupportedOperationException("MultiReader does not support this method.");
/*     */   }
/*     */ 
/*     */   public IndexReader[] getSequentialSubReaders()
/*     */   {
/* 428 */     return this.subReaders;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.MultiReader
 * JD-Core Version:    0.6.2
 */