/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import org.apache.lucene.search.DocIdSetIterator;
/*     */ 
/*     */ public class OpenBitSetIterator extends DocIdSetIterator
/*     */ {
/*  35 */   protected static final int[] bitlist = { 0, 1, 2, 33, 3, 49, 50, 801, 4, 65, 66, 1057, 67, 1073, 1074, 17185, 5, 81, 82, 1313, 83, 1329, 1330, 21281, 84, 1345, 1346, 21537, 1347, 21553, 21554, 344865, 6, 97, 98, 1569, 99, 1585, 1586, 25377, 100, 1601, 1602, 25633, 1603, 25649, 25650, 410401, 101, 1617, 1618, 25889, 1619, 25905, 25906, 414497, 1620, 25921, 25922, 414753, 25923, 414769, 414770, 6636321, 7, 113, 114, 1825, 115, 1841, 1842, 29473, 116, 1857, 1858, 29729, 1859, 29745, 29746, 475937, 117, 1873, 1874, 29985, 1875, 30001, 30002, 480033, 1876, 30017, 30018, 480289, 30019, 480305, 480306, 7684897, 118, 1889, 1890, 30241, 1891, 30257, 30258, 484129, 1892, 30273, 30274, 484385, 30275, 484401, 484402, 7750433, 1893, 30289, 30290, 484641, 30291, 484657, 484658, 7754529, 30292, 484673, 484674, 7754785, 484675, 7754801, 7754802, 124076833, 8, 129, 130, 2081, 131, 2097, 2098, 33569, 132, 2113, 2114, 33825, 2115, 33841, 33842, 541473, 133, 2129, 2130, 34081, 2131, 34097, 34098, 545569, 2132, 34113, 34114, 545825, 34115, 545841, 545842, 8733473, 134, 2145, 2146, 34337, 2147, 34353, 34354, 549665, 2148, 34369, 34370, 549921, 34371, 549937, 549938, 8799009, 2149, 34385, 34386, 550177, 34387, 550193, 550194, 8803105, 34388, 550209, 550210, 8803361, 550211, 8803377, 8803378, 140854049, 135, 2161, 2162, 34593, 2163, 34609, 34610, 553761, 2164, 34625, 34626, 554017, 34627, 554033, 554034, 8864545, 2165, 34641, 34642, 554273, 34643, 554289, 554290, 8868641, 34644, 554305, 554306, 8868897, 554307, 8868913, 8868914, 141902625, 2166, 34657, 34658, 554529, 34659, 554545, 554546, 8872737, 34660, 554561, 554562, 8872993, 554563, 8873009, 8873010, 141968161, 34661, 554577, 554578, 8873249, 554579, 8873265, 8873266, 141972257, 554580, 8873281, 8873282, 141972513, 8873283, 141972529, 141972530, -2023406815 };
/*     */   private final long[] arr;
/*     */   private final int words;
/*  86 */   private int i = -1;
/*     */   private long word;
/*     */   private int wordShift;
/*     */   private int indexArray;
/*  90 */   private int curDocId = -1;
/*     */ 
/*     */   public OpenBitSetIterator(OpenBitSet obs) {
/*  93 */     this(obs.getBits(), obs.getNumWords());
/*     */   }
/*     */ 
/*     */   public OpenBitSetIterator(long[] bits, int numWords) {
/*  97 */     this.arr = bits;
/*  98 */     this.words = numWords;
/*     */   }
/*     */ 
/*     */   private void shift()
/*     */   {
/* 103 */     if ((int)this.word == 0) { this.wordShift += 32; this.word >>>= 32; }
/* 104 */     if ((this.word & 0xFFFF) == 0L) { this.wordShift += 16; this.word >>>= 16; }
/* 105 */     if ((this.word & 0xFF) == 0L) { this.wordShift += 8; this.word >>>= 8; }
/* 106 */     this.indexArray = bitlist[((int)this.word & 0xFF)];
/*     */   }
/*     */ 
/*     */   public int nextDoc()
/*     */   {
/* 133 */     if (this.indexArray == 0) {
/* 134 */       if (this.word != 0L) {
/* 135 */         this.word >>>= 8;
/* 136 */         this.wordShift += 8;
/*     */       }
/*     */ 
/* 139 */       while (this.word == 0L) {
/* 140 */         if (++this.i >= this.words) {
/* 141 */           return this.curDocId = 2147483647;
/*     */         }
/* 143 */         this.word = this.arr[this.i];
/* 144 */         this.wordShift = -1;
/*     */       }
/*     */ 
/* 149 */       shift();
/*     */     }
/*     */ 
/* 152 */     int bitIndex = (this.indexArray & 0xF) + this.wordShift;
/* 153 */     this.indexArray >>>= 4;
/*     */ 
/* 156 */     return this.curDocId = (this.i << 6) + bitIndex;
/*     */   }
/*     */ 
/*     */   public int advance(int target)
/*     */   {
/* 161 */     this.indexArray = 0;
/* 162 */     this.i = (target >> 6);
/* 163 */     if (this.i >= this.words) {
/* 164 */       this.word = 0L;
/* 165 */       return this.curDocId = 2147483647;
/*     */     }
/* 167 */     this.wordShift = (target & 0x3F);
/* 168 */     this.word = (this.arr[this.i] >>> this.wordShift);
/* 169 */     if (this.word != 0L) {
/* 170 */       this.wordShift -= 1;
/*     */     } else {
/* 172 */       while (this.word == 0L) {
/* 173 */         if (++this.i >= this.words) {
/* 174 */           return this.curDocId = 2147483647;
/*     */         }
/* 176 */         this.word = this.arr[this.i];
/*     */       }
/* 178 */       this.wordShift = -1;
/*     */     }
/*     */ 
/* 181 */     shift();
/*     */ 
/* 183 */     int bitIndex = (this.indexArray & 0xF) + this.wordShift;
/* 184 */     this.indexArray >>>= 4;
/*     */ 
/* 187 */     return this.curDocId = (this.i << 6) + bitIndex;
/*     */   }
/*     */ 
/*     */   public int docID()
/*     */   {
/* 192 */     return this.curDocId;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.OpenBitSetIterator
 * JD-Core Version:    0.6.2
 */