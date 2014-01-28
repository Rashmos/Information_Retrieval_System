/*     */ package org.apache.lucene.analysis.compound.hyphenation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class CharVector
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final int DEFAULT_BLOCK_SIZE = 2048;
/*     */   private int blockSize;
/*     */   private char[] array;
/*     */   private int n;
/*     */ 
/*     */   public CharVector()
/*     */   {
/*  48 */     this(2048);
/*     */   }
/*     */ 
/*     */   public CharVector(int capacity) {
/*  52 */     if (capacity > 0)
/*  53 */       this.blockSize = capacity;
/*     */     else {
/*  55 */       this.blockSize = 2048;
/*     */     }
/*  57 */     this.array = new char[this.blockSize];
/*  58 */     this.n = 0;
/*     */   }
/*     */ 
/*     */   public CharVector(char[] a) {
/*  62 */     this.blockSize = 2048;
/*  63 */     this.array = a;
/*  64 */     this.n = a.length;
/*     */   }
/*     */ 
/*     */   public CharVector(char[] a, int capacity) {
/*  68 */     if (capacity > 0)
/*  69 */       this.blockSize = capacity;
/*     */     else {
/*  71 */       this.blockSize = 2048;
/*     */     }
/*  73 */     this.array = a;
/*  74 */     this.n = a.length;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*  81 */     this.n = 0;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  86 */     CharVector cv = new CharVector((char[])this.array.clone(), this.blockSize);
/*  87 */     cv.n = this.n;
/*  88 */     return cv;
/*     */   }
/*     */ 
/*     */   public char[] getArray() {
/*  92 */     return this.array;
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/*  99 */     return this.n;
/*     */   }
/*     */ 
/*     */   public int capacity()
/*     */   {
/* 106 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   public void put(int index, char val) {
/* 110 */     this.array[index] = val;
/*     */   }
/*     */ 
/*     */   public char get(int index) {
/* 114 */     return this.array[index];
/*     */   }
/*     */ 
/*     */   public int alloc(int size) {
/* 118 */     int index = this.n;
/* 119 */     int len = this.array.length;
/* 120 */     if (this.n + size >= len) {
/* 121 */       char[] aux = new char[len + this.blockSize];
/* 122 */       System.arraycopy(this.array, 0, aux, 0, len);
/* 123 */       this.array = aux;
/*     */     }
/* 125 */     this.n += size;
/* 126 */     return index;
/*     */   }
/*     */ 
/*     */   public void trimToSize() {
/* 130 */     if (this.n < this.array.length) {
/* 131 */       char[] aux = new char[this.n];
/* 132 */       System.arraycopy(this.array, 0, aux, 0, this.n);
/* 133 */       this.array = aux;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.CharVector
 * JD-Core Version:    0.6.2
 */