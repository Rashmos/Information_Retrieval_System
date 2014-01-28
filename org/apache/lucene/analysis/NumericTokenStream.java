/*     */ package org.apache.lucene.analysis;
/*     */ 
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ import org.apache.lucene.util.NumericUtils;
/*     */ 
/*     */ public final class NumericTokenStream extends TokenStream
/*     */ {
/*     */   public static final String TOKEN_TYPE_FULL_PREC = "fullPrecNumeric";
/*     */   public static final String TOKEN_TYPE_LOWER_PREC = "lowerPrecNumeric";
/* 244 */   private final TermAttribute termAtt = (TermAttribute)addAttribute(TermAttribute.class);
/* 245 */   private final TypeAttribute typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
/* 246 */   private final PositionIncrementAttribute posIncrAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/*     */ 
/* 248 */   private int shift = 0; private int valSize = 0;
/*     */   private final int precisionStep;
/* 251 */   private long value = 0L;
/*     */ 
/*     */   public NumericTokenStream()
/*     */   {
/* 104 */     this(4);
/*     */   }
/*     */ 
/*     */   public NumericTokenStream(int precisionStep)
/*     */   {
/* 114 */     this.precisionStep = precisionStep;
/* 115 */     if (precisionStep < 1)
/* 116 */       throw new IllegalArgumentException("precisionStep must be >=1");
/*     */   }
/*     */ 
/*     */   public NumericTokenStream(AttributeSource source, int precisionStep)
/*     */   {
/* 126 */     super(source);
/* 127 */     this.precisionStep = precisionStep;
/* 128 */     if (precisionStep < 1)
/* 129 */       throw new IllegalArgumentException("precisionStep must be >=1");
/*     */   }
/*     */ 
/*     */   public NumericTokenStream(AttributeSource.AttributeFactory factory, int precisionStep)
/*     */   {
/* 140 */     super(factory);
/* 141 */     this.precisionStep = precisionStep;
/* 142 */     if (precisionStep < 1)
/* 143 */       throw new IllegalArgumentException("precisionStep must be >=1");
/*     */   }
/*     */ 
/*     */   public NumericTokenStream setLongValue(long value)
/*     */   {
/* 153 */     this.value = value;
/* 154 */     this.valSize = 64;
/* 155 */     this.shift = 0;
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */   public NumericTokenStream setIntValue(int value)
/*     */   {
/* 166 */     this.value = value;
/* 167 */     this.valSize = 32;
/* 168 */     this.shift = 0;
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   public NumericTokenStream setDoubleValue(double value)
/*     */   {
/* 179 */     this.value = NumericUtils.doubleToSortableLong(value);
/* 180 */     this.valSize = 64;
/* 181 */     this.shift = 0;
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   public NumericTokenStream setFloatValue(float value)
/*     */   {
/* 192 */     this.value = NumericUtils.floatToSortableInt(value);
/* 193 */     this.valSize = 32;
/* 194 */     this.shift = 0;
/* 195 */     return this;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 200 */     if (this.valSize == 0)
/* 201 */       throw new IllegalStateException("call set???Value() before usage");
/* 202 */     this.shift = 0;
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */   {
/* 207 */     if (this.valSize == 0)
/* 208 */       throw new IllegalStateException("call set???Value() before usage");
/* 209 */     if (this.shift >= this.valSize) {
/* 210 */       return false;
/*     */     }
/* 212 */     clearAttributes();
/*     */     char[] buffer;
/* 214 */     switch (this.valSize) {
/*     */     case 64:
/* 216 */       buffer = this.termAtt.resizeTermBuffer(11);
/* 217 */       this.termAtt.setTermLength(NumericUtils.longToPrefixCoded(this.value, this.shift, buffer));
/* 218 */       break;
/*     */     case 32:
/* 221 */       buffer = this.termAtt.resizeTermBuffer(6);
/* 222 */       this.termAtt.setTermLength(NumericUtils.intToPrefixCoded((int)this.value, this.shift, buffer));
/* 223 */       break;
/*     */     default:
/* 227 */       throw new IllegalArgumentException("valSize must be 32 or 64");
/*     */     }
/*     */ 
/* 230 */     this.typeAtt.setType(this.shift == 0 ? "fullPrecNumeric" : "lowerPrecNumeric");
/* 231 */     this.posIncrAtt.setPositionIncrement(this.shift == 0 ? 1 : 0);
/* 232 */     this.shift += this.precisionStep;
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 238 */     StringBuilder sb = new StringBuilder("(numeric,valSize=").append(this.valSize);
/* 239 */     sb.append(",precisionStep=").append(this.precisionStep).append(')');
/* 240 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.NumericTokenStream
 * JD-Core Version:    0.6.2
 */