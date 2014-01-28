/*     */ package com.aliasi.hmm;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tag.Tagging;
/*     */ import com.aliasi.util.Compilable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public abstract class AbstractHmmEstimator extends AbstractHmm
/*     */   implements ObjectHandler<Tagging<String>>, Compilable
/*     */ {
/*  51 */   private long mNumTrainingTokens = 0L;
/*  52 */   private long mNumTrainingTaggings = 0L;
/*     */ 
/*     */   public AbstractHmmEstimator(SymbolTable table)
/*     */   {
/*  60 */     super(table);
/*     */   }
/*     */ 
/*     */   public abstract void trainStart(String paramString);
/*     */ 
/*     */   public abstract void trainEnd(String paramString);
/*     */ 
/*     */   public abstract void trainTransit(String paramString1, String paramString2);
/*     */ 
/*     */   public abstract void trainEmit(String paramString, CharSequence paramCharSequence);
/*     */ 
/*     */   public abstract void compileTo(ObjectOutput paramObjectOutput)
/*     */     throws IOException;
/*     */ 
/*     */   public long numTrainingCases()
/*     */   {
/* 123 */     return this.mNumTrainingTaggings;
/*     */   }
/*     */ 
/*     */   public long numTrainingTokens()
/*     */   {
/* 134 */     return this.mNumTrainingTokens;
/*     */   }
/*     */ 
/*     */   public void handle(Tagging<String> tagging)
/*     */   {
/* 155 */     this.mNumTrainingTaggings += 1L;
/* 156 */     this.mNumTrainingTokens += tagging.size();
/* 157 */     if (tagging.size() < 1) return;
/* 158 */     trainStart(tagging.tag(0));
/* 159 */     for (int i = 0; i < tagging.size(); i++) {
/* 160 */       trainEmit(tagging.tag(i), (CharSequence)tagging.token(i));
/* 161 */       if (i > 0) trainTransit(tagging.tag(i - 1), tagging.tag(i));
/*     */     }
/*     */ 
/* 164 */     trainEnd(tagging.tag(tagging.size() - 1));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.hmm.AbstractHmmEstimator
 * JD-Core Version:    0.6.2
 */