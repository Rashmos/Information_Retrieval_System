/*     */ package org.apache.lucene.analysis.util;
/*     */ 
/*     */ public class StemmerUtil
/*     */ {
/*     */   public static boolean startsWith(char[] s, int len, String prefix)
/*     */   {
/*  31 */     int prefixLen = prefix.length();
/*  32 */     if (prefixLen > len)
/*  33 */       return false;
/*  34 */     for (int i = 0; i < prefixLen; i++)
/*  35 */       if (s[i] != prefix.charAt(i))
/*  36 */         return false;
/*  37 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean endsWith(char[] s, int len, String suffix)
/*     */   {
/*  49 */     int suffixLen = suffix.length();
/*  50 */     if (suffixLen > len)
/*  51 */       return false;
/*  52 */     for (int i = suffixLen - 1; i >= 0; i--) {
/*  53 */       if (s[(len - (suffixLen - i))] != suffix.charAt(i))
/*  54 */         return false;
/*     */     }
/*  56 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean endsWith(char[] s, int len, char[] suffix)
/*     */   {
/*  68 */     int suffixLen = suffix.length;
/*  69 */     if (suffixLen > len)
/*  70 */       return false;
/*  71 */     for (int i = suffixLen - 1; i >= 0; i--) {
/*  72 */       if (s[(len - (suffixLen - i))] != suffix[i])
/*  73 */         return false;
/*     */     }
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   public static int delete(char[] s, int pos, int len)
/*     */   {
/*  87 */     if (pos < len) {
/*  88 */       System.arraycopy(s, pos + 1, s, pos, len - pos - 1);
/*     */     }
/*  90 */     return len - 1;
/*     */   }
/*     */ 
/*     */   public static int deleteN(char[] s, int pos, int len, int nChars)
/*     */   {
/* 104 */     for (int i = 0; i < nChars; i++)
/* 105 */       len = delete(s, pos, len);
/* 106 */     return len;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.util.StemmerUtil
 * JD-Core Version:    0.6.2
 */