/*     */ package org.apache.lucene.analysis.shingle;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ 
/*     */ public final class ShingleFilter extends TokenFilter
/*     */ {
/*  49 */   public static final char[] FILLER_TOKEN = { '_' };
/*     */   public static final int DEFAULT_MAX_SHINGLE_SIZE = 2;
/*     */   public static final int DEFAULT_MIN_SHINGLE_SIZE = 2;
/*     */   public static final String DEFAULT_TOKEN_TYPE = "shingle";
/*     */   public static final String TOKEN_SEPARATOR = " ";
/*  75 */   private LinkedList<InputWindowToken> inputWindow = new LinkedList();
/*     */   private CircularSequence gramSize;
/*  87 */   private StringBuilder gramBuilder = new StringBuilder();
/*     */ 
/*  92 */   private String tokenType = "shingle";
/*     */ 
/*  97 */   private String tokenSeparator = " ";
/*     */ 
/* 103 */   private boolean outputUnigrams = true;
/*     */ 
/* 108 */   private boolean outputUnigramsIfNoShingles = false;
/*     */   private int maxShingleSize;
/*     */   private int minShingleSize;
/*     */   private int numFillerTokensToInsert;
/*     */   private AttributeSource nextInputStreamToken;
/* 137 */   private boolean isNextInputStreamToken = false;
/*     */ 
/* 143 */   private boolean isOutputHere = false;
/*     */ 
/* 148 */   boolean noShingleOutput = true;
/*     */ 
/* 150 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 151 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/* 152 */   private final PositionIncrementAttribute posIncrAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/* 153 */   private final TypeAttribute typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
/*     */   private boolean exhausted;
/*     */ 
/*     */   public ShingleFilter(TokenStream input, int minShingleSize, int maxShingleSize)
/*     */   {
/* 165 */     super(input);
/* 166 */     setMaxShingleSize(maxShingleSize);
/* 167 */     setMinShingleSize(minShingleSize);
/*     */   }
/*     */ 
/*     */   public ShingleFilter(TokenStream input, int maxShingleSize)
/*     */   {
/* 178 */     this(input, 2, maxShingleSize);
/*     */   }
/*     */ 
/*     */   public ShingleFilter(TokenStream input)
/*     */   {
/* 187 */     this(input, 2, 2);
/*     */   }
/*     */ 
/*     */   public ShingleFilter(TokenStream input, String tokenType)
/*     */   {
/* 198 */     this(input, 2, 2);
/* 199 */     setTokenType(tokenType);
/*     */   }
/*     */ 
/*     */   public void setTokenType(String tokenType)
/*     */   {
/* 209 */     this.tokenType = tokenType;
/*     */   }
/*     */ 
/*     */   public void setOutputUnigrams(boolean outputUnigrams)
/*     */   {
/* 220 */     this.outputUnigrams = outputUnigrams;
/* 221 */     this.gramSize = new CircularSequence();
/*     */   }
/*     */ 
/*     */   public void setOutputUnigramsIfNoShingles(boolean outputUnigramsIfNoShingles)
/*     */   {
/* 235 */     this.outputUnigramsIfNoShingles = outputUnigramsIfNoShingles;
/*     */   }
/*     */ 
/*     */   public void setMaxShingleSize(int maxShingleSize)
/*     */   {
/* 244 */     if (maxShingleSize < 2) {
/* 245 */       throw new IllegalArgumentException("Max shingle size must be >= 2");
/*     */     }
/* 247 */     this.maxShingleSize = maxShingleSize;
/*     */   }
/*     */ 
/*     */   public void setMinShingleSize(int minShingleSize)
/*     */   {
/* 260 */     if (minShingleSize < 2) {
/* 261 */       throw new IllegalArgumentException("Min shingle size must be >= 2");
/*     */     }
/* 263 */     if (minShingleSize > this.maxShingleSize) {
/* 264 */       throw new IllegalArgumentException("Min shingle size must be <= max shingle size");
/*     */     }
/*     */ 
/* 267 */     this.minShingleSize = minShingleSize;
/* 268 */     this.gramSize = new CircularSequence();
/*     */   }
/*     */ 
/*     */   public void setTokenSeparator(String tokenSeparator)
/*     */   {
/* 276 */     this.tokenSeparator = (null == tokenSeparator ? "" : tokenSeparator);
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken() throws IOException
/*     */   {
/* 281 */     boolean tokenAvailable = false;
/* 282 */     int builtGramSize = 0;
/* 283 */     if ((this.gramSize.atMinValue()) || (this.inputWindow.size() < this.gramSize.getValue())) {
/* 284 */       shiftInputWindow();
/* 285 */       this.gramBuilder.setLength(0);
/*     */     } else {
/* 287 */       builtGramSize = this.gramSize.getPreviousValue();
/*     */     }
/* 289 */     if (this.inputWindow.size() >= this.gramSize.getValue()) {
/* 290 */       boolean isAllFiller = true;
/* 291 */       InputWindowToken nextToken = null;
/* 292 */       Iterator iter = this.inputWindow.iterator();
/* 293 */       for (int gramNum = 1; 
/* 294 */         (iter.hasNext()) && (builtGramSize < this.gramSize.getValue()); 
/* 295 */         gramNum++) {
/* 296 */         nextToken = (InputWindowToken)iter.next();
/* 297 */         if (builtGramSize < gramNum) {
/* 298 */           if (builtGramSize > 0) {
/* 299 */             this.gramBuilder.append(this.tokenSeparator);
/*     */           }
/* 301 */           this.gramBuilder.append(nextToken.termAtt.buffer(), 0, nextToken.termAtt.length());
/*     */ 
/* 303 */           builtGramSize++;
/*     */         }
/* 305 */         if ((isAllFiller) && (nextToken.isFiller)) {
/* 306 */           if (gramNum == this.gramSize.getValue())
/* 307 */             this.gramSize.advance();
/*     */         }
/*     */         else {
/* 310 */           isAllFiller = false;
/*     */         }
/*     */       }
/* 313 */       if ((!isAllFiller) && (builtGramSize == this.gramSize.getValue())) {
/* 314 */         ((InputWindowToken)this.inputWindow.getFirst()).attSource.copyTo(this);
/* 315 */         this.posIncrAtt.setPositionIncrement(this.isOutputHere ? 0 : 1);
/* 316 */         this.termAtt.setEmpty().append(this.gramBuilder);
/* 317 */         if (this.gramSize.getValue() > 1) {
/* 318 */           this.typeAtt.setType(this.tokenType);
/* 319 */           this.noShingleOutput = false;
/*     */         }
/* 321 */         this.offsetAtt.setOffset(this.offsetAtt.startOffset(), nextToken.offsetAtt.endOffset());
/* 322 */         this.isOutputHere = true;
/* 323 */         this.gramSize.advance();
/* 324 */         tokenAvailable = true;
/*     */       }
/*     */     }
/* 327 */     return tokenAvailable;
/*     */   }
/*     */ 
/*     */   private InputWindowToken getNextToken(InputWindowToken target)
/*     */     throws IOException
/*     */   {
/* 343 */     InputWindowToken newTarget = target;
/* 344 */     if (this.numFillerTokensToInsert > 0) {
/* 345 */       if (null == target)
/* 346 */         newTarget = new InputWindowToken(this.nextInputStreamToken.cloneAttributes());
/*     */       else {
/* 348 */         this.nextInputStreamToken.copyTo(target.attSource);
/*     */       }
/*     */ 
/* 351 */       newTarget.offsetAtt.setOffset(newTarget.offsetAtt.startOffset(), newTarget.offsetAtt.startOffset());
/*     */ 
/* 353 */       newTarget.termAtt.copyBuffer(FILLER_TOKEN, 0, FILLER_TOKEN.length);
/* 354 */       newTarget.isFiller = true;
/* 355 */       this.numFillerTokensToInsert -= 1;
/* 356 */     } else if (this.isNextInputStreamToken) {
/* 357 */       if (null == target)
/* 358 */         newTarget = new InputWindowToken(this.nextInputStreamToken.cloneAttributes());
/*     */       else {
/* 360 */         this.nextInputStreamToken.copyTo(target.attSource);
/*     */       }
/* 362 */       this.isNextInputStreamToken = false;
/* 363 */       newTarget.isFiller = false;
/* 364 */     } else if ((!this.exhausted) && (this.input.incrementToken())) {
/* 365 */       if (null == target)
/* 366 */         newTarget = new InputWindowToken(cloneAttributes());
/*     */       else {
/* 368 */         copyTo(target.attSource);
/*     */       }
/* 370 */       if (this.posIncrAtt.getPositionIncrement() > 1)
/*     */       {
/* 373 */         this.numFillerTokensToInsert = Math.min(this.posIncrAtt.getPositionIncrement() - 1, this.maxShingleSize - 1);
/*     */ 
/* 376 */         if (null == this.nextInputStreamToken)
/* 377 */           this.nextInputStreamToken = cloneAttributes();
/*     */         else {
/* 379 */           copyTo(this.nextInputStreamToken);
/*     */         }
/* 381 */         this.isNextInputStreamToken = true;
/*     */ 
/* 383 */         newTarget.offsetAtt.setOffset(this.offsetAtt.startOffset(), this.offsetAtt.startOffset());
/* 384 */         newTarget.termAtt.copyBuffer(FILLER_TOKEN, 0, FILLER_TOKEN.length);
/* 385 */         newTarget.isFiller = true;
/* 386 */         this.numFillerTokensToInsert -= 1;
/*     */       } else {
/* 388 */         newTarget.isFiller = false;
/*     */       }
/*     */     } else {
/* 391 */       newTarget = null;
/* 392 */       this.exhausted = true;
/*     */     }
/* 394 */     return newTarget;
/*     */   }
/*     */ 
/*     */   private void shiftInputWindow()
/*     */     throws IOException
/*     */   {
/* 405 */     InputWindowToken firstToken = null;
/* 406 */     if (this.inputWindow.size() > 0) {
/* 407 */       firstToken = (InputWindowToken)this.inputWindow.removeFirst();
/*     */     }
/* 409 */     while (this.inputWindow.size() < this.maxShingleSize) {
/* 410 */       if (null != firstToken) {
/* 411 */         if (null == getNextToken(firstToken)) break;
/* 412 */         this.inputWindow.add(firstToken);
/* 413 */         firstToken = null;
/*     */       }
/*     */       else
/*     */       {
/* 418 */         InputWindowToken nextToken = getNextToken(null);
/* 419 */         if (null == nextToken) break;
/* 420 */         this.inputWindow.add(nextToken);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 426 */     if ((this.outputUnigramsIfNoShingles) && (this.noShingleOutput) && (this.gramSize.minValue > 1) && (this.inputWindow.size() < this.minShingleSize))
/*     */     {
/* 428 */       this.gramSize.minValue = 1;
/*     */     }
/* 430 */     this.gramSize.reset();
/* 431 */     this.isOutputHere = false;
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 436 */     super.reset();
/* 437 */     this.gramSize.reset();
/* 438 */     this.inputWindow.clear();
/* 439 */     this.numFillerTokensToInsert = 0;
/* 440 */     this.isOutputHere = false;
/* 441 */     this.noShingleOutput = true;
/* 442 */     this.exhausted = false;
/* 443 */     if ((this.outputUnigramsIfNoShingles) && (!this.outputUnigrams))
/*     */     {
/* 445 */       this.gramSize.minValue = this.minShingleSize;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class InputWindowToken
/*     */   {
/*     */     final AttributeSource attSource;
/*     */     final CharTermAttribute termAtt;
/*     */     final OffsetAttribute offsetAtt;
/* 533 */     boolean isFiller = false;
/*     */ 
/*     */     public InputWindowToken(AttributeSource attSource) {
/* 536 */       this.attSource = attSource;
/* 537 */       this.termAtt = ((CharTermAttribute)attSource.getAttribute(CharTermAttribute.class));
/* 538 */       this.offsetAtt = ((OffsetAttribute)attSource.getAttribute(OffsetAttribute.class));
/*     */     }
/*     */   }
/*     */ 
/*     */   private class CircularSequence
/*     */   {
/*     */     private int value;
/*     */     private int previousValue;
/*     */     private int minValue;
/*     */ 
/*     */     public CircularSequence()
/*     */     {
/* 465 */       this.minValue = (ShingleFilter.this.outputUnigrams ? 1 : ShingleFilter.this.minShingleSize);
/* 466 */       reset();
/*     */     }
/*     */ 
/*     */     public int getValue()
/*     */     {
/* 474 */       return this.value;
/*     */     }
/*     */ 
/*     */     public void advance()
/*     */     {
/* 486 */       this.previousValue = this.value;
/* 487 */       if (this.value == 1)
/* 488 */         this.value = ShingleFilter.this.minShingleSize;
/* 489 */       else if (this.value == ShingleFilter.this.maxShingleSize)
/* 490 */         reset();
/*     */       else
/* 492 */         this.value += 1;
/*     */     }
/*     */ 
/*     */     public void reset()
/*     */     {
/* 505 */       this.previousValue = (this.value = this.minValue);
/*     */     }
/*     */ 
/*     */     public boolean atMinValue()
/*     */     {
/* 518 */       return this.value == this.minValue;
/*     */     }
/*     */ 
/*     */     public int getPreviousValue()
/*     */     {
/* 525 */       return this.previousValue;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.shingle.ShingleFilter
 * JD-Core Version:    0.6.2
 */