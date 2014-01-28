/*     */ package com.aliasi.tag;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Tagging<E>
/*     */ {
/*     */   private final List<E> mTokens;
/*     */   private final List<String> mTags;
/*     */ 
/*     */   public Tagging(List<E> tokens, List<String> tags)
/*     */   {
/*  47 */     this(new ArrayList(tokens), new ArrayList(tags), true);
/*  48 */     if (tokens.size() != tags.size()) {
/*  49 */       String msg = "Tokens and tags must be same size. Found tokens.size()=" + tokens.size() + " tags.size()=" + tags.size();
/*     */ 
/*  52 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   Tagging(List<E> tokens, List<String> tags, boolean ignore) {
/*  57 */     this.mTokens = tokens;
/*  58 */     this.mTags = tags;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  68 */     return this.mTokens.size();
/*     */   }
/*     */ 
/*     */   public E token(int n)
/*     */   {
/*  81 */     return this.mTokens.get(n);
/*     */   }
/*     */ 
/*     */   public String tag(int n)
/*     */   {
/*  94 */     return (String)this.mTags.get(n);
/*     */   }
/*     */ 
/*     */   public List<E> tokens()
/*     */   {
/* 103 */     return Collections.unmodifiableList(this.mTokens);
/*     */   }
/*     */ 
/*     */   public List<String> tags()
/*     */   {
/* 110 */     return Collections.unmodifiableList(this.mTags);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 119 */     StringBuilder sb = new StringBuilder();
/* 120 */     for (int i = 0; i < size(); i++) {
/* 121 */       if (i > 0) sb.append(" ");
/* 122 */       sb.append(token(i) + "/" + tag(i));
/*     */     }
/* 124 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.Tagging
 * JD-Core Version:    0.6.2
 */