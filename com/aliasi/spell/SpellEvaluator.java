/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.classify.ConfusionMatrix;
/*     */ import com.aliasi.lm.LanguageModel;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SpellEvaluator
/*     */ {
/*     */   private final SpellChecker mSpellChecker;
/*  96 */   private final List<String> mTextList = new ArrayList();
/*     */ 
/*  98 */   private final List<String> mCorrectTextList = new ArrayList();
/*     */ 
/* 100 */   private final List<String> mSuggestionList = new ArrayList();
/*     */ 
/* 103 */   private String mLastCaseReport = "No cases added yet.";
/*     */ 
/* 105 */   private int mUserCorrectSystemWrongSuggestion = 0;
/* 106 */   private int mUserCorrectSystemNoSuggestion = 0;
/* 107 */   private int mUserErrorSystemNoSuggestion = 0;
/* 108 */   private int mUserErrorSystemCorrect = 0;
/* 109 */   private int mUserErrorSystemWrongSuggestion = 0;
/*     */   private final ObjectToCounterMap<String> mTokenCounter;
/* 224 */   static final DecimalFormat LP_FORMAT = new DecimalFormat("#0.0");
/*     */ 
/* 281 */   static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("##0.0");
/*     */ 
/*     */   public SpellEvaluator(SpellChecker checker)
/*     */   {
/* 119 */     this(checker, null);
/*     */   }
/*     */ 
/*     */   public SpellEvaluator(SpellChecker checker, ObjectToCounterMap<String> tokenCounter)
/*     */   {
/* 136 */     this.mSpellChecker = checker;
/* 137 */     this.mTokenCounter = tokenCounter;
/*     */   }
/*     */ 
/*     */   public void addCase(String text, String correctText)
/*     */   {
/* 148 */     String normalizedText = normalize(text);
/* 149 */     String normalizedCorrectText = normalize(correctText);
/* 150 */     String suggestion = this.mSpellChecker.didYouMean(text);
/* 151 */     String normalizedSuggestion = suggestion == null ? normalizedText : normalize(suggestion);
/*     */ 
/* 155 */     this.mTextList.add(normalizedText);
/* 156 */     this.mCorrectTextList.add(normalizedCorrectText);
/* 157 */     this.mSuggestionList.add(normalizedSuggestion);
/*     */ 
/* 159 */     String resultDescription = null;
/* 160 */     if (normalizedText.equals(normalizedCorrectText)) {
/* 161 */       resultDescription = "user correct, ";
/* 162 */       if (normalizedText.equals(normalizedSuggestion)) {
/* 163 */         resultDescription = resultDescription + "spell check wrong suggestion (FP)";
/* 164 */         this.mUserCorrectSystemWrongSuggestion += 1;
/*     */       } else {
/* 166 */         resultDescription = resultDescription + "spell check no suggestion (TN)";
/* 167 */         this.mUserCorrectSystemNoSuggestion += 1;
/*     */       }
/*     */     } else {
/* 170 */       resultDescription = "user incorrect, ";
/* 171 */       if (normalizedText.equals(normalizedSuggestion)) {
/* 172 */         resultDescription = resultDescription + "spell check no suggestion (FN)";
/* 173 */         this.mUserErrorSystemNoSuggestion += 1;
/* 174 */       } else if (normalizedCorrectText.equals(normalizedSuggestion)) {
/* 175 */         resultDescription = resultDescription + "spell check correct (TP)";
/* 176 */         this.mUserErrorSystemCorrect += 1;
/*     */       } else {
/* 178 */         resultDescription = resultDescription + "spell check wrong suggestion (FP,FN)";
/* 179 */         this.mUserErrorSystemWrongSuggestion += 1;
/*     */       }
/*     */     }
/*     */ 
/* 183 */     StringBuilder sb = new StringBuilder();
/* 184 */     report(sb, "input", normalizedText);
/* 185 */     sb.append("\n");
/* 186 */     report(sb, "correct", normalizedCorrectText);
/* 187 */     sb.append("\n");
/* 188 */     report(sb, "suggest", normalizedSuggestion);
/* 189 */     sb.append("\n");
/*     */ 
/* 191 */     this.mLastCaseReport = sb.toString();
/*     */   }
/*     */ 
/*     */   void report(StringBuilder sb, String msg, String text)
/*     */   {
/* 196 */     sb.append(msg + "=|" + text + "|");
/* 197 */     if (!(this.mSpellChecker instanceof CompiledSpellChecker))
/* 198 */       return;
/* 199 */     CompiledSpellChecker checker = (CompiledSpellChecker)this.mSpellChecker;
/* 200 */     LanguageModel lm = checker.languageModel();
/* 201 */     double estimate = lm.log2Estimate(" " + text + " ") - lm.log2Estimate(" ");
/*     */ 
/* 205 */     sb.append(" log2 p=" + lpFormat(estimate));
/*     */ 
/* 207 */     TokenizerFactory tf = checker.tokenizerFactory();
/* 208 */     char[] cs = text.toCharArray();
/* 209 */     Tokenizer tokenizer = tf.tokenizer(cs, 0, cs.length);
/* 210 */     String[] tokens = tokenizer.tokenize();
/* 211 */     Set tokenSet = checker.tokenSet();
/* 212 */     for (int i = 0; i < tokens.length; i++) {
/* 213 */       sb.append(" ");
/* 214 */       sb.append(tokens[i]);
/* 215 */       sb.append("[");
/* 216 */       if (this.mTokenCounter == null)
/* 217 */         sb.append(tokenSet.contains(tokens[i]) ? "+" : "-");
/*     */       else
/* 219 */         sb.append(this.mTokenCounter.getCount(tokens[i]));
/* 220 */       sb.append("]");
/*     */     }
/*     */   }
/*     */ 
/*     */   static String lpFormat(double x)
/*     */   {
/* 227 */     return LP_FORMAT.format(x);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 239 */     int userErrors = this.mUserErrorSystemWrongSuggestion + this.mUserErrorSystemCorrect + this.mUserErrorSystemNoSuggestion;
/*     */ 
/* 242 */     int userCorrect = this.mUserCorrectSystemWrongSuggestion + this.mUserCorrectSystemNoSuggestion;
/*     */ 
/* 244 */     int total = userErrors + userCorrect;
/*     */ 
/* 246 */     StringBuilder sb = new StringBuilder();
/* 247 */     sb.append("EVALUATION\n");
/* 248 */     addReport(sb, "User Error", userErrors, total);
/*     */ 
/* 250 */     addReport(sb, "     System Correct", this.mUserErrorSystemCorrect, userErrors);
/*     */ 
/* 252 */     addReport(sb, "     System Error", this.mUserErrorSystemWrongSuggestion, userErrors);
/*     */ 
/* 254 */     addReport(sb, "     System No Suggestion", this.mUserErrorSystemNoSuggestion, userErrors);
/*     */ 
/* 257 */     addReport(sb, "User Correct", userCorrect, total);
/*     */ 
/* 259 */     addReport(sb, "     System Error", this.mUserCorrectSystemWrongSuggestion, userCorrect);
/*     */ 
/* 261 */     addReport(sb, "     System No Suggestion", this.mUserCorrectSystemNoSuggestion, userCorrect);
/*     */ 
/* 264 */     sb.append("SPELL CHECKER toString()\n");
/* 265 */     sb.append(this.mSpellChecker);
/*     */ 
/* 267 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static void addReport(StringBuilder sb, String msg, int correct, int total)
/*     */   {
/* 272 */     sb.append(msg);
/* 273 */     sb.append(": ");
/* 274 */     sb.append(correct);
/* 275 */     sb.append(" [");
/* 276 */     double percentage = total > 0 ? 100.0D * correct / total : 0.0D;
/* 277 */     sb.append(PERCENT_FORMAT.format(percentage));
/* 278 */     sb.append("%]\n");
/*     */   }
/*     */ 
/*     */   public String[][] userCorrectSystemNoSuggestion()
/*     */   {
/* 291 */     return extract(true, true, true);
/*     */   }
/*     */ 
/*     */   public String[][] userCorrectSystemWrongSuggestion()
/*     */   {
/* 302 */     return extract(true, false, false);
/*     */   }
/*     */ 
/*     */   public String[][] userErrorSystemCorrect()
/*     */   {
/* 313 */     return extract(false, true, false);
/*     */   }
/*     */ 
/*     */   public String[][] userErrorSystemWrongSuggestion()
/*     */   {
/* 325 */     return extract(false, false, false);
/*     */   }
/*     */ 
/*     */   public String[][] userErrorSystemNoSuggestion()
/*     */   {
/* 336 */     return extract(false, false, true);
/*     */   }
/*     */ 
/*     */   String[][] extract(boolean textEqualsCorrect, boolean correctEqualsSuggestion, boolean textEqualsSuggestion)
/*     */   {
/* 343 */     List result = new ArrayList();
/* 344 */     for (int i = 0; i < this.mSuggestionList.size(); i++) {
/* 345 */       String text = ((String)this.mTextList.get(i)).toString();
/* 346 */       String correct = ((String)this.mCorrectTextList.get(i)).toString();
/* 347 */       String suggestion = ((String)this.mSuggestionList.get(i)).toString();
/* 348 */       if ((text.equals(correct) == textEqualsCorrect) && (correct.equals(suggestion) == correctEqualsSuggestion) && (text.equals(suggestion) == textEqualsSuggestion))
/*     */       {
/* 352 */         result.add(new String[] { text, correct, suggestion });
/*     */       }
/*     */     }
/* 354 */     return (String[][])result.toArray(Strings.EMPTY_STRING_2D_ARRAY);
/*     */   }
/*     */ 
/*     */   public String getLastCaseReport()
/*     */   {
/* 364 */     return this.mLastCaseReport;
/*     */   }
/*     */ 
/*     */   public ConfusionMatrix confusionMatrix()
/*     */   {
/* 382 */     int tn = this.mUserCorrectSystemNoSuggestion;
/* 383 */     int tp = this.mUserErrorSystemCorrect;
/* 384 */     int fn = this.mUserErrorSystemNoSuggestion + this.mUserErrorSystemWrongSuggestion;
/* 385 */     int fp = this.mUserCorrectSystemWrongSuggestion;
/* 386 */     return new ConfusionMatrix(new String[] { "correct", "misspelled" }, new int[][] { { tp, fp }, { fn, tn } });
/*     */   }
/*     */ 
/*     */   public String normalize(String text)
/*     */   {
/* 407 */     return text;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.SpellEvaluator
 * JD-Core Version:    0.6.2
 */