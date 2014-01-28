/*     */ package org.apache.lucene.analysis;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class PerFieldAnalyzerWrapper extends Analyzer
/*     */ {
/*     */   private Analyzer defaultAnalyzer;
/*  47 */   private Map<String, Analyzer> analyzerMap = new HashMap();
/*     */ 
/*     */   public PerFieldAnalyzerWrapper(Analyzer defaultAnalyzer)
/*     */   {
/*  57 */     this(defaultAnalyzer, null);
/*     */   }
/*     */ 
/*     */   public PerFieldAnalyzerWrapper(Analyzer defaultAnalyzer, Map<String, Analyzer> fieldAnalyzers)
/*     */   {
/*  71 */     this.defaultAnalyzer = defaultAnalyzer;
/*  72 */     if (fieldAnalyzers != null) {
/*  73 */       this.analyzerMap.putAll(fieldAnalyzers);
/*     */     }
/*  75 */     setOverridesTokenStreamMethod(PerFieldAnalyzerWrapper.class);
/*     */   }
/*     */ 
/*     */   public void addAnalyzer(String fieldName, Analyzer analyzer)
/*     */   {
/*  86 */     this.analyzerMap.put(fieldName, analyzer);
/*     */   }
/*     */ 
/*     */   public TokenStream tokenStream(String fieldName, Reader reader)
/*     */   {
/*  91 */     Analyzer analyzer = (Analyzer)this.analyzerMap.get(fieldName);
/*  92 */     if (analyzer == null) {
/*  93 */       analyzer = this.defaultAnalyzer;
/*     */     }
/*     */ 
/*  96 */     return analyzer.tokenStream(fieldName, reader);
/*     */   }
/*     */ 
/*     */   public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException
/*     */   {
/* 101 */     if (this.overridesTokenStreamMethod)
/*     */     {
/* 105 */       return tokenStream(fieldName, reader);
/*     */     }
/* 107 */     Analyzer analyzer = (Analyzer)this.analyzerMap.get(fieldName);
/* 108 */     if (analyzer == null) {
/* 109 */       analyzer = this.defaultAnalyzer;
/*     */     }
/* 111 */     return analyzer.reusableTokenStream(fieldName, reader);
/*     */   }
/*     */ 
/*     */   public int getPositionIncrementGap(String fieldName)
/*     */   {
/* 117 */     Analyzer analyzer = (Analyzer)this.analyzerMap.get(fieldName);
/* 118 */     if (analyzer == null)
/* 119 */       analyzer = this.defaultAnalyzer;
/* 120 */     return analyzer.getPositionIncrementGap(fieldName);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 125 */     return "PerFieldAnalyzerWrapper(" + this.analyzerMap + ", default=" + this.defaultAnalyzer + ")";
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.PerFieldAnalyzerWrapper
 * JD-Core Version:    0.6.2
 */