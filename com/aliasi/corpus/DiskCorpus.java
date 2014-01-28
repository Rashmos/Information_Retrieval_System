/*     */ package com.aliasi.corpus;
/*     */ 
/*     */ import com.aliasi.util.Streams;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class DiskCorpus<H extends Handler> extends Corpus<H>
/*     */ {
/*     */   private final Parser<H> mParser;
/*     */   private final File mTrainDir;
/*     */   private final File mTestDir;
/*  49 */   private String mCharEncoding = null;
/*  50 */   private String mSystemId = null;
/*     */   public static final String DEFAULT_TRAIN_DIR_NAME = "train";
/*     */   public static final String DEFAULT_TEST_DIR_NAME = "test";
/*     */ 
/*     */   public DiskCorpus(Parser<H> parser, File dir)
/*     */   {
/*  67 */     this(parser, new File(dir, "train"), new File(dir, "test"));
/*     */   }
/*     */ 
/*     */   public DiskCorpus(Parser<H> parser, File trainDir, File testDir)
/*     */   {
/*  85 */     this.mParser = parser;
/*  86 */     this.mTrainDir = trainDir;
/*  87 */     this.mTestDir = testDir;
/*     */   }
/*     */ 
/*     */   public void setCharEncoding(String encoding)
/*     */   {
/*  99 */     this.mCharEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public String getCharEncoding()
/*     */   {
/* 109 */     return this.mCharEncoding;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 123 */     this.mSystemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 134 */     return this.mSystemId;
/*     */   }
/*     */ 
/*     */   public Parser<H> parser()
/*     */   {
/* 143 */     return this.mParser;
/*     */   }
/*     */ 
/*     */   public void visitTrain(H handler)
/*     */     throws IOException
/*     */   {
/* 158 */     visit(handler, this.mTrainDir);
/*     */   }
/*     */ 
/*     */   public void visitTest(H handler)
/*     */     throws IOException
/*     */   {
/* 172 */     visit(handler, this.mTestDir);
/*     */   }
/*     */ 
/*     */   private void visit(H handler, File file) throws IOException
/*     */   {
/* 177 */     Parser parser = parser();
/* 178 */     parser.setHandler(handler);
/* 179 */     visit(parser, file);
/*     */   }
/*     */ 
/*     */   private void visit(Parser<H> parser, File file)
/*     */     throws IOException
/*     */   {
/* 199 */     if (file.isDirectory())
/* 200 */       visitDir(parser, file);
/* 201 */     else if (file.getName().endsWith(".gz"))
/* 202 */       visitGzip(parser, file);
/* 203 */     else if (file.getName().endsWith(".zip"))
/* 204 */       visitZip(parser, file);
/*     */     else
/* 206 */       visitOrdinaryFile(parser, file);
/*     */   }
/*     */ 
/*     */   private void visitDir(Parser<H> parser, File dir)
/*     */     throws IOException
/*     */   {
/* 212 */     File[] files = dir.listFiles();
/* 213 */     for (int i = 0; i < files.length; i++)
/* 214 */       visit(parser, files[i]);
/*     */   }
/*     */ 
/*     */   private void visitGzip(Parser<H> parser, File gzipFile)
/*     */     throws IOException
/*     */   {
/* 220 */     FileInputStream fileIn = null;
/* 221 */     BufferedInputStream bufIn = null;
/* 222 */     GZIPInputStream gzipIn = null;
/*     */     try {
/* 224 */       fileIn = new FileInputStream(gzipFile);
/* 225 */       bufIn = new BufferedInputStream(bufIn);
/* 226 */       gzipIn = new GZIPInputStream(bufIn);
/* 227 */       InputSource inSource = new InputSource(gzipIn);
/* 228 */       configure(inSource, gzipFile);
/* 229 */       parser.parse(inSource);
/*     */     } finally {
/* 231 */       Streams.closeInputStream(gzipIn);
/* 232 */       Streams.closeInputStream(bufIn);
/* 233 */       Streams.closeInputStream(fileIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visitZip(Parser<H> parser, File zipFile)
/*     */     throws IOException
/*     */   {
/* 240 */     FileInputStream fileIn = null;
/* 241 */     BufferedInputStream bufIn = null;
/* 242 */     ZipInputStream zipIn = null;
/*     */     try {
/* 244 */       fileIn = new FileInputStream(zipFile);
/* 245 */       bufIn = new BufferedInputStream(bufIn);
/* 246 */       zipIn = new ZipInputStream(bufIn);
/* 247 */       ZipEntry entry = null;
/* 248 */       while ((entry = zipIn.getNextEntry()) != null)
/* 249 */         if (!entry.isDirectory()) {
/* 250 */           InputSource inSource = new InputSource(zipIn);
/* 251 */           configure(inSource, zipFile);
/* 252 */           parser.parse(inSource);
/*     */         }
/*     */     }
/*     */     finally {
/* 256 */       Streams.closeInputStream(zipIn);
/* 257 */       Streams.closeInputStream(bufIn);
/* 258 */       Streams.closeInputStream(fileIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void visitOrdinaryFile(Parser<H> parser, File file)
/*     */     throws IOException
/*     */   {
/* 265 */     InputSource in = new InputSource(file.toURI().toURL().toString());
/* 266 */     configure(in, file);
/* 267 */     parser.parse(in);
/*     */   }
/*     */ 
/*     */   private void configure(InputSource inSource, File file)
/*     */     throws IOException
/*     */   {
/* 274 */     inSource.setSystemId(this.mSystemId == null ? file.toURI().toURL().toString() : this.mSystemId);
/*     */ 
/* 277 */     if (this.mCharEncoding != null)
/* 278 */       inSource.setEncoding(this.mCharEncoding);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.DiskCorpus
 * JD-Core Version:    0.6.2
 */