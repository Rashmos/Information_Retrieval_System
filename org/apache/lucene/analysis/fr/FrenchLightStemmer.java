/*     */ package org.apache.lucene.analysis.fr;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class FrenchLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  67 */     if ((len > 5) && (s[(len - 1)] == 'x')) {
/*  68 */       if ((s[(len - 3)] == 'a') && (s[(len - 2)] == 'u') && (s[(len - 4)] != 'e'))
/*  69 */         s[(len - 2)] = 'l';
/*  70 */       len--;
/*     */     }
/*     */ 
/*  73 */     if ((len > 3) && (s[(len - 1)] == 'x')) {
/*  74 */       len--;
/*     */     }
/*  76 */     if ((len > 3) && (s[(len - 1)] == 's')) {
/*  77 */       len--;
/*     */     }
/*  79 */     if ((len > 9) && (StemmerUtil.endsWith(s, len, "issement"))) {
/*  80 */       len -= 6;
/*  81 */       s[(len - 1)] = 'r';
/*  82 */       return norm(s, len);
/*     */     }
/*     */ 
/*  85 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "issant"))) {
/*  86 */       len -= 4;
/*  87 */       s[(len - 1)] = 'r';
/*  88 */       return norm(s, len);
/*     */     }
/*     */ 
/*  91 */     if ((len > 6) && (StemmerUtil.endsWith(s, len, "ement"))) {
/*  92 */       len -= 4;
/*  93 */       if ((len > 3) && (StemmerUtil.endsWith(s, len, "ive"))) {
/*  94 */         len--;
/*  95 */         s[(len - 1)] = 'f';
/*     */       }
/*  97 */       return norm(s, len);
/*     */     }
/*     */ 
/* 100 */     if ((len > 11) && (StemmerUtil.endsWith(s, len, "ficatrice"))) {
/* 101 */       len -= 5;
/* 102 */       s[(len - 2)] = 'e';
/* 103 */       s[(len - 1)] = 'r';
/* 104 */       return norm(s, len);
/*     */     }
/*     */ 
/* 107 */     if ((len > 10) && (StemmerUtil.endsWith(s, len, "ficateur"))) {
/* 108 */       len -= 4;
/* 109 */       s[(len - 2)] = 'e';
/* 110 */       s[(len - 1)] = 'r';
/* 111 */       return norm(s, len);
/*     */     }
/*     */ 
/* 114 */     if ((len > 9) && (StemmerUtil.endsWith(s, len, "catrice"))) {
/* 115 */       len -= 3;
/* 116 */       s[(len - 4)] = 'q';
/* 117 */       s[(len - 3)] = 'u';
/* 118 */       s[(len - 2)] = 'e';
/*     */ 
/* 120 */       return norm(s, len);
/*     */     }
/*     */ 
/* 123 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "cateur"))) {
/* 124 */       len -= 2;
/* 125 */       s[(len - 4)] = 'q';
/* 126 */       s[(len - 3)] = 'u';
/* 127 */       s[(len - 2)] = 'e';
/* 128 */       s[(len - 1)] = 'r';
/* 129 */       return norm(s, len);
/*     */     }
/*     */ 
/* 132 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "atrice"))) {
/* 133 */       len -= 4;
/* 134 */       s[(len - 2)] = 'e';
/* 135 */       s[(len - 1)] = 'r';
/* 136 */       return norm(s, len);
/*     */     }
/*     */ 
/* 139 */     if ((len > 7) && (StemmerUtil.endsWith(s, len, "ateur"))) {
/* 140 */       len -= 3;
/* 141 */       s[(len - 2)] = 'e';
/* 142 */       s[(len - 1)] = 'r';
/* 143 */       return norm(s, len);
/*     */     }
/*     */ 
/* 146 */     if ((len > 6) && (StemmerUtil.endsWith(s, len, "trice"))) {
/* 147 */       len--;
/* 148 */       s[(len - 3)] = 'e';
/* 149 */       s[(len - 2)] = 'u';
/* 150 */       s[(len - 1)] = 'r';
/*     */     }
/*     */ 
/* 153 */     if ((len > 5) && (StemmerUtil.endsWith(s, len, "ième"))) {
/* 154 */       return norm(s, len - 4);
/*     */     }
/* 156 */     if ((len > 7) && (StemmerUtil.endsWith(s, len, "teuse"))) {
/* 157 */       len -= 2;
/* 158 */       s[(len - 1)] = 'r';
/* 159 */       return norm(s, len);
/*     */     }
/*     */ 
/* 162 */     if ((len > 6) && (StemmerUtil.endsWith(s, len, "teur"))) {
/* 163 */       len--;
/* 164 */       s[(len - 1)] = 'r';
/* 165 */       return norm(s, len);
/*     */     }
/*     */ 
/* 168 */     if ((len > 5) && (StemmerUtil.endsWith(s, len, "euse"))) {
/* 169 */       return norm(s, len - 2);
/*     */     }
/* 171 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "ère"))) {
/* 172 */       len--;
/* 173 */       s[(len - 2)] = 'e';
/* 174 */       return norm(s, len);
/*     */     }
/*     */ 
/* 177 */     if ((len > 7) && (StemmerUtil.endsWith(s, len, "ive"))) {
/* 178 */       len--;
/* 179 */       s[(len - 1)] = 'f';
/* 180 */       return norm(s, len);
/*     */     }
/*     */ 
/* 183 */     if ((len > 4) && ((StemmerUtil.endsWith(s, len, "folle")) || (StemmerUtil.endsWith(s, len, "molle"))))
/*     */     {
/* 186 */       len -= 2;
/* 187 */       s[(len - 1)] = 'u';
/* 188 */       return norm(s, len);
/*     */     }
/*     */ 
/* 191 */     if ((len > 9) && (StemmerUtil.endsWith(s, len, "nnelle"))) {
/* 192 */       return norm(s, len - 5);
/*     */     }
/* 194 */     if ((len > 9) && (StemmerUtil.endsWith(s, len, "nnel"))) {
/* 195 */       return norm(s, len - 3);
/*     */     }
/* 197 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "ète"))) {
/* 198 */       len--;
/* 199 */       s[(len - 2)] = 'e';
/*     */     }
/*     */ 
/* 202 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "ique"))) {
/* 203 */       len -= 4;
/*     */     }
/* 205 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "esse"))) {
/* 206 */       return norm(s, len - 3);
/*     */     }
/* 208 */     if ((len > 7) && (StemmerUtil.endsWith(s, len, "inage"))) {
/* 209 */       return norm(s, len - 3);
/*     */     }
/* 211 */     if ((len > 9) && (StemmerUtil.endsWith(s, len, "isation"))) {
/* 212 */       len -= 7;
/* 213 */       if ((len > 5) && (StemmerUtil.endsWith(s, len, "ual")))
/* 214 */         s[(len - 2)] = 'e';
/* 215 */       return norm(s, len);
/*     */     }
/*     */ 
/* 218 */     if ((len > 9) && (StemmerUtil.endsWith(s, len, "isateur"))) {
/* 219 */       return norm(s, len - 7);
/*     */     }
/* 221 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "ation"))) {
/* 222 */       return norm(s, len - 5);
/*     */     }
/* 224 */     if ((len > 8) && (StemmerUtil.endsWith(s, len, "ition"))) {
/* 225 */       return norm(s, len - 5);
/*     */     }
/* 227 */     return norm(s, len);
/*     */   }
/*     */ 
/*     */   private int norm(char[] s, int len) {
/* 231 */     if (len > 4) {
/* 232 */       for (int i = 0; i < len; i++)
/* 233 */         switch (s[i]) { case 'à':
/*     */         case 'á':
/*     */         case 'â':
/* 236 */           s[i] = 'a'; break;
/*     */         case 'ô':
/* 237 */           s[i] = 'o'; break;
/*     */         case 'è':
/*     */         case 'é':
/*     */         case 'ê':
/* 240 */           s[i] = 'e'; break;
/*     */         case 'ù':
/*     */         case 'û':
/* 242 */           s[i] = 'u'; break;
/*     */         case 'î':
/* 243 */           s[i] = 'i'; break;
/*     */         case 'ç':
/* 244 */           s[i] = 'c';
/*     */         case 'ã':
/*     */         case 'ä':
/*     */         case 'å':
/*     */         case 'æ':
/*     */         case 'ë':
/*     */         case 'ì':
/*     */         case 'í':
/*     */         case 'ï':
/*     */         case 'ð':
/*     */         case 'ñ':
/*     */         case 'ò':
/*     */         case 'ó':
/*     */         case 'õ':
/*     */         case 'ö':
/*     */         case '÷':
/*     */         case 'ø':
/* 247 */         case 'ú': }  char ch = s[0];
/* 248 */       for (int i = 1; i < len; i++) {
/* 249 */         if (s[i] == ch)
/* 250 */           len = StemmerUtil.delete(s, i--, len);
/*     */         else {
/* 252 */           ch = s[i];
/*     */         }
/*     */       }
/*     */     }
/* 256 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "ie"))) {
/* 257 */       len -= 2;
/*     */     }
/* 259 */     if (len > 4) {
/* 260 */       if (s[(len - 1)] == 'r') len--;
/* 261 */       if (s[(len - 1)] == 'e') len--;
/* 262 */       if (s[(len - 1)] == 'e') len--;
/* 263 */       if (s[(len - 1)] == s[(len - 2)]) len--;
/*     */     }
/* 265 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fr.FrenchLightStemmer
 * JD-Core Version:    0.6.2
 */