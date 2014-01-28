/*    */ package org.apache.lucene.analysis.pt;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ public class PortugueseMinimalStemmer extends RSLPStemmerBase
/*    */ {
/* 33 */   private static final RSLPStemmerBase.Step pluralStep = (RSLPStemmerBase.Step)parse(PortugueseMinimalStemmer.class, "portuguese.rslp").get("Plural");
/*    */ 
/*    */   public int stem(char[] s, int len)
/*    */   {
/* 37 */     return pluralStep.apply(s, len);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.pt.PortugueseMinimalStemmer
 * JD-Core Version:    0.6.2
 */