/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ 
/*     */ public final class FieldInvertState
/*     */ {
/*     */   int position;
/*     */   int length;
/*     */   int numOverlap;
/*     */   int offset;
/*     */   float boost;
/*     */   AttributeSource attributeSource;
/*     */ 
/*     */   public FieldInvertState()
/*     */   {
/*     */   }
/*     */ 
/*     */   public FieldInvertState(int position, int length, int numOverlap, int offset, float boost)
/*     */   {
/*  41 */     this.position = position;
/*  42 */     this.length = length;
/*  43 */     this.numOverlap = numOverlap;
/*  44 */     this.offset = offset;
/*  45 */     this.boost = boost;
/*     */   }
/*     */ 
/*     */   void reset(float docBoost)
/*     */   {
/*  53 */     this.position = 0;
/*  54 */     this.length = 0;
/*  55 */     this.numOverlap = 0;
/*  56 */     this.offset = 0;
/*  57 */     this.boost = docBoost;
/*  58 */     this.attributeSource = null;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/*  66 */     return this.position;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  74 */     return this.length;
/*     */   }
/*     */ 
/*     */   public int getNumOverlap()
/*     */   {
/*  82 */     return this.numOverlap;
/*     */   }
/*     */ 
/*     */   public int getOffset()
/*     */   {
/*  90 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public float getBoost()
/*     */   {
/* 100 */     return this.boost;
/*     */   }
/*     */ 
/*     */   public AttributeSource getAttributeSource() {
/* 104 */     return this.attributeSource;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FieldInvertState
 * JD-Core Version:    0.6.2
 */