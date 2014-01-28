/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.lm.LanguageModel.Process;
/*     */ import com.aliasi.lm.LanguageModel.Sequence;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class AbstractCharLmRescoringChunker<B extends NBestChunker, O extends LanguageModel.Process, C extends LanguageModel.Sequence> extends RescoringChunker<B>
/*     */ {
/*     */   final Map<String, Character> mTypeToChar;
/*     */   final Map<String, C> mTypeToLM;
/*     */   final O mOutLM;
/*     */   static final char UNKNOWN_TYPE_CHAR = '￿';
/*     */   static final char BOS_CHAR = '￾';
/*     */   static final char EOS_CHAR = '�';
/*     */ 
/*     */   public AbstractCharLmRescoringChunker(B baseNBestChunker, int numChunkingsRescored, O outLM, Map<String, Character> typeToChar, Map<String, C> typeToLM)
/*     */   {
/* 129 */     super(baseNBestChunker, numChunkingsRescored);
/* 130 */     this.mOutLM = outLM;
/* 131 */     this.mTypeToChar = typeToChar;
/* 132 */     this.mTypeToLM = typeToLM;
/*     */   }
/*     */ 
/*     */   public char typeToChar(String chunkType)
/*     */   {
/* 146 */     Character result = (Character)this.mTypeToChar.get(chunkType);
/* 147 */     if (result == null)
/* 148 */       return 65535;
/* 149 */     return result.charValue();
/*     */   }
/*     */ 
/*     */   public O outLM()
/*     */   {
/* 160 */     return this.mOutLM;
/*     */   }
/*     */ 
/*     */   public C chunkLM(String chunkType)
/*     */   {
/* 171 */     return (LanguageModel.Sequence)this.mTypeToLM.get(chunkType);
/*     */   }
/*     */ 
/*     */   public double rescore(Chunking chunking)
/*     */   {
/* 184 */     String text = chunking.charSequence().toString();
/* 185 */     double logProb = 0.0D;
/* 186 */     int pos = 0;
/* 187 */     char prevTagChar = 65534;
/* 188 */     for (Chunk chunk : orderedSet(chunking)) {
/* 189 */       int start = chunk.start();
/* 190 */       int end = chunk.end();
/* 191 */       String chunkType = chunk.type();
/* 192 */       char tagChar = typeToChar(chunkType);
/* 193 */       logProb += outLMEstimate(text.substring(pos, start), prevTagChar, tagChar);
/*     */ 
/* 196 */       if (this.mTypeToLM.get(chunkType) == null) {
/* 197 */         System.out.println("\nFound null lm for type=" + chunkType + " Full type set =" + this.mTypeToLM.keySet());
/*     */ 
/* 199 */         System.out.println("Chunking=" + chunking);
/*     */       }
/*     */ 
/* 202 */       logProb += typeLMEstimate(chunkType, text.substring(start, end));
/* 203 */       pos = end;
/* 204 */       prevTagChar = tagChar;
/*     */     }
/* 206 */     logProb += outLMEstimate(text.substring(pos), prevTagChar, 65533);
/*     */ 
/* 208 */     return logProb;
/*     */   }
/*     */ 
/*     */   double typeLMEstimate(String type, String text)
/*     */   {
/* 213 */     LanguageModel.Sequence lm = (LanguageModel.Sequence)this.mTypeToLM.get(type);
/* 214 */     if (lm == null) {
/* 215 */       String msg = "Found null lm for type=" + type + " Full type set =" + this.mTypeToLM.keySet();
/*     */ 
/* 217 */       System.out.println("TypeLM Estimate:\n" + msg);
/* 218 */       return -16.0D * text.length();
/*     */     }
/* 220 */     double estimate = lm.log2Estimate(text);
/* 221 */     return estimate;
/*     */   }
/*     */ 
/*     */   double outLMEstimate(String text, char prevTagChar, char nextTagChar)
/*     */   {
/* 227 */     String seq = prevTagChar + text + nextTagChar;
/* 228 */     String start = seq.substring(0, 1);
/* 229 */     double estimate = this.mOutLM.log2Estimate(seq) - this.mOutLM.log2Estimate(start);
/*     */ 
/* 231 */     return estimate;
/*     */   }
/*     */ 
/*     */   static char[] wrapText(String text, char prevTagChar, char nextTagChar) {
/* 235 */     char[] cs = new char[text.length() + 2];
/* 236 */     cs[0] = prevTagChar;
/* 237 */     cs[(cs.length - 1)] = nextTagChar;
/* 238 */     for (int i = 0; i < text.length(); i++)
/* 239 */       cs[(i + 1)] = text.charAt(i);
/* 240 */     return cs;
/*     */   }
/*     */ 
/*     */   static Set<Chunk> orderedSet(Chunking chunking) {
/* 244 */     Set orderedChunkSet = new TreeSet(Chunk.TEXT_ORDER_COMPARATOR);
/* 245 */     orderedChunkSet.addAll(chunking.chunkSet());
/* 246 */     return orderedChunkSet;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.AbstractCharLmRescoringChunker
 * JD-Core Version:    0.6.2
 */