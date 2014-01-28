/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.Proximity;
/*     */ 
/*     */ public abstract class WeightedEditDistance
/*     */   implements Distance<CharSequence>, Proximity<CharSequence>
/*     */ {
/*     */   public double distance(CharSequence csIn, CharSequence csOut)
/*     */   {
/* 165 */     return -proximity(csIn, csOut);
/*     */   }
/*     */ 
/*     */   public double proximity(CharSequence csIn, CharSequence csOut)
/*     */   {
/* 181 */     return distance(csIn, csOut, true);
/*     */   }
/*     */ 
/*     */   double distance(CharSequence csIn, CharSequence csOut, boolean isSimilarity)
/*     */   {
/* 208 */     if (csOut.length() == 0) {
/* 209 */       double sum = 0.0D;
/* 210 */       for (int i = 0; i < csIn.length(); i++)
/* 211 */         sum += deleteWeight(csIn.charAt(i));
/* 212 */       return sum;
/*     */     }
/* 214 */     if (csIn.length() == 0) {
/* 215 */       double sum = 0.0D;
/* 216 */       for (int j = 0; j < csOut.length(); j++)
/* 217 */         sum += insertWeight(csOut.charAt(j));
/* 218 */       return sum;
/*     */     }
/*     */ 
/* 221 */     int xsLength = csIn.length() + 1;
/* 222 */     int ysLength = csOut.length() + 1;
/*     */ 
/* 225 */     double[] lastSlice = new double[ysLength];
/* 226 */     lastSlice[0] = 0.0D;
/* 227 */     for (int y = 1; y < ysLength; y++) {
/* 228 */       lastSlice[y] = (lastSlice[(y - 1)] + insertWeight(csOut.charAt(y - 1)));
/*     */     }
/*     */ 
/* 231 */     double[] currentSlice = new double[ysLength];
/* 232 */     currentSlice[0] = insertWeight(csOut.charAt(0));
/* 233 */     char cX = csIn.charAt(0);
/* 234 */     for (int y = 1; y < ysLength; y++) {
/* 235 */       int yMinus1 = y - 1;
/* 236 */       char cY = csOut.charAt(yMinus1);
/* 237 */       double matchSubstWeight = lastSlice[yMinus1] + (cX == cY ? matchWeight(cX) : substituteWeight(cX, cY));
/*     */ 
/* 240 */       double deleteWeight = lastSlice[y] + deleteWeight(cX);
/* 241 */       double insertWeight = currentSlice[yMinus1] + insertWeight(cY);
/* 242 */       currentSlice[y] = best(isSimilarity, matchSubstWeight, deleteWeight, insertWeight);
/*     */     }
/*     */ 
/* 249 */     if (xsLength == 2) return currentSlice[(currentSlice.length - 1)];
/*     */ 
/* 251 */     char cYZero = csOut.charAt(0);
/* 252 */     double[] twoLastSlice = new double[ysLength];
/*     */ 
/* 255 */     for (int x = 2; x < xsLength; x++) {
/* 256 */       char cXMinus1 = cX;
/* 257 */       cX = csIn.charAt(x - 1);
/*     */ 
/* 260 */       double[] tmpSlice = twoLastSlice;
/* 261 */       twoLastSlice = lastSlice;
/* 262 */       lastSlice = currentSlice;
/* 263 */       currentSlice = tmpSlice;
/*     */ 
/* 265 */       lastSlice[0] += deleteWeight(cX);
/*     */ 
/* 268 */       currentSlice[1] = best(isSimilarity, cX == cYZero ? lastSlice[0] + matchWeight(cX) : lastSlice[0] + substituteWeight(cX, cYZero), lastSlice[1] + deleteWeight(cX), currentSlice[0] + insertWeight(cYZero));
/*     */ 
/* 276 */       char cY = cYZero;
/* 277 */       for (int y = 2; y < ysLength; y++) {
/* 278 */         int yMinus1 = y - 1;
/* 279 */         char cYMinus1 = cY;
/* 280 */         cY = csOut.charAt(yMinus1);
/* 281 */         currentSlice[y] = best(isSimilarity, cX == cY ? lastSlice[yMinus1] + matchWeight(cX) : lastSlice[yMinus1] + substituteWeight(cX, cY), lastSlice[y] + deleteWeight(cX), currentSlice[yMinus1] + insertWeight(cY));
/*     */ 
/* 287 */         if ((cX == cYMinus1) && (cY == cXMinus1)) {
/* 288 */           currentSlice[y] = best(isSimilarity, currentSlice[y], twoLastSlice[(y - 2)] + transposeWeight(cXMinus1, cX));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 293 */     return currentSlice[(currentSlice.length - 1)];
/*     */   }
/*     */ 
/*     */   private double best(boolean isSimilarity, double x, double y, double z) {
/* 297 */     return best(isSimilarity, x, best(isSimilarity, y, z));
/*     */   }
/*     */ 
/*     */   private double best(boolean isSimilarity, double x, double y) {
/* 301 */     return isSimilarity ? Math.max(x, y) : Math.min(x, y);
/*     */   }
/*     */ 
/*     */   public abstract double matchWeight(char paramChar);
/*     */ 
/*     */   public abstract double deleteWeight(char paramChar);
/*     */ 
/*     */   public abstract double insertWeight(char paramChar);
/*     */ 
/*     */   public abstract double substituteWeight(char paramChar1, char paramChar2);
/*     */ 
/*     */   public abstract double transposeWeight(char paramChar1, char paramChar2);
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.WeightedEditDistance
 * JD-Core Version:    0.6.2
 */