/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.Proximity;
/*     */ 
/*     */ public class EditDistance
/*     */   implements Distance<CharSequence>, Proximity<CharSequence>
/*     */ {
/*     */   private final boolean mAllowTransposition;
/* 304 */   public static final Distance<CharSequence> TRANSPOSING = new EditDistance(true);
/*     */ 
/* 311 */   public static final Distance<CharSequence> NON_TRANSPOSING = new EditDistance(false);
/*     */ 
/*     */   public EditDistance(boolean allowTransposition)
/*     */   {
/* 118 */     this.mAllowTransposition = allowTransposition;
/*     */   }
/*     */ 
/*     */   public double distance(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 132 */     return editDistance(cSeq1, cSeq2, this.mAllowTransposition);
/*     */   }
/*     */ 
/*     */   public double proximity(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 151 */     return -distance(cSeq1, cSeq2);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 161 */     return "EditDistance(" + this.mAllowTransposition + ")";
/*     */   }
/*     */ 
/*     */   public static int editDistance(CharSequence cSeq1, CharSequence cSeq2, boolean allowTransposition)
/*     */   {
/* 180 */     if (cSeq1.length() < cSeq2.length()) {
/* 181 */       CharSequence temp = cSeq1;
/* 182 */       cSeq1 = cSeq2;
/* 183 */       cSeq2 = temp;
/*     */     }
/*     */ 
/* 187 */     if (cSeq2.length() == 0) return cSeq1.length();
/* 188 */     if (cSeq2.length() == 1) {
/* 189 */       char c = cSeq2.charAt(0);
/* 190 */       for (int i = 0; i < cSeq1.length(); i++)
/* 191 */         if (cSeq1.charAt(i) == c)
/* 192 */           return cSeq1.length() - 1;
/* 193 */       return cSeq1.length();
/*     */     }
/*     */ 
/* 196 */     if (allowTransposition)
/* 197 */       return editDistanceTranspose(cSeq1, cSeq2);
/* 198 */     return editDistanceNonTranspose(cSeq1, cSeq2);
/*     */   }
/*     */ 
/*     */   private static int editDistanceNonTranspose(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 204 */     int xsLength = cSeq1.length() + 1;
/* 205 */     int ysLength = cSeq2.length() + 1;
/*     */ 
/* 207 */     int[] lastSlice = new int[ysLength];
/* 208 */     int[] currentSlice = new int[ysLength];
/*     */ 
/* 211 */     for (int y = 0; y < ysLength; y++) {
/* 212 */       currentSlice[y] = y;
/*     */     }
/* 214 */     for (int x = 1; x < xsLength; x++) {
/* 215 */       char cX = cSeq1.charAt(x - 1);
/* 216 */       int[] lastSliceTmp = lastSlice;
/* 217 */       lastSlice = currentSlice;
/* 218 */       currentSlice = lastSliceTmp;
/* 219 */       currentSlice[0] = x;
/* 220 */       for (int y = 1; y < ysLength; y++) {
/* 221 */         int yMinus1 = y - 1;
/*     */ 
/* 223 */         currentSlice[y] = Math.min(cX == cSeq2.charAt(yMinus1) ? lastSlice[yMinus1] : 1 + lastSlice[yMinus1], 1 + Math.min(lastSlice[y], currentSlice[yMinus1]));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 230 */     return currentSlice[(currentSlice.length - 1)];
/*     */   }
/*     */ 
/*     */   private static int editDistanceTranspose(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 237 */     int xsLength = cSeq1.length() + 1;
/* 238 */     int ysLength = cSeq2.length() + 1;
/*     */ 
/* 240 */     int[] twoLastSlice = new int[ysLength];
/* 241 */     int[] lastSlice = new int[ysLength];
/* 242 */     int[] currentSlice = new int[ysLength];
/*     */ 
/* 245 */     for (int y = 0; y < ysLength; y++) {
/* 246 */       lastSlice[y] = y;
/*     */     }
/*     */ 
/* 249 */     currentSlice[0] = 1;
/* 250 */     char cX = cSeq1.charAt(0);
/* 251 */     for (int y = 1; y < ysLength; y++) {
/* 252 */       int yMinus1 = y - 1;
/* 253 */       currentSlice[y] = Math.min(cX == cSeq2.charAt(yMinus1) ? lastSlice[yMinus1] : 1 + lastSlice[yMinus1], 1 + Math.min(lastSlice[y], currentSlice[yMinus1]));
/*     */     }
/*     */ 
/* 260 */     char cYZero = cSeq2.charAt(0);
/*     */ 
/* 263 */     for (int x = 2; x < xsLength; x++) {
/* 264 */       char cXMinus1 = cX;
/* 265 */       cX = cSeq1.charAt(x - 1);
/*     */ 
/* 268 */       int[] tmpSlice = twoLastSlice;
/* 269 */       twoLastSlice = lastSlice;
/* 270 */       lastSlice = currentSlice;
/* 271 */       currentSlice = tmpSlice;
/*     */ 
/* 273 */       currentSlice[0] = x;
/*     */ 
/* 276 */       currentSlice[1] = Math.min(cX == cYZero ? lastSlice[0] : 1 + lastSlice[0], 1 + Math.min(lastSlice[1], currentSlice[0]));
/*     */ 
/* 283 */       char cY = cYZero;
/* 284 */       for (int y = 2; y < ysLength; y++) {
/* 285 */         int yMinus1 = y - 1;
/* 286 */         char cYMinus1 = cY;
/* 287 */         cY = cSeq2.charAt(yMinus1);
/* 288 */         currentSlice[y] = Math.min(cX == cY ? lastSlice[yMinus1] : 1 + lastSlice[yMinus1], 1 + Math.min(lastSlice[y], currentSlice[yMinus1]));
/*     */ 
/* 293 */         if ((cX == cYMinus1) && (cY == cXMinus1))
/* 294 */           currentSlice[y] = Math.min(currentSlice[y], 1 + twoLastSlice[(y - 2)]);
/*     */       }
/*     */     }
/* 297 */     return currentSlice[(currentSlice.length - 1)];
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.EditDistance
 * JD-Core Version:    0.6.2
 */