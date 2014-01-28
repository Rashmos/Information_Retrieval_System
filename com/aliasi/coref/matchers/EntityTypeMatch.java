/*    */ package com.aliasi.coref.matchers;
/*    */ 
/*    */ import com.aliasi.coref.BooleanMatcherAdapter;
/*    */ import com.aliasi.coref.Mention;
/*    */ import com.aliasi.coref.MentionChain;
/*    */ 
/*    */ public class EntityTypeMatch extends BooleanMatcherAdapter
/*    */ {
/*    */   private final String mEntityType;
/*    */ 
/*    */   public EntityTypeMatch(int score, String entityType)
/*    */   {
/* 48 */     super(score);
/* 49 */     this.mEntityType = entityType;
/*    */   }
/*    */ 
/*    */   public boolean matchBoolean(Mention mention, MentionChain chain)
/*    */   {
/* 62 */     return mention.entityType().equals(this.mEntityType);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.matchers.EntityTypeMatch
 * JD-Core Version:    0.6.2
 */