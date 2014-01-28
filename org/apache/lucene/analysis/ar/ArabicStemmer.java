/*     */ package org.apache.lucene.analysis.ar;
/*     */ 
/*     */ import org.apache.lucene.analysis.util.StemmerUtil;
/*     */ 
/*     */ public class ArabicStemmer
/*     */ {
/*     */   public static final char ALEF = 'ا';
/*     */   public static final char BEH = 'ب';
/*     */   public static final char TEH_MARBUTA = 'ة';
/*     */   public static final char TEH = 'ت';
/*     */   public static final char FEH = 'ف';
/*     */   public static final char KAF = 'ك';
/*     */   public static final char LAM = 'ل';
/*     */   public static final char NOON = 'ن';
/*     */   public static final char HEH = 'ه';
/*     */   public static final char WAW = 'و';
/*     */   public static final char YEH = 'ي';
/*  48 */   public static final char[][] prefixes = { "ال".toCharArray(), "وال".toCharArray(), "بال".toCharArray(), "كال".toCharArray(), "فال".toCharArray(), "لل".toCharArray(), "و".toCharArray() };
/*     */ 
/*  58 */   public static final char[][] suffixes = { "ها".toCharArray(), "ان".toCharArray(), "ات".toCharArray(), "ون".toCharArray(), "ين".toCharArray(), "يه".toCharArray(), "ية".toCharArray(), "ه".toCharArray(), "ة".toCharArray(), "ي".toCharArray() };
/*     */ 
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  79 */     len = stemPrefix(s, len);
/*  80 */     len = stemSuffix(s, len);
/*     */ 
/*  82 */     return len;
/*     */   }
/*     */ 
/*     */   public int stemPrefix(char[] s, int len)
/*     */   {
/*  92 */     for (int i = 0; i < prefixes.length; i++)
/*  93 */       if (startsWithCheckLength(s, len, prefixes[i]))
/*  94 */         return StemmerUtil.deleteN(s, 0, len, prefixes[i].length);
/*  95 */     return len;
/*     */   }
/*     */ 
/*     */   public int stemSuffix(char[] s, int len)
/*     */   {
/* 105 */     for (int i = 0; i < suffixes.length; i++)
/* 106 */       if (endsWithCheckLength(s, len, suffixes[i]))
/* 107 */         len = StemmerUtil.deleteN(s, len - suffixes[i].length, len, suffixes[i].length);
/* 108 */     return len;
/*     */   }
/*     */ 
/*     */   boolean startsWithCheckLength(char[] s, int len, char[] prefix)
/*     */   {
/* 119 */     if ((prefix.length == 1) && (len < 4))
/* 120 */       return false;
/* 121 */     if (len < prefix.length + 2) {
/* 122 */       return false;
/*     */     }
/* 124 */     for (int i = 0; i < prefix.length; i++) {
/* 125 */       if (s[i] != prefix[i])
/* 126 */         return false;
/*     */     }
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */   boolean endsWithCheckLength(char[] s, int len, char[] suffix)
/*     */   {
/* 140 */     if (len < suffix.length + 2) {
/* 141 */       return false;
/*     */     }
/* 143 */     for (int i = 0; i < suffix.length; i++) {
/* 144 */       if (s[(len - suffix.length + i)] != suffix[i])
/* 145 */         return false;
/*     */     }
/* 147 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ar.ArabicStemmer
 * JD-Core Version:    0.6.2
 */