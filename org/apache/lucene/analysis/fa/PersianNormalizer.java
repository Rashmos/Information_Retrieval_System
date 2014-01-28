/*    */ package org.apache.lucene.analysis.fa;
/*    */ 
/*    */ import org.apache.lucene.analysis.util.StemmerUtil;
/*    */ 
/*    */ public class PersianNormalizer
/*    */ {
/*    */   public static final char YEH = 'ي';
/*    */   public static final char FARSI_YEH = 'ی';
/*    */   public static final char YEH_BARREE = 'ے';
/*    */   public static final char KEHEH = 'ک';
/*    */   public static final char KAF = 'ك';
/*    */   public static final char HAMZA_ABOVE = 'ٔ';
/*    */   public static final char HEH_YEH = 'ۀ';
/*    */   public static final char HEH_GOAL = 'ہ';
/*    */   public static final char HEH = 'ه';
/*    */ 
/*    */   public int normalize(char[] s, int len)
/*    */   {
/* 63 */     for (int i = 0; i < len; i++) {
/* 64 */       switch (s[i]) {
/*    */       case 'ی':
/*    */       case 'ے':
/* 67 */         s[i] = 'ي';
/* 68 */         break;
/*    */       case 'ک':
/* 70 */         s[i] = 'ك';
/* 71 */         break;
/*    */       case 'ۀ':
/*    */       case 'ہ':
/* 74 */         s[i] = 'ه';
/* 75 */         break;
/*    */       case 'ٔ':
/* 77 */         len = StemmerUtil.delete(s, i, len);
/* 78 */         i--;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 85 */     return len;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fa.PersianNormalizer
 * JD-Core Version:    0.6.2
 */