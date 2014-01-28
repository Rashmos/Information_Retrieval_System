/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ final class Tags
/*     */ {
/*     */   public static final String OUT_TAG = "O";
/*     */   public static final String START_TAG = "O";
/*     */   public static final String START_TOKEN = ".";
/*     */   private static final String START_PREFIX = "B-";
/*     */   private static final String IN_PREFIX = "I-";
/*     */   private static final int PREFIX_LENGTH = 2;
/* 216 */   public static String PERSON_TAG = "PERSON";
/*     */ 
/* 221 */   public static String LOCATION_TAG = "LOCATION";
/*     */ 
/* 226 */   public static String ORGANIZATION_TAG = "ORGANIZATION";
/*     */ 
/* 231 */   public static String OTHER_TAG = "OTHER";
/*     */ 
/* 236 */   public static String MALE_PRONOUN_TAG = "MALE_PRONOUN";
/*     */ 
/* 241 */   public static String FEMALE_PRONOUN_TAG = "FEMALE_PRONOUN";
/*     */ 
/* 247 */   public static String DATABASE_MATCH_TAG_XDC = "USER_ENTITY_XDC1";
/*     */ 
/* 253 */   public static String DATABASE_MATCH_TAG_NO_XDC = "USER_ENTITY_XDC0";
/*     */ 
/* 258 */   public static final String[] TAG_SET = { PERSON_TAG, LOCATION_TAG, ORGANIZATION_TAG, OTHER_TAG, MALE_PRONOUN_TAG, FEMALE_PRONOUN_TAG, DATABASE_MATCH_TAG_XDC, DATABASE_MATCH_TAG_NO_XDC };
/*     */ 
/*     */   public static String baseTag(String tag)
/*     */   {
/* 106 */     return (tag.startsWith("B-")) || (tag.startsWith("I-")) ? stripPrefix(tag) : tag;
/*     */   }
/*     */ 
/*     */   public static boolean equalBaseTags(String tag1, String tag2)
/*     */   {
/* 120 */     return baseTag(tag1).equals(baseTag(tag2));
/*     */   }
/*     */ 
/*     */   public static boolean isStartTag(String tag)
/*     */   {
/* 130 */     return tag.startsWith("B-");
/*     */   }
/*     */ 
/*     */   public static boolean isInnerTag(String tag)
/*     */   {
/* 140 */     return tag.startsWith("I-");
/*     */   }
/*     */ 
/*     */   public static boolean isOutTag(String tag)
/*     */   {
/* 150 */     return tag.equals("O");
/*     */   }
/*     */ 
/*     */   public static boolean isMidTag(String tag1, String tag2)
/*     */   {
/* 163 */     return isInnerTag(tag2);
/*     */   }
/*     */ 
/*     */   public static boolean illegalSequence(String tag1, String tag2)
/*     */   {
/* 178 */     return (isInnerTag(tag2)) && (!equalBaseTags(tag1, tag2));
/*     */   }
/*     */ 
/*     */   public static String toStartTag(String tag)
/*     */   {
/* 191 */     if ((isOutTag(tag)) || (isStartTag(tag))) return tag;
/* 192 */     return "B-" + tag;
/*     */   }
/*     */ 
/*     */   public static String toInnerTag(String tag)
/*     */   {
/* 197 */     return "I-" + baseTag(tag);
/*     */   }
/*     */ 
/*     */   private static String stripPrefix(String tag)
/*     */   {
/* 210 */     return tag.substring(2);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.Tags
 * JD-Core Version:    0.6.2
 */