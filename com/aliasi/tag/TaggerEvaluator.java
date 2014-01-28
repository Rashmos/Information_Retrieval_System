/*     */ package com.aliasi.tag;
/*     */ 
/*     */ import com.aliasi.classify.BaseClassifierEvaluator;
/*     */ import com.aliasi.classify.Classification;
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Formatter;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TaggerEvaluator<E>
/*     */   implements ObjectHandler<Tagging<E>>
/*     */ {
/*     */   private Tagger<E> mTagger;
/*  97 */   private final List<List<String>> mReferenceTagss = new ArrayList();
/*     */ 
/*  99 */   private final List<List<String>> mResponseTagss = new ArrayList();
/*     */   private final List<List<E>> mTokenss;
/*     */ 
/*     */   public TaggerEvaluator(Tagger<E> tagger, boolean storeTokens)
/*     */   {
/* 114 */     this.mTagger = tagger;
/* 115 */     this.mTokenss = (storeTokens ? new ArrayList() : null);
/*     */   }
/*     */ 
/*     */   public Tagger<E> tagger()
/*     */   {
/* 124 */     return this.mTagger;
/*     */   }
/*     */ 
/*     */   public void setTagger(Tagger<E> tagger)
/*     */   {
/* 133 */     this.mTagger = tagger;
/*     */   }
/*     */ 
/*     */   public boolean storeTokens()
/*     */   {
/* 142 */     return this.mTokenss != null;
/*     */   }
/*     */ 
/*     */   public void handle(Tagging<E> referenceTagging)
/*     */   {
/* 153 */     List tokens = referenceTagging.tokens();
/* 154 */     Tagging responseTagging = this.mTagger.tag(tokens);
/* 155 */     addCase(referenceTagging, responseTagging);
/*     */   }
/*     */ 
/*     */   public void addCase(Tagging<E> referenceTagging, Tagging<E> responseTagging)
/*     */   {
/* 169 */     if (!referenceTagging.tokens().equals(responseTagging.tokens())) {
/* 170 */       String msg = "Require taggings to have same tokens. Found referenceTagging.tokens() = " + referenceTagging.tokens() + " responseTagging.tokens()=" + responseTagging.tokens();
/*     */ 
/* 173 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 175 */     this.mReferenceTagss.add(referenceTagging.tags());
/* 176 */     this.mResponseTagss.add(responseTagging.tags());
/* 177 */     if (storeTokens())
/* 178 */       this.mTokenss.add(referenceTagging.tokens());
/*     */   }
/*     */ 
/*     */   public int numCases()
/*     */   {
/* 188 */     return this.mReferenceTagss.size();
/*     */   }
/*     */ 
/*     */   public long numTokens()
/*     */   {
/* 198 */     long count = 0L;
/* 199 */     for (List tags : this.mReferenceTagss)
/* 200 */       count += tags.size();
/* 201 */     return count;
/*     */   }
/*     */ 
/*     */   public List<String> tags()
/*     */   {
/* 211 */     Set tagSet = new HashSet();
/* 212 */     for (int i = 0; i < this.mReferenceTagss.size(); i++) {
/* 213 */       tagSet.addAll((Collection)this.mReferenceTagss.get(i));
/* 214 */       tagSet.addAll((Collection)this.mResponseTagss.get(i));
/*     */     }
/* 216 */     return new ArrayList(tagSet);
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap<Integer> inputSizeHistogram()
/*     */   {
/* 228 */     ObjectToCounterMap hist = new ObjectToCounterMap();
/* 229 */     for (List tags : this.mReferenceTagss)
/* 230 */       hist.increment(Integer.valueOf(tags.size()));
/* 231 */     return hist;
/*     */   }
/*     */ 
/*     */   public double caseAccuracy()
/*     */   {
/* 243 */     int correct = 0;
/* 244 */     for (int i = 0; i < this.mReferenceTagss.size(); i++)
/* 245 */       if (((List)this.mReferenceTagss.get(i)).equals(this.mResponseTagss.get(i)))
/* 246 */         correct++;
/* 247 */     return correct / this.mReferenceTagss.size();
/*     */   }
/*     */ 
/*     */   public BaseClassifierEvaluator<E> unknownTokenEval(Set<E> knownTokenSet)
/*     */   {
/* 262 */     if (!storeTokens()) {
/* 263 */       String msg = "Must store inputs to compute unknown token accuracy.";
/* 264 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 266 */     return eval(knownTokenSet);
/*     */   }
/*     */ 
/*     */   public BaseClassifierEvaluator<E> tokenEval()
/*     */   {
/* 277 */     return eval(null);
/*     */   }
/*     */ 
/*     */   public String lastCaseToString(Set<E> knownTokenSet)
/*     */   {
/* 290 */     if (this.mTokenss.isEmpty())
/* 291 */       return "No cases handled yet.";
/* 292 */     List lastTokens = (List)this.mTokenss.get(this.mTokenss.size() - 1);
/* 293 */     List refTags = (List)this.mReferenceTagss.get(this.mReferenceTagss.size() - 1);
/* 294 */     List respTags = (List)this.mResponseTagss.get(this.mResponseTagss.size() - 1);
/* 295 */     StringBuilder sb = new StringBuilder();
/* 296 */     Formatter formatter = new Formatter(sb, Locale.US);
/* 297 */     sb.append("Known  Token     Reference | Response  ?correct\n");
/* 298 */     for (int tokenIndex = 0; tokenIndex < lastTokens.size(); tokenIndex++) {
/* 299 */       sb.append((knownTokenSet == null) || (knownTokenSet.contains(lastTokens.get(tokenIndex))) ? "    " : "  ? ");
/*     */ 
/* 302 */       sb.append(pad(lastTokens.get(tokenIndex), 20));
/* 303 */       sb.append(pad(refTags.get(tokenIndex), 4));
/* 304 */       sb.append("  |  ");
/* 305 */       sb.append(pad(respTags.get(tokenIndex), 6));
/* 306 */       if (((String)refTags.get(tokenIndex)).equals(respTags.get(tokenIndex)))
/* 307 */         sb.append("\n");
/*     */       else
/* 309 */         sb.append(" XX\n");
/*     */     }
/* 311 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static String pad(Object obj, int length) {
/* 315 */     String in = obj.toString();
/* 316 */     if (in.length() > length) return in.substring(0, length - 4) + "... ";
/* 317 */     if (in.length() == length) return in;
/* 318 */     StringBuilder sb = new StringBuilder(length);
/* 319 */     sb.append(in);
/* 320 */     while (sb.length() < length) sb.append(' ');
/* 321 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   BaseClassifierEvaluator<E> eval(Set<E> knownTokenSet)
/*     */   {
/* 326 */     String[] tags = (String[])tags().toArray(Strings.EMPTY_STRING_ARRAY);
/* 327 */     BaseClassifierEvaluator evaluator = new BaseClassifierEvaluator(null, tags, storeTokens());
/*     */ 
/* 329 */     for (int i = 0; i < this.mReferenceTagss.size(); i++) {
/* 330 */       List referenceTags = (List)this.mReferenceTagss.get(i);
/* 331 */       List responseTags = (List)this.mResponseTagss.get(i);
/* 332 */       List tokens = (List)this.mTokenss.get(i);
/* 333 */       for (int j = 0; j < tokens.size(); j++) {
/* 334 */         String referenceTag = (String)referenceTags.get(j);
/* 335 */         Classification responseClassification = new Classification((String)responseTags.get(j));
/* 336 */         if ((knownTokenSet == null) || (!knownTokenSet.contains(tokens.get(j)))) {
/* 337 */           evaluator.addClassification(referenceTag, responseClassification, tokens.get(j));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 343 */     return evaluator;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.TaggerEvaluator
 * JD-Core Version:    0.6.2
 */