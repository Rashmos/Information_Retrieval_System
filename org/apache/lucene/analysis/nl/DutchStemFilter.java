/*     */ package org.apache.lucene.analysis.nl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*     */ 
/*     */ @Deprecated
/*     */ public final class DutchStemFilter extends TokenFilter
/*     */ {
/*  55 */   private DutchStemmer stemmer = new DutchStemmer();
/*  56 */   private Set<?> exclusions = null;
/*     */ 
/*  58 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  59 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*     */ 
/*     */   public DutchStemFilter(TokenStream _in) {
/*  62 */     super(_in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DutchStemFilter(TokenStream _in, Set<?> exclusiontable)
/*     */   {
/*  71 */     this(_in);
/*  72 */     this.exclusions = exclusiontable;
/*     */   }
/*     */ 
/*     */   public DutchStemFilter(TokenStream _in, Map<?, ?> stemdictionary)
/*     */   {
/*  79 */     this(_in);
/*  80 */     this.stemmer.setStemDictionary(stemdictionary);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DutchStemFilter(TokenStream _in, Set<?> exclusiontable, Map<?, ?> stemdictionary)
/*     */   {
/*  89 */     this(_in, exclusiontable);
/*  90 */     this.stemmer.setStemDictionary(stemdictionary);
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*  98 */     if (this.input.incrementToken()) {
/*  99 */       String term = this.termAtt.toString();
/*     */ 
/* 102 */       if ((!this.keywordAttr.isKeyword()) && ((this.exclusions == null) || (!this.exclusions.contains(term)))) {
/* 103 */         String s = this.stemmer.stem(term);
/*     */ 
/* 105 */         if ((s != null) && (!s.equals(term)))
/* 106 */           this.termAtt.setEmpty().append(s);
/*     */       }
/* 108 */       return true;
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public void setStemmer(DutchStemmer stemmer)
/*     */   {
/* 118 */     if (stemmer != null)
/* 119 */       this.stemmer = stemmer;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setExclusionTable(HashSet<?> exclusiontable)
/*     */   {
/* 129 */     this.exclusions = exclusiontable;
/*     */   }
/*     */ 
/*     */   public void setStemDictionary(HashMap<?, ?> dict)
/*     */   {
/* 137 */     if (this.stemmer != null)
/* 138 */       this.stemmer.setStemDictionary(dict);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.nl.DutchStemFilter
 * JD-Core Version:    0.6.2
 */