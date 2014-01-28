/*     */ package org.apache.lucene.search.payloads;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.Explanation;
/*     */ import org.apache.lucene.search.Scorer;
/*     */ import org.apache.lucene.search.Searcher;
/*     */ import org.apache.lucene.search.Similarity;
/*     */ import org.apache.lucene.search.Weight;
/*     */ import org.apache.lucene.search.spans.NearSpansOrdered;
/*     */ import org.apache.lucene.search.spans.NearSpansUnordered;
/*     */ import org.apache.lucene.search.spans.SpanNearQuery;
/*     */ import org.apache.lucene.search.spans.SpanQuery;
/*     */ import org.apache.lucene.search.spans.SpanScorer;
/*     */ import org.apache.lucene.search.spans.SpanWeight;
/*     */ import org.apache.lucene.search.spans.Spans;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class PayloadNearQuery extends SpanNearQuery
/*     */ {
/*     */   protected String fieldName;
/*     */   protected PayloadFunction function;
/*     */ 
/*     */   public PayloadNearQuery(SpanQuery[] clauses, int slop, boolean inOrder)
/*     */   {
/*  58 */     this(clauses, slop, inOrder, new AveragePayloadFunction());
/*     */   }
/*     */ 
/*     */   public PayloadNearQuery(SpanQuery[] clauses, int slop, boolean inOrder, PayloadFunction function)
/*     */   {
/*  63 */     super(clauses, slop, inOrder);
/*  64 */     this.fieldName = clauses[0].getField();
/*  65 */     this.function = function;
/*     */   }
/*     */ 
/*     */   public Weight createWeight(Searcher searcher) throws IOException
/*     */   {
/*  70 */     return new PayloadNearSpanWeight(this, searcher);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  75 */     int sz = this.clauses.size();
/*  76 */     SpanQuery[] newClauses = new SpanQuery[sz];
/*     */ 
/*  78 */     for (int i = 0; i < sz; i++) {
/*  79 */       newClauses[i] = ((SpanQuery)((SpanQuery)this.clauses.get(i)).clone());
/*     */     }
/*  81 */     PayloadNearQuery boostingNearQuery = new PayloadNearQuery(newClauses, this.slop, this.inOrder);
/*     */ 
/*  83 */     boostingNearQuery.setBoost(getBoost());
/*  84 */     return boostingNearQuery;
/*     */   }
/*     */ 
/*     */   public String toString(String field)
/*     */   {
/*  89 */     StringBuilder buffer = new StringBuilder();
/*  90 */     buffer.append("payloadNear([");
/*  91 */     Iterator i = this.clauses.iterator();
/*  92 */     while (i.hasNext()) {
/*  93 */       SpanQuery clause = (SpanQuery)i.next();
/*  94 */       buffer.append(clause.toString(field));
/*  95 */       if (i.hasNext()) {
/*  96 */         buffer.append(", ");
/*     */       }
/*     */     }
/*  99 */     buffer.append("], ");
/* 100 */     buffer.append(this.slop);
/* 101 */     buffer.append(", ");
/* 102 */     buffer.append(this.inOrder);
/* 103 */     buffer.append(")");
/* 104 */     buffer.append(ToStringUtils.boost(getBoost()));
/* 105 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 110 */     int prime = 31;
/* 111 */     int result = super.hashCode();
/* 112 */     result = 31 * result + (this.fieldName == null ? 0 : this.fieldName.hashCode());
/* 113 */     result = 31 * result + (this.function == null ? 0 : this.function.hashCode());
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 119 */     if (this == obj)
/* 120 */       return true;
/* 121 */     if (!super.equals(obj))
/* 122 */       return false;
/* 123 */     if (getClass() != obj.getClass())
/* 124 */       return false;
/* 125 */     PayloadNearQuery other = (PayloadNearQuery)obj;
/* 126 */     if (this.fieldName == null) {
/* 127 */       if (other.fieldName != null)
/* 128 */         return false;
/* 129 */     } else if (!this.fieldName.equals(other.fieldName))
/* 130 */       return false;
/* 131 */     if (this.function == null) {
/* 132 */       if (other.function != null)
/* 133 */         return false;
/* 134 */     } else if (!this.function.equals(other.function))
/* 135 */       return false;
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */   public class PayloadNearSpanScorer extends SpanScorer
/*     */   {
/*     */     Spans spans;
/*     */     protected float payloadScore;
/*     */     private int payloadsSeen;
/* 158 */     Similarity similarity = getSimilarity();
/*     */ 
/*     */     protected PayloadNearSpanScorer(Spans spans, Weight weight, Similarity similarity, byte[] norms) throws IOException
/*     */     {
/* 162 */       super(weight, similarity, norms);
/* 163 */       this.spans = spans;
/*     */     }
/*     */ 
/*     */     public void getPayloads(Spans[] subSpans) throws IOException
/*     */     {
/* 168 */       for (int i = 0; i < subSpans.length; i++)
/* 169 */         if ((subSpans[i] instanceof NearSpansOrdered)) {
/* 170 */           if (((NearSpansOrdered)subSpans[i]).isPayloadAvailable()) {
/* 171 */             processPayloads(((NearSpansOrdered)subSpans[i]).getPayload(), subSpans[i].start(), subSpans[i].end());
/*     */           }
/*     */ 
/* 174 */           getPayloads(((NearSpansOrdered)subSpans[i]).getSubSpans());
/* 175 */         } else if ((subSpans[i] instanceof NearSpansUnordered)) {
/* 176 */           if (((NearSpansUnordered)subSpans[i]).isPayloadAvailable()) {
/* 177 */             processPayloads(((NearSpansUnordered)subSpans[i]).getPayload(), subSpans[i].start(), subSpans[i].end());
/*     */           }
/*     */ 
/* 180 */           getPayloads(((NearSpansUnordered)subSpans[i]).getSubSpans());
/*     */         }
/*     */     }
/*     */ 
/*     */     protected void processPayloads(Collection<byte[]> payLoads, int start, int end)
/*     */     {
/* 196 */       for (byte[] thePayload : payLoads) {
/* 197 */         this.payloadScore = PayloadNearQuery.this.function.currentScore(this.doc, PayloadNearQuery.this.fieldName, start, end, this.payloadsSeen, this.payloadScore, this.similarity.scorePayload(this.doc, PayloadNearQuery.this.fieldName, this.spans.start(), this.spans.end(), thePayload, 0, thePayload.length));
/*     */ 
/* 200 */         this.payloadsSeen += 1;
/*     */       }
/*     */     }
/*     */ 
/*     */     protected boolean setFreqCurrentDoc()
/*     */       throws IOException
/*     */     {
/* 207 */       if (!this.more) {
/* 208 */         return false;
/*     */       }
/* 210 */       Spans[] spansArr = new Spans[1];
/* 211 */       spansArr[0] = this.spans;
/* 212 */       this.payloadScore = 0.0F;
/* 213 */       this.payloadsSeen = 0;
/* 214 */       getPayloads(spansArr);
/* 215 */       return super.setFreqCurrentDoc();
/*     */     }
/*     */ 
/*     */     public float score()
/*     */       throws IOException
/*     */     {
/* 221 */       return super.score() * PayloadNearQuery.this.function.docScore(this.doc, PayloadNearQuery.this.fieldName, this.payloadsSeen, this.payloadScore);
/*     */     }
/*     */ 
/*     */     protected Explanation explain(int doc)
/*     */       throws IOException
/*     */     {
/* 227 */       Explanation result = new Explanation();
/* 228 */       Explanation nonPayloadExpl = super.explain(doc);
/* 229 */       result.addDetail(nonPayloadExpl);
/* 230 */       Explanation payloadBoost = new Explanation();
/* 231 */       result.addDetail(payloadBoost);
/* 232 */       float avgPayloadScore = this.payloadsSeen > 0 ? this.payloadScore / this.payloadsSeen : 1.0F;
/*     */ 
/* 234 */       payloadBoost.setValue(avgPayloadScore);
/* 235 */       payloadBoost.setDescription("scorePayload(...)");
/* 236 */       result.setValue(nonPayloadExpl.getValue() * avgPayloadScore);
/* 237 */       result.setDescription("bnq, product of:");
/* 238 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class PayloadNearSpanWeight extends SpanWeight
/*     */   {
/*     */     public PayloadNearSpanWeight(SpanQuery query, Searcher searcher)
/*     */       throws IOException
/*     */     {
/* 142 */       super(searcher);
/*     */     }
/*     */ 
/*     */     public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer)
/*     */       throws IOException
/*     */     {
/* 148 */       return new PayloadNearQuery.PayloadNearSpanScorer(PayloadNearQuery.this, this.query.getSpans(reader), this, this.similarity, reader.norms(this.query.getField()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.payloads.PayloadNearQuery
 * JD-Core Version:    0.6.2
 */