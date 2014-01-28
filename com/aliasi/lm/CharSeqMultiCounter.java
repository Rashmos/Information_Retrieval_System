/*     */ package com.aliasi.lm;
/*     */ 
/*     */ public class CharSeqMultiCounter
/*     */   implements CharSeqCounter
/*     */ {
/*     */   private final CharSeqCounter mCounter1;
/*     */   private final CharSeqCounter mCounter2;
/*     */ 
/*     */   public CharSeqMultiCounter(CharSeqCounter[] counters)
/*     */   {
/*  81 */     this(counter(counters, 0, counters.length / 2), counter(counters, counters.length / 2, counters.length));
/*     */   }
/*     */ 
/*     */   public CharSeqMultiCounter(CharSeqCounter counter1, CharSeqCounter counter2)
/*     */   {
/*  93 */     this.mCounter1 = counter1;
/*  94 */     this.mCounter2 = counter2;
/*     */   }
/*     */ 
/*     */   public long count(char[] cs, int start, int end) {
/*  98 */     return this.mCounter1.count(cs, start, end) + this.mCounter2.count(cs, start, end);
/*     */   }
/*     */ 
/*     */   public long extensionCount(char[] cs, int start, int end)
/*     */   {
/* 103 */     return this.mCounter1.extensionCount(cs, start, end) + this.mCounter2.extensionCount(cs, start, end);
/*     */   }
/*     */ 
/*     */   public int numCharactersFollowing(char[] cs, int start, int end)
/*     */   {
/* 108 */     char[] cs1 = this.mCounter1.charactersFollowing(cs, start, end);
/* 109 */     char[] cs2 = this.mCounter2.charactersFollowing(cs, start, end);
/* 110 */     return unionSize(cs1, cs2);
/*     */   }
/*     */ 
/*     */   public char[] charactersFollowing(char[] cs, int start, int end) {
/* 114 */     char[] cs1 = this.mCounter1.charactersFollowing(cs, start, end);
/* 115 */     char[] cs2 = this.mCounter2.charactersFollowing(cs, start, end);
/* 116 */     return orderedUnion(cs1, cs2);
/*     */   }
/*     */ 
/*     */   public char[] observedCharacters() {
/* 120 */     return charactersFollowing(new char[0], 0, 0);
/*     */   }
/*     */ 
/*     */   private int unionSize(char[] cs1, char[] cs2) {
/* 124 */     int count = 0;
/* 125 */     int i1 = 0;
/* 126 */     int i2 = 0;
/* 127 */     while (i1 < cs1.length) {
/* 128 */       while ((i2 < cs2.length) && (cs2[i2] < cs1[i1])) {
/* 129 */         i2++;
/* 130 */         count++;
/*     */       }
/* 132 */       if ((i2 < cs2.length) && (cs2[i2] == cs1[i1]))
/* 133 */         i2++;
/* 134 */       count++;
/* 135 */       i1++;
/*     */     }
/* 137 */     return count + cs2.length - i2;
/*     */   }
/*     */ 
/*     */   private char[] orderedUnion(char[] cs1, char[] cs2) {
/* 141 */     char[] cs = new char[unionSize(cs1, cs2)];
/* 142 */     int i = 0;
/* 143 */     int i1 = 0;
/* 144 */     int i2 = 0;
/* 145 */     while (i1 < cs1.length) {
/* 146 */       while ((i2 < cs2.length) && (cs2[i2] < cs1[i1]))
/* 147 */         cs[(i++)] = cs2[(i2++)];
/* 148 */       if ((i2 < cs2.length) && (cs1[i1] == cs2[i2]))
/* 149 */         i2++;
/* 150 */       cs[(i++)] = cs1[(i1++)];
/*     */     }
/* 152 */     System.arraycopy(cs2, i2, cs, i, cs2.length - i2);
/* 153 */     return cs;
/*     */   }
/*     */ 
/*     */   private static CharSeqCounter counter(CharSeqCounter[] counters, int start, int end)
/*     */   {
/* 158 */     if (end <= start) {
/* 159 */       String msg = "Too few counters provided.";
/* 160 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 162 */     if (end - start == 1)
/* 163 */       return counters[start];
/* 164 */     int mid = start + (end - start) / 2;
/* 165 */     return new CharSeqMultiCounter(counter(counters, start, mid), counter(counters, mid, end));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.CharSeqMultiCounter
 * JD-Core Version:    0.6.2
 */