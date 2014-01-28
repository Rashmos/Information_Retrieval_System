/*      */ package org.apache.lucene.index;
/*      */ 
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import org.apache.lucene.document.Document;
/*      */ import org.apache.lucene.document.FieldSelector;
/*      */ import org.apache.lucene.search.DefaultSimilarity;
/*      */ import org.apache.lucene.search.FieldCache;
/*      */ import org.apache.lucene.store.Directory;
/*      */ import org.apache.lucene.store.Lock;
/*      */ import org.apache.lucene.store.LockObtainFailedException;
/*      */ 
/*      */ class DirectoryReader extends IndexReader
/*      */   implements Cloneable
/*      */ {
/*      */   protected Directory directory;
/*      */   protected boolean readOnly;
/*      */   IndexWriter writer;
/*      */   private IndexDeletionPolicy deletionPolicy;
/*   50 */   private final HashSet<String> synced = new HashSet();
/*      */   private Lock writeLock;
/*      */   private SegmentInfos segmentInfos;
/*      */   private SegmentInfos segmentInfosStart;
/*      */   private boolean stale;
/*      */   private final int termInfosIndexDivisor;
/*      */   private boolean rollbackHasChanges;
/*      */   private SegmentInfos rollbackSegmentInfos;
/*      */   private SegmentReader[] subReaders;
/*      */   private int[] starts;
/*   62 */   private Map<String, byte[]> normsCache = new HashMap();
/*   63 */   private int maxDoc = 0;
/*   64 */   private int numDocs = -1;
/*   65 */   private boolean hasDeletions = false;
/*      */ 
/*      */   static IndexReader open(Directory directory, final IndexDeletionPolicy deletionPolicy, IndexCommit commit, final boolean readOnly, final int termInfosIndexDivisor) throws CorruptIndexException, IOException
/*      */   {
/*   69 */     return (IndexReader)new SegmentInfos.FindSegmentsFile(directory)
/*      */     {
/*      */       protected Object doBody(String segmentFileName) throws CorruptIndexException, IOException {
/*   72 */         SegmentInfos infos = new SegmentInfos();
/*   73 */         infos.read(this.directory, segmentFileName);
/*   74 */         if (readOnly) {
/*   75 */           return new ReadOnlyDirectoryReader(this.directory, infos, deletionPolicy, termInfosIndexDivisor);
/*      */         }
/*   77 */         return new DirectoryReader(this.directory, infos, deletionPolicy, false, termInfosIndexDivisor);
/*      */       }
/*      */     }
/*   69 */     .run(commit);
/*      */   }
/*      */ 
/*      */   DirectoryReader(Directory directory, SegmentInfos sis, IndexDeletionPolicy deletionPolicy, boolean readOnly, int termInfosIndexDivisor)
/*      */     throws IOException
/*      */   {
/*   84 */     this.directory = directory;
/*   85 */     this.readOnly = readOnly;
/*   86 */     this.segmentInfos = sis;
/*   87 */     this.deletionPolicy = deletionPolicy;
/*   88 */     this.termInfosIndexDivisor = termInfosIndexDivisor;
/*      */ 
/*   90 */     if (!readOnly)
/*      */     {
/*   93 */       this.synced.addAll(sis.files(directory, true));
/*      */     }
/*      */ 
/*  101 */     SegmentReader[] readers = new SegmentReader[sis.size()];
/*  102 */     for (int i = sis.size() - 1; i >= 0; i--) {
/*  103 */       boolean success = false;
/*      */       try {
/*  105 */         readers[i] = SegmentReader.get(readOnly, sis.info(i), termInfosIndexDivisor);
/*  106 */         success = true;
/*      */       } finally {
/*  108 */         if (!success)
/*      */         {
/*  110 */           for (i++; i < sis.size(); i++) {
/*      */             try {
/*  112 */               readers[i].close();
/*      */             }
/*      */             catch (Throwable ignore)
/*      */             {
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  121 */     initialize(readers);
/*      */   }
/*      */ 
/*      */   DirectoryReader(IndexWriter writer, SegmentInfos infos, int termInfosIndexDivisor) throws IOException
/*      */   {
/*  126 */     this.directory = writer.getDirectory();
/*  127 */     this.readOnly = true;
/*  128 */     this.segmentInfos = infos;
/*  129 */     this.segmentInfosStart = ((SegmentInfos)infos.clone());
/*  130 */     this.termInfosIndexDivisor = termInfosIndexDivisor;
/*  131 */     if (!this.readOnly)
/*      */     {
/*  134 */       this.synced.addAll(infos.files(this.directory, true));
/*      */     }
/*      */ 
/*  140 */     int numSegments = infos.size();
/*  141 */     SegmentReader[] readers = new SegmentReader[numSegments];
/*  142 */     Directory dir = writer.getDirectory();
/*  143 */     int upto = 0;
/*      */ 
/*  145 */     for (int i = 0; i < numSegments; i++) {
/*  146 */       boolean success = false;
/*      */       try {
/*  148 */         SegmentInfo info = infos.info(i);
/*  149 */         if (info.dir == dir) {
/*  150 */           readers[(upto++)] = writer.readerPool.getReadOnlyClone(info, true, termInfosIndexDivisor);
/*      */         }
/*  152 */         success = true;
/*      */       } finally {
/*  154 */         if (!success)
/*      */         {
/*  156 */           for (upto--; upto >= 0; upto--) {
/*      */             try {
/*  158 */               readers[upto].close();
/*      */             }
/*      */             catch (Throwable ignore)
/*      */             {
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  167 */     this.writer = writer;
/*      */ 
/*  169 */     if (upto < readers.length)
/*      */     {
/*  171 */       SegmentReader[] newReaders = new SegmentReader[upto];
/*  172 */       System.arraycopy(readers, 0, newReaders, 0, upto);
/*  173 */       readers = newReaders;
/*      */     }
/*      */ 
/*  176 */     initialize(readers);
/*      */   }
/*      */ 
/*      */   DirectoryReader(Directory directory, SegmentInfos infos, SegmentReader[] oldReaders, int[] oldStarts, Map<String, byte[]> oldNormsCache, boolean readOnly, boolean doClone, int termInfosIndexDivisor)
/*      */     throws IOException
/*      */   {
/*  182 */     this.directory = directory;
/*  183 */     this.readOnly = readOnly;
/*  184 */     this.segmentInfos = infos;
/*  185 */     this.termInfosIndexDivisor = termInfosIndexDivisor;
/*  186 */     if (!readOnly)
/*      */     {
/*  189 */       this.synced.addAll(infos.files(directory, true));
/*      */     }
/*      */ 
/*  194 */     Map segmentReaders = new HashMap();
/*      */ 
/*  196 */     if (oldReaders != null)
/*      */     {
/*  198 */       for (int i = 0; i < oldReaders.length; i++) {
/*  199 */         segmentReaders.put(oldReaders[i].getSegmentName(), Integer.valueOf(i));
/*      */       }
/*      */     }
/*      */ 
/*  203 */     SegmentReader[] newReaders = new SegmentReader[infos.size()];
/*      */ 
/*  207 */     boolean[] readerShared = new boolean[infos.size()];
/*      */ 
/*  209 */     for (int i = infos.size() - 1; i >= 0; i--)
/*      */     {
/*  211 */       Integer oldReaderIndex = (Integer)segmentReaders.get(infos.info(i).name);
/*  212 */       if (oldReaderIndex == null)
/*      */       {
/*  214 */         newReaders[i] = null;
/*      */       }
/*      */       else {
/*  217 */         newReaders[i] = oldReaders[oldReaderIndex.intValue()];
/*      */       }
/*      */ 
/*  220 */       boolean success = false;
/*      */       try
/*      */       {
/*      */         SegmentReader newReader;
/*      */         SegmentReader newReader;
/*  223 */         if ((newReaders[i] == null) || (infos.info(i).getUseCompoundFile() != newReaders[i].getSegmentInfo().getUseCompoundFile()))
/*      */         {
/*  226 */           assert (!doClone);
/*      */ 
/*  229 */           newReader = SegmentReader.get(readOnly, infos.info(i), termInfosIndexDivisor);
/*      */         } else {
/*  231 */           newReader = newReaders[i].reopenSegment(infos.info(i), doClone, readOnly);
/*      */         }
/*  233 */         if (newReader == newReaders[i])
/*      */         {
/*  236 */           readerShared[i] = true;
/*  237 */           newReader.incRef();
/*      */         } else {
/*  239 */           readerShared[i] = false;
/*  240 */           newReaders[i] = newReader;
/*      */         }
/*  242 */         success = true;
/*      */       } finally {
/*  244 */         if (!success) {
/*  245 */           for (i++; i < infos.size(); i++) {
/*  246 */             if (newReaders[i] != null) {
/*      */               try {
/*  248 */                 if (readerShared[i] == 0)
/*      */                 {
/*  251 */                   newReaders[i].close();
/*      */                 }
/*      */                 else
/*      */                 {
/*  255 */                   newReaders[i].decRef();
/*      */                 }
/*      */               }
/*      */               catch (IOException ignore)
/*      */               {
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  267 */     initialize(newReaders);
/*      */ 
/*  270 */     if (oldNormsCache != null)
/*  271 */       for (Map.Entry entry : oldNormsCache.entrySet()) {
/*  272 */         String field = (String)entry.getKey();
/*  273 */         if (hasNorms(field))
/*      */         {
/*  277 */           byte[] oldBytes = (byte[])entry.getValue();
/*      */ 
/*  279 */           byte[] bytes = new byte[maxDoc()];
/*      */ 
/*  281 */           for (int i = 0; i < this.subReaders.length; i++) {
/*  282 */             Integer oldReaderIndex = (Integer)segmentReaders.get(this.subReaders[i].getSegmentName());
/*      */ 
/*  285 */             if ((oldReaderIndex != null) && ((oldReaders[oldReaderIndex.intValue()] == this.subReaders[i]) || (oldReaders[oldReaderIndex.intValue()].norms.get(field) == this.subReaders[i].norms.get(field))))
/*      */             {
/*  291 */               System.arraycopy(oldBytes, oldStarts[oldReaderIndex.intValue()], bytes, this.starts[i], this.starts[(i + 1)] - this.starts[i]);
/*      */             }
/*  293 */             else this.subReaders[i].norms(field, bytes, this.starts[i]);
/*      */ 
/*      */           }
/*      */ 
/*  297 */           this.normsCache.put(field, bytes);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void initialize(SegmentReader[] subReaders) {
/*  303 */     this.subReaders = subReaders;
/*  304 */     this.starts = new int[subReaders.length + 1];
/*  305 */     for (int i = 0; i < subReaders.length; i++) {
/*  306 */       this.starts[i] = this.maxDoc;
/*  307 */       this.maxDoc += subReaders[i].maxDoc();
/*      */ 
/*  309 */       if (subReaders[i].hasDeletions())
/*  310 */         this.hasDeletions = true;
/*      */     }
/*  312 */     this.starts[subReaders.length] = this.maxDoc;
/*      */   }
/*      */ 
/*      */   public final synchronized Object clone()
/*      */   {
/*      */     try {
/*  318 */       return clone(this.readOnly);
/*      */     } catch (Exception ex) {
/*  320 */       throw new RuntimeException(ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final synchronized IndexReader clone(boolean openReadOnly) throws CorruptIndexException, IOException
/*      */   {
/*  326 */     DirectoryReader newReader = doReopen((SegmentInfos)this.segmentInfos.clone(), true, openReadOnly);
/*      */ 
/*  328 */     if (this != newReader) {
/*  329 */       newReader.deletionPolicy = this.deletionPolicy;
/*      */     }
/*  331 */     newReader.writer = this.writer;
/*      */ 
/*  334 */     if ((!openReadOnly) && (this.writeLock != null))
/*      */     {
/*  336 */       assert (this.writer == null);
/*  337 */       newReader.writeLock = this.writeLock;
/*  338 */       newReader.hasChanges = this.hasChanges;
/*  339 */       newReader.hasDeletions = this.hasDeletions;
/*  340 */       this.writeLock = null;
/*  341 */       this.hasChanges = false;
/*      */     }
/*      */ 
/*  344 */     return newReader;
/*      */   }
/*      */ 
/*      */   public final IndexReader reopen()
/*      */     throws CorruptIndexException, IOException
/*      */   {
/*  350 */     return doReopen(this.readOnly, null);
/*      */   }
/*      */ 
/*      */   public final IndexReader reopen(boolean openReadOnly) throws CorruptIndexException, IOException
/*      */   {
/*  355 */     return doReopen(openReadOnly, null);
/*      */   }
/*      */ 
/*      */   public final IndexReader reopen(IndexCommit commit) throws CorruptIndexException, IOException
/*      */   {
/*  360 */     return doReopen(true, commit);
/*      */   }
/*      */ 
/*      */   private final IndexReader doReopenFromWriter(boolean openReadOnly, IndexCommit commit) throws CorruptIndexException, IOException {
/*  364 */     assert (this.readOnly);
/*      */ 
/*  366 */     if (!openReadOnly) {
/*  367 */       throw new IllegalArgumentException("a reader obtained from IndexWriter.getReader() can only be reopened with openReadOnly=true (got false)");
/*      */     }
/*      */ 
/*  370 */     if (commit != null) {
/*  371 */       throw new IllegalArgumentException("a reader obtained from IndexWriter.getReader() cannot currently accept a commit");
/*      */     }
/*      */ 
/*  377 */     return this.writer.getReader();
/*      */   }
/*      */ 
/*      */   private IndexReader doReopen(boolean openReadOnly, IndexCommit commit) throws CorruptIndexException, IOException {
/*  381 */     ensureOpen();
/*      */ 
/*  383 */     assert ((commit == null) || (openReadOnly));
/*      */ 
/*  387 */     if (this.writer != null) {
/*  388 */       return doReopenFromWriter(openReadOnly, commit);
/*      */     }
/*  390 */     return doReopenNoWriter(openReadOnly, commit);
/*      */   }
/*      */ 
/*      */   private synchronized IndexReader doReopenNoWriter(final boolean openReadOnly, IndexCommit commit)
/*      */     throws CorruptIndexException, IOException
/*      */   {
/*  396 */     if (commit == null) {
/*  397 */       if (this.hasChanges)
/*      */       {
/*  399 */         assert (!this.readOnly);
/*      */ 
/*  401 */         assert (this.writeLock != null);
/*      */ 
/*  404 */         assert (isCurrent());
/*      */ 
/*  406 */         if (openReadOnly) {
/*  407 */           return clone(openReadOnly);
/*      */         }
/*  409 */         return this;
/*      */       }
/*  411 */       if (isCurrent()) {
/*  412 */         if (openReadOnly != this.readOnly)
/*      */         {
/*  414 */           return clone(openReadOnly);
/*      */         }
/*  416 */         return this;
/*      */       }
/*      */     }
/*      */     else {
/*  420 */       if (this.directory != commit.getDirectory())
/*  421 */         throw new IOException("the specified commit does not match the specified Directory");
/*  422 */       if ((this.segmentInfos != null) && (commit.getSegmentsFileName().equals(this.segmentInfos.getCurrentSegmentFileName()))) {
/*  423 */         if (this.readOnly != openReadOnly)
/*      */         {
/*  425 */           return clone(openReadOnly);
/*      */         }
/*  427 */         return this;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  432 */     return (IndexReader)new SegmentInfos.FindSegmentsFile(this.directory)
/*      */     {
/*      */       protected Object doBody(String segmentFileName) throws CorruptIndexException, IOException {
/*  435 */         SegmentInfos infos = new SegmentInfos();
/*  436 */         infos.read(this.directory, segmentFileName);
/*  437 */         return DirectoryReader.this.doReopen(infos, false, openReadOnly);
/*      */       }
/*      */     }
/*  432 */     .run(commit);
/*      */   }
/*      */ 
/*      */   private synchronized DirectoryReader doReopen(SegmentInfos infos, boolean doClone, boolean openReadOnly)
/*      */     throws CorruptIndexException, IOException
/*      */   {
/*      */     DirectoryReader reader;
/*      */     DirectoryReader reader;
/*  444 */     if (openReadOnly)
/*  445 */       reader = new ReadOnlyDirectoryReader(this.directory, infos, this.subReaders, this.starts, this.normsCache, doClone, this.termInfosIndexDivisor);
/*      */     else {
/*  447 */       reader = new DirectoryReader(this.directory, infos, this.subReaders, this.starts, this.normsCache, false, doClone, this.termInfosIndexDivisor);
/*      */     }
/*  449 */     return reader;
/*      */   }
/*      */ 
/*      */   public long getVersion()
/*      */   {
/*  455 */     ensureOpen();
/*  456 */     return this.segmentInfos.getVersion();
/*      */   }
/*      */ 
/*      */   public TermFreqVector[] getTermFreqVectors(int n) throws IOException
/*      */   {
/*  461 */     ensureOpen();
/*  462 */     int i = readerIndex(n);
/*  463 */     return this.subReaders[i].getTermFreqVectors(n - this.starts[i]);
/*      */   }
/*      */ 
/*      */   public TermFreqVector getTermFreqVector(int n, String field)
/*      */     throws IOException
/*      */   {
/*  469 */     ensureOpen();
/*  470 */     int i = readerIndex(n);
/*  471 */     return this.subReaders[i].getTermFreqVector(n - this.starts[i], field);
/*      */   }
/*      */ 
/*      */   public void getTermFreqVector(int docNumber, String field, TermVectorMapper mapper)
/*      */     throws IOException
/*      */   {
/*  477 */     ensureOpen();
/*  478 */     int i = readerIndex(docNumber);
/*  479 */     this.subReaders[i].getTermFreqVector(docNumber - this.starts[i], field, mapper);
/*      */   }
/*      */ 
/*      */   public void getTermFreqVector(int docNumber, TermVectorMapper mapper) throws IOException
/*      */   {
/*  484 */     ensureOpen();
/*  485 */     int i = readerIndex(docNumber);
/*  486 */     this.subReaders[i].getTermFreqVector(docNumber - this.starts[i], mapper);
/*      */   }
/*      */ 
/*      */   public boolean isOptimized()
/*      */   {
/*  495 */     ensureOpen();
/*  496 */     return (this.segmentInfos.size() == 1) && (!hasDeletions());
/*      */   }
/*      */ 
/*      */   public int numDocs()
/*      */   {
/*  505 */     if (this.numDocs == -1) {
/*  506 */       int n = 0;
/*  507 */       for (int i = 0; i < this.subReaders.length; i++)
/*  508 */         n += this.subReaders[i].numDocs();
/*  509 */       this.numDocs = n;
/*      */     }
/*  511 */     return this.numDocs;
/*      */   }
/*      */ 
/*      */   public int maxDoc()
/*      */   {
/*  517 */     return this.maxDoc;
/*      */   }
/*      */ 
/*      */   public Document document(int n, FieldSelector fieldSelector)
/*      */     throws CorruptIndexException, IOException
/*      */   {
/*  523 */     ensureOpen();
/*  524 */     int i = readerIndex(n);
/*  525 */     return this.subReaders[i].document(n - this.starts[i], fieldSelector);
/*      */   }
/*      */ 
/*      */   public boolean isDeleted(int n)
/*      */   {
/*  531 */     int i = readerIndex(n);
/*  532 */     return this.subReaders[i].isDeleted(n - this.starts[i]);
/*      */   }
/*      */ 
/*      */   public boolean hasDeletions()
/*      */   {
/*  538 */     return this.hasDeletions;
/*      */   }
/*      */ 
/*      */   protected void doDelete(int n) throws CorruptIndexException, IOException
/*      */   {
/*  543 */     this.numDocs = -1;
/*  544 */     int i = readerIndex(n);
/*  545 */     this.subReaders[i].deleteDocument(n - this.starts[i]);
/*  546 */     this.hasDeletions = true;
/*      */   }
/*      */ 
/*      */   protected void doUndeleteAll() throws CorruptIndexException, IOException
/*      */   {
/*  551 */     for (int i = 0; i < this.subReaders.length; i++) {
/*  552 */       this.subReaders[i].undeleteAll();
/*      */     }
/*  554 */     this.hasDeletions = false;
/*  555 */     this.numDocs = -1;
/*      */   }
/*      */ 
/*      */   private int readerIndex(int n) {
/*  559 */     return readerIndex(n, this.starts, this.subReaders.length);
/*      */   }
/*      */ 
/*      */   static final int readerIndex(int n, int[] starts, int numSubReaders) {
/*  563 */     int lo = 0;
/*  564 */     int hi = numSubReaders - 1;
/*      */ 
/*  566 */     while (hi >= lo) {
/*  567 */       int mid = lo + hi >>> 1;
/*  568 */       int midValue = starts[mid];
/*  569 */       if (n < midValue) {
/*  570 */         hi = mid - 1;
/*  571 */       } else if (n > midValue) {
/*  572 */         lo = mid + 1;
/*      */       } else {
/*  574 */         while ((mid + 1 < numSubReaders) && (starts[(mid + 1)] == midValue)) {
/*  575 */           mid++;
/*      */         }
/*  577 */         return mid;
/*      */       }
/*      */     }
/*  580 */     return hi;
/*      */   }
/*      */ 
/*      */   public boolean hasNorms(String field) throws IOException
/*      */   {
/*  585 */     ensureOpen();
/*  586 */     for (int i = 0; i < this.subReaders.length; i++) {
/*  587 */       if (this.subReaders[i].hasNorms(field)) return true;
/*      */     }
/*  589 */     return false;
/*      */   }
/*      */ 
/*      */   public synchronized byte[] norms(String field) throws IOException
/*      */   {
/*  594 */     ensureOpen();
/*  595 */     byte[] bytes = (byte[])this.normsCache.get(field);
/*  596 */     if (bytes != null)
/*  597 */       return bytes;
/*  598 */     if (!hasNorms(field)) {
/*  599 */       return null;
/*      */     }
/*  601 */     bytes = new byte[maxDoc()];
/*  602 */     for (int i = 0; i < this.subReaders.length; i++)
/*  603 */       this.subReaders[i].norms(field, bytes, this.starts[i]);
/*  604 */     this.normsCache.put(field, bytes);
/*  605 */     return bytes;
/*      */   }
/*      */ 
/*      */   public synchronized void norms(String field, byte[] result, int offset)
/*      */     throws IOException
/*      */   {
/*  611 */     ensureOpen();
/*  612 */     byte[] bytes = (byte[])this.normsCache.get(field);
/*  613 */     if ((bytes == null) && (!hasNorms(field)))
/*  614 */       Arrays.fill(result, offset, result.length, DefaultSimilarity.encodeNorm(1.0F));
/*  615 */     else if (bytes != null)
/*  616 */       System.arraycopy(bytes, 0, result, offset, maxDoc());
/*      */     else
/*  618 */       for (int i = 0; i < this.subReaders.length; i++)
/*  619 */         this.subReaders[i].norms(field, result, offset + this.starts[i]);
/*      */   }
/*      */ 
/*      */   protected void doSetNorm(int n, String field, byte value)
/*      */     throws CorruptIndexException, IOException
/*      */   {
/*  627 */     synchronized (this.normsCache) {
/*  628 */       this.normsCache.remove(field);
/*      */     }
/*  630 */     int i = readerIndex(n);
/*  631 */     this.subReaders[i].setNorm(n - this.starts[i], field, value);
/*      */   }
/*      */ 
/*      */   public TermEnum terms() throws IOException
/*      */   {
/*  636 */     ensureOpen();
/*  637 */     return new MultiTermEnum(this, this.subReaders, this.starts, null);
/*      */   }
/*      */ 
/*      */   public TermEnum terms(Term term) throws IOException
/*      */   {
/*  642 */     ensureOpen();
/*  643 */     return new MultiTermEnum(this, this.subReaders, this.starts, term);
/*      */   }
/*      */ 
/*      */   public int docFreq(Term t) throws IOException
/*      */   {
/*  648 */     ensureOpen();
/*  649 */     int total = 0;
/*  650 */     for (int i = 0; i < this.subReaders.length; i++)
/*  651 */       total += this.subReaders[i].docFreq(t);
/*  652 */     return total;
/*      */   }
/*      */ 
/*      */   public TermDocs termDocs() throws IOException
/*      */   {
/*  657 */     ensureOpen();
/*  658 */     return new MultiTermDocs(this, this.subReaders, this.starts);
/*      */   }
/*      */ 
/*      */   public TermPositions termPositions() throws IOException
/*      */   {
/*  663 */     ensureOpen();
/*  664 */     return new MultiTermPositions(this, this.subReaders, this.starts);
/*      */   }
/*      */ 
/*      */   protected void acquireWriteLock()
/*      */     throws StaleReaderException, CorruptIndexException, LockObtainFailedException, IOException
/*      */   {
/*  681 */     if (this.readOnly)
/*      */     {
/*  685 */       ReadOnlySegmentReader.noWrite();
/*      */     }
/*      */ 
/*  688 */     if (this.segmentInfos != null) {
/*  689 */       ensureOpen();
/*  690 */       if (this.stale) {
/*  691 */         throw new StaleReaderException("IndexReader out of date and no longer valid for delete, undelete, or setNorm operations");
/*      */       }
/*  693 */       if (this.writeLock == null) {
/*  694 */         Lock writeLock = this.directory.makeLock("write.lock");
/*  695 */         if (!writeLock.obtain(IndexWriter.WRITE_LOCK_TIMEOUT))
/*  696 */           throw new LockObtainFailedException("Index locked for write: " + writeLock);
/*  697 */         this.writeLock = writeLock;
/*      */ 
/*  701 */         if (SegmentInfos.readCurrentVersion(this.directory) > this.segmentInfos.getVersion()) {
/*  702 */           this.stale = true;
/*  703 */           this.writeLock.release();
/*  704 */           this.writeLock = null;
/*  705 */           throw new StaleReaderException("IndexReader out of date and no longer valid for delete, undelete, or setNorm operations");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void doCommit(Map<String, String> commitUserData)
/*      */     throws IOException
/*      */   {
/*  721 */     if (this.hasChanges) {
/*  722 */       this.segmentInfos.setUserData(commitUserData);
/*      */ 
/*  725 */       IndexFileDeleter deleter = new IndexFileDeleter(this.directory, this.deletionPolicy == null ? new KeepOnlyLastCommitDeletionPolicy() : this.deletionPolicy, this.segmentInfos, null, null);
/*      */ 
/*  731 */       startCommit();
/*      */ 
/*  733 */       boolean success = false;
/*      */       try {
/*  735 */         for (int i = 0; i < this.subReaders.length; i++) {
/*  736 */           this.subReaders[i].commit();
/*      */         }
/*      */ 
/*  739 */         Collection files = this.segmentInfos.files(this.directory, false);
/*  740 */         for (String fileName : files) {
/*  741 */           if (!this.synced.contains(fileName)) {
/*  742 */             assert (this.directory.fileExists(fileName));
/*  743 */             this.directory.sync(fileName);
/*  744 */             this.synced.add(fileName);
/*      */           }
/*      */         }
/*      */ 
/*  748 */         this.segmentInfos.commit(this.directory);
/*  749 */         success = true;
/*      */       }
/*      */       finally {
/*  752 */         if (!success)
/*      */         {
/*  759 */           rollbackCommit();
/*      */ 
/*  764 */           deleter.refresh();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  770 */       deleter.checkpoint(this.segmentInfos, true);
/*  771 */       deleter.close();
/*      */ 
/*  773 */       if (this.writeLock != null) {
/*  774 */         this.writeLock.release();
/*  775 */         this.writeLock = null;
/*      */       }
/*      */     }
/*  778 */     this.hasChanges = false;
/*      */   }
/*      */ 
/*      */   void startCommit() {
/*  782 */     this.rollbackHasChanges = this.hasChanges;
/*  783 */     this.rollbackSegmentInfos = ((SegmentInfos)this.segmentInfos.clone());
/*  784 */     for (int i = 0; i < this.subReaders.length; i++)
/*  785 */       this.subReaders[i].startCommit();
/*      */   }
/*      */ 
/*      */   void rollbackCommit()
/*      */   {
/*  790 */     this.hasChanges = this.rollbackHasChanges;
/*  791 */     for (int i = 0; i < this.segmentInfos.size(); i++)
/*      */     {
/*  796 */       this.segmentInfos.info(i).reset(this.rollbackSegmentInfos.info(i));
/*      */     }
/*  798 */     this.rollbackSegmentInfos = null;
/*  799 */     for (int i = 0; i < this.subReaders.length; i++)
/*  800 */       this.subReaders[i].rollbackCommit();
/*      */   }
/*      */ 
/*      */   public Map<String, String> getCommitUserData()
/*      */   {
/*  806 */     ensureOpen();
/*  807 */     return this.segmentInfos.getUserData();
/*      */   }
/*      */ 
/*      */   public boolean isCurrent() throws CorruptIndexException, IOException
/*      */   {
/*  812 */     ensureOpen();
/*  813 */     if ((this.writer == null) || (this.writer.isClosed()))
/*      */     {
/*  815 */       return SegmentInfos.readCurrentVersion(this.directory) == this.segmentInfos.getVersion();
/*      */     }
/*  817 */     return this.writer.nrtIsCurrent(this.segmentInfosStart);
/*      */   }
/*      */ 
/*      */   protected synchronized void doClose()
/*      */     throws IOException
/*      */   {
/*  823 */     IOException ioe = null;
/*  824 */     this.normsCache = null;
/*  825 */     for (int i = 0; i < this.subReaders.length; i++) {
/*      */       try
/*      */       {
/*  828 */         this.subReaders[i].decRef();
/*      */       } catch (IOException e) {
/*  830 */         if (ioe == null) ioe = e;
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  837 */     FieldCache.DEFAULT.purge(this);
/*      */ 
/*  840 */     if (ioe != null) throw ioe;
/*      */   }
/*      */ 
/*      */   public Collection<String> getFieldNames(IndexReader.FieldOption fieldNames)
/*      */   {
/*  845 */     ensureOpen();
/*  846 */     return getFieldNames(fieldNames, this.subReaders);
/*      */   }
/*      */ 
/*      */   static Collection<String> getFieldNames(IndexReader.FieldOption fieldNames, IndexReader[] subReaders)
/*      */   {
/*  851 */     Set fieldSet = new HashSet();
/*  852 */     for (IndexReader reader : subReaders) {
/*  853 */       Collection names = reader.getFieldNames(fieldNames);
/*  854 */       fieldSet.addAll(names);
/*      */     }
/*  856 */     return fieldSet;
/*      */   }
/*      */ 
/*      */   public IndexReader[] getSequentialSubReaders()
/*      */   {
/*  861 */     return this.subReaders;
/*      */   }
/*      */ 
/*      */   public Directory directory()
/*      */   {
/*  870 */     return this.directory;
/*      */   }
/*      */ 
/*      */   public int getTermInfosIndexDivisor()
/*      */   {
/*  875 */     return this.termInfosIndexDivisor;
/*      */   }
/*      */ 
/*      */   public IndexCommit getIndexCommit()
/*      */     throws IOException
/*      */   {
/*  885 */     return new ReaderCommit(this.segmentInfos, this.directory);
/*      */   }
/*      */ 
/*      */   public static Collection<IndexCommit> listCommits(Directory dir) throws IOException
/*      */   {
/*  890 */     String[] files = dir.listAll();
/*      */ 
/*  892 */     Collection commits = new ArrayList();
/*      */ 
/*  894 */     SegmentInfos latest = new SegmentInfos();
/*  895 */     latest.read(dir);
/*  896 */     long currentGen = latest.getGeneration();
/*      */ 
/*  898 */     commits.add(new ReaderCommit(latest, dir));
/*      */ 
/*  900 */     for (int i = 0; i < files.length; i++)
/*      */     {
/*  902 */       String fileName = files[i];
/*      */ 
/*  904 */       if ((fileName.startsWith("segments")) && (!fileName.equals("segments.gen")) && (SegmentInfos.generationFromSegmentsFileName(fileName) < currentGen))
/*      */       {
/*  908 */         SegmentInfos sis = new SegmentInfos();
/*      */         try
/*      */         {
/*  912 */           sis.read(dir, fileName);
/*      */         }
/*      */         catch (FileNotFoundException fnfe)
/*      */         {
/*  921 */           sis = null;
/*      */         }
/*      */ 
/*  924 */         if (sis != null) {
/*  925 */           commits.add(new ReaderCommit(sis, dir));
/*      */         }
/*      */       }
/*      */     }
/*  929 */     return commits;
/*      */   }
/*      */ 
/*      */   static class MultiTermPositions extends DirectoryReader.MultiTermDocs
/*      */     implements TermPositions
/*      */   {
/*      */     public MultiTermPositions(IndexReader topReader, IndexReader[] r, int[] s)
/*      */     {
/* 1236 */       super(r, s);
/*      */     }
/*      */ 
/*      */     protected TermDocs termDocs(IndexReader reader) throws IOException
/*      */     {
/* 1241 */       return reader.termPositions();
/*      */     }
/*      */ 
/*      */     public int nextPosition() throws IOException {
/* 1245 */       return ((TermPositions)this.current).nextPosition();
/*      */     }
/*      */ 
/*      */     public int getPayloadLength() {
/* 1249 */       return ((TermPositions)this.current).getPayloadLength();
/*      */     }
/*      */ 
/*      */     public byte[] getPayload(byte[] data, int offset) throws IOException {
/* 1253 */       return ((TermPositions)this.current).getPayload(data, offset);
/*      */     }
/*      */ 
/*      */     public boolean isPayloadAvailable()
/*      */     {
/* 1259 */       return ((TermPositions)this.current).isPayloadAvailable();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class MultiTermDocs
/*      */     implements TermDocs
/*      */   {
/*      */     IndexReader topReader;
/*      */     protected IndexReader[] readers;
/*      */     protected int[] starts;
/*      */     protected Term term;
/* 1089 */     protected int base = 0;
/* 1090 */     protected int pointer = 0;
/*      */     private TermDocs[] readerTermDocs;
/*      */     protected TermDocs current;
/*      */     private DirectoryReader.MultiTermEnum tenum;
/*      */     int matchingSegmentPos;
/*      */     SegmentMergeInfo smi;
/*      */ 
/*      */     public MultiTermDocs(IndexReader topReader, IndexReader[] r, int[] s)
/*      */     {
/* 1100 */       this.topReader = topReader;
/* 1101 */       this.readers = r;
/* 1102 */       this.starts = s;
/*      */ 
/* 1104 */       this.readerTermDocs = new TermDocs[r.length];
/*      */     }
/*      */ 
/*      */     public int doc() {
/* 1108 */       return this.base + this.current.doc();
/*      */     }
/*      */     public int freq() {
/* 1111 */       return this.current.freq();
/*      */     }
/*      */ 
/*      */     public void seek(Term term) {
/* 1115 */       this.term = term;
/* 1116 */       this.base = 0;
/* 1117 */       this.pointer = 0;
/* 1118 */       this.current = null;
/* 1119 */       this.tenum = null;
/* 1120 */       this.smi = null;
/* 1121 */       this.matchingSegmentPos = 0;
/*      */     }
/*      */ 
/*      */     public void seek(TermEnum termEnum) throws IOException {
/* 1125 */       seek(termEnum.term());
/* 1126 */       if ((termEnum instanceof DirectoryReader.MultiTermEnum)) {
/* 1127 */         this.tenum = ((DirectoryReader.MultiTermEnum)termEnum);
/* 1128 */         if (this.topReader != this.tenum.topReader)
/* 1129 */           this.tenum = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean next() throws IOException {
/*      */       while (true) {
/* 1135 */         if ((this.current != null) && (this.current.next())) {
/* 1136 */           return true;
/*      */         }
/* 1138 */         if (this.pointer >= this.readers.length) break;
/* 1139 */         if (this.tenum != null) {
/* 1140 */           this.smi = this.tenum.matchingSegments[(this.matchingSegmentPos++)];
/* 1141 */           if (this.smi == null) {
/* 1142 */             this.pointer = this.readers.length;
/* 1143 */             return false;
/*      */           }
/* 1145 */           this.pointer = this.smi.ord;
/*      */         }
/* 1147 */         this.base = this.starts[this.pointer];
/* 1148 */         this.current = termDocs(this.pointer++);
/*      */       }
/* 1150 */       return false;
/*      */     }
/*      */ 
/*      */     public int read(int[] docs, int[] freqs)
/*      */       throws IOException
/*      */     {
/*      */       while (true)
/* 1158 */         if (this.current == null) {
/* 1159 */           if (this.pointer < this.readers.length) {
/* 1160 */             if (this.tenum != null) {
/* 1161 */               this.smi = this.tenum.matchingSegments[(this.matchingSegmentPos++)];
/* 1162 */               if (this.smi == null) {
/* 1163 */                 this.pointer = this.readers.length;
/* 1164 */                 return 0;
/*      */               }
/* 1166 */               this.pointer = this.smi.ord;
/*      */             }
/* 1168 */             this.base = this.starts[this.pointer];
/* 1169 */             this.current = termDocs(this.pointer++);
/*      */           } else {
/* 1171 */             return 0;
/*      */           }
/*      */         } else {
/* 1174 */           int end = this.current.read(docs, freqs);
/* 1175 */           if (end == 0) {
/* 1176 */             this.current = null;
/*      */           } else {
/* 1178 */             int b = this.base;
/* 1179 */             for (int i = 0; i < end; i++)
/* 1180 */               docs[i] += b;
/* 1181 */             return end;
/*      */           }
/*      */         }
/*      */     }
/*      */ 
/*      */     public boolean skipTo(int target) throws IOException
/*      */     {
/*      */       while (true) {
/* 1189 */         if ((this.current != null) && (this.current.skipTo(target - this.base)))
/* 1190 */           return true;
/* 1191 */         if (this.pointer >= this.readers.length) break;
/* 1192 */         if (this.tenum != null) {
/* 1193 */           SegmentMergeInfo smi = this.tenum.matchingSegments[(this.matchingSegmentPos++)];
/* 1194 */           if (smi == null) {
/* 1195 */             this.pointer = this.readers.length;
/* 1196 */             return false;
/*      */           }
/* 1198 */           this.pointer = smi.ord;
/*      */         }
/* 1200 */         this.base = this.starts[this.pointer];
/* 1201 */         this.current = termDocs(this.pointer++);
/*      */       }
/* 1203 */       return false;
/*      */     }
/*      */ 
/*      */     private TermDocs termDocs(int i) throws IOException
/*      */     {
/* 1208 */       TermDocs result = this.readerTermDocs[i];
/* 1209 */       if (result == null)
/* 1210 */         result = this.readerTermDocs[i] =  = termDocs(this.readers[i]);
/* 1211 */       if (this.smi != null) {
/* 1212 */         assert (this.smi.ord == i);
/* 1213 */         assert (this.smi.termEnum.term().equals(this.term));
/* 1214 */         result.seek(this.smi.termEnum);
/*      */       } else {
/* 1216 */         result.seek(this.term);
/*      */       }
/* 1218 */       return result;
/*      */     }
/*      */ 
/*      */     protected TermDocs termDocs(IndexReader reader) throws IOException
/*      */     {
/* 1223 */       return this.term == null ? reader.termDocs(null) : reader.termDocs();
/*      */     }
/*      */ 
/*      */     public void close() throws IOException {
/* 1227 */       for (int i = 0; i < this.readerTermDocs.length; i++)
/* 1228 */         if (this.readerTermDocs[i] != null)
/* 1229 */           this.readerTermDocs[i].close();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class MultiTermEnum extends TermEnum
/*      */   {
/*      */     IndexReader topReader;
/*      */     private SegmentMergeQueue queue;
/*      */     private Term term;
/*      */     private int docFreq;
/*      */     final SegmentMergeInfo[] matchingSegments;
/*      */ 
/*      */     public MultiTermEnum(IndexReader topReader, IndexReader[] readers, int[] starts, Term t)
/*      */       throws IOException
/*      */     {
/* 1007 */       this.topReader = topReader;
/* 1008 */       this.queue = new SegmentMergeQueue(readers.length);
/* 1009 */       this.matchingSegments = new SegmentMergeInfo[readers.length + 1];
/* 1010 */       for (int i = 0; i < readers.length; i++) {
/* 1011 */         IndexReader reader = readers[i];
/*      */         TermEnum termEnum;
/*      */         TermEnum termEnum;
/* 1014 */         if (t != null)
/* 1015 */           termEnum = reader.terms(t);
/*      */         else {
/* 1017 */           termEnum = reader.terms();
/*      */         }
/* 1019 */         SegmentMergeInfo smi = new SegmentMergeInfo(starts[i], termEnum, reader);
/* 1020 */         smi.ord = i;
/* 1021 */         if (t == null ? smi.next() : termEnum.term() != null)
/* 1022 */           this.queue.add(smi);
/*      */         else {
/* 1024 */           smi.close();
/*      */         }
/*      */       }
/* 1027 */       if ((t != null) && (this.queue.size() > 0))
/* 1028 */         next();
/*      */     }
/*      */ 
/*      */     public boolean next()
/*      */       throws IOException
/*      */     {
/* 1034 */       for (int i = 0; i < this.matchingSegments.length; i++) {
/* 1035 */         SegmentMergeInfo smi = this.matchingSegments[i];
/* 1036 */         if (smi == null) break;
/* 1037 */         if (smi.next())
/* 1038 */           this.queue.add(smi);
/*      */         else {
/* 1040 */           smi.close();
/*      */         }
/*      */       }
/* 1043 */       int numMatchingSegments = 0;
/* 1044 */       this.matchingSegments[0] = null;
/*      */ 
/* 1046 */       SegmentMergeInfo top = (SegmentMergeInfo)this.queue.top();
/*      */ 
/* 1048 */       if (top == null) {
/* 1049 */         this.term = null;
/* 1050 */         return false;
/*      */       }
/*      */ 
/* 1053 */       this.term = top.term;
/* 1054 */       this.docFreq = 0;
/*      */ 
/* 1056 */       while ((top != null) && (this.term.compareTo(top.term) == 0)) {
/* 1057 */         this.matchingSegments[(numMatchingSegments++)] = top;
/* 1058 */         this.queue.pop();
/* 1059 */         this.docFreq += top.termEnum.docFreq();
/* 1060 */         top = (SegmentMergeInfo)this.queue.top();
/*      */       }
/*      */ 
/* 1063 */       this.matchingSegments[numMatchingSegments] = null;
/* 1064 */       return true;
/*      */     }
/*      */ 
/*      */     public Term term()
/*      */     {
/* 1069 */       return this.term;
/*      */     }
/*      */ 
/*      */     public int docFreq()
/*      */     {
/* 1074 */       return this.docFreq;
/*      */     }
/*      */ 
/*      */     public void close() throws IOException
/*      */     {
/* 1079 */       this.queue.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class ReaderCommit extends IndexCommit
/*      */   {
/*      */     private String segmentsFileName;
/*      */     Collection<String> files;
/*      */     Directory dir;
/*      */     long generation;
/*      */     long version;
/*      */     final boolean isOptimized;
/*      */     final Map<String, String> userData;
/*      */ 
/*      */     ReaderCommit(SegmentInfos infos, Directory dir)
/*      */       throws IOException
/*      */     {
/*  942 */       this.segmentsFileName = infos.getCurrentSegmentFileName();
/*  943 */       this.dir = dir;
/*  944 */       this.userData = infos.getUserData();
/*  945 */       this.files = Collections.unmodifiableCollection(infos.files(dir, true));
/*  946 */       this.version = infos.getVersion();
/*  947 */       this.generation = infos.getGeneration();
/*  948 */       this.isOptimized = ((infos.size() == 1) && (!infos.info(0).hasDeletions()));
/*      */     }
/*      */ 
/*      */     public boolean isOptimized()
/*      */     {
/*  953 */       return this.isOptimized;
/*      */     }
/*      */ 
/*      */     public String getSegmentsFileName()
/*      */     {
/*  958 */       return this.segmentsFileName;
/*      */     }
/*      */ 
/*      */     public Collection<String> getFileNames()
/*      */     {
/*  963 */       return this.files;
/*      */     }
/*      */ 
/*      */     public Directory getDirectory()
/*      */     {
/*  968 */       return this.dir;
/*      */     }
/*      */ 
/*      */     public long getVersion()
/*      */     {
/*  973 */       return this.version;
/*      */     }
/*      */ 
/*      */     public long getGeneration()
/*      */     {
/*  978 */       return this.generation;
/*      */     }
/*      */ 
/*      */     public boolean isDeleted()
/*      */     {
/*  983 */       return false;
/*      */     }
/*      */ 
/*      */     public Map<String, String> getUserData()
/*      */     {
/*  988 */       return this.userData;
/*      */     }
/*      */ 
/*      */     public void delete()
/*      */     {
/*  993 */       throw new UnsupportedOperationException("This IndexCommit does not support deletions");
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DirectoryReader
 * JD-Core Version:    0.6.2
 */