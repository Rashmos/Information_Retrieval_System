/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.lm.CompiledNGramProcessLM;
/*     */ import com.aliasi.lm.NGramProcessLM;
/*     */ import com.aliasi.lm.TrieCharSeqCounter;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TrainSpellChecker
/*     */   implements ObjectHandler<CharSequence>, Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -3599682964675009111L;
/*     */   private final WeightedEditDistance mEditDistance;
/*     */   private final NGramProcessLM mLM;
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final ObjectToCounterMap<String> mTokenCounter;
/* 139 */   private long mNumTrainingChars = 0L;
/*     */ 
/*     */   private TrainSpellChecker(long numTrainingChars, WeightedEditDistance editDistance, NGramProcessLM lm, TokenizerFactory tokenizerFactory, ObjectToCounterMap<String> tokenCounter)
/*     */   {
/* 146 */     this.mNumTrainingChars = numTrainingChars;
/* 147 */     this.mEditDistance = editDistance;
/* 148 */     this.mLM = lm;
/* 149 */     this.mTokenizerFactory = tokenizerFactory;
/* 150 */     this.mTokenCounter = tokenCounter;
/*     */   }
/*     */ 
/*     */   public TrainSpellChecker(NGramProcessLM lm, WeightedEditDistance editDistance)
/*     */   {
/* 166 */     this(lm, editDistance, null);
/*     */   }
/*     */ 
/*     */   public TrainSpellChecker(NGramProcessLM lm, WeightedEditDistance editDistance, TokenizerFactory tokenizerFactory)
/*     */   {
/* 188 */     this.mLM = lm;
/* 189 */     this.mTokenizerFactory = tokenizerFactory;
/* 190 */     this.mEditDistance = editDistance;
/* 191 */     this.mTokenCounter = new ObjectToCounterMap();
/*     */   }
/*     */ 
/*     */   public NGramProcessLM languageModel()
/*     */   {
/* 206 */     return this.mLM;
/*     */   }
/*     */ 
/*     */   public WeightedEditDistance editDistance()
/*     */   {
/* 220 */     return this.mEditDistance;
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap<String> tokenCounter()
/*     */   {
/* 234 */     return this.mTokenCounter;
/*     */   }
/*     */ 
/*     */   public void train(CharSequence cSeq, int count)
/*     */   {
/* 259 */     if (count < 0) {
/* 260 */       String msg = "Training counts must be non-negative. Found count=" + count;
/*     */ 
/* 262 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 264 */     if (count == 0) return;
/* 265 */     this.mLM.train(normalizeQuery(cSeq), count);
/* 266 */     this.mNumTrainingChars += count * cSeq.length();
/*     */   }
/*     */ 
/*     */   public long numTrainingChars()
/*     */   {
/* 276 */     return this.mNumTrainingChars;
/*     */   }
/*     */ 
/*     */   public void handle(CharSequence cSeq)
/*     */   {
/* 290 */     this.mLM.train(normalizeQuery(cSeq));
/* 291 */     this.mNumTrainingChars += cSeq.length();
/*     */   }
/*     */ 
/*     */   public void pruneTokens(int minCount)
/*     */   {
/* 303 */     this.mTokenCounter.prune(minCount);
/*     */   }
/*     */ 
/*     */   public void pruneLM(int minCount)
/*     */   {
/* 313 */     this.mLM.substringCounter().prune(minCount);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 326 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 331 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   StringBuilder normalizeQuery(CharSequence cSeq) {
/* 335 */     StringBuilder sb = new StringBuilder();
/* 336 */     sb.append(' ');
/* 337 */     if (this.mTokenizerFactory == null) {
/* 338 */       Strings.normalizeWhitespace(cSeq, sb);
/* 339 */       sb.append(' ');
/*     */     } else {
/* 341 */       char[] cs = Strings.toCharArray(cSeq);
/* 342 */       Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*     */       String nextToken;
/* 344 */       while ((nextToken = tokenizer.nextToken()) != null) {
/* 345 */         this.mTokenCounter.increment(nextToken);
/* 346 */         sb.append(nextToken);
/* 347 */         sb.append(' ');
/*     */       }
/*     */     }
/* 350 */     return sb;
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -8575906929649837646L;
/*     */     private TrainSpellChecker mTrainer;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 401 */       this(null);
/*     */     }
/*     */     public Serializer(TrainSpellChecker trainer) {
/* 404 */       this.mTrainer = trainer;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 408 */       objOut.writeLong(this.mTrainer.mNumTrainingChars);
/* 409 */       objOut.writeObject(this.mTrainer.mLM);
/* 410 */       boolean tokenizing = this.mTrainer.mTokenizerFactory != null;
/* 411 */       objOut.writeBoolean(tokenizing);
/* 412 */       if (tokenizing) {
/* 413 */         AbstractExternalizable.serializeOrCompile(this.mTrainer.mTokenizerFactory, objOut);
/* 414 */         objOut.writeObject(this.mTrainer.mTokenCounter);
/*     */       }
/* 416 */       AbstractExternalizable.serializeOrCompile(this.mTrainer.mEditDistance, objOut);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 422 */       long numTrainingChars = objIn.readLong();
/* 423 */       NGramProcessLM lm = (NGramProcessLM)objIn.readObject();
/* 424 */       boolean tokenizing = objIn.readBoolean();
/* 425 */       TokenizerFactory tokenizerFactory = null;
/* 426 */       ObjectToCounterMap tokenCounter = null;
/* 427 */       if (tokenizing) {
/* 428 */         tokenizerFactory = (TokenizerFactory)objIn.readObject();
/*     */ 
/* 431 */         ObjectToCounterMap tempTokenCounter = (ObjectToCounterMap)objIn.readObject();
/* 432 */         tokenCounter = tempTokenCounter;
/*     */       }
/* 434 */       WeightedEditDistance editDistance = (WeightedEditDistance)objIn.readObject();
/*     */ 
/* 436 */       return new TrainSpellChecker(numTrainingChars, editDistance, lm, tokenizerFactory, tokenCounter, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = 4907338741905144267L;
/*     */     private final TrainSpellChecker mTrainer;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 357 */       this(null);
/*     */     }
/*     */     public Externalizer(TrainSpellChecker trainer) {
/* 360 */       this.mTrainer = trainer;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 364 */       this.mTrainer.mLM.compileTo(objOut);
/* 365 */       boolean tokenizing = this.mTrainer.mTokenizerFactory != null;
/* 366 */       objOut.writeBoolean(tokenizing);
/* 367 */       if (tokenizing) {
/* 368 */         Set keySet = this.mTrainer.mTokenCounter.keySet();
/* 369 */         objOut.writeObject(new HashSet(keySet));
/*     */       }
/* 371 */       AbstractExternalizable.compileOrSerialize(this.mTrainer.mEditDistance, objOut);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 377 */       CompiledNGramProcessLM lm = (CompiledNGramProcessLM)objIn.readObject();
/*     */ 
/* 379 */       boolean tokenizing = objIn.readBoolean();
/*     */ 
/* 381 */       Set tokenSet = null;
/* 382 */       if (tokenizing)
/*     */       {
/* 385 */         Set tempTokenSet = (Set)objIn.readObject();
/* 386 */         tokenSet = tempTokenSet;
/*     */       }
/*     */ 
/* 389 */       WeightedEditDistance editDistance = (WeightedEditDistance)objIn.readObject();
/*     */ 
/* 391 */       return new CompiledSpellChecker(lm, editDistance, tokenSet);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.TrainSpellChecker
 * JD-Core Version:    0.6.2
 */