/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class CompiledTokenizedLM
/*     */   implements LanguageModel.Sequence, LanguageModel.Tokenized
/*     */ {
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final SymbolTable mSymbolTable;
/*     */   private final LanguageModel.Sequence mUnknownTokenModel;
/*     */   private final LanguageModel.Sequence mWhitespaceModel;
/*     */   private final int mMaxNGram;
/*     */   private final int[] mTokens;
/*     */   private final float[] mLogProbs;
/*     */   private final float[] mLogLambdas;
/*     */   private final int[] mFirstChild;
/*     */ 
/*     */   CompiledTokenizedLM(ObjectInput in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  64 */     String tokenizerClassName = in.readUTF();
/*  65 */     if (tokenizerClassName.equals(""))
/*  66 */       this.mTokenizerFactory = ((TokenizerFactory)in.readObject());
/*     */     else {
/*     */       try
/*     */       {
/*  70 */         Class tokenizerClass = Class.forName(tokenizerClassName);
/*     */ 
/*  72 */         Constructor tokCons = tokenizerClass.getConstructor(new Class[0]);
/*     */ 
/*  74 */         this.mTokenizerFactory = ((TokenizerFactory)tokCons.newInstance(new Object[0]));
/*     */       }
/*     */       catch (NoSuchMethodException e) {
/*  77 */         throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
/*     */       }
/*     */       catch (InstantiationException e) {
/*  80 */         throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
/*     */       }
/*     */       catch (IllegalAccessException e) {
/*  83 */         throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
/*     */       }
/*     */       catch (InvocationTargetException e) {
/*  86 */         throw new ClassNotFoundException("Constructing " + tokenizerClassName, e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  91 */     this.mSymbolTable = ((SymbolTable)in.readObject());
/*     */ 
/*  93 */     this.mUnknownTokenModel = ((LanguageModel.Tokenized)in.readObject());
/*     */ 
/*  96 */     this.mWhitespaceModel = ((LanguageModel.Tokenized)in.readObject());
/*     */ 
/*  99 */     this.mMaxNGram = in.readInt();
/*     */ 
/* 101 */     int numNodes = in.readInt();
/* 102 */     int lastInternalNodeIndex = in.readInt();
/* 103 */     this.mTokens = new int[numNodes];
/* 104 */     this.mLogProbs = new float[numNodes];
/* 105 */     this.mLogLambdas = new float[lastInternalNodeIndex + 1];
/* 106 */     this.mFirstChild = new int[lastInternalNodeIndex + 2];
/* 107 */     this.mFirstChild[(this.mFirstChild.length - 1)] = numNodes;
/* 108 */     for (int i = 0; i < numNodes; i++) {
/* 109 */       this.mTokens[i] = in.readInt();
/* 110 */       this.mLogProbs[i] = in.readFloat();
/* 111 */       if (i <= lastInternalNodeIndex) {
/* 112 */         this.mLogLambdas[i] = in.readFloat();
/* 113 */         this.mFirstChild[i] = in.readInt();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 130 */     StringBuilder sb = new StringBuilder();
/* 131 */     sb.append("Tokenizer Class Name=" + this.mTokenizerFactory);
/* 132 */     sb.append('\n');
/* 133 */     sb.append("Symbol Table=" + this.mSymbolTable);
/* 134 */     sb.append('\n');
/* 135 */     sb.append("Unknown Token Model=" + this.mUnknownTokenModel);
/* 136 */     sb.append('\n');
/* 137 */     sb.append("Whitespace Model=" + this.mWhitespaceModel);
/* 138 */     sb.append('\n');
/* 139 */     sb.append("Token Trie");
/* 140 */     sb.append('\n');
/* 141 */     sb.append("Nodes=" + this.mTokens.length + " Internal=" + this.mLogLambdas.length);
/* 142 */     sb.append('\n');
/* 143 */     sb.append("Index Tok logP firstDtr log(1-L)");
/* 144 */     sb.append('\n');
/* 145 */     for (int i = 0; i < this.mTokens.length; i++) {
/* 146 */       sb.append(i);
/*     */ 
/* 148 */       sb.append('\t');
/* 149 */       sb.append(this.mTokens[i]);
/* 150 */       sb.append('\t');
/* 151 */       sb.append(this.mLogProbs[i]);
/* 152 */       if (i < this.mFirstChild.length) {
/* 153 */         sb.append('\t');
/* 154 */         sb.append(this.mFirstChild[i]);
/* 155 */         if (i < this.mLogLambdas.length) {
/* 156 */           sb.append('\t');
/* 157 */           sb.append(this.mLogLambdas[i]);
/*     */         }
/*     */       }
/* 160 */       sb.append('\n');
/*     */     }
/* 162 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public double log2Estimate(CharSequence cSeq)
/*     */   {
/* 167 */     char[] cs = Strings.toCharArray(cSeq);
/* 168 */     return log2Estimate(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public double log2Estimate(char[] cs, int start, int end) {
/* 172 */     Strings.checkArgsStartEnd(cs, start, end);
/* 173 */     double logEstimate = 0.0D;
/*     */ 
/* 175 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/* 176 */     List tokenList = new ArrayList();
/*     */     while (true) {
/* 178 */       String whitespace = tokenizer.nextWhitespace();
/* 179 */       logEstimate += this.mWhitespaceModel.log2Estimate(whitespace);
/* 180 */       String token = tokenizer.nextToken();
/* 181 */       if (token == null) break;
/* 182 */       tokenList.add(token);
/*     */     }
/*     */ 
/* 186 */     int[] tokIds = new int[tokenList.size() + 2];
/* 187 */     tokIds[0] = -2;
/* 188 */     tokIds[(tokIds.length - 1)] = -2;
/* 189 */     Iterator it = tokenList.iterator();
/* 190 */     for (int i = 1; it.hasNext(); i++) {
/* 191 */       String token = (String)it.next();
/* 192 */       tokIds[i] = this.mSymbolTable.symbolToID(token);
/* 193 */       if (tokIds[i] < 0) {
/* 194 */         logEstimate += this.mUnknownTokenModel.log2Estimate(token);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 199 */     for (int i = 2; i <= tokIds.length; i++) {
/* 200 */       logEstimate += conditionalTokenEstimate(tokIds, 0, i);
/*     */     }
/* 202 */     return logEstimate;
/*     */   }
/*     */ 
/*     */   private double conditionalTokenEstimate(int[] tokIds, int start, int end) {
/* 206 */     double estimate = 0.0D;
/* 207 */     int contextEnd = end - 1;
/* 208 */     int tokId = tokIds[contextEnd];
/* 209 */     int maxContextLength = Math.min(contextEnd - start, this.mMaxNGram - 1);
/* 210 */     for (int contextLength = maxContextLength; 
/* 211 */       contextLength >= 0; 
/* 212 */       contextLength--)
/*     */     {
/* 214 */       int contextStart = contextEnd - contextLength;
/* 215 */       int contextIndex = getIndex(tokIds, contextStart, contextEnd);
/* 216 */       if (contextIndex != -1)
/* 217 */         if (tokId == -1) {
/* 218 */           if (hasDtrs(contextIndex))
/* 219 */             estimate += this.mLogLambdas[contextIndex];
/*     */         }
/*     */         else {
/* 222 */           int outcomeIndex = getIndex(contextIndex, tokId);
/* 223 */           if (outcomeIndex != -1)
/* 224 */             return estimate + this.mLogProbs[outcomeIndex];
/* 225 */           if (hasDtrs(contextIndex))
/* 226 */             estimate += this.mLogLambdas[contextIndex];
/*     */         }
/*     */     }
/* 229 */     return estimate;
/*     */   }
/*     */ 
/*     */   public double tokenLog2Probability(String[] tokens, int start, int end) {
/* 233 */     int[] tokIds = new int[tokens.length];
/* 234 */     for (int i = 0; i < tokens.length; i++)
/* 235 */       tokIds[i] = this.mSymbolTable.symbolToID(tokens[i]);
/* 236 */     double sum = 0.0D;
/* 237 */     for (int i = start + 1; i <= end; i++)
/* 238 */       sum += conditionalTokenEstimate(tokIds, start, i);
/* 239 */     return sum;
/*     */   }
/*     */ 
/*     */   public double tokenProbability(String[] tokens, int start, int end) {
/* 243 */     return Math.pow(2.0D, tokenLog2Probability(tokens, start, end));
/*     */   }
/*     */ 
/*     */   boolean hasDtrs(int contextIndex)
/*     */   {
/* 254 */     return (contextIndex < this.mLogLambdas.length) && (!Double.isNaN(this.mLogLambdas[contextIndex]));
/*     */   }
/*     */ 
/*     */   private int getIndex(int fromIndex, int tokId)
/*     */   {
/* 260 */     if (fromIndex + 1 >= this.mFirstChild.length) return -1;
/* 261 */     int low = this.mFirstChild[fromIndex];
/* 262 */     int high = this.mFirstChild[(fromIndex + 1)] - 1;
/* 263 */     while (low <= high) {
/* 264 */       int mid = (high + low) / 2;
/* 265 */       if (this.mTokens[mid] == tokId) {
/* 266 */         return mid;
/*     */       }
/* 268 */       if (this.mTokens[mid] < tokId)
/* 269 */         low = low == mid ? mid + 1 : mid;
/*     */       else
/* 271 */         high = high == mid ? mid - 1 : mid;
/*     */     }
/* 273 */     return -1;
/*     */   }
/*     */ 
/*     */   private int getIndex(int[] tokIds, int start, int end) {
/* 277 */     int index = 0;
/* 278 */     for (int currentStart = start; 
/* 279 */       currentStart < end; 
/* 280 */       currentStart++) {
/* 281 */       index = getIndex(index, tokIds[currentStart]);
/* 282 */       if (index == -1) return -1;
/*     */     }
/* 284 */     return index;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.CompiledTokenizedLM
 * JD-Core Version:    0.6.2
 */