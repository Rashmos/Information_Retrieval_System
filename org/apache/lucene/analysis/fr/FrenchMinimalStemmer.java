/*    */ package org.apache.lucene.analysis.fr;
/*    */ 
/*    */ public class FrenchMinimalStemmer
/*    */ {
/*    */   public int stem(char[] s, int len)
/*    */   {
/* 64 */     if (len < 6) {
/* 65 */       return len;
/*    */     }
/* 67 */     if (s[(len - 1)] == 'x') {
/* 68 */       if ((s[(len - 3)] == 'a') && (s[(len - 2)] == 'u'))
/* 69 */         s[(len - 2)] = 'l';
/* 70 */       return len - 1;
/*    */     }
/*    */ 
/* 73 */     if (s[(len - 1)] == 's') len--;
/* 74 */     if (s[(len - 1)] == 'r') len--;
/* 75 */     if (s[(len - 1)] == 'e') len--;
/* 76 */     if (s[(len - 1)] == 'é') len--;
/* 77 */     if (s[(len - 1)] == s[(len - 2)]) len--;
/* 78 */     return len;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fr.FrenchMinimalStemmer
 * JD-Core Version:    0.6.2
 */