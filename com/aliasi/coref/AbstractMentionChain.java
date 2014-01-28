/*     */ package com.aliasi.coref;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractMentionChain
/*     */   implements MentionChain
/*     */ {
/*  42 */   private final HashSet<Mention> mMentions = new HashSet();
/*     */   String mEntityType;
/*  53 */   final HashSet<String> mHonorifics = new HashSet();
/*     */   private int mMaxSentenceOffset;
/*  67 */   String mGender = null;
/*     */   private final int mIdentifier;
/*     */ 
/*     */   public AbstractMentionChain(Mention mention, int offset, int identifier)
/*     */   {
/*  75 */     this.mEntityType = mention.entityType();
/*  76 */     this.mMaxSentenceOffset = offset;
/*  77 */     this.mMentions.add(mention);
/*  78 */     this.mIdentifier = identifier;
/*  79 */     this.mGender = mention.gender();
/*  80 */     this.mHonorifics.addAll(mention.honorifics());
/*     */   }
/*     */ 
/*     */   public void setGender(String gender)
/*     */   {
/*  89 */     this.mGender = gender;
/*     */   }
/*     */ 
/*     */   public void addHonorific(String honorific)
/*     */   {
/*  99 */     this.mHonorifics.add(honorific);
/*     */   }
/*     */ 
/*     */   public final Set<Mention> mentions()
/*     */   {
/* 110 */     return this.mMentions;
/*     */   }
/*     */ 
/*     */   public final Set<String> honorifics()
/*     */   {
/* 121 */     return this.mHonorifics;
/*     */   }
/*     */ 
/*     */   public final String gender()
/*     */   {
/* 130 */     return this.mGender;
/*     */   }
/*     */ 
/*     */   public final int maxSentenceOffset()
/*     */   {
/* 141 */     return this.mMaxSentenceOffset;
/*     */   }
/*     */ 
/*     */   public String entityType()
/*     */   {
/* 150 */     return this.mEntityType;
/*     */   }
/*     */ 
/*     */   public void setEntityType(String entityType)
/*     */   {
/* 159 */     this.mEntityType = entityType;
/*     */   }
/*     */ 
/*     */   public final void add(Mention mention, int sentenceOffset)
/*     */   {
/* 173 */     this.mMentions.add(mention);
/* 174 */     if (sentenceOffset > this.mMaxSentenceOffset)
/* 175 */       this.mMaxSentenceOffset = sentenceOffset;
/* 176 */     add(mention);
/*     */   }
/*     */ 
/*     */   protected void add(Mention mention)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final int identifier()
/*     */   {
/* 201 */     return this.mIdentifier;
/*     */   }
/*     */ 
/*     */   public final boolean killed(Mention mention)
/*     */   {
/* 216 */     Killer[] killers = killers();
/* 217 */     for (int i = 0; i < killers.length; i++)
/* 218 */       if (killers[i].kill(mention, this))
/* 219 */         return true;
/* 220 */     return false;
/*     */   }
/*     */ 
/*     */   public final int matchScore(Mention mention)
/*     */   {
/* 235 */     Matcher[] matchers = matchers();
/* 236 */     int bestScore = 7;
/* 237 */     for (int i = 0; i < matchers.length; i++) {
/* 238 */       int score = matchers[i].match(mention, this);
/* 239 */       if ((score != -1) && (score < bestScore)) {
/* 240 */         bestScore = score;
/*     */       }
/*     */     }
/* 243 */     return bestScore > 6 ? -1 : bestScore;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 257 */     return ((that instanceof MentionChain)) && (equals((MentionChain)that));
/*     */   }
/*     */ 
/*     */   public boolean equals(MentionChain that)
/*     */   {
/* 270 */     return identifier() == that.identifier();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 280 */     return identifier();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 290 */     StringBuilder sb = new StringBuilder();
/* 291 */     sb.append("ID=");
/* 292 */     sb.append(identifier());
/* 293 */     sb.append("; mentions=");
/* 294 */     sb.append(mentions());
/* 295 */     sb.append("; gender=");
/* 296 */     sb.append(gender());
/* 297 */     sb.append("; honorifics=");
/* 298 */     sb.append(honorifics());
/* 299 */     sb.append("; maxSentenceOffset=");
/* 300 */     sb.append(maxSentenceOffset());
/* 301 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public abstract Matcher[] matchers();
/*     */ 
/*     */   public abstract Killer[] killers();
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.AbstractMentionChain
 * JD-Core Version:    0.6.2
 */