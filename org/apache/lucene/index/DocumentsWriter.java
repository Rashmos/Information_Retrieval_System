/*      */ package org.apache.lucene.index;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import org.apache.lucene.analysis.Analyzer;
/*      */ import org.apache.lucene.document.Document;
/*      */ import org.apache.lucene.search.IndexSearcher;
/*      */ import org.apache.lucene.search.Query;
/*      */ import org.apache.lucene.search.Scorer;
/*      */ import org.apache.lucene.search.Similarity;
/*      */ import org.apache.lucene.search.Weight;
/*      */ import org.apache.lucene.store.AlreadyClosedException;
/*      */ import org.apache.lucene.store.Directory;
/*      */ import org.apache.lucene.store.RAMFile;
/*      */ import org.apache.lucene.util.ArrayUtil;
/*      */ import org.apache.lucene.util.Constants;
/*      */ import org.apache.lucene.util.ThreadInterruptedException;
/*      */ 
/*      */ final class DocumentsWriter
/*      */ {
/*      */   IndexWriter writer;
/*      */   Directory directory;
/*      */   String segment;
/*      */   private String docStoreSegment;
/*      */   private int docStoreOffset;
/*      */   private int nextDocID;
/*      */   private int numDocsInRAM;
/*      */   int numDocsInStore;
/*      */   private static final int MAX_THREAD_STATE = 5;
/*  128 */   private DocumentsWriterThreadState[] threadStates = new DocumentsWriterThreadState[0];
/*  129 */   private final HashMap<Thread, DocumentsWriterThreadState> threadBindings = new HashMap();
/*      */   private int pauseThreads;
/*      */   boolean flushPending;
/*      */   boolean bufferIsFull;
/*      */   private boolean aborting;
/*      */   private DocFieldProcessor docFieldProcessor;
/*      */   PrintStream infoStream;
/*  140 */   int maxFieldLength = 10000;
/*      */   Similarity similarity;
/*      */   List<String> newFiles;
/*  229 */   static final IndexingChain DefaultIndexingChain = new IndexingChain()
/*      */   {
/*      */     DocConsumer getChain(DocumentsWriter documentsWriter)
/*      */     {
/*  253 */       TermsHashConsumer termVectorsWriter = new TermVectorsTermsWriter(documentsWriter);
/*  254 */       TermsHashConsumer freqProxWriter = new FreqProxTermsWriter();
/*      */ 
/*  256 */       InvertedDocConsumer termsHash = new TermsHash(documentsWriter, true, freqProxWriter, new TermsHash(documentsWriter, false, termVectorsWriter, null));
/*      */ 
/*  258 */       NormsWriter normsWriter = new NormsWriter();
/*  259 */       DocInverter docInverter = new DocInverter(termsHash, normsWriter);
/*  260 */       return new DocFieldProcessor(documentsWriter, docInverter);
/*      */     }
/*  229 */   };
/*      */   final DocConsumer consumer;
/*  268 */   private BufferedDeletes deletesInRAM = new BufferedDeletes(false);
/*      */ 
/*  272 */   private BufferedDeletes deletesFlushed = new BufferedDeletes(true);
/*      */ 
/*  276 */   private int maxBufferedDeleteTerms = -1;
/*      */ 
/*  280 */   private long ramBufferSize = 16777216L;
/*  281 */   private long waitQueuePauseBytes = ()(this.ramBufferSize * 0.1D);
/*  282 */   private long waitQueueResumeBytes = ()(this.ramBufferSize * 0.05D);
/*      */ 
/*  286 */   private long freeTrigger = 17616076L;
/*  287 */   private long freeLevel = 15938355L;
/*      */ 
/*  291 */   private int maxBufferedDocs = -1;
/*      */   private int flushedDocCount;
/*      */   private boolean closed;
/*      */   private Collection<String> abortedFiles;
/*      */   private SegmentWriteState flushState;
/*  446 */   final List<String> openFiles = new ArrayList();
/*  447 */   final List<String> closedFiles = new ArrayList();
/*      */   private Term lastDeleteTerm;
/* 1191 */   final SkipDocWriter skipDocWriter = new SkipDocWriter(null);
/*      */   long numBytesAlloc;
/*      */   long numBytesUsed;
/* 1200 */   NumberFormat nf = NumberFormat.getInstance();
/*      */   static final int OBJECT_HEADER_BYTES = 8;
/* 1204 */   static final int POINTER_NUM_BYTE = Constants.JRE_IS_64BIT ? 8 : 4;
/*      */   static final int INT_NUM_BYTE = 4;
/*      */   static final int CHAR_NUM_BYTE = 2;
/* 1218 */   static final int BYTES_PER_DEL_TERM = 8 * POINTER_NUM_BYTE + 40 + 24;
/*      */ 
/* 1223 */   static final int BYTES_PER_DEL_DOCID = 2 * POINTER_NUM_BYTE + 8 + 4;
/*      */ 
/* 1230 */   static final int BYTES_PER_DEL_QUERY = 5 * POINTER_NUM_BYTE + 16 + 8 + 24;
/*      */   static final int BYTE_BLOCK_SHIFT = 15;
/*      */   static final int BYTE_BLOCK_SIZE = 32768;
/*      */   static final int BYTE_BLOCK_MASK = 32767;
/*      */   static final int BYTE_BLOCK_NOT_MASK = -32768;
/*      */   static final int INT_BLOCK_SHIFT = 13;
/*      */   static final int INT_BLOCK_SIZE = 8192;
/*      */   static final int INT_BLOCK_MASK = 8191;
/* 1300 */   private ArrayList<int[]> freeIntBlocks = new ArrayList();
/*      */ 
/* 1340 */   ByteBlockAllocator byteBlockAllocator = new ByteBlockAllocator(32768);
/*      */   static final int PER_DOC_BLOCK_SIZE = 1024;
/* 1344 */   final ByteBlockAllocator perDocAllocator = new ByteBlockAllocator(1024);
/*      */   static final int CHAR_BLOCK_SHIFT = 14;
/*      */   static final int CHAR_BLOCK_SIZE = 16384;
/*      */   static final int CHAR_BLOCK_MASK = 16383;
/*      */   static final int MAX_TERM_LENGTH = 16383;
/* 1355 */   private ArrayList<char[]> freeCharBlocks = new ArrayList();
/*      */ 
/* 1507 */   final WaitQueue waitQueue = new WaitQueue();
/*      */ 
/*      */   PerDocBuffer newPerDocBuffer()
/*      */   {
/*  187 */     return new PerDocBuffer();
/*      */   }
/*      */ 
/*      */   synchronized void updateFlushedDocCount(int n)
/*      */   {
/*  296 */     this.flushedDocCount += n;
/*      */   }
/*      */   synchronized int getFlushedDocCount() {
/*  299 */     return this.flushedDocCount;
/*      */   }
/*      */   synchronized void setFlushedDocCount(int n) {
/*  302 */     this.flushedDocCount = n;
/*      */   }
/*      */ 
/*      */   DocumentsWriter(Directory directory, IndexWriter writer, IndexingChain indexingChain)
/*      */     throws IOException
/*      */   {
/*  308 */     this.directory = directory;
/*  309 */     this.writer = writer;
/*  310 */     this.similarity = writer.getSimilarity();
/*  311 */     this.flushedDocCount = writer.maxDoc();
/*      */ 
/*  313 */     this.consumer = indexingChain.getChain(this);
/*  314 */     if ((this.consumer instanceof DocFieldProcessor))
/*  315 */       this.docFieldProcessor = ((DocFieldProcessor)this.consumer);
/*      */   }
/*      */ 
/*      */   boolean hasProx()
/*      */   {
/*  322 */     return this.docFieldProcessor != null ? this.docFieldProcessor.fieldInfos.hasProx() : true;
/*      */   }
/*      */ 
/*      */   synchronized void setInfoStream(PrintStream infoStream)
/*      */   {
/*  329 */     this.infoStream = infoStream;
/*  330 */     for (int i = 0; i < this.threadStates.length; i++)
/*  331 */       this.threadStates[i].docState.infoStream = infoStream;
/*      */   }
/*      */ 
/*      */   synchronized void setMaxFieldLength(int maxFieldLength) {
/*  335 */     this.maxFieldLength = maxFieldLength;
/*  336 */     for (int i = 0; i < this.threadStates.length; i++)
/*  337 */       this.threadStates[i].docState.maxFieldLength = maxFieldLength;
/*      */   }
/*      */ 
/*      */   synchronized void setSimilarity(Similarity similarity) {
/*  341 */     this.similarity = similarity;
/*  342 */     for (int i = 0; i < this.threadStates.length; i++)
/*  343 */       this.threadStates[i].docState.similarity = similarity;
/*      */   }
/*      */ 
/*      */   synchronized void setRAMBufferSizeMB(double mb)
/*      */   {
/*  348 */     if (mb == -1.0D) {
/*  349 */       this.ramBufferSize = -1L;
/*  350 */       this.waitQueuePauseBytes = 4194304L;
/*  351 */       this.waitQueueResumeBytes = 2097152L;
/*      */     } else {
/*  353 */       this.ramBufferSize = (()(mb * 1024.0D * 1024.0D));
/*  354 */       this.waitQueuePauseBytes = (()(this.ramBufferSize * 0.1D));
/*  355 */       this.waitQueueResumeBytes = (()(this.ramBufferSize * 0.05D));
/*  356 */       this.freeTrigger = (()(1.05D * this.ramBufferSize));
/*  357 */       this.freeLevel = (()(0.95D * this.ramBufferSize));
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized double getRAMBufferSizeMB() {
/*  362 */     if (this.ramBufferSize == -1L) {
/*  363 */       return this.ramBufferSize;
/*      */     }
/*  365 */     return this.ramBufferSize / 1024.0D / 1024.0D;
/*      */   }
/*      */ 
/*      */   void setMaxBufferedDocs(int count)
/*      */   {
/*  372 */     this.maxBufferedDocs = count;
/*      */   }
/*      */ 
/*      */   int getMaxBufferedDocs() {
/*  376 */     return this.maxBufferedDocs;
/*      */   }
/*      */ 
/*      */   String getSegment()
/*      */   {
/*  381 */     return this.segment;
/*      */   }
/*      */ 
/*      */   int getNumDocsInRAM()
/*      */   {
/*  386 */     return this.numDocsInRAM;
/*      */   }
/*      */ 
/*      */   synchronized String getDocStoreSegment()
/*      */   {
/*  392 */     return this.docStoreSegment;
/*      */   }
/*      */ 
/*      */   int getDocStoreOffset()
/*      */   {
/*  398 */     return this.docStoreOffset;
/*      */   }
/*      */ 
/*      */   synchronized String closeDocStore()
/*      */     throws IOException
/*      */   {
/*  406 */     assert (allThreadsIdle());
/*      */ 
/*  408 */     if (this.infoStream != null) {
/*  409 */       message("closeDocStore: " + this.openFiles.size() + " files to flush to segment " + this.docStoreSegment + " numDocs=" + this.numDocsInStore);
/*      */     }
/*  411 */     boolean success = false;
/*      */     try
/*      */     {
/*  414 */       initFlushState(true);
/*  415 */       this.closedFiles.clear();
/*      */ 
/*  417 */       this.consumer.closeDocStore(this.flushState);
/*  418 */       assert (0 == this.openFiles.size());
/*      */ 
/*  420 */       String s = this.docStoreSegment;
/*  421 */       this.docStoreSegment = null;
/*  422 */       this.docStoreOffset = 0;
/*  423 */       this.numDocsInStore = 0;
/*  424 */       success = true;
/*  425 */       return s;
/*      */     } finally {
/*  427 */       if (!success)
/*  428 */         abort();
/*      */     }
/*      */   }
/*      */ 
/*      */   Collection<String> abortedFiles()
/*      */   {
/*  438 */     return this.abortedFiles;
/*      */   }
/*      */ 
/*      */   void message(String message) {
/*  442 */     if (this.infoStream != null)
/*  443 */       this.writer.message("DW: " + message);
/*      */   }
/*      */ 
/*      */   synchronized List<String> openFiles()
/*      */   {
/*  453 */     return (List)((ArrayList)this.openFiles).clone();
/*      */   }
/*      */ 
/*      */   synchronized List<String> closedFiles()
/*      */   {
/*  458 */     return (List)((ArrayList)this.closedFiles).clone();
/*      */   }
/*      */ 
/*      */   synchronized void addOpenFile(String name) {
/*  462 */     assert (!this.openFiles.contains(name));
/*  463 */     this.openFiles.add(name);
/*      */   }
/*      */ 
/*      */   synchronized void removeOpenFile(String name) {
/*  467 */     assert (this.openFiles.contains(name));
/*  468 */     this.openFiles.remove(name);
/*  469 */     this.closedFiles.add(name);
/*      */   }
/*      */ 
/*      */   synchronized void setAborting() {
/*  473 */     this.aborting = true;
/*      */   }
/*      */ 
/*      */   synchronized void abort()
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  483 */       if (this.infoStream != null) {
/*  484 */         message("docWriter: now abort");
/*      */       }
/*      */ 
/*  488 */       this.waitQueue.abort();
/*      */ 
/*  492 */       pauseAllThreads();
/*      */       try
/*      */       {
/*  496 */         assert (0 == this.waitQueue.numWaiting);
/*      */ 
/*  498 */         this.waitQueue.waitingBytes = 0L;
/*      */         try
/*      */         {
/*  501 */           this.abortedFiles = openFiles();
/*      */         } catch (Throwable t) {
/*  503 */           this.abortedFiles = null;
/*      */         }
/*      */ 
/*  506 */         this.deletesInRAM.clear();
/*      */ 
/*  508 */         this.openFiles.clear();
/*      */ 
/*  510 */         for (int i = 0; i < this.threadStates.length; i++)
/*      */           try {
/*  512 */             this.threadStates[i].consumer.abort();
/*      */           }
/*      */           catch (Throwable t) {
/*      */           }
/*      */         try {
/*  517 */           this.consumer.abort();
/*      */         }
/*      */         catch (Throwable t) {
/*      */         }
/*  521 */         this.docStoreSegment = null;
/*  522 */         this.numDocsInStore = 0;
/*  523 */         this.docStoreOffset = 0;
/*      */ 
/*  526 */         doAfterFlush();
/*      */       }
/*      */       finally {
/*  529 */         resumeAllThreads();
/*      */       }
/*      */     } finally {
/*  532 */       this.aborting = false;
/*  533 */       notifyAll();
/*  534 */       if (this.infoStream != null)
/*  535 */         message("docWriter: done abort");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doAfterFlush()
/*      */     throws IOException
/*      */   {
/*  543 */     assert (allThreadsIdle());
/*  544 */     this.threadBindings.clear();
/*  545 */     this.waitQueue.reset();
/*  546 */     this.segment = null;
/*  547 */     this.numDocsInRAM = 0;
/*  548 */     this.nextDocID = 0;
/*  549 */     this.bufferIsFull = false;
/*  550 */     this.flushPending = false;
/*  551 */     for (int i = 0; i < this.threadStates.length; i++)
/*  552 */       this.threadStates[i].doAfterFlush();
/*  553 */     this.numBytesUsed = 0L;
/*      */   }
/*      */ 
/*      */   synchronized boolean pauseAllThreads()
/*      */   {
/*  558 */     this.pauseThreads += 1;
/*  559 */     while (!allThreadsIdle()) {
/*      */       try {
/*  561 */         wait();
/*      */       } catch (InterruptedException ie) {
/*  563 */         throw new ThreadInterruptedException(ie);
/*      */       }
/*      */     }
/*      */ 
/*  567 */     return this.aborting;
/*      */   }
/*      */ 
/*      */   synchronized void resumeAllThreads() {
/*  571 */     this.pauseThreads -= 1;
/*  572 */     assert (this.pauseThreads >= 0);
/*  573 */     if (0 == this.pauseThreads)
/*  574 */       notifyAll();
/*      */   }
/*      */ 
/*      */   private synchronized boolean allThreadsIdle() {
/*  578 */     for (int i = 0; i < this.threadStates.length; i++)
/*  579 */       if (!this.threadStates[i].isIdle)
/*  580 */         return false;
/*  581 */     return true;
/*      */   }
/*      */ 
/*      */   synchronized boolean anyChanges() {
/*  585 */     return (this.numDocsInRAM != 0) || (this.deletesInRAM.numTerms != 0) || (this.deletesInRAM.docIDs.size() != 0) || (this.deletesInRAM.queries.size() != 0);
/*      */   }
/*      */ 
/*      */   private synchronized void initFlushState(boolean onlyDocStore)
/*      */   {
/*  592 */     initSegmentName(onlyDocStore);
/*  593 */     this.flushState = new SegmentWriteState(this, this.directory, this.segment, this.docStoreSegment, this.numDocsInRAM, this.numDocsInStore, this.writer.getTermIndexInterval());
/*      */   }
/*      */ 
/*      */   synchronized int flush(boolean closeDocStore)
/*      */     throws IOException
/*      */   {
/*  599 */     assert (allThreadsIdle());
/*      */ 
/*  601 */     assert (this.numDocsInRAM > 0);
/*      */ 
/*  603 */     assert (this.nextDocID == this.numDocsInRAM);
/*  604 */     assert (this.waitQueue.numWaiting == 0);
/*  605 */     assert (this.waitQueue.waitingBytes == 0L);
/*      */ 
/*  607 */     initFlushState(false);
/*      */ 
/*  609 */     this.docStoreOffset = this.numDocsInStore;
/*      */ 
/*  611 */     if (this.infoStream != null) {
/*  612 */       message("flush postings as segment " + this.flushState.segmentName + " numDocs=" + this.numDocsInRAM);
/*      */     }
/*  614 */     boolean success = false;
/*      */     try
/*      */     {
/*  618 */       if (closeDocStore) {
/*  619 */         assert (this.flushState.docStoreSegmentName != null);
/*  620 */         assert (this.flushState.docStoreSegmentName.equals(this.flushState.segmentName));
/*  621 */         closeDocStore();
/*  622 */         this.flushState.numDocsInStore = 0;
/*      */       }
/*      */ 
/*  625 */       Collection threads = new HashSet();
/*  626 */       for (int i = 0; i < this.threadStates.length; i++)
/*  627 */         threads.add(this.threadStates[i].consumer);
/*  628 */       this.consumer.flush(threads, this.flushState);
/*      */ 
/*  630 */       if (this.infoStream != null) {
/*  631 */         SegmentInfo si = new SegmentInfo(this.flushState.segmentName, this.flushState.numDocs, this.directory);
/*  632 */         long newSegmentSize = si.sizeInBytes();
/*  633 */         String message = "  oldRAMSize=" + this.numBytesUsed + " newFlushedSize=" + newSegmentSize + " docs/MB=" + this.nf.format(this.numDocsInRAM / (newSegmentSize / 1024.0D / 1024.0D)) + " new/old=" + this.nf.format(100.0D * newSegmentSize / this.numBytesUsed) + "%";
/*      */ 
/*  637 */         message(message);
/*      */       }
/*      */ 
/*  640 */       this.flushedDocCount += this.flushState.numDocs;
/*      */ 
/*  642 */       doAfterFlush();
/*      */ 
/*  644 */       success = true;
/*      */     }
/*      */     finally {
/*  647 */       if (!success) {
/*  648 */         abort();
/*      */       }
/*      */     }
/*      */ 
/*  652 */     assert (this.waitQueue.waitingBytes == 0L);
/*      */ 
/*  654 */     return this.flushState.numDocs;
/*      */   }
/*      */ 
/*      */   void createCompoundFile(String segment)
/*      */     throws IOException
/*      */   {
/*  660 */     CompoundFileWriter cfsWriter = new CompoundFileWriter(this.directory, segment + "." + "cfs");
/*  661 */     for (String flushedFile : this.flushState.flushedFiles) {
/*  662 */       cfsWriter.addFile(flushedFile);
/*      */     }
/*      */ 
/*  665 */     cfsWriter.close();
/*      */   }
/*      */ 
/*      */   synchronized boolean setFlushPending()
/*      */   {
/*  673 */     if (this.flushPending) {
/*  674 */       return false;
/*      */     }
/*  676 */     this.flushPending = true;
/*  677 */     return true;
/*      */   }
/*      */ 
/*      */   synchronized void clearFlushPending()
/*      */   {
/*  682 */     this.flushPending = false;
/*      */   }
/*      */ 
/*      */   synchronized void pushDeletes() {
/*  686 */     this.deletesFlushed.update(this.deletesInRAM);
/*      */   }
/*      */ 
/*      */   synchronized void close() {
/*  690 */     this.closed = true;
/*  691 */     notifyAll();
/*      */   }
/*      */ 
/*      */   synchronized void initSegmentName(boolean onlyDocStore) {
/*  695 */     if ((this.segment == null) && ((!onlyDocStore) || (this.docStoreSegment == null))) {
/*  696 */       this.segment = this.writer.newSegmentName();
/*  697 */       assert (this.numDocsInRAM == 0);
/*      */     }
/*  699 */     if (this.docStoreSegment == null) {
/*  700 */       this.docStoreSegment = this.segment;
/*  701 */       assert (this.numDocsInStore == 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized DocumentsWriterThreadState getThreadState(Document doc, Term delTerm)
/*      */     throws IOException
/*      */   {
/*  715 */     DocumentsWriterThreadState state = (DocumentsWriterThreadState)this.threadBindings.get(Thread.currentThread());
/*  716 */     if (state == null)
/*      */     {
/*  720 */       DocumentsWriterThreadState minThreadState = null;
/*  721 */       for (int i = 0; i < this.threadStates.length; i++) {
/*  722 */         DocumentsWriterThreadState ts = this.threadStates[i];
/*  723 */         if ((minThreadState == null) || (ts.numThreads < minThreadState.numThreads))
/*  724 */           minThreadState = ts;
/*      */       }
/*  726 */       if ((minThreadState != null) && ((minThreadState.numThreads == 0) || (this.threadStates.length >= 5))) {
/*  727 */         state = minThreadState;
/*  728 */         state.numThreads += 1;
/*      */       }
/*      */       else {
/*  731 */         DocumentsWriterThreadState[] newArray = new DocumentsWriterThreadState[1 + this.threadStates.length];
/*  732 */         if (this.threadStates.length > 0)
/*  733 */           System.arraycopy(this.threadStates, 0, newArray, 0, this.threadStates.length);
/*  734 */         state = newArray[this.threadStates.length] =  = new DocumentsWriterThreadState(this);
/*  735 */         this.threadStates = newArray;
/*      */       }
/*  737 */       this.threadBindings.put(Thread.currentThread(), state);
/*      */     }
/*      */ 
/*  743 */     waitReady(state);
/*      */ 
/*  747 */     initSegmentName(false);
/*      */ 
/*  749 */     state.isIdle = false;
/*      */ 
/*  751 */     boolean success = false;
/*      */     try {
/*  753 */       state.docState.docID = this.nextDocID;
/*      */ 
/*  755 */       assert (this.writer.testPoint("DocumentsWriter.ThreadState.init start"));
/*      */ 
/*  757 */       if (delTerm != null) {
/*  758 */         addDeleteTerm(delTerm, state.docState.docID);
/*  759 */         state.doFlushAfter = timeToFlushDeletes();
/*      */       }
/*      */ 
/*  762 */       assert (this.writer.testPoint("DocumentsWriter.ThreadState.init after delTerm"));
/*      */ 
/*  764 */       this.nextDocID += 1;
/*  765 */       this.numDocsInRAM += 1;
/*      */ 
/*  770 */       if ((!this.flushPending) && (this.maxBufferedDocs != -1) && (this.numDocsInRAM >= this.maxBufferedDocs))
/*      */       {
/*  773 */         this.flushPending = true;
/*  774 */         state.doFlushAfter = true;
/*      */       }
/*      */ 
/*  777 */       success = true;
/*      */     } finally {
/*  779 */       if (!success)
/*      */       {
/*  781 */         state.isIdle = true;
/*  782 */         notifyAll();
/*  783 */         if (state.doFlushAfter) {
/*  784 */           state.doFlushAfter = false;
/*  785 */           this.flushPending = false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  790 */     return state;
/*      */   }
/*      */ 
/*      */   boolean addDocument(Document doc, Analyzer analyzer)
/*      */     throws CorruptIndexException, IOException
/*      */   {
/*  797 */     return updateDocument(doc, analyzer, null);
/*      */   }
/*      */ 
/*      */   boolean updateDocument(Term t, Document doc, Analyzer analyzer) throws CorruptIndexException, IOException
/*      */   {
/*  802 */     return updateDocument(doc, analyzer, t);
/*      */   }
/*      */ 
/*      */   boolean updateDocument(Document doc, Analyzer analyzer, Term delTerm)
/*      */     throws CorruptIndexException, IOException
/*      */   {
/*  809 */     DocumentsWriterThreadState state = getThreadState(doc, delTerm);
/*      */ 
/*  811 */     DocState docState = state.docState;
/*  812 */     docState.doc = doc;
/*  813 */     docState.analyzer = analyzer;
/*      */ 
/*  815 */     boolean success = false;
/*      */     try
/*      */     {
/*      */       DocWriter perDoc;
/*      */       try
/*      */       {
/*  821 */         perDoc = state.consumer.processDocument();
/*      */       } finally {
/*  823 */         docState.clear();
/*      */       }
/*      */ 
/*  827 */       finishDocument(state, perDoc);
/*      */ 
/*  829 */       success = true;
/*      */     } finally {
/*      */     }
/*  832 */     synchronized (this)
/*      */     {
/*  834 */       if (this.aborting) {
/*  835 */         state.isIdle = true;
/*  836 */         notifyAll();
/*  837 */         abort();
/*      */       } else {
/*  839 */         this.skipDocWriter.docID = docState.docID;
/*  840 */         boolean success2 = false;
/*      */         try {
/*  842 */           this.waitQueue.add(this.skipDocWriter);
/*  843 */           success2 = true;
/*      */         } finally {
/*  845 */           if (!success2) {
/*  846 */             state.isIdle = true;
/*  847 */             notifyAll();
/*  848 */             abort();
/*  849 */             return false;
/*      */           }
/*      */         }
/*      */ 
/*  853 */         state.isIdle = true;
/*  854 */         notifyAll();
/*      */ 
/*  858 */         if (state.doFlushAfter) {
/*  859 */           state.doFlushAfter = false;
/*  860 */           this.flushPending = false;
/*  861 */           notifyAll();
/*      */         }
/*      */ 
/*  868 */         addDeleteDocID(state.docState.docID);
/*      */       }
/*      */     }
/*  870 */     ret;
/*      */ 
/*  874 */     return (state.doFlushAfter) || (timeToFlushDeletes());
/*      */   }
/*      */ 
/*      */   synchronized int getNumBufferedDeleteTerms()
/*      */   {
/*  879 */     return this.deletesInRAM.numTerms;
/*      */   }
/*      */ 
/*      */   synchronized Map<Term, BufferedDeletes.Num> getBufferedDeleteTerms()
/*      */   {
/*  884 */     return this.deletesInRAM.terms;
/*      */   }
/*      */ 
/*      */   synchronized void remapDeletes(SegmentInfos infos, int[][] docMaps, int[] delCounts, MergePolicy.OneMerge merge, int mergeDocCount)
/*      */   {
/*  889 */     if (docMaps == null)
/*      */     {
/*  891 */       return;
/*  892 */     }MergeDocIDRemapper mapper = new MergeDocIDRemapper(infos, docMaps, delCounts, merge, mergeDocCount);
/*  893 */     this.deletesInRAM.remap(mapper, infos, docMaps, delCounts, merge, mergeDocCount);
/*  894 */     this.deletesFlushed.remap(mapper, infos, docMaps, delCounts, merge, mergeDocCount);
/*  895 */     this.flushedDocCount -= mapper.docShift;
/*      */   }
/*      */ 
/*      */   private synchronized void waitReady(DocumentsWriterThreadState state)
/*      */   {
/*  900 */     while ((!this.closed) && (((state != null) && (!state.isIdle)) || (this.pauseThreads != 0) || (this.flushPending) || (this.aborting))) {
/*      */       try {
/*  902 */         wait();
/*      */       } catch (InterruptedException ie) {
/*  904 */         throw new ThreadInterruptedException(ie);
/*      */       }
/*      */     }
/*      */ 
/*  908 */     if (this.closed)
/*  909 */       throw new AlreadyClosedException("this IndexWriter is closed");
/*      */   }
/*      */ 
/*      */   synchronized boolean bufferDeleteTerms(Term[] terms) throws IOException {
/*  913 */     waitReady(null);
/*  914 */     for (int i = 0; i < terms.length; i++)
/*  915 */       addDeleteTerm(terms[i], this.numDocsInRAM);
/*  916 */     return timeToFlushDeletes();
/*      */   }
/*      */ 
/*      */   synchronized boolean bufferDeleteTerm(Term term) throws IOException {
/*  920 */     waitReady(null);
/*  921 */     addDeleteTerm(term, this.numDocsInRAM);
/*  922 */     return timeToFlushDeletes();
/*      */   }
/*      */ 
/*      */   synchronized boolean bufferDeleteQueries(Query[] queries) throws IOException {
/*  926 */     waitReady(null);
/*  927 */     for (int i = 0; i < queries.length; i++)
/*  928 */       addDeleteQuery(queries[i], this.numDocsInRAM);
/*  929 */     return timeToFlushDeletes();
/*      */   }
/*      */ 
/*      */   synchronized boolean bufferDeleteQuery(Query query) throws IOException {
/*  933 */     waitReady(null);
/*  934 */     addDeleteQuery(query, this.numDocsInRAM);
/*  935 */     return timeToFlushDeletes();
/*      */   }
/*      */ 
/*      */   synchronized boolean deletesFull() {
/*  939 */     return ((this.ramBufferSize != -1L) && (this.deletesInRAM.bytesUsed + this.deletesFlushed.bytesUsed + this.numBytesUsed >= this.ramBufferSize)) || ((this.maxBufferedDeleteTerms != -1) && (this.deletesInRAM.size() + this.deletesFlushed.size() >= this.maxBufferedDeleteTerms));
/*      */   }
/*      */ 
/*      */   synchronized boolean doApplyDeletes()
/*      */   {
/*  954 */     return ((this.ramBufferSize != -1L) && (this.deletesInRAM.bytesUsed + this.deletesFlushed.bytesUsed >= this.ramBufferSize / 2L)) || ((this.maxBufferedDeleteTerms != -1) && (this.deletesInRAM.size() + this.deletesFlushed.size() >= this.maxBufferedDeleteTerms));
/*      */   }
/*      */ 
/*      */   private synchronized boolean timeToFlushDeletes()
/*      */   {
/*  961 */     return ((this.bufferIsFull) || (deletesFull())) && (setFlushPending());
/*      */   }
/*      */ 
/*      */   void setMaxBufferedDeleteTerms(int maxBufferedDeleteTerms) {
/*  965 */     this.maxBufferedDeleteTerms = maxBufferedDeleteTerms;
/*      */   }
/*      */ 
/*      */   int getMaxBufferedDeleteTerms() {
/*  969 */     return this.maxBufferedDeleteTerms;
/*      */   }
/*      */ 
/*      */   synchronized boolean hasDeletes() {
/*  973 */     return this.deletesFlushed.any();
/*      */   }
/*      */ 
/*      */   synchronized boolean applyDeletes(SegmentInfos infos) throws IOException
/*      */   {
/*  978 */     if (!hasDeletes()) {
/*  979 */       return false;
/*      */     }
/*  981 */     if (this.infoStream != null) {
/*  982 */       message("apply " + this.deletesFlushed.numTerms + " buffered deleted terms and " + this.deletesFlushed.docIDs.size() + " deleted docIDs and " + this.deletesFlushed.queries.size() + " deleted queries on " + infos.size() + " segments.");
/*      */     }
/*      */ 
/*  987 */     int infosEnd = infos.size();
/*      */ 
/*  989 */     int docStart = 0;
/*  990 */     boolean any = false;
/*  991 */     for (int i = 0; i < infosEnd; i++)
/*      */     {
/*  995 */       assert (infos.info(i).dir == this.directory);
/*      */ 
/*  997 */       SegmentReader reader = this.writer.readerPool.get(infos.info(i), false);
/*      */       try {
/*  999 */         any |= applyDeletes(reader, docStart);
/* 1000 */         docStart += reader.maxDoc();
/*      */       } finally {
/* 1002 */         this.writer.readerPool.release(reader);
/*      */       }
/*      */     }
/*      */ 
/* 1006 */     this.deletesFlushed.clear();
/*      */ 
/* 1008 */     return any;
/*      */   }
/*      */ 
/*      */   private boolean checkDeleteTerm(Term term)
/*      */   {
/* 1016 */     if ((term != null) && 
/* 1017 */       (!$assertionsDisabled) && (this.lastDeleteTerm != null) && (term.compareTo(this.lastDeleteTerm) <= 0)) throw new AssertionError("lastTerm=" + this.lastDeleteTerm + " vs term=" + term);
/*      */ 
/* 1019 */     this.lastDeleteTerm = term;
/* 1020 */     return true;
/*      */   }
/*      */ 
/*      */   private final synchronized boolean applyDeletes(IndexReader reader, int docIDStart)
/*      */     throws CorruptIndexException, IOException
/*      */   {
/* 1028 */     int docEnd = docIDStart + reader.maxDoc();
/* 1029 */     boolean any = false;
/*      */ 
/* 1031 */     assert (checkDeleteTerm(null));
/*      */ 
/* 1034 */     TermDocs docs = reader.termDocs();
/*      */     try {
/* 1036 */       for (Map.Entry entry : this.deletesFlushed.terms.entrySet()) {
/* 1037 */         Term term = (Term)entry.getKey();
/*      */ 
/* 1040 */         assert (checkDeleteTerm(term));
/* 1041 */         docs.seek(term);
/* 1042 */         int limit = ((BufferedDeletes.Num)entry.getValue()).getNum();
/* 1043 */         while (docs.next()) {
/* 1044 */           int docID = docs.doc();
/* 1045 */           if (docIDStart + docID >= limit)
/*      */             break;
/* 1047 */           reader.deleteDocument(docID);
/* 1048 */           any = true;
/*      */         }
/*      */       }
/*      */     } finally {
/* 1052 */       docs.close();
/*      */     }
/*      */ 
/* 1056 */     for (Integer docIdInt : this.deletesFlushed.docIDs) {
/* 1057 */       int docID = docIdInt.intValue();
/* 1058 */       if ((docID >= docIDStart) && (docID < docEnd)) {
/* 1059 */         reader.deleteDocument(docID - docIDStart);
/* 1060 */         any = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1065 */     IndexSearcher searcher = new IndexSearcher(reader);
/* 1066 */     for (Map.Entry entry : this.deletesFlushed.queries.entrySet()) {
/* 1067 */       Query query = (Query)entry.getKey();
/* 1068 */       int limit = ((Integer)entry.getValue()).intValue();
/* 1069 */       Weight weight = query.weight(searcher);
/* 1070 */       Scorer scorer = weight.scorer(reader, true, false);
/* 1071 */       if (scorer != null) {
/*      */         while (true) {
/* 1073 */           int doc = scorer.nextDoc();
/* 1074 */           if (docIDStart + doc >= limit)
/*      */             break;
/* 1076 */           reader.deleteDocument(doc);
/* 1077 */           any = true;
/*      */         }
/*      */       }
/*      */     }
/* 1081 */     searcher.close();
/* 1082 */     return any;
/*      */   }
/*      */ 
/*      */   private synchronized void addDeleteTerm(Term term, int docCount)
/*      */   {
/* 1090 */     BufferedDeletes.Num num = (BufferedDeletes.Num)this.deletesInRAM.terms.get(term);
/* 1091 */     int docIDUpto = this.flushedDocCount + docCount;
/* 1092 */     if (num == null)
/* 1093 */       this.deletesInRAM.terms.put(term, new BufferedDeletes.Num(docIDUpto));
/*      */     else
/* 1095 */       num.setNum(docIDUpto);
/* 1096 */     this.deletesInRAM.numTerms += 1;
/*      */ 
/* 1098 */     this.deletesInRAM.addBytesUsed(BYTES_PER_DEL_TERM + term.text.length() * 2);
/*      */   }
/*      */ 
/*      */   private synchronized void addDeleteDocID(int docID)
/*      */   {
/* 1104 */     this.deletesInRAM.docIDs.add(Integer.valueOf(this.flushedDocCount + docID));
/* 1105 */     this.deletesInRAM.addBytesUsed(BYTES_PER_DEL_DOCID);
/*      */   }
/*      */ 
/*      */   private synchronized void addDeleteQuery(Query query, int docID) {
/* 1109 */     this.deletesInRAM.queries.put(query, Integer.valueOf(this.flushedDocCount + docID));
/* 1110 */     this.deletesInRAM.addBytesUsed(BYTES_PER_DEL_QUERY);
/*      */   }
/*      */ 
/*      */   synchronized boolean doBalanceRAM() {
/* 1114 */     return (this.ramBufferSize != -1L) && (!this.bufferIsFull) && ((this.numBytesUsed + this.deletesInRAM.bytesUsed + this.deletesFlushed.bytesUsed >= this.ramBufferSize) || (this.numBytesAlloc >= this.freeTrigger));
/*      */   }
/*      */ 
/*      */   private void finishDocument(DocumentsWriterThreadState perThread, DocWriter docWriter)
/*      */     throws IOException
/*      */   {
/* 1121 */     if (doBalanceRAM())
/*      */     {
/* 1124 */       balanceRAM();
/*      */     }
/* 1126 */     synchronized (this)
/*      */     {
/* 1128 */       assert ((docWriter == null) || (docWriter.docID == perThread.docState.docID));
/*      */ 
/* 1130 */       if (this.aborting)
/*      */       {
/* 1136 */         if (docWriter != null)
/*      */           try {
/* 1138 */             docWriter.abort();
/*      */           }
/*      */           catch (Throwable t) {
/*      */           }
/* 1142 */         perThread.isIdle = true;
/* 1143 */         notifyAll();
/*      */         return;
/*      */       }
/*      */       boolean doPause;
/*      */       boolean doPause;
/* 1149 */       if (docWriter != null) {
/* 1150 */         doPause = this.waitQueue.add(docWriter);
/*      */       } else {
/* 1152 */         this.skipDocWriter.docID = perThread.docState.docID;
/* 1153 */         doPause = this.waitQueue.add(this.skipDocWriter);
/*      */       }
/*      */ 
/* 1156 */       if (doPause) {
/* 1157 */         waitForWaitQueue();
/*      */       }
/* 1159 */       if ((this.bufferIsFull) && (!this.flushPending)) {
/* 1160 */         this.flushPending = true;
/* 1161 */         perThread.doFlushAfter = true;
/*      */       }
/*      */ 
/* 1164 */       perThread.isIdle = true;
/* 1165 */       notifyAll();
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void waitForWaitQueue() {
/*      */     do
/*      */       try {
/* 1172 */         wait();
/*      */       } catch (InterruptedException ie) {
/* 1174 */         throw new ThreadInterruptedException(ie);
/*      */       }
/* 1176 */     while (!this.waitQueue.doResume());
/*      */   }
/*      */ 
/*      */   long getRAMUsed()
/*      */   {
/* 1194 */     return this.numBytesUsed + this.deletesInRAM.bytesUsed + this.deletesFlushed.bytesUsed;
/*      */   }
/*      */ 
/*      */   synchronized int[] getIntBlock(boolean trackAllocations)
/*      */   {
/* 1304 */     int size = this.freeIntBlocks.size();
/*      */     int[] b;
/*      */     int[] b;
/* 1306 */     if (0 == size)
/*      */     {
/* 1313 */       this.numBytesAlloc += 32768L;
/* 1314 */       b = new int[8192];
/*      */     } else {
/* 1316 */       b = (int[])this.freeIntBlocks.remove(size - 1);
/* 1317 */     }if (trackAllocations)
/* 1318 */       this.numBytesUsed += 32768L;
/* 1319 */     assert (this.numBytesUsed <= this.numBytesAlloc);
/* 1320 */     return b;
/*      */   }
/*      */ 
/*      */   synchronized void bytesAllocated(long numBytes) {
/* 1324 */     this.numBytesAlloc += numBytes;
/*      */   }
/*      */ 
/*      */   synchronized void bytesUsed(long numBytes) {
/* 1328 */     this.numBytesUsed += numBytes;
/* 1329 */     assert (this.numBytesUsed <= this.numBytesAlloc);
/*      */   }
/*      */ 
/*      */   synchronized void recycleIntBlocks(int[][] blocks, int start, int end)
/*      */   {
/* 1334 */     for (int i = start; i < end; i++) {
/* 1335 */       this.freeIntBlocks.add(blocks[i]);
/* 1336 */       blocks[i] = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized char[] getCharBlock()
/*      */   {
/* 1359 */     int size = this.freeCharBlocks.size();
/*      */     char[] c;
/*      */     char[] c;
/* 1361 */     if (0 == size) {
/* 1362 */       this.numBytesAlloc += 32768L;
/* 1363 */       c = new char[16384];
/*      */     } else {
/* 1365 */       c = (char[])this.freeCharBlocks.remove(size - 1);
/*      */     }
/*      */ 
/* 1370 */     this.numBytesUsed += 32768L;
/* 1371 */     assert (this.numBytesUsed <= this.numBytesAlloc);
/* 1372 */     return c;
/*      */   }
/*      */ 
/*      */   synchronized void recycleCharBlocks(char[][] blocks, int numBlocks)
/*      */   {
/* 1377 */     for (int i = 0; i < numBlocks; i++) {
/* 1378 */       this.freeCharBlocks.add(blocks[i]);
/* 1379 */       blocks[i] = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   String toMB(long v) {
/* 1384 */     return this.nf.format(v / 1024.0D / 1024.0D);
/*      */   }
/*      */ 
/*      */   void balanceRAM()
/*      */   {
/* 1403 */     long flushTrigger = this.ramBufferSize;
/*      */ 
/* 1405 */     long deletesRAMUsed = this.deletesInRAM.bytesUsed + this.deletesFlushed.bytesUsed;
/*      */ 
/* 1407 */     if (this.numBytesAlloc + deletesRAMUsed > this.freeTrigger)
/*      */     {
/* 1409 */       if (this.infoStream != null) {
/* 1410 */         message("  RAM: now balance allocations: usedMB=" + toMB(this.numBytesUsed) + " vs trigger=" + toMB(flushTrigger) + " allocMB=" + toMB(this.numBytesAlloc) + " deletesMB=" + toMB(deletesRAMUsed) + " vs trigger=" + toMB(this.freeTrigger) + " byteBlockFree=" + toMB(this.byteBlockAllocator.freeByteBlocks.size() * 32768) + " perDocFree=" + toMB(this.perDocAllocator.freeByteBlocks.size() * 1024) + " charBlockFree=" + toMB(this.freeCharBlocks.size() * 16384 * 2));
/*      */       }
/*      */ 
/* 1419 */       long startBytesAlloc = this.numBytesAlloc + deletesRAMUsed;
/*      */ 
/* 1421 */       int iter = 0;
/*      */ 
/* 1427 */       boolean any = true;
/*      */ 
/* 1429 */       while (this.numBytesAlloc + deletesRAMUsed > this.freeLevel)
/*      */       {
/* 1431 */         synchronized (this) {
/* 1432 */           if ((0 == this.perDocAllocator.freeByteBlocks.size()) && (0 == this.byteBlockAllocator.freeByteBlocks.size()) && (0 == this.freeCharBlocks.size()) && (0 == this.freeIntBlocks.size()) && (!any))
/*      */           {
/* 1438 */             this.bufferIsFull = (this.numBytesUsed + deletesRAMUsed > flushTrigger);
/* 1439 */             if (this.infoStream != null) {
/* 1440 */               if (this.bufferIsFull)
/* 1441 */                 message("    nothing to free; now set bufferIsFull");
/*      */               else
/* 1443 */                 message("    nothing to free");
/*      */             }
/* 1445 */             assert (this.numBytesUsed <= this.numBytesAlloc);
/* 1446 */             break;
/*      */           }
/*      */ 
/* 1449 */           if ((0 == iter % 5) && (this.byteBlockAllocator.freeByteBlocks.size() > 0)) {
/* 1450 */             this.byteBlockAllocator.freeByteBlocks.remove(this.byteBlockAllocator.freeByteBlocks.size() - 1);
/* 1451 */             this.numBytesAlloc -= 32768L;
/*      */           }
/*      */ 
/* 1454 */           if ((1 == iter % 5) && (this.freeCharBlocks.size() > 0)) {
/* 1455 */             this.freeCharBlocks.remove(this.freeCharBlocks.size() - 1);
/* 1456 */             this.numBytesAlloc -= 32768L;
/*      */           }
/*      */ 
/* 1459 */           if ((2 == iter % 5) && (this.freeIntBlocks.size() > 0)) {
/* 1460 */             this.freeIntBlocks.remove(this.freeIntBlocks.size() - 1);
/* 1461 */             this.numBytesAlloc -= 32768L;
/*      */           }
/*      */ 
/* 1464 */           if ((3 == iter % 5) && (this.perDocAllocator.freeByteBlocks.size() > 0))
/*      */           {
/* 1466 */             for (int i = 0; i < 32; i++) {
/* 1467 */               this.perDocAllocator.freeByteBlocks.remove(this.perDocAllocator.freeByteBlocks.size() - 1);
/* 1468 */               this.numBytesAlloc -= 1024L;
/* 1469 */               if (this.perDocAllocator.freeByteBlocks.size() == 0)
/*      */               {
/*      */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1476 */         if ((4 == iter % 5) && (any))
/*      */         {
/* 1478 */           any = this.consumer.freeRAM();
/*      */         }
/* 1480 */         iter++;
/*      */       }
/*      */ 
/* 1483 */       if (this.infoStream != null) {
/* 1484 */         message("    after free: freedMB=" + this.nf.format((startBytesAlloc - this.numBytesAlloc - deletesRAMUsed) / 1024.0D / 1024.0D) + " usedMB=" + this.nf.format((this.numBytesUsed + deletesRAMUsed) / 1024.0D / 1024.0D) + " allocMB=" + this.nf.format(this.numBytesAlloc / 1024.0D / 1024.0D));
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1492 */       synchronized (this)
/*      */       {
/* 1494 */         if (this.numBytesUsed + deletesRAMUsed > flushTrigger) {
/* 1495 */           if (this.infoStream != null) {
/* 1496 */             message("  RAM: now flush @ usedMB=" + this.nf.format(this.numBytesUsed / 1024.0D / 1024.0D) + " allocMB=" + this.nf.format(this.numBytesAlloc / 1024.0D / 1024.0D) + " deletesMB=" + this.nf.format(deletesRAMUsed / 1024.0D / 1024.0D) + " triggerMB=" + this.nf.format(flushTrigger / 1024.0D / 1024.0D));
/*      */           }
/*      */ 
/* 1501 */           this.bufferIsFull = true;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class WaitQueue { DocumentsWriter.DocWriter[] waiting;
/*      */     int nextWriteDocID;
/*      */     int nextWriteLoc;
/*      */     int numWaiting;
/*      */     long waitingBytes;
/*      */ 
/* 1517 */     public WaitQueue() { this.waiting = new DocumentsWriter.DocWriter[10]; }
/*      */ 
/*      */ 
/*      */     synchronized void reset()
/*      */     {
/* 1522 */       assert (this.numWaiting == 0);
/* 1523 */       assert (this.waitingBytes == 0L);
/* 1524 */       this.nextWriteDocID = 0;
/*      */     }
/*      */ 
/*      */     synchronized boolean doResume() {
/* 1528 */       return this.waitingBytes <= DocumentsWriter.this.waitQueueResumeBytes;
/*      */     }
/*      */ 
/*      */     synchronized boolean doPause() {
/* 1532 */       return this.waitingBytes > DocumentsWriter.this.waitQueuePauseBytes;
/*      */     }
/*      */ 
/*      */     synchronized void abort() {
/* 1536 */       int count = 0;
/* 1537 */       for (int i = 0; i < this.waiting.length; i++) {
/* 1538 */         DocumentsWriter.DocWriter doc = this.waiting[i];
/* 1539 */         if (doc != null) {
/* 1540 */           doc.abort();
/* 1541 */           this.waiting[i] = null;
/* 1542 */           count++;
/*      */         }
/*      */       }
/* 1545 */       this.waitingBytes = 0L;
/* 1546 */       assert (count == this.numWaiting);
/* 1547 */       this.numWaiting = 0;
/*      */     }
/*      */ 
/*      */     private void writeDocument(DocumentsWriter.DocWriter doc) throws IOException {
/* 1551 */       assert ((doc == DocumentsWriter.this.skipDocWriter) || (this.nextWriteDocID == doc.docID));
/* 1552 */       boolean success = false;
/*      */       try {
/* 1554 */         doc.finish();
/* 1555 */         this.nextWriteDocID += 1;
/* 1556 */         DocumentsWriter.this.numDocsInStore += 1;
/* 1557 */         this.nextWriteLoc += 1;
/* 1558 */         assert (this.nextWriteLoc <= this.waiting.length);
/* 1559 */         if (this.nextWriteLoc == this.waiting.length)
/* 1560 */           this.nextWriteLoc = 0;
/* 1561 */         success = true;
/*      */       } finally {
/* 1563 */         if (!success)
/* 1564 */           DocumentsWriter.this.setAborting();
/*      */       }
/*      */     }
/*      */ 
/*      */     public synchronized boolean add(DocumentsWriter.DocWriter doc) throws IOException
/*      */     {
/* 1570 */       assert (doc.docID >= this.nextWriteDocID);
/*      */ 
/* 1572 */       if (doc.docID == this.nextWriteDocID) {
/* 1573 */         writeDocument(doc);
/*      */         while (true) {
/* 1575 */           doc = this.waiting[this.nextWriteLoc];
/* 1576 */           if (doc == null) break;
/* 1577 */           this.numWaiting -= 1;
/* 1578 */           this.waiting[this.nextWriteLoc] = null;
/* 1579 */           this.waitingBytes -= doc.sizeInBytes();
/* 1580 */           writeDocument(doc);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1592 */       int gap = doc.docID - this.nextWriteDocID;
/* 1593 */       if (gap >= this.waiting.length)
/*      */       {
/* 1595 */         DocumentsWriter.DocWriter[] newArray = new DocumentsWriter.DocWriter[ArrayUtil.getNextSize(gap)];
/* 1596 */         assert (this.nextWriteLoc >= 0);
/* 1597 */         System.arraycopy(this.waiting, this.nextWriteLoc, newArray, 0, this.waiting.length - this.nextWriteLoc);
/* 1598 */         System.arraycopy(this.waiting, 0, newArray, this.waiting.length - this.nextWriteLoc, this.nextWriteLoc);
/* 1599 */         this.nextWriteLoc = 0;
/* 1600 */         this.waiting = newArray;
/* 1601 */         gap = doc.docID - this.nextWriteDocID;
/*      */       }
/*      */ 
/* 1604 */       int loc = this.nextWriteLoc + gap;
/* 1605 */       if (loc >= this.waiting.length) {
/* 1606 */         loc -= this.waiting.length;
/*      */       }
/*      */ 
/* 1609 */       assert (loc < this.waiting.length);
/*      */ 
/* 1612 */       assert (this.waiting[loc] == null);
/* 1613 */       this.waiting[loc] = doc;
/* 1614 */       this.numWaiting += 1;
/* 1615 */       this.waitingBytes += doc.sizeInBytes();
/*      */ 
/* 1618 */       return doPause();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ByteBlockAllocator extends ByteBlockPool.Allocator
/*      */   {
/*      */     final int blockSize;
/* 1246 */     ArrayList<byte[]> freeByteBlocks = new ArrayList();
/*      */ 
/*      */     ByteBlockAllocator(int blockSize)
/*      */     {
/* 1243 */       this.blockSize = blockSize;
/*      */     }
/*      */ 
/*      */     byte[] getByteBlock(boolean trackAllocations)
/*      */     {
/* 1251 */       synchronized (DocumentsWriter.this) {
/* 1252 */         int size = this.freeByteBlocks.size();
/*      */         byte[] b;
/*      */         byte[] b;
/* 1254 */         if (0 == size)
/*      */         {
/* 1261 */           DocumentsWriter.this.numBytesAlloc += this.blockSize;
/* 1262 */           b = new byte[this.blockSize];
/*      */         } else {
/* 1264 */           b = (byte[])this.freeByteBlocks.remove(size - 1);
/* 1265 */         }if (trackAllocations)
/* 1266 */           DocumentsWriter.this.numBytesUsed += this.blockSize;
/* 1267 */         assert (DocumentsWriter.this.numBytesUsed <= DocumentsWriter.this.numBytesAlloc);
/* 1268 */         return b;
/*      */       }
/*      */     }
/*      */ 
/*      */     void recycleByteBlocks(byte[][] blocks, int start, int end)
/*      */     {
/* 1276 */       synchronized (DocumentsWriter.this) {
/* 1277 */         for (int i = start; i < end; i++) {
/* 1278 */           this.freeByteBlocks.add(blocks[i]);
/* 1279 */           blocks[i] = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void recycleByteBlocks(List<byte[]> blocks)
/*      */     {
/* 1286 */       synchronized (DocumentsWriter.this) {
/* 1287 */         int size = blocks.size();
/* 1288 */         for (int i = 0; i < size; i++)
/* 1289 */           this.freeByteBlocks.add(blocks.get(i));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class SkipDocWriter extends DocumentsWriter.DocWriter
/*      */   {
/*      */     void finish()
/*      */     {
/*      */     }
/*      */ 
/*      */     void abort()
/*      */     {
/*      */     }
/*      */ 
/*      */     long sizeInBytes()
/*      */     {
/* 1188 */       return 0L;
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class IndexingChain
/*      */   {
/*      */     abstract DocConsumer getChain(DocumentsWriter paramDocumentsWriter);
/*      */   }
/*      */ 
/*      */   class PerDocBuffer extends RAMFile
/*      */   {
/*      */     PerDocBuffer()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected byte[] newBuffer(int size)
/*      */     {
/*  199 */       assert (size == 1024);
/*  200 */       return DocumentsWriter.this.perDocAllocator.getByteBlock(false);
/*      */     }
/*      */ 
/*      */     synchronized void recycle()
/*      */     {
/*  207 */       if (this.buffers.size() > 0) {
/*  208 */         setLength(0L);
/*      */ 
/*  211 */         DocumentsWriter.this.perDocAllocator.recycleByteBlocks(this.buffers);
/*  212 */         this.buffers.clear();
/*  213 */         this.sizeInBytes = 0L;
/*      */ 
/*  215 */         assert (numBuffers() == 0);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class DocWriter
/*      */   {
/*      */     DocWriter next;
/*      */     int docID;
/*      */ 
/*      */     abstract void finish()
/*      */       throws IOException;
/*      */ 
/*      */     abstract void abort();
/*      */ 
/*      */     abstract long sizeInBytes();
/*      */ 
/*      */     void setNext(DocWriter next)
/*      */     {
/*  179 */       this.next = next;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class DocState
/*      */   {
/*      */     DocumentsWriter docWriter;
/*      */     Analyzer analyzer;
/*      */     int maxFieldLength;
/*      */     PrintStream infoStream;
/*      */     Similarity similarity;
/*      */     int docID;
/*      */     Document doc;
/*      */     String maxTermPrefix;
/*      */ 
/*      */     public boolean testPoint(String name)
/*      */     {
/*  157 */       return this.docWriter.writer.testPoint(name);
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/*  163 */       this.doc = null;
/*  164 */       this.analyzer = null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocumentsWriter
 * JD-Core Version:    0.6.2
 */