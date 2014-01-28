/*     */ package org.apache.lucene.analysis.fr;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*     */ 
/*     */ @Deprecated
/*     */ public final class FrenchStemFilter extends TokenFilter
/*     */ {
/*  53 */   private FrenchStemmer stemmer = new FrenchStemmer();
/*  54 */   private Set<?> exclusions = null;
/*     */ 
/*  56 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  57 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*     */ 
/*     */   public FrenchStemFilter(TokenStream in) {
/*  60 */     super(in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public FrenchStemFilter(TokenStream in, Set<?> exclusiontable)
/*     */   {
/*  71 */     this(in);
/*  72 */     this.exclusions = exclusiontable;
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*  80 */     if (this.input.incrementToken()) {
/*  81 */       String term = this.termAtt.toString();
/*     */ 
/*  84 */       if ((!this.keywordAttr.isKeyword()) && ((this.exclusions == null) || (!this.exclusions.contains(term)))) {
/*  85 */         String s = this.stemmer.stem(term);
/*     */ 
/*  87 */         if ((s != null) && (!s.equals(term)))
/*  88 */           this.termAtt.setEmpty().append(s);
/*     */       }
/*  90 */       return true;
/*     */     }
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   public void setStemmer(FrenchStemmer stemmer)
/*     */   {
/*  99 */     if (stemmer != null)
/* 100 */       this.stemmer = stemmer;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setExclusionTable(Map<?, ?> exclusiontable)
/*     */   {
/* 109 */     this.exclusions = exclusiontable.keySet();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fr.FrenchStemFilter
 * JD-Core Version:    0.6.2
 */