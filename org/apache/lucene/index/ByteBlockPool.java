/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ final class ByteBlockPool
/*     */ {
/*  47 */   public byte[][] buffers = new byte[10][];
/*     */ 
/*  49 */   int bufferUpto = -1;
/*  50 */   public int byteUpto = 32768;
/*     */   public byte[] buffer;
/*  53 */   public int byteOffset = -32768;
/*     */   private final boolean trackAllocations;
/*     */   private final Allocator allocator;
/* 113 */   static final int[] nextLevelArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 9 };
/* 114 */   static final int[] levelSizeArray = { 5, 14, 20, 30, 40, 40, 80, 80, 120, 200 };
/* 115 */   static final int FIRST_LEVEL_SIZE = levelSizeArray[0];
/*     */ 
/*     */   public ByteBlockPool(Allocator allocator, boolean trackAllocations)
/*     */   {
/*  59 */     this.allocator = allocator;
/*  60 */     this.trackAllocations = trackAllocations;
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  64 */     if (this.bufferUpto != -1)
/*     */     {
/*  67 */       for (int i = 0; i < this.bufferUpto; i++)
/*     */       {
/*  69 */         Arrays.fill(this.buffers[i], (byte)0);
/*     */       }
/*     */ 
/*  72 */       Arrays.fill(this.buffers[this.bufferUpto], 0, this.byteUpto, (byte)0);
/*     */ 
/*  74 */       if (this.bufferUpto > 0)
/*     */       {
/*  76 */         this.allocator.recycleByteBlocks(this.buffers, 1, 1 + this.bufferUpto);
/*     */       }
/*     */ 
/*  79 */       this.bufferUpto = 0;
/*  80 */       this.byteUpto = 0;
/*  81 */       this.byteOffset = 0;
/*  82 */       this.buffer = this.buffers[0];
/*     */     }
/*     */   }
/*     */ 
/*     */   public void nextBuffer() {
/*  87 */     if (1 + this.bufferUpto == this.buffers.length) {
/*  88 */       byte[][] newBuffers = new byte[(int)(this.buffers.length * 1.5D)][];
/*  89 */       System.arraycopy(this.buffers, 0, newBuffers, 0, this.buffers.length);
/*  90 */       this.buffers = newBuffers;
/*     */     }
/*  92 */     this.buffer = (this.buffers[(1 + this.bufferUpto)] =  = this.allocator.getByteBlock(this.trackAllocations));
/*  93 */     this.bufferUpto += 1;
/*     */ 
/*  95 */     this.byteUpto = 0;
/*  96 */     this.byteOffset += 32768;
/*     */   }
/*     */ 
/*     */   public int newSlice(int size) {
/* 100 */     if (this.byteUpto > 32768 - size)
/* 101 */       nextBuffer();
/* 102 */     int upto = this.byteUpto;
/* 103 */     this.byteUpto += size;
/* 104 */     this.buffer[(this.byteUpto - 1)] = 16;
/* 105 */     return upto;
/*     */   }
/*     */ 
/*     */   public int allocSlice(byte[] slice, int upto)
/*     */   {
/* 119 */     int level = slice[upto] & 0xF;
/* 120 */     int newLevel = nextLevelArray[level];
/* 121 */     int newSize = levelSizeArray[newLevel];
/*     */ 
/* 124 */     if (this.byteUpto > 32768 - newSize) {
/* 125 */       nextBuffer();
/*     */     }
/* 127 */     int newUpto = this.byteUpto;
/* 128 */     int offset = newUpto + this.byteOffset;
/* 129 */     this.byteUpto += newSize;
/*     */ 
/* 133 */     this.buffer[newUpto] = slice[(upto - 3)];
/* 134 */     this.buffer[(newUpto + 1)] = slice[(upto - 2)];
/* 135 */     this.buffer[(newUpto + 2)] = slice[(upto - 1)];
/*     */ 
/* 138 */     slice[(upto - 3)] = ((byte)(offset >>> 24));
/* 139 */     slice[(upto - 2)] = ((byte)(offset >>> 16));
/* 140 */     slice[(upto - 1)] = ((byte)(offset >>> 8));
/* 141 */     slice[upto] = ((byte)offset);
/*     */ 
/* 144 */     this.buffer[(this.byteUpto - 1)] = ((byte)(0x10 | newLevel));
/*     */ 
/* 146 */     return newUpto + 3;
/*     */   }
/*     */ 
/*     */   static abstract class Allocator
/*     */   {
/*     */     abstract void recycleByteBlocks(byte[][] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */     abstract void recycleByteBlocks(List<byte[]> paramList);
/*     */ 
/*     */     abstract byte[] getByteBlock(boolean paramBoolean);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.ByteBlockPool
 * JD-Core Version:    0.6.2
 */