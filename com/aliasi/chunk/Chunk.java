/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.util.Scored;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public abstract interface Chunk extends Scored
/*     */ {
/* 117 */   public static final Comparator<Chunk> TEXT_ORDER_COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(Chunk c1, Chunk c2) {
/* 120 */       if (c1.start() < c2.start()) return -1;
/* 121 */       if (c1.start() > c2.start()) return 1;
/* 122 */       if (c1.end() < c2.end()) return -1;
/* 123 */       if (c1.end() > c2.end()) return 1;
/* 124 */       return 0;
/*     */     }
/* 117 */   };
/*     */ 
/* 141 */   public static final Comparator<Chunk> LONGEST_MATCH_ORDER_COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(Chunk c1, Chunk c2) {
/* 144 */       if (c1.start() < c2.start()) return -1;
/* 145 */       if (c1.start() > c2.start()) return 1;
/* 146 */       if (c1.end() < c2.end()) return 1;
/* 147 */       if (c1.end() > c2.end()) return -1;
/* 148 */       if (c1.score() > c2.score()) return -1;
/* 149 */       if (c1.score() < c2.score()) return 1;
/* 150 */       return c1.type().compareTo(c2.type());
/*     */     }
/* 141 */   };
/*     */ 
/*     */   public abstract int start();
/*     */ 
/*     */   public abstract int end();
/*     */ 
/*     */   public abstract String type();
/*     */ 
/*     */   public abstract double score();
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ 
/*     */   public abstract int hashCode();
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.Chunk
 * JD-Core Version:    0.6.2
 */