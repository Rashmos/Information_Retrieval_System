/*    */ package org.apache.lucene.analysis.th;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import org.apache.lucene.analysis.LowerCaseFilter;
/*    */ import org.apache.lucene.analysis.ReusableAnalyzerBase;
/*    */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*    */ import org.apache.lucene.analysis.StopAnalyzer;
/*    */ import org.apache.lucene.analysis.StopFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.Tokenizer;
/*    */ import org.apache.lucene.analysis.standard.StandardFilter;
/*    */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*    */ import org.apache.lucene.util.Version;
/*    */ 
/*    */ public final class ThaiAnalyzer extends ReusableAnalyzerBase
/*    */ {
/*    */   private final Version matchVersion;
/*    */ 
/*    */   public ThaiAnalyzer(Version matchVersion)
/*    */   {
/* 44 */     this.matchVersion = matchVersion;
/*    */   }
/*    */ 
/*    */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*    */   {
/* 60 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 61 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 62 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31))
/* 63 */       result = new LowerCaseFilter(this.matchVersion, result);
/* 64 */     result = new ThaiWordFilter(this.matchVersion, result);
/* 65 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new StopFilter(this.matchVersion, result, StopAnalyzer.ENGLISH_STOP_WORDS_SET));
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.th.ThaiAnalyzer
 * JD-Core Version:    0.6.2
 */