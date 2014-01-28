/*     */ package org.apache.lucene.analysis.hunspell;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class HunspellAffix
/*     */ {
/*     */   private String append;
/*     */   private char[] appendFlags;
/*     */   private String strip;
/*     */   private String condition;
/*     */   private Pattern conditionPattern;
/*     */   private char flag;
/*     */   private boolean crossProduct;
/*     */ 
/*     */   public boolean checkCondition(CharSequence text)
/*     */   {
/*  45 */     return this.conditionPattern.matcher(text).matches();
/*     */   }
/*     */ 
/*     */   public String getAppend()
/*     */   {
/*  54 */     return this.append;
/*     */   }
/*     */ 
/*     */   public void setAppend(String append)
/*     */   {
/*  63 */     this.append = append;
/*     */   }
/*     */ 
/*     */   public char[] getAppendFlags()
/*     */   {
/*  72 */     return this.appendFlags;
/*     */   }
/*     */ 
/*     */   public void setAppendFlags(char[] appendFlags)
/*     */   {
/*  81 */     this.appendFlags = appendFlags;
/*     */   }
/*     */ 
/*     */   public String getStrip()
/*     */   {
/*  90 */     return this.strip;
/*     */   }
/*     */ 
/*     */   public void setStrip(String strip)
/*     */   {
/*  99 */     this.strip = strip;
/*     */   }
/*     */ 
/*     */   public String getCondition()
/*     */   {
/* 108 */     return this.condition;
/*     */   }
/*     */ 
/*     */   public void setCondition(String condition, String pattern)
/*     */   {
/* 118 */     this.condition = condition;
/* 119 */     this.conditionPattern = Pattern.compile(pattern);
/*     */   }
/*     */ 
/*     */   public char getFlag()
/*     */   {
/* 128 */     return this.flag;
/*     */   }
/*     */ 
/*     */   public void setFlag(char flag)
/*     */   {
/* 137 */     this.flag = flag;
/*     */   }
/*     */ 
/*     */   public boolean isCrossProduct()
/*     */   {
/* 146 */     return this.crossProduct;
/*     */   }
/*     */ 
/*     */   public void setCrossProduct(boolean crossProduct)
/*     */   {
/* 155 */     this.crossProduct = crossProduct;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hunspell.HunspellAffix
 * JD-Core Version:    0.6.2
 */