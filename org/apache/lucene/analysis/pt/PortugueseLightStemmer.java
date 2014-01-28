/*     */ package org.apache.lucene.analysis.pt;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class PortugueseLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  67 */     if (len < 4) {
/*  68 */       return len;
/*     */     }
/*  70 */     len = removeSuffix(s, len);
/*     */ 
/*  72 */     if ((len > 3) && (s[(len - 1)] == 'a')) {
/*  73 */       len = normFeminine(s, len);
/*     */     }
/*  75 */     if (len > 4) {
/*  76 */       switch (s[(len - 1)]) { case 'a':
/*     */       case 'e':
/*     */       case 'o':
/*  79 */         len--;
/*     */       }
/*     */     }
/*  82 */     for (int i = 0; i < len; i++)
/*  83 */       switch (s[i]) { case 'à':
/*     */       case 'á':
/*     */       case 'â':
/*     */       case 'ã':
/*     */       case 'ä':
/*  88 */         s[i] = 'a'; break;
/*     */       case 'ò':
/*     */       case 'ó':
/*     */       case 'ô':
/*     */       case 'õ':
/*     */       case 'ö':
/*  93 */         s[i] = 'o'; break;
/*     */       case 'è':
/*     */       case 'é':
/*     */       case 'ê':
/*     */       case 'ë':
/*  97 */         s[i] = 'e'; break;
/*     */       case 'ù':
/*     */       case 'ú':
/*     */       case 'û':
/*     */       case 'ü':
/* 101 */         s[i] = 'u'; break;
/*     */       case 'ì':
/*     */       case 'í':
/*     */       case 'î':
/*     */       case 'ï':
/* 105 */         s[i] = 'i'; break;
/*     */       case 'ç':
/* 106 */         s[i] = 'c';
/*     */       case 'å':
/*     */       case 'æ':
/*     */       case 'ð':
/*     */       case 'ñ':
/*     */       case '÷':
/* 109 */       case 'ø': }  return len;
/*     */   }
/*     */ 
/*     */   private int removeSuffix(char[] s, int len) {
/* 113 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "es"))) {
/* 114 */       switch (s[(len - 3)]) { case 'l':
/*     */       case 'r':
/*     */       case 's':
/*     */       case 'z':
/* 118 */         return len - 2;
/*     */       }
/*     */     }
/* 121 */     if ((len > 3) && (StemmerUtil.endsWith(s, len, "ns"))) {
/* 122 */       s[(len - 2)] = 'm';
/* 123 */       return len - 1;
/*     */     }
/*     */ 
/* 126 */     if ((len > 4) && ((StemmerUtil.endsWith(s, len, "eis")) || (StemmerUtil.endsWith(s, len, "éis")))) {
/* 127 */       s[(len - 3)] = 'e';
/* 128 */       s[(len - 2)] = 'l';
/* 129 */       return len - 1;
/*     */     }
/*     */ 
/* 132 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "ais"))) {
/* 133 */       s[(len - 2)] = 'l';
/* 134 */       return len - 1;
/*     */     }
/*     */ 
/* 137 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "óis"))) {
/* 138 */       s[(len - 3)] = 'o';
/* 139 */       s[(len - 2)] = 'l';
/* 140 */       return len - 1;
/*     */     }
/*     */ 
/* 143 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "is"))) {
/* 144 */       s[(len - 1)] = 'l';
/* 145 */       return len;
/*     */     }
/*     */ 
/* 148 */     if ((len > 3) && ((StemmerUtil.endsWith(s, len, "ões")) || (StemmerUtil.endsWith(s, len, "ães"))))
/*     */     {
/* 151 */       len--;
/* 152 */       s[(len - 2)] = 'ã';
/* 153 */       s[(len - 1)] = 'o';
/* 154 */       return len;
/*     */     }
/*     */ 
/* 157 */     if ((len > 6) && (StemmerUtil.endsWith(s, len, "mente"))) {
/* 158 */       return len - 5;
/*     */     }
/* 160 */     if ((len > 3) && (s[(len - 1)] == 's'))
/* 161 */       return len - 1;
/* 162 */     return len;
/*     */   }
/*     */ 
/*     */   private int normFeminine(char[] s, int len) {
/* 166 */     if ((len > 7) && ((StemmerUtil.endsWith(s, len, "inha")) || (StemmerUtil.endsWith(s, len, "iaca")) || (StemmerUtil.endsWith(s, len, "eira"))))
/*     */     {
/* 170 */       s[(len - 1)] = 'o';
/* 171 */       return len;
/*     */     }
/*     */ 
/* 174 */     if (len > 6) {
/* 175 */       if ((StemmerUtil.endsWith(s, len, "osa")) || (StemmerUtil.endsWith(s, len, "ica")) || (StemmerUtil.endsWith(s, len, "ida")) || (StemmerUtil.endsWith(s, len, "ada")) || (StemmerUtil.endsWith(s, len, "iva")) || (StemmerUtil.endsWith(s, len, "ama")))
/*     */       {
/* 181 */         s[(len - 1)] = 'o';
/* 182 */         return len;
/*     */       }
/*     */ 
/* 185 */       if (StemmerUtil.endsWith(s, len, "ona")) {
/* 186 */         s[(len - 3)] = 'ã';
/* 187 */         s[(len - 2)] = 'o';
/* 188 */         return len - 1;
/*     */       }
/*     */ 
/* 191 */       if (StemmerUtil.endsWith(s, len, "ora")) {
/* 192 */         return len - 1;
/*     */       }
/* 194 */       if (StemmerUtil.endsWith(s, len, "esa")) {
/* 195 */         s[(len - 3)] = 'ê';
/* 196 */         return len - 1;
/*     */       }
/*     */ 
/* 199 */       if (StemmerUtil.endsWith(s, len, "na")) {
/* 200 */         s[(len - 1)] = 'o';
/* 201 */         return len;
/*     */       }
/*     */     }
/* 204 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.pt.PortugueseLightStemmer
 * JD-Core Version:    0.6.2
 */