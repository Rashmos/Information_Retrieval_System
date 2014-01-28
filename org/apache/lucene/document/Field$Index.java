/*     */ package org.apache.lucene.document;
/*     */ 
/*     */ public enum Field$Index
/*     */ {
/*  65 */   NO, 
/*     */ 
/*  77 */   ANALYZED, 
/*     */ 
/*  90 */   NOT_ANALYZED, 
/*     */ 
/* 113 */   NOT_ANALYZED_NO_NORMS, 
/*     */ 
/* 127 */   ANALYZED_NO_NORMS;
/*     */ 
/*     */   public static Index toIndex(boolean indexed, boolean analyzed)
/*     */   {
/* 138 */     return toIndex(indexed, analyzed, false);
/*     */   }
/*     */ 
/*     */   public static Index toIndex(boolean indexed, boolean analyzed, boolean omitNorms)
/*     */   {
/* 145 */     if (!indexed) {
/* 146 */       return NO;
/*     */     }
/*     */ 
/* 150 */     if (!omitNorms) {
/* 151 */       if (analyzed) {
/* 152 */         return ANALYZED;
/*     */       }
/* 154 */       return NOT_ANALYZED;
/*     */     }
/*     */ 
/* 158 */     if (analyzed) {
/* 159 */       return ANALYZED_NO_NORMS;
/*     */     }
/* 161 */     return NOT_ANALYZED_NO_NORMS;
/*     */   }
/*     */ 
/*     */   public abstract boolean isIndexed();
/*     */ 
/*     */   public abstract boolean isAnalyzed();
/*     */ 
/*     */   public abstract boolean omitNorms();
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.Field.Index
 * JD-Core Version:    0.6.2
 */