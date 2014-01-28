/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ public abstract class StringHelper
/*    */ {
/* 30 */   public static StringInterner interner = new SimpleStringInterner(1024, 8);
/*    */ 
/*    */   public static String intern(String s)
/*    */   {
/* 34 */     return interner.intern(s);
/*    */   }
/*    */ 
/*    */   public static final int bytesDifference(byte[] bytes1, int len1, byte[] bytes2, int len2)
/*    */   {
/* 46 */     int len = len1 < len2 ? len1 : len2;
/* 47 */     for (int i = 0; i < len; i++)
/* 48 */       if (bytes1[i] != bytes2[i])
/* 49 */         return i;
/* 50 */     return len;
/*    */   }
/*    */ 
/*    */   public static final int stringDifference(String s1, String s2)
/*    */   {
/* 62 */     int len1 = s1.length();
/* 63 */     int len2 = s2.length();
/* 64 */     int len = len1 < len2 ? len1 : len2;
/* 65 */     for (int i = 0; i < len; i++) {
/* 66 */       if (s1.charAt(i) != s2.charAt(i)) {
/* 67 */         return i;
/*    */       }
/*    */     }
/* 70 */     return len;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.StringHelper
 * JD-Core Version:    0.6.2
 */