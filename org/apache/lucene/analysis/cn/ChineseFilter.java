/*     */ package org.apache.lucene.analysis.cn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ @Deprecated
/*     */ public final class ChineseFilter extends TokenFilter
/*     */ {
/*  53 */   public static final String[] STOP_WORDS = { "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with" };
/*     */   private CharArraySet stopTable;
/*  64 */   private CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*     */ 
/*     */   public ChineseFilter(TokenStream in) {
/*  67 */     super(in);
/*     */ 
/*  69 */     this.stopTable = new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(STOP_WORDS), false);
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*  75 */     while (this.input.incrementToken()) {
/*  76 */       char[] text = this.termAtt.buffer();
/*  77 */       int termLength = this.termAtt.length();
/*     */ 
/*  80 */       if (!this.stopTable.contains(text, 0, termLength)) {
/*  81 */         switch (Character.getType(text[0]))
/*     */         {
/*     */         case 1:
/*     */         case 2:
/*  87 */           if (termLength > 1) {
/*  88 */             return true;
/*     */           }
/*     */ 
/*     */           break;
/*     */         case 5:
/*  96 */           return true;
/*     */         case 3:
/*     */         case 4:
/*     */         }
/*     */       }
/*     */     }
/* 102 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cn.ChineseFilter
 * JD-Core Version:    0.6.2
 */