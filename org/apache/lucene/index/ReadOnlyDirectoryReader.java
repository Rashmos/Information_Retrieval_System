/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import org.apache.lucene.store.Directory;
/*    */ 
/*    */ class ReadOnlyDirectoryReader extends DirectoryReader
/*    */ {
/*    */   ReadOnlyDirectoryReader(Directory directory, SegmentInfos sis, IndexDeletionPolicy deletionPolicy, int termInfosIndexDivisor)
/*    */     throws IOException
/*    */   {
/* 27 */     super(directory, sis, deletionPolicy, true, termInfosIndexDivisor);
/*    */   }
/*    */ 
/*    */   ReadOnlyDirectoryReader(Directory directory, SegmentInfos infos, SegmentReader[] oldReaders, int[] oldStarts, Map<String, byte[]> oldNormsCache, boolean doClone, int termInfosIndexDivisor) throws IOException
/*    */   {
/* 32 */     super(directory, infos, oldReaders, oldStarts, oldNormsCache, true, doClone, termInfosIndexDivisor);
/*    */   }
/*    */ 
/*    */   ReadOnlyDirectoryReader(IndexWriter writer, SegmentInfos infos, int termInfosIndexDivisor) throws IOException {
/* 36 */     super(writer, infos, termInfosIndexDivisor);
/*    */   }
/*    */ 
/*    */   protected void acquireWriteLock()
/*    */   {
/* 41 */     ReadOnlySegmentReader.noWrite();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.ReadOnlyDirectoryReader
 * JD-Core Version:    0.6.2
 */