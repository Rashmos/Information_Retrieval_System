/*    */ package com.aliasi.stats;
/*    */ 
/*    */ public class BernoulliConstant extends BernoulliDistribution
/*    */ {
/*    */   private final double mSuccessProbability;
/*    */ 
/*    */   public BernoulliConstant(double successProbability)
/*    */   {
/* 41 */     validateProbability(successProbability);
/* 42 */     this.mSuccessProbability = successProbability;
/*    */   }
/*    */ 
/*    */   public double successProbability()
/*    */   {
/* 52 */     return this.mSuccessProbability;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.BernoulliConstant
 * JD-Core Version:    0.6.2
 */