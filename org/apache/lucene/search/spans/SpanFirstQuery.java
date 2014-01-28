/*     */ package org.apache.lucene.search.spans;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.search.Query;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class SpanFirstQuery extends SpanQuery
/*     */   implements Cloneable
/*     */ {
/*     */   private SpanQuery match;
/*     */   private int end;
/*     */ 
/*     */   public SpanFirstQuery(SpanQuery match, int end)
/*     */   {
/*  39 */     this.match = match;
/*  40 */     this.end = end;
/*     */   }
/*     */ 
/*     */   public SpanQuery getMatch() {
/*  44 */     return this.match;
/*     */   }
/*     */   public int getEnd() {
/*  47 */     return this.end;
/*     */   }
/*     */   public String getField() {
/*  50 */     return this.match.getField();
/*     */   }
/*     */ 
/*     */   public String toString(String field) {
/*  54 */     StringBuilder buffer = new StringBuilder();
/*  55 */     buffer.append("spanFirst(");
/*  56 */     buffer.append(this.match.toString(field));
/*  57 */     buffer.append(", ");
/*  58 */     buffer.append(this.end);
/*  59 */     buffer.append(")");
/*  60 */     buffer.append(ToStringUtils.boost(getBoost()));
/*  61 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  66 */     SpanFirstQuery spanFirstQuery = new SpanFirstQuery((SpanQuery)this.match.clone(), this.end);
/*  67 */     spanFirstQuery.setBoost(getBoost());
/*  68 */     return spanFirstQuery;
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms)
/*     */   {
/*  73 */     this.match.extractTerms(terms);
/*     */   }
/*     */ 
/*     */   public Spans getSpans(final IndexReader reader) throws IOException
/*     */   {
/*  78 */     return new Spans() {
/*  79 */       private Spans spans = SpanFirstQuery.this.match.getSpans(reader);
/*     */ 
/*     */       public boolean next() throws IOException
/*     */       {
/*  83 */         while (this.spans.next()) {
/*  84 */           if (end() <= SpanFirstQuery.this.end)
/*  85 */             return true;
/*     */         }
/*  87 */         return false;
/*     */       }
/*     */ 
/*     */       public boolean skipTo(int target) throws IOException
/*     */       {
/*  92 */         if (!this.spans.skipTo(target)) {
/*  93 */           return false;
/*     */         }
/*  95 */         return (this.spans.end() <= SpanFirstQuery.this.end) || (next());
/*     */       }
/*     */ 
/*     */       public int doc()
/*     */       {
/* 100 */         return this.spans.doc();
/*     */       }
/* 102 */       public int start() { return this.spans.start(); } 
/*     */       public int end() {
/* 104 */         return this.spans.end();
/*     */       }
/*     */ 
/*     */       public Collection<byte[]> getPayload() throws IOException
/*     */       {
/* 109 */         ArrayList result = null;
/* 110 */         if (this.spans.isPayloadAvailable()) {
/* 111 */           result = new ArrayList(this.spans.getPayload());
/*     */         }
/* 113 */         return result;
/*     */       }
/*     */ 
/*     */       public boolean isPayloadAvailable()
/*     */       {
/* 119 */         return this.spans.isPayloadAvailable();
/*     */       }
/*     */ 
/*     */       public String toString()
/*     */       {
/* 124 */         return "spans(" + SpanFirstQuery.this.toString() + ")";
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader)
/*     */     throws IOException
/*     */   {
/* 132 */     SpanFirstQuery clone = null;
/*     */ 
/* 134 */     SpanQuery rewritten = (SpanQuery)this.match.rewrite(reader);
/* 135 */     if (rewritten != this.match) {
/* 136 */       clone = (SpanFirstQuery)clone();
/* 137 */       clone.match = rewritten;
/*     */     }
/*     */ 
/* 140 */     if (clone != null) {
/* 141 */       return clone;
/*     */     }
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 149 */     if (this == o) return true;
/* 150 */     if (!(o instanceof SpanFirstQuery)) return false;
/*     */ 
/* 152 */     SpanFirstQuery other = (SpanFirstQuery)o;
/* 153 */     return (this.end == other.end) && (this.match.equals(other.match)) && (getBoost() == other.getBoost());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 160 */     int h = this.match.hashCode();
/* 161 */     h ^= (h << 8 | h >>> 25);
/* 162 */     h ^= Float.floatToRawIntBits(getBoost()) ^ this.end;
/* 163 */     return h;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.spans.SpanFirstQuery
 * JD-Core Version:    0.6.2
 */