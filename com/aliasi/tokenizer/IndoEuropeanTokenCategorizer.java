/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public final class IndoEuropeanTokenCategorizer
/*     */   implements Compilable, TokenCategorizer
/*     */ {
/*     */   private static final String NULL_CLASS = "NULL-TOK";
/*     */   private static final String ONE_DIGIT_CLASS = "1-DIG";
/*     */   private static final String TWO_DIGIT_CLASS = "2-DIG";
/*     */   private static final String THREE_DIGIT_CLASS = "3-DIG";
/*     */   private static final String FOUR_DIGIT_CLASS = "4-DIG";
/*     */   private static final String FIVE_PLUS_DIGITS_CLASS = "5+-DIG";
/*     */   private static final String DIGITS_LETTERS_CLASS = "DIG-LET";
/*     */   private static final String MISC_DIGITS_CLASS = "DIG-MSC";
/*     */   private static final String DIGITS_DASH_CLASS = "DIG--";
/*     */   private static final String DIGITS_SLASH_CLASS = "DIG-/";
/*     */   private static final String DIGITS_COMMA_CLASS = "DIG-,";
/*     */   private static final String DIGITS_PERIOD_CLASS = "DIG-.";
/*     */   private static final String UPPERCASE_CLASS = "LET-UP";
/*     */   private static final String LOWERCASE_CLASS = "LET-LOW";
/*     */   private static final String CAPITALIZED_CLASS = "LET-CAP";
/*     */   private static final String MIXEDCASE_CLASS = "LET-MIX";
/*     */   private static final String ONE_UPPERCASE_CLASS = "1-LET-UP";
/*     */   private static final String ONE_LOWERCASE_CLASS = "1-LET-LOW";
/*     */   private static final String PUNCTUATION_CLASS = "PUNC-";
/*     */   private static final String OTHER_CLASS = "OTHER";
/* 175 */   private static final String[] CATEGORY_ARRAY = { "NULL-TOK", "1-DIG", "2-DIG", "3-DIG", "4-DIG", "5+-DIG", "DIG-LET", "DIG-MSC", "DIG--", "DIG-/", "DIG-,", "DIG-.", "LET-UP", "LET-LOW", "LET-CAP", "LET-MIX", "OTHER", "1-LET-UP", "1-LET-LOW", "PUNC-" };
/*     */ 
/* 203 */   public static final IndoEuropeanTokenCategorizer CATEGORIZER = new IndoEuropeanTokenCategorizer();
/*     */ 
/*     */   public String categorize(String token)
/*     */   {
/* 106 */     char[] chars = token.toCharArray();
/* 107 */     if (chars.length == 0) return "NULL-TOK";
/* 108 */     if (Strings.allDigits(chars, 0, chars.length)) {
/* 109 */       if (chars.length == 1) return "1-DIG";
/* 110 */       if (chars.length == 2) return "2-DIG";
/* 111 */       if (chars.length == 3) return "3-DIG";
/* 112 */       if (chars.length == 4) return "4-DIG";
/* 113 */       return "5+-DIG";
/*     */     }
/* 115 */     if (Strings.containsDigits(chars)) {
/* 116 */       if (Strings.containsLetter(chars)) return "DIG-LET";
/* 117 */       if (token.indexOf('-') >= 0) return "DIG--";
/* 118 */       if (token.indexOf('/') >= 0) return "DIG-/";
/* 119 */       if (token.indexOf(',') >= 0) return "DIG-,";
/* 120 */       if (token.indexOf('.') >= 0) return "DIG-.";
/* 121 */       return "DIG-MSC";
/*     */     }
/* 123 */     if (Strings.allPunctuation(chars)) return "PUNC-";
/* 124 */     if ((Character.isUpperCase(chars[0])) && (chars.length == 1))
/* 125 */       return "1-LET-UP";
/* 126 */     if ((Character.isLowerCase(chars[0])) && (chars.length == 1))
/* 127 */       return "1-LET-LOW";
/* 128 */     if (Strings.allUpperCase(chars)) return "LET-UP";
/* 129 */     if (Strings.allLowerCase(chars)) return "LET-LOW";
/* 130 */     if (Strings.capitalized(chars)) return "LET-CAP";
/* 131 */     if (Strings.allLetters(chars)) return "LET-MIX";
/* 132 */     return "OTHER";
/*     */   }
/*     */ 
/*     */   public String[] categories()
/*     */   {
/* 142 */     return (String[])CATEGORY_ARRAY.clone();
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 217 */     objOut.writeObject(new Externalizer());
/*     */   }
/*     */ 
/*     */   private static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = -7153532326881222261L;
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut)
/*     */     {
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */     {
/* 231 */       return IndoEuropeanTokenCategorizer.CATEGORIZER;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.IndoEuropeanTokenCategorizer
 * JD-Core Version:    0.6.2
 */