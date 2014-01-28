/*     */ package com.aliasi.crf;
/*     */ 
/*     */ import com.aliasi.chunk.Chunk;
/*     */ import com.aliasi.chunk.Chunker;
/*     */ import com.aliasi.chunk.Chunking;
/*     */ import com.aliasi.chunk.ConfidenceChunker;
/*     */ import com.aliasi.chunk.NBestChunker;
/*     */ import com.aliasi.chunk.TagChunkCodec;
/*     */ import com.aliasi.corpus.Corpus;
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.io.Reporter;
/*     */ import com.aliasi.io.Reporters;
/*     */ import com.aliasi.stats.AnnealingSchedule;
/*     */ import com.aliasi.stats.RegressionPrior;
/*     */ import com.aliasi.tag.ScoredTagging;
/*     */ import com.aliasi.tag.StringTagging;
/*     */ import com.aliasi.tag.TagLattice;
/*     */ import com.aliasi.tag.Tagging;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ChainCrfChunker
/*     */   implements Chunker, ConfidenceChunker, NBestChunker, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -2244399751558084581L;
/*     */   private final ChainCrf<String> mCrf;
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final TagChunkCodec mCodec;
/*     */   static final boolean ALLOW_UNSEEN_TAG_TRANSITIONS = false;
/*     */ 
/*     */   public ChainCrfChunker(ChainCrf<String> crf, TokenizerFactory tokenizerFactory, TagChunkCodec codec)
/*     */   {
/* 141 */     this.mCrf = crf;
/* 142 */     this.mTokenizerFactory = tokenizerFactory;
/* 143 */     this.mCodec = codec;
/*     */   }
/*     */ 
/*     */   public ChainCrf<String> crf()
/*     */   {
/* 152 */     return this.mCrf;
/*     */   }
/*     */ 
/*     */   public TagChunkCodec codec()
/*     */   {
/* 161 */     return this.mCodec;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/* 170 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 180 */     StringBuilder sb = new StringBuilder();
/* 181 */     sb.append("TagChunkCodec=" + codec());
/* 182 */     sb.append("\n");
/* 183 */     sb.append("Tokenizer Factory=" + tokenizerFactory());
/* 184 */     sb.append("\n");
/* 185 */     sb.append("CRF=\n");
/* 186 */     sb.append(crf().toString());
/* 187 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq) {
/* 191 */     char[] cs = Strings.toCharArray(cSeq);
/* 192 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end) {
/* 196 */     PreTagging preTagging = preTag(cs, start, end);
/* 197 */     List tokens = preTagging.mTokens;
/* 198 */     Tagging tagging = this.mCrf.tag(tokens);
/* 199 */     return toChunking(tagging, preTagging, cs, start, end, this.mCodec);
/*     */   }
/*     */ 
/*     */   public Iterator<ScoredObject<Chunking>> nBest(char[] cs, int start, int end, int maxResults)
/*     */   {
/* 204 */     PreTagging preTagging = preTag(cs, start, end);
/* 205 */     List tokens = preTagging.mTokens;
/* 206 */     Iterator it = this.mCrf.tagNBest(tokens, maxResults);
/*     */ 
/* 208 */     return new IteratorWrapper(it, preTagging, cs, start, end, this.mCodec);
/*     */   }
/*     */ 
/*     */   public Iterator<ScoredObject<Chunking>> nBestConditional(char[] cs, int start, int end, int maxResults)
/*     */   {
/* 234 */     PreTagging preTagging = preTag(cs, start, end);
/* 235 */     List tokens = preTagging.mTokens;
/* 236 */     Iterator it = this.mCrf.tagNBestConditional(tokens, maxResults);
/*     */ 
/* 238 */     return new IteratorWrapper(it, preTagging, cs, start, end, this.mCodec);
/*     */   }
/*     */ 
/*     */   public Iterator<Chunk> nBestChunks(char[] cs, int start, int end, int maxNBest)
/*     */   {
/* 243 */     PreTagging preTagging = preTag(cs, start, end);
/* 244 */     List tokens = preTagging.mTokens;
/* 245 */     TagLattice lattice = this.mCrf.tagMarginal(tokens);
/* 246 */     return this.mCodec.nBestChunks(lattice, preTagging.mTokenStarts, preTagging.mTokenEnds, maxNBest);
/*     */   }
/*     */ 
/*     */   PreTagging preTag(char[] cs, int start, int end)
/*     */   {
/* 253 */     List tokenStarts = new ArrayList();
/* 254 */     List tokenEnds = new ArrayList();
/* 255 */     List tokens = new ArrayList();
/* 256 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/*     */     String token;
/* 258 */     while ((token = tokenizer.nextToken()) != null) {
/* 259 */       tokens.add(token);
/* 260 */       tokenStarts.add(Integer.valueOf(tokenizer.lastTokenStartPosition()));
/* 261 */       tokenEnds.add(Integer.valueOf(tokenizer.lastTokenEndPosition()));
/*     */     }
/* 263 */     return new PreTagging(tokens, toArray(tokenStarts), toArray(tokenEnds));
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 269 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   public static ChainCrfChunker estimate(Corpus<ObjectHandler<Chunking>> chunkingCorpus, TagChunkCodec codec, TokenizerFactory tokenizerFactory, ChainCrfFeatureExtractor<String> featureExtractor, boolean addInterceptFeature, int minFeatureCount, boolean cacheFeatureVectors, RegressionPrior prior, int priorBlockSize, AnnealingSchedule annealingSchedule, double minImprovement, int minEpochs, int maxEpochs, Reporter reporter)
/*     */     throws IOException
/*     */   {
/* 329 */     if (reporter == null)
/* 330 */       reporter = Reporters.silent();
/* 331 */     reporter.info("Training chain CRF chunker");
/* 332 */     reporter.info("Converting chunk corpus to tag corpus using codec.");
/* 333 */     Corpus taggingCorpus = new TagCorpus(chunkingCorpus, codec);
/*     */ 
/* 335 */     ChainCrf crf = ChainCrf.estimate(taggingCorpus, featureExtractor, addInterceptFeature, minFeatureCount, cacheFeatureVectors, false, prior, priorBlockSize, annealingSchedule, minImprovement, minEpochs, maxEpochs, reporter);
/*     */ 
/* 349 */     return new ChainCrfChunker(crf, tokenizerFactory, codec);
/*     */   }
/*     */ 
/*     */   static Chunking toChunking(Tagging<String> tagging, PreTagging preTagging, char[] cs, int start, int end, TagChunkCodec codec)
/*     */   {
/* 355 */     String s = new String(cs, start, end - start);
/* 356 */     List tokens = preTagging.mTokens;
/* 357 */     int[] tokenStarts = preTagging.mTokenStarts;
/* 358 */     int[] tokenEnds = preTagging.mTokenEnds;
/* 359 */     List tags = tagging.tags();
/*     */ 
/* 361 */     StringTagging stringTagging = new StringTagging(tokens, tags, s, tokenStarts, tokenEnds);
/*     */ 
/* 363 */     return codec.toChunking(stringTagging);
/*     */   }
/*     */ 
/*     */   static int[] toArray(List<Integer> xs) {
/* 367 */     int len = xs.size();
/* 368 */     int[] ys = new int[len];
/* 369 */     for (int i = 0; i < len; i++)
/* 370 */       ys[i] = ((Integer)xs.get(i)).intValue();
/* 371 */     return ys;
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 2460314741682974199L;
/*     */     private final ChainCrfChunker mChunker;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 458 */       this(null);
/*     */     }
/*     */     public Serializer(ChainCrfChunker chunker) {
/* 461 */       this.mChunker = chunker;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 465 */       ChainCrf crf = (ChainCrf)in.readObject();
/*     */ 
/* 467 */       TokenizerFactory factory = (TokenizerFactory)in.readObject();
/*     */ 
/* 469 */       TagChunkCodec codec = (TagChunkCodec)in.readObject();
/* 470 */       return new ChainCrfChunker(crf, factory, codec);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 474 */       out.writeObject(this.mChunker.mCrf);
/* 475 */       out.writeObject(this.mChunker.mTokenizerFactory);
/* 476 */       out.writeObject(this.mChunker.mCodec);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class IteratorWrapper
/*     */     implements Iterator<ScoredObject<Chunking>>
/*     */   {
/*     */     private final Iterator<ScoredTagging<String>> mIt;
/*     */     private final ChainCrfChunker.PreTagging mPreTagging;
/*     */     private final char[] mCs;
/*     */     private final int mStart;
/*     */     private final int mEnd;
/*     */     private final TagChunkCodec mCodec;
/*     */ 
/*     */     IteratorWrapper(Iterator<ScoredTagging<String>> it, ChainCrfChunker.PreTagging preTagging, char[] cs, int start, int end, TagChunkCodec codec)
/*     */     {
/* 432 */       this.mIt = it;
/* 433 */       this.mPreTagging = preTagging;
/* 434 */       this.mCs = cs;
/* 435 */       this.mStart = start;
/* 436 */       this.mEnd = end;
/* 437 */       this.mCodec = codec;
/*     */     }
/*     */     public boolean hasNext() {
/* 440 */       return this.mIt.hasNext();
/*     */     }
/*     */     public void remove() {
/* 443 */       this.mIt.remove();
/*     */     }
/*     */     public ScoredObject<Chunking> next() {
/* 446 */       ScoredTagging tagging = (ScoredTagging)this.mIt.next();
/* 447 */       double score = tagging.score();
/* 448 */       Chunking chunking = ChainCrfChunker.toChunking(tagging, this.mPreTagging, this.mCs, this.mStart, this.mEnd, this.mCodec);
/*     */ 
/* 450 */       return new ScoredObject(chunking, score);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TagCorpus extends Corpus<ObjectHandler<Tagging<String>>>
/*     */   {
/*     */     private final Corpus<ObjectHandler<Chunking>> mChunkingCorpus;
/*     */     private final TagChunkCodec mCodec;
/*     */ 
/*     */     public TagCorpus(Corpus<ObjectHandler<Chunking>> chunkingCorpus, TagChunkCodec codec)
/*     */     {
/* 406 */       this.mChunkingCorpus = chunkingCorpus;
/* 407 */       this.mCodec = codec;
/*     */     }
/*     */ 
/*     */     public void visitTrain(ObjectHandler<Tagging<String>> handler) throws IOException
/*     */     {
/* 412 */       this.mChunkingCorpus.visitTrain(new ChainCrfChunker.ChunkingAdapter(handler, this.mCodec));
/*     */     }
/*     */ 
/*     */     public void visitTest(ObjectHandler<Tagging<String>> handler) throws IOException
/*     */     {
/* 417 */       this.mChunkingCorpus.visitTest(new ChainCrfChunker.ChunkingAdapter(handler, this.mCodec));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ChunkingAdapter
/*     */     implements ObjectHandler<Chunking>
/*     */   {
/*     */     private final ObjectHandler<Tagging<String>> mTagHandler;
/*     */     private final TagChunkCodec mCodec;
/*     */ 
/*     */     public ChunkingAdapter(ObjectHandler<Tagging<String>> tagHandler, TagChunkCodec codec)
/*     */     {
/* 392 */       this.mTagHandler = tagHandler;
/* 393 */       this.mCodec = codec;
/*     */     }
/*     */     public void handle(Chunking chunking) {
/* 396 */       Tagging tagging = this.mCodec.toTagging(chunking);
/* 397 */       this.mTagHandler.handle(tagging);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class PreTagging
/*     */   {
/*     */     final List<String> mTokens;
/*     */     final int[] mTokenStarts;
/*     */     final int[] mTokenEnds;
/*     */ 
/*     */     public PreTagging(List<String> tokens, int[] tokenStarts, int[] tokenEnds)
/*     */     {
/* 381 */       this.mTokens = tokens;
/* 382 */       this.mTokenStarts = tokenStarts;
/* 383 */       this.mTokenEnds = tokenEnds;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.crf.ChainCrfChunker
 * JD-Core Version:    0.6.2
 */