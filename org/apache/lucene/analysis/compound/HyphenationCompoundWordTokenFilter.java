/*     */ package org.apache.lucene.analysis.compound;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.compound.hyphenation.Hyphenation;
/*     */ import org.apache.lucene.analysis.compound.hyphenation.HyphenationTree;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.util.Version;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class HyphenationCompoundWordTokenFilter extends CompoundWordTokenFilterBase
/*     */ {
/*     */   private HyphenationTree hyphenator;
/*     */ 
/*     */   @Deprecated
/*     */   public HyphenationCompoundWordTokenFilter(Version matchVersion, TokenStream input, HyphenationTree hyphenator, String[] dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/*  85 */     this(matchVersion, input, hyphenator, makeDictionary(matchVersion, dictionary), minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public HyphenationCompoundWordTokenFilter(Version matchVersion, TokenStream input, HyphenationTree hyphenator, String[] dictionary)
/*     */   {
/* 108 */     this(Version.LUCENE_30, input, hyphenator, makeDictionary(Version.LUCENE_30, dictionary), 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   public HyphenationCompoundWordTokenFilter(Version matchVersion, TokenStream input, HyphenationTree hyphenator, Set<?> dictionary)
/*     */   {
/* 129 */     this(input, hyphenator, dictionary, 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   public HyphenationCompoundWordTokenFilter(Version matchVersion, TokenStream input, HyphenationTree hyphenator, Set<?> dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/* 159 */     super(matchVersion, input, dictionary, minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */ 
/* 162 */     this.hyphenator = hyphenator;
/*     */   }
/*     */ 
/*     */   public HyphenationCompoundWordTokenFilter(Version matchVersion, TokenStream input, HyphenationTree hyphenator, int minWordSize, int minSubwordSize, int maxSubwordSize)
/*     */   {
/* 175 */     this(matchVersion, input, hyphenator, (Set)null, minWordSize, minSubwordSize, maxSubwordSize, false);
/*     */   }
/*     */ 
/*     */   public HyphenationCompoundWordTokenFilter(Version matchVersion, TokenStream input, HyphenationTree hyphenator)
/*     */   {
/* 188 */     this(matchVersion, input, hyphenator, 5, 2, 15);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public HyphenationCompoundWordTokenFilter(TokenStream input, HyphenationTree hyphenator, String[] dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/* 210 */     this(Version.LUCENE_30, input, hyphenator, makeDictionary(Version.LUCENE_30, dictionary), minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public HyphenationCompoundWordTokenFilter(TokenStream input, HyphenationTree hyphenator, String[] dictionary)
/*     */   {
/* 225 */     this(Version.LUCENE_30, input, hyphenator, makeDictionary(Version.LUCENE_30, dictionary), 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public HyphenationCompoundWordTokenFilter(TokenStream input, HyphenationTree hyphenator, Set<?> dictionary)
/*     */   {
/* 241 */     this(Version.LUCENE_30, input, hyphenator, dictionary, 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public HyphenationCompoundWordTokenFilter(TokenStream input, HyphenationTree hyphenator, Set<?> dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/* 264 */     super(Version.LUCENE_30, input, dictionary, minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */ 
/* 267 */     this.hyphenator = hyphenator;
/*     */   }
/*     */ 
/*     */   public static HyphenationTree getHyphenationTree(String hyphenationFilename)
/*     */     throws Exception
/*     */   {
/* 279 */     return getHyphenationTree(new InputSource(hyphenationFilename));
/*     */   }
/*     */ 
/*     */   public static HyphenationTree getHyphenationTree(File hyphenationFile)
/*     */     throws Exception
/*     */   {
/* 291 */     return getHyphenationTree(new InputSource(hyphenationFile.toURL().toExternalForm()));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static HyphenationTree getHyphenationTree(Reader hyphenationReader)
/*     */     throws Exception
/*     */   {
/* 307 */     InputSource is = new InputSource(hyphenationReader);
/*     */ 
/* 311 */     is.setSystemId("urn:java:" + HyphenationTree.class.getName());
/* 312 */     return getHyphenationTree(is);
/*     */   }
/*     */ 
/*     */   public static HyphenationTree getHyphenationTree(InputSource hyphenationSource)
/*     */     throws Exception
/*     */   {
/* 324 */     HyphenationTree tree = new HyphenationTree();
/* 325 */     tree.loadPatterns(hyphenationSource);
/* 326 */     return tree;
/*     */   }
/*     */ 
/*     */   protected void decompose()
/*     */   {
/* 332 */     Hyphenation hyphens = this.hyphenator.hyphenate(this.termAtt.buffer(), 0, this.termAtt.length(), 1, 1);
/*     */ 
/* 334 */     if (hyphens == null) {
/* 335 */       return;
/*     */     }
/*     */ 
/* 338 */     int[] hyp = hyphens.getHyphenationPoints();
/*     */ 
/* 340 */     for (int i = 0; i < hyp.length; i++) {
/* 341 */       int remaining = hyp.length - i;
/* 342 */       int start = hyp[i];
/* 343 */       CompoundWordTokenFilterBase.CompoundToken longestMatchToken = null;
/* 344 */       for (int j = 1; j < remaining; j++) {
/* 345 */         int partLength = hyp[(i + j)] - start;
/*     */ 
/* 349 */         if (partLength > this.maxSubwordSize)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 355 */         if (partLength >= this.minSubwordSize)
/*     */         {
/* 360 */           if ((this.dictionary == null) || (this.dictionary.contains(this.termAtt.buffer(), start, partLength))) {
/* 361 */             if (this.onlyLongestMatch) {
/* 362 */               if (longestMatchToken != null) {
/* 363 */                 if (longestMatchToken.txt.length() < partLength)
/* 364 */                   longestMatchToken = new CompoundWordTokenFilterBase.CompoundToken(this, start, partLength);
/*     */               }
/*     */               else
/* 367 */                 longestMatchToken = new CompoundWordTokenFilterBase.CompoundToken(this, start, partLength);
/*     */             }
/*     */             else
/* 370 */               this.tokens.add(new CompoundWordTokenFilterBase.CompoundToken(this, start, partLength));
/*     */           }
/* 372 */           else if (this.dictionary.contains(this.termAtt.buffer(), start, partLength - 1))
/*     */           {
/* 377 */             if (this.onlyLongestMatch) {
/* 378 */               if (longestMatchToken != null) {
/* 379 */                 if (longestMatchToken.txt.length() < partLength - 1)
/* 380 */                   longestMatchToken = new CompoundWordTokenFilterBase.CompoundToken(this, start, partLength - 1);
/*     */               }
/*     */               else
/* 383 */                 longestMatchToken = new CompoundWordTokenFilterBase.CompoundToken(this, start, partLength - 1);
/*     */             }
/*     */             else
/* 386 */               this.tokens.add(new CompoundWordTokenFilterBase.CompoundToken(this, start, partLength - 1));
/*     */           }
/*     */         }
/*     */       }
/* 390 */       if ((this.onlyLongestMatch) && (longestMatchToken != null))
/* 391 */         this.tokens.add(longestMatchToken);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.HyphenationCompoundWordTokenFilter
 * JD-Core Version:    0.6.2
 */