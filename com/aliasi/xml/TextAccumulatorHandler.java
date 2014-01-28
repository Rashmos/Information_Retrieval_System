/*     */ package com.aliasi.xml;
/*     */ 
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class TextAccumulatorHandler extends DefaultHandler
/*     */ {
/*     */   private int mMinBufLength;
/*     */   private StringBuilder mBuf;
/*     */ 
/*     */   public TextAccumulatorHandler()
/*     */   {
/*  40 */     this(128);
/*     */   }
/*     */ 
/*     */   public TextAccumulatorHandler(int minBufLength)
/*     */   {
/*  50 */     this.mMinBufLength = minBufLength;
/*  51 */     this.mBuf = new StringBuilder(this.mMinBufLength);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  59 */     this.mBuf = new StringBuilder(this.mMinBufLength);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/*  67 */     reset();
/*     */   }
/*     */ 
/*     */   public void characters(char[] cs, int start, int length)
/*     */   {
/*  79 */     if (this.mBuf == null) return;
/*  80 */     this.mBuf.append(cs, start, length);
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/*  89 */     return this.mBuf.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 100 */     return getText();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.xml.TextAccumulatorHandler
 * JD-Core Version:    0.6.2
 */