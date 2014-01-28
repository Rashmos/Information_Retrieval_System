/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.io.BitInput;
/*     */ import com.aliasi.io.BitOutput;
/*     */ import com.aliasi.stats.Model;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class NGramBoundaryLM
/*     */   implements LanguageModel.Sequence, LanguageModel.Conditional, LanguageModel.Dynamic, Model<CharSequence>
/*     */ {
/*     */   private final NGramProcessLM mProcessLM;
/*     */   private final char mBoundaryChar;
/*     */   private final char[] mBoundaryArray;
/*     */ 
/*     */   public NGramBoundaryLM(int maxNGram)
/*     */   {
/* 110 */     this(maxNGram, 65534);
/*     */   }
/*     */ 
/*     */   public NGramBoundaryLM(int maxNGram, int numChars)
/*     */   {
/* 127 */     this(maxNGram, numChars, maxNGram, 65535);
/*     */   }
/*     */ 
/*     */   public NGramBoundaryLM(int maxNGram, int numChars, double lambdaFactor, char boundaryChar)
/*     */   {
/* 154 */     this(new NGramProcessLM(maxNGram, numChars + 1, lambdaFactor), boundaryChar);
/*     */   }
/*     */ 
/*     */   public NGramBoundaryLM(NGramProcessLM processLm, char boundaryChar)
/*     */   {
/* 175 */     this.mBoundaryChar = boundaryChar;
/* 176 */     this.mBoundaryArray = new char[] { boundaryChar };
/* 177 */     this.mProcessLM = processLm;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 192 */     BitOutput bitOut = new BitOutput(out);
/* 193 */     bitOut.writeDelta(this.mBoundaryChar + '\001');
/* 194 */     this.mProcessLM.writeTo(bitOut);
/* 195 */     bitOut.flush();
/*     */   }
/*     */ 
/*     */   public static NGramBoundaryLM readFrom(InputStream in)
/*     */     throws IOException
/*     */   {
/* 212 */     BitInput bitIn = new BitInput(in);
/* 213 */     char boundaryChar = (char)(int)(bitIn.readDelta() - 1L);
/* 214 */     NGramProcessLM processLM = NGramProcessLM.readFrom(bitIn);
/* 215 */     return new NGramBoundaryLM(processLM, boundaryChar);
/*     */   }
/*     */ 
/*     */   public NGramProcessLM getProcessLM()
/*     */   {
/* 226 */     return this.mProcessLM;
/*     */   }
/*     */ 
/*     */   public char[] observedCharacters()
/*     */   {
/* 236 */     return this.mProcessLM.observedCharacters();
/*     */   }
/*     */ 
/*     */   public TrieCharSeqCounter substringCounter()
/*     */   {
/* 247 */     return this.mProcessLM.substringCounter();
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 262 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public void handle(CharSequence cSeq)
/*     */   {
/* 273 */     train(cSeq);
/*     */   }
/*     */ 
/*     */   public void train(CharSequence cs, int count) {
/* 277 */     char[] csBounded = addBoundaries(cs, this.mBoundaryChar);
/* 278 */     this.mProcessLM.train(csBounded, 0, csBounded.length, count);
/*     */ 
/* 280 */     this.mProcessLM.decrementUnigram(this.mBoundaryChar, count);
/*     */   }
/*     */ 
/*     */   public void train(CharSequence cs) {
/* 284 */     train(cs, 1);
/*     */   }
/*     */ 
/*     */   public void train(char[] cs, int start, int end) {
/* 288 */     train(cs, start, end, 1);
/*     */   }
/*     */ 
/*     */   public void train(char[] cs, int start, int end, int count) {
/* 292 */     char[] csBounded = addBoundaries(cs, start, end, this.mBoundaryChar);
/* 293 */     this.mProcessLM.train(csBounded, 0, csBounded.length, count);
/* 294 */     this.mProcessLM.decrementUnigram(this.mBoundaryChar, count);
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(CharSequence cs) {
/* 298 */     if (cs.length() < 1) {
/* 299 */       String msg = "Conditional estimate must be at least one character.";
/* 300 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 302 */     char[] csBounded = addBoundaries(cs, this.mBoundaryChar);
/* 303 */     return this.mProcessLM.log2ConditionalEstimate(csBounded, 0, csBounded.length - 1);
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(char[] cs, int start, int end) {
/* 307 */     if (end <= start) {
/* 308 */       String msg = "Conditional estimate must be at least one character.";
/* 309 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 311 */     char[] csBounded = addBoundaries(cs, start, end, this.mBoundaryChar);
/* 312 */     return this.mProcessLM.log2ConditionalEstimate(csBounded, 0, csBounded.length - 1);
/*     */   }
/*     */ 
/*     */   public double log2Estimate(CharSequence cs) {
/* 316 */     char[] csBounded = addBoundaries(cs, this.mBoundaryChar);
/* 317 */     return this.mProcessLM.log2Estimate(csBounded, 0, csBounded.length) - this.mProcessLM.log2Estimate(this.mBoundaryArray, 0, 1);
/*     */   }
/*     */ 
/*     */   public double log2Estimate(char[] cs, int start, int end)
/*     */   {
/* 323 */     char[] csBounded = addBoundaries(cs, start, end, this.mBoundaryChar);
/* 324 */     return this.mProcessLM.log2Estimate(csBounded, 0, csBounded.length) - this.mProcessLM.log2Estimate(this.mBoundaryArray, 0, 1);
/*     */   }
/*     */ 
/*     */   public double log2Prob(CharSequence cSeq)
/*     */   {
/* 337 */     return log2Estimate(cSeq);
/*     */   }
/*     */ 
/*     */   public double prob(CharSequence cSeq)
/*     */   {
/* 349 */     return Math.pow(2.0D, log2Estimate(cSeq));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 361 */     StringBuilder sb = new StringBuilder();
/* 362 */     sb.append("Boundary char=" + this.mBoundaryChar);
/* 363 */     sb.append('\n');
/* 364 */     this.mProcessLM.toStringBuilder(sb);
/* 365 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static char[] addBoundaries(CharSequence cs, char boundaryChar) {
/* 369 */     char[] cs2 = new char[cs.length() + 2];
/* 370 */     for (int i = 0; i < cs.length(); i++) {
/* 371 */       char c = cs.charAt(i);
/* 372 */       if (c == boundaryChar) {
/* 373 */         String msg = "Estimated string cannot contain boundary char. Found boundary char=" + c + " at index=" + i;
/*     */ 
/* 376 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 378 */       cs2[(i + 1)] = cs.charAt(i);
/*     */     }
/* 380 */     addBoundaryChars(cs2, boundaryChar);
/* 381 */     return cs2;
/*     */   }
/*     */ 
/*     */   static char[] addBoundaries(char[] cs, int start, int end, char boundaryChar) {
/* 385 */     char[] cs2 = new char[cs.length + 1];
/* 386 */     int len = end - start;
/* 387 */     for (int i = 0; i < len; i++) {
/* 388 */       char c = cs[(i + start)];
/* 389 */       if (c == boundaryChar)
/*     */       {
/* 391 */         String msg = "Estimated string cannot contain boundary char. Found boundary char=" + c + " at index=" + (i + start);
/*     */ 
/* 394 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 396 */       cs2[(i + 1)] = c;
/*     */     }
/* 398 */     addBoundaryChars(cs2, boundaryChar);
/* 399 */     return cs2;
/*     */   }
/*     */ 
/*     */   static void addBoundaryChars(char[] cs, char boundaryChar) {
/* 403 */     cs[0] = boundaryChar;
/* 404 */     cs[(cs.length - 1)] = boundaryChar;
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     private static final long serialVersionUID = -7945082563035787530L;
/*     */     final NGramBoundaryLM mLM;
/*     */ 
/* 411 */     public Externalizer() { this(null); }
/*     */ 
/*     */     public Externalizer(NGramBoundaryLM lm) {
/* 414 */       this.mLM = lm;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws IOException {
/* 418 */       return new CompiledNGramBoundaryLM(objIn);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 422 */       objOut.writeChar(this.mLM.mBoundaryChar);
/* 423 */       this.mLM.mProcessLM.compileTo(objOut);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.NGramBoundaryLM
 * JD-Core Version:    0.6.2
 */