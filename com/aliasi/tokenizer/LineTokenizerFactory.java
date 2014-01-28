/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ public class LineTokenizerFactory extends RegExTokenizerFactory
/*     */ {
/*     */   static final long serialVersionUID = -6005548133398620559L;
/* 102 */   public static final LineTokenizerFactory INSTANCE = new LineTokenizerFactory();
/*     */ 
/*     */   LineTokenizerFactory()
/*     */   {
/*  84 */     super(".+");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  94 */     return getClass().getName();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.LineTokenizerFactory
 * JD-Core Version:    0.6.2
 */