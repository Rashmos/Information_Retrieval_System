/*     */ package org.apache.lucene.analysis.hi;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class HindiStemmer
/*     */ {
/*     */   public int stem(char[] buffer, int len)
/*     */   {
/*  34 */     if ((len > 6) && ((StemmerUtil.endsWith(buffer, len, "ाएंगी")) || (StemmerUtil.endsWith(buffer, len, "ाएंगे")) || (StemmerUtil.endsWith(buffer, len, "ाऊंगी")) || (StemmerUtil.endsWith(buffer, len, "ाऊंगा")) || (StemmerUtil.endsWith(buffer, len, "ाइयाँ")) || (StemmerUtil.endsWith(buffer, len, "ाइयों")) || (StemmerUtil.endsWith(buffer, len, "ाइयां"))))
/*     */     {
/*  42 */       return len - 5;
/*     */     }
/*     */ 
/*  45 */     if ((len > 5) && ((StemmerUtil.endsWith(buffer, len, "ाएगी")) || (StemmerUtil.endsWith(buffer, len, "ाएगा")) || (StemmerUtil.endsWith(buffer, len, "ाओगी")) || (StemmerUtil.endsWith(buffer, len, "ाओगे")) || (StemmerUtil.endsWith(buffer, len, "एंगी")) || (StemmerUtil.endsWith(buffer, len, "ेंगी")) || (StemmerUtil.endsWith(buffer, len, "एंगे")) || (StemmerUtil.endsWith(buffer, len, "ेंगे")) || (StemmerUtil.endsWith(buffer, len, "ूंगी")) || (StemmerUtil.endsWith(buffer, len, "ूंगा")) || (StemmerUtil.endsWith(buffer, len, "ातीं")) || (StemmerUtil.endsWith(buffer, len, "नाओं")) || (StemmerUtil.endsWith(buffer, len, "नाएं")) || (StemmerUtil.endsWith(buffer, len, "ताओं")) || (StemmerUtil.endsWith(buffer, len, "ताएं")) || (StemmerUtil.endsWith(buffer, len, "ियाँ")) || (StemmerUtil.endsWith(buffer, len, "ियों")) || (StemmerUtil.endsWith(buffer, len, "ियां"))))
/*     */     {
/*  64 */       return len - 4;
/*     */     }
/*     */ 
/*  67 */     if ((len > 4) && ((StemmerUtil.endsWith(buffer, len, "ाकर")) || (StemmerUtil.endsWith(buffer, len, "ाइए")) || (StemmerUtil.endsWith(buffer, len, "ाईं")) || (StemmerUtil.endsWith(buffer, len, "ाया")) || (StemmerUtil.endsWith(buffer, len, "ेगी")) || (StemmerUtil.endsWith(buffer, len, "ेगा")) || (StemmerUtil.endsWith(buffer, len, "ोगी")) || (StemmerUtil.endsWith(buffer, len, "ोगे")) || (StemmerUtil.endsWith(buffer, len, "ाने")) || (StemmerUtil.endsWith(buffer, len, "ाना")) || (StemmerUtil.endsWith(buffer, len, "ाते")) || (StemmerUtil.endsWith(buffer, len, "ाती")) || (StemmerUtil.endsWith(buffer, len, "ाता")) || (StemmerUtil.endsWith(buffer, len, "तीं")) || (StemmerUtil.endsWith(buffer, len, "ाओं")) || (StemmerUtil.endsWith(buffer, len, "ाएं")) || (StemmerUtil.endsWith(buffer, len, "ुओं")) || (StemmerUtil.endsWith(buffer, len, "ुएं")) || (StemmerUtil.endsWith(buffer, len, "ुआं"))))
/*     */     {
/*  87 */       return len - 3;
/*     */     }
/*     */ 
/*  90 */     if ((len > 3) && ((StemmerUtil.endsWith(buffer, len, "कर")) || (StemmerUtil.endsWith(buffer, len, "ाओ")) || (StemmerUtil.endsWith(buffer, len, "िए")) || (StemmerUtil.endsWith(buffer, len, "ाई")) || (StemmerUtil.endsWith(buffer, len, "ाए")) || (StemmerUtil.endsWith(buffer, len, "ने")) || (StemmerUtil.endsWith(buffer, len, "नी")) || (StemmerUtil.endsWith(buffer, len, "ना")) || (StemmerUtil.endsWith(buffer, len, "ते")) || (StemmerUtil.endsWith(buffer, len, "ीं")) || (StemmerUtil.endsWith(buffer, len, "ती")) || (StemmerUtil.endsWith(buffer, len, "ता")) || (StemmerUtil.endsWith(buffer, len, "ाँ")) || (StemmerUtil.endsWith(buffer, len, "ां")) || (StemmerUtil.endsWith(buffer, len, "ों")) || (StemmerUtil.endsWith(buffer, len, "ें"))))
/*     */     {
/* 107 */       return len - 2;
/*     */     }
/*     */ 
/* 110 */     if ((len > 2) && ((StemmerUtil.endsWith(buffer, len, "ो")) || (StemmerUtil.endsWith(buffer, len, "े")) || (StemmerUtil.endsWith(buffer, len, "ू")) || (StemmerUtil.endsWith(buffer, len, "ु")) || (StemmerUtil.endsWith(buffer, len, "ी")) || (StemmerUtil.endsWith(buffer, len, "ि")) || (StemmerUtil.endsWith(buffer, len, "ा"))))
/*     */     {
/* 118 */       return len - 1;
/* 119 */     }return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hi.HindiStemmer
 * JD-Core Version:    0.6.2
 */