/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import org.apache.lucene.search.Similarity;
/*    */ import org.apache.lucene.util.ArrayUtil;
/*    */ 
/*    */ final class NormsWriterPerField extends InvertedDocEndConsumerPerField
/*    */   implements Comparable<NormsWriterPerField>
/*    */ {
/*    */   final NormsWriterPerThread perThread;
/*    */   final FieldInfo fieldInfo;
/*    */   final DocumentsWriter.DocState docState;
/* 35 */   int[] docIDs = new int[1];
/* 36 */   byte[] norms = new byte[1];
/*    */   int upto;
/*    */   final FieldInvertState fieldState;
/*    */ 
/*    */   public void reset()
/*    */   {
/* 43 */     this.docIDs = ArrayUtil.shrink(this.docIDs, this.upto);
/* 44 */     this.norms = ArrayUtil.shrink(this.norms, this.upto);
/* 45 */     this.upto = 0;
/*    */   }
/*    */ 
/*    */   public NormsWriterPerField(DocInverterPerField docInverterPerField, NormsWriterPerThread perThread, FieldInfo fieldInfo) {
/* 49 */     this.perThread = perThread;
/* 50 */     this.fieldInfo = fieldInfo;
/* 51 */     this.docState = perThread.docState;
/* 52 */     this.fieldState = docInverterPerField.fieldState;
/*    */   }
/*    */ 
/*    */   void abort()
/*    */   {
/* 57 */     this.upto = 0;
/*    */   }
/*    */ 
/*    */   public int compareTo(NormsWriterPerField other) {
/* 61 */     return this.fieldInfo.name.compareTo(other.fieldInfo.name);
/*    */   }
/*    */ 
/*    */   void finish()
/*    */   {
/* 66 */     assert (this.docIDs.length == this.norms.length);
/* 67 */     if ((this.fieldInfo.isIndexed) && (!this.fieldInfo.omitNorms)) {
/* 68 */       if (this.docIDs.length <= this.upto) {
/* 69 */         assert (this.docIDs.length == this.upto);
/* 70 */         this.docIDs = ArrayUtil.grow(this.docIDs, 1 + this.upto);
/* 71 */         this.norms = ArrayUtil.grow(this.norms, 1 + this.upto);
/*    */       }
/* 73 */       float norm = this.docState.similarity.computeNorm(this.fieldInfo.name, this.fieldState);
/* 74 */       this.norms[this.upto] = Similarity.encodeNorm(norm);
/* 75 */       this.docIDs[this.upto] = this.docState.docID;
/* 76 */       this.upto += 1;
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.NormsWriterPerField
 * JD-Core Version:    0.6.2
 */