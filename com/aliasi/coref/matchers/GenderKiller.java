/*    */ package com.aliasi.coref.matchers;
/*    */ 
/*    */ import com.aliasi.coref.Killer;
/*    */ import com.aliasi.coref.Mention;
/*    */ import com.aliasi.coref.MentionChain;
/*    */ 
/*    */ public class GenderKiller
/*    */   implements Killer
/*    */ {
/*    */   public boolean kill(Mention mention, MentionChain chain)
/*    */   {
/* 51 */     return (mention.gender() != null) && (chain.gender() != null) && (!mention.gender().equals(chain.gender()));
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.matchers.GenderKiller
 * JD-Core Version:    0.6.2
 */