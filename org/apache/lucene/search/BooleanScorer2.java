/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ class BooleanScorer2 extends Scorer
/*     */ {
/*     */   private final List<Scorer> requiredScorers;
/*     */   private final List<Scorer> optionalScorers;
/*     */   private final List<Scorer> prohibitedScorers;
/*     */   private final Coordinator coordinator;
/*     */   private final Scorer countingSumScorer;
/*     */   private final int minNrShouldMatch;
/*  63 */   private int doc = -1;
/*     */ 
/* 168 */   private static final Similarity defaultSimilarity = Similarity.getDefault();
/*     */ 
/*     */   public BooleanScorer2(Similarity similarity, int minNrShouldMatch, List<Scorer> required, List<Scorer> prohibited, List<Scorer> optional)
/*     */     throws IOException
/*     */   {
/*  85 */     super(similarity);
/*  86 */     if (minNrShouldMatch < 0) {
/*  87 */       throw new IllegalArgumentException("Minimum number of optional scorers should not be negative");
/*     */     }
/*  89 */     this.coordinator = new Coordinator(null);
/*  90 */     this.minNrShouldMatch = minNrShouldMatch;
/*     */ 
/*  92 */     this.optionalScorers = optional;
/*  93 */     this.coordinator.maxCoord += optional.size();
/*     */ 
/*  95 */     this.requiredScorers = required;
/*  96 */     this.coordinator.maxCoord += required.size();
/*     */ 
/*  98 */     this.prohibitedScorers = prohibited;
/*     */ 
/* 100 */     this.coordinator.init();
/* 101 */     this.countingSumScorer = makeCountingSumScorer();
/*     */   }
/*     */ 
/*     */   private Scorer countingDisjunctionSumScorer(List<Scorer> scorers, int minNrShouldMatch)
/*     */     throws IOException
/*     */   {
/* 149 */     return new DisjunctionSumScorer(scorers, minNrShouldMatch) {
/* 150 */       private int lastScoredDoc = -1;
/*     */ 
/* 153 */       private float lastDocScore = (0.0F / 0.0F);
/*     */ 
/* 155 */       public float score() throws IOException { int doc = docID();
/* 156 */         if (doc >= this.lastScoredDoc) {
/* 157 */           if (doc > this.lastScoredDoc) {
/* 158 */             this.lastDocScore = super.score();
/* 159 */             this.lastScoredDoc = doc;
/*     */           }
/* 161 */           BooleanScorer2.this.coordinator.nrMatchers += this.nrMatchers;
/*     */         }
/* 163 */         return this.lastDocScore;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private Scorer countingConjunctionSumScorer(List<Scorer> requiredScorers)
/*     */     throws IOException
/*     */   {
/* 172 */     final int requiredNrMatchers = requiredScorers.size();
/* 173 */     return new ConjunctionScorer(defaultSimilarity, requiredScorers) {
/* 174 */       private int lastScoredDoc = -1;
/*     */ 
/* 177 */       private float lastDocScore = (0.0F / 0.0F);
/*     */ 
/* 179 */       public float score() throws IOException { int doc = docID();
/* 180 */         if (doc >= this.lastScoredDoc) {
/* 181 */           if (doc > this.lastScoredDoc) {
/* 182 */             this.lastDocScore = super.score();
/* 183 */             this.lastScoredDoc = doc;
/*     */           }
/* 185 */           BooleanScorer2.this.coordinator.nrMatchers += requiredNrMatchers;
/*     */         }
/*     */ 
/* 191 */         return this.lastDocScore; }
/*     */     };
/*     */   }
/*     */ 
/*     */   private Scorer dualConjunctionSumScorer(Scorer req1, Scorer req2) throws IOException
/*     */   {
/* 197 */     return new ConjunctionScorer(defaultSimilarity, new Scorer[] { req1, req2 });
/*     */   }
/*     */ 
/*     */   private Scorer makeCountingSumScorer()
/*     */     throws IOException
/*     */   {
/* 208 */     return this.requiredScorers.size() == 0 ? makeCountingSumScorerNoReq() : makeCountingSumScorerSomeReq();
/*     */   }
/*     */ 
/*     */   private Scorer makeCountingSumScorerNoReq()
/*     */     throws IOException
/*     */   {
/* 215 */     int nrOptRequired = this.minNrShouldMatch < 1 ? 1 : this.minNrShouldMatch;
/*     */     Scorer requiredCountingSumScorer;
/*     */     Scorer requiredCountingSumScorer;
/* 217 */     if (this.optionalScorers.size() > nrOptRequired) {
/* 218 */       requiredCountingSumScorer = countingDisjunctionSumScorer(this.optionalScorers, nrOptRequired);
/*     */     }
/*     */     else
/*     */     {
/*     */       Scorer requiredCountingSumScorer;
/* 219 */       if (this.optionalScorers.size() == 1)
/* 220 */         requiredCountingSumScorer = new SingleMatchScorer((Scorer)this.optionalScorers.get(0));
/*     */       else
/* 222 */         requiredCountingSumScorer = countingConjunctionSumScorer(this.optionalScorers); 
/*     */     }
/* 223 */     return addProhibitedScorers(requiredCountingSumScorer);
/*     */   }
/*     */ 
/*     */   private Scorer makeCountingSumScorerSomeReq() throws IOException {
/* 227 */     if (this.optionalScorers.size() == this.minNrShouldMatch) {
/* 228 */       ArrayList allReq = new ArrayList(this.requiredScorers);
/* 229 */       allReq.addAll(this.optionalScorers);
/* 230 */       return addProhibitedScorers(countingConjunctionSumScorer(allReq));
/*     */     }
/* 232 */     Scorer requiredCountingSumScorer = this.requiredScorers.size() == 1 ? new SingleMatchScorer((Scorer)this.requiredScorers.get(0)) : countingConjunctionSumScorer(this.requiredScorers);
/*     */ 
/* 236 */     if (this.minNrShouldMatch > 0) {
/* 237 */       return addProhibitedScorers(dualConjunctionSumScorer(requiredCountingSumScorer, countingDisjunctionSumScorer(this.optionalScorers, this.minNrShouldMatch)));
/*     */     }
/*     */ 
/* 244 */     return new ReqOptSumScorer(addProhibitedScorers(requiredCountingSumScorer), this.optionalScorers.size() == 1 ? new SingleMatchScorer((Scorer)this.optionalScorers.get(0)) : countingDisjunctionSumScorer(this.optionalScorers, 1));
/*     */   }
/*     */ 
/*     */   private Scorer addProhibitedScorers(Scorer requiredCountingSumScorer)
/*     */     throws IOException
/*     */   {
/* 260 */     return this.prohibitedScorers.size() == 0 ? requiredCountingSumScorer : new ReqExclScorer(requiredCountingSumScorer, this.prohibitedScorers.size() == 1 ? (Scorer)this.prohibitedScorers.get(0) : new DisjunctionSumScorer(this.prohibitedScorers));
/*     */   }
/*     */ 
/*     */   public void score(Collector collector)
/*     */     throws IOException
/*     */   {
/* 273 */     collector.setScorer(this);
/* 274 */     while ((this.doc = this.countingSumScorer.nextDoc()) != 2147483647)
/* 275 */       collector.collect(this.doc);
/*     */   }
/*     */ 
/*     */   protected boolean score(Collector collector, int max, int firstDocID)
/*     */     throws IOException
/*     */   {
/* 281 */     this.doc = firstDocID;
/* 282 */     collector.setScorer(this);
/* 283 */     while (this.doc < max) {
/* 284 */       collector.collect(this.doc);
/* 285 */       this.doc = this.countingSumScorer.nextDoc();
/*     */     }
/* 287 */     return this.doc != 2147483647;
/*     */   }
/*     */ 
/*     */   public int docID()
/*     */   {
/* 292 */     return this.doc;
/*     */   }
/*     */ 
/*     */   public int nextDoc() throws IOException
/*     */   {
/* 297 */     return this.doc = this.countingSumScorer.nextDoc();
/*     */   }
/*     */ 
/*     */   public float score() throws IOException
/*     */   {
/* 302 */     this.coordinator.nrMatchers = 0;
/* 303 */     float sum = this.countingSumScorer.score();
/* 304 */     return sum * this.coordinator.coordFactors[this.coordinator.nrMatchers];
/*     */   }
/*     */ 
/*     */   public int advance(int target) throws IOException
/*     */   {
/* 309 */     return this.doc = this.countingSumScorer.advance(target);
/*     */   }
/*     */ 
/*     */   private class SingleMatchScorer extends Scorer
/*     */   {
/*     */     private Scorer scorer;
/* 107 */     private int lastScoredDoc = -1;
/*     */ 
/* 110 */     private float lastDocScore = (0.0F / 0.0F);
/*     */ 
/*     */     SingleMatchScorer(Scorer scorer) {
/* 113 */       super();
/* 114 */       this.scorer = scorer;
/*     */     }
/*     */ 
/*     */     public float score() throws IOException
/*     */     {
/* 119 */       int doc = docID();
/* 120 */       if (doc >= this.lastScoredDoc) {
/* 121 */         if (doc > this.lastScoredDoc) {
/* 122 */           this.lastDocScore = this.scorer.score();
/* 123 */           this.lastScoredDoc = doc;
/*     */         }
/* 125 */         BooleanScorer2.this.coordinator.nrMatchers += 1;
/*     */       }
/* 127 */       return this.lastDocScore;
/*     */     }
/*     */ 
/*     */     public int docID()
/*     */     {
/* 132 */       return this.scorer.docID();
/*     */     }
/*     */ 
/*     */     public int nextDoc() throws IOException
/*     */     {
/* 137 */       return this.scorer.nextDoc();
/*     */     }
/*     */ 
/*     */     public int advance(int target) throws IOException
/*     */     {
/* 142 */       return this.scorer.advance(target);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Coordinator
/*     */   {
/*  39 */     float[] coordFactors = null;
/*  40 */     int maxCoord = 0;
/*     */     int nrMatchers;
/*     */ 
/*     */     private Coordinator()
/*     */     {
/*     */     }
/*     */ 
/*     */     void init()
/*     */     {
/*  44 */       this.coordFactors = new float[this.maxCoord + 1];
/*  45 */       Similarity sim = BooleanScorer2.this.getSimilarity();
/*  46 */       for (int i = 0; i <= this.maxCoord; i++)
/*  47 */         this.coordFactors[i] = sim.coord(i, this.maxCoord);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanScorer2
 * JD-Core Version:    0.6.2
 */