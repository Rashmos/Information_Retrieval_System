/*     */ package org.apache.lucene.analysis.cz;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class CzechStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  45 */     len = removeCase(s, len);
/*  46 */     len = removePossessives(s, len);
/*  47 */     len = normalize(s, len);
/*  48 */     return len;
/*     */   }
/*     */ 
/*     */   private int removeCase(char[] s, int len) {
/*  52 */     if ((len > 7) && (StemmerUtil.endsWith(s, len, "atech"))) {
/*  53 */       return len - 5;
/*     */     }
/*  55 */     if ((len > 6) && ((StemmerUtil.endsWith(s, len, "ětem")) || (StemmerUtil.endsWith(s, len, "etem")) || (StemmerUtil.endsWith(s, len, "atům"))))
/*     */     {
/*  59 */       return len - 4;
/*     */     }
/*  61 */     if ((len > 5) && ((StemmerUtil.endsWith(s, len, "ech")) || (StemmerUtil.endsWith(s, len, "ich")) || (StemmerUtil.endsWith(s, len, "ích")) || (StemmerUtil.endsWith(s, len, "ého")) || (StemmerUtil.endsWith(s, len, "ěmi")) || (StemmerUtil.endsWith(s, len, "emi")) || (StemmerUtil.endsWith(s, len, "ému")) || (StemmerUtil.endsWith(s, len, "ěte")) || (StemmerUtil.endsWith(s, len, "ete")) || (StemmerUtil.endsWith(s, len, "ěti")) || (StemmerUtil.endsWith(s, len, "eti")) || (StemmerUtil.endsWith(s, len, "ího")) || (StemmerUtil.endsWith(s, len, "iho")) || (StemmerUtil.endsWith(s, len, "ími")) || (StemmerUtil.endsWith(s, len, "ímu")) || (StemmerUtil.endsWith(s, len, "imu")) || (StemmerUtil.endsWith(s, len, "ách")) || (StemmerUtil.endsWith(s, len, "ata")) || (StemmerUtil.endsWith(s, len, "aty")) || (StemmerUtil.endsWith(s, len, "ých")) || (StemmerUtil.endsWith(s, len, "ama")) || (StemmerUtil.endsWith(s, len, "ami")) || (StemmerUtil.endsWith(s, len, "ové")) || (StemmerUtil.endsWith(s, len, "ovi")) || (StemmerUtil.endsWith(s, len, "ými"))))
/*     */     {
/*  87 */       return len - 3;
/*     */     }
/*  89 */     if ((len > 4) && ((StemmerUtil.endsWith(s, len, "em")) || (StemmerUtil.endsWith(s, len, "es")) || (StemmerUtil.endsWith(s, len, "ém")) || (StemmerUtil.endsWith(s, len, "ím")) || (StemmerUtil.endsWith(s, len, "ům")) || (StemmerUtil.endsWith(s, len, "at")) || (StemmerUtil.endsWith(s, len, "ám")) || (StemmerUtil.endsWith(s, len, "os")) || (StemmerUtil.endsWith(s, len, "us")) || (StemmerUtil.endsWith(s, len, "ým")) || (StemmerUtil.endsWith(s, len, "mi")) || (StemmerUtil.endsWith(s, len, "ou"))))
/*     */     {
/* 102 */       return len - 2;
/*     */     }
/* 104 */     if (len > 3) {
/* 105 */       switch (s[(len - 1)]) {
/*     */       case 'a':
/*     */       case 'e':
/*     */       case 'i':
/*     */       case 'o':
/*     */       case 'u':
/*     */       case 'y':
/*     */       case 'á':
/*     */       case 'é':
/*     */       case 'í':
/*     */       case 'ý':
/*     */       case 'ě':
/*     */       case 'ů':
/* 118 */         return len - 1;
/*     */       }
/*     */     }
/*     */ 
/* 122 */     return len;
/*     */   }
/*     */ 
/*     */   private int removePossessives(char[] s, int len) {
/* 126 */     if ((len > 5) && ((StemmerUtil.endsWith(s, len, "ov")) || (StemmerUtil.endsWith(s, len, "in")) || (StemmerUtil.endsWith(s, len, "ův"))))
/*     */     {
/* 130 */       return len - 2;
/*     */     }
/* 132 */     return len;
/*     */   }
/*     */ 
/*     */   private int normalize(char[] s, int len) {
/* 136 */     if (StemmerUtil.endsWith(s, len, "čt")) {
/* 137 */       s[(len - 2)] = 'c';
/* 138 */       s[(len - 1)] = 'k';
/* 139 */       return len;
/*     */     }
/*     */ 
/* 142 */     if (StemmerUtil.endsWith(s, len, "št")) {
/* 143 */       s[(len - 2)] = 's';
/* 144 */       s[(len - 1)] = 'k';
/* 145 */       return len;
/*     */     }
/*     */ 
/* 148 */     switch (s[(len - 1)]) {
/*     */     case 'c':
/*     */     case 'č':
/* 151 */       s[(len - 1)] = 'k';
/* 152 */       return len;
/*     */     case 'z':
/*     */     case 'ž':
/* 155 */       s[(len - 1)] = 'h';
/* 156 */       return len;
/*     */     }
/*     */ 
/* 159 */     if ((len > 1) && (s[(len - 2)] == 'e')) {
/* 160 */       s[(len - 2)] = s[(len - 1)];
/* 161 */       return len - 1;
/*     */     }
/*     */ 
/* 164 */     if ((len > 2) && (s[(len - 2)] == 'ů')) {
/* 165 */       s[(len - 2)] = 'o';
/* 166 */       return len;
/*     */     }
/*     */ 
/* 169 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cz.CzechStemmer
 * JD-Core Version:    0.6.2
 */