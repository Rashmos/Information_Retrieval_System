/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.BitSet;
/*     */ import org.apache.lucene.search.DocIdSet;
/*     */ import org.apache.lucene.search.DocIdSetIterator;
/*     */ 
/*     */ public class SortedVIntList extends DocIdSet
/*     */ {
/*     */   static final int BITS2VINTLIST_SIZE = 8;
/*     */   private int size;
/*     */   private byte[] bytes;
/*     */   private int lastBytePos;
/*     */   private static final int VB1 = 127;
/*     */   private static final int BIT_SHIFT = 7;
/* 167 */   private final int MAX_BYTES_PER_INT = 5;
/*     */ 
/*     */   public SortedVIntList(int[] sortedInts)
/*     */   {
/*  54 */     this(sortedInts, sortedInts.length);
/*     */   }
/*     */ 
/*     */   public SortedVIntList(int[] sortedInts, int inputSize)
/*     */   {
/*  63 */     SortedVIntListBuilder builder = new SortedVIntListBuilder();
/*  64 */     for (int i = 0; i < inputSize; i++) {
/*  65 */       builder.addInt(sortedInts[i]);
/*     */     }
/*  67 */     builder.done();
/*     */   }
/*     */ 
/*     */   public SortedVIntList(BitSet bits)
/*     */   {
/*  75 */     SortedVIntListBuilder builder = new SortedVIntListBuilder();
/*  76 */     int nextInt = bits.nextSetBit(0);
/*  77 */     while (nextInt != -1) {
/*  78 */       builder.addInt(nextInt);
/*  79 */       nextInt = bits.nextSetBit(nextInt + 1);
/*     */     }
/*  81 */     builder.done();
/*     */   }
/*     */ 
/*     */   public SortedVIntList(OpenBitSet bits)
/*     */   {
/*  89 */     SortedVIntListBuilder builder = new SortedVIntListBuilder();
/*  90 */     int nextInt = bits.nextSetBit(0);
/*  91 */     while (nextInt != -1) {
/*  92 */       builder.addInt(nextInt);
/*  93 */       nextInt = bits.nextSetBit(nextInt + 1);
/*     */     }
/*  95 */     builder.done();
/*     */   }
/*     */ 
/*     */   public SortedVIntList(DocIdSetIterator docIdSetIterator)
/*     */     throws IOException
/*     */   {
/* 106 */     SortedVIntListBuilder builder = new SortedVIntListBuilder();
/*     */     int doc;
/* 108 */     while ((doc = docIdSetIterator.nextDoc()) != 2147483647) {
/* 109 */       builder.addInt(doc);
/*     */     }
/* 111 */     builder.done();
/*     */   }
/*     */ 
/*     */   private void initBytes()
/*     */   {
/* 152 */     this.size = 0;
/* 153 */     this.bytes = new byte[''];
/* 154 */     this.lastBytePos = 0;
/*     */   }
/*     */ 
/*     */   private void resizeBytes(int newSize) {
/* 158 */     if (newSize != this.bytes.length) {
/* 159 */       byte[] newBytes = new byte[newSize];
/* 160 */       System.arraycopy(this.bytes, 0, newBytes, 0, this.lastBytePos);
/* 161 */       this.bytes = newBytes;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 173 */     return this.size;
/*     */   }
/*     */ 
/*     */   public int getByteSize()
/*     */   {
/* 180 */     return this.bytes.length;
/*     */   }
/*     */ 
/*     */   public boolean isCacheable()
/*     */   {
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */   public DocIdSetIterator iterator()
/*     */   {
/* 194 */     return new DocIdSetIterator() {
/* 195 */       int bytePos = 0;
/* 196 */       int lastInt = 0;
/* 197 */       int doc = -1;
/*     */ 
/*     */       private void advance()
/*     */       {
/* 201 */         byte b = SortedVIntList.this.bytes[(this.bytePos++)];
/* 202 */         this.lastInt += (b & 0x7F);
/* 203 */         for (int s = 7; (b & 0xFFFFFF80) != 0; s += 7) {
/* 204 */           b = SortedVIntList.this.bytes[(this.bytePos++)];
/* 205 */           this.lastInt += ((b & 0x7F) << s);
/*     */         }
/*     */       }
/*     */ 
/*     */       public int docID()
/*     */       {
/* 211 */         return this.doc;
/*     */       }
/*     */ 
/*     */       public int nextDoc()
/*     */       {
/* 216 */         if (this.bytePos >= SortedVIntList.this.lastBytePos) {
/* 217 */           this.doc = 2147483647;
/*     */         } else {
/* 219 */           advance();
/* 220 */           this.doc = this.lastInt;
/*     */         }
/* 222 */         return this.doc;
/*     */       }
/*     */ 
/*     */       public int advance(int target)
/*     */       {
/* 227 */         while (this.bytePos < SortedVIntList.this.lastBytePos) {
/* 228 */           advance();
/* 229 */           if (this.lastInt >= target) {
/* 230 */             return this.doc = this.lastInt;
/*     */           }
/*     */         }
/* 233 */         return this.doc = 2147483647;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private class SortedVIntListBuilder
/*     */   {
/* 116 */     private int lastInt = 0;
/*     */ 
/*     */     SortedVIntListBuilder() {
/* 119 */       SortedVIntList.this.initBytes();
/* 120 */       this.lastInt = 0;
/*     */     }
/*     */ 
/*     */     void addInt(int nextInt) {
/* 124 */       int diff = nextInt - this.lastInt;
/* 125 */       if (diff < 0) {
/* 126 */         throw new IllegalArgumentException("Input not sorted or first element negative.");
/*     */       }
/*     */ 
/* 130 */       if (SortedVIntList.this.lastBytePos + 5 > SortedVIntList.this.bytes.length)
/*     */       {
/* 132 */         SortedVIntList.this.resizeBytes(SortedVIntList.this.bytes.length * 2 + 5);
/*     */       }
/*     */ 
/* 136 */       while ((diff & 0xFFFFFF80) != 0) {
/* 137 */         SortedVIntList.this.bytes[SortedVIntList.access$108(SortedVIntList.this)] = ((byte)(diff & 0x7F | 0xFFFFFF80));
/* 138 */         diff >>>= 7;
/*     */       }
/* 140 */       SortedVIntList.this.bytes[SortedVIntList.access$108(SortedVIntList.this)] = ((byte)diff);
/* 141 */       SortedVIntList.access$408(SortedVIntList.this);
/* 142 */       this.lastInt = nextInt;
/*     */     }
/*     */ 
/*     */     void done() {
/* 146 */       SortedVIntList.this.resizeBytes(SortedVIntList.this.lastBytePos);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.SortedVIntList
 * JD-Core Version:    0.6.2
 */