/*     */ package org.apache.lucene.analysis.lv;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class LatvianStemmer
/*     */ {
/*  53 */   static final Affix[] affixes = { new Affix("ajiem", 3, false), new Affix("ajai", 3, false), new Affix("ajam", 2, false), new Affix("ajām", 2, false), new Affix("ajos", 2, false), new Affix("ajās", 2, false), new Affix("iem", 2, true), new Affix("ajā", 2, false), new Affix("ais", 2, false), new Affix("ai", 2, false), new Affix("ei", 2, false), new Affix("ām", 1, false), new Affix("am", 1, false), new Affix("ēm", 1, false), new Affix("īm", 1, false), new Affix("im", 1, false), new Affix("um", 1, false), new Affix("us", 1, true), new Affix("as", 1, false), new Affix("ās", 1, false), new Affix("es", 1, false), new Affix("os", 1, true), new Affix("ij", 1, false), new Affix("īs", 1, false), new Affix("ēs", 1, false), new Affix("is", 1, false), new Affix("ie", 1, false), new Affix("u", 1, true), new Affix("a", 1, true), new Affix("i", 1, true), new Affix("e", 1, false), new Affix("ā", 1, false), new Affix("ē", 1, false), new Affix("ī", 1, false), new Affix("ū", 1, false), new Affix("o", 1, false), new Affix("s", 0, false), new Affix("š", 0, false) };
/*     */ 
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  40 */     int numVowels = numVowels(s, len);
/*     */ 
/*  42 */     for (int i = 0; i < affixes.length; i++) {
/*  43 */       Affix affix = affixes[i];
/*  44 */       if ((numVowels > affix.vc) && (len >= affix.affix.length + 3) && (StemmerUtil.endsWith(s, len, affix.affix))) {
/*  45 */         len -= affix.affix.length;
/*  46 */         return affix.palatalizes ? unpalatalize(s, len) : len;
/*     */       }
/*     */     }
/*     */ 
/*  50 */     return len;
/*     */   }
/*     */ 
/*     */   private int unpalatalize(char[] s, int len)
/*     */   {
/*  99 */     if (s[len] == 'u')
/*     */     {
/* 101 */       if (StemmerUtil.endsWith(s, len, "kš")) {
/* 102 */         len++;
/* 103 */         s[(len - 2)] = 's';
/* 104 */         s[(len - 1)] = 't';
/* 105 */         return len;
/*     */       }
/*     */ 
/* 108 */       if (StemmerUtil.endsWith(s, len, "ņņ")) {
/* 109 */         s[(len - 2)] = 'n';
/* 110 */         s[(len - 1)] = 'n';
/* 111 */         return len;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 116 */     if ((StemmerUtil.endsWith(s, len, "pj")) || (StemmerUtil.endsWith(s, len, "bj")) || (StemmerUtil.endsWith(s, len, "mj")) || (StemmerUtil.endsWith(s, len, "vj")))
/*     */     {
/* 119 */       return len - 1;
/* 120 */     }if (StemmerUtil.endsWith(s, len, "šņ")) {
/* 121 */       s[(len - 2)] = 's';
/* 122 */       s[(len - 1)] = 'n';
/* 123 */       return len;
/* 124 */     }if (StemmerUtil.endsWith(s, len, "žņ")) {
/* 125 */       s[(len - 2)] = 'z';
/* 126 */       s[(len - 1)] = 'n';
/* 127 */       return len;
/* 128 */     }if (StemmerUtil.endsWith(s, len, "šļ")) {
/* 129 */       s[(len - 2)] = 's';
/* 130 */       s[(len - 1)] = 'l';
/* 131 */       return len;
/* 132 */     }if (StemmerUtil.endsWith(s, len, "žļ")) {
/* 133 */       s[(len - 2)] = 'z';
/* 134 */       s[(len - 1)] = 'l';
/* 135 */       return len;
/* 136 */     }if (StemmerUtil.endsWith(s, len, "ļņ")) {
/* 137 */       s[(len - 2)] = 'l';
/* 138 */       s[(len - 1)] = 'n';
/* 139 */       return len;
/* 140 */     }if (StemmerUtil.endsWith(s, len, "ļļ")) {
/* 141 */       s[(len - 2)] = 'l';
/* 142 */       s[(len - 1)] = 'l';
/* 143 */       return len;
/* 144 */     }if (s[(len - 1)] == 'č') {
/* 145 */       s[(len - 1)] = 'c';
/* 146 */       return len;
/* 147 */     }if (s[(len - 1)] == 'ļ') {
/* 148 */       s[(len - 1)] = 'l';
/* 149 */       return len;
/* 150 */     }if (s[(len - 1)] == 'ņ') {
/* 151 */       s[(len - 1)] = 'n';
/* 152 */       return len;
/*     */     }
/*     */ 
/* 155 */     return len;
/*     */   }
/*     */ 
/*     */   private int numVowels(char[] s, int len)
/*     */   {
/* 163 */     int n = 0;
/* 164 */     for (int i = 0; i < len; i++) {
/* 165 */       switch (s[i]) { case 'a':
/*     */       case 'e':
/*     */       case 'i':
/*     */       case 'o':
/*     */       case 'u':
/*     */       case 'ā':
/*     */       case 'ē':
/*     */       case 'ī':
/*     */       case 'ū':
/* 169 */         n++;
/*     */       }
/*     */     }
/* 172 */     return n;
/*     */   }
/*     */ 
/*     */   static class Affix
/*     */   {
/*     */     char[] affix;
/*     */     int vc;
/*     */     boolean palatalizes;
/*     */ 
/*     */     Affix(String affix, int vc, boolean palatalizes)
/*     */     {
/*  81 */       this.affix = affix.toCharArray();
/*  82 */       this.vc = vc;
/*  83 */       this.palatalizes = palatalizes;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.lv.LatvianStemmer
 * JD-Core Version:    0.6.2
 */