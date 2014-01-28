/*    */ package com.aliasi.tag;
/*    */ 
/*    */ import com.aliasi.util.Scored;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ScoredTagging<E> extends Tagging<E>
/*    */   implements Scored
/*    */ {
/*    */   private final double mScore;
/*    */ 
/*    */   public ScoredTagging(List<E> tokens, List<String> tags, double score)
/*    */   {
/* 51 */     this(tokens, tags, score, true);
/*    */   }
/*    */ 
/*    */   ScoredTagging(List<E> tokens, List<String> tags, double score, boolean ignore)
/*    */   {
/* 59 */     super(tokens, tags, true);
/* 60 */     this.mScore = score;
/*    */   }
/*    */ 
/*    */   public double score()
/*    */   {
/* 69 */     return this.mScore;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 79 */     return score() + " " + super.toString();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.ScoredTagging
 * JD-Core Version:    0.6.2
 */