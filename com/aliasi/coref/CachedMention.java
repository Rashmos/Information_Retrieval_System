/*     */ package com.aliasi.coref;
/*     */ 
/*     */ import com.aliasi.util.Arrays;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CachedMention
/*     */   implements Mention
/*     */ {
/*     */   private final String mPhrase;
/*     */   private final String mEntityType;
/*     */   private final Set<String> mHonorifics;
/*     */   private final String mNormalPhrase;
/*     */   private final String[] mNormalTokens;
/*     */   private final String mGender;
/*     */   private final boolean mIsPronominal;
/*     */ 
/*     */   public CachedMention(String phrase, String entityType, Set<String> honorifics, String[] normalTokens, String gender, boolean isPronominal)
/*     */   {
/*  87 */     this.mPhrase = phrase;
/*  88 */     this.mEntityType = entityType;
/*  89 */     this.mHonorifics = honorifics;
/*  90 */     this.mNormalTokens = normalTokens;
/*  91 */     this.mNormalPhrase = Strings.concatenate(normalTokens);
/*  92 */     if (this.mNormalPhrase == null) throw new IllegalArgumentException("yikes");
/*  93 */     this.mGender = gender;
/*  94 */     this.mIsPronominal = isPronominal;
/*     */   }
/*     */ 
/*     */   public String phrase()
/*     */   {
/* 103 */     return this.mPhrase;
/*     */   }
/*     */ 
/*     */   public String entityType()
/*     */   {
/* 112 */     return this.mEntityType;
/*     */   }
/*     */ 
/*     */   public Set<String> honorifics()
/*     */   {
/* 121 */     return this.mHonorifics;
/*     */   }
/*     */ 
/*     */   public String normalPhrase()
/*     */   {
/* 132 */     return this.mNormalPhrase;
/*     */   }
/*     */ 
/*     */   public String[] normalTokens()
/*     */   {
/* 141 */     return this.mNormalTokens;
/*     */   }
/*     */ 
/*     */   public boolean isPronominal()
/*     */   {
/* 150 */     return this.mIsPronominal;
/*     */   }
/*     */ 
/*     */   public String gender()
/*     */   {
/* 159 */     return this.mGender;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 170 */     StringBuilder sb = new StringBuilder();
/* 171 */     sb.append("phrase=");
/* 172 */     sb.append(phrase());
/* 173 */     sb.append("; type=");
/* 174 */     sb.append(entityType());
/* 175 */     sb.append("; honorifics=");
/* 176 */     sb.append(honorifics());
/* 177 */     sb.append("; isPronominal=");
/* 178 */     sb.append(isPronominal());
/* 179 */     sb.append("; gender=");
/* 180 */     sb.append(gender());
/* 181 */     sb.append("; normalPhrase=");
/* 182 */     sb.append(normalPhrase());
/* 183 */     sb.append("; normalTokens=");
/* 184 */     sb.append(Arrays.arrayToString(normalTokens()));
/* 185 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.CachedMention
 * JD-Core Version:    0.6.2
 */