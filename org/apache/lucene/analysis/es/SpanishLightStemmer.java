/*     */ package org.apache.lucene.analysis.es;
/*     */ 
/*     */ public class SpanishLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  65 */     if (len < 5) {
/*  66 */       return len;
/*     */     }
/*  68 */     for (int i = 0; i < len; i++)
/*  69 */       switch (s[i]) { case 'à':
/*     */       case 'á':
/*     */       case 'â':
/*     */       case 'ä':
/*  73 */         s[i] = 'a'; break;
/*     */       case 'ò':
/*     */       case 'ó':
/*     */       case 'ô':
/*     */       case 'ö':
/*  77 */         s[i] = 'o'; break;
/*     */       case 'è':
/*     */       case 'é':
/*     */       case 'ê':
/*     */       case 'ë':
/*  81 */         s[i] = 'e'; break;
/*     */       case 'ù':
/*     */       case 'ú':
/*     */       case 'û':
/*     */       case 'ü':
/*  85 */         s[i] = 'u'; break;
/*     */       case 'ì':
/*     */       case 'í':
/*     */       case 'î':
/*     */       case 'ï':
/*  89 */         s[i] = 'i';
/*     */       case 'ã':
/*     */       case 'å':
/*     */       case 'æ':
/*     */       case 'ç':
/*     */       case 'ð':
/*     */       case 'ñ':
/*     */       case 'õ':
/*     */       case '÷':
/*  92 */       case 'ø': }  switch (s[(len - 1)]) { case 'a':
/*     */     case 'e':
/*     */     case 'o':
/*  95 */       return len - 1;
/*     */     case 's':
/*  97 */       if ((s[(len - 2)] == 'e') && (s[(len - 3)] == 's') && (s[(len - 4)] == 'e'))
/*  98 */         return len - 2;
/*  99 */       if ((s[(len - 2)] == 'e') && (s[(len - 3)] == 'c')) {
/* 100 */         s[(len - 3)] = 'z';
/* 101 */         return len - 2;
/*     */       }
/* 103 */       if ((s[(len - 2)] == 'o') || (s[(len - 2)] == 'a') || (s[(len - 2)] == 'e'))
/* 104 */         return len - 2;
/*     */       break;
/*     */     }
/* 107 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.es.SpanishLightStemmer
 * JD-Core Version:    0.6.2
 */