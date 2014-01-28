/*     */ package org.apache.lucene.analysis.compound.hyphenation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class ByteVector
/*     */   implements Serializable
/*     */ {
/*     */   private static final int DEFAULT_BLOCK_SIZE = 2048;
/*     */   private int blockSize;
/*     */   private byte[] array;
/*     */   private int n;
/*     */ 
/*     */   public ByteVector()
/*     */   {
/*  48 */     this(2048);
/*     */   }
/*     */ 
/*     */   public ByteVector(int capacity) {
/*  52 */     if (capacity > 0)
/*  53 */       this.blockSize = capacity;
/*     */     else {
/*  55 */       this.blockSize = 2048;
/*     */     }
/*  57 */     this.array = new byte[this.blockSize];
/*  58 */     this.n = 0;
/*     */   }
/*     */ 
/*     */   public ByteVector(byte[] a) {
/*  62 */     this.blockSize = 2048;
/*  63 */     this.array = a;
/*  64 */     this.n = 0;
/*     */   }
/*     */ 
/*     */   public ByteVector(byte[] a, int capacity) {
/*  68 */     if (capacity > 0)
/*  69 */       this.blockSize = capacity;
/*     */     else {
/*  71 */       this.blockSize = 2048;
/*     */     }
/*  73 */     this.array = a;
/*  74 */     this.n = 0;
/*     */   }
/*     */ 
/*     */   public byte[] getArray() {
/*  78 */     return this.array;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/*  85 */     return this.n;
/*     */   }
/*     */ 
/*     */   public int capacity()
/*     */   {
/*  92 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   public void put(int index, byte val) {
/*  96 */     this.array[index] = val;
/*     */   }
/*     */ 
/*     */   public byte get(int index) {
/* 100 */     return this.array[index];
/*     */   }
/*     */ 
/*     */   public int alloc(int size)
/*     */   {
/* 107 */     int index = this.n;
/* 108 */     int len = this.array.length;
/* 109 */     if (this.n + size >= len) {
/* 110 */       byte[] aux = new byte[len + this.blockSize];
/* 111 */       System.arraycopy(this.array, 0, aux, 0, len);
/* 112 */       this.array = aux;
/*     */     }
/* 114 */     this.n += size;
/* 115 */     return index;
/*     */   }
/*     */ 
/*     */   public void trimToSize() {
/* 119 */     if (this.n < this.array.length) {
/* 120 */       byte[] aux = new byte[this.n];
/* 121 */       System.arraycopy(this.array, 0, aux, 0, this.n);
/* 122 */       this.array = aux;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.ByteVector
 * JD-Core Version:    0.6.2
 */