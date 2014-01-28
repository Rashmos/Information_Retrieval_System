/*     */ package org.apache.lucene.analysis.path;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ 
/*     */ public class PathHierarchyTokenizer extends Tokenizer
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 1024;
/*     */   public static final char DEFAULT_DELIMITER = '/';
/*     */   public static final int DEFAULT_SKIP = 0;
/*     */   private final char delimiter;
/*     */   private final char replacement;
/*     */   private final int skip;
/*  83 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  84 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*  85 */   private final PositionIncrementAttribute posAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/*  86 */   private int startPosition = 0;
/*  87 */   private int finalOffset = 0;
/*  88 */   private int skipped = 0;
/*  89 */   private boolean endDelimiter = false;
/*     */   private StringBuilder resultToken;
/*     */ 
/*     */   public PathHierarchyTokenizer(Reader input)
/*     */   {
/*  46 */     this(input, 1024, '/', '/', 0);
/*     */   }
/*     */ 
/*     */   public PathHierarchyTokenizer(Reader input, int skip) {
/*  50 */     this(input, 1024, '/', '/', skip);
/*     */   }
/*     */ 
/*     */   public PathHierarchyTokenizer(Reader input, int bufferSize, char delimiter) {
/*  54 */     this(input, bufferSize, delimiter, delimiter, 0);
/*     */   }
/*     */ 
/*     */   public PathHierarchyTokenizer(Reader input, char delimiter, char replacement) {
/*  58 */     this(input, 1024, delimiter, replacement, 0);
/*     */   }
/*     */ 
/*     */   public PathHierarchyTokenizer(Reader input, char delimiter, char replacement, int skip) {
/*  62 */     this(input, 1024, delimiter, replacement, skip);
/*     */   }
/*     */ 
/*     */   public PathHierarchyTokenizer(Reader input, int bufferSize, char delimiter, char replacement, int skip) {
/*  66 */     super(input);
/*  67 */     this.termAtt.resizeBuffer(bufferSize);
/*     */ 
/*  69 */     this.delimiter = delimiter;
/*  70 */     this.replacement = replacement;
/*  71 */     this.skip = skip;
/*  72 */     this.resultToken = new StringBuilder(bufferSize);
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*  95 */     clearAttributes();
/*  96 */     this.termAtt.append(this.resultToken);
/*  97 */     if (this.resultToken.length() == 0) {
/*  98 */       this.posAtt.setPositionIncrement(1);
/*     */     }
/*     */     else {
/* 101 */       this.posAtt.setPositionIncrement(0);
/*     */     }
/* 103 */     int length = 0;
/* 104 */     boolean added = false;
/* 105 */     if (this.endDelimiter) {
/* 106 */       this.termAtt.append(this.replacement);
/* 107 */       length++;
/* 108 */       this.endDelimiter = false;
/* 109 */       added = true;
/*     */     }
/*     */     while (true)
/*     */     {
/* 113 */       int c = this.input.read();
/* 114 */       if (c < 0) {
/* 115 */         if (this.skipped > this.skip) {
/* 116 */           length += this.resultToken.length();
/* 117 */           this.termAtt.setLength(length);
/* 118 */           this.finalOffset = correctOffset(this.startPosition + length);
/* 119 */           this.offsetAtt.setOffset(correctOffset(this.startPosition), this.finalOffset);
/* 120 */           if (added) {
/* 121 */             this.resultToken.setLength(0);
/* 122 */             this.resultToken.append(this.termAtt.buffer(), 0, length);
/*     */           }
/* 124 */           return added;
/*     */         }
/*     */ 
/* 127 */         this.finalOffset = correctOffset(this.startPosition + length);
/* 128 */         return false;
/*     */       }
/*     */ 
/* 131 */       if (!added) {
/* 132 */         added = true;
/* 133 */         this.skipped += 1;
/* 134 */         if (this.skipped > this.skip) {
/* 135 */           this.termAtt.append(c == this.delimiter ? this.replacement : (char)c);
/* 136 */           length++;
/*     */         }
/*     */         else {
/* 139 */           this.startPosition += 1;
/*     */         }
/*     */ 
/*     */       }
/* 143 */       else if (c == this.delimiter) {
/* 144 */         if (this.skipped > this.skip) {
/* 145 */           this.endDelimiter = true;
/* 146 */           break;
/*     */         }
/* 148 */         this.skipped += 1;
/* 149 */         if (this.skipped > this.skip) {
/* 150 */           this.termAtt.append(this.replacement);
/* 151 */           length++;
/*     */         }
/*     */         else {
/* 154 */           this.startPosition += 1;
/*     */         }
/*     */ 
/*     */       }
/* 158 */       else if (this.skipped > this.skip) {
/* 159 */         this.termAtt.append((char)c);
/* 160 */         length++;
/*     */       }
/*     */       else {
/* 163 */         this.startPosition += 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 168 */     length += this.resultToken.length();
/* 169 */     this.termAtt.setLength(length);
/* 170 */     this.finalOffset = correctOffset(this.startPosition + length);
/* 171 */     this.offsetAtt.setOffset(correctOffset(this.startPosition), this.finalOffset);
/* 172 */     this.resultToken.setLength(0);
/* 173 */     this.resultToken.append(this.termAtt.buffer(), 0, length);
/* 174 */     return true;
/*     */   }
/*     */ 
/*     */   public final void end()
/*     */   {
/* 180 */     this.offsetAtt.setOffset(this.finalOffset, this.finalOffset);
/*     */   }
/*     */ 
/*     */   public void reset(Reader input) throws IOException
/*     */   {
/* 185 */     super.reset(input);
/* 186 */     this.resultToken.setLength(0);
/* 187 */     this.finalOffset = 0;
/* 188 */     this.endDelimiter = false;
/* 189 */     this.skipped = 0;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.path.PathHierarchyTokenizer
 * JD-Core Version:    0.6.2
 */