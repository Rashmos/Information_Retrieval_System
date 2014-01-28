/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.Counter;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Collections;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TfIdfDistance extends TokenizedDistance
/*     */   implements ObjectHandler<CharSequence>
/*     */ {
/* 161 */   private int mDocCount = 0;
/* 162 */   private final ObjectToCounterMap<String> mDocFrequency = new ObjectToCounterMap();
/*     */ 
/*     */   public TfIdfDistance(TokenizerFactory tokenizerFactory)
/*     */   {
/* 172 */     super(tokenizerFactory);
/*     */   }
/*     */ 
/*     */   public void handle(CharSequence cSeq)
/*     */   {
/* 181 */     char[] cs = Strings.toCharArray(cSeq);
/* 182 */     for (String token : tokenSet(cs, 0, cs.length))
/* 183 */       this.mDocFrequency.increment(token);
/* 184 */     this.mDocCount += 1;
/*     */   }
/*     */ 
/*     */   public double distance(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 199 */     return 1.0D - proximity(cSeq1, cSeq2);
/*     */   }
/*     */ 
/*     */   public double proximity(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 214 */     ObjectToCounterMap tf1 = termFrequencyVector(cSeq1);
/* 215 */     ObjectToCounterMap tf2 = termFrequencyVector(cSeq2);
/* 216 */     double len1 = 0.0D;
/* 217 */     double len2 = 0.0D;
/* 218 */     double prod = 0.0D;
/* 219 */     for (Map.Entry entry : tf1.entrySet()) {
/* 220 */       String term = (String)entry.getKey();
/* 221 */       Counter count1 = (Counter)entry.getValue();
/* 222 */       double tfIdf1 = tfIdf(term, count1);
/* 223 */       len1 += tfIdf1 * tfIdf1;
/* 224 */       Counter count2 = (Counter)tf2.remove(term);
/* 225 */       if (count2 != null) {
/* 226 */         double tfIdf2 = tfIdf(term, count2);
/* 227 */         len2 += tfIdf2 * tfIdf2;
/* 228 */         prod += tfIdf1 * tfIdf2;
/*     */       }
/*     */     }
/* 231 */     for (Map.Entry entry : tf2.entrySet()) {
/* 232 */       String term = (String)entry.getKey();
/* 233 */       Counter count2 = (Counter)entry.getValue();
/* 234 */       double tfIdf2 = tfIdf(term, count2);
/* 235 */       len2 += tfIdf2 * tfIdf2;
/*     */     }
/* 237 */     if (len1 == 0.0D)
/* 238 */       return len2 == 0.0D ? 1.0D : 0.0D;
/* 239 */     if (len2 == 0.0D) return 0.0D;
/* 240 */     double prox = prod / Math.sqrt(len1 * len2);
/* 241 */     return prox > 1.0D ? 1.0D : prox < 0.0D ? 0.0D : prox;
/*     */   }
/*     */ 
/*     */   public int docFrequency(String term)
/*     */   {
/* 258 */     return this.mDocFrequency.getCount(term);
/*     */   }
/*     */ 
/*     */   public double idf(String term)
/*     */   {
/* 270 */     int df = this.mDocFrequency.getCount(term);
/* 271 */     if (df == 0) return 0.0D;
/* 272 */     return Math.log(this.mDocCount / df);
/*     */   }
/*     */ 
/*     */   public int numDocuments()
/*     */   {
/* 281 */     return this.mDocCount;
/*     */   }
/*     */ 
/*     */   public int numTerms()
/*     */   {
/* 291 */     return this.mDocFrequency.size();
/*     */   }
/*     */ 
/*     */   public Set<String> termSet()
/*     */   {
/* 305 */     return Collections.unmodifiableSet(this.mDocFrequency.keySet());
/*     */   }
/*     */ 
/*     */   double tfIdf(String term, Counter count) {
/* 309 */     double idf = idf(term);
/* 310 */     double tf = count.doubleValue();
/* 311 */     return Math.sqrt(tf * idf);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.TfIdfDistance
 * JD-Core Version:    0.6.2
 */