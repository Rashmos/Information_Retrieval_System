/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import java.util.List;
/*     */ 
/*     */ class IntNode
/*     */ {
/*     */   int mCount;
/*     */   long mExtCount;
/*     */   DtrMap mDtrs;
/* 215 */   static final int[] EMPTY_INT_ARRAY = new int[0];
/*     */ 
/*     */   IntNode()
/*     */   {
/*  37 */     this.mCount = 0;
/*  38 */     this.mExtCount = 0L;
/*  39 */     this.mDtrs = DtrMap0.EMPTY_DTR_MAP;
/*     */   }
/*     */   IntNode(int[] toks, int start, int end) {
/*  42 */     this.mCount = 1;
/*  43 */     if (start == end) {
/*  44 */       this.mDtrs = DtrMap0.EMPTY_DTR_MAP;
/*  45 */       this.mExtCount = 0L;
/*  46 */       return;
/*     */     }
/*  48 */     this.mExtCount = 1L;
/*  49 */     int tok = toks[start];
/*  50 */     IntNode dtr = new IntNode(toks, start + 1, end);
/*  51 */     this.mDtrs = new DtrMap1(tok, dtr);
/*     */   }
/*     */   IntNode(int[] toks, int start, int end, int count) {
/*  54 */     this.mCount = count;
/*  55 */     if (start == end) {
/*  56 */       this.mDtrs = DtrMap0.EMPTY_DTR_MAP;
/*  57 */       this.mExtCount = 0L;
/*  58 */       return;
/*     */     }
/*  60 */     this.mExtCount = count;
/*  61 */     int tok = toks[start];
/*  62 */     IntNode dtr = new IntNode(toks, start + 1, end, count);
/*  63 */     this.mDtrs = new DtrMap1(tok, dtr);
/*     */   }
/*     */ 
/*     */   IntNode(int[] toks, int start, int end, int count, boolean incrementPath) {
/*  67 */     if (incrementPath)
/*  68 */       throw new IllegalArgumentException("require true");
/*  69 */     if (start == end) {
/*  70 */       this.mCount = count;
/*  71 */       this.mDtrs = DtrMap0.EMPTY_DTR_MAP;
/*  72 */       this.mExtCount = 0L;
/*  73 */       return;
/*     */     }
/*  75 */     this.mCount = 0;
/*  76 */     this.mExtCount = (start + 1 == end ? count : 0L);
/*  77 */     int tok = toks[start];
/*  78 */     IntNode dtr = new IntNode(toks, start + 1, end, count, incrementPath);
/*  79 */     this.mDtrs = new DtrMap1(tok, dtr);
/*     */   }
/*     */   public void prune(int minCount) {
/*  82 */     this.mDtrs = this.mDtrs.prune(minCount);
/*  83 */     this.mExtCount = this.mDtrs.extensionCount();
/*     */   }
/*     */   public void rescale(double countMultiplier) {
/*  86 */     this.mCount = ((int)(countMultiplier * this.mCount));
/*  87 */     this.mDtrs = this.mDtrs.rescale(countMultiplier);
/*  88 */     this.mExtCount = this.mDtrs.extensionCount();
/*     */   }
/*     */   public static String idToSymbol(int id, SymbolTable st) {
/*  91 */     if (id == -2) return "EOS";
/*  92 */     if (id == -1) return "UNK";
/*  93 */     return st.idToSymbol(id);
/*     */   }
/*     */   int trieSize() {
/*  96 */     return 1 + this.mDtrs.dtrsTrieSize();
/*     */   }
/*     */   void decrement(int symbol) {
/*  99 */     IntNode dtr = this.mDtrs.getDtr(symbol);
/* 100 */     if (dtr == null) {
/* 101 */       String msg = "symbol doesn't exist=" + symbol;
/* 102 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 104 */     if (this.mCount <= 0) {
/* 105 */       String msg = "Cannot decrement below zero.";
/* 106 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 108 */     if (this.mExtCount < 1L) {
/* 109 */       String msg = "Cannot decrement extensions below zero.";
/* 110 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 112 */     this.mCount -= 1;
/* 113 */     this.mExtCount -= 1L;
/* 114 */     dtr.decrement();
/*     */   }
/*     */   private void decrement() {
/* 117 */     if (this.mCount == 0) {
/* 118 */       String msg = "Cannot decrement below 0.";
/* 119 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 121 */     this.mCount -= 1;
/*     */   }
/*     */   void decrement(int symbol, int count) {
/* 124 */     IntNode dtr = this.mDtrs.getDtr(symbol);
/* 125 */     if (dtr == null) {
/* 126 */       String msg = "symbol doesn't exist=" + symbol;
/* 127 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 129 */     if (this.mCount - count < 0) {
/* 130 */       String msg = "Cannot decrement below zero. Count=" + this.mCount + " decrement=" + count;
/*     */ 
/* 132 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 134 */     if (this.mExtCount - count < 0L) {
/* 135 */       String msg = "Cannot decrement extension count below zero. Ext count=" + this.mExtCount + " decrement=" + count;
/*     */ 
/* 137 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 139 */     this.mCount -= count;
/* 140 */     this.mExtCount -= count;
/* 141 */     dtr.decrementCount(count);
/*     */   }
/*     */   private void decrementCount(int count) {
/* 144 */     if (this.mCount - count < 0) {
/* 145 */       String msg = "Cannot decrement below 0. Count=" + this.mCount + " decrement=" + count;
/*     */ 
/* 147 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 149 */     this.mCount -= count;
/*     */   }
/*     */   int count() {
/* 152 */     return this.mCount;
/*     */   }
/*     */   void addDaughters(List<IntNode> queue) {
/* 155 */     this.mDtrs.addDtrs(queue);
/*     */   }
/*     */   long extensionCount() {
/* 158 */     return this.mExtCount;
/*     */   }
/*     */   int numExtensions() {
/* 161 */     return this.mDtrs.numExtensions();
/*     */   }
/*     */   int[] integersFollowing() {
/* 164 */     return this.mDtrs.integersFollowing();
/*     */   }
/*     */   int[] integersFollowing(int[] is, int start, int end) {
/* 167 */     IntNode dtr = getDtr(is, start, end);
/* 168 */     if (dtr == null) return EMPTY_INT_ARRAY;
/* 169 */     return dtr.integersFollowing();
/*     */   }
/*     */   int[] observedIntegers() {
/* 172 */     return integersFollowing(EMPTY_INT_ARRAY, 0, 0);
/*     */   }
/*     */   void incrementSequence(int[] tokIndices, int start, int end, int count) {
/* 175 */     if (start == end) {
/* 176 */       this.mCount += count;
/* 177 */       return;
/*     */     }
/* 179 */     if (start + 1 == end)
/* 180 */       this.mExtCount += count;
/* 181 */     DtrMap newDtrs = this.mDtrs.incrementSequence(tokIndices, start, end, count);
/* 182 */     if (!newDtrs.equals(this.mDtrs)) this.mDtrs = newDtrs; 
/*     */   }
/*     */ 
/*     */   void increment(int[] tokIndices, int start, int end)
/*     */   {
/* 186 */     this.mCount += 1;
/* 187 */     if (start == end) return;
/* 188 */     this.mExtCount += 1L;
/* 189 */     DtrMap newDtrs = this.mDtrs.incrementDtrs(tokIndices, start, end);
/* 190 */     if (!newDtrs.equals(this.mDtrs)) this.mDtrs = newDtrs; 
/*     */   }
/*     */ 
/* 193 */   void increment(int[] tokIndices, int start, int end, int count) { this.mCount += count;
/* 194 */     if (start == end) return;
/* 195 */     this.mExtCount += count;
/* 196 */     DtrMap newDtrs = this.mDtrs.incrementDtrs(tokIndices, start, end, count);
/* 197 */     if (!newDtrs.equals(this.mDtrs)) this.mDtrs = newDtrs;  } 
/*     */   IntNode getDtr(int[] toks, int start, int end)
/*     */   {
/* 200 */     if (start == end) return this;
/* 201 */     IntNode dtr = this.mDtrs.getDtr(toks[start]);
/* 202 */     if (dtr == null) return null;
/* 203 */     return dtr.getDtr(toks, start + 1, end);
/*     */   }
/*     */   public String toString(SymbolTable st) {
/* 206 */     StringBuilder sb = new StringBuilder();
/* 207 */     toString(sb, 0, st);
/* 208 */     return sb.toString();
/*     */   }
/*     */   public void toString(StringBuilder sb, int depth, SymbolTable st) {
/* 211 */     sb.append(count());
/* 212 */     AbstractNode.indent(sb, depth);
/* 213 */     this.mDtrs.toString(sb, depth, st);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.IntNode
 * JD-Core Version:    0.6.2
 */