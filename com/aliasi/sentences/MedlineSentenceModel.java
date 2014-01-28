/*     */ package com.aliasi.sentences;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class MedlineSentenceModel extends HeuristicSentenceModel
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -8958290440993791272L;
/* 226 */   private static final Set<String> POSSIBLE_STOPS = new HashSet();
/*     */   private static final Set<String> IMPOSSIBLE_PENULTIMATES;
/*     */   private static final Set<String> IMPOSSIBLE_SENTENCE_STARTS;
/*     */   private static final Set<String> LOWERCASE_STARTS;
/* 322 */   public static final MedlineSentenceModel INSTANCE = new MedlineSentenceModel();
/*     */ 
/*     */   public MedlineSentenceModel()
/*     */   {
/* 125 */     super(POSSIBLE_STOPS, IMPOSSIBLE_PENULTIMATES, IMPOSSIBLE_SENTENCE_STARTS, true, true);
/*     */   }
/*     */ 
/*     */   protected boolean possibleStart(String[] tokens, String[] whitespaces, int start, int end)
/*     */   {
/* 204 */     for (int i = start; i < end; i++) {
/* 205 */       if (LOWERCASE_STARTS.contains(tokens[i])) return true;
/* 206 */       if (containsDigitOrUpper(tokens[i])) return true;
/* 207 */       if (whitespaces[(i + 1)].length() > 0) return false;
/*     */     }
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean containsDigitOrUpper(CharSequence token) {
/* 213 */     int len = token.length();
/* 214 */     for (int i = 0; i < len; i++) {
/* 215 */       if (Character.isUpperCase(token.charAt(i))) return true;
/* 216 */       if (Character.isDigit(token.charAt(i))) return true;
/*     */     }
/* 218 */     return false;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 222 */     return new Serializer();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 228 */     POSSIBLE_STOPS.add(".");
/* 229 */     POSSIBLE_STOPS.add("..");
/* 230 */     POSSIBLE_STOPS.add("!");
/* 231 */     POSSIBLE_STOPS.add("?");
/*     */ 
/* 234 */     IMPOSSIBLE_PENULTIMATES = new HashSet();
/*     */ 
/* 238 */     IMPOSSIBLE_PENULTIMATES.add("Bros");
/* 239 */     IMPOSSIBLE_PENULTIMATES.add("No");
/* 240 */     IMPOSSIBLE_PENULTIMATES.add("al");
/* 241 */     IMPOSSIBLE_PENULTIMATES.add("vs");
/* 242 */     IMPOSSIBLE_PENULTIMATES.add("etc");
/* 243 */     IMPOSSIBLE_PENULTIMATES.add("Fig");
/*     */ 
/* 246 */     IMPOSSIBLE_PENULTIMATES.add("Dr");
/* 247 */     IMPOSSIBLE_PENULTIMATES.add("Prof");
/* 248 */     IMPOSSIBLE_PENULTIMATES.add("PhD");
/* 249 */     IMPOSSIBLE_PENULTIMATES.add("MD");
/*     */ 
/* 252 */     IMPOSSIBLE_PENULTIMATES.add("Co");
/* 253 */     IMPOSSIBLE_PENULTIMATES.add("Corp");
/* 254 */     IMPOSSIBLE_PENULTIMATES.add("Inc");
/*     */ 
/* 257 */     IMPOSSIBLE_PENULTIMATES.add("Jan");
/* 258 */     IMPOSSIBLE_PENULTIMATES.add("Feb");
/* 259 */     IMPOSSIBLE_PENULTIMATES.add("Mar");
/* 260 */     IMPOSSIBLE_PENULTIMATES.add("Apr");
/*     */ 
/* 262 */     IMPOSSIBLE_PENULTIMATES.add("Jul");
/* 263 */     IMPOSSIBLE_PENULTIMATES.add("Aug");
/* 264 */     IMPOSSIBLE_PENULTIMATES.add("Sep");
/* 265 */     IMPOSSIBLE_PENULTIMATES.add("Sept");
/* 266 */     IMPOSSIBLE_PENULTIMATES.add("Oct");
/* 267 */     IMPOSSIBLE_PENULTIMATES.add("Nov");
/* 268 */     IMPOSSIBLE_PENULTIMATES.add("Dec");
/*     */ 
/* 271 */     IMPOSSIBLE_PENULTIMATES.add("St");
/*     */ 
/* 274 */     IMPOSSIBLE_PENULTIMATES.add("AM");
/* 275 */     IMPOSSIBLE_PENULTIMATES.add("PM");
/*     */ 
/* 279 */     IMPOSSIBLE_SENTENCE_STARTS = new HashSet();
/*     */ 
/* 282 */     IMPOSSIBLE_SENTENCE_STARTS.add(")");
/* 283 */     IMPOSSIBLE_SENTENCE_STARTS.add("]");
/* 284 */     IMPOSSIBLE_SENTENCE_STARTS.add("}");
/* 285 */     IMPOSSIBLE_SENTENCE_STARTS.add(">");
/* 286 */     IMPOSSIBLE_SENTENCE_STARTS.add("<");
/* 287 */     IMPOSSIBLE_SENTENCE_STARTS.add(".");
/* 288 */     IMPOSSIBLE_SENTENCE_STARTS.add("!");
/* 289 */     IMPOSSIBLE_SENTENCE_STARTS.add("?");
/* 290 */     IMPOSSIBLE_SENTENCE_STARTS.add(":");
/* 291 */     IMPOSSIBLE_SENTENCE_STARTS.add(";");
/* 292 */     IMPOSSIBLE_SENTENCE_STARTS.add("-");
/* 293 */     IMPOSSIBLE_SENTENCE_STARTS.add("--");
/* 294 */     IMPOSSIBLE_SENTENCE_STARTS.add("---");
/* 295 */     IMPOSSIBLE_SENTENCE_STARTS.add("%");
/*     */ 
/* 298 */     LOWERCASE_STARTS = new HashSet();
/*     */ 
/* 301 */     LOWERCASE_STARTS.add("alpha");
/* 302 */     LOWERCASE_STARTS.add("beta");
/* 303 */     LOWERCASE_STARTS.add("gamma");
/* 304 */     LOWERCASE_STARTS.add("delta");
/* 305 */     LOWERCASE_STARTS.add("c");
/* 306 */     LOWERCASE_STARTS.add("i");
/* 307 */     LOWERCASE_STARTS.add("ii");
/* 308 */     LOWERCASE_STARTS.add("iii");
/* 309 */     LOWERCASE_STARTS.add("iv");
/* 310 */     LOWERCASE_STARTS.add("v");
/* 311 */     LOWERCASE_STARTS.add("vi");
/* 312 */     LOWERCASE_STARTS.add("vii");
/* 313 */     LOWERCASE_STARTS.add("viii");
/* 314 */     LOWERCASE_STARTS.add("ix");
/* 315 */     LOWERCASE_STARTS.add("x");
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 8384392069391677984L;
/*     */ 
/*     */     public void writeExternal(ObjectOutput out)
/*     */     {
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */     {
/* 333 */       return MedlineSentenceModel.INSTANCE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.sentences.MedlineSentenceModel
 * JD-Core Version:    0.6.2
 */