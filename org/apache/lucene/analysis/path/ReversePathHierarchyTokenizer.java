/*     */ package org.apache.lucene.analysis.path;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ 
/*     */ public class ReversePathHierarchyTokenizer extends Tokenizer
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 1024;
/*     */   public static final char DEFAULT_DELIMITER = '/';
/*     */   public static final int DEFAULT_SKIP = 0;
/*     */   private final char delimiter;
/*     */   private final char replacement;
/*     */   private final int skip;
/*  96 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  97 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*  98 */   private final PositionIncrementAttribute posAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/*     */ 
/* 100 */   private int endPosition = 0;
/* 101 */   private int finalOffset = 0;
/* 102 */   private int skipped = 0;
/*     */   private StringBuilder resultToken;
/*     */   private List<Integer> delimiterPositions;
/* 106 */   private int delimitersCount = -1;
/*     */   private char[] resultTokenBuffer;
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input)
/*     */   {
/*  50 */     this(input, 1024, '/', '/', 0);
/*     */   }
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input, int skip) {
/*  54 */     this(input, 1024, '/', '/', skip);
/*     */   }
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input, int bufferSize, char delimiter) {
/*  58 */     this(input, bufferSize, delimiter, delimiter, 0);
/*     */   }
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input, char delimiter, char replacement) {
/*  62 */     this(input, 1024, delimiter, replacement, 0);
/*     */   }
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input, int bufferSize, char delimiter, char replacement) {
/*  66 */     this(input, bufferSize, delimiter, replacement, 0);
/*     */   }
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input, char delimiter, int skip) {
/*  70 */     this(input, 1024, delimiter, delimiter, skip);
/*     */   }
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input, char delimiter, char replacement, int skip) {
/*  74 */     this(input, 1024, delimiter, replacement, skip);
/*     */   }
/*     */ 
/*     */   public ReversePathHierarchyTokenizer(Reader input, int bufferSize, char delimiter, char replacement, int skip) {
/*  78 */     super(input);
/*  79 */     this.termAtt.resizeBuffer(bufferSize);
/*  80 */     this.delimiter = delimiter;
/*  81 */     this.replacement = replacement;
/*  82 */     this.skip = skip;
/*  83 */     this.resultToken = new StringBuilder(bufferSize);
/*  84 */     this.resultTokenBuffer = new char[bufferSize];
/*  85 */     this.delimiterPositions = new ArrayList(bufferSize / 10);
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken()
/*     */     throws IOException
/*     */   {
/* 111 */     clearAttributes();
/* 112 */     if (this.delimitersCount == -1) {
/* 113 */       int length = 0;
/* 114 */       this.delimiterPositions.add(Integer.valueOf(0));
/*     */       while (true) {
/* 116 */         int c = this.input.read();
/* 117 */         if (c < 0) {
/*     */           break;
/*     */         }
/* 120 */         length++;
/* 121 */         if (c == this.delimiter) {
/* 122 */           this.delimiterPositions.add(Integer.valueOf(length));
/* 123 */           this.resultToken.append(this.replacement);
/*     */         }
/*     */         else {
/* 126 */           this.resultToken.append((char)c);
/*     */         }
/*     */       }
/* 129 */       this.delimitersCount = this.delimiterPositions.size();
/* 130 */       if (((Integer)this.delimiterPositions.get(this.delimitersCount - 1)).intValue() < length) {
/* 131 */         this.delimiterPositions.add(Integer.valueOf(length));
/* 132 */         this.delimitersCount += 1;
/*     */       }
/* 134 */       if (this.resultTokenBuffer.length < this.resultToken.length()) {
/* 135 */         this.resultTokenBuffer = new char[this.resultToken.length()];
/*     */       }
/* 137 */       this.resultToken.getChars(0, this.resultToken.length(), this.resultTokenBuffer, 0);
/* 138 */       this.resultToken.setLength(0);
/* 139 */       this.endPosition = ((Integer)this.delimiterPositions.get(this.delimitersCount - 1 - this.skip)).intValue();
/* 140 */       this.finalOffset = correctOffset(length);
/* 141 */       this.posAtt.setPositionIncrement(1);
/*     */     }
/*     */     else {
/* 144 */       this.posAtt.setPositionIncrement(0);
/*     */     }
/*     */ 
/* 147 */     if (this.skipped < this.delimitersCount - this.skip - 1) {
/* 148 */       int start = ((Integer)this.delimiterPositions.get(this.skipped)).intValue();
/* 149 */       this.termAtt.copyBuffer(this.resultTokenBuffer, start, this.endPosition - start);
/* 150 */       this.offsetAtt.setOffset(correctOffset(start), correctOffset(this.endPosition));
/* 151 */       this.skipped += 1;
/* 152 */       return true;
/*     */     }
/*     */ 
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   public final void end()
/*     */   {
/* 161 */     this.offsetAtt.setOffset(this.finalOffset, this.finalOffset);
/*     */   }
/*     */ 
/*     */   public void reset(Reader input) throws IOException
/*     */   {
/* 166 */     super.reset(input);
/* 167 */     this.resultToken.setLength(0);
/* 168 */     this.finalOffset = 0;
/* 169 */     this.skipped = 0;
/* 170 */     this.delimitersCount = -1;
/* 171 */     this.delimiterPositions.clear();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.path.ReversePathHierarchyTokenizer
 * JD-Core Version:    0.6.2
 */