/*    */ package com.aliasi.coref;
/*    */ 
/*    */ public abstract class BooleanMatcherAdapter
/*    */   implements Matcher
/*    */ {
/*    */   private final int mScore;
/*    */ 
/*    */   public BooleanMatcherAdapter(int score)
/*    */   {
/* 44 */     this.mScore = score;
/*    */   }
/*    */ 
/*    */   public abstract boolean matchBoolean(Mention paramMention, MentionChain paramMentionChain);
/*    */ 
/*    */   public final int match(Mention mention, MentionChain mentionChain)
/*    */   {
/* 75 */     return matchBoolean(mention, mentionChain) ? this.mScore : -1;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.BooleanMatcherAdapter
 * JD-Core Version:    0.6.2
 */