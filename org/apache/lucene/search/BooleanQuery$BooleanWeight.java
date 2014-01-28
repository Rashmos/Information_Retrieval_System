/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ 
/*     */ public class BooleanQuery$BooleanWeight extends Weight
/*     */ {
/*     */   protected Similarity similarity;
/*     */   protected ArrayList<Weight> weights;
/*     */ 
/*     */   public BooleanQuery$BooleanWeight(BooleanQuery arg1, Searcher searcher)
/*     */     throws IOException
/*     */   {
/* 185 */     this.similarity = ???.getSimilarity(searcher);
/* 186 */     this.weights = new ArrayList(BooleanQuery.access$100(???).size());
/* 187 */     for (int i = 0; i < BooleanQuery.access$100(???).size(); i++)
/* 188 */       this.weights.add(((BooleanClause)BooleanQuery.access$100(???).get(i)).getQuery().createWeight(searcher));
/*     */   }
/*     */ 
/*     */   public Query getQuery()
/*     */   {
/* 193 */     return this.this$0;
/*     */   }
/*     */   public float getValue() {
/* 196 */     return this.this$0.getBoost();
/*     */   }
/*     */ 
/*     */   public float sumOfSquaredWeights() throws IOException {
/* 200 */     float sum = 0.0F;
/* 201 */     for (int i = 0; i < this.weights.size(); i++)
/*     */     {
/* 203 */       float s = ((Weight)this.weights.get(i)).sumOfSquaredWeights();
/* 204 */       if (!((BooleanClause)BooleanQuery.access$100(this.this$0).get(i)).isProhibited())
/*     */       {
/* 206 */         sum += s;
/*     */       }
/*     */     }
/* 209 */     sum *= this.this$0.getBoost() * this.this$0.getBoost();
/*     */ 
/* 211 */     return sum;
/*     */   }
/*     */ 
/*     */   public void normalize(float norm)
/*     */   {
/* 217 */     norm *= this.this$0.getBoost();
/* 218 */     for (Weight w : this.weights)
/*     */     {
/* 220 */       w.normalize(norm);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Explanation explain(IndexReader reader, int doc)
/*     */     throws IOException
/*     */   {
/* 227 */     int minShouldMatch = this.this$0.getMinimumNumberShouldMatch();
/*     */ 
/* 229 */     ComplexExplanation sumExpl = new ComplexExplanation();
/* 230 */     sumExpl.setDescription("sum of:");
/* 231 */     int coord = 0;
/* 232 */     int maxCoord = 0;
/* 233 */     float sum = 0.0F;
/* 234 */     boolean fail = false;
/* 235 */     int shouldMatchCount = 0;
/* 236 */     Iterator cIter = BooleanQuery.access$100(this.this$0).iterator();
/* 237 */     for (Iterator wIter = this.weights.iterator(); wIter.hasNext(); ) {
/* 238 */       Weight w = (Weight)wIter.next();
/* 239 */       BooleanClause c = (BooleanClause)cIter.next();
/* 240 */       if (w.scorer(reader, true, true) != null)
/*     */       {
/* 243 */         Explanation e = w.explain(reader, doc);
/* 244 */         if (!c.isProhibited()) maxCoord++;
/* 245 */         if (e.isMatch()) {
/* 246 */           if (!c.isProhibited()) {
/* 247 */             sumExpl.addDetail(e);
/* 248 */             sum += e.getValue();
/* 249 */             coord++;
/*     */           } else {
/* 251 */             Explanation r = new Explanation(0.0F, "match on prohibited clause (" + c.getQuery().toString() + ")");
/*     */ 
/* 253 */             r.addDetail(e);
/* 254 */             sumExpl.addDetail(r);
/* 255 */             fail = true;
/*     */           }
/* 257 */           if (c.getOccur() == BooleanClause.Occur.SHOULD)
/* 258 */             shouldMatchCount++;
/* 259 */         } else if (c.isRequired()) {
/* 260 */           Explanation r = new Explanation(0.0F, "no match on required clause (" + c.getQuery().toString() + ")");
/* 261 */           r.addDetail(e);
/* 262 */           sumExpl.addDetail(r);
/* 263 */           fail = true;
/*     */         }
/*     */       }
/*     */     }
/* 266 */     if (fail) {
/* 267 */       sumExpl.setMatch(Boolean.FALSE);
/* 268 */       sumExpl.setValue(0.0F);
/* 269 */       sumExpl.setDescription("Failure to meet condition(s) of required/prohibited clause(s)");
/*     */ 
/* 271 */       return sumExpl;
/* 272 */     }if (shouldMatchCount < minShouldMatch) {
/* 273 */       sumExpl.setMatch(Boolean.FALSE);
/* 274 */       sumExpl.setValue(0.0F);
/* 275 */       sumExpl.setDescription("Failure to match minimum number of optional clauses: " + minShouldMatch);
/*     */ 
/* 277 */       return sumExpl;
/*     */     }
/*     */ 
/* 280 */     sumExpl.setMatch(0 < coord ? Boolean.TRUE : Boolean.FALSE);
/* 281 */     sumExpl.setValue(sum);
/*     */ 
/* 283 */     float coordFactor = this.similarity.coord(coord, maxCoord);
/* 284 */     if (coordFactor == 1.0F) {
/* 285 */       return sumExpl;
/*     */     }
/* 287 */     ComplexExplanation result = new ComplexExplanation(sumExpl.isMatch(), sum * coordFactor, "product of:");
/*     */ 
/* 290 */     result.addDetail(sumExpl);
/* 291 */     result.addDetail(new Explanation(coordFactor, "coord(" + coord + "/" + maxCoord + ")"));
/*     */ 
/* 293 */     return result;
/*     */   }
/*     */ 
/*     */   public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer)
/*     */     throws IOException
/*     */   {
/* 300 */     List required = new ArrayList();
/* 301 */     List prohibited = new ArrayList();
/* 302 */     List optional = new ArrayList();
/* 303 */     Iterator cIter = BooleanQuery.access$100(this.this$0).iterator();
/* 304 */     for (Weight w : this.weights) {
/* 305 */       BooleanClause c = (BooleanClause)cIter.next();
/* 306 */       Scorer subScorer = w.scorer(reader, true, false);
/* 307 */       if (subScorer == null) {
/* 308 */         if (c.isRequired())
/* 309 */           return null;
/*     */       }
/* 311 */       else if (c.isRequired())
/* 312 */         required.add(subScorer);
/* 313 */       else if (c.isProhibited())
/* 314 */         prohibited.add(subScorer);
/*     */       else {
/* 316 */         optional.add(subScorer);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 321 */     if ((!scoreDocsInOrder) && (topScorer) && (required.size() == 0) && (prohibited.size() < 32)) {
/* 322 */       return new BooleanScorer(this.similarity, this.this$0.minNrShouldMatch, optional, prohibited);
/*     */     }
/*     */ 
/* 325 */     if ((required.size() == 0) && (optional.size() == 0))
/*     */     {
/* 327 */       return null;
/* 328 */     }if (optional.size() < this.this$0.minNrShouldMatch)
/*     */     {
/* 332 */       return null;
/*     */     }
/*     */ 
/* 336 */     return new BooleanScorer2(this.similarity, this.this$0.minNrShouldMatch, required, prohibited, optional);
/*     */   }
/*     */ 
/*     */   public boolean scoresDocsOutOfOrder()
/*     */   {
/* 341 */     int numProhibited = 0;
/* 342 */     for (BooleanClause c : BooleanQuery.access$100(this.this$0)) {
/* 343 */       if (c.isRequired())
/* 344 */         return false;
/* 345 */       if (c.isProhibited()) {
/* 346 */         numProhibited++;
/*     */       }
/*     */     }
/*     */ 
/* 350 */     if (numProhibited > 32) {
/* 351 */       return false;
/*     */     }
/*     */ 
/* 355 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanQuery.BooleanWeight
 * JD-Core Version:    0.6.2
 */