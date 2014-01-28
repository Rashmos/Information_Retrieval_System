/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class MultiTrieReader
/*     */   implements TrieReader
/*     */ {
/*  33 */   private final Stack<Long> mStack1 = new Stack();
/*  34 */   private final Stack<Long> mStack2 = new Stack();
/*     */   private final TrieReader mReader1;
/*     */   private final TrieReader mReader2;
/*  39 */   private boolean mNotInitialized = true;
/*     */ 
/*     */   public MultiTrieReader(TrieReader reader1, TrieReader reader2)
/*     */   {
/*  49 */     this.mReader1 = reader1;
/*  50 */     this.mReader2 = reader2;
/*     */   }
/*     */ 
/*     */   public long readSymbol() throws IOException {
/*  54 */     if (this.mStack1.size() > this.mStack2.size()) {
/*  55 */       long symbol = ((Long)this.mStack1.peek()).longValue();
/*  56 */       if (symbol == -1L) {
/*  57 */         this.mStack1.pop();
/*  58 */         replace(this.mStack1, this.mReader1);
/*     */       } else {
/*  60 */         replace(this.mStack1, -2L);
/*     */       }
/*  62 */       return symbol;
/*     */     }
/*  64 */     if (this.mStack1.size() < this.mStack2.size()) {
/*  65 */       long symbol = ((Long)this.mStack2.peek()).longValue();
/*  66 */       if (symbol == -1L) {
/*  67 */         this.mStack2.pop();
/*  68 */         replace(this.mStack2, this.mReader2);
/*     */       } else {
/*  70 */         replace(this.mStack2, -2L);
/*     */       }
/*  72 */       return symbol;
/*     */     }
/*     */ 
/*  76 */     long top1 = ((Long)this.mStack1.peek()).longValue();
/*  77 */     long top2 = ((Long)this.mStack2.peek()).longValue();
/*  78 */     if ((top1 == -1L) && (top2 == -1L)) {
/*  79 */       this.mStack1.pop();
/*  80 */       replace(this.mStack1, this.mReader1);
/*  81 */       this.mStack2.pop();
/*  82 */       replace(this.mStack2, this.mReader2);
/*  83 */       return -1L;
/*     */     }
/*  85 */     if ((top2 == -1L) || ((top1 != -1L) && (top1 < top2))) {
/*  86 */       replace(this.mStack1, -2L);
/*  87 */       return top1;
/*     */     }
/*  89 */     if ((top1 == -1L) || ((top2 != -1L) && (top2 < top1))) {
/*  90 */       replace(this.mStack2, -2L);
/*  91 */       return top2;
/*     */     }
/*     */ 
/*  95 */     replace(this.mStack1, -2L);
/*  96 */     replace(this.mStack2, -2L);
/*  97 */     return top1;
/*     */   }
/*     */ 
/*     */   public long readCount() throws IOException {
/* 101 */     if (this.mNotInitialized) {
/* 102 */       this.mNotInitialized = false;
/* 103 */       long count = this.mReader1.readCount() + this.mReader2.readCount();
/* 104 */       this.mStack1.push(Long.valueOf(this.mReader1.readSymbol()));
/* 105 */       this.mStack2.push(Long.valueOf(this.mReader2.readSymbol()));
/* 106 */       return count;
/*     */     }
/* 108 */     if (this.mStack1.size() > this.mStack2.size()) {
/* 109 */       long count = this.mReader1.readCount();
/* 110 */       this.mStack1.push(Long.valueOf(this.mReader1.readSymbol()));
/* 111 */       return count;
/*     */     }
/* 113 */     if (this.mStack1.size() < this.mStack2.size()) {
/* 114 */       long count = this.mReader2.readCount();
/* 115 */       this.mStack2.push(Long.valueOf(this.mReader2.readSymbol()));
/* 116 */       return count;
/*     */     }
/* 118 */     if ((((Long)this.mStack1.peek()).longValue() == -2L) && (((Long)this.mStack2.peek()).longValue() == -2L)) {
/* 119 */       long count = this.mReader1.readCount() + this.mReader2.readCount();
/* 120 */       this.mStack1.push(Long.valueOf(this.mReader1.readSymbol()));
/* 121 */       this.mStack2.push(Long.valueOf(this.mReader2.readSymbol()));
/* 122 */       return count;
/*     */     }
/* 124 */     if (((Long)this.mStack1.peek()).longValue() == -2L) {
/* 125 */       long count = this.mReader1.readCount();
/* 126 */       this.mStack1.push(Long.valueOf(this.mReader1.readSymbol()));
/* 127 */       return count;
/*     */     }
/* 129 */     if (((Long)this.mStack2.peek()).longValue() == -2L) {
/* 130 */       long count = this.mReader2.readCount();
/* 131 */       this.mStack2.push(Long.valueOf(this.mReader2.readSymbol()));
/* 132 */       return count;
/*     */     }
/* 134 */     throw new IllegalStateException("readCount(): Stack1=" + this.mStack1 + " Stack2=" + this.mStack2);
/*     */   }
/*     */ 
/*     */   static void replace(Stack<Long> stack, TrieReader reader)
/*     */     throws IOException
/*     */   {
/* 140 */     if (stack.size() > 0)
/* 141 */       replace(stack, reader.readSymbol());
/*     */   }
/*     */ 
/*     */   static void replace(Stack<Long> stack, long x) {
/* 145 */     if (stack.size() == 0) return;
/* 146 */     stack.pop();
/* 147 */     stack.push(Long.valueOf(x));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.MultiTrieReader
 * JD-Core Version:    0.6.2
 */