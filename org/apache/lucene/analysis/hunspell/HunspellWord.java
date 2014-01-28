/*    */ package org.apache.lucene.analysis.hunspell;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class HunspellWord
/*    */ {
/*    */   private final char[] flags;
/*    */ 
/*    */   public HunspellWord()
/*    */   {
/* 30 */     this.flags = null;
/*    */   }
/*    */ 
/*    */   public HunspellWord(char[] flags)
/*    */   {
/* 39 */     this.flags = flags;
/*    */   }
/*    */ 
/*    */   public boolean hasFlag(char flag)
/*    */   {
/* 49 */     return (this.flags != null) && (Arrays.binarySearch(this.flags, flag) >= 0);
/*    */   }
/*    */ 
/*    */   public char[] getFlags()
/*    */   {
/* 58 */     return this.flags;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hunspell.HunspellWord
 * JD-Core Version:    0.6.2
 */