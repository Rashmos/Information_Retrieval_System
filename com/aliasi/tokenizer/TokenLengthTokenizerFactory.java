/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TokenLengthTokenizerFactory extends ModifyTokenTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -431337104035802336L;
/*     */   private final int mShortestTokenLength;
/*     */   private final int mLongestTokenLength;
/*     */ 
/*     */   public TokenLengthTokenizerFactory(TokenizerFactory factory, int shortestTokenLength, int longestTokenLength)
/*     */   {
/*  70 */     super(factory);
/*  71 */     if (shortestTokenLength < 0) {
/*  72 */       String msg = "Shortest token length must be non-negative. Found shortestTokenLength=" + shortestTokenLength;
/*     */ 
/*  74 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  76 */     if (shortestTokenLength > longestTokenLength) {
/*  77 */       String msg = "Shortest token length must be <= longest. Found shortestTokenLength=" + shortestTokenLength + " longestTokenLength=" + longestTokenLength;
/*     */ 
/*  80 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  82 */     this.mShortestTokenLength = shortestTokenLength;
/*  83 */     this.mLongestTokenLength = longestTokenLength;
/*     */   }
/*     */ 
/*     */   public String modifyToken(String token)
/*     */   {
/*  97 */     return (token.length() < this.mShortestTokenLength) || (token.length() > this.mLongestTokenLength) ? null : token;
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 104 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<TokenLengthTokenizerFactory> {
/*     */     static final long serialVersionUID = -8886994304636616691L;
/*     */ 
/*     */     public Serializer() {
/* 111 */       this(null);
/*     */     }
/*     */     public Serializer(TokenLengthTokenizerFactory factory) {
/* 114 */       super();
/*     */     }
/*     */ 
/*     */     public void writeExternalRest(ObjectOutput out) throws IOException {
/* 118 */       out.writeInt(((TokenLengthTokenizerFactory)factory()).mShortestTokenLength);
/* 119 */       out.writeInt(((TokenLengthTokenizerFactory)factory()).mLongestTokenLength);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in, TokenizerFactory baseFactory) throws IOException {
/* 123 */       int shortest = in.readInt();
/* 124 */       int longest = in.readInt();
/* 125 */       return new TokenLengthTokenizerFactory(baseFactory, shortest, longest);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.TokenLengthTokenizerFactory
 * JD-Core Version:    0.6.2
 */