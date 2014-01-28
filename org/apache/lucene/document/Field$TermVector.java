/*     */ package org.apache.lucene.document;
/*     */ 
/*     */ public enum Field$TermVector
/*     */ {
/* 174 */   NO, 
/*     */ 
/* 185 */   YES, 
/*     */ 
/* 199 */   WITH_POSITIONS, 
/*     */ 
/* 213 */   WITH_OFFSETS, 
/*     */ 
/* 229 */   WITH_POSITIONS_OFFSETS;
/*     */ 
/*     */   public static TermVector toTermVector(boolean stored, boolean withOffsets, boolean withPositions)
/*     */   {
/* 242 */     if (!stored) {
/* 243 */       return NO;
/*     */     }
/*     */ 
/* 246 */     if (withOffsets) {
/* 247 */       if (withPositions) {
/* 248 */         return WITH_POSITIONS_OFFSETS;
/*     */       }
/* 250 */       return WITH_OFFSETS;
/*     */     }
/*     */ 
/* 253 */     if (withPositions) {
/* 254 */       return WITH_POSITIONS;
/*     */     }
/* 256 */     return YES;
/*     */   }
/*     */ 
/*     */   public abstract boolean isStored();
/*     */ 
/*     */   public abstract boolean withPositions();
/*     */ 
/*     */   public abstract boolean withOffsets();
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.Field.TermVector
 * JD-Core Version:    0.6.2
 */