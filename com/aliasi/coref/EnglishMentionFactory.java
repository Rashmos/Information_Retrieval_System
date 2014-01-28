/*     */ package com.aliasi.coref;
/*     */ 
/*     */ import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class EnglishMentionFactory extends AbstractMentionFactory
/*     */ {
/*     */   public static final String MALE_GENDER = "male";
/*     */   public static final String FEMALE_GENDER = "female";
/*     */   public static final String NEUTER_GENDER = "neuter";
/* 115 */   public static final Set<String> MALE_HONORIFICS = new HashSet();
/*     */   public static final Set<String> FEMALE_HONORIFICS;
/*     */   public static final Set<String> HONORIFICS;
/*     */ 
/*     */   public EnglishMentionFactory()
/*     */   {
/*  42 */     super(IndoEuropeanTokenizerFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */   public boolean isHonorific(String normalizedToken)
/*     */   {
/*  47 */     return HONORIFICS.contains(normalizedToken);
/*     */   }
/*     */ 
/*     */   public boolean isPronominal(String entityType)
/*     */   {
/*  60 */     return (entityType.equals(Tags.MALE_PRONOUN_TAG)) || (entityType.equals(Tags.FEMALE_PRONOUN_TAG)) || (entityType.equals(Tags.NEUTER_PRONOUN_TAG));
/*     */   }
/*     */ 
/*     */   public String normalizeToken(String token)
/*     */   {
/*  75 */     if (Strings.allPunctuation(token)) return null;
/*  76 */     return token.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   public String gender(String entityType)
/*     */   {
/*  88 */     if (entityType.equals(Tags.MALE_PRONOUN_TAG))
/*  89 */       return "male";
/*  90 */     if (entityType.equals(Tags.FEMALE_PRONOUN_TAG))
/*  91 */       return "female";
/*  92 */     if (entityType.equals(Tags.PERSON_TAG))
/*  93 */       return null;
/*  94 */     return "neuter";
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 117 */     MALE_HONORIFICS.add("mr");
/* 118 */     MALE_HONORIFICS.add("mssr");
/* 119 */     MALE_HONORIFICS.add("mister");
/*     */ 
/* 125 */     FEMALE_HONORIFICS = new HashSet();
/*     */ 
/* 127 */     FEMALE_HONORIFICS.add("ms");
/* 128 */     FEMALE_HONORIFICS.add("mrs");
/* 129 */     FEMALE_HONORIFICS.add("missus");
/* 130 */     FEMALE_HONORIFICS.add("miss");
/*     */ 
/* 136 */     HONORIFICS = new HashSet();
/*     */ 
/* 138 */     HONORIFICS.addAll(MALE_HONORIFICS);
/* 139 */     HONORIFICS.addAll(FEMALE_HONORIFICS);
/* 140 */     HONORIFICS.add("dr");
/* 141 */     HONORIFICS.add("gen");
/* 142 */     HONORIFICS.add("adm");
/* 143 */     HONORIFICS.add("pres");
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.EnglishMentionFactory
 * JD-Core Version:    0.6.2
 */