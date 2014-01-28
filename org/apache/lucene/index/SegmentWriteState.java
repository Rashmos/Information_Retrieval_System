/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import org.apache.lucene.store.Directory;
/*    */ 
/*    */ class SegmentWriteState
/*    */ {
/*    */   DocumentsWriter docWriter;
/*    */   Directory directory;
/*    */   String segmentName;
/*    */   String docStoreSegmentName;
/*    */   int numDocs;
/*    */   int termIndexInterval;
/*    */   int numDocsInStore;
/*    */   Collection<String> flushedFiles;
/*    */ 
/*    */   public SegmentWriteState(DocumentsWriter docWriter, Directory directory, String segmentName, String docStoreSegmentName, int numDocs, int numDocsInStore, int termIndexInterval)
/*    */   {
/* 37 */     this.docWriter = docWriter;
/* 38 */     this.directory = directory;
/* 39 */     this.segmentName = segmentName;
/* 40 */     this.docStoreSegmentName = docStoreSegmentName;
/* 41 */     this.numDocs = numDocs;
/* 42 */     this.numDocsInStore = numDocsInStore;
/* 43 */     this.termIndexInterval = termIndexInterval;
/* 44 */     this.flushedFiles = new HashSet();
/*    */   }
/*    */ 
/*    */   public String segmentFileName(String ext) {
/* 48 */     return this.segmentName + "." + ext;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentWriteState
 * JD-Core Version:    0.6.2
 */