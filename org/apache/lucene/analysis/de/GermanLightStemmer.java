/*     */ package org.apache.lucene.analysis.de;
/*     */ 
/*     */ public class GermanLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  65 */     for (int i = 0; i < len; i++)
/*  66 */       switch (s[i]) { case 'à':
/*     */       case 'á':
/*     */       case 'â':
/*     */       case 'ä':
/*  70 */         s[i] = 'a'; break;
/*     */       case 'ò':
/*     */       case 'ó':
/*     */       case 'ô':
/*     */       case 'ö':
/*  74 */         s[i] = 'o'; break;
/*     */       case 'ì':
/*     */       case 'í':
/*     */       case 'î':
/*     */       case 'ï':
/*  78 */         s[i] = 'i'; break;
/*     */       case 'ù':
/*     */       case 'ú':
/*     */       case 'û':
/*     */       case 'ü':
/*  82 */         s[i] = 'u';
/*     */       case 'ã':
/*     */       case 'å':
/*     */       case 'æ':
/*     */       case 'ç':
/*     */       case 'è':
/*     */       case 'é':
/*     */       case 'ê':
/*     */       case 'ë':
/*     */       case 'ð':
/*     */       case 'ñ':
/*     */       case 'õ':
/*     */       case '÷':
/*  85 */       case 'ø': }  len = step1(s, len);
/*  86 */     return step2(s, len);
/*     */   }
/*     */ 
/*     */   private boolean stEnding(char ch) {
/*  90 */     switch (ch) { case 'b':
/*     */     case 'd':
/*     */     case 'f':
/*     */     case 'g':
/*     */     case 'h':
/*     */     case 'k':
/*     */     case 'l':
/*     */     case 'm':
/*     */     case 'n':
/*     */     case 't':
/* 100 */       return true;
/*     */     case 'c':
/*     */     case 'e':
/*     */     case 'i':
/*     */     case 'j':
/*     */     case 'o':
/*     */     case 'p':
/*     */     case 'q':
/*     */     case 'r':
/* 101 */     case 's': } return false;
/*     */   }
/*     */ 
/*     */   private int step1(char[] s, int len)
/*     */   {
/* 106 */     if ((len > 5) && (s[(len - 3)] == 'e') && (s[(len - 2)] == 'r') && (s[(len - 1)] == 'n')) {
/* 107 */       return len - 3;
/*     */     }
/* 109 */     if ((len > 4) && (s[(len - 2)] == 'e'))
/* 110 */       switch (s[(len - 1)]) { case 'm':
/*     */       case 'n':
/*     */       case 'r':
/*     */       case 's':
/* 114 */         return len - 2;
/*     */       case 'o':
/*     */       case 'p':
/* 117 */       case 'q': }  if ((len > 3) && (s[(len - 1)] == 'e')) {
/* 118 */       return len - 1;
/*     */     }
/* 120 */     if ((len > 3) && (s[(len - 1)] == 's') && (stEnding(s[(len - 2)]))) {
/* 121 */       return len - 1;
/*     */     }
/* 123 */     return len;
/*     */   }
/*     */ 
/*     */   private int step2(char[] s, int len) {
/* 127 */     if ((len > 5) && (s[(len - 3)] == 'e') && (s[(len - 2)] == 's') && (s[(len - 1)] == 't')) {
/* 128 */       return len - 3;
/*     */     }
/* 130 */     if ((len > 4) && (s[(len - 2)] == 'e') && ((s[(len - 1)] == 'r') || (s[(len - 1)] == 'n'))) {
/* 131 */       return len - 2;
/*     */     }
/* 133 */     if ((len > 4) && (s[(len - 2)] == 's') && (s[(len - 1)] == 't') && (stEnding(s[(len - 3)]))) {
/* 134 */       return len - 2;
/*     */     }
/* 136 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.de.GermanLightStemmer
 * JD-Core Version:    0.6.2
 */