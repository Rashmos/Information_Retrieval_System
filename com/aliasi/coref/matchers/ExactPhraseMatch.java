/*    */ package com.aliasi.coref.matchers;
/*    */ 
/*    */ import com.aliasi.coref.BooleanMatcherAdapter;
/*    */ import com.aliasi.coref.Mention;
/*    */ import com.aliasi.coref.MentionChain;
/*    */ 
/*    */ public final class ExactPhraseMatch extends BooleanMatcherAdapter
/*    */ {
/*    */   public ExactPhraseMatch(int score)
/*    */   {
/* 42 */     super(score);
/*    */   }
/*    */ 
/*    */   public boolean matchBoolean(Mention mention, MentionChain chain)
/*    */   {
/* 57 */     if (mention.isPronominal()) return false;
/* 58 */     String mentionPhrase = mention.normalPhrase();
/* 59 */     for (Mention chainMention : chain.mentions())
/* 60 */       if (mentionPhrase.equals(chainMention.normalPhrase()))
/* 61 */         return true;
/* 62 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.matchers.ExactPhraseMatch
 * JD-Core Version:    0.6.2
 */