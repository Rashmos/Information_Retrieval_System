/*     */ package org.apache.lucene.analysis.sv;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class SwedishLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  67 */     if ((len > 4) && (s[(len - 1)] == 's')) {
/*  68 */       len--;
/*     */     }
/*  70 */     if ((len > 7) && ((StemmerUtil.endsWith(s, len, "elser")) || (StemmerUtil.endsWith(s, len, "heten"))))
/*     */     {
/*  73 */       return len - 5;
/*     */     }
/*  75 */     if ((len > 6) && ((StemmerUtil.endsWith(s, len, "arne")) || (StemmerUtil.endsWith(s, len, "erna")) || (StemmerUtil.endsWith(s, len, "ande")) || (StemmerUtil.endsWith(s, len, "else")) || (StemmerUtil.endsWith(s, len, "aste")) || (StemmerUtil.endsWith(s, len, "orna")) || (StemmerUtil.endsWith(s, len, "aren"))))
/*     */     {
/*  83 */       return len - 4;
/*     */     }
/*  85 */     if ((len > 5) && ((StemmerUtil.endsWith(s, len, "are")) || (StemmerUtil.endsWith(s, len, "ast")) || (StemmerUtil.endsWith(s, len, "het"))))
/*     */     {
/*  89 */       return len - 3;
/*     */     }
/*  91 */     if ((len > 4) && ((StemmerUtil.endsWith(s, len, "ar")) || (StemmerUtil.endsWith(s, len, "er")) || (StemmerUtil.endsWith(s, len, "or")) || (StemmerUtil.endsWith(s, len, "en")) || (StemmerUtil.endsWith(s, len, "at")) || (StemmerUtil.endsWith(s, len, "te")) || (StemmerUtil.endsWith(s, len, "et"))))
/*     */     {
/*  99 */       return len - 2;
/*     */     }
/* 101 */     if (len > 3) {
/* 102 */       switch (s[(len - 1)]) { case 'a':
/*     */       case 'e':
/*     */       case 'n':
/*     */       case 't':
/* 106 */         return len - 1;
/*     */       }
/*     */     }
/* 109 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.sv.SwedishLightStemmer
 * JD-Core Version:    0.6.2
 */