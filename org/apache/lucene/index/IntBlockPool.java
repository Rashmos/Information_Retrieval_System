/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ final class IntBlockPool
/*    */ {
/* 22 */   public int[][] buffers = new int[10][];
/*    */ 
/* 24 */   int bufferUpto = -1;
/* 25 */   public int intUpto = 8192;
/*    */   public int[] buffer;
/* 28 */   public int intOffset = -8192;
/*    */   private final DocumentsWriter docWriter;
/*    */   final boolean trackAllocations;
/*    */ 
/*    */   public IntBlockPool(DocumentsWriter docWriter, boolean trackAllocations)
/*    */   {
/* 34 */     this.docWriter = docWriter;
/* 35 */     this.trackAllocations = trackAllocations;
/*    */   }
/*    */ 
/*    */   public void reset() {
/* 39 */     if (this.bufferUpto != -1) {
/* 40 */       if (this.bufferUpto > 0)
/*    */       {
/* 42 */         this.docWriter.recycleIntBlocks(this.buffers, 1, 1 + this.bufferUpto);
/*    */       }
/*    */ 
/* 45 */       this.bufferUpto = 0;
/* 46 */       this.intUpto = 0;
/* 47 */       this.intOffset = 0;
/* 48 */       this.buffer = this.buffers[0];
/*    */     }
/*    */   }
/*    */ 
/*    */   public void nextBuffer() {
/* 53 */     if (1 + this.bufferUpto == this.buffers.length) {
/* 54 */       int[][] newBuffers = new int[(int)(this.buffers.length * 1.5D)][];
/* 55 */       System.arraycopy(this.buffers, 0, newBuffers, 0, this.buffers.length);
/* 56 */       this.buffers = newBuffers;
/*    */     }
/* 58 */     this.buffer = (this.buffers[(1 + this.bufferUpto)] =  = this.docWriter.getIntBlock(this.trackAllocations));
/* 59 */     this.bufferUpto += 1;
/*    */ 
/* 61 */     this.intUpto = 0;
/* 62 */     this.intOffset += 8192;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IntBlockPool
 * JD-Core Version:    0.6.2
 */