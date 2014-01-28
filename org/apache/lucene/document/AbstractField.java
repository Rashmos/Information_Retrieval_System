/*     */ package org.apache.lucene.document;
/*     */ 
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.util.StringHelper;
/*     */ 
/*     */ public abstract class AbstractField
/*     */   implements Fieldable
/*     */ {
/*  30 */   protected String name = "body";
/*  31 */   protected boolean storeTermVector = false;
/*  32 */   protected boolean storeOffsetWithTermVector = false;
/*  33 */   protected boolean storePositionWithTermVector = false;
/*  34 */   protected boolean omitNorms = false;
/*  35 */   protected boolean isStored = false;
/*  36 */   protected boolean isIndexed = true;
/*  37 */   protected boolean isTokenized = true;
/*  38 */   protected boolean isBinary = false;
/*  39 */   protected boolean lazy = false;
/*  40 */   protected boolean omitTermFreqAndPositions = false;
/*  41 */   protected float boost = 1.0F;
/*     */ 
/*  43 */   protected Object fieldsData = null;
/*     */   protected TokenStream tokenStream;
/*     */   protected int binaryLength;
/*     */   protected int binaryOffset;
/*     */ 
/*     */   protected AbstractField()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected AbstractField(String name, Field.Store store, Field.Index index, Field.TermVector termVector)
/*     */   {
/*  55 */     if (name == null)
/*  56 */       throw new NullPointerException("name cannot be null");
/*  57 */     this.name = StringHelper.intern(name);
/*     */ 
/*  59 */     this.isStored = store.isStored();
/*  60 */     this.isIndexed = index.isIndexed();
/*  61 */     this.isTokenized = index.isAnalyzed();
/*  62 */     this.omitNorms = index.omitNorms();
/*     */ 
/*  64 */     this.isBinary = false;
/*     */ 
/*  66 */     setStoreTermVector(termVector);
/*     */   }
/*     */ 
/*     */   public void setBoost(float boost)
/*     */   {
/*  92 */     this.boost = boost;
/*     */   }
/*     */ 
/*     */   public float getBoost()
/*     */   {
/* 107 */     return this.boost;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 113 */     return this.name;
/*     */   }
/*     */   protected void setStoreTermVector(Field.TermVector termVector) {
/* 116 */     this.storeTermVector = termVector.isStored();
/* 117 */     this.storePositionWithTermVector = termVector.withPositions();
/* 118 */     this.storeOffsetWithTermVector = termVector.withOffsets();
/*     */   }
/*     */ 
/*     */   public final boolean isStored()
/*     */   {
/* 124 */     return this.isStored;
/*     */   }
/*     */ 
/*     */   public final boolean isIndexed() {
/* 128 */     return this.isIndexed;
/*     */   }
/*     */ 
/*     */   public final boolean isTokenized()
/*     */   {
/* 133 */     return this.isTokenized;
/*     */   }
/*     */ 
/*     */   public final boolean isTermVectorStored()
/*     */   {
/* 143 */     return this.storeTermVector;
/*     */   }
/*     */ 
/*     */   public boolean isStoreOffsetWithTermVector()
/*     */   {
/* 150 */     return this.storeOffsetWithTermVector;
/*     */   }
/*     */ 
/*     */   public boolean isStorePositionWithTermVector()
/*     */   {
/* 157 */     return this.storePositionWithTermVector;
/*     */   }
/*     */ 
/*     */   public final boolean isBinary()
/*     */   {
/* 162 */     return this.isBinary;
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue()
/*     */   {
/* 174 */     return getBinaryValue(null);
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue(byte[] result) {
/* 178 */     if ((this.isBinary) || ((this.fieldsData instanceof byte[]))) {
/* 179 */       return (byte[])this.fieldsData;
/*     */     }
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   public int getBinaryLength()
/*     */   {
/* 190 */     if (this.isBinary)
/* 191 */       return this.binaryLength;
/* 192 */     if ((this.fieldsData instanceof byte[])) {
/* 193 */       return ((byte[])this.fieldsData).length;
/*     */     }
/* 195 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getBinaryOffset()
/*     */   {
/* 204 */     return this.binaryOffset;
/*     */   }
/*     */ 
/*     */   public boolean getOmitNorms() {
/* 208 */     return this.omitNorms;
/*     */   }
/*     */   public boolean getOmitTermFreqAndPositions() {
/* 211 */     return this.omitTermFreqAndPositions;
/*     */   }
/*     */ 
/*     */   public void setOmitNorms(boolean omitNorms)
/*     */   {
/* 218 */     this.omitNorms = omitNorms;
/*     */   }
/*     */ 
/*     */   public void setOmitTermFreqAndPositions(boolean omitTermFreqAndPositions)
/*     */   {
/* 231 */     this.omitTermFreqAndPositions = omitTermFreqAndPositions;
/*     */   }
/*     */   public boolean isLazy() {
/* 234 */     return this.lazy;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 240 */     StringBuilder result = new StringBuilder();
/* 241 */     if (this.isStored) {
/* 242 */       result.append("stored");
/*     */     }
/* 244 */     if (this.isIndexed) {
/* 245 */       if (result.length() > 0)
/* 246 */         result.append(",");
/* 247 */       result.append("indexed");
/*     */     }
/* 249 */     if (this.isTokenized) {
/* 250 */       if (result.length() > 0)
/* 251 */         result.append(",");
/* 252 */       result.append("tokenized");
/*     */     }
/* 254 */     if (this.storeTermVector) {
/* 255 */       if (result.length() > 0)
/* 256 */         result.append(",");
/* 257 */       result.append("termVector");
/*     */     }
/* 259 */     if (this.storeOffsetWithTermVector) {
/* 260 */       if (result.length() > 0)
/* 261 */         result.append(",");
/* 262 */       result.append("termVectorOffsets");
/*     */     }
/* 264 */     if (this.storePositionWithTermVector) {
/* 265 */       if (result.length() > 0)
/* 266 */         result.append(",");
/* 267 */       result.append("termVectorPosition");
/*     */     }
/* 269 */     if (this.isBinary) {
/* 270 */       if (result.length() > 0)
/* 271 */         result.append(",");
/* 272 */       result.append("binary");
/*     */     }
/* 274 */     if (this.omitNorms) {
/* 275 */       result.append(",omitNorms");
/*     */     }
/* 277 */     if (this.omitTermFreqAndPositions) {
/* 278 */       result.append(",omitTermFreqAndPositions");
/*     */     }
/* 280 */     if (this.lazy) {
/* 281 */       result.append(",lazy");
/*     */     }
/* 283 */     result.append('<');
/* 284 */     result.append(this.name);
/* 285 */     result.append(':');
/*     */ 
/* 287 */     if ((this.fieldsData != null) && (!this.lazy)) {
/* 288 */       result.append(this.fieldsData);
/*     */     }
/*     */ 
/* 291 */     result.append('>');
/* 292 */     return result.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.AbstractField
 * JD-Core Version:    0.6.2
 */