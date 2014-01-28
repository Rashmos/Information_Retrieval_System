/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.Iterators.Buffered;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class Tokenizer
/*     */   implements Iterable<String>
/*     */ {
/*     */   public Iterator<String> iterator()
/*     */   {
/*  84 */     return new TokenIterator();
/*     */   }
/*     */ 
/*     */   public abstract String nextToken();
/*     */ 
/*     */   public String nextWhitespace()
/*     */   {
/* 107 */     return " ";
/*     */   }
/*     */ 
/*     */   public int lastTokenStartPosition()
/*     */   {
/* 130 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int lastTokenEndPosition()
/*     */   {
/* 152 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void tokenize(List<? super String> tokens, List<? super String> whitespaces)
/*     */   {
/* 165 */     whitespaces.add(nextWhitespace());
/*     */     String token;
/* 167 */     while ((token = nextToken()) != null) {
/* 168 */       tokens.add(token.toString());
/* 169 */       whitespaces.add(nextWhitespace().toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] tokenize()
/*     */   {
/* 181 */     List tokenList = new ArrayList();
/*     */     String token;
/* 183 */     while ((token = nextToken()) != null)
/* 184 */       tokenList.add(token);
/* 185 */     return (String[])tokenList.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */   }
/*     */   class TokenIterator extends Iterators.Buffered<String> {
/*     */     TokenIterator() {
/*     */     }
/*     */     public String bufferNext() {
/* 191 */       return Tokenizer.this.nextToken();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.Tokenizer
 * JD-Core Version:    0.6.2
 */