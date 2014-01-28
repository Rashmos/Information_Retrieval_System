/*     */ package com.aliasi.corpus;
/*     */ 
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public abstract class Parser<H extends Handler>
/*     */ {
/*     */   private H mHandler;
/*     */ 
/*     */   public Parser()
/*     */   {
/*  52 */     this(null);
/*     */   }
/*     */ 
/*     */   public Parser(H handler)
/*     */   {
/*  61 */     this.mHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setHandler(H handler)
/*     */   {
/*  72 */     this.mHandler = handler;
/*     */   }
/*     */ 
/*     */   public H getHandler()
/*     */   {
/*  82 */     return this.mHandler;
/*     */   }
/*     */ 
/*     */   public void parse(String sysId)
/*     */     throws IOException
/*     */   {
/*  98 */     InputSource in = new InputSource(sysId);
/*  99 */     parse(in);
/*     */   }
/*     */ 
/*     */   public void parse(File file)
/*     */     throws IOException
/*     */   {
/* 115 */     parse(file.toURI().toURL().toString());
/*     */   }
/*     */ 
/*     */   public void parseString(CharSequence cSeq)
/*     */     throws IOException
/*     */   {
/* 131 */     char[] cs = Strings.toCharArray(cSeq);
/* 132 */     parseString(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public abstract void parse(InputSource paramInputSource)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void parseString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.Parser
 * JD-Core Version:    0.6.2
 */