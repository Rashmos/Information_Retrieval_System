/*     */ package com.aliasi.tag;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public class StringTagging extends Tagging<String>
/*     */ {
/*     */   private final String mCs;
/*     */   private final int[] mTokenStarts;
/*     */   private final int[] mTokenEnds;
/*     */ 
/*     */   public StringTagging(List<String> tokens, List<String> tags, CharSequence cs, int[] tokenStarts, int[] tokenEnds)
/*     */   {
/*  64 */     super(tokens, tags);
/*  65 */     if (tokenStarts.length != tokens.size()) {
/*  66 */       String msg = "Token starts must be same length as tokens. Found tokens.size()=" + tokens.size() + " tokenStarts.length=" + tokenStarts.length;
/*     */ 
/*  69 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  71 */     if (tokenEnds.length != tokens.size()) {
/*  72 */       String msg = "Token ends must be same length as tokens. Found tokens.size()=" + tokens.size() + " tokenEnds.length=" + tokenEnds.length;
/*     */ 
/*  75 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  77 */     for (int n = 0; n < tokenStarts.length; n++) {
/*  78 */       if (tokenStarts[n] > tokenEnds[n]) {
/*  79 */         String msg = "Tokens must start before they end. tokenStarts[" + n + "]=" + tokenStarts[n] + " tokenEnds[n" + n + "]=" + tokenEnds[n];
/*     */ 
/*  82 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/*  85 */     for (int n = 1; n < tokenStarts.length; n++) {
/*  86 */       if (tokenStarts[(n - 1)] > tokenStarts[n]) {
/*  87 */         String msg = "Token starts must be in ascending order. Found tokenStarts[" + (n - 1) + "]=" + tokenStarts[(n - 1)] + " tokenStarts[" + n + "]=" + tokenStarts[n];
/*     */ 
/*  90 */         throw new IllegalArgumentException(msg);
/*     */       }
/*  92 */       if (tokenEnds[(n - 1)] > tokenEnds[n]) {
/*  93 */         String msg = "Token ends must be in ascending order. Found tokenEnds[" + (n - 1) + "]=" + tokenEnds[(n - 1)] + " tokenEnds[" + n + "]=" + tokenEnds[n];
/*     */ 
/*  96 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/*  99 */     if (tokenStarts.length > 0) {
/* 100 */       if (tokenStarts[0] < 0) {
/* 101 */         String msg = "Token starts must be >= 0. Found tokenStarts[0]=" + tokenStarts[0];
/*     */ 
/* 103 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 105 */       if (tokenEnds[(tokenEnds.length - 1)] > cs.length()) {
/* 106 */         String msg = "Tokens must fall within span of chars. Found cs=" + cs + " cs.length()=" + cs.length() + " tokenEnds[" + (tokenEnds.length - 1) + "]=" + tokenEnds[(tokenEnds.length - 1)];
/*     */ 
/* 111 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 114 */     this.mCs = cs.toString();
/* 115 */     this.mTokenStarts = ((int[])tokenStarts.clone());
/* 116 */     this.mTokenEnds = ((int[])tokenEnds.clone());
/*     */   }
/*     */ 
/*     */   public StringTagging(List<String> tokens, List<String> tags, CharSequence cs, List<Integer> tokenStarts, List<Integer> tokenEnds)
/*     */   {
/* 143 */     this(tokens, tags, cs, toArray(tokenStarts), toArray(tokenEnds));
/*     */   }
/*     */ 
/*     */   static int[] toArray(List<Integer> xs) {
/* 147 */     int[] result = new int[xs.size()];
/* 148 */     for (int i = 0; i < xs.size(); i++)
/* 149 */       result[i] = ((Integer)xs.get(i)).intValue();
/* 150 */     return result;
/*     */   }
/*     */ 
/*     */   StringTagging(String s, List<String> tokens, List<String> tags, int[] tokenStarts, int[] tokenEnds, boolean ignore)
/*     */   {
/* 160 */     super(tokens, tags, ignore);
/* 161 */     this.mCs = s;
/* 162 */     this.mTokenStarts = tokenStarts;
/* 163 */     this.mTokenEnds = tokenEnds;
/*     */   }
/*     */ 
/*     */   public int tokenStart(int n)
/*     */   {
/* 175 */     return this.mTokenStarts[n];
/*     */   }
/*     */ 
/*     */   public int tokenEnd(int n)
/*     */   {
/* 187 */     return this.mTokenEnds[n];
/*     */   }
/*     */ 
/*     */   public String rawToken(int n)
/*     */   {
/* 198 */     return this.mCs.substring(tokenStart(n), tokenEnd(n));
/*     */   }
/*     */ 
/*     */   public String characters()
/*     */   {
/* 207 */     return this.mCs;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 233 */     StringBuilder sb = new StringBuilder();
/* 234 */     sb.append(this.mCs);
/* 235 */     sb.append('\n');
/* 236 */     for (int i = 0; i < size(); i++) {
/* 237 */       if (i > 0) sb.append(" ");
/* 238 */       sb.append((String)token(i) + "/" + tag(i) + "//" + rawToken(i) + "///@(" + tokenStart(i) + "," + tokenEnd(i) + ")");
/*     */     }
/* 240 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 256 */     if (this == that) return true;
/* 257 */     if (!(that instanceof StringTagging))
/* 258 */       return false;
/* 259 */     StringTagging thatTagging = (StringTagging)that;
/* 260 */     if (!characters().equals(thatTagging.characters()))
/* 261 */       return false;
/* 262 */     if (size() != thatTagging.size())
/* 263 */       return false;
/* 264 */     for (int n = 0; n < size(); n++) {
/* 265 */       if (!((String)token(n)).equals(thatTagging.token(n)))
/* 266 */         return false;
/* 267 */       if (!tag(n).equals(thatTagging.tag(n)))
/* 268 */         return false;
/* 269 */       if (tokenStart(n) != thatTagging.tokenStart(n))
/* 270 */         return false;
/* 271 */       if (tokenEnd(n) != thatTagging.tokenEnd(n))
/* 272 */         return false;
/*     */     }
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 298 */     int c = characters().hashCode();
/* 299 */     for (int n = 0; n < size(); n++)
/* 300 */       c = 31 * c + tag(n).hashCode();
/* 301 */     return c;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.StringTagging
 * JD-Core Version:    0.6.2
 */