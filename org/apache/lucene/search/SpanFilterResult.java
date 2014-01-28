/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SpanFilterResult
/*     */ {
/*     */   private DocIdSet docIdSet;
/*     */   private List<PositionInfo> positions;
/*     */ 
/*     */   public SpanFilterResult(DocIdSet docIdSet, List<PositionInfo> positions)
/*     */   {
/*  40 */     this.docIdSet = docIdSet;
/*  41 */     this.positions = positions;
/*     */   }
/*     */ 
/*     */   public List<PositionInfo> getPositions()
/*     */   {
/*  50 */     return this.positions;
/*     */   }
/*     */ 
/*     */   public DocIdSet getDocIdSet()
/*     */   {
/*  55 */     return this.docIdSet;
/*     */   }
/*     */ 
/*     */   public static class StartEnd
/*     */   {
/*     */     private int start;
/*     */     private int end;
/*     */ 
/*     */     public StartEnd(int start, int end)
/*     */     {
/*  93 */       this.start = start;
/*  94 */       this.end = end;
/*     */     }
/*     */ 
/*     */     public int getEnd()
/*     */     {
/* 102 */       return this.end;
/*     */     }
/*     */ 
/*     */     public int getStart()
/*     */     {
/* 110 */       return this.start;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class PositionInfo
/*     */   {
/*     */     private int doc;
/*     */     private List<SpanFilterResult.StartEnd> positions;
/*     */ 
/*     */     public PositionInfo(int doc)
/*     */     {
/*  64 */       this.doc = doc;
/*  65 */       this.positions = new ArrayList();
/*     */     }
/*     */ 
/*     */     public void addPosition(int start, int end)
/*     */     {
/*  70 */       this.positions.add(new SpanFilterResult.StartEnd(start, end));
/*     */     }
/*     */ 
/*     */     public int getDoc() {
/*  74 */       return this.doc;
/*     */     }
/*     */ 
/*     */     public List<SpanFilterResult.StartEnd> getPositions()
/*     */     {
/*  82 */       return this.positions;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.SpanFilterResult
 * JD-Core Version:    0.6.2
 */