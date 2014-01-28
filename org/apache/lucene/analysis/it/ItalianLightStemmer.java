/*     */ package org.apache.lucene.analysis.it;
/*     */ 
/*     */ public class ItalianLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  65 */     if (len < 6) {
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
/*  92 */       case 'ø': }  switch (s[(len - 1)]) {
/*     */     case 'e':
/*  94 */       if ((s[(len - 2)] == 'i') || (s[(len - 2)] == 'h')) {
/*  95 */         return len - 2;
/*     */       }
/*  97 */       return len - 1;
/*     */     case 'i':
/*  99 */       if ((s[(len - 2)] == 'h') || (s[(len - 2)] == 'i')) {
/* 100 */         return len - 2;
/*     */       }
/* 102 */       return len - 1;
/*     */     case 'a':
/* 104 */       if (s[(len - 2)] == 'i') {
/* 105 */         return len - 2;
/*     */       }
/* 107 */       return len - 1;
/*     */     case 'o':
/* 109 */       if (s[(len - 2)] == 'i') {
/* 110 */         return len - 2;
/*     */       }
/* 112 */       return len - 1;
/*     */     }
/*     */ 
/* 115 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.it.ItalianLightStemmer
 * JD-Core Version:    0.6.2
 */