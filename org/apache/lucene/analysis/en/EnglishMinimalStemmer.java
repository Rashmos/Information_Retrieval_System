/*    */ package org.apache.lucene.analysis.en;
/*    */ 
/*    */ public class EnglishMinimalStemmer
/*    */ {
/*    */   public int stem(char[] s, int len)
/*    */   {
/* 30 */     if ((len < 3) || (s[(len - 1)] != 's')) {
/* 31 */       return len;
/*    */     }
/* 33 */     switch (s[(len - 2)]) { case 's':
/*    */     case 'u':
/* 35 */       return len;
/*    */     case 'e':
/* 37 */       if ((len > 3) && (s[(len - 3)] == 'i') && (s[(len - 4)] != 'a') && (s[(len - 4)] != 'e')) {
/* 38 */         s[(len - 3)] = 'y';
/* 39 */         return len - 2;
/*    */       }
/* 41 */       if ((s[(len - 3)] == 'i') || (s[(len - 3)] == 'a') || (s[(len - 3)] == 'o') || (s[(len - 3)] == 'e'))
/* 42 */         return len; break; }
/* 43 */     return len - 1;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.en.EnglishMinimalStemmer
 * JD-Core Version:    0.6.2
 */