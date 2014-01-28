/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ final class CharBlockPool
/*    */ {
/* 22 */   public char[][] buffers = new char[10][];
/*    */   int numBuffer;
/* 25 */   int bufferUpto = -1;
/* 26 */   public int charUpto = 16384;
/*    */   public char[] buffer;
/* 29 */   public int charOffset = -16384;
/*    */   private final DocumentsWriter docWriter;
/*    */ 
/*    */   public CharBlockPool(DocumentsWriter docWriter)
/*    */   {
/* 33 */     this.docWriter = docWriter;
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 37 */     this.docWriter.recycleCharBlocks(this.buffers, 1 + this.bufferUpto);
/* 38 */     this.bufferUpto = -1;
/* 39 */     this.charUpto = 16384;
/* 40 */     this.charOffset = -16384;
/*    */   }
/*    */ 
/*    */   public void nextBuffer() {
/* 44 */     if (1 + this.bufferUpto == this.buffers.length) {
/* 45 */       char[][] newBuffers = new char[(int)(this.buffers.length * 1.5D)][];
/* 46 */       System.arraycopy(this.buffers, 0, newBuffers, 0, this.buffers.length);
/* 47 */       this.buffers = newBuffers;
/*    */     }
/* 49 */     this.buffer = (this.buffers[(1 + this.bufferUpto)] =  = this.docWriter.getCharBlock());
/* 50 */     this.bufferUpto += 1;
/*    */ 
/* 52 */     this.charUpto = 0;
/* 53 */     this.charOffset += 16384;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.CharBlockPool
 * JD-Core Version:    0.6.2
 */