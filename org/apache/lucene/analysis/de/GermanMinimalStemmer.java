/*    */ package org.apache.lucene.analysis.de;
/*    */ 
/*    */ public class GermanMinimalStemmer
/*    */ {
/*    */   public int stem(char[] s, int len)
/*    */   {
/* 65 */     if (len < 5) {
/* 66 */       return len;
/*    */     }
/* 68 */     for (int i = 0; i < len; i++) {
/* 69 */       switch (s[i]) { case 'ä':
/* 70 */         s[i] = 'a'; break;
/*    */       case 'ö':
/* 71 */         s[i] = 'o'; break;
/*    */       case 'ü':
/* 72 */         s[i] = 'u';
/*    */       }
/*    */     }
/* 75 */     if ((len > 6) && (s[(len - 3)] == 'n') && (s[(len - 2)] == 'e') && (s[(len - 1)] == 'n')) {
/* 76 */       return len - 3;
/*    */     }
/* 78 */     if (len > 5)
/* 79 */       switch (s[(len - 1)]) { case 'n':
/* 80 */         if (s[(len - 2)] == 'e') return len - 2; break;
/*    */       case 'e':
/* 81 */         if (s[(len - 2)] == 's') return len - 2; break;
/*    */       case 's':
/* 82 */         if (s[(len - 2)] == 'e') return len - 2; break;
/*    */       case 'r':
/* 83 */         if (s[(len - 2)] == 'e') return len - 2;
/*    */         break;
/*    */       }
/* 86 */     switch (s[(len - 1)]) { case 'e':
/*    */     case 'n':
/*    */     case 'r':
/*    */     case 's':
/* 90 */       return len - 1;
/*    */     }
/*    */ 
/* 93 */     return len;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.de.GermanMinimalStemmer
 * JD-Core Version:    0.6.2
 */