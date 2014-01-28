/*    */ package org.apache.lucene.analysis.gl;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.lucene.analysis.pt.RSLPStemmerBase;
/*    */ import org.apache.lucene.analysis.pt.RSLPStemmerBase.Step;
/*    */ 
/*    */ public class GalicianStemmer extends RSLPStemmerBase
/*    */ {
/* 35 */   private static final RSLPStemmerBase.Step plural = (RSLPStemmerBase.Step)steps.get("Plural");
/* 36 */   private static final RSLPStemmerBase.Step unification = (RSLPStemmerBase.Step)steps.get("Unification");
/* 37 */   private static final RSLPStemmerBase.Step adverb = (RSLPStemmerBase.Step)steps.get("Adverb");
/* 38 */   private static final RSLPStemmerBase.Step augmentative = (RSLPStemmerBase.Step)steps.get("Augmentative");
/* 39 */   private static final RSLPStemmerBase.Step noun = (RSLPStemmerBase.Step)steps.get("Noun");
/* 40 */   private static final RSLPStemmerBase.Step verb = (RSLPStemmerBase.Step)steps.get("Verb");
/* 41 */   private static final RSLPStemmerBase.Step vowel = (RSLPStemmerBase.Step)steps.get("Vowel");
/*    */ 
/*    */   public int stem(char[] s, int len)
/*    */   {
/* 50 */     assert (s.length >= len + 1) : "this stemmer requires an oversized array of at least 1";
/*    */ 
/* 52 */     len = plural.apply(s, len);
/* 53 */     len = unification.apply(s, len);
/* 54 */     len = adverb.apply(s, len);
/*    */     do
/*    */     {
/* 58 */       oldlen = len;
/* 59 */       len = augmentative.apply(s, len);
/* 60 */     }while (len != oldlen);
/*    */ 
/* 62 */     int oldlen = len;
/* 63 */     len = noun.apply(s, len);
/* 64 */     if (len == oldlen) {
/* 65 */       len = verb.apply(s, len);
/*    */     }
/*    */ 
/* 68 */     len = vowel.apply(s, len);
/*    */ 
/* 71 */     for (int i = 0; i < len; i++) {
/* 72 */       switch (s[i]) { case 'á':
/* 73 */         s[i] = 'a'; break;
/*    */       case 'é':
/*    */       case 'ê':
/* 75 */         s[i] = 'e'; break;
/*    */       case 'í':
/* 76 */         s[i] = 'i'; break;
/*    */       case 'ó':
/* 77 */         s[i] = 'o'; break;
/*    */       case 'ú':
/* 78 */         s[i] = 'u';
/*    */       }
/*    */     }
/* 81 */     return len;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 34 */     Map steps = parse(GalicianStemmer.class, "galician.rslp");
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.gl.GalicianStemmer
 * JD-Core Version:    0.6.2
 */