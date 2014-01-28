/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.lucene.document.Document;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.FSDirectory;
/*     */ import org.apache.lucene.store.IndexInput;
/*     */ import org.apache.lucene.util.BitVector;
/*     */ 
/*     */ public class CheckIndex
/*     */ {
/*     */   private PrintStream infoStream;
/*     */   private Directory dir;
/*     */   private static boolean assertsOn;
/*     */ 
/*     */   public CheckIndex(Directory dir)
/*     */   {
/* 254 */     this.dir = dir;
/* 255 */     this.infoStream = null;
/*     */   }
/*     */ 
/*     */   public void setInfoStream(PrintStream out)
/*     */   {
/* 261 */     this.infoStream = out;
/*     */   }
/*     */ 
/*     */   private void msg(String msg) {
/* 265 */     if (this.infoStream != null)
/* 266 */       this.infoStream.println(msg);
/*     */   }
/*     */ 
/*     */   public Status checkIndex()
/*     */     throws IOException
/*     */   {
/* 299 */     return checkIndex(null);
/*     */   }
/*     */ 
/*     */   public Status checkIndex(List<String> onlySegments)
/*     */     throws IOException
/*     */   {
/* 315 */     NumberFormat nf = NumberFormat.getInstance();
/* 316 */     SegmentInfos sis = new SegmentInfos();
/* 317 */     Status result = new Status();
/* 318 */     result.dir = this.dir;
/*     */     try {
/* 320 */       sis.read(this.dir);
/*     */     } catch (Throwable t) {
/* 322 */       msg("ERROR: could not read any segments file in directory");
/* 323 */       result.missingSegments = true;
/* 324 */       if (this.infoStream != null)
/* 325 */         t.printStackTrace(this.infoStream);
/* 326 */       return result;
/*     */     }
/*     */ 
/* 329 */     int numSegments = sis.size();
/* 330 */     String segmentsFileName = sis.getCurrentSegmentFileName();
/* 331 */     IndexInput input = null;
/*     */     try {
/* 333 */       input = this.dir.openInput(segmentsFileName);
/*     */     } catch (Throwable t) {
/* 335 */       msg("ERROR: could not open segments file in directory");
/* 336 */       if (this.infoStream != null)
/* 337 */         t.printStackTrace(this.infoStream);
/* 338 */       result.cantOpenSegments = true;
/* 339 */       return result;
/*     */     }
/* 341 */     int format = 0;
/*     */     try {
/* 343 */       format = input.readInt();
/*     */     } catch (Throwable t) {
/* 345 */       msg("ERROR: could not read segment file version in directory");
/* 346 */       if (this.infoStream != null)
/* 347 */         t.printStackTrace(this.infoStream);
/* 348 */       result.missingSegmentVersion = true;
/* 349 */       return result;
/*     */     } finally {
/* 351 */       if (input != null) {
/* 352 */         input.close();
/*     */       }
/*     */     }
/* 355 */     String sFormat = "";
/* 356 */     boolean skip = false;
/*     */ 
/* 358 */     if (format == -1)
/* 359 */       sFormat = "FORMAT [Lucene Pre-2.1]";
/* 360 */     if (format == -2) {
/* 361 */       sFormat = "FORMAT_LOCKLESS [Lucene 2.1]";
/* 362 */     } else if (format == -3) {
/* 363 */       sFormat = "FORMAT_SINGLE_NORM_FILE [Lucene 2.2]";
/* 364 */     } else if (format == -4) {
/* 365 */       sFormat = "FORMAT_SHARED_DOC_STORE [Lucene 2.3]";
/*     */     }
/* 367 */     else if (format == -5) {
/* 368 */       sFormat = "FORMAT_CHECKSUM [Lucene 2.4]";
/* 369 */     } else if (format == -6) {
/* 370 */       sFormat = "FORMAT_DEL_COUNT [Lucene 2.4]";
/* 371 */     } else if (format == -7) {
/* 372 */       sFormat = "FORMAT_HAS_PROX [Lucene 2.4]";
/* 373 */     } else if (format == -8) {
/* 374 */       sFormat = "FORMAT_USER_DATA [Lucene 2.9]";
/* 375 */     } else if (format == -9) {
/* 376 */       sFormat = "FORMAT_DIAGNOSTICS [Lucene 2.9]";
/* 377 */     } else if (format < -9) {
/* 378 */       sFormat = "int=" + format + " [newer version of Lucene than this tool]";
/* 379 */       skip = true;
/*     */     } else {
/* 381 */       sFormat = format + " [Lucene 1.3 or prior]";
/*     */     }
/*     */ 
/* 385 */     result.segmentsFileName = segmentsFileName;
/* 386 */     result.numSegments = numSegments;
/* 387 */     result.segmentFormat = sFormat;
/* 388 */     result.userData = sis.getUserData();
/*     */     String userDataString;
/*     */     String userDataString;
/* 390 */     if (sis.getUserData().size() > 0)
/* 391 */       userDataString = " userData=" + sis.getUserData();
/*     */     else {
/* 393 */       userDataString = "";
/*     */     }
/*     */ 
/* 396 */     msg("Segments file=" + segmentsFileName + " numSegments=" + numSegments + " version=" + sFormat + userDataString);
/*     */ 
/* 398 */     if (onlySegments != null) {
/* 399 */       result.partial = true;
/* 400 */       if (this.infoStream != null)
/* 401 */         this.infoStream.print("\nChecking only these segments:");
/* 402 */       for (String s : onlySegments) {
/* 403 */         if (this.infoStream != null)
/* 404 */           this.infoStream.print(" " + s);
/*     */       }
/* 406 */       result.segmentsChecked.addAll(onlySegments);
/* 407 */       msg(":");
/*     */     }
/*     */ 
/* 410 */     if (skip) {
/* 411 */       msg("\nERROR: this index appears to be created by a newer version of Lucene than this tool was compiled on; please re-compile this tool on the matching version of Lucene; exiting");
/* 412 */       result.toolOutOfDate = true;
/* 413 */       return result;
/*     */     }
/*     */ 
/* 417 */     result.newSegments = ((SegmentInfos)sis.clone());
/* 418 */     result.newSegments.clear();
/*     */ 
/* 420 */     for (int i = 0; i < numSegments; i++) {
/* 421 */       SegmentInfo info = sis.info(i);
/* 422 */       if ((onlySegments == null) || (onlySegments.contains(info.name)))
/*     */       {
/* 424 */         CheckIndex.Status.SegmentInfoStatus segInfoStat = new CheckIndex.Status.SegmentInfoStatus();
/* 425 */         result.segmentInfos.add(segInfoStat);
/* 426 */         msg("  " + (1 + i) + " of " + numSegments + ": name=" + info.name + " docCount=" + info.docCount);
/* 427 */         segInfoStat.name = info.name;
/* 428 */         segInfoStat.docCount = info.docCount;
/*     */ 
/* 430 */         int toLoseDocCount = info.docCount;
/*     */ 
/* 432 */         SegmentReader reader = null;
/*     */         try
/*     */         {
/* 435 */           msg("    compound=" + info.getUseCompoundFile());
/* 436 */           segInfoStat.compound = info.getUseCompoundFile();
/* 437 */           msg("    hasProx=" + info.getHasProx());
/* 438 */           segInfoStat.hasProx = info.getHasProx();
/* 439 */           msg("    numFiles=" + info.files().size());
/* 440 */           segInfoStat.numFiles = info.files().size();
/* 441 */           msg("    size (MB)=" + nf.format(info.sizeInBytes() / 1048576.0D));
/* 442 */           segInfoStat.sizeMB = (info.sizeInBytes() / 1048576.0D);
/* 443 */           Map diagnostics = info.getDiagnostics();
/* 444 */           segInfoStat.diagnostics = diagnostics;
/* 445 */           if (diagnostics.size() > 0) {
/* 446 */             msg("    diagnostics = " + diagnostics);
/*     */           }
/*     */ 
/* 449 */           int docStoreOffset = info.getDocStoreOffset();
/* 450 */           if (docStoreOffset != -1) {
/* 451 */             msg("    docStoreOffset=" + docStoreOffset);
/* 452 */             segInfoStat.docStoreOffset = docStoreOffset;
/* 453 */             msg("    docStoreSegment=" + info.getDocStoreSegment());
/* 454 */             segInfoStat.docStoreSegment = info.getDocStoreSegment();
/* 455 */             msg("    docStoreIsCompoundFile=" + info.getDocStoreIsCompoundFile());
/* 456 */             segInfoStat.docStoreCompoundFile = info.getDocStoreIsCompoundFile();
/*     */           }
/* 458 */           String delFileName = info.getDelFileName();
/* 459 */           if (delFileName == null) {
/* 460 */             msg("    no deletions");
/* 461 */             segInfoStat.hasDeletions = false;
/*     */           }
/*     */           else {
/* 464 */             msg("    has deletions [delFileName=" + delFileName + "]");
/* 465 */             segInfoStat.hasDeletions = true;
/* 466 */             segInfoStat.deletionsFileName = delFileName;
/*     */           }
/* 468 */           if (this.infoStream != null)
/* 469 */             this.infoStream.print("    test: open reader.........");
/* 470 */           reader = SegmentReader.get(true, info, IndexReader.DEFAULT_TERMS_INDEX_DIVISOR);
/*     */ 
/* 472 */           segInfoStat.openReaderPassed = true;
/*     */ 
/* 474 */           int numDocs = reader.numDocs();
/* 475 */           toLoseDocCount = numDocs;
/* 476 */           if (reader.hasDeletions()) {
/* 477 */             if (reader.deletedDocs.count() != info.getDelCount()) {
/* 478 */               throw new RuntimeException("delete count mismatch: info=" + info.getDelCount() + " vs deletedDocs.count()=" + reader.deletedDocs.count());
/*     */             }
/* 480 */             if (reader.deletedDocs.count() > reader.maxDoc()) {
/* 481 */               throw new RuntimeException("too many deleted docs: maxDoc()=" + reader.maxDoc() + " vs deletedDocs.count()=" + reader.deletedDocs.count());
/*     */             }
/* 483 */             if (info.docCount - numDocs != info.getDelCount()) {
/* 484 */               throw new RuntimeException("delete count mismatch: info=" + info.getDelCount() + " vs reader=" + (info.docCount - numDocs));
/*     */             }
/* 486 */             segInfoStat.numDeleted = (info.docCount - numDocs);
/* 487 */             msg("OK [" + segInfoStat.numDeleted + " deleted docs]");
/*     */           } else {
/* 489 */             if (info.getDelCount() != 0) {
/* 490 */               throw new RuntimeException("delete count mismatch: info=" + info.getDelCount() + " vs reader=" + (info.docCount - numDocs));
/*     */             }
/* 492 */             msg("OK");
/*     */           }
/* 494 */           if (reader.maxDoc() != info.docCount) {
/* 495 */             throw new RuntimeException("SegmentReader.maxDoc() " + reader.maxDoc() + " != SegmentInfos.docCount " + info.docCount);
/*     */           }
/*     */ 
/* 498 */           if (this.infoStream != null) {
/* 499 */             this.infoStream.print("    test: fields..............");
/*     */           }
/* 501 */           Collection fieldNames = reader.getFieldNames(IndexReader.FieldOption.ALL);
/* 502 */           msg("OK [" + fieldNames.size() + " fields]");
/* 503 */           segInfoStat.numFields = fieldNames.size();
/*     */ 
/* 506 */           segInfoStat.fieldNormStatus = testFieldNorms(fieldNames, reader);
/*     */ 
/* 509 */           segInfoStat.termIndexStatus = testTermIndex(info, reader);
/*     */ 
/* 512 */           segInfoStat.storedFieldStatus = testStoredFields(info, reader, nf);
/*     */ 
/* 515 */           segInfoStat.termVectorStatus = testTermVectors(info, reader, nf);
/*     */ 
/* 519 */           if (segInfoStat.fieldNormStatus.error != null)
/* 520 */             throw new RuntimeException("Field Norm test failed");
/* 521 */           if (segInfoStat.termIndexStatus.error != null)
/* 522 */             throw new RuntimeException("Term Index test failed");
/* 523 */           if (segInfoStat.storedFieldStatus.error != null)
/* 524 */             throw new RuntimeException("Stored Field test failed");
/* 525 */           if (segInfoStat.termVectorStatus.error != null) {
/* 526 */             throw new RuntimeException("Term Vector test failed");
/*     */           }
/*     */ 
/* 529 */           msg("");
/*     */ 
/* 531 */           jsr 106; } catch (Throwable t) {
/* 532 */           msg("FAILED");
/*     */ 
/* 534 */           String comment = "fixIndex() would remove reference to this segment";
/* 535 */           msg("    WARNING: " + comment + "; full exception:");
/* 536 */           if (this.infoStream != null)
/* 537 */             t.printStackTrace(this.infoStream);
/* 538 */           msg("");
/* 539 */           result.totLoseDocCount += toLoseDocCount;
/* 540 */           result.numBadSegments += 1;
/*     */         }
/*     */         finally {
/* 543 */           jsr 6; } if (reader != null)
/* 544 */           reader.close(); ret;
/*     */ 
/* 548 */         result.newSegments.add((SegmentInfo)info.clone());
/*     */       }
/*     */     }
/* 551 */     if (0 == result.numBadSegments) {
/* 552 */       result.clean = true;
/* 553 */       msg("No problems were detected with this index.\n");
/*     */     } else {
/* 555 */       msg("WARNING: " + result.numBadSegments + " broken segments (containing " + result.totLoseDocCount + " documents) detected");
/*     */     }
/* 557 */     return result;
/*     */   }
/*     */ 
/*     */   private CheckIndex.Status.FieldNormStatus testFieldNorms(Collection<String> fieldNames, SegmentReader reader)
/*     */   {
/* 564 */     CheckIndex.Status.FieldNormStatus status = new CheckIndex.Status.FieldNormStatus();
/*     */     try
/*     */     {
/* 568 */       if (this.infoStream != null) {
/* 569 */         this.infoStream.print("    test: field norms.........");
/*     */       }
/* 571 */       byte[] b = new byte[reader.maxDoc()];
/* 572 */       for (String fieldName : fieldNames) {
/* 573 */         reader.norms(fieldName, b, 0);
/* 574 */         status.totFields += 1L;
/*     */       }
/*     */ 
/* 577 */       msg("OK [" + status.totFields + " fields]");
/*     */     } catch (Throwable e) {
/* 579 */       msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
/* 580 */       status.error = e;
/* 581 */       if (this.infoStream != null) {
/* 582 */         e.printStackTrace(this.infoStream);
/*     */       }
/*     */     }
/*     */ 
/* 586 */     return status;
/*     */   }
/*     */ 
/*     */   private CheckIndex.Status.TermIndexStatus testTermIndex(SegmentInfo info, SegmentReader reader)
/*     */   {
/* 593 */     CheckIndex.Status.TermIndexStatus status = new CheckIndex.Status.TermIndexStatus();
/*     */     try
/*     */     {
/* 596 */       if (this.infoStream != null) {
/* 597 */         this.infoStream.print("    test: terms, freq, prox...");
/*     */       }
/*     */ 
/* 600 */       TermEnum termEnum = reader.terms();
/* 601 */       TermPositions termPositions = reader.termPositions();
/*     */ 
/* 604 */       MySegmentTermDocs myTermDocs = new MySegmentTermDocs(reader);
/*     */ 
/* 606 */       int maxDoc = reader.maxDoc();
/*     */ 
/* 608 */       while (termEnum.next()) {
/* 609 */         status.termCount += 1L;
/* 610 */         Term term = termEnum.term();
/* 611 */         int docFreq = termEnum.docFreq();
/* 612 */         termPositions.seek(term);
/* 613 */         int lastDoc = -1;
/* 614 */         int freq0 = 0;
/* 615 */         status.totFreq += docFreq;
/* 616 */         while (termPositions.next()) {
/* 617 */           freq0++;
/* 618 */           int doc = termPositions.doc();
/* 619 */           int freq = termPositions.freq();
/* 620 */           if (doc <= lastDoc)
/* 621 */             throw new RuntimeException("term " + term + ": doc " + doc + " <= lastDoc " + lastDoc);
/* 622 */           if (doc >= maxDoc) {
/* 623 */             throw new RuntimeException("term " + term + ": doc " + doc + " >= maxDoc " + maxDoc);
/*     */           }
/* 625 */           lastDoc = doc;
/* 626 */           if (freq <= 0) {
/* 627 */             throw new RuntimeException("term " + term + ": doc " + doc + ": freq " + freq + " is out of bounds");
/*     */           }
/* 629 */           int lastPos = -1;
/* 630 */           status.totPos += freq;
/* 631 */           for (int j = 0; j < freq; j++) {
/* 632 */             int pos = termPositions.nextPosition();
/* 633 */             if (pos < -1)
/* 634 */               throw new RuntimeException("term " + term + ": doc " + doc + ": pos " + pos + " is out of bounds");
/* 635 */             if (pos < lastPos)
/* 636 */               throw new RuntimeException("term " + term + ": doc " + doc + ": pos " + pos + " < lastPos " + lastPos);
/* 637 */             lastPos = pos;
/*     */           }
/*     */         }
/*     */         int delCount;
/*     */         int delCount;
/* 644 */         if (reader.hasDeletions()) {
/* 645 */           myTermDocs.seek(term);
/* 646 */           while (myTermDocs.next());
/* 647 */           delCount = myTermDocs.delCount;
/*     */         } else {
/* 649 */           delCount = 0;
/*     */         }
/*     */ 
/* 652 */         if (freq0 + delCount != docFreq) {
/* 653 */           throw new RuntimeException("term " + term + " docFreq=" + docFreq + " != num docs seen " + freq0 + " + num docs deleted " + delCount);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 658 */       msg("OK [" + status.termCount + " terms; " + status.totFreq + " terms/docs pairs; " + status.totPos + " tokens]");
/*     */     }
/*     */     catch (Throwable e) {
/* 661 */       msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
/* 662 */       status.error = e;
/* 663 */       if (this.infoStream != null) {
/* 664 */         e.printStackTrace(this.infoStream);
/*     */       }
/*     */     }
/*     */ 
/* 668 */     return status;
/*     */   }
/*     */ 
/*     */   private CheckIndex.Status.StoredFieldStatus testStoredFields(SegmentInfo info, SegmentReader reader, NumberFormat format)
/*     */   {
/* 675 */     CheckIndex.Status.StoredFieldStatus status = new CheckIndex.Status.StoredFieldStatus();
/*     */     try
/*     */     {
/* 678 */       if (this.infoStream != null) {
/* 679 */         this.infoStream.print("    test: stored fields.......");
/*     */       }
/*     */ 
/* 683 */       for (int j = 0; j < info.docCount; j++) {
/* 684 */         if (!reader.isDeleted(j)) {
/* 685 */           status.docCount += 1;
/* 686 */           Document doc = reader.document(j);
/* 687 */           status.totFields += doc.getFields().size();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 692 */       if (status.docCount != reader.numDocs()) {
/* 693 */         throw new RuntimeException("docCount=" + status.docCount + " but saw " + status.docCount + " undeleted docs");
/*     */       }
/*     */ 
/* 696 */       msg("OK [" + status.totFields + " total field count; avg " + format.format((float)status.totFields / status.docCount) + " fields per doc]");
/*     */     }
/*     */     catch (Throwable e) {
/* 699 */       msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
/* 700 */       status.error = e;
/* 701 */       if (this.infoStream != null) {
/* 702 */         e.printStackTrace(this.infoStream);
/*     */       }
/*     */     }
/*     */ 
/* 706 */     return status;
/*     */   }
/*     */ 
/*     */   private CheckIndex.Status.TermVectorStatus testTermVectors(SegmentInfo info, SegmentReader reader, NumberFormat format)
/*     */   {
/* 713 */     CheckIndex.Status.TermVectorStatus status = new CheckIndex.Status.TermVectorStatus();
/*     */     try
/*     */     {
/* 716 */       if (this.infoStream != null) {
/* 717 */         this.infoStream.print("    test: term vectors........");
/*     */       }
/*     */ 
/* 720 */       for (int j = 0; j < info.docCount; j++) {
/* 721 */         if (!reader.isDeleted(j)) {
/* 722 */           status.docCount += 1;
/* 723 */           TermFreqVector[] tfv = reader.getTermFreqVectors(j);
/* 724 */           if (tfv != null) {
/* 725 */             status.totVectors += tfv.length;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 730 */       msg("OK [" + status.totVectors + " total vector count; avg " + format.format((float)status.totVectors / status.docCount) + " term/freq vector fields per doc]");
/*     */     }
/*     */     catch (Throwable e) {
/* 733 */       msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
/* 734 */       status.error = e;
/* 735 */       if (this.infoStream != null) {
/* 736 */         e.printStackTrace(this.infoStream);
/*     */       }
/*     */     }
/*     */ 
/* 740 */     return status;
/*     */   }
/*     */ 
/*     */   public void fixIndex(Status result)
/*     */     throws IOException
/*     */   {
/* 757 */     if (result.partial)
/* 758 */       throw new IllegalArgumentException("can only fix an index that was fully checked (this status checked a subset of segments)");
/* 759 */     result.newSegments.commit(result.dir);
/*     */   }
/*     */ 
/*     */   private static boolean testAsserts()
/*     */   {
/* 765 */     assertsOn = true;
/* 766 */     return true;
/*     */   }
/*     */ 
/*     */   private static boolean assertsOn() {
/* 770 */     assert (testAsserts());
/* 771 */     return assertsOn;
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws IOException, InterruptedException
/*     */   {
/* 808 */     boolean doFix = false;
/* 809 */     List onlySegments = new ArrayList();
/* 810 */     String indexPath = null;
/* 811 */     int i = 0;
/* 812 */     while (i < args.length) {
/* 813 */       if (args[i].equals("-fix")) {
/* 814 */         doFix = true;
/* 815 */         i++;
/* 816 */       } else if (args[i].equals("-segment")) {
/* 817 */         if (i == args.length - 1) {
/* 818 */           System.out.println("ERROR: missing name for -segment option");
/* 819 */           System.exit(1);
/*     */         }
/* 821 */         onlySegments.add(args[(i + 1)]);
/* 822 */         i += 2;
/*     */       } else {
/* 824 */         if (indexPath != null) {
/* 825 */           System.out.println("ERROR: unexpected extra argument '" + args[i] + "'");
/* 826 */           System.exit(1);
/*     */         }
/* 828 */         indexPath = args[i];
/* 829 */         i++;
/*     */       }
/*     */     }
/*     */ 
/* 833 */     if (indexPath == null) {
/* 834 */       System.out.println("\nERROR: index path not specified");
/* 835 */       System.out.println("\nUsage: java org.apache.lucene.index.CheckIndex pathToIndex [-fix] [-segment X] [-segment Y]\n\n  -fix: actually write a new segments_N file, removing any problematic segments\n  -segment X: only check the specified segments.  This can be specified multiple\n              times, to check more than one segment, eg '-segment _2 -segment _a'.\n              You can't use this with the -fix option\n\n**WARNING**: -fix should only be used on an emergency basis as it will cause\ndocuments (perhaps many) to be permanently removed from the index.  Always make\na backup copy of your index before running this!  Do not run this tool on an index\nthat is actively being written to.  You have been warned!\n\nRun without -fix, this tool will open the index, report version information\nand report any exceptions it hits and what action it would take if -fix were\nspecified.  With -fix, this tool will remove any segments that have issues and\nwrite a new segments_N file.  This means all documents contained in the affected\nsegments will be removed.\n\nThis tool exits with exit code 1 if the index cannot be opened or has any\ncorruption, else 0.\n");
/*     */ 
/* 855 */       System.exit(1);
/*     */     }
/*     */ 
/* 858 */     if (!assertsOn()) {
/* 859 */       System.out.println("\nNOTE: testing will be more thorough if you run java with '-ea:org.apache.lucene...', so assertions are enabled");
/*     */     }
/* 861 */     if (onlySegments.size() == 0) {
/* 862 */       onlySegments = null;
/* 863 */     } else if (doFix) {
/* 864 */       System.out.println("ERROR: cannot specify both -fix and -segment");
/* 865 */       System.exit(1);
/*     */     }
/*     */ 
/* 868 */     System.out.println("\nOpening index @ " + indexPath + "\n");
/* 869 */     Directory dir = null;
/*     */     try {
/* 871 */       dir = FSDirectory.open(new File(indexPath));
/*     */     } catch (Throwable t) {
/* 873 */       System.out.println("ERROR: could not open directory \"" + indexPath + "\"; exiting");
/* 874 */       t.printStackTrace(System.out);
/* 875 */       System.exit(1);
/*     */     }
/*     */ 
/* 878 */     CheckIndex checker = new CheckIndex(dir);
/* 879 */     checker.setInfoStream(System.out);
/*     */ 
/* 881 */     Status result = checker.checkIndex(onlySegments);
/* 882 */     if (result.missingSegments) {
/* 883 */       System.exit(1);
/*     */     }
/*     */ 
/* 886 */     if (!result.clean) {
/* 887 */       if (!doFix) {
/* 888 */         System.out.println("WARNING: would write new segments file, and " + result.totLoseDocCount + " documents would be lost, if -fix were specified\n");
/*     */       } else {
/* 890 */         System.out.println("WARNING: " + result.totLoseDocCount + " documents will be lost\n");
/* 891 */         System.out.println("NOTE: will write new segments file in 5 seconds; this will remove " + result.totLoseDocCount + " docs from the index. THIS IS YOUR LAST CHANCE TO CTRL+C!");
/* 892 */         for (int s = 0; s < 5; s++) {
/* 893 */           Thread.sleep(1000L);
/* 894 */           System.out.println("  " + (5 - s) + "...");
/*     */         }
/* 896 */         System.out.println("Writing...");
/* 897 */         checker.fixIndex(result);
/* 898 */         System.out.println("OK");
/* 899 */         System.out.println("Wrote new segments file \"" + result.newSegments.getCurrentSegmentFileName() + "\"");
/*     */       }
/*     */     }
/* 902 */     System.out.println("");
/*     */     int exitCode;
/*     */     int exitCode;
/* 905 */     if ((result != null) && (result.clean == true))
/* 906 */       exitCode = 0;
/*     */     else
/* 908 */       exitCode = 1;
/* 909 */     System.exit(exitCode);
/*     */   }
/*     */ 
/*     */   private static class MySegmentTermDocs extends SegmentTermDocs
/*     */   {
/*     */     int delCount;
/*     */ 
/*     */     MySegmentTermDocs(SegmentReader p)
/*     */     {
/* 274 */       super();
/*     */     }
/*     */ 
/*     */     public void seek(Term term) throws IOException
/*     */     {
/* 279 */       super.seek(term);
/* 280 */       this.delCount = 0;
/*     */     }
/*     */ 
/*     */     protected void skippingDoc() throws IOException
/*     */     {
/* 285 */       this.delCount += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Status
/*     */   {
/*     */     public boolean clean;
/*     */     public boolean missingSegments;
/*     */     public boolean cantOpenSegments;
/*     */     public boolean missingSegmentVersion;
/*     */     public String segmentsFileName;
/*     */     public int numSegments;
/*     */     public String segmentFormat;
/*     */     public List<String> segmentsChecked;
/*     */     public boolean toolOutOfDate;
/*     */     public List<SegmentInfoStatus> segmentInfos;
/*     */     public Directory dir;
/*     */     SegmentInfos newSegments;
/*     */     public int totLoseDocCount;
/*     */     public int numBadSegments;
/*     */     public boolean partial;
/*     */     public Map<String, String> userData;
/*     */ 
/*     */     public Status()
/*     */     {
/*  86 */       this.segmentsChecked = new ArrayList();
/*     */ 
/*  92 */       this.segmentInfos = new ArrayList();
/*     */     }
/*     */ 
/*     */     public static final class TermVectorStatus
/*     */     {
/* 242 */       public int docCount = 0;
/*     */ 
/* 245 */       public long totVectors = 0L;
/*     */ 
/* 248 */       public Throwable error = null;
/*     */     }
/*     */ 
/*     */     public static final class StoredFieldStatus
/*     */     {
/* 227 */       public int docCount = 0;
/*     */ 
/* 230 */       public long totFields = 0L;
/*     */ 
/* 233 */       public Throwable error = null;
/*     */     }
/*     */ 
/*     */     public static final class TermIndexStatus
/*     */     {
/* 209 */       public long termCount = 0L;
/*     */ 
/* 212 */       public long totFreq = 0L;
/*     */ 
/* 215 */       public long totPos = 0L;
/*     */ 
/* 218 */       public Throwable error = null;
/*     */     }
/*     */ 
/*     */     public static final class FieldNormStatus
/*     */     {
/* 198 */       public long totFields = 0L;
/*     */ 
/* 201 */       public Throwable error = null;
/*     */     }
/*     */ 
/*     */     public static class SegmentInfoStatus
/*     */     {
/*     */       public String name;
/*     */       public int docCount;
/*     */       public boolean compound;
/*     */       public int numFiles;
/*     */       public double sizeMB;
/* 144 */       public int docStoreOffset = -1;
/*     */       public String docStoreSegment;
/*     */       public boolean docStoreCompoundFile;
/*     */       public boolean hasDeletions;
/*     */       public String deletionsFileName;
/*     */       public int numDeleted;
/*     */       public boolean openReaderPassed;
/*     */       int numFields;
/*     */       public boolean hasProx;
/*     */       public Map<String, String> diagnostics;
/*     */       public CheckIndex.Status.FieldNormStatus fieldNormStatus;
/*     */       public CheckIndex.Status.TermIndexStatus termIndexStatus;
/*     */       public CheckIndex.Status.StoredFieldStatus storedFieldStatus;
/*     */       public CheckIndex.Status.TermVectorStatus termVectorStatus;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.CheckIndex
 * JD-Core Version:    0.6.2
 */