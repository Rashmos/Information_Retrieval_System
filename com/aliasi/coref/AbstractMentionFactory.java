/*       */ package com.aliasi.coref;
/*       */ 
/*       */ import com.aliasi.tokenizer.Tokenizer;
/*       */ import com.aliasi.tokenizer.TokenizerFactory;
/*       */ import com.aliasi.util.SmallSet;
/*       */ import com.aliasi.util.Strings;
/*       */ import java.util.ArrayList;
/*       */ import java.util.List;
/*       */ import java.util.Set;
/*       */ 
/*       */ public abstract class AbstractMentionFactory
/*       */   implements MentionFactory
/*       */ {
/*    46 */   private int mNextChainIdentifier = 0;
/*       */   private final TokenizerFactory mTokenizerFactory;
/*       */ 
/*       */   public AbstractMentionFactory(TokenizerFactory tokenizerFactory)
/*       */   {
/*    61 */     this.mTokenizerFactory = tokenizerFactory;
/*       */   }
/*       */ 
/*       */   public Mention create(String phrase, String entityType)
/*       */   {
/*    72 */     List tokens = new ArrayList();
/*    73 */     Set honorifics = extractTokens(phrase, tokens);
/*    74 */     return new CachedMention(phrase, entityType, honorifics, (String[])tokens.toArray(Strings.EMPTY_STRING_ARRAY), gender(entityType), isPronominal(entityType));
/*       */   }
/*       */ 
/*       */   public MentionChain promote(Mention mention, int offset)
/*       */   {
/*    89 */     return new MentionChainImpl(mention, offset, nextChainIdentifier());
/*       */   }
/*       */ 
/*       */   private int nextChainIdentifier() {
/*    93 */     synchronized (this) {
/*    94 */       return this.mNextChainIdentifier++;
/*       */     }
/*       */   }
/*       */ 
/*       */   protected abstract boolean isHonorific(String paramString);
/*       */ 
/*       */   protected abstract String gender(String paramString);
/*       */ 
/*       */   protected abstract boolean isPronominal(String paramString);
/*       */ 
/*       */   protected abstract String normalizeToken(String paramString);
/*       */ 
/*       */   private Set<String> extractTokens(String phrase, List<String> tokens)
/*       */   {
/*   145 */     Set honorifics = SmallSet.create();
/*   146 */     char[] cs = phrase.toCharArray();
/*   147 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*   148 */     for (String token : tokenizer) {
/*   149 */       String normalToken = normalizeToken(token);
/*   150 */       if (normalToken != null)
/*   151 */         if (isHonorific(normalToken))
/*   152 */           honorifics = SmallSet.create(normalToken, honorifics);
/*       */         else
/*   154 */           tokens.add(normalToken);
/*       */     }
/*   156 */     return honorifics;
/*       */   }
/*       */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.AbstractMentionFactory
 * JD-Core Version:    0.6.2
 */