/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.spans.SpanQuery;
/*     */ import org.apache.lucene.search.spans.Spans;
/*     */ import org.apache.lucene.util.OpenBitSet;
/*     */ 
/*     */ public class SpanQueryFilter extends SpanFilter
/*     */ {
/*     */   protected SpanQuery query;
/*     */ 
/*     */   protected SpanQueryFilter()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SpanQueryFilter(SpanQuery query)
/*     */   {
/*  54 */     this.query = query;
/*     */   }
/*     */ 
/*     */   public DocIdSet getDocIdSet(IndexReader reader) throws IOException
/*     */   {
/*  59 */     SpanFilterResult result = bitSpans(reader);
/*  60 */     return result.getDocIdSet();
/*     */   }
/*     */ 
/*     */   public SpanFilterResult bitSpans(IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  66 */     OpenBitSet bits = new OpenBitSet(reader.maxDoc());
/*  67 */     Spans spans = this.query.getSpans(reader);
/*  68 */     List tmp = new ArrayList(20);
/*  69 */     int currentDoc = -1;
/*  70 */     SpanFilterResult.PositionInfo currentInfo = null;
/*  71 */     while (spans.next())
/*     */     {
/*  73 */       int doc = spans.doc();
/*  74 */       bits.set(doc);
/*  75 */       if (currentDoc != doc)
/*     */       {
/*  77 */         currentInfo = new SpanFilterResult.PositionInfo(doc);
/*  78 */         tmp.add(currentInfo);
/*  79 */         currentDoc = doc;
/*     */       }
/*  81 */       currentInfo.addPosition(spans.start(), spans.end());
/*     */     }
/*  83 */     return new SpanFilterResult(bits, tmp);
/*     */   }
/*     */ 
/*     */   public SpanQuery getQuery()
/*     */   {
/*  88 */     return this.query;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  93 */     return "SpanQueryFilter(" + this.query + ")";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  98 */     return ((o instanceof SpanQueryFilter)) && (this.query.equals(((SpanQueryFilter)o).query));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 103 */     return this.query.hashCode() ^ 0x923F64B9;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.SpanQueryFilter
 * JD-Core Version:    0.6.2
 */