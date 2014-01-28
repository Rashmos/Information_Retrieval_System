/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import org.apache.lucene.store.Directory;
/*    */ import org.apache.lucene.store.IndexOutput;
/*    */ 
/*    */ final class FormatPostingsPositionsWriter extends FormatPostingsPositionsConsumer
/*    */ {
/*    */   final FormatPostingsDocsWriter parent;
/*    */   final IndexOutput out;
/*    */   boolean omitTermFreqAndPositions;
/*    */   boolean storePayloads;
/* 32 */   int lastPayloadLength = -1;
/*    */   int lastPosition;
/*    */ 
/*    */   FormatPostingsPositionsWriter(SegmentWriteState state, FormatPostingsDocsWriter parent)
/*    */     throws IOException
/*    */   {
/* 35 */     this.parent = parent;
/* 36 */     this.omitTermFreqAndPositions = parent.omitTermFreqAndPositions;
/* 37 */     if (parent.parent.parent.fieldInfos.hasProx())
/*    */     {
/* 40 */       String fileName = IndexFileNames.segmentFileName(parent.parent.parent.segment, "prx");
/* 41 */       state.flushedFiles.add(fileName);
/* 42 */       this.out = parent.parent.parent.dir.createOutput(fileName);
/* 43 */       parent.skipListWriter.setProxOutput(this.out);
/*    */     }
/*    */     else {
/* 46 */       this.out = null;
/*    */     }
/*    */   }
/*    */ 
/*    */   void addPosition(int position, byte[] payload, int payloadOffset, int payloadLength)
/*    */     throws IOException
/*    */   {
/* 54 */     assert (!this.omitTermFreqAndPositions) : "omitTermFreqAndPositions is true";
/* 55 */     assert (this.out != null);
/*    */ 
/* 57 */     int delta = position - this.lastPosition;
/* 58 */     this.lastPosition = position;
/*    */ 
/* 60 */     if (this.storePayloads) {
/* 61 */       if (payloadLength != this.lastPayloadLength) {
/* 62 */         this.lastPayloadLength = payloadLength;
/* 63 */         this.out.writeVInt(delta << 1 | 0x1);
/* 64 */         this.out.writeVInt(payloadLength);
/*    */       } else {
/* 66 */         this.out.writeVInt(delta << 1);
/* 67 */       }if (payloadLength > 0)
/* 68 */         this.out.writeBytes(payload, payloadLength);
/*    */     } else {
/* 70 */       this.out.writeVInt(delta);
/*    */     }
/*    */   }
/*    */ 
/* 74 */   void setField(FieldInfo fieldInfo) { this.omitTermFreqAndPositions = fieldInfo.omitTermFreqAndPositions;
/* 75 */     this.storePayloads = (this.omitTermFreqAndPositions ? false : fieldInfo.storePayloads);
/*    */   }
/*    */ 
/*    */   void finish()
/*    */   {
/* 81 */     this.lastPosition = 0;
/* 82 */     this.lastPayloadLength = -1;
/*    */   }
/*    */ 
/*    */   void close() throws IOException {
/* 86 */     if (this.out != null)
/* 87 */       this.out.close();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FormatPostingsPositionsWriter
 * JD-Core Version:    0.6.2
 */