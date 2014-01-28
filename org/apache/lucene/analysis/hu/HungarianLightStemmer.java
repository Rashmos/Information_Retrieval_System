/*     */ package org.apache.lucene.analysis.hu;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class HungarianLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  66 */     for (int i = 0; i < len; i++) {
/*  67 */       switch (s[i]) { case 'á':
/*  68 */         s[i] = 'a'; break;
/*     */       case 'é':
/*     */       case 'ë':
/*  70 */         s[i] = 'e'; break;
/*     */       case 'í':
/*  71 */         s[i] = 'i'; break;
/*     */       case 'ó':
/*     */       case 'õ':
/*     */       case 'ö':
/*     */       case 'ő':
/*  75 */         s[i] = 'o'; break;
/*     */       case 'ú':
/*     */       case 'û':
/*     */       case 'ü':
/*     */       case 'ũ':
/*     */       case 'ű':
/*  80 */         s[i] = 'u';
/*     */       }
/*     */     }
/*  83 */     len = removeCase(s, len);
/*  84 */     len = removePossessive(s, len);
/*  85 */     len = removePlural(s, len);
/*  86 */     return normalize(s, len);
/*     */   }
/*     */ 
/*     */   private int removeCase(char[] s, int len) {
/*  90 */     if ((len > 6) && (StemmerUtil.endsWith(s, len, "kent"))) {
/*  91 */       return len - 4;
/*     */     }
/*  93 */     if (len > 5) {
/*  94 */       if ((StemmerUtil.endsWith(s, len, "nak")) || (StemmerUtil.endsWith(s, len, "nek")) || (StemmerUtil.endsWith(s, len, "val")) || (StemmerUtil.endsWith(s, len, "vel")) || (StemmerUtil.endsWith(s, len, "ert")) || (StemmerUtil.endsWith(s, len, "rol")) || (StemmerUtil.endsWith(s, len, "ban")) || (StemmerUtil.endsWith(s, len, "ben")) || (StemmerUtil.endsWith(s, len, "bol")) || (StemmerUtil.endsWith(s, len, "nal")) || (StemmerUtil.endsWith(s, len, "nel")) || (StemmerUtil.endsWith(s, len, "hoz")) || (StemmerUtil.endsWith(s, len, "hez")) || (StemmerUtil.endsWith(s, len, "tol")))
/*     */       {
/* 108 */         return len - 3;
/*     */       }
/* 110 */       if (((StemmerUtil.endsWith(s, len, "al")) || (StemmerUtil.endsWith(s, len, "el"))) && 
/* 111 */         (!isVowel(s[(len - 3)])) && (s[(len - 3)] == s[(len - 4)])) {
/* 112 */         return len - 3;
/*     */       }
/*     */     }
/*     */ 
/* 116 */     if (len > 4) {
/* 117 */       if ((StemmerUtil.endsWith(s, len, "at")) || (StemmerUtil.endsWith(s, len, "et")) || (StemmerUtil.endsWith(s, len, "ot")) || (StemmerUtil.endsWith(s, len, "va")) || (StemmerUtil.endsWith(s, len, "ve")) || (StemmerUtil.endsWith(s, len, "ra")) || (StemmerUtil.endsWith(s, len, "re")) || (StemmerUtil.endsWith(s, len, "ba")) || (StemmerUtil.endsWith(s, len, "be")) || (StemmerUtil.endsWith(s, len, "ul")) || (StemmerUtil.endsWith(s, len, "ig")))
/*     */       {
/* 128 */         return len - 2;
/*     */       }
/* 130 */       if (((StemmerUtil.endsWith(s, len, "on")) || (StemmerUtil.endsWith(s, len, "en"))) && (!isVowel(s[(len - 3)]))) {
/* 131 */         return len - 2;
/*     */       }
/* 133 */       switch (s[(len - 1)]) { case 'n':
/*     */       case 't':
/* 135 */         return len - 1;
/*     */       case 'a':
/*     */       case 'e':
/* 137 */         if ((s[(len - 2)] == s[(len - 3)]) && (!isVowel(s[(len - 2)]))) return len - 2;
/*     */         break;
/*     */       }
/*     */     }
/* 141 */     return len;
/*     */   }
/*     */ 
/*     */   private int removePossessive(char[] s, int len) {
/* 145 */     if (len > 6) {
/* 146 */       if ((!isVowel(s[(len - 5)])) && ((StemmerUtil.endsWith(s, len, "atok")) || (StemmerUtil.endsWith(s, len, "otok")) || (StemmerUtil.endsWith(s, len, "etek"))))
/*     */       {
/* 150 */         return len - 4;
/*     */       }
/* 152 */       if ((StemmerUtil.endsWith(s, len, "itek")) || (StemmerUtil.endsWith(s, len, "itok"))) {
/* 153 */         return len - 4;
/*     */       }
/*     */     }
/* 156 */     if (len > 5) {
/* 157 */       if ((!isVowel(s[(len - 4)])) && ((StemmerUtil.endsWith(s, len, "unk")) || (StemmerUtil.endsWith(s, len, "tok")) || (StemmerUtil.endsWith(s, len, "tek"))))
/*     */       {
/* 161 */         return len - 3;
/*     */       }
/* 163 */       if ((isVowel(s[(len - 4)])) && (StemmerUtil.endsWith(s, len, "juk"))) {
/* 164 */         return len - 3;
/*     */       }
/* 166 */       if (StemmerUtil.endsWith(s, len, "ink")) {
/* 167 */         return len - 3;
/*     */       }
/*     */     }
/* 170 */     if (len > 4) {
/* 171 */       if ((!isVowel(s[(len - 3)])) && ((StemmerUtil.endsWith(s, len, "am")) || (StemmerUtil.endsWith(s, len, "em")) || (StemmerUtil.endsWith(s, len, "om")) || (StemmerUtil.endsWith(s, len, "ad")) || (StemmerUtil.endsWith(s, len, "ed")) || (StemmerUtil.endsWith(s, len, "od")) || (StemmerUtil.endsWith(s, len, "uk"))))
/*     */       {
/* 179 */         return len - 2;
/*     */       }
/* 181 */       if ((isVowel(s[(len - 3)])) && ((StemmerUtil.endsWith(s, len, "nk")) || (StemmerUtil.endsWith(s, len, "ja")) || (StemmerUtil.endsWith(s, len, "je"))))
/*     */       {
/* 185 */         return len - 2;
/*     */       }
/* 187 */       if ((StemmerUtil.endsWith(s, len, "im")) || (StemmerUtil.endsWith(s, len, "id")) || (StemmerUtil.endsWith(s, len, "ik")))
/*     */       {
/* 190 */         return len - 2;
/*     */       }
/*     */     }
/* 193 */     if (len > 3)
/* 194 */       switch (s[(len - 1)]) { case 'a':
/*     */       case 'e':
/* 196 */         if (!isVowel(s[(len - 2)])) return len - 1; break;
/*     */       case 'd':
/*     */       case 'm':
/* 198 */         if (isVowel(s[(len - 2)])) return len - 1; break;
/*     */       case 'i':
/* 199 */         return len - 1;
/*     */       case 'b':
/*     */       case 'c':
/*     */       case 'f':
/*     */       case 'g':
/*     */       case 'h':
/*     */       case 'j':
/*     */       case 'k':
/* 202 */       case 'l': }  return len;
/*     */   }
/*     */ 
/*     */   private int removePlural(char[] s, int len)
/*     */   {
/* 207 */     if ((len > 3) && (s[(len - 1)] == 'k')) {
/* 208 */       switch (s[(len - 2)]) { case 'a':
/*     */       case 'e':
/*     */       case 'o':
/* 211 */         if (len > 4) return len - 2; break; }
/* 212 */       return len - 1;
/*     */     }
/* 214 */     return len;
/*     */   }
/*     */ 
/*     */   private int normalize(char[] s, int len) {
/* 218 */     if (len > 3)
/* 219 */       switch (s[(len - 1)]) { case 'a':
/*     */       case 'e':
/*     */       case 'i':
/*     */       case 'o':
/* 223 */         return len - 1;
/*     */       }
/* 225 */     return len;
/*     */   }
/*     */ 
/*     */   private boolean isVowel(char ch) {
/* 229 */     switch (ch) { case 'a':
/*     */     case 'e':
/*     */     case 'i':
/*     */     case 'o':
/*     */     case 'u':
/*     */     case 'y':
/* 235 */       return true; }
/* 236 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hu.HungarianLightStemmer
 * JD-Core Version:    0.6.2
 */