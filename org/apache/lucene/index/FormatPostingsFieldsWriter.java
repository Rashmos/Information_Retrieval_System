/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import org.apache.lucene.store.Directory;
/*    */ 
/*    */ final class FormatPostingsFieldsWriter extends FormatPostingsFieldsConsumer
/*    */ {
/*    */   final Directory dir;
/*    */   final String segment;
/*    */   final TermInfosWriter termsOut;
/*    */   final FieldInfos fieldInfos;
/*    */   final FormatPostingsTermsWriter termsWriter;
/*    */   final DefaultSkipListWriter skipListWriter;
/*    */   final int totalNumDocs;
/*    */ 
/*    */   public FormatPostingsFieldsWriter(SegmentWriteState state, FieldInfos fieldInfos)
/*    */     throws IOException
/*    */   {
/* 37 */     this.dir = state.directory;
/* 38 */     this.segment = state.segmentName;
/* 39 */     this.totalNumDocs = state.numDocs;
/* 40 */     this.fieldInfos = fieldInfos;
/* 41 */     this.termsOut = new TermInfosWriter(this.dir, this.segment, fieldInfos, state.termIndexInterval);
/*    */ 
/* 50 */     this.skipListWriter = new DefaultSkipListWriter(this.termsOut.skipInterval, this.termsOut.maxSkipLevels, this.totalNumDocs, null, null);
/*    */ 
/* 56 */     state.flushedFiles.add(state.segmentFileName("tis"));
/* 57 */     state.flushedFiles.add(state.segmentFileName("tii"));
/*    */ 
/* 59 */     this.termsWriter = new FormatPostingsTermsWriter(state, this);
/*    */   }
/*    */ 
/*    */   FormatPostingsTermsConsumer addField(FieldInfo field)
/*    */   {
/* 65 */     this.termsWriter.setField(field);
/* 66 */     return this.termsWriter;
/*    */   }
/*    */ 
/*    */   void finish()
/*    */     throws IOException
/*    */   {
/* 72 */     this.termsOut.close();
/* 73 */     this.termsWriter.close();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FormatPostingsFieldsWriter
 * JD-Core Version:    0.6.2
 */