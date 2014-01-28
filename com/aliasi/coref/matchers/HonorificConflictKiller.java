/*    */ package com.aliasi.coref.matchers;
/*    */ 
/*    */ import com.aliasi.coref.EnglishMentionFactory;
/*    */ import com.aliasi.coref.Killer;
/*    */ import com.aliasi.coref.Mention;
/*    */ import com.aliasi.coref.MentionChain;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class HonorificConflictKiller
/*    */   implements Killer
/*    */ {
/*    */   public boolean kill(Mention mention, MentionChain chain)
/*    */   {
/* 51 */     Set honorifics1 = mention.honorifics();
/* 52 */     Set honorifics2 = chain.honorifics();
/* 53 */     return (honorifics1.size() > 0) && (honorifics2.size() > 0) && (honorificConflict(honorifics1, honorifics2));
/*    */   }
/*    */ 
/*    */   private static boolean honorificConflict(Set<String> honorifics1, Set<String> honorifics2)
/*    */   {
/* 60 */     return ((male(honorifics1)) && (female(honorifics2))) || ((female(honorifics1)) && (male(honorifics2)));
/*    */   }
/*    */ 
/*    */   private static boolean male(Set<String> honorifics)
/*    */   {
/* 65 */     return !Collections.disjoint(honorifics, EnglishMentionFactory.MALE_HONORIFICS);
/*    */   }
/*    */ 
/*    */   private static boolean female(Set<String> honorifics)
/*    */   {
/* 71 */     return !Collections.disjoint(honorifics, EnglishMentionFactory.FEMALE_HONORIFICS);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.matchers.HonorificConflictKiller
 * JD-Core Version:    0.6.2
 */