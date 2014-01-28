/*     */ package org.apache.lucene.analysis.ru;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class RussianLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  67 */     len = removeCase(s, len);
/*  68 */     return normalize(s, len);
/*     */   }
/*     */ 
/*     */   private int normalize(char[] s, int len) {
/*  72 */     if (len > 3)
/*  73 */       switch (s[(len - 1)]) { case 'и':
/*     */       case 'ь':
/*  75 */         return len - 1;
/*     */       case 'н':
/*  76 */         if (s[(len - 2)] == 'н') return len - 1; break;
/*     */       }
/*  78 */     return len;
/*     */   }
/*     */ 
/*     */   private int removeCase(char[] s, int len) {
/*  82 */     if ((len > 6) && ((StemmerUtil.endsWith(s, len, "иями")) || (StemmerUtil.endsWith(s, len, "оями"))))
/*     */     {
/*  85 */       return len - 4;
/*     */     }
/*  87 */     if ((len > 5) && ((StemmerUtil.endsWith(s, len, "иям")) || (StemmerUtil.endsWith(s, len, "иях")) || (StemmerUtil.endsWith(s, len, "оях")) || (StemmerUtil.endsWith(s, len, "ями")) || (StemmerUtil.endsWith(s, len, "оям")) || (StemmerUtil.endsWith(s, len, "оьв")) || (StemmerUtil.endsWith(s, len, "ами")) || (StemmerUtil.endsWith(s, len, "его")) || (StemmerUtil.endsWith(s, len, "ему")) || (StemmerUtil.endsWith(s, len, "ери")) || (StemmerUtil.endsWith(s, len, "ими")) || (StemmerUtil.endsWith(s, len, "ого")) || (StemmerUtil.endsWith(s, len, "ому")) || (StemmerUtil.endsWith(s, len, "ыми")) || (StemmerUtil.endsWith(s, len, "оев"))))
/*     */     {
/* 103 */       return len - 3;
/*     */     }
/* 105 */     if ((len > 4) && ((StemmerUtil.endsWith(s, len, "ая")) || (StemmerUtil.endsWith(s, len, "яя")) || (StemmerUtil.endsWith(s, len, "ях")) || (StemmerUtil.endsWith(s, len, "юю")) || (StemmerUtil.endsWith(s, len, "ах")) || (StemmerUtil.endsWith(s, len, "ею")) || (StemmerUtil.endsWith(s, len, "их")) || (StemmerUtil.endsWith(s, len, "ия")) || (StemmerUtil.endsWith(s, len, "ию")) || (StemmerUtil.endsWith(s, len, "ьв")) || (StemmerUtil.endsWith(s, len, "ою")) || (StemmerUtil.endsWith(s, len, "ую")) || (StemmerUtil.endsWith(s, len, "ям")) || (StemmerUtil.endsWith(s, len, "ых")) || (StemmerUtil.endsWith(s, len, "ея")) || (StemmerUtil.endsWith(s, len, "ам")) || (StemmerUtil.endsWith(s, len, "ем")) || (StemmerUtil.endsWith(s, len, "ей")) || (StemmerUtil.endsWith(s, len, "ём")) || (StemmerUtil.endsWith(s, len, "ев")) || (StemmerUtil.endsWith(s, len, "ий")) || (StemmerUtil.endsWith(s, len, "им")) || (StemmerUtil.endsWith(s, len, "ое")) || (StemmerUtil.endsWith(s, len, "ой")) || (StemmerUtil.endsWith(s, len, "ом")) || (StemmerUtil.endsWith(s, len, "ов")) || (StemmerUtil.endsWith(s, len, "ые")) || (StemmerUtil.endsWith(s, len, "ый")) || (StemmerUtil.endsWith(s, len, "ым")) || (StemmerUtil.endsWith(s, len, "ми"))))
/*     */     {
/* 136 */       return len - 2;
/*     */     }
/* 138 */     if (len > 3)
/* 139 */       switch (s[(len - 1)]) { case 'а':
/*     */       case 'е':
/*     */       case 'и':
/*     */       case 'й':
/*     */       case 'о':
/*     */       case 'у':
/*     */       case 'ы':
/*     */       case 'ь':
/*     */       case 'я':
/* 148 */         return len - 1;
/*     */       case 'б':
/*     */       case 'в':
/*     */       case 'г':
/*     */       case 'д':
/*     */       case 'ж':
/*     */       case 'з':
/*     */       case 'к':
/*     */       case 'л':
/*     */       case 'м':
/*     */       case 'н':
/*     */       case 'п':
/*     */       case 'р':
/*     */       case 'с':
/*     */       case 'т':
/*     */       case 'ф':
/*     */       case 'х':
/*     */       case 'ц':
/*     */       case 'ч':
/*     */       case 'ш':
/*     */       case 'щ':
/*     */       case 'ъ':
/*     */       case 'э':
/* 151 */       case 'ю': }  return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ru.RussianLightStemmer
 * JD-Core Version:    0.6.2
 */