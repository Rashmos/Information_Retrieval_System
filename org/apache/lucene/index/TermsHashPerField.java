/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/*     */ import org.apache.lucene.document.Fieldable;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ 
/*     */ final class TermsHashPerField extends InvertedDocConsumerPerField
/*     */ {
/*     */   final TermsHashConsumerPerField consumer;
/*     */   final TermsHashPerField nextPerField;
/*     */   final TermsHashPerThread perThread;
/*     */   final DocumentsWriter.DocState docState;
/*     */   final FieldInvertState fieldState;
/*     */   TermAttribute termAtt;
/*     */   final CharBlockPool charPool;
/*     */   final IntBlockPool intPool;
/*     */   final ByteBlockPool bytePool;
/*     */   final int streamCount;
/*     */   final int numPostingInt;
/*     */   final FieldInfo fieldInfo;
/*     */   boolean postingsCompacted;
/*     */   int numPostings;
/*  48 */   private int postingsHashSize = 4;
/*  49 */   private int postingsHashHalfSize = this.postingsHashSize / 2;
/*  50 */   private int postingsHashMask = this.postingsHashSize - 1;
/*  51 */   private RawPostingList[] postingsHash = new RawPostingList[this.postingsHashSize];
/*     */   private RawPostingList p;
/*     */   private boolean doCall;
/*     */   private boolean doNextCall;
/*     */   int[] intUptos;
/*     */   int intUptoStart;
/*     */ 
/*     */   public TermsHashPerField(DocInverterPerField docInverterPerField, TermsHashPerThread perThread, TermsHashPerThread nextPerThread, FieldInfo fieldInfo)
/*     */   {
/*  55 */     this.perThread = perThread;
/*  56 */     this.intPool = perThread.intPool;
/*  57 */     this.charPool = perThread.charPool;
/*  58 */     this.bytePool = perThread.bytePool;
/*  59 */     this.docState = perThread.docState;
/*  60 */     this.fieldState = docInverterPerField.fieldState;
/*  61 */     this.consumer = perThread.consumer.addField(this, fieldInfo);
/*  62 */     this.streamCount = this.consumer.getStreamCount();
/*  63 */     this.numPostingInt = (2 * this.streamCount);
/*  64 */     this.fieldInfo = fieldInfo;
/*  65 */     if (nextPerThread != null)
/*  66 */       this.nextPerField = ((TermsHashPerField)nextPerThread.addField(docInverterPerField, fieldInfo));
/*     */     else
/*  68 */       this.nextPerField = null;
/*     */   }
/*     */ 
/*     */   void shrinkHash(int targetSize) {
/*  72 */     assert ((this.postingsCompacted) || (this.numPostings == 0));
/*     */ 
/*  74 */     int newSize = 4;
/*  75 */     if (4 != this.postingsHash.length) {
/*  76 */       this.postingsHash = new RawPostingList[4];
/*  77 */       this.postingsHashSize = 4;
/*  78 */       this.postingsHashHalfSize = 2;
/*  79 */       this.postingsHashMask = 3;
/*     */     }
/*  81 */     Arrays.fill(this.postingsHash, null);
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  85 */     if (!this.postingsCompacted)
/*  86 */       compactPostings();
/*  87 */     assert (this.numPostings <= this.postingsHash.length);
/*  88 */     if (this.numPostings > 0) {
/*  89 */       this.perThread.termsHash.recyclePostings(this.postingsHash, this.numPostings);
/*  90 */       Arrays.fill(this.postingsHash, 0, this.numPostings, null);
/*  91 */       this.numPostings = 0;
/*     */     }
/*  93 */     this.postingsCompacted = false;
/*  94 */     if (this.nextPerField != null)
/*  95 */       this.nextPerField.reset();
/*     */   }
/*     */ 
/*     */   public synchronized void abort()
/*     */   {
/* 100 */     reset();
/* 101 */     if (this.nextPerField != null)
/* 102 */       this.nextPerField.abort();
/*     */   }
/*     */ 
/*     */   public void initReader(ByteSliceReader reader, RawPostingList p, int stream) {
/* 106 */     assert (stream < this.streamCount);
/* 107 */     int[] ints = this.intPool.buffers[(p.intStart >> 13)];
/* 108 */     int upto = p.intStart & 0x1FFF;
/* 109 */     reader.init(this.bytePool, p.byteStart + stream * ByteBlockPool.FIRST_LEVEL_SIZE, ints[(upto + stream)]);
/*     */   }
/*     */ 
/*     */   private synchronized void compactPostings()
/*     */   {
/* 115 */     int upto = 0;
/* 116 */     for (int i = 0; i < this.postingsHashSize; i++) {
/* 117 */       if (this.postingsHash[i] != null) {
/* 118 */         if (upto < i) {
/* 119 */           this.postingsHash[upto] = this.postingsHash[i];
/* 120 */           this.postingsHash[i] = null;
/*     */         }
/* 122 */         upto++;
/*     */       }
/*     */     }
/*     */ 
/* 126 */     assert (upto == this.numPostings);
/* 127 */     this.postingsCompacted = true;
/*     */   }
/*     */ 
/*     */   public RawPostingList[] sortPostings()
/*     */   {
/* 132 */     compactPostings();
/* 133 */     quickSort(this.postingsHash, 0, this.numPostings - 1);
/* 134 */     return this.postingsHash;
/*     */   }
/*     */ 
/*     */   void quickSort(RawPostingList[] postings, int lo, int hi) {
/* 138 */     if (lo >= hi)
/* 139 */       return;
/* 140 */     if (hi == 1 + lo) {
/* 141 */       if (comparePostings(postings[lo], postings[hi]) > 0) {
/* 142 */         RawPostingList tmp = postings[lo];
/* 143 */         postings[lo] = postings[hi];
/* 144 */         postings[hi] = tmp;
/*     */       }
/* 146 */       return;
/*     */     }
/*     */ 
/* 149 */     int mid = lo + hi >>> 1;
/*     */ 
/* 151 */     if (comparePostings(postings[lo], postings[mid]) > 0) {
/* 152 */       RawPostingList tmp = postings[lo];
/* 153 */       postings[lo] = postings[mid];
/* 154 */       postings[mid] = tmp;
/*     */     }
/*     */ 
/* 157 */     if (comparePostings(postings[mid], postings[hi]) > 0) {
/* 158 */       RawPostingList tmp = postings[mid];
/* 159 */       postings[mid] = postings[hi];
/* 160 */       postings[hi] = tmp;
/*     */ 
/* 162 */       if (comparePostings(postings[lo], postings[mid]) > 0) {
/* 163 */         RawPostingList tmp2 = postings[lo];
/* 164 */         postings[lo] = postings[mid];
/* 165 */         postings[mid] = tmp2;
/*     */       }
/*     */     }
/*     */ 
/* 169 */     int left = lo + 1;
/* 170 */     int right = hi - 1;
/*     */ 
/* 172 */     if (left >= right) {
/* 173 */       return;
/*     */     }
/* 175 */     RawPostingList partition = postings[mid];
/*     */     while (true)
/*     */     {
/* 178 */       if (comparePostings(postings[right], partition) > 0) {
/* 179 */         right--;
/*     */       } else {
/* 181 */         while ((left < right) && (comparePostings(postings[left], partition) <= 0)) {
/* 182 */           left++;
/*     */         }
/* 184 */         if (left >= right) break;
/* 185 */         RawPostingList tmp = postings[left];
/* 186 */         postings[left] = postings[right];
/* 187 */         postings[right] = tmp;
/* 188 */         right--;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 194 */     quickSort(postings, lo, left);
/* 195 */     quickSort(postings, left + 1, hi);
/*     */   }
/*     */ 
/*     */   int comparePostings(RawPostingList p1, RawPostingList p2)
/*     */   {
/* 202 */     if (p1 == p2) {
/* 203 */       return 0;
/*     */     }
/* 205 */     char[] text1 = this.charPool.buffers[(p1.textStart >> 14)];
/* 206 */     int pos1 = p1.textStart & 0x3FFF;
/* 207 */     char[] text2 = this.charPool.buffers[(p2.textStart >> 14)];
/* 208 */     int pos2 = p2.textStart & 0x3FFF;
/*     */ 
/* 210 */     assert ((text1 != text2) || (pos1 != pos2));
/*     */     while (true)
/*     */     {
/* 213 */       char c1 = text1[(pos1++)];
/* 214 */       char c2 = text2[(pos2++)];
/* 215 */       if (c1 != c2) {
/* 216 */         if (65535 == c2)
/* 217 */           return 1;
/* 218 */         if (65535 == c1) {
/* 219 */           return -1;
/*     */         }
/* 221 */         return c1 - c2;
/*     */       }
/*     */ 
/* 225 */       assert (c1 != 65535);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean postingEquals(char[] tokenText, int tokenTextLen)
/*     */   {
/* 233 */     char[] text = this.perThread.charPool.buffers[(this.p.textStart >> 14)];
/* 234 */     assert (text != null);
/* 235 */     int pos = this.p.textStart & 0x3FFF;
/*     */ 
/* 237 */     for (int tokenPos = 0; 
/* 238 */       tokenPos < tokenTextLen; tokenPos++) {
/* 239 */       if (tokenText[tokenPos] != text[pos])
/* 240 */         return false;
/* 238 */       pos++;
/*     */     }
/*     */ 
/* 241 */     return 65535 == text[pos];
/*     */   }
/*     */ 
/*     */   void start(Fieldable f)
/*     */   {
/* 249 */     this.termAtt = ((TermAttribute)this.fieldState.attributeSource.addAttribute(TermAttribute.class));
/* 250 */     this.consumer.start(f);
/* 251 */     if (this.nextPerField != null)
/* 252 */       this.nextPerField.start(f);
/*     */   }
/*     */ 
/*     */   boolean start(Fieldable[] fields, int count)
/*     */     throws IOException
/*     */   {
/* 258 */     this.doCall = this.consumer.start(fields, count);
/* 259 */     if (this.nextPerField != null)
/* 260 */       this.doNextCall = this.nextPerField.start(fields, count);
/* 261 */     return (this.doCall) || (this.doNextCall);
/*     */   }
/*     */ 
/*     */   public void add(int textStart)
/*     */     throws IOException
/*     */   {
/* 269 */     int code = textStart;
/*     */ 
/* 271 */     int hashPos = code & this.postingsHashMask;
/*     */ 
/* 273 */     assert (!this.postingsCompacted);
/*     */ 
/* 276 */     this.p = this.postingsHash[hashPos];
/*     */ 
/* 278 */     if ((this.p != null) && (this.p.textStart != textStart))
/*     */     {
/* 281 */       int inc = (code >> 8) + code | 0x1;
/*     */       do {
/* 283 */         code += inc;
/* 284 */         hashPos = code & this.postingsHashMask;
/* 285 */         this.p = this.postingsHash[hashPos];
/* 286 */       }while ((this.p != null) && (this.p.textStart != textStart));
/*     */     }
/*     */ 
/* 289 */     if (this.p == null)
/*     */     {
/* 295 */       if (0 == this.perThread.freePostingsCount) {
/* 296 */         this.perThread.morePostings();
/*     */       }
/*     */ 
/* 299 */       this.p = this.perThread.freePostings[(--this.perThread.freePostingsCount)];
/* 300 */       assert (this.p != null);
/*     */ 
/* 302 */       this.p.textStart = textStart;
/*     */ 
/* 304 */       assert (this.postingsHash[hashPos] == null);
/* 305 */       this.postingsHash[hashPos] = this.p;
/* 306 */       this.numPostings += 1;
/*     */ 
/* 308 */       if (this.numPostings == this.postingsHashHalfSize) {
/* 309 */         rehashPostings(2 * this.postingsHashSize);
/*     */       }
/*     */ 
/* 312 */       if (this.numPostingInt + this.intPool.intUpto > 8192) {
/* 313 */         this.intPool.nextBuffer();
/*     */       }
/* 315 */       if (32768 - this.bytePool.byteUpto < this.numPostingInt * ByteBlockPool.FIRST_LEVEL_SIZE) {
/* 316 */         this.bytePool.nextBuffer();
/*     */       }
/* 318 */       this.intUptos = this.intPool.buffer;
/* 319 */       this.intUptoStart = this.intPool.intUpto;
/* 320 */       this.intPool.intUpto += this.streamCount;
/*     */ 
/* 322 */       this.p.intStart = (this.intUptoStart + this.intPool.intOffset);
/*     */ 
/* 324 */       for (int i = 0; i < this.streamCount; i++) {
/* 325 */         int upto = this.bytePool.newSlice(ByteBlockPool.FIRST_LEVEL_SIZE);
/* 326 */         this.intUptos[(this.intUptoStart + i)] = (upto + this.bytePool.byteOffset);
/*     */       }
/* 328 */       this.p.byteStart = this.intUptos[this.intUptoStart];
/*     */ 
/* 330 */       this.consumer.newTerm(this.p);
/*     */     }
/*     */     else {
/* 333 */       this.intUptos = this.intPool.buffers[(this.p.intStart >> 13)];
/* 334 */       this.intUptoStart = (this.p.intStart & 0x1FFF);
/* 335 */       this.consumer.addTerm(this.p);
/*     */     }
/*     */   }
/*     */ 
/*     */   void add()
/*     */     throws IOException
/*     */   {
/* 343 */     assert (!this.postingsCompacted);
/*     */ 
/* 349 */     char[] tokenText = this.termAtt.termBuffer();
/* 350 */     int tokenTextLen = this.termAtt.termLength();
/*     */ 
/* 353 */     int downto = tokenTextLen;
/* 354 */     int code = 0;
/* 355 */     while (downto > 0) {
/* 356 */       char ch = tokenText[(--downto)];
/*     */ 
/* 358 */       if ((ch >= 56320) && (ch <= 57343)) {
/* 359 */         if (0 == downto)
/*     */         {
/* 361 */           ch = tokenText[downto] = 65533;
/*     */         } else {
/* 363 */           char ch2 = tokenText[(downto - 1)];
/* 364 */           if ((ch2 >= 55296) && (ch2 <= 56319))
/*     */           {
/* 367 */             code = (code * 31 + ch) * 31 + ch2;
/* 368 */             downto--;
/* 369 */             continue;
/*     */           }
/*     */ 
/* 372 */           ch = tokenText[downto] = 65533;
/*     */         }
/*     */       } else {
/* 375 */         if ((ch >= 55296) && ((ch <= 56319) || (ch == 65535)))
/*     */         {
/* 378 */           ch = tokenText[downto] = 65533;
/*     */         }
/*     */ 
/* 381 */         code = code * 31 + ch;
/*     */       }
/*     */     }
/* 384 */     int hashPos = code & this.postingsHashMask;
/*     */ 
/* 387 */     this.p = this.postingsHash[hashPos];
/*     */ 
/* 389 */     if ((this.p != null) && (!postingEquals(tokenText, tokenTextLen)))
/*     */     {
/* 392 */       int inc = (code >> 8) + code | 0x1;
/*     */       do {
/* 394 */         code += inc;
/* 395 */         hashPos = code & this.postingsHashMask;
/* 396 */         this.p = this.postingsHash[hashPos];
/* 397 */       }while ((this.p != null) && (!postingEquals(tokenText, tokenTextLen)));
/*     */     }
/*     */ 
/* 400 */     if (this.p == null)
/*     */     {
/* 404 */       int textLen1 = 1 + tokenTextLen;
/* 405 */       if (textLen1 + this.charPool.charUpto > 16384) {
/* 406 */         if (textLen1 > 16384)
/*     */         {
/* 413 */           if (this.docState.maxTermPrefix == null) {
/* 414 */             this.docState.maxTermPrefix = new String(tokenText, 0, 30);
/*     */           }
/* 416 */           this.consumer.skippingLongTerm();
/* 417 */           return;
/*     */         }
/* 419 */         this.charPool.nextBuffer();
/*     */       }
/*     */ 
/* 423 */       if (0 == this.perThread.freePostingsCount) {
/* 424 */         this.perThread.morePostings();
/*     */       }
/*     */ 
/* 427 */       this.p = this.perThread.freePostings[(--this.perThread.freePostingsCount)];
/* 428 */       assert (this.p != null);
/*     */ 
/* 430 */       char[] text = this.charPool.buffer;
/* 431 */       int textUpto = this.charPool.charUpto;
/* 432 */       this.p.textStart = (textUpto + this.charPool.charOffset);
/* 433 */       this.charPool.charUpto += textLen1;
/* 434 */       System.arraycopy(tokenText, 0, text, textUpto, tokenTextLen);
/* 435 */       text[(textUpto + tokenTextLen)] = 65535;
/*     */ 
/* 437 */       assert (this.postingsHash[hashPos] == null);
/* 438 */       this.postingsHash[hashPos] = this.p;
/* 439 */       this.numPostings += 1;
/*     */ 
/* 441 */       if (this.numPostings == this.postingsHashHalfSize) {
/* 442 */         rehashPostings(2 * this.postingsHashSize);
/*     */       }
/*     */ 
/* 445 */       if (this.numPostingInt + this.intPool.intUpto > 8192) {
/* 446 */         this.intPool.nextBuffer();
/*     */       }
/* 448 */       if (32768 - this.bytePool.byteUpto < this.numPostingInt * ByteBlockPool.FIRST_LEVEL_SIZE) {
/* 449 */         this.bytePool.nextBuffer();
/*     */       }
/* 451 */       this.intUptos = this.intPool.buffer;
/* 452 */       this.intUptoStart = this.intPool.intUpto;
/* 453 */       this.intPool.intUpto += this.streamCount;
/*     */ 
/* 455 */       this.p.intStart = (this.intUptoStart + this.intPool.intOffset);
/*     */ 
/* 457 */       for (int i = 0; i < this.streamCount; i++) {
/* 458 */         int upto = this.bytePool.newSlice(ByteBlockPool.FIRST_LEVEL_SIZE);
/* 459 */         this.intUptos[(this.intUptoStart + i)] = (upto + this.bytePool.byteOffset);
/*     */       }
/* 461 */       this.p.byteStart = this.intUptos[this.intUptoStart];
/*     */ 
/* 463 */       this.consumer.newTerm(this.p);
/*     */     }
/*     */     else {
/* 466 */       this.intUptos = this.intPool.buffers[(this.p.intStart >> 13)];
/* 467 */       this.intUptoStart = (this.p.intStart & 0x1FFF);
/* 468 */       this.consumer.addTerm(this.p);
/*     */     }
/*     */ 
/* 471 */     if (this.doNextCall)
/* 472 */       this.nextPerField.add(this.p.textStart);
/*     */   }
/*     */ 
/*     */   void writeByte(int stream, byte b)
/*     */   {
/* 479 */     int upto = this.intUptos[(this.intUptoStart + stream)];
/* 480 */     byte[] bytes = this.bytePool.buffers[(upto >> 15)];
/* 481 */     assert (bytes != null);
/* 482 */     int offset = upto & 0x7FFF;
/* 483 */     if (bytes[offset] != 0)
/*     */     {
/* 485 */       offset = this.bytePool.allocSlice(bytes, offset);
/* 486 */       bytes = this.bytePool.buffer;
/* 487 */       this.intUptos[(this.intUptoStart + stream)] = (offset + this.bytePool.byteOffset);
/*     */     }
/* 489 */     bytes[offset] = b;
/* 490 */     this.intUptos[(this.intUptoStart + stream)] += 1;
/*     */   }
/*     */ 
/*     */   public void writeBytes(int stream, byte[] b, int offset, int len)
/*     */   {
/* 495 */     int end = offset + len;
/* 496 */     for (int i = offset; i < end; i++)
/* 497 */       writeByte(stream, b[i]);
/*     */   }
/*     */ 
/*     */   void writeVInt(int stream, int i) {
/* 501 */     assert (stream < this.streamCount);
/* 502 */     while ((i & 0xFFFFFF80) != 0) {
/* 503 */       writeByte(stream, (byte)(i & 0x7F | 0x80));
/* 504 */       i >>>= 7;
/*     */     }
/* 506 */     writeByte(stream, (byte)i);
/*     */   }
/*     */ 
/*     */   void finish() throws IOException
/*     */   {
/* 511 */     this.consumer.finish();
/* 512 */     if (this.nextPerField != null)
/* 513 */       this.nextPerField.finish();
/*     */   }
/*     */ 
/*     */   void rehashPostings(int newSize)
/*     */   {
/* 520 */     int newMask = newSize - 1;
/*     */ 
/* 522 */     RawPostingList[] newHash = new RawPostingList[newSize];
/* 523 */     for (int i = 0; i < this.postingsHashSize; i++) {
/* 524 */       RawPostingList p0 = this.postingsHash[i];
/* 525 */       if (p0 != null)
/*     */       {
/*     */         int code;
/* 527 */         if (this.perThread.primary) {
/* 528 */           int start = p0.textStart & 0x3FFF;
/* 529 */           char[] text = this.charPool.buffers[(p0.textStart >> 14)];
/* 530 */           int pos = start;
/* 531 */           while (text[pos] != 65535)
/* 532 */             pos++;
/* 533 */           int code = 0;
/* 534 */           while (pos > start)
/* 535 */             code = code * 31 + text[(--pos)];
/*     */         } else {
/* 537 */           code = p0.textStart;
/*     */         }
/* 539 */         int hashPos = code & newMask;
/* 540 */         assert (hashPos >= 0);
/* 541 */         if (newHash[hashPos] != null) {
/* 542 */           int inc = (code >> 8) + code | 0x1;
/*     */           do {
/* 544 */             code += inc;
/* 545 */             hashPos = code & newMask;
/* 546 */           }while (newHash[hashPos] != null);
/*     */         }
/* 548 */         newHash[hashPos] = p0;
/*     */       }
/*     */     }
/*     */ 
/* 552 */     this.postingsHashMask = newMask;
/* 553 */     this.postingsHash = newHash;
/* 554 */     this.postingsHashSize = newSize;
/* 555 */     this.postingsHashHalfSize = (newSize >> 1);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermsHashPerField
 * JD-Core Version:    0.6.2
 */