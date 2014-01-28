/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Proximity;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class TokenizedDistance
/*     */   implements Distance<CharSequence>, Proximity<CharSequence>
/*     */ {
/*     */   final TokenizerFactory mTokenizerFactory;
/*     */ 
/*     */   public TokenizedDistance(TokenizerFactory tokenizerFactory)
/*     */   {
/*  63 */     this.mTokenizerFactory = tokenizerFactory;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/*  72 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public Set<String> tokenSet(CharSequence cSeq)
/*     */   {
/*  83 */     char[] cs = Strings.toCharArray(cSeq);
/*  84 */     return tokenSet(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Set<String> tokenSet(char[] cs, int start, int length)
/*     */   {
/* 101 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, length);
/* 102 */     Set tokenSet = new HashSet();
/*     */     String token;
/* 104 */     while ((token = tokenizer.nextToken()) != null)
/* 105 */       tokenSet.add(token);
/* 106 */     return tokenSet;
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap<String> termFrequencyVector(CharSequence cSeq)
/*     */   {
/* 118 */     ObjectToCounterMap termFrequency = new ObjectToCounterMap();
/* 119 */     char[] cs = Strings.toCharArray(cSeq);
/* 120 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*     */     String token;
/* 122 */     while ((token = tokenizer.nextToken()) != null)
/* 123 */       termFrequency.increment(token);
/* 124 */     return termFrequency;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.TokenizedDistance
 * JD-Core Version:    0.6.2
 */