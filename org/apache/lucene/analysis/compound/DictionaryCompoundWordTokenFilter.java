/*     */ package org.apache.lucene.analysis.compound;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public class DictionaryCompoundWordTokenFilter extends CompoundWordTokenFilterBase
/*     */ {
/*     */   @Deprecated
/*     */   public DictionaryCompoundWordTokenFilter(TokenStream input, String[] dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/*  64 */     super(Version.LUCENE_30, input, dictionary, minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DictionaryCompoundWordTokenFilter(TokenStream input, String[] dictionary)
/*     */   {
/*  76 */     super(Version.LUCENE_30, input, dictionary);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DictionaryCompoundWordTokenFilter(TokenStream input, Set dictionary)
/*     */   {
/*  89 */     super(Version.LUCENE_30, input, dictionary);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DictionaryCompoundWordTokenFilter(TokenStream input, Set dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/* 107 */     super(Version.LUCENE_30, input, dictionary, minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DictionaryCompoundWordTokenFilter(Version matchVersion, TokenStream input, String[] dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/* 135 */     super(matchVersion, input, dictionary, minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DictionaryCompoundWordTokenFilter(Version matchVersion, TokenStream input, String[] dictionary)
/*     */   {
/* 155 */     super(matchVersion, input, dictionary);
/*     */   }
/*     */ 
/*     */   public DictionaryCompoundWordTokenFilter(Version matchVersion, TokenStream input, Set<?> dictionary)
/*     */   {
/* 172 */     super(matchVersion, input, dictionary);
/*     */   }
/*     */ 
/*     */   public DictionaryCompoundWordTokenFilter(Version matchVersion, TokenStream input, Set<?> dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/* 198 */     super(matchVersion, input, dictionary, minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   protected void decompose()
/*     */   {
/* 203 */     int len = this.termAtt.length();
/* 204 */     for (int i = 0; i <= len - this.minSubwordSize; i++) {
/* 205 */       CompoundWordTokenFilterBase.CompoundToken longestMatchToken = null;
/* 206 */       for (int j = this.minSubwordSize; (j <= this.maxSubwordSize) && 
/* 207 */         (i + j <= len); j++)
/*     */       {
/* 210 */         if (this.dictionary.contains(this.termAtt.buffer(), i, j)) {
/* 211 */           if (this.onlyLongestMatch) {
/* 212 */             if (longestMatchToken != null) {
/* 213 */               if (longestMatchToken.txt.length() < j)
/* 214 */                 longestMatchToken = new CompoundWordTokenFilterBase.CompoundToken(this, i, j);
/*     */             }
/*     */             else
/* 217 */               longestMatchToken = new CompoundWordTokenFilterBase.CompoundToken(this, i, j);
/*     */           }
/*     */           else {
/* 220 */             this.tokens.add(new CompoundWordTokenFilterBase.CompoundToken(this, i, j));
/*     */           }
/*     */         }
/*     */       }
/* 224 */       if ((this.onlyLongestMatch) && (longestMatchToken != null))
/* 225 */         this.tokens.add(longestMatchToken);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.DictionaryCompoundWordTokenFilter
 * JD-Core Version:    0.6.2
 */