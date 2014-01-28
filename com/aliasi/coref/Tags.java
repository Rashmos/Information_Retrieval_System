/*     */ package com.aliasi.coref;
/*     */ 
/*     */ final class Tags
/*     */ {
/*     */   public static final String OUT_TAG = "OUT";
/*     */   public static final String START_TAG = "OUT";
/*     */   public static final String START_TOKEN = ".";
/*     */   private static final String START_PREFIX = "ST_";
/*  92 */   private static final int START_PREFIX_LENGTH = "ST_".length();
/*     */ 
/* 211 */   public static String PERSON_TAG = "PERSON";
/*     */ 
/* 216 */   public static String LOCATION_TAG = "LOCATION";
/*     */ 
/* 221 */   public static String ORGANIZATION_TAG = "ORGANIZATION";
/*     */ 
/* 226 */   public static String OTHER_TAG = "OTHER";
/*     */ 
/* 231 */   public static String MALE_PRONOUN_TAG = "MALE_PRONOUN";
/*     */ 
/* 236 */   public static String FEMALE_PRONOUN_TAG = "FEMALE_PRONOUN";
/*     */ 
/* 241 */   public static String NEUTER_PRONOUN_TAG = "NEUTER_PRONOUN";
/*     */ 
/* 247 */   public static String DATABASE_MATCH_TAG_XDC = "USER_ENTITY_XDC1";
/*     */ 
/* 253 */   public static String DATABASE_MATCH_TAG_NO_XDC = "USER_ENTITY_XDC0";
/*     */ 
/* 258 */   public static final String[] TAG_SET = { PERSON_TAG, LOCATION_TAG, ORGANIZATION_TAG, OTHER_TAG, MALE_PRONOUN_TAG, FEMALE_PRONOUN_TAG, DATABASE_MATCH_TAG_XDC, DATABASE_MATCH_TAG_NO_XDC };
/*     */ 
/*     */   public static String baseTag(String tag)
/*     */   {
/* 107 */     return tag.startsWith("ST_") ? stripPrefix(tag) : tag;
/*     */   }
/*     */ 
/*     */   public static boolean equalBaseTags(String tag1, String tag2)
/*     */   {
/* 121 */     return baseTag(tag1).equals(baseTag(tag2));
/*     */   }
/*     */ 
/*     */   public static boolean isStartTag(String tag)
/*     */   {
/* 131 */     return tag.startsWith("ST_");
/*     */   }
/*     */ 
/*     */   public static boolean isInnerTag(String tag)
/*     */   {
/* 141 */     return (!tag.equals("OUT")) && (!isStartTag(tag));
/*     */   }
/*     */ 
/*     */   public static boolean isOutTag(String tag)
/*     */   {
/* 151 */     return tag.equals("OUT");
/*     */   }
/*     */ 
/*     */   public static boolean isMidTag(String tag1, String tag2)
/*     */   {
/* 164 */     return isInnerTag(tag2);
/*     */   }
/*     */ 
/*     */   public static boolean illegalSequence(String tag1, String tag2)
/*     */   {
/* 179 */     return (isInnerTag(tag2)) && (!equalBaseTags(tag1, tag2));
/*     */   }
/*     */ 
/*     */   public static String toStartTag(String tag)
/*     */   {
/* 192 */     if ((isOutTag(tag)) || (isStartTag(tag))) return tag;
/* 193 */     return "ST_" + tag;
/*     */   }
/*     */ 
/*     */   private static String stripPrefix(String tag)
/*     */   {
/* 205 */     return tag.substring(START_PREFIX_LENGTH);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.Tags
 * JD-Core Version:    0.6.2
 */