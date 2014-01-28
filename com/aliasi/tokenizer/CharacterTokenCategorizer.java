/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.Strings;
/*     */ 
/*     */ public class CharacterTokenCategorizer
/*     */   implements TokenCategorizer
/*     */ {
/*     */   public static final String UNKNOWN_CAT = "UNKNOWN";
/*     */   public static final String DIGIT_CAT = "DIGIT";
/*     */   public static final String LETTER_CAT = "LETTER";
/*     */   public static final String PUNCTUATION_CAT = "PUNCTUATION";
/*     */   public static final String OTHER_CAT = "OTHER";
/* 111 */   static final CharacterTokenCategorizer INSTANCE = new CharacterTokenCategorizer();
/*     */ 
/* 114 */   private static final String[] CATEGORY_ARRAY = { "UNKNOWN", "DIGIT", "LETTER", "PUNCTUATION", "OTHER" };
/*     */ 
/*     */   public String categorize(String token)
/*     */   {
/*  54 */     if (token.length() != 1) return "UNKNOWN";
/*  55 */     char c = token.charAt(0);
/*  56 */     if (Character.isDigit(c)) return "DIGIT";
/*  57 */     if (Character.isLetter(c)) return "LETTER";
/*  58 */     if (Strings.isPunctuation(c)) return "PUNCTUATION";
/*  59 */     return "OTHER";
/*     */   }
/*     */ 
/*     */   public String[] categories()
/*     */   {
/*  68 */     return (String[])CATEGORY_ARRAY.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  77 */     return getClass().getName();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.CharacterTokenCategorizer
 * JD-Core Version:    0.6.2
 */