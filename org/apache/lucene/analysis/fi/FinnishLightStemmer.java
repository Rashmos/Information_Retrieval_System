/*     */ package org.apache.lucene.analysis.fi;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class FinnishLightStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  67 */     if (len < 4) {
/*  68 */       return len;
/*     */     }
/*  70 */     for (int i = 0; i < len; i++) {
/*  71 */       switch (s[i]) { case 'ä':
/*     */       case 'å':
/*  73 */         s[i] = 'a'; break;
/*     */       case 'ö':
/*  74 */         s[i] = 'o';
/*     */       }
/*     */     }
/*  77 */     len = step1(s, len);
/*  78 */     len = step2(s, len);
/*  79 */     len = step3(s, len);
/*  80 */     len = norm1(s, len);
/*  81 */     len = norm2(s, len);
/*  82 */     return len;
/*     */   }
/*     */ 
/*     */   private int step1(char[] s, int len) {
/*  86 */     if (len > 8) {
/*  87 */       if (StemmerUtil.endsWith(s, len, "kin"))
/*  88 */         return step1(s, len - 3);
/*  89 */       if (StemmerUtil.endsWith(s, len, "ko")) {
/*  90 */         return step1(s, len - 2);
/*     */       }
/*     */     }
/*  93 */     if (len > 11) {
/*  94 */       if (StemmerUtil.endsWith(s, len, "dellinen"))
/*  95 */         return len - 8;
/*  96 */       if (StemmerUtil.endsWith(s, len, "dellisuus"))
/*  97 */         return len - 9;
/*     */     }
/*  99 */     return len;
/*     */   }
/*     */ 
/*     */   private int step2(char[] s, int len) {
/* 103 */     if (len > 5) {
/* 104 */       if ((StemmerUtil.endsWith(s, len, "lla")) || (StemmerUtil.endsWith(s, len, "tse")) || (StemmerUtil.endsWith(s, len, "sti")))
/*     */       {
/* 107 */         return len - 3;
/*     */       }
/* 109 */       if (StemmerUtil.endsWith(s, len, "ni")) {
/* 110 */         return len - 2;
/*     */       }
/* 112 */       if (StemmerUtil.endsWith(s, len, "aa")) {
/* 113 */         return len - 1;
/*     */       }
/*     */     }
/* 116 */     return len;
/*     */   }
/*     */ 
/*     */   private int step3(char[] s, int len) {
/* 120 */     if (len > 8) {
/* 121 */       if (StemmerUtil.endsWith(s, len, "nnen")) {
/* 122 */         s[(len - 4)] = 's';
/* 123 */         return len - 3;
/*     */       }
/*     */ 
/* 126 */       if (StemmerUtil.endsWith(s, len, "ntena")) {
/* 127 */         s[(len - 5)] = 's';
/* 128 */         return len - 4;
/*     */       }
/*     */ 
/* 131 */       if (StemmerUtil.endsWith(s, len, "tten")) {
/* 132 */         return len - 4;
/*     */       }
/* 134 */       if (StemmerUtil.endsWith(s, len, "eiden")) {
/* 135 */         return len - 5;
/*     */       }
/*     */     }
/* 138 */     if (len > 6) {
/* 139 */       if ((StemmerUtil.endsWith(s, len, "neen")) || (StemmerUtil.endsWith(s, len, "niin")) || (StemmerUtil.endsWith(s, len, "seen")) || (StemmerUtil.endsWith(s, len, "teen")) || (StemmerUtil.endsWith(s, len, "inen")))
/*     */       {
/* 144 */         return len - 4;
/*     */       }
/* 146 */       if ((s[(len - 3)] == 'h') && (isVowel(s[(len - 2)])) && (s[(len - 1)] == 'n')) {
/* 147 */         return len - 3;
/*     */       }
/* 149 */       if (StemmerUtil.endsWith(s, len, "den")) {
/* 150 */         s[(len - 3)] = 's';
/* 151 */         return len - 2;
/*     */       }
/*     */ 
/* 154 */       if (StemmerUtil.endsWith(s, len, "ksen")) {
/* 155 */         s[(len - 4)] = 's';
/* 156 */         return len - 3;
/*     */       }
/*     */ 
/* 159 */       if ((StemmerUtil.endsWith(s, len, "ssa")) || (StemmerUtil.endsWith(s, len, "sta")) || (StemmerUtil.endsWith(s, len, "lla")) || (StemmerUtil.endsWith(s, len, "lta")) || (StemmerUtil.endsWith(s, len, "tta")) || (StemmerUtil.endsWith(s, len, "ksi")) || (StemmerUtil.endsWith(s, len, "lle")))
/*     */       {
/* 166 */         return len - 3;
/*     */       }
/*     */     }
/* 169 */     if (len > 5) {
/* 170 */       if ((StemmerUtil.endsWith(s, len, "na")) || (StemmerUtil.endsWith(s, len, "ne")))
/*     */       {
/* 172 */         return len - 2;
/*     */       }
/* 174 */       if (StemmerUtil.endsWith(s, len, "nei")) {
/* 175 */         return len - 3;
/*     */       }
/*     */     }
/* 178 */     if (len > 4) {
/* 179 */       if ((StemmerUtil.endsWith(s, len, "ja")) || (StemmerUtil.endsWith(s, len, "ta")))
/*     */       {
/* 181 */         return len - 2;
/*     */       }
/* 183 */       if (s[(len - 1)] == 'a') {
/* 184 */         return len - 1;
/*     */       }
/* 186 */       if ((s[(len - 1)] == 'n') && (isVowel(s[(len - 2)]))) {
/* 187 */         return len - 2;
/*     */       }
/* 189 */       if (s[(len - 1)] == 'n') {
/* 190 */         return len - 1;
/*     */       }
/*     */     }
/* 193 */     return len;
/*     */   }
/*     */ 
/*     */   private int norm1(char[] s, int len) {
/* 197 */     if ((len > 5) && (StemmerUtil.endsWith(s, len, "hde"))) {
/* 198 */       s[(len - 3)] = 'k';
/* 199 */       s[(len - 2)] = 's';
/* 200 */       s[(len - 1)] = 'i';
/*     */     }
/*     */ 
/* 203 */     if ((len > 4) && (
/* 204 */       (StemmerUtil.endsWith(s, len, "ei")) || (StemmerUtil.endsWith(s, len, "at")))) {
/* 205 */       return len - 2;
/*     */     }
/*     */ 
/* 208 */     if (len > 3)
/* 209 */       switch (s[(len - 1)]) { case 'a':
/*     */       case 'e':
/*     */       case 'i':
/*     */       case 'j':
/*     */       case 's':
/*     */       case 't':
/* 215 */         return len - 1;
/*     */       case 'b':
/*     */       case 'c':
/*     */       case 'd':
/*     */       case 'f':
/*     */       case 'g':
/*     */       case 'h':
/*     */       case 'k':
/*     */       case 'l':
/*     */       case 'm':
/*     */       case 'n':
/*     */       case 'o':
/*     */       case 'p':
/*     */       case 'q':
/* 218 */       case 'r': }  return len;
/*     */   }
/*     */ 
/*     */   private int norm2(char[] s, int len) {
/* 222 */     if ((len > 8) && (
/* 223 */       (s[(len - 1)] == 'e') || (s[(len - 1)] == 'o') || (s[(len - 1)] == 'u')))
/*     */     {
/* 226 */       len--;
/*     */     }
/*     */ 
/* 229 */     if (len > 4) {
/* 230 */       if (s[(len - 1)] == 'i') {
/* 231 */         len--;
/*     */       }
/* 233 */       if (len > 4) {
/* 234 */         char ch = s[0];
/* 235 */         for (int i = 1; i < len; i++) {
/* 236 */           if ((s[i] == ch) && ((ch == 'k') || (ch == 'p') || (ch == 't')))
/*     */           {
/* 238 */             len = StemmerUtil.delete(s, i--, len);
/*     */           }
/* 240 */           else ch = s[i];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 245 */     return len;
/*     */   }
/*     */ 
/*     */   private boolean isVowel(char ch) {
/* 249 */     switch (ch) { case 'a':
/*     */     case 'e':
/*     */     case 'i':
/*     */     case 'o':
/*     */     case 'u':
/*     */     case 'y':
/* 255 */       return true; }
/* 256 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fi.FinnishLightStemmer
 * JD-Core Version:    0.6.2
 */