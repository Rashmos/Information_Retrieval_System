/*     */ package org.apache.lucene.search.spans;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.search.Query;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class SpanNearQuery extends SpanQuery
/*     */   implements Cloneable
/*     */ {
/*     */   protected List<SpanQuery> clauses;
/*     */   protected int slop;
/*     */   protected boolean inOrder;
/*     */   protected String field;
/*     */   private boolean collectPayloads;
/*     */ 
/*     */   public SpanNearQuery(SpanQuery[] clauses, int slop, boolean inOrder)
/*     */   {
/*  50 */     this(clauses, slop, inOrder, true);
/*     */   }
/*     */ 
/*     */   public SpanNearQuery(SpanQuery[] clauses, int slop, boolean inOrder, boolean collectPayloads)
/*     */   {
/*  56 */     this.clauses = new ArrayList(clauses.length);
/*  57 */     for (int i = 0; i < clauses.length; i++) {
/*  58 */       SpanQuery clause = clauses[i];
/*  59 */       if (i == 0)
/*  60 */         this.field = clause.getField();
/*  61 */       else if (!clause.getField().equals(this.field)) {
/*  62 */         throw new IllegalArgumentException("Clauses must have same field.");
/*     */       }
/*  64 */       this.clauses.add(clause);
/*     */     }
/*  66 */     this.collectPayloads = collectPayloads;
/*  67 */     this.slop = slop;
/*  68 */     this.inOrder = inOrder;
/*     */   }
/*     */ 
/*     */   public SpanQuery[] getClauses()
/*     */   {
/*  73 */     return (SpanQuery[])this.clauses.toArray(new SpanQuery[this.clauses.size()]);
/*     */   }
/*     */ 
/*     */   public int getSlop() {
/*  77 */     return this.slop;
/*     */   }
/*     */   public boolean isInOrder() {
/*  80 */     return this.inOrder;
/*     */   }
/*     */   public String getField() {
/*  83 */     return this.field;
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms) {
/*  87 */     for (SpanQuery clause : this.clauses)
/*  88 */       clause.extractTerms(terms);
/*     */   }
/*     */ 
/*     */   public String toString(String field)
/*     */   {
/*  95 */     StringBuilder buffer = new StringBuilder();
/*  96 */     buffer.append("spanNear([");
/*  97 */     Iterator i = this.clauses.iterator();
/*  98 */     while (i.hasNext()) {
/*  99 */       SpanQuery clause = (SpanQuery)i.next();
/* 100 */       buffer.append(clause.toString(field));
/* 101 */       if (i.hasNext()) {
/* 102 */         buffer.append(", ");
/*     */       }
/*     */     }
/* 105 */     buffer.append("], ");
/* 106 */     buffer.append(this.slop);
/* 107 */     buffer.append(", ");
/* 108 */     buffer.append(this.inOrder);
/* 109 */     buffer.append(")");
/* 110 */     buffer.append(ToStringUtils.boost(getBoost()));
/* 111 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public Spans getSpans(IndexReader reader) throws IOException
/*     */   {
/* 116 */     if (this.clauses.size() == 0) {
/* 117 */       return new SpanOrQuery(getClauses()).getSpans(reader);
/*     */     }
/* 119 */     if (this.clauses.size() == 1) {
/* 120 */       return ((SpanQuery)this.clauses.get(0)).getSpans(reader);
/*     */     }
/* 122 */     return this.inOrder ? new NearSpansOrdered(this, reader, this.collectPayloads) : new NearSpansUnordered(this, reader);
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader)
/*     */     throws IOException
/*     */   {
/* 129 */     SpanNearQuery clone = null;
/* 130 */     for (int i = 0; i < this.clauses.size(); i++) {
/* 131 */       SpanQuery c = (SpanQuery)this.clauses.get(i);
/* 132 */       SpanQuery query = (SpanQuery)c.rewrite(reader);
/* 133 */       if (query != c) {
/* 134 */         if (clone == null)
/* 135 */           clone = (SpanNearQuery)clone();
/* 136 */         clone.clauses.set(i, query);
/*     */       }
/*     */     }
/* 139 */     if (clone != null) {
/* 140 */       return clone;
/*     */     }
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 148 */     int sz = this.clauses.size();
/* 149 */     SpanQuery[] newClauses = new SpanQuery[sz];
/*     */ 
/* 151 */     for (int i = 0; i < sz; i++) {
/* 152 */       newClauses[i] = ((SpanQuery)((SpanQuery)this.clauses.get(i)).clone());
/*     */     }
/* 154 */     SpanNearQuery spanNearQuery = new SpanNearQuery(newClauses, this.slop, this.inOrder);
/* 155 */     spanNearQuery.setBoost(getBoost());
/* 156 */     return spanNearQuery;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 162 */     if (this == o) return true;
/* 163 */     if (!(o instanceof SpanNearQuery)) return false;
/*     */ 
/* 165 */     SpanNearQuery spanNearQuery = (SpanNearQuery)o;
/*     */ 
/* 167 */     if (this.inOrder != spanNearQuery.inOrder) return false;
/* 168 */     if (this.slop != spanNearQuery.slop) return false;
/* 169 */     if (!this.clauses.equals(spanNearQuery.clauses)) return false;
/*     */ 
/* 171 */     return getBoost() == spanNearQuery.getBoost();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 177 */     int result = this.clauses.hashCode();
/*     */ 
/* 181 */     result ^= (result << 14 | result >>> 19);
/* 182 */     result += Float.floatToRawIntBits(getBoost());
/* 183 */     result += this.slop;
/* 184 */     result ^= (this.inOrder ? -1716530243 : 0);
/* 185 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.spans.SpanNearQuery
 * JD-Core Version:    0.6.2
 */