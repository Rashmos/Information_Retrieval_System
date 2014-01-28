/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ public class SimpleStringInterner extends StringInterner
/*    */ {
/*    */   private final Entry[] cache;
/*    */   private final int maxChainLength;
/*    */ 
/*    */   public SimpleStringInterner(int tableSize, int maxChainLength)
/*    */   {
/* 45 */     this.cache = new Entry[Math.max(1, BitUtil.nextHighestPowerOfTwo(tableSize))];
/* 46 */     this.maxChainLength = Math.max(2, maxChainLength);
/*    */   }
/*    */ 
/*    */   public String intern(String s)
/*    */   {
/* 51 */     int h = s.hashCode();
/*    */ 
/* 54 */     int slot = h & this.cache.length - 1;
/*    */ 
/* 56 */     Entry first = this.cache[slot];
/* 57 */     Entry nextToLast = null;
/*    */ 
/* 59 */     int chainLength = 0;
/*    */ 
/* 61 */     for (Entry e = first; e != null; e = e.next) {
/* 62 */       if ((e.hash == h) && ((e.str == s) || (e.str.compareTo(s) == 0)))
/*    */       {
/* 64 */         return e.str;
/*    */       }
/*    */ 
/* 67 */       chainLength++;
/* 68 */       if (e.next != null) {
/* 69 */         nextToLast = e;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 74 */     s = s.intern();
/* 75 */     this.cache[slot] = new Entry(s, h, first, null);
/* 76 */     if (chainLength >= this.maxChainLength)
/*    */     {
/* 78 */       nextToLast.next = null;
/*    */     }
/* 80 */     return s;
/*    */   }
/*    */ 
/*    */   private static class Entry
/*    */   {
/*    */     private final String str;
/*    */     private final int hash;
/*    */     private Entry next;
/*    */ 
/*    */     private Entry(String str, int hash, Entry next)
/*    */     {
/* 31 */       this.str = str;
/* 32 */       this.hash = hash;
/* 33 */       this.next = next;
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.SimpleStringInterner
 * JD-Core Version:    0.6.2
 */