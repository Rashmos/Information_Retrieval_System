/*     */ package org.apache.lucene.search.spans;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.search.Query;
/*     */ import org.apache.lucene.search.Searcher;
/*     */ import org.apache.lucene.search.Similarity;
/*     */ import org.apache.lucene.search.Weight;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class FieldMaskingSpanQuery extends SpanQuery
/*     */ {
/*     */   private SpanQuery maskedQuery;
/*     */   private String field;
/*     */ 
/*     */   public FieldMaskingSpanQuery(SpanQuery maskedQuery, String maskedField)
/*     */   {
/*  78 */     this.maskedQuery = maskedQuery;
/*  79 */     this.field = maskedField;
/*     */   }
/*     */ 
/*     */   public String getField()
/*     */   {
/*  84 */     return this.field;
/*     */   }
/*     */ 
/*     */   public SpanQuery getMaskedQuery() {
/*  88 */     return this.maskedQuery;
/*     */   }
/*     */ 
/*     */   public Spans getSpans(IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  96 */     return this.maskedQuery.getSpans(reader);
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms)
/*     */   {
/* 101 */     this.maskedQuery.extractTerms(terms);
/*     */   }
/*     */ 
/*     */   public Weight createWeight(Searcher searcher) throws IOException
/*     */   {
/* 106 */     return this.maskedQuery.createWeight(searcher);
/*     */   }
/*     */ 
/*     */   public Similarity getSimilarity(Searcher searcher)
/*     */   {
/* 111 */     return this.maskedQuery.getSimilarity(searcher);
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader) throws IOException
/*     */   {
/* 116 */     FieldMaskingSpanQuery clone = null;
/*     */ 
/* 118 */     SpanQuery rewritten = (SpanQuery)this.maskedQuery.rewrite(reader);
/* 119 */     if (rewritten != this.maskedQuery) {
/* 120 */       clone = (FieldMaskingSpanQuery)clone();
/* 121 */       clone.maskedQuery = rewritten;
/*     */     }
/*     */ 
/* 124 */     if (clone != null) {
/* 125 */       return clone;
/*     */     }
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString(String field)
/*     */   {
/* 133 */     StringBuilder buffer = new StringBuilder();
/* 134 */     buffer.append("mask(");
/* 135 */     buffer.append(this.maskedQuery.toString(field));
/* 136 */     buffer.append(")");
/* 137 */     buffer.append(ToStringUtils.boost(getBoost()));
/* 138 */     buffer.append(" as ");
/* 139 */     buffer.append(this.field);
/* 140 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 145 */     if (!(o instanceof FieldMaskingSpanQuery))
/* 146 */       return false;
/* 147 */     FieldMaskingSpanQuery other = (FieldMaskingSpanQuery)o;
/* 148 */     return (getField().equals(other.getField())) && (getBoost() == other.getBoost()) && (getMaskedQuery().equals(other.getMaskedQuery()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 156 */     return getMaskedQuery().hashCode() ^ getField().hashCode() ^ Float.floatToRawIntBits(getBoost());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.spans.FieldMaskingSpanQuery
 * JD-Core Version:    0.6.2
 */