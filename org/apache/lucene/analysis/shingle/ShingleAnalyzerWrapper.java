/*     */ package org.apache.lucene.analysis.shingle;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.standard.StandardAnalyzer;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class ShingleAnalyzerWrapper extends Analyzer
/*     */ {
/*     */   private final Analyzer defaultAnalyzer;
/*  37 */   private int maxShingleSize = 2;
/*  38 */   private int minShingleSize = 2;
/*  39 */   private String tokenSeparator = " ";
/*  40 */   private boolean outputUnigrams = true;
/*  41 */   private boolean outputUnigramsIfNoShingles = false;
/*     */ 
/*     */   public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer) {
/*  44 */     this(defaultAnalyzer, 2);
/*     */   }
/*     */ 
/*     */   public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer, int maxShingleSize) {
/*  48 */     this(defaultAnalyzer, 2, maxShingleSize);
/*     */   }
/*     */ 
/*     */   public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer, int minShingleSize, int maxShingleSize) {
/*  52 */     this(defaultAnalyzer, minShingleSize, maxShingleSize, " ", true, false);
/*     */   }
/*     */ 
/*     */   public ShingleAnalyzerWrapper(Analyzer defaultAnalyzer, int minShingleSize, int maxShingleSize, String tokenSeparator, boolean outputUnigrams, boolean outputUnigramsIfNoShingles)
/*     */   {
/*  77 */     this.defaultAnalyzer = defaultAnalyzer;
/*     */ 
/*  79 */     if (maxShingleSize < 2) {
/*  80 */       throw new IllegalArgumentException("Max shingle size must be >= 2");
/*     */     }
/*  82 */     this.maxShingleSize = maxShingleSize;
/*     */ 
/*  84 */     if (minShingleSize < 2) {
/*  85 */       throw new IllegalArgumentException("Min shingle size must be >= 2");
/*     */     }
/*  87 */     if (minShingleSize > maxShingleSize) {
/*  88 */       throw new IllegalArgumentException("Min shingle size must be <= max shingle size");
/*     */     }
/*     */ 
/*  91 */     this.minShingleSize = minShingleSize;
/*     */ 
/*  93 */     this.tokenSeparator = (tokenSeparator == null ? "" : tokenSeparator);
/*  94 */     this.outputUnigrams = outputUnigrams;
/*  95 */     this.outputUnigramsIfNoShingles = outputUnigramsIfNoShingles;
/*     */   }
/*     */ 
/*     */   public ShingleAnalyzerWrapper(Version matchVersion)
/*     */   {
/* 102 */     this(matchVersion, 2, 2);
/*     */   }
/*     */ 
/*     */   public ShingleAnalyzerWrapper(Version matchVersion, int minShingleSize, int maxShingleSize)
/*     */   {
/* 109 */     this(new StandardAnalyzer(matchVersion), minShingleSize, maxShingleSize);
/*     */   }
/*     */ 
/*     */   public int getMaxShingleSize()
/*     */   {
/* 118 */     return this.maxShingleSize;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setMaxShingleSize(int maxShingleSize)
/*     */   {
/* 130 */     if (maxShingleSize < 2) {
/* 131 */       throw new IllegalArgumentException("Max shingle size must be >= 2");
/*     */     }
/* 133 */     this.maxShingleSize = maxShingleSize;
/*     */   }
/*     */ 
/*     */   public int getMinShingleSize()
/*     */   {
/* 142 */     return this.minShingleSize;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setMinShingleSize(int minShingleSize)
/*     */   {
/* 157 */     if (minShingleSize < 2) {
/* 158 */       throw new IllegalArgumentException("Min shingle size must be >= 2");
/*     */     }
/* 160 */     if (minShingleSize > this.maxShingleSize) {
/* 161 */       throw new IllegalArgumentException("Min shingle size must be <= max shingle size");
/*     */     }
/*     */ 
/* 164 */     this.minShingleSize = minShingleSize;
/*     */   }
/*     */ 
/*     */   public String getTokenSeparator() {
/* 168 */     return this.tokenSeparator;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setTokenSeparator(String tokenSeparator)
/*     */   {
/* 179 */     this.tokenSeparator = (tokenSeparator == null ? "" : tokenSeparator);
/*     */   }
/*     */ 
/*     */   public boolean isOutputUnigrams() {
/* 183 */     return this.outputUnigrams;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setOutputUnigrams(boolean outputUnigrams)
/*     */   {
/* 197 */     this.outputUnigrams = outputUnigrams;
/*     */   }
/*     */ 
/*     */   public boolean isOutputUnigramsIfNoShingles() {
/* 201 */     return this.outputUnigramsIfNoShingles;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setOutputUnigramsIfNoShingles(boolean outputUnigramsIfNoShingles)
/*     */   {
/* 218 */     this.outputUnigramsIfNoShingles = outputUnigramsIfNoShingles;
/*     */   }
/*     */ 
/*     */   public TokenStream tokenStream(String fieldName, Reader reader)
/*     */   {
/*     */     TokenStream wrapped;
/*     */     try {
/* 225 */       wrapped = this.defaultAnalyzer.reusableTokenStream(fieldName, reader);
/*     */     } catch (IOException e) {
/* 227 */       wrapped = this.defaultAnalyzer.tokenStream(fieldName, reader);
/*     */     }
/* 229 */     ShingleFilter filter = new ShingleFilter(wrapped, this.minShingleSize, this.maxShingleSize);
/* 230 */     filter.setMinShingleSize(this.minShingleSize);
/* 231 */     filter.setMaxShingleSize(this.maxShingleSize);
/* 232 */     filter.setTokenSeparator(this.tokenSeparator);
/* 233 */     filter.setOutputUnigrams(this.outputUnigrams);
/* 234 */     filter.setOutputUnigramsIfNoShingles(this.outputUnigramsIfNoShingles);
/* 235 */     return filter;
/*     */   }
/*     */ 
/*     */   public TokenStream reusableTokenStream(String fieldName, Reader reader)
/*     */     throws IOException
/*     */   {
/* 245 */     SavedStreams streams = (SavedStreams)getPreviousTokenStream();
/* 246 */     if (streams == null) {
/* 247 */       streams = new SavedStreams(null);
/* 248 */       streams.wrapped = this.defaultAnalyzer.reusableTokenStream(fieldName, reader);
/* 249 */       streams.shingle = new ShingleFilter(streams.wrapped);
/* 250 */       setPreviousTokenStream(streams);
/*     */     } else {
/* 252 */       TokenStream result = this.defaultAnalyzer.reusableTokenStream(fieldName, reader);
/* 253 */       if (result != streams.wrapped)
/*     */       {
/* 255 */         streams.wrapped = result;
/* 256 */         streams.shingle = new ShingleFilter(streams.wrapped);
/*     */       }
/*     */     }
/* 259 */     streams.shingle.setMaxShingleSize(this.maxShingleSize);
/* 260 */     streams.shingle.setMinShingleSize(this.minShingleSize);
/* 261 */     streams.shingle.setTokenSeparator(this.tokenSeparator);
/* 262 */     streams.shingle.setOutputUnigrams(this.outputUnigrams);
/* 263 */     streams.shingle.setOutputUnigramsIfNoShingles(this.outputUnigramsIfNoShingles);
/* 264 */     return streams.shingle;
/*     */   }
/*     */ 
/*     */   private class SavedStreams
/*     */   {
/*     */     TokenStream wrapped;
/*     */     ShingleFilter shingle;
/*     */ 
/*     */     private SavedStreams()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper
 * JD-Core Version:    0.6.2
 */