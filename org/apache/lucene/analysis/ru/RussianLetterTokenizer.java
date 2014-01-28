/*     */ package org.apache.lucene.analysis.ru;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.CharTokenizer;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ @Deprecated
/*     */ public class RussianLetterTokenizer extends CharTokenizer
/*     */ {
/*     */   private static final int DIGIT_0 = 48;
/*     */   private static final int DIGIT_9 = 57;
/*     */ 
/*     */   public RussianLetterTokenizer(Version matchVersion, Reader in)
/*     */   {
/*  57 */     super(matchVersion, in);
/*     */   }
/*     */ 
/*     */   public RussianLetterTokenizer(Version matchVersion, AttributeSource source, Reader in)
/*     */   {
/*  71 */     super(matchVersion, source, in);
/*     */   }
/*     */ 
/*     */   public RussianLetterTokenizer(Version matchVersion, AttributeSource.AttributeFactory factory, Reader in)
/*     */   {
/*  86 */     super(matchVersion, factory, in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public RussianLetterTokenizer(Reader in)
/*     */   {
/*  97 */     super(in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public RussianLetterTokenizer(AttributeSource source, Reader in)
/*     */   {
/* 108 */     super(source, in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public RussianLetterTokenizer(AttributeSource.AttributeFactory factory, Reader in)
/*     */   {
/* 120 */     super(factory, in);
/*     */   }
/*     */ 
/*     */   protected boolean isTokenChar(int c)
/*     */   {
/* 130 */     return (Character.isLetter(c)) || ((c >= 48) && (c <= 57));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ru.RussianLetterTokenizer
 * JD-Core Version:    0.6.2
 */