/*     */ package com.aliasi.tag;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.corpus.StringParser;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class LineTaggingParser extends StringParser<ObjectHandler<Tagging<String>>>
/*     */ {
/*     */   private final Pattern mTokenTagPattern;
/*     */   private final Pattern mIgnoreLinePattern;
/*     */   private final Pattern mEosPattern;
/*     */   private final int mTokenGroup;
/*     */   private final int mTagGroup;
/*     */ 
/*     */   public LineTaggingParser(String matchRegex, int tokenGroup, int tagGroup, String ignoreRegex, String eosRegex)
/*     */   {
/* 127 */     super(null);
/* 128 */     this.mTokenTagPattern = Pattern.compile(matchRegex);
/* 129 */     this.mTokenGroup = tokenGroup;
/* 130 */     this.mTagGroup = tagGroup;
/* 131 */     this.mIgnoreLinePattern = Pattern.compile(ignoreRegex);
/* 132 */     this.mEosPattern = Pattern.compile(eosRegex);
/*     */   }
/*     */ 
/*     */   public void parseString(char[] cs, int start, int end)
/*     */   {
/* 137 */     int i = -1;
/* 138 */     String line = null;
/*     */     try {
/* 140 */       String in = new String(cs, start, end - start);
/* 141 */       String[] lines = in.split("\n");
/* 142 */       List tokenList = new ArrayList();
/* 143 */       List tagList = new ArrayList();
/* 144 */       for (i = 0; i < lines.length; i++) {
/* 145 */         line = lines[i];
/* 146 */         Matcher lineIgnorer = this.mIgnoreLinePattern.matcher(lines[i]);
/* 147 */         if (!lineIgnorer.matches())
/*     */         {
/* 149 */           Matcher eosMatcher = this.mEosPattern.matcher(lines[i]);
/* 150 */           if (eosMatcher.matches()) {
/* 151 */             handle(tokenList, tagList);
/*     */           }
/*     */           else
/*     */           {
/* 155 */             Matcher matcher = this.mTokenTagPattern.matcher(lines[i]);
/* 156 */             if (!matcher.matches()) {
/* 157 */               String msg = "Illegal frmat around line=" + i + " line=|" + lines[i] + "|";
/* 158 */               throw new IllegalArgumentException(msg);
/*     */             }
/*     */ 
/* 161 */             String token = matcher.group(this.mTokenGroup);
/* 162 */             String tag = matcher.group(this.mTagGroup);
/* 163 */             tokenList.add(token);
/* 164 */             tagList.add(tag);
/*     */           }
/*     */         }
/*     */       }
/* 166 */       handle(tokenList, tagList);
/*     */     }
/*     */     catch (IllegalArgumentException e) {
/* 169 */       throw new IllegalArgumentException("Parsing exception around line=" + i + " line=|" + line + "|", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   void handle(List<String> tokenList, List<String> tagList)
/*     */   {
/* 175 */     Tagging tagging = new Tagging(tokenList, tagList);
/* 176 */     ((ObjectHandler)getHandler()).handle(tagging);
/* 177 */     tokenList.clear();
/* 178 */     tagList.clear();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.LineTaggingParser
 * JD-Core Version:    0.6.2
 */