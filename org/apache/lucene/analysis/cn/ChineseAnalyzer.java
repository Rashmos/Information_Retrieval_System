/*    */ package org.apache.lucene.analysis.cn;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import org.apache.lucene.analysis.ReusableAnalyzerBase;
/*    */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*    */ import org.apache.lucene.analysis.Tokenizer;
/*    */ 
/*    */ @Deprecated
/*    */ public final class ChineseAnalyzer extends ReusableAnalyzerBase
/*    */ {
/*    */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*    */   {
/* 48 */     Tokenizer source = new ChineseTokenizer(reader);
/* 49 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new ChineseFilter(source));
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cn.ChineseAnalyzer
 * JD-Core Version:    0.6.2
 */