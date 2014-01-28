/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import org.apache.lucene.index.Term;
/*     */ 
/*     */ final class FuzzyQuery$ScoreTerm
/*     */   implements Comparable<ScoreTerm>
/*     */ {
/*     */   public Term term;
/*     */   public float score;
/*     */ 
/*     */   public int compareTo(ScoreTerm other)
/*     */   {
/* 173 */     if (this.score == other.score) {
/* 174 */       return other.term.compareTo(this.term);
/*     */     }
/* 176 */     return Float.compare(this.score, other.score);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FuzzyQuery.ScoreTerm
 * JD-Core Version:    0.6.2
 */