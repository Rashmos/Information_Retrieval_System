/*     */ package org.apache.lucene.analysis.bg;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class BulgarianStemmer
/*     */ {
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  41 */     if (len < 4) {
/*  42 */       return len;
/*     */     }
/*  44 */     if ((len > 5) && (StemmerUtil.endsWith(s, len, "ища"))) {
/*  45 */       return len - 3;
/*     */     }
/*  47 */     len = removeArticle(s, len);
/*  48 */     len = removePlural(s, len);
/*     */ 
/*  50 */     if (len > 3) {
/*  51 */       if (StemmerUtil.endsWith(s, len, "я"))
/*  52 */         len--;
/*  53 */       if ((StemmerUtil.endsWith(s, len, "а")) || (StemmerUtil.endsWith(s, len, "о")) || (StemmerUtil.endsWith(s, len, "е")))
/*     */       {
/*  56 */         len--;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  62 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "ен"))) {
/*  63 */       s[(len - 2)] = 'н';
/*  64 */       len--;
/*     */     }
/*     */ 
/*  67 */     if ((len > 5) && (s[(len - 2)] == 'ъ')) {
/*  68 */       s[(len - 2)] = s[(len - 1)];
/*  69 */       len--;
/*     */     }
/*     */ 
/*  72 */     return len;
/*     */   }
/*     */ 
/*     */   private int removeArticle(char[] s, int len)
/*     */   {
/*  82 */     if ((len > 6) && (StemmerUtil.endsWith(s, len, "ият"))) {
/*  83 */       return len - 3;
/*     */     }
/*  85 */     if ((len > 5) && (
/*  86 */       (StemmerUtil.endsWith(s, len, "ът")) || (StemmerUtil.endsWith(s, len, "то")) || (StemmerUtil.endsWith(s, len, "те")) || (StemmerUtil.endsWith(s, len, "та")) || (StemmerUtil.endsWith(s, len, "ия"))))
/*     */     {
/*  91 */       return len - 2;
/*     */     }
/*     */ 
/*  94 */     if ((len > 4) && (StemmerUtil.endsWith(s, len, "ят"))) {
/*  95 */       return len - 2;
/*     */     }
/*  97 */     return len;
/*     */   }
/*     */ 
/*     */   private int removePlural(char[] s, int len) {
/* 101 */     if (len > 6) {
/* 102 */       if (StemmerUtil.endsWith(s, len, "овци"))
/* 103 */         return len - 3;
/* 104 */       if (StemmerUtil.endsWith(s, len, "ове"))
/* 105 */         return len - 3;
/* 106 */       if (StemmerUtil.endsWith(s, len, "еве")) {
/* 107 */         s[(len - 3)] = 'й';
/* 108 */         return len - 2;
/*     */       }
/*     */     }
/*     */ 
/* 112 */     if (len > 5) {
/* 113 */       if (StemmerUtil.endsWith(s, len, "ища"))
/* 114 */         return len - 3;
/* 115 */       if (StemmerUtil.endsWith(s, len, "та"))
/* 116 */         return len - 2;
/* 117 */       if (StemmerUtil.endsWith(s, len, "ци")) {
/* 118 */         s[(len - 2)] = 'к';
/* 119 */         return len - 1;
/*     */       }
/* 121 */       if (StemmerUtil.endsWith(s, len, "зи")) {
/* 122 */         s[(len - 2)] = 'г';
/* 123 */         return len - 1;
/*     */       }
/*     */ 
/* 126 */       if ((s[(len - 3)] == 'е') && (s[(len - 1)] == 'и')) {
/* 127 */         s[(len - 3)] = 'я';
/* 128 */         return len - 1;
/*     */       }
/*     */     }
/*     */ 
/* 132 */     if (len > 4) {
/* 133 */       if (StemmerUtil.endsWith(s, len, "си")) {
/* 134 */         s[(len - 2)] = 'х';
/* 135 */         return len - 1;
/*     */       }
/* 137 */       if (StemmerUtil.endsWith(s, len, "и")) {
/* 138 */         return len - 1;
/*     */       }
/*     */     }
/* 141 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.bg.BulgarianStemmer
 * JD-Core Version:    0.6.2
 */