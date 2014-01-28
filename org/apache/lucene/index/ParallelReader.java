/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.lucene.document.Document;
/*     */ import org.apache.lucene.document.FieldSelector;
/*     */ import org.apache.lucene.document.FieldSelectorResult;
/*     */ import org.apache.lucene.document.Fieldable;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ 
/*     */ public class ParallelReader extends IndexReader
/*     */ {
/*  48 */   private List<IndexReader> readers = new ArrayList();
/*  49 */   private List<Boolean> decrefOnClose = new ArrayList();
/*  50 */   boolean incRefReaders = false;
/*  51 */   private SortedMap<String, IndexReader> fieldToReader = new TreeMap();
/*  52 */   private Map<IndexReader, Collection<String>> readerToFields = new HashMap();
/*  53 */   private List<IndexReader> storedFieldReaders = new ArrayList();
/*     */   private int maxDoc;
/*     */   private int numDocs;
/*     */   private boolean hasDeletions;
/*     */ 
/*     */   public ParallelReader()
/*     */     throws IOException
/*     */   {
/*  62 */     this(true);
/*     */   }
/*     */ 
/*     */   public ParallelReader(boolean closeSubReaders)
/*     */     throws IOException
/*     */   {
/*  70 */     this.incRefReaders = (!closeSubReaders);
/*     */   }
/*     */ 
/*     */   public void add(IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  77 */     ensureOpen();
/*  78 */     add(reader, false);
/*     */   }
/*     */ 
/*     */   public void add(IndexReader reader, boolean ignoreStoredFields)
/*     */     throws IOException
/*     */   {
/*  94 */     ensureOpen();
/*  95 */     if (this.readers.size() == 0) {
/*  96 */       this.maxDoc = reader.maxDoc();
/*  97 */       this.numDocs = reader.numDocs();
/*  98 */       this.hasDeletions = reader.hasDeletions();
/*     */     }
/*     */ 
/* 101 */     if (reader.maxDoc() != this.maxDoc) {
/* 102 */       throw new IllegalArgumentException("All readers must have same maxDoc: " + this.maxDoc + "!=" + reader.maxDoc());
/*     */     }
/* 104 */     if (reader.numDocs() != this.numDocs) {
/* 105 */       throw new IllegalArgumentException("All readers must have same numDocs: " + this.numDocs + "!=" + reader.numDocs());
/*     */     }
/*     */ 
/* 108 */     Collection fields = reader.getFieldNames(IndexReader.FieldOption.ALL);
/* 109 */     this.readerToFields.put(reader, fields);
/* 110 */     for (String field : fields) {
/* 111 */       if (this.fieldToReader.get(field) == null) {
/* 112 */         this.fieldToReader.put(field, reader);
/*     */       }
/*     */     }
/* 115 */     if (!ignoreStoredFields)
/* 116 */       this.storedFieldReaders.add(reader);
/* 117 */     this.readers.add(reader);
/*     */ 
/* 119 */     if (this.incRefReaders) {
/* 120 */       reader.incRef();
/*     */     }
/* 122 */     this.decrefOnClose.add(Boolean.valueOf(this.incRefReaders));
/*     */   }
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/*     */     try {
/* 128 */       return doReopen(true);
/*     */     } catch (Exception ex) {
/* 130 */       throw new RuntimeException(ex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized IndexReader reopen()
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 155 */     return doReopen(false);
/*     */   }
/*     */ 
/*     */   protected IndexReader doReopen(boolean doClone) throws CorruptIndexException, IOException {
/* 159 */     ensureOpen();
/*     */ 
/* 161 */     boolean reopened = false;
/* 162 */     List newReaders = new ArrayList();
/*     */ 
/* 164 */     boolean success = false;
/*     */     try
/*     */     {
/* 167 */       for (IndexReader oldReader : this.readers) {
/* 168 */         IndexReader newReader = null;
/* 169 */         if (doClone)
/* 170 */           newReader = (IndexReader)oldReader.clone();
/*     */         else {
/* 172 */           newReader = oldReader.reopen();
/*     */         }
/* 174 */         newReaders.add(newReader);
/*     */ 
/* 177 */         if (newReader != oldReader) {
/* 178 */           reopened = true;
/*     */         }
/*     */       }
/* 181 */       success = true;
/*     */     } finally {
/* 183 */       if ((!success) && (reopened)) {
/* 184 */         for (int i = 0; i < newReaders.size(); i++) {
/* 185 */           IndexReader r = (IndexReader)newReaders.get(i);
/* 186 */           if (r != this.readers.get(i)) {
/*     */             try {
/* 188 */               r.close();
/*     */             }
/*     */             catch (IOException ignore)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 197 */     if (reopened) {
/* 198 */       List newDecrefOnClose = new ArrayList();
/* 199 */       ParallelReader pr = new ParallelReader();
/* 200 */       for (int i = 0; i < this.readers.size(); i++) {
/* 201 */         IndexReader oldReader = (IndexReader)this.readers.get(i);
/* 202 */         IndexReader newReader = (IndexReader)newReaders.get(i);
/* 203 */         if (newReader == oldReader) {
/* 204 */           newDecrefOnClose.add(Boolean.TRUE);
/* 205 */           newReader.incRef();
/*     */         }
/*     */         else
/*     */         {
/* 209 */           newDecrefOnClose.add(Boolean.FALSE);
/*     */         }
/* 211 */         pr.add(newReader, !this.storedFieldReaders.contains(oldReader));
/*     */       }
/* 213 */       pr.decrefOnClose = newDecrefOnClose;
/* 214 */       pr.incRefReaders = this.incRefReaders;
/* 215 */       return pr;
/*     */     }
/*     */ 
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */   public int numDocs()
/*     */   {
/* 226 */     return this.numDocs;
/*     */   }
/*     */ 
/*     */   public int maxDoc()
/*     */   {
/* 232 */     return this.maxDoc;
/*     */   }
/*     */ 
/*     */   public boolean hasDeletions()
/*     */   {
/* 238 */     return this.hasDeletions;
/*     */   }
/*     */ 
/*     */   public boolean isDeleted(int n)
/*     */   {
/* 245 */     if (this.readers.size() > 0)
/* 246 */       return ((IndexReader)this.readers.get(0)).isDeleted(n);
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   protected void doDelete(int n)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 253 */     for (IndexReader reader : this.readers) {
/* 254 */       reader.deleteDocument(n);
/*     */     }
/* 256 */     this.hasDeletions = true;
/*     */   }
/*     */ 
/*     */   protected void doUndeleteAll()
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 262 */     for (IndexReader reader : this.readers) {
/* 263 */       reader.undeleteAll();
/*     */     }
/* 265 */     this.hasDeletions = false;
/*     */   }
/*     */ 
/*     */   public Document document(int n, FieldSelector fieldSelector)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 271 */     ensureOpen();
/* 272 */     Document result = new Document();
/* 273 */     for (IndexReader reader : this.storedFieldReaders)
/*     */     {
/* 275 */       boolean include = fieldSelector == null;
/* 276 */       if (!include) {
/* 277 */         Collection fields = (Collection)this.readerToFields.get(reader);
/* 278 */         for (String field : fields)
/* 279 */           if (fieldSelector.accept(field) != FieldSelectorResult.NO_LOAD) {
/* 280 */             include = true;
/* 281 */             break;
/*     */           }
/*     */       }
/* 284 */       if (include) {
/* 285 */         List fields = reader.document(n, fieldSelector).getFields();
/* 286 */         for (Fieldable field : fields) {
/* 287 */           result.add(field);
/*     */         }
/*     */       }
/*     */     }
/* 291 */     return result;
/*     */   }
/*     */ 
/*     */   public TermFreqVector[] getTermFreqVectors(int n)
/*     */     throws IOException
/*     */   {
/* 297 */     ensureOpen();
/* 298 */     ArrayList results = new ArrayList();
/* 299 */     for (Map.Entry e : this.fieldToReader.entrySet())
/*     */     {
/* 301 */       String field = (String)e.getKey();
/* 302 */       IndexReader reader = (IndexReader)e.getValue();
/* 303 */       TermFreqVector vector = reader.getTermFreqVector(n, field);
/* 304 */       if (vector != null)
/* 305 */         results.add(vector);
/*     */     }
/* 307 */     return (TermFreqVector[])results.toArray(new TermFreqVector[results.size()]);
/*     */   }
/*     */ 
/*     */   public TermFreqVector getTermFreqVector(int n, String field)
/*     */     throws IOException
/*     */   {
/* 313 */     ensureOpen();
/* 314 */     IndexReader reader = (IndexReader)this.fieldToReader.get(field);
/* 315 */     return reader == null ? null : reader.getTermFreqVector(n, field);
/*     */   }
/*     */ 
/*     */   public void getTermFreqVector(int docNumber, String field, TermVectorMapper mapper)
/*     */     throws IOException
/*     */   {
/* 321 */     ensureOpen();
/* 322 */     IndexReader reader = (IndexReader)this.fieldToReader.get(field);
/* 323 */     if (reader != null)
/* 324 */       reader.getTermFreqVector(docNumber, field, mapper);
/*     */   }
/*     */ 
/*     */   public void getTermFreqVector(int docNumber, TermVectorMapper mapper)
/*     */     throws IOException
/*     */   {
/* 330 */     ensureOpen();
/*     */ 
/* 332 */     for (Map.Entry e : this.fieldToReader.entrySet())
/*     */     {
/* 334 */       String field = (String)e.getKey();
/* 335 */       IndexReader reader = (IndexReader)e.getValue();
/* 336 */       reader.getTermFreqVector(docNumber, field, mapper);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasNorms(String field)
/*     */     throws IOException
/*     */   {
/* 343 */     ensureOpen();
/* 344 */     IndexReader reader = (IndexReader)this.fieldToReader.get(field);
/* 345 */     return reader == null ? false : reader.hasNorms(field);
/*     */   }
/*     */ 
/*     */   public byte[] norms(String field) throws IOException
/*     */   {
/* 350 */     ensureOpen();
/* 351 */     IndexReader reader = (IndexReader)this.fieldToReader.get(field);
/* 352 */     return reader == null ? null : reader.norms(field);
/*     */   }
/*     */ 
/*     */   public void norms(String field, byte[] result, int offset)
/*     */     throws IOException
/*     */   {
/* 358 */     ensureOpen();
/* 359 */     IndexReader reader = (IndexReader)this.fieldToReader.get(field);
/* 360 */     if (reader != null)
/* 361 */       reader.norms(field, result, offset);
/*     */   }
/*     */ 
/*     */   protected void doSetNorm(int n, String field, byte value)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 367 */     IndexReader reader = (IndexReader)this.fieldToReader.get(field);
/* 368 */     if (reader != null)
/* 369 */       reader.doSetNorm(n, field, value);
/*     */   }
/*     */ 
/*     */   public TermEnum terms() throws IOException
/*     */   {
/* 374 */     ensureOpen();
/* 375 */     return new ParallelTermEnum();
/*     */   }
/*     */ 
/*     */   public TermEnum terms(Term term) throws IOException
/*     */   {
/* 380 */     ensureOpen();
/* 381 */     return new ParallelTermEnum(term);
/*     */   }
/*     */ 
/*     */   public int docFreq(Term term) throws IOException
/*     */   {
/* 386 */     ensureOpen();
/* 387 */     IndexReader reader = (IndexReader)this.fieldToReader.get(term.field());
/* 388 */     return reader == null ? 0 : reader.docFreq(term);
/*     */   }
/*     */ 
/*     */   public TermDocs termDocs(Term term) throws IOException
/*     */   {
/* 393 */     ensureOpen();
/* 394 */     return new ParallelTermDocs(term);
/*     */   }
/*     */ 
/*     */   public TermDocs termDocs() throws IOException
/*     */   {
/* 399 */     ensureOpen();
/* 400 */     return new ParallelTermDocs();
/*     */   }
/*     */ 
/*     */   public TermPositions termPositions(Term term) throws IOException
/*     */   {
/* 405 */     ensureOpen();
/* 406 */     return new ParallelTermPositions(term);
/*     */   }
/*     */ 
/*     */   public TermPositions termPositions() throws IOException
/*     */   {
/* 411 */     ensureOpen();
/* 412 */     return new ParallelTermPositions();
/*     */   }
/*     */ 
/*     */   public boolean isCurrent()
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 420 */     for (IndexReader reader : this.readers) {
/* 421 */       if (!reader.isCurrent()) {
/* 422 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 427 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isOptimized()
/*     */   {
/* 435 */     for (IndexReader reader : this.readers) {
/* 436 */       if (!reader.isOptimized()) {
/* 437 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 442 */     return true;
/*     */   }
/*     */ 
/*     */   public long getVersion()
/*     */   {
/* 451 */     throw new UnsupportedOperationException("ParallelReader does not support this method.");
/*     */   }
/*     */ 
/*     */   IndexReader[] getSubReaders()
/*     */   {
/* 456 */     return (IndexReader[])this.readers.toArray(new IndexReader[this.readers.size()]);
/*     */   }
/*     */ 
/*     */   protected void doCommit(Map<String, String> commitUserData) throws IOException
/*     */   {
/* 461 */     for (IndexReader reader : this.readers)
/* 462 */       reader.commit(commitUserData);
/*     */   }
/*     */ 
/*     */   protected synchronized void doClose() throws IOException
/*     */   {
/* 467 */     for (int i = 0; i < this.readers.size(); i++) {
/* 468 */       if (((Boolean)this.decrefOnClose.get(i)).booleanValue())
/* 469 */         ((IndexReader)this.readers.get(i)).decRef();
/*     */       else {
/* 471 */         ((IndexReader)this.readers.get(i)).close();
/*     */       }
/*     */     }
/*     */ 
/* 475 */     FieldCache.DEFAULT.purge(this);
/*     */   }
/*     */ 
/*     */   public Collection<String> getFieldNames(IndexReader.FieldOption fieldNames)
/*     */   {
/* 480 */     ensureOpen();
/* 481 */     Set fieldSet = new HashSet();
/* 482 */     for (IndexReader reader : this.readers) {
/* 483 */       Collection names = reader.getFieldNames(fieldNames);
/* 484 */       fieldSet.addAll(names);
/*     */     }
/* 486 */     return fieldSet;
/*     */   }
/*     */ 
/*     */   private class ParallelTermPositions extends ParallelReader.ParallelTermDocs
/*     */     implements TermPositions
/*     */   {
/*     */     public ParallelTermPositions()
/*     */     {
/* 620 */       super(); } 
/* 621 */     public ParallelTermPositions(Term term) throws IOException { super(); seek(term); }
/*     */ 
/*     */     public void seek(Term term) throws IOException
/*     */     {
/* 625 */       IndexReader reader = (IndexReader)ParallelReader.this.fieldToReader.get(term.field());
/* 626 */       this.termDocs = (reader != null ? reader.termPositions(term) : null);
/*     */     }
/*     */ 
/*     */     public int nextPosition() throws IOException
/*     */     {
/* 631 */       return ((TermPositions)this.termDocs).nextPosition();
/*     */     }
/*     */ 
/*     */     public int getPayloadLength() {
/* 635 */       return ((TermPositions)this.termDocs).getPayloadLength();
/*     */     }
/*     */ 
/*     */     public byte[] getPayload(byte[] data, int offset) throws IOException {
/* 639 */       return ((TermPositions)this.termDocs).getPayload(data, offset);
/*     */     }
/*     */ 
/*     */     public boolean isPayloadAvailable()
/*     */     {
/* 645 */       return ((TermPositions)this.termDocs).isPayloadAvailable();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ParallelTermDocs
/*     */     implements TermDocs
/*     */   {
/*     */     protected TermDocs termDocs;
/*     */ 
/*     */     public ParallelTermDocs()
/*     */     {
/*     */     }
/*     */ 
/*     */     public ParallelTermDocs(Term term)
/*     */       throws IOException
/*     */     {
/* 571 */       if (term == null)
/* 572 */         this.termDocs = (ParallelReader.this.readers.isEmpty() ? null : ((IndexReader)ParallelReader.this.readers.get(0)).termDocs(null));
/*     */       else
/* 574 */         seek(term); 
/*     */     }
/*     */ 
/* 577 */     public int doc() { return this.termDocs.doc(); } 
/* 578 */     public int freq() { return this.termDocs.freq(); }
/*     */ 
/*     */     public void seek(Term term) throws IOException {
/* 581 */       IndexReader reader = (IndexReader)ParallelReader.this.fieldToReader.get(term.field());
/* 582 */       this.termDocs = (reader != null ? reader.termDocs(term) : null);
/*     */     }
/*     */ 
/*     */     public void seek(TermEnum termEnum) throws IOException {
/* 586 */       seek(termEnum.term());
/*     */     }
/*     */ 
/*     */     public boolean next() throws IOException {
/* 590 */       if (this.termDocs == null) {
/* 591 */         return false;
/*     */       }
/* 593 */       return this.termDocs.next();
/*     */     }
/*     */ 
/*     */     public int read(int[] docs, int[] freqs) throws IOException {
/* 597 */       if (this.termDocs == null) {
/* 598 */         return 0;
/*     */       }
/* 600 */       return this.termDocs.read(docs, freqs);
/*     */     }
/*     */ 
/*     */     public boolean skipTo(int target) throws IOException {
/* 604 */       if (this.termDocs == null) {
/* 605 */         return false;
/*     */       }
/* 607 */       return this.termDocs.skipTo(target);
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 611 */       if (this.termDocs != null)
/* 612 */         this.termDocs.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ParallelTermEnum extends TermEnum
/*     */   {
/*     */     private String field;
/*     */     private Iterator<String> fieldIterator;
/*     */     private TermEnum termEnum;
/*     */ 
/*     */     public ParallelTermEnum()
/*     */       throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 496 */         this.field = ((String)ParallelReader.this.fieldToReader.firstKey());
/*     */       }
/*     */       catch (NoSuchElementException e) {
/* 499 */         return;
/*     */       }
/* 501 */       if (this.field != null)
/* 502 */         this.termEnum = ((IndexReader)ParallelReader.this.fieldToReader.get(this.field)).terms();
/*     */     }
/*     */ 
/*     */     public ParallelTermEnum(Term term) throws IOException {
/* 506 */       this.field = term.field();
/* 507 */       IndexReader reader = (IndexReader)ParallelReader.this.fieldToReader.get(this.field);
/* 508 */       if (reader != null)
/* 509 */         this.termEnum = reader.terms(term);
/*     */     }
/*     */ 
/*     */     public boolean next() throws IOException
/*     */     {
/* 514 */       if (this.termEnum == null) {
/* 515 */         return false;
/*     */       }
/*     */ 
/* 518 */       if ((this.termEnum.next()) && (this.termEnum.term().field() == this.field)) {
/* 519 */         return true;
/*     */       }
/* 521 */       this.termEnum.close();
/*     */ 
/* 524 */       if (this.fieldIterator == null) {
/* 525 */         this.fieldIterator = ParallelReader.this.fieldToReader.tailMap(this.field).keySet().iterator();
/* 526 */         this.fieldIterator.next();
/*     */       }
/* 528 */       while (this.fieldIterator.hasNext()) {
/* 529 */         this.field = ((String)this.fieldIterator.next());
/* 530 */         this.termEnum = ((IndexReader)ParallelReader.this.fieldToReader.get(this.field)).terms(new Term(this.field));
/* 531 */         Term term = this.termEnum.term();
/* 532 */         if ((term != null) && (term.field() == this.field)) {
/* 533 */           return true;
/*     */         }
/* 535 */         this.termEnum.close();
/*     */       }
/*     */ 
/* 538 */       return false;
/*     */     }
/*     */ 
/*     */     public Term term()
/*     */     {
/* 543 */       if (this.termEnum == null) {
/* 544 */         return null;
/*     */       }
/* 546 */       return this.termEnum.term();
/*     */     }
/*     */ 
/*     */     public int docFreq()
/*     */     {
/* 551 */       if (this.termEnum == null) {
/* 552 */         return 0;
/*     */       }
/* 554 */       return this.termEnum.docFreq();
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 559 */       if (this.termEnum != null)
/* 560 */         this.termEnum.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.ParallelReader
 * JD-Core Version:    0.6.2
 */