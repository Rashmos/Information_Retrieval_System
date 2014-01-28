/*     */ package com.aliasi.sentences;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class AbstractSentenceModel
/*     */   implements SentenceModel
/*     */ {
/*     */   public int[] boundaryIndices(String[] tokens, String[] whitespaces)
/*     */   {
/*  69 */     verifyTokensWhitespaces(tokens, whitespaces);
/*  70 */     return boundaryIndices(tokens, whitespaces, 0, tokens.length);
/*     */   }
/*     */ 
/*     */   public int[] boundaryIndices(String[] tokens, String[] whitespaces, int start, int length)
/*     */   {
/*  94 */     verifyBounds(tokens, whitespaces, start, length);
/*  95 */     List boundaries = new ArrayList();
/*  96 */     boundaryIndices(tokens, whitespaces, start, length, boundaries);
/*  97 */     int[] result = new int[boundaries.size()];
/*  98 */     for (int i = 0; i < result.length; i++)
/*  99 */       result[i] = ((Integer)boundaries.get(i)).intValue();
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */   public abstract void boundaryIndices(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt1, int paramInt2, Collection<Integer> paramCollection);
/*     */ 
/*     */   protected static void verifyBounds(String[] tokens, String[] whitespaces, int start, int length)
/*     */   {
/* 147 */     if (tokens.length < start + length) {
/* 148 */       String msg = "Token array must be at least as long as start+length. tokens.length=" + tokens.length + " start=" + start + " length=" + length;
/*     */ 
/* 152 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 154 */     if (whitespaces.length <= start + length) {
/* 155 */       String msg = "Whitespace array must be longer than start+length. whitespaces.length=" + whitespaces.length + " start=" + start + " length=" + length;
/*     */ 
/* 159 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static void verifyTokensWhitespaces(String[] tokens, String[] whitespaces)
/*     */   {
/* 175 */     if (whitespaces.length == tokens.length + 1) return;
/* 176 */     String msg = "Whitespaces array must be one longer than tokens. Found tokens.length=" + tokens.length + " whitespaces.length=" + whitespaces.length;
/*     */ 
/* 179 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.sentences.AbstractSentenceModel
 * JD-Core Version:    0.6.2
 */