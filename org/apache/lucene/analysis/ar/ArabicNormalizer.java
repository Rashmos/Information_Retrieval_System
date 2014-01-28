/*    */ package org.apache.lucene.analysis.ar;
/*    */ 
/*    */ import org.apache.lucene.analysis.util.StemmerUtil;
/*    */ 
/*    */ public class ArabicNormalizer
/*    */ {
/*    */   public static final char ALEF = 'ا';
/*    */   public static final char ALEF_MADDA = 'آ';
/*    */   public static final char ALEF_HAMZA_ABOVE = 'أ';
/*    */   public static final char ALEF_HAMZA_BELOW = 'إ';
/*    */   public static final char YEH = 'ي';
/*    */   public static final char DOTLESS_YEH = 'ى';
/*    */   public static final char TEH_MARBUTA = 'ة';
/*    */   public static final char HEH = 'ه';
/*    */   public static final char TATWEEL = 'ـ';
/*    */   public static final char FATHATAN = 'ً';
/*    */   public static final char DAMMATAN = 'ٌ';
/*    */   public static final char KASRATAN = 'ٍ';
/*    */   public static final char FATHA = 'َ';
/*    */   public static final char DAMMA = 'ُ';
/*    */   public static final char KASRA = 'ِ';
/*    */   public static final char SHADDA = 'ّ';
/*    */   public static final char SUKUN = 'ْ';
/*    */ 
/*    */   public int normalize(char[] s, int len)
/*    */   {
/* 69 */     for (int i = 0; i < len; i++)
/* 70 */       switch (s[i]) {
/*    */       case 'آ':
/*    */       case 'أ':
/*    */       case 'إ':
/* 74 */         s[i] = 'ا';
/* 75 */         break;
/*    */       case 'ى':
/* 77 */         s[i] = 'ي';
/* 78 */         break;
/*    */       case 'ة':
/* 80 */         s[i] = 'ه';
/* 81 */         break;
/*    */       case 'ـ':
/*    */       case 'ً':
/*    */       case 'ٌ':
/*    */       case 'ٍ':
/*    */       case 'َ':
/*    */       case 'ُ':
/*    */       case 'ِ':
/*    */       case 'ّ':
/*    */       case 'ْ':
/* 91 */         len = StemmerUtil.delete(s, i, len);
/* 92 */         i--;
/*    */       case 'ؤ':
/*    */       case 'ئ':
/*    */       case 'ا':
/*    */       case 'ب':
/*    */       case 'ت':
/*    */       case 'ث':
/*    */       case 'ج':
/*    */       case 'ح':
/*    */       case 'خ':
/*    */       case 'د':
/*    */       case 'ذ':
/*    */       case 'ر':
/*    */       case 'ز':
/*    */       case 'س':
/*    */       case 'ش':
/*    */       case 'ص':
/*    */       case 'ض':
/*    */       case 'ط':
/*    */       case 'ظ':
/*    */       case 'ع':
/*    */       case 'غ':
/*    */       case 'ػ':
/*    */       case 'ؼ':
/*    */       case 'ؽ':
/*    */       case 'ؾ':
/*    */       case 'ؿ':
/*    */       case 'ف':
/*    */       case 'ق':
/*    */       case 'ك':
/*    */       case 'ل':
/*    */       case 'م':
/*    */       case 'ن':
/*    */       case 'ه':
/*    */       case 'و':
/* 99 */       case 'ي': }  return len;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ar.ArabicNormalizer
 * JD-Core Version:    0.6.2
 */