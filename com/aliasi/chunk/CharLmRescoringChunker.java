/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.hmm.HmmCharLmEstimator;
/*     */ import com.aliasi.lm.LanguageModel.Process;
/*     */ import com.aliasi.lm.LanguageModel.Sequence;
/*     */ import com.aliasi.lm.NGramBoundaryLM;
/*     */ import com.aliasi.lm.NGramProcessLM;
/*     */ import com.aliasi.lm.TrieCharSeqCounter;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CharLmRescoringChunker extends AbstractCharLmRescoringChunker<CharLmHmmChunker, NGramProcessLM, NGramBoundaryLM>
/*     */   implements ObjectHandler<Chunking>, Compilable
/*     */ {
/*     */   final int mNGram;
/*     */   final int mNumChars;
/*     */   final double mInterpolationRatio;
/* 111 */   char mNextCodeChar = 65532;
/*     */ 
/*     */   public CharLmRescoringChunker(TokenizerFactory tokenizerFactory, int numChunkingsRescored, int nGram, int numChars, double interpolationRatio)
/*     */   {
/* 133 */     super(new CharLmHmmChunker(tokenizerFactory, new HmmCharLmEstimator(nGram, numChars, interpolationRatio)), numChunkingsRescored, new NGramProcessLM(nGram, numChars, interpolationRatio), new HashMap(), new HashMap());
/*     */ 
/* 141 */     this.mNGram = nGram;
/* 142 */     this.mNumChars = numChars;
/* 143 */     this.mInterpolationRatio = interpolationRatio;
/*     */   }
/*     */ 
/*     */   public CharLmRescoringChunker(TokenizerFactory tokenizerFactory, int numChunkingsRescored, int nGram, int numChars, double interpolationRatio, boolean smoothTags)
/*     */   {
/* 172 */     super(new CharLmHmmChunker(tokenizerFactory, new HmmCharLmEstimator(nGram, numChars, interpolationRatio), smoothTags), numChunkingsRescored, new NGramProcessLM(nGram, numChars, interpolationRatio), new HashMap(), new HashMap());
/*     */ 
/* 181 */     this.mNGram = nGram;
/* 182 */     this.mNumChars = numChars;
/* 183 */     this.mInterpolationRatio = interpolationRatio;
/*     */   }
/*     */ 
/*     */   public void handle(Chunking chunking)
/*     */   {
/* 193 */     ObjectHandler handler2 = (ObjectHandler)baseChunker();
/* 194 */     handler2.handle(chunking);
/*     */ 
/* 197 */     String text = chunking.charSequence().toString();
/* 198 */     char prevTagChar = 65534;
/* 199 */     int pos = 0;
/* 200 */     for (Chunk chunk : orderedSet(chunking)) {
/* 201 */       int start = chunk.start();
/* 202 */       int end = chunk.end();
/* 203 */       String chunkType = chunk.type();
/* 204 */       createTypeIfNecessary(chunkType);
/* 205 */       char tagChar = typeToChar(chunkType);
/* 206 */       trainOutLM(text.substring(pos, start), prevTagChar, tagChar);
/*     */ 
/* 208 */       trainTypeLM(chunkType, text.substring(start, end));
/* 209 */       pos = end;
/* 210 */       prevTagChar = tagChar;
/*     */     }
/* 212 */     trainOutLM(text.substring(pos), prevTagChar, 65533);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 228 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public void trainDictionary(CharSequence cSeq, String type)
/*     */   {
/* 253 */     ((CharLmHmmChunker)baseChunker()).trainDictionary(cSeq, type);
/* 254 */     trainTypeLM(type, cSeq);
/*     */   }
/*     */ 
/*     */   public void trainOut(CharSequence cSeq)
/*     */   {
/* 272 */     ((NGramProcessLM)outLM()).train(cSeq);
/*     */   }
/*     */ 
/*     */   void createTypeIfNecessary(String chunkType)
/*     */   {
/* 277 */     if (this.mTypeToChar.containsKey(chunkType)) return;
/* 278 */     Character c = Character.valueOf(this.mNextCodeChar--);
/* 279 */     this.mTypeToChar.put(chunkType, c);
/* 280 */     NGramBoundaryLM lm = new NGramBoundaryLM(this.mNGram, this.mNumChars, this.mInterpolationRatio, 65535);
/*     */ 
/* 283 */     this.mTypeToLM.put(chunkType, lm);
/*     */   }
/*     */ 
/*     */   void trainTypeLM(String type, CharSequence text)
/*     */   {
/* 288 */     createTypeIfNecessary(type);
/* 289 */     NGramBoundaryLM lm = (NGramBoundaryLM)this.mTypeToLM.get(type);
/* 290 */     lm.train(text);
/*     */   }
/*     */ 
/*     */   void trainOutLM(String text, char prevTagChar, char nextTagChar)
/*     */   {
/* 295 */     String trainSeq = prevTagChar + text + nextTagChar;
/* 296 */     ((NGramProcessLM)outLM()).train(trainSeq);
/* 297 */     ((NGramProcessLM)outLM()).substringCounter().decrementUnigram(prevTagChar);
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     private static final long serialVersionUID = 3555143657918695241L;
/*     */     final CharLmRescoringChunker mChunker;
/*     */ 
/* 304 */     public Externalizer() { this(null); }
/*     */ 
/*     */     public Externalizer(CharLmRescoringChunker chunker) {
/* 307 */       this.mChunker = chunker;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut)
/*     */       throws IOException
/*     */     {
/* 316 */       ((CharLmHmmChunker)this.mChunker.baseChunker()).compileTo(objOut);
/* 317 */       objOut.writeInt(this.mChunker.numChunkingsRescored());
/* 318 */       String[] types = (String[])this.mChunker.mTypeToLM.keySet().toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 320 */       objOut.writeInt(types.length);
/* 321 */       for (int i = 0; i < types.length; i++) {
/* 322 */         objOut.writeUTF(types[i]);
/* 323 */         objOut.writeChar(this.mChunker.typeToChar(types[i]));
/* 324 */         NGramBoundaryLM lm = (NGramBoundaryLM)this.mChunker.mTypeToLM.get(types[i]);
/*     */ 
/* 326 */         lm.compileTo(objOut);
/*     */       }
/* 328 */       ((NGramProcessLM)this.mChunker.outLM()).compileTo(objOut);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 334 */       NBestChunker baseChunker = (NBestChunker)in.readObject();
/* 335 */       int numChunkingsRescored = in.readInt();
/* 336 */       int numTypes = in.readInt();
/* 337 */       Map typeToChar = new HashMap();
/* 338 */       Map typeToLM = new HashMap();
/* 339 */       for (int i = 0; i < numTypes; i++) {
/* 340 */         String type = in.readUTF();
/* 341 */         char c = in.readChar();
/* 342 */         LanguageModel.Sequence lm = (LanguageModel.Sequence)in.readObject();
/*     */ 
/* 344 */         typeToChar.put(type, Character.valueOf(c));
/* 345 */         typeToLM.put(type, lm);
/*     */       }
/* 347 */       LanguageModel.Process outLM = (LanguageModel.Process)in.readObject();
/*     */ 
/* 349 */       return new AbstractCharLmRescoringChunker(baseChunker, numChunkingsRescored, outLM, typeToChar, typeToLM);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.CharLmRescoringChunker
 * JD-Core Version:    0.6.2
 */