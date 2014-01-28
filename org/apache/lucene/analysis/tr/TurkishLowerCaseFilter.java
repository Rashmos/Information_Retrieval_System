/*     */ package org.apache.lucene.analysis.tr;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ 
/*     */ public final class TurkishLowerCaseFilter extends TokenFilter
/*     */ {
/*     */   private static final int LATIN_CAPITAL_LETTER_I = 73;
/*     */   private static final int LATIN_SMALL_LETTER_I = 105;
/*     */   private static final int LATIN_SMALL_LETTER_DOTLESS_I = 305;
/*     */   private static final int COMBINING_DOT_ABOVE = 775;
/*  40 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*     */ 
/*     */   public TurkishLowerCaseFilter(TokenStream in)
/*     */   {
/*  49 */     super(in);
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken() throws IOException
/*     */   {
/*  54 */     boolean iOrAfter = false;
/*     */ 
/*  56 */     if (this.input.incrementToken()) {
/*  57 */       char[] buffer = this.termAtt.buffer();
/*  58 */       int length = this.termAtt.length();
/*  59 */       for (int i = 0; i < length; ) {
/*  60 */         int ch = Character.codePointAt(buffer, i);
/*     */ 
/*  62 */         iOrAfter = (ch == 73) || ((iOrAfter) && (Character.getType(ch) == 6));
/*     */ 
/*  65 */         if (iOrAfter) {
/*  66 */           switch (ch)
/*     */           {
/*     */           case 775:
/*  69 */             length = delete(buffer, i, length);
/*  70 */             break;
/*     */           case 73:
/*  74 */             if (isBeforeDot(buffer, i + 1, length)) {
/*  75 */               buffer[i] = 'i';
/*     */             } else {
/*  77 */               buffer[i] = 'ı';
/*     */ 
/*  80 */               iOrAfter = false;
/*     */             }
/*  82 */             i++;
/*  83 */             break;
/*     */           }
/*     */         }
/*     */         else {
/*  87 */           i += Character.toChars(Character.toLowerCase(ch), buffer, i);
/*     */         }
/*     */       }
/*  90 */       this.termAtt.setLength(length);
/*  91 */       return true;
/*     */     }
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isBeforeDot(char[] s, int pos, int len)
/*     */   {
/* 102 */     for (int i = pos; i < len; ) {
/* 103 */       int ch = Character.codePointAt(s, i);
/* 104 */       if (Character.getType(ch) != 6)
/* 105 */         return false;
/* 106 */       if (ch == 775)
/* 107 */         return true;
/* 108 */       i += Character.charCount(ch);
/*     */     }
/*     */ 
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   private int delete(char[] s, int pos, int len)
/*     */   {
/* 119 */     if (pos < len) {
/* 120 */       System.arraycopy(s, pos + 1, s, pos, len - pos - 1);
/*     */     }
/* 122 */     return len - 1;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tr.TurkishLowerCaseFilter
 * JD-Core Version:    0.6.2
 */