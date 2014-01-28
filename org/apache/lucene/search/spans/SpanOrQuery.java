/*     */ package org.apache.lucene.search.spans;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.search.Query;
/*     */ import org.apache.lucene.util.PriorityQueue;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class SpanOrQuery extends SpanQuery
/*     */   implements Cloneable
/*     */ {
/*     */   private List<SpanQuery> clauses;
/*     */   private String field;
/*     */ 
/*     */   public SpanOrQuery(SpanQuery[] clauses)
/*     */   {
/*  43 */     this.clauses = new ArrayList(clauses.length);
/*  44 */     for (int i = 0; i < clauses.length; i++) {
/*  45 */       SpanQuery clause = clauses[i];
/*  46 */       if (i == 0)
/*  47 */         this.field = clause.getField();
/*  48 */       else if (!clause.getField().equals(this.field)) {
/*  49 */         throw new IllegalArgumentException("Clauses must have same field.");
/*     */       }
/*  51 */       this.clauses.add(clause);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SpanQuery[] getClauses()
/*     */   {
/*  57 */     return (SpanQuery[])this.clauses.toArray(new SpanQuery[this.clauses.size()]);
/*     */   }
/*     */ 
/*     */   public String getField() {
/*  61 */     return this.field;
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms) {
/*  65 */     for (SpanQuery clause : this.clauses)
/*  66 */       clause.extractTerms(terms);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  72 */     int sz = this.clauses.size();
/*  73 */     SpanQuery[] newClauses = new SpanQuery[sz];
/*     */ 
/*  75 */     for (int i = 0; i < sz; i++) {
/*  76 */       newClauses[i] = ((SpanQuery)((SpanQuery)this.clauses.get(i)).clone());
/*     */     }
/*  78 */     SpanOrQuery soq = new SpanOrQuery(newClauses);
/*  79 */     soq.setBoost(getBoost());
/*  80 */     return soq;
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader) throws IOException
/*     */   {
/*  85 */     SpanOrQuery clone = null;
/*  86 */     for (int i = 0; i < this.clauses.size(); i++) {
/*  87 */       SpanQuery c = (SpanQuery)this.clauses.get(i);
/*  88 */       SpanQuery query = (SpanQuery)c.rewrite(reader);
/*  89 */       if (query != c) {
/*  90 */         if (clone == null)
/*  91 */           clone = (SpanOrQuery)clone();
/*  92 */         clone.clauses.set(i, query);
/*     */       }
/*     */     }
/*  95 */     if (clone != null) {
/*  96 */       return clone;
/*     */     }
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString(String field)
/*     */   {
/* 104 */     StringBuilder buffer = new StringBuilder();
/* 105 */     buffer.append("spanOr([");
/* 106 */     Iterator i = this.clauses.iterator();
/* 107 */     while (i.hasNext()) {
/* 108 */       SpanQuery clause = (SpanQuery)i.next();
/* 109 */       buffer.append(clause.toString(field));
/* 110 */       if (i.hasNext()) {
/* 111 */         buffer.append(", ");
/*     */       }
/*     */     }
/* 114 */     buffer.append("])");
/* 115 */     buffer.append(ToStringUtils.boost(getBoost()));
/* 116 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 121 */     if (this == o) return true;
/* 122 */     if ((o == null) || (getClass() != o.getClass())) return false;
/*     */ 
/* 124 */     SpanOrQuery that = (SpanOrQuery)o;
/*     */ 
/* 126 */     if (!this.clauses.equals(that.clauses)) return false;
/* 127 */     if ((!this.clauses.isEmpty()) && (!this.field.equals(that.field))) return false;
/*     */ 
/* 129 */     return getBoost() == that.getBoost();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 134 */     int h = this.clauses.hashCode();
/* 135 */     h ^= (h << 10 | h >>> 23);
/* 136 */     h ^= Float.floatToRawIntBits(getBoost());
/* 137 */     return h;
/*     */   }
/*     */ 
/*     */   public Spans getSpans(final IndexReader reader)
/*     */     throws IOException
/*     */   {
/* 162 */     if (this.clauses.size() == 1) {
/* 163 */       return ((SpanQuery)this.clauses.get(0)).getSpans(reader);
/*     */     }
/* 165 */     return new Spans() {
/* 166 */       private SpanOrQuery.SpanQueue queue = null;
/*     */ 
/*     */       private boolean initSpanQueue(int target) throws IOException {
/* 169 */         this.queue = new SpanOrQuery.SpanQueue(SpanOrQuery.this, SpanOrQuery.this.clauses.size());
/* 170 */         Iterator i = SpanOrQuery.this.clauses.iterator();
/* 171 */         while (i.hasNext()) {
/* 172 */           Spans spans = ((SpanQuery)i.next()).getSpans(reader);
/* 173 */           if (((target == -1) && (spans.next())) || ((target != -1) && (spans.skipTo(target))))
/*     */           {
/* 175 */             this.queue.add(spans);
/*     */           }
/*     */         }
/* 178 */         return this.queue.size() != 0;
/*     */       }
/*     */ 
/*     */       public boolean next() throws IOException
/*     */       {
/* 183 */         if (this.queue == null) {
/* 184 */           return initSpanQueue(-1);
/*     */         }
/*     */ 
/* 187 */         if (this.queue.size() == 0) {
/* 188 */           return false;
/*     */         }
/*     */ 
/* 191 */         if (top().next()) {
/* 192 */           this.queue.updateTop();
/* 193 */           return true;
/*     */         }
/*     */ 
/* 196 */         this.queue.pop();
/* 197 */         return this.queue.size() != 0;
/*     */       }
/*     */       private Spans top() {
/* 200 */         return (Spans)this.queue.top();
/*     */       }
/*     */ 
/*     */       public boolean skipTo(int target) throws IOException {
/* 204 */         if (this.queue == null) {
/* 205 */           return initSpanQueue(target);
/*     */         }
/*     */ 
/* 208 */         boolean skipCalled = false;
/* 209 */         while ((this.queue.size() != 0) && (top().doc() < target)) {
/* 210 */           if (top().skipTo(target))
/* 211 */             this.queue.updateTop();
/*     */           else {
/* 213 */             this.queue.pop();
/*     */           }
/* 215 */           skipCalled = true;
/*     */         }
/*     */ 
/* 218 */         if (skipCalled) {
/* 219 */           return this.queue.size() != 0;
/*     */         }
/* 221 */         return next();
/*     */       }
/*     */ 
/*     */       public int doc() {
/* 225 */         return top().doc();
/*     */       }
/* 227 */       public int start() { return top().start(); } 
/*     */       public int end() {
/* 229 */         return top().end();
/*     */       }
/*     */ 
/*     */       public Collection<byte[]> getPayload() throws IOException {
/* 233 */         ArrayList result = null;
/* 234 */         Spans theTop = top();
/* 235 */         if ((theTop != null) && (theTop.isPayloadAvailable())) {
/* 236 */           result = new ArrayList(theTop.getPayload());
/*     */         }
/* 238 */         return result;
/*     */       }
/*     */ 
/*     */       public boolean isPayloadAvailable()
/*     */       {
/* 243 */         Spans top = top();
/* 244 */         return (top != null) && (top.isPayloadAvailable());
/*     */       }
/*     */ 
/*     */       public String toString()
/*     */       {
/* 249 */         return "spans(" + SpanOrQuery.this + ")@" + (this.queue.size() > 0 ? doc() + ":" + start() + "-" + end() : this.queue == null ? "START" : "END");
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private class SpanQueue extends PriorityQueue<Spans>
/*     */   {
/*     */     public SpanQueue(int size)
/*     */     {
/* 143 */       initialize(size);
/*     */     }
/*     */ 
/*     */     protected final boolean lessThan(Spans spans1, Spans spans2)
/*     */     {
/* 148 */       if (spans1.doc() == spans2.doc()) {
/* 149 */         if (spans1.start() == spans2.start()) {
/* 150 */           return spans1.end() < spans2.end();
/*     */         }
/* 152 */         return spans1.start() < spans2.start();
/*     */       }
/*     */ 
/* 155 */       return spans1.doc() < spans2.doc();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.spans.SpanOrQuery
 * JD-Core Version:    0.6.2
 */