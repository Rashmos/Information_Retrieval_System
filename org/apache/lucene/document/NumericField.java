/*     */ package org.apache.lucene.document;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.NumericTokenStream;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ 
/*     */ public final class NumericField extends AbstractField
/*     */ {
/*     */   private final NumericTokenStream tokenStream;
/*     */ 
/*     */   public NumericField(String name)
/*     */   {
/* 156 */     this(name, 4, Field.Store.NO, true);
/*     */   }
/*     */ 
/*     */   public NumericField(String name, Field.Store store, boolean index)
/*     */   {
/* 170 */     this(name, 4, store, index);
/*     */   }
/*     */ 
/*     */   public NumericField(String name, int precisionStep)
/*     */   {
/* 183 */     this(name, precisionStep, Field.Store.NO, true);
/*     */   }
/*     */ 
/*     */   public NumericField(String name, int precisionStep, Field.Store store, boolean index)
/*     */   {
/* 198 */     super(name, store, index ? Field.Index.ANALYZED_NO_NORMS : Field.Index.NO, Field.TermVector.NO);
/* 199 */     setOmitTermFreqAndPositions(true);
/* 200 */     this.tokenStream = new NumericTokenStream(precisionStep);
/*     */   }
/*     */ 
/*     */   public TokenStream tokenStreamValue()
/*     */   {
/* 205 */     return isIndexed() ? this.tokenStream : null;
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue(byte[] result)
/*     */   {
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */   public Reader readerValue()
/*     */   {
/* 216 */     return null;
/*     */   }
/*     */ 
/*     */   public String stringValue()
/*     */   {
/* 221 */     return this.fieldsData == null ? null : this.fieldsData.toString();
/*     */   }
/*     */ 
/*     */   public Number getNumericValue()
/*     */   {
/* 226 */     return (Number)this.fieldsData;
/*     */   }
/*     */ 
/*     */   public NumericField setLongValue(long value)
/*     */   {
/* 236 */     this.tokenStream.setLongValue(value);
/* 237 */     this.fieldsData = Long.valueOf(value);
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */   public NumericField setIntValue(int value)
/*     */   {
/* 248 */     this.tokenStream.setIntValue(value);
/* 249 */     this.fieldsData = Integer.valueOf(value);
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */   public NumericField setDoubleValue(double value)
/*     */   {
/* 260 */     this.tokenStream.setDoubleValue(value);
/* 261 */     this.fieldsData = Double.valueOf(value);
/* 262 */     return this;
/*     */   }
/*     */ 
/*     */   public NumericField setFloatValue(float value)
/*     */   {
/* 272 */     this.tokenStream.setFloatValue(value);
/* 273 */     this.fieldsData = Float.valueOf(value);
/* 274 */     return this;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.NumericField
 * JD-Core Version:    0.6.2
 */