/*     */ package org.apache.lucene.analysis.ar;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.LetterTokenizer;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ @Deprecated
/*     */ public class ArabicLetterTokenizer extends LetterTokenizer
/*     */ {
/*     */   public ArabicLetterTokenizer(Version matchVersion, Reader in)
/*     */   {
/*  57 */     super(matchVersion, in);
/*     */   }
/*     */ 
/*     */   public ArabicLetterTokenizer(Version matchVersion, AttributeSource source, Reader in)
/*     */   {
/*  71 */     super(matchVersion, source, in);
/*     */   }
/*     */ 
/*     */   public ArabicLetterTokenizer(Version matchVersion, AttributeSource.AttributeFactory factory, Reader in)
/*     */   {
/*  86 */     super(matchVersion, factory, in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ArabicLetterTokenizer(Reader in)
/*     */   {
/*  97 */     super(in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ArabicLetterTokenizer(AttributeSource source, Reader in)
/*     */   {
/* 108 */     super(source, in);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ArabicLetterTokenizer(AttributeSource.AttributeFactory factory, Reader in)
/*     */   {
/* 120 */     super(factory, in);
/*     */   }
/*     */ 
/*     */   protected boolean isTokenChar(int c)
/*     */   {
/* 130 */     return (super.isTokenChar(c)) || (Character.getType(c) == 6);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ar.ArabicLetterTokenizer
 * JD-Core Version:    0.6.2
 */