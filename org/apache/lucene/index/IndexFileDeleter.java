/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.lucene.store.Directory;
/*     */ 
/*     */ final class IndexFileDeleter
/*     */ {
/*     */   private List<String> deletable;
/*  80 */   private Map<String, RefCount> refCounts = new HashMap();
/*     */ 
/*  87 */   private List<CommitPoint> commits = new ArrayList();
/*     */ 
/*  91 */   private List<Collection<String>> lastFiles = new ArrayList();
/*     */ 
/*  94 */   private List<CommitPoint> commitsToDelete = new ArrayList();
/*     */   private PrintStream infoStream;
/*     */   private Directory directory;
/*     */   private IndexDeletionPolicy policy;
/*     */   private DocumentsWriter docWriter;
/*     */   final boolean startingCommitDeleted;
/* 105 */   public static boolean VERBOSE_REF_COUNTS = false;
/*     */ 
/*     */   void setInfoStream(PrintStream infoStream) {
/* 108 */     this.infoStream = infoStream;
/* 109 */     if (infoStream != null)
/* 110 */       message("setInfoStream deletionPolicy=" + this.policy);
/*     */   }
/*     */ 
/*     */   private void message(String message) {
/* 114 */     this.infoStream.println("IFD [" + Thread.currentThread().getName() + "]: " + message);
/*     */   }
/*     */ 
/*     */   public IndexFileDeleter(Directory directory, IndexDeletionPolicy policy, SegmentInfos segmentInfos, PrintStream infoStream, DocumentsWriter docWriter)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 128 */     this.docWriter = docWriter;
/* 129 */     this.infoStream = infoStream;
/*     */ 
/* 131 */     if (infoStream != null) {
/* 132 */       message("init: current segments file is \"" + segmentInfos.getCurrentSegmentFileName() + "\"; deletionPolicy=" + policy);
/*     */     }
/* 134 */     this.policy = policy;
/* 135 */     this.directory = directory;
/*     */ 
/* 139 */     long currentGen = segmentInfos.getGeneration();
/* 140 */     IndexFileNameFilter filter = IndexFileNameFilter.getFilter();
/*     */ 
/* 142 */     String[] files = directory.listAll();
/*     */ 
/* 144 */     CommitPoint currentCommitPoint = null;
/*     */ 
/* 146 */     for (int i = 0; i < files.length; i++)
/*     */     {
/* 148 */       String fileName = files[i];
/*     */ 
/* 150 */       if ((filter.accept(null, fileName)) && (!fileName.equals("segments.gen")))
/*     */       {
/* 153 */         getRefCount(fileName);
/*     */ 
/* 155 */         if (fileName.startsWith("segments"))
/*     */         {
/* 160 */           if (SegmentInfos.generationFromSegmentsFileName(fileName) <= currentGen) {
/* 161 */             if (infoStream != null) {
/* 162 */               message("init: load commit \"" + fileName + "\"");
/*     */             }
/* 164 */             SegmentInfos sis = new SegmentInfos();
/*     */             try {
/* 166 */               sis.read(directory, fileName);
/*     */             }
/*     */             catch (FileNotFoundException e)
/*     */             {
/* 175 */               if (infoStream != null) {
/* 176 */                 message("init: hit FileNotFoundException when loading commit \"" + fileName + "\"; skipping this commit point");
/*     */               }
/* 178 */               sis = null;
/*     */             }
/* 180 */             if (sis != null) {
/* 181 */               CommitPoint commitPoint = new CommitPoint(this.commitsToDelete, directory, sis);
/* 182 */               if (sis.getGeneration() == segmentInfos.getGeneration()) {
/* 183 */                 currentCommitPoint = commitPoint;
/*     */               }
/* 185 */               this.commits.add(commitPoint);
/* 186 */               incRef(sis, true);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 193 */     if (currentCommitPoint == null)
/*     */     {
/* 201 */       SegmentInfos sis = new SegmentInfos();
/*     */       try {
/* 203 */         sis.read(directory, segmentInfos.getCurrentSegmentFileName());
/*     */       } catch (IOException e) {
/* 205 */         throw new CorruptIndexException("failed to locate current segments_N file");
/*     */       }
/* 207 */       if (infoStream != null)
/* 208 */         message("forced open of current segments file " + segmentInfos.getCurrentSegmentFileName());
/* 209 */       currentCommitPoint = new CommitPoint(this.commitsToDelete, directory, sis);
/* 210 */       this.commits.add(currentCommitPoint);
/* 211 */       incRef(sis, true);
/*     */     }
/*     */ 
/* 215 */     Collections.sort(this.commits);
/*     */ 
/* 220 */     for (Map.Entry entry : this.refCounts.entrySet()) {
/* 221 */       RefCount rc = (RefCount)entry.getValue();
/* 222 */       String fileName = (String)entry.getKey();
/* 223 */       if (0 == rc.count) {
/* 224 */         if (infoStream != null) {
/* 225 */           message("init: removing unreferenced file \"" + fileName + "\"");
/*     */         }
/* 227 */         deleteFile(fileName);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 233 */     policy.onInit(this.commits);
/*     */ 
/* 237 */     checkpoint(segmentInfos, false);
/*     */ 
/* 239 */     this.startingCommitDeleted = currentCommitPoint.isDeleted();
/*     */ 
/* 241 */     deleteCommits();
/*     */   }
/*     */ 
/*     */   private void deleteCommits()
/*     */     throws IOException
/*     */   {
/* 250 */     int size = this.commitsToDelete.size();
/*     */ 
/* 252 */     if (size > 0)
/*     */     {
/* 256 */       for (int i = 0; i < size; i++) {
/* 257 */         CommitPoint commit = (CommitPoint)this.commitsToDelete.get(i);
/* 258 */         if (this.infoStream != null) {
/* 259 */           message("deleteCommits: now decRef commit \"" + commit.getSegmentsFileName() + "\"");
/*     */         }
/* 261 */         for (String file : commit.files) {
/* 262 */           decRef(file);
/*     */         }
/*     */       }
/* 265 */       this.commitsToDelete.clear();
/*     */ 
/* 268 */       size = this.commits.size();
/* 269 */       int readFrom = 0;
/* 270 */       int writeTo = 0;
/* 271 */       while (readFrom < size) {
/* 272 */         CommitPoint commit = (CommitPoint)this.commits.get(readFrom);
/* 273 */         if (!commit.deleted) {
/* 274 */           if (writeTo != readFrom) {
/* 275 */             this.commits.set(writeTo, this.commits.get(readFrom));
/*     */           }
/* 277 */           writeTo++;
/*     */         }
/* 279 */         readFrom++;
/*     */       }
/*     */ 
/* 282 */       while (size > writeTo) {
/* 283 */         this.commits.remove(size - 1);
/* 284 */         size--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void refresh(String segmentName)
/*     */     throws IOException
/*     */   {
/* 298 */     String[] files = this.directory.listAll();
/* 299 */     IndexFileNameFilter filter = IndexFileNameFilter.getFilter();
/*     */     String segmentPrefix2;
/*     */     String segmentPrefix1;
/*     */     String segmentPrefix2;
/* 302 */     if (segmentName != null) {
/* 303 */       String segmentPrefix1 = segmentName + ".";
/* 304 */       segmentPrefix2 = segmentName + "_";
/*     */     } else {
/* 306 */       segmentPrefix1 = null;
/* 307 */       segmentPrefix2 = null;
/*     */     }
/*     */ 
/* 310 */     for (int i = 0; i < files.length; i++) {
/* 311 */       String fileName = files[i];
/* 312 */       if ((filter.accept(null, fileName)) && ((segmentName == null) || (fileName.startsWith(segmentPrefix1)) || (fileName.startsWith(segmentPrefix2))) && (!this.refCounts.containsKey(fileName)) && (!fileName.equals("segments.gen")))
/*     */       {
/* 317 */         if (this.infoStream != null) {
/* 318 */           message("refresh [prefix=" + segmentName + "]: removing newly created unreferenced file \"" + fileName + "\"");
/*     */         }
/* 320 */         deleteFile(fileName);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void refresh() throws IOException {
/* 326 */     refresh(null);
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/* 331 */     int size = this.lastFiles.size();
/* 332 */     if (size > 0) {
/* 333 */       for (int i = 0; i < size; i++)
/* 334 */         decRef((Collection)this.lastFiles.get(i));
/* 335 */       this.lastFiles.clear();
/*     */     }
/*     */ 
/* 338 */     deletePendingFiles();
/*     */   }
/*     */ 
/*     */   private void deletePendingFiles() throws IOException {
/* 342 */     if (this.deletable != null) {
/* 343 */       List oldDeletable = this.deletable;
/* 344 */       this.deletable = null;
/* 345 */       int size = oldDeletable.size();
/* 346 */       for (int i = 0; i < size; i++) {
/* 347 */         if (this.infoStream != null)
/* 348 */           message("delete pending file " + (String)oldDeletable.get(i));
/* 349 */         deleteFile((String)oldDeletable.get(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkpoint(SegmentInfos segmentInfos, boolean isCommit)
/*     */     throws IOException
/*     */   {
/* 376 */     if (this.infoStream != null) {
/* 377 */       message("now checkpoint \"" + segmentInfos.getCurrentSegmentFileName() + "\" [" + segmentInfos.size() + " segments " + "; isCommit = " + isCommit + "]");
/*     */     }
/*     */ 
/* 382 */     deletePendingFiles();
/*     */ 
/* 385 */     incRef(segmentInfos, isCommit);
/*     */ 
/* 387 */     if (isCommit)
/*     */     {
/* 389 */       this.commits.add(new CommitPoint(this.commitsToDelete, this.directory, segmentInfos));
/*     */ 
/* 392 */       this.policy.onCommit(this.commits);
/*     */ 
/* 395 */       deleteCommits();
/*     */     }
/*     */     else
/*     */     {
/*     */       List docWriterFiles;
/* 399 */       if (this.docWriter != null) {
/* 400 */         List docWriterFiles = this.docWriter.openFiles();
/* 401 */         if (docWriterFiles != null)
/*     */         {
/* 405 */           incRef(docWriterFiles);
/*     */         }
/*     */       } else { docWriterFiles = null; }
/*     */ 
/*     */ 
/* 410 */       int size = this.lastFiles.size();
/* 411 */       if (size > 0) {
/* 412 */         for (int i = 0; i < size; i++)
/* 413 */           decRef((Collection)this.lastFiles.get(i));
/* 414 */         this.lastFiles.clear();
/*     */       }
/*     */ 
/* 418 */       this.lastFiles.add(segmentInfos.files(this.directory, false));
/*     */ 
/* 420 */       if (docWriterFiles != null)
/* 421 */         this.lastFiles.add(docWriterFiles);
/*     */     }
/*     */   }
/*     */ 
/*     */   void incRef(SegmentInfos segmentInfos, boolean isCommit)
/*     */     throws IOException
/*     */   {
/* 428 */     for (String fileName : segmentInfos.files(this.directory, isCommit))
/* 429 */       incRef(fileName);
/*     */   }
/*     */ 
/*     */   void incRef(Collection<String> files) throws IOException
/*     */   {
/* 434 */     for (String file : files)
/* 435 */       incRef(file);
/*     */   }
/*     */ 
/*     */   void incRef(String fileName) throws IOException
/*     */   {
/* 440 */     RefCount rc = getRefCount(fileName);
/* 441 */     if ((this.infoStream != null) && (VERBOSE_REF_COUNTS)) {
/* 442 */       message("  IncRef \"" + fileName + "\": pre-incr count is " + rc.count);
/*     */     }
/* 444 */     rc.IncRef();
/*     */   }
/*     */ 
/*     */   void decRef(Collection<String> files) throws IOException {
/* 448 */     for (String file : files)
/* 449 */       decRef(file);
/*     */   }
/*     */ 
/*     */   void decRef(String fileName) throws IOException
/*     */   {
/* 454 */     RefCount rc = getRefCount(fileName);
/* 455 */     if ((this.infoStream != null) && (VERBOSE_REF_COUNTS)) {
/* 456 */       message("  DecRef \"" + fileName + "\": pre-decr count is " + rc.count);
/*     */     }
/* 458 */     if (0 == rc.DecRef())
/*     */     {
/* 461 */       deleteFile(fileName);
/* 462 */       this.refCounts.remove(fileName);
/*     */     }
/*     */   }
/*     */ 
/*     */   void decRef(SegmentInfos segmentInfos) throws IOException {
/* 467 */     for (String file : segmentInfos.files(this.directory, false))
/* 468 */       decRef(file);
/*     */   }
/*     */ 
/*     */   private RefCount getRefCount(String fileName)
/*     */   {
/*     */     RefCount rc;
/* 474 */     if (!this.refCounts.containsKey(fileName)) {
/* 475 */       RefCount rc = new RefCount(fileName);
/* 476 */       this.refCounts.put(fileName, rc);
/*     */     } else {
/* 478 */       rc = (RefCount)this.refCounts.get(fileName);
/*     */     }
/* 480 */     return rc;
/*     */   }
/*     */ 
/*     */   void deleteFiles(List<String> files) throws IOException {
/* 484 */     for (String file : files)
/* 485 */       deleteFile(file);
/*     */   }
/*     */ 
/*     */   void deleteNewFiles(Collection<String> files)
/*     */     throws IOException
/*     */   {
/* 491 */     for (String fileName : files)
/* 492 */       if (!this.refCounts.containsKey(fileName))
/* 493 */         deleteFile(fileName);
/*     */   }
/*     */ 
/*     */   void deleteFile(String fileName) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 500 */       if (this.infoStream != null) {
/* 501 */         message("delete \"" + fileName + "\"");
/*     */       }
/* 503 */       this.directory.deleteFile(fileName);
/*     */     } catch (IOException e) {
/* 505 */       if (this.directory.fileExists(fileName))
/*     */       {
/* 514 */         if (this.infoStream != null) {
/* 515 */           message("IndexFileDeleter: unable to remove file \"" + fileName + "\": " + e.toString() + "; Will re-try later.");
/*     */         }
/* 517 */         if (this.deletable == null) {
/* 518 */           this.deletable = new ArrayList();
/*     */         }
/* 520 */         this.deletable.add(fileName);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class CommitPoint extends IndexCommit
/*     */     implements Comparable<CommitPoint>
/*     */   {
/*     */     long gen;
/*     */     Collection<String> files;
/*     */     String segmentsFileName;
/*     */     boolean deleted;
/*     */     Directory directory;
/*     */     Collection<CommitPoint> commitsToDelete;
/*     */     long version;
/*     */     long generation;
/*     */     final boolean isOptimized;
/*     */     final Map<String, String> userData;
/*     */ 
/*     */     public CommitPoint(Collection<CommitPoint> commitsToDelete, Directory directory, SegmentInfos segmentInfos)
/*     */       throws IOException
/*     */     {
/* 575 */       this.directory = directory;
/* 576 */       this.commitsToDelete = commitsToDelete;
/* 577 */       this.userData = segmentInfos.getUserData();
/* 578 */       this.segmentsFileName = segmentInfos.getCurrentSegmentFileName();
/* 579 */       this.version = segmentInfos.getVersion();
/* 580 */       this.generation = segmentInfos.getGeneration();
/* 581 */       this.files = Collections.unmodifiableCollection(segmentInfos.files(directory, true));
/* 582 */       this.gen = segmentInfos.getGeneration();
/* 583 */       this.isOptimized = ((segmentInfos.size() == 1) && (!segmentInfos.info(0).hasDeletions()));
/*     */ 
/* 585 */       assert (!segmentInfos.hasExternalSegments(directory));
/*     */     }
/*     */ 
/*     */     public boolean isOptimized()
/*     */     {
/* 590 */       return this.isOptimized;
/*     */     }
/*     */ 
/*     */     public String getSegmentsFileName()
/*     */     {
/* 595 */       return this.segmentsFileName;
/*     */     }
/*     */ 
/*     */     public Collection<String> getFileNames() throws IOException
/*     */     {
/* 600 */       return this.files;
/*     */     }
/*     */ 
/*     */     public Directory getDirectory()
/*     */     {
/* 605 */       return this.directory;
/*     */     }
/*     */ 
/*     */     public long getVersion()
/*     */     {
/* 610 */       return this.version;
/*     */     }
/*     */ 
/*     */     public long getGeneration()
/*     */     {
/* 615 */       return this.generation;
/*     */     }
/*     */ 
/*     */     public Map<String, String> getUserData()
/*     */     {
/* 620 */       return this.userData;
/*     */     }
/*     */ 
/*     */     public void delete()
/*     */     {
/* 629 */       if (!this.deleted) {
/* 630 */         this.deleted = true;
/* 631 */         this.commitsToDelete.add(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isDeleted()
/*     */     {
/* 637 */       return this.deleted;
/*     */     }
/*     */ 
/*     */     public int compareTo(CommitPoint commit) {
/* 641 */       if (this.gen < commit.gen)
/* 642 */         return -1;
/* 643 */       if (this.gen > commit.gen) {
/* 644 */         return 1;
/*     */       }
/* 646 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class RefCount
/*     */   {
/*     */     final String fileName;
/*     */     boolean initDone;
/*     */     int count;
/*     */ 
/*     */     RefCount(String fileName)
/*     */     {
/* 534 */       this.fileName = fileName;
/*     */     }
/*     */ 
/*     */     public int IncRef()
/*     */     {
/* 540 */       if (!this.initDone)
/* 541 */         this.initDone = true;
/*     */       else {
/* 543 */         assert (this.count > 0) : ("RefCount is 0 pre-increment for file \"" + this.fileName + "\"");
/*     */       }
/* 545 */       return ++this.count;
/*     */     }
/*     */ 
/*     */     public int DecRef() {
/* 549 */       assert (this.count > 0) : ("RefCount is 0 pre-decrement for file \"" + this.fileName + "\"");
/* 550 */       return --this.count;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexFileDeleter
 * JD-Core Version:    0.6.2
 */