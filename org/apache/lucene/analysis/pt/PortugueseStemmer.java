/*     */ package org.apache.lucene.analysis.pt;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ public class PortugueseStemmer extends RSLPStemmerBase
/*     */ {
/*  33 */   private static final RSLPStemmerBase.Step plural = (RSLPStemmerBase.Step)steps.get("Plural");
/*  34 */   private static final RSLPStemmerBase.Step feminine = (RSLPStemmerBase.Step)steps.get("Feminine");
/*  35 */   private static final RSLPStemmerBase.Step adverb = (RSLPStemmerBase.Step)steps.get("Adverb");
/*  36 */   private static final RSLPStemmerBase.Step augmentative = (RSLPStemmerBase.Step)steps.get("Augmentative");
/*  37 */   private static final RSLPStemmerBase.Step noun = (RSLPStemmerBase.Step)steps.get("Noun");
/*  38 */   private static final RSLPStemmerBase.Step verb = (RSLPStemmerBase.Step)steps.get("Verb");
/*  39 */   private static final RSLPStemmerBase.Step vowel = (RSLPStemmerBase.Step)steps.get("Vowel");
/*     */ 
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  48 */     assert (s.length >= len + 1) : "this stemmer requires an oversized array of at least 1";
/*     */ 
/*  50 */     len = plural.apply(s, len);
/*  51 */     len = adverb.apply(s, len);
/*  52 */     len = feminine.apply(s, len);
/*  53 */     len = augmentative.apply(s, len);
/*     */ 
/*  55 */     int oldlen = len;
/*  56 */     len = noun.apply(s, len);
/*     */ 
/*  58 */     if (len == oldlen) {
/*  59 */       oldlen = len;
/*     */ 
/*  61 */       len = verb.apply(s, len);
/*     */ 
/*  63 */       if (len == oldlen) {
/*  64 */         len = vowel.apply(s, len);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  69 */     for (int i = 0; i < len; i++)
/*  70 */       switch (s[i]) { case 'à':
/*     */       case 'á':
/*     */       case 'â':
/*     */       case 'ã':
/*     */       case 'ä':
/*     */       case 'å':
/*  76 */         s[i] = 'a'; break;
/*     */       case 'ç':
/*  77 */         s[i] = 'c'; break;
/*     */       case 'è':
/*     */       case 'é':
/*     */       case 'ê':
/*     */       case 'ë':
/*  81 */         s[i] = 'e'; break;
/*     */       case 'ì':
/*     */       case 'í':
/*     */       case 'î':
/*     */       case 'ï':
/*  85 */         s[i] = 'i'; break;
/*     */       case 'ñ':
/*  86 */         s[i] = 'n'; break;
/*     */       case 'ò':
/*     */       case 'ó':
/*     */       case 'ô':
/*     */       case 'õ':
/*     */       case 'ö':
/*  91 */         s[i] = 'o'; break;
/*     */       case 'ù':
/*     */       case 'ú':
/*     */       case 'û':
/*     */       case 'ü':
/*  95 */         s[i] = 'u'; break;
/*     */       case 'ý':
/*     */       case 'ÿ':
/*  97 */         s[i] = 'y';
/*     */       case 'æ':
/*     */       case 'ð':
/*     */       case '÷':
/*     */       case 'ø':
/* 100 */       case 'þ': }  return len;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  32 */     Map steps = parse(PortugueseStemmer.class, "portuguese.rslp");
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.pt.PortugueseStemmer
 * JD-Core Version:    0.6.2
 */