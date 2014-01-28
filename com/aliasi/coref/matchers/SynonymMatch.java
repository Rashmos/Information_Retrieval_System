/*     */ package com.aliasi.coref.matchers;
/*     */ 
/*     */ import com.aliasi.coref.BooleanMatcherAdapter;
/*     */ import com.aliasi.coref.Mention;
/*     */ import com.aliasi.coref.MentionChain;
/*     */ import com.aliasi.util.ObjectToSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SynonymMatch extends BooleanMatcherAdapter
/*     */ {
/*  48 */   private final ObjectToSet<String, String> mSynonymDictionary = new ObjectToSet();
/*     */ 
/*     */   public SynonymMatch(int score)
/*     */   {
/*  58 */     super(score);
/*     */   }
/*     */ 
/*     */   public boolean matchBoolean(Mention mention, MentionChain chain)
/*     */   {
/*  72 */     String phrase = mention.normalPhrase();
/*  73 */     if (!this.mSynonymDictionary.containsKey(phrase)) return false;
/*  74 */     Set synonyms = this.mSynonymDictionary.getSet(phrase);
/*  75 */     for (Iterator i$ = synonyms.iterator(); i$.hasNext(); ) { synonym = (String)i$.next();
/*  76 */       for (Mention chainMention : chain.mentions())
/*  77 */         if (synonym.equals(chainMention.normalPhrase()))
/*  78 */           return true;
/*     */     }
/*     */     String synonym;
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   public void addSynonym(String phrase1, String phrase2)
/*     */   {
/*  91 */     this.mSynonymDictionary.addMember(phrase1, phrase2);
/*  92 */     this.mSynonymDictionary.addMember(phrase2, phrase1);
/*     */   }
/*     */ 
/*     */   public void removeSynonym(String phrase1, String phrase2)
/*     */   {
/* 104 */     this.mSynonymDictionary.removeMember(phrase1, phrase2);
/* 105 */     this.mSynonymDictionary.removeMember(phrase2, phrase1);
/*     */   }
/*     */ 
/*     */   public void clearSynonyms()
/*     */   {
/* 112 */     this.mSynonymDictionary.clear();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.matchers.SynonymMatch
 * JD-Core Version:    0.6.2
 */