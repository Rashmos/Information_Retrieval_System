/*     */ package org.apache.lucene.analysis.synonym;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*     */ import org.apache.lucene.store.ByteArrayDataInput;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ import org.apache.lucene.util.AttributeSource.State;
/*     */ import org.apache.lucene.util.BytesRef;
/*     */ import org.apache.lucene.util.BytesRefHash;
/*     */ import org.apache.lucene.util.CharsRef;
/*     */ import org.apache.lucene.util.RamUsageEstimator;
/*     */ import org.apache.lucene.util.UnicodeUtil;
/*     */ import org.apache.lucene.util.fst.FST;
/*     */ import org.apache.lucene.util.fst.FST.Arc;
/*     */ import org.apache.lucene.util.fst.Outputs;
/*     */ 
/*     */ public final class SynonymFilter extends TokenFilter
/*     */ {
/*     */   public static final String TYPE_SYNONYM = "SYNONYM";
/*     */   private final SynonymMap synonyms;
/*     */   private final boolean ignoreCase;
/*     */   private final int rollBufferSize;
/*     */   private int captureCount;
/* 115 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 116 */   private final PositionIncrementAttribute posIncrAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/* 117 */   private final TypeAttribute typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
/* 118 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */   private int inputSkipCount;
/*     */   private final PendingInput[] futureInputs;
/* 193 */   private final ByteArrayDataInput bytesReader = new ByteArrayDataInput();
/*     */   private final PendingOutputs[] futureOutputs;
/*     */   private int nextWrite;
/*     */   private int nextRead;
/*     */   private boolean finished;
/*     */   private final FST.Arc<BytesRef> scratchArc;
/*     */   private final FST<BytesRef> fst;
/* 212 */   private final BytesRef scratchBytes = new BytesRef();
/* 213 */   private final CharsRef scratchChars = new CharsRef();
/*     */ 
/*     */   public SynonymFilter(TokenStream input, SynonymMap synonyms, boolean ignoreCase)
/*     */   {
/* 223 */     super(input);
/* 224 */     this.synonyms = synonyms;
/* 225 */     this.ignoreCase = ignoreCase;
/* 226 */     this.fst = synonyms.fst;
/*     */ 
/* 228 */     if (this.fst == null) {
/* 229 */       throw new IllegalArgumentException("fst must be non-null");
/*     */     }
/*     */ 
/* 235 */     this.rollBufferSize = (1 + synonyms.maxHorizontalContext);
/*     */ 
/* 237 */     this.futureInputs = new PendingInput[this.rollBufferSize];
/* 238 */     this.futureOutputs = new PendingOutputs[this.rollBufferSize];
/* 239 */     for (int pos = 0; pos < this.rollBufferSize; pos++) {
/* 240 */       this.futureInputs[pos] = new PendingInput(null);
/* 241 */       this.futureOutputs[pos] = new PendingOutputs();
/*     */     }
/*     */ 
/* 246 */     this.scratchArc = new FST.Arc();
/*     */   }
/*     */ 
/*     */   private void capture() {
/* 250 */     this.captureCount += 1;
/*     */ 
/* 252 */     PendingInput input = this.futureInputs[this.nextWrite];
/*     */ 
/* 254 */     input.state = captureState();
/* 255 */     input.consumed = false;
/* 256 */     input.term.copy(this.termAtt.buffer(), 0, this.termAtt.length());
/*     */ 
/* 258 */     this.nextWrite = rollIncr(this.nextWrite);
/*     */ 
/* 261 */     assert (this.nextWrite != this.nextRead);
/*     */   }
/*     */ 
/*     */   private void parse()
/*     */     throws IOException
/*     */   {
/* 277 */     assert (this.inputSkipCount == 0);
/*     */ 
/* 279 */     int curNextRead = this.nextRead;
/*     */ 
/* 282 */     BytesRef matchOutput = null;
/* 283 */     int matchInputLength = 0;
/*     */ 
/* 285 */     BytesRef pendingOutput = (BytesRef)this.fst.outputs.getNoOutput();
/* 286 */     this.fst.getFirstArc(this.scratchArc);
/*     */ 
/* 288 */     assert (this.scratchArc.output == this.fst.outputs.getNoOutput());
/*     */ 
/* 290 */     int tokenCount = 0;
/*     */     while (true)
/*     */     {
/*     */       char[] buffer;
/*     */       int bufferLen;
/* 300 */       if (curNextRead == this.nextWrite)
/*     */       {
/* 305 */         if (this.finished)
/*     */         {
/*     */           break;
/*     */         }
/* 309 */         assert (this.futureInputs[this.nextWrite].consumed);
/*     */ 
/* 314 */         if (this.input.incrementToken()) {
/* 315 */           char[] buffer = this.termAtt.buffer();
/* 316 */           int bufferLen = this.termAtt.length();
/* 317 */           PendingInput input = this.futureInputs[this.nextWrite];
/* 318 */           input.startOffset = this.offsetAtt.startOffset();
/* 319 */           input.endOffset = this.offsetAtt.endOffset();
/*     */ 
/* 321 */           if (this.nextRead != this.nextWrite)
/* 322 */             capture();
/*     */           else {
/* 324 */             input.consumed = false;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 330 */           this.finished = true;
/* 331 */           break;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 336 */         buffer = this.futureInputs[curNextRead].term.chars;
/* 337 */         bufferLen = this.futureInputs[curNextRead].term.length;
/*     */       }
/*     */ 
/* 341 */       tokenCount++;
/*     */ 
/* 344 */       int bufUpto = 0;
/* 345 */       while (bufUpto < bufferLen) {
/* 346 */         int codePoint = Character.codePointAt(buffer, bufUpto, bufferLen);
/* 347 */         if (this.fst.findTargetArc(this.ignoreCase ? Character.toLowerCase(codePoint) : codePoint, this.scratchArc, this.scratchArc) == null)
/*     */         {
/*     */           break label480;
/*     */         }
/*     */ 
/* 353 */         pendingOutput = (BytesRef)this.fst.outputs.add(pendingOutput, this.scratchArc.output);
/*     */ 
/* 355 */         bufUpto += Character.charCount(codePoint);
/*     */       }
/*     */ 
/* 360 */       if (this.scratchArc.isFinal()) {
/* 361 */         matchOutput = (BytesRef)this.fst.outputs.add(pendingOutput, this.scratchArc.nextFinalOutput);
/* 362 */         matchInputLength = tokenCount;
/*     */       }
/*     */ 
/* 368 */       if (this.fst.findTargetArc(0, this.scratchArc, this.scratchArc) == null)
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 376 */       pendingOutput = (BytesRef)this.fst.outputs.add(pendingOutput, this.scratchArc.output);
/* 377 */       if (this.nextRead == this.nextWrite) {
/* 378 */         capture();
/*     */       }
/*     */ 
/* 382 */       curNextRead = rollIncr(curNextRead);
/*     */     }
/*     */ 
/* 385 */     label480: if ((this.nextRead == this.nextWrite) && (!this.finished))
/*     */     {
/* 387 */       this.nextWrite = rollIncr(this.nextWrite);
/*     */     }
/*     */ 
/* 390 */     if (matchOutput != null)
/*     */     {
/* 392 */       this.inputSkipCount = matchInputLength;
/* 393 */       addOutput(matchOutput, matchInputLength);
/* 394 */     } else if (this.nextRead != this.nextWrite)
/*     */     {
/* 398 */       this.inputSkipCount = 1;
/*     */     } else {
/* 400 */       assert (this.finished);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addOutput(BytesRef bytes, int matchInputLength)
/*     */   {
/* 408 */     this.bytesReader.reset(bytes.bytes, bytes.offset, bytes.length);
/*     */ 
/* 410 */     int code = this.bytesReader.readVInt();
/* 411 */     boolean keepOrig = (code & 0x1) == 0;
/* 412 */     int count = code >>> 1;
/*     */ 
/* 414 */     for (int outputIDX = 0; outputIDX < count; outputIDX++) {
/* 415 */       this.synonyms.words.get(this.bytesReader.readVInt(), this.scratchBytes);
/*     */ 
/* 418 */       UnicodeUtil.UTF8toUTF16(this.scratchBytes, this.scratchChars);
/* 419 */       int lastStart = this.scratchChars.offset;
/* 420 */       int chEnd = lastStart + this.scratchChars.length;
/* 421 */       int outputUpto = this.nextRead;
/* 422 */       for (int chIDX = lastStart; chIDX <= chEnd; chIDX++) {
/* 423 */         if ((chIDX == chEnd) || (this.scratchChars.chars[chIDX] == 0)) {
/* 424 */           int outputLen = chIDX - lastStart;
/*     */ 
/* 427 */           assert (outputLen > 0) : ("output contains empty string: " + this.scratchChars);
/* 428 */           this.futureOutputs[outputUpto].add(this.scratchChars.chars, lastStart, outputLen);
/*     */ 
/* 430 */           lastStart = 1 + chIDX;
/*     */ 
/* 432 */           outputUpto = rollIncr(outputUpto);
/* 433 */           assert (this.futureOutputs[outputUpto].posIncr == 1) : ("outputUpto=" + outputUpto + " vs nextWrite=" + this.nextWrite);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 438 */     int upto = this.nextRead;
/* 439 */     for (int idx = 0; idx < matchInputLength; idx++) {
/* 440 */       this.futureInputs[upto].keepOrig |= keepOrig;
/* 441 */       this.futureInputs[upto].matched = true;
/* 442 */       upto = rollIncr(upto);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int rollIncr(int count)
/*     */   {
/* 448 */     count++;
/* 449 */     if (count == this.rollBufferSize) {
/* 450 */       return 0;
/*     */     }
/* 452 */     return count;
/*     */   }
/*     */ 
/*     */   int getCaptureCount()
/*     */   {
/* 458 */     return this.captureCount;
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*     */     while (true)
/* 470 */       if (this.inputSkipCount != 0)
/*     */       {
/* 477 */         PendingInput input = this.futureInputs[this.nextRead];
/* 478 */         PendingOutputs outputs = this.futureOutputs[this.nextRead];
/*     */ 
/* 482 */         if ((!input.consumed) && ((input.keepOrig) || (!input.matched))) {
/* 483 */           if (input.state != null)
/*     */           {
/* 486 */             restoreState(input.state);
/*     */           }
/*     */           else
/*     */           {
/* 490 */             assert (this.inputSkipCount == 1) : ("inputSkipCount=" + this.inputSkipCount + " nextRead=" + this.nextRead);
/*     */           }
/* 492 */           input.reset();
/* 493 */           if (outputs.count > 0) {
/* 494 */             outputs.posIncr = 0;
/*     */           } else {
/* 496 */             this.nextRead = rollIncr(this.nextRead);
/* 497 */             this.inputSkipCount -= 1;
/*     */           }
/*     */ 
/* 500 */           return true;
/* 501 */         }if (outputs.upto < outputs.count)
/*     */         {
/* 504 */           input.reset();
/* 505 */           int posIncr = outputs.posIncr;
/* 506 */           CharsRef output = outputs.pullNext();
/* 507 */           clearAttributes();
/* 508 */           this.termAtt.copyBuffer(output.chars, output.offset, output.length);
/* 509 */           this.typeAtt.setType("SYNONYM");
/* 510 */           this.offsetAtt.setOffset(input.startOffset, input.endOffset);
/* 511 */           this.posIncrAtt.setPositionIncrement(posIncr);
/* 512 */           if (outputs.count == 0)
/*     */           {
/* 515 */             this.nextRead = rollIncr(this.nextRead);
/* 516 */             this.inputSkipCount -= 1;
/*     */           }
/*     */ 
/* 519 */           return true;
/*     */         }
/*     */ 
/* 523 */         input.reset();
/* 524 */         this.nextRead = rollIncr(this.nextRead);
/* 525 */         this.inputSkipCount -= 1;
/*     */       }
/*     */       else
/*     */       {
/* 529 */         if ((this.finished) && (this.nextRead == this.nextWrite))
/*     */         {
/* 532 */           PendingOutputs outputs = this.futureOutputs[this.nextRead];
/* 533 */           if (outputs.upto < outputs.count) {
/* 534 */             int posIncr = outputs.posIncr;
/* 535 */             CharsRef output = outputs.pullNext();
/* 536 */             this.futureInputs[this.nextRead].reset();
/* 537 */             if (outputs.count == 0) {
/* 538 */               this.nextWrite = (this.nextRead = rollIncr(this.nextRead));
/*     */             }
/* 540 */             clearAttributes();
/* 541 */             this.termAtt.copyBuffer(output.chars, output.offset, output.length);
/* 542 */             this.typeAtt.setType("SYNONYM");
/*     */ 
/* 544 */             this.posIncrAtt.setPositionIncrement(posIncr);
/*     */ 
/* 546 */             return true;
/*     */           }
/* 548 */           return false;
/*     */         }
/*     */ 
/* 553 */         parse();
/*     */       }
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 559 */     super.reset();
/* 560 */     this.captureCount = 0;
/* 561 */     this.finished = false;
/*     */ 
/* 567 */     for (PendingInput input : this.futureInputs) {
/* 568 */       input.reset();
/*     */     }
/* 570 */     for (PendingOutputs output : this.futureOutputs)
/* 571 */       output.reset();
/*     */   }
/*     */ 
/*     */   private static class PendingOutputs
/*     */   {
/*     */     CharsRef[] outputs;
/*     */     int upto;
/*     */     int count;
/* 158 */     int posIncr = 1;
/*     */ 
/*     */     public PendingOutputs() {
/* 161 */       this.outputs = new CharsRef[1];
/*     */     }
/*     */ 
/*     */     public void reset() {
/* 165 */       this.upto = (this.count = 0);
/* 166 */       this.posIncr = 1;
/*     */     }
/*     */ 
/*     */     public CharsRef pullNext() {
/* 170 */       assert (this.upto < this.count);
/* 171 */       CharsRef result = this.outputs[(this.upto++)];
/* 172 */       this.posIncr = 0;
/* 173 */       if (this.upto == this.count) {
/* 174 */         reset();
/*     */       }
/* 176 */       return result;
/*     */     }
/*     */ 
/*     */     public void add(char[] output, int offset, int len) {
/* 180 */       if (this.count == this.outputs.length) {
/* 181 */         CharsRef[] next = new CharsRef[ArrayUtil.oversize(1 + this.count, RamUsageEstimator.NUM_BYTES_OBJECT_REF)];
/* 182 */         System.arraycopy(this.outputs, 0, next, 0, this.count);
/* 183 */         this.outputs = next;
/*     */       }
/* 185 */       if (this.outputs[this.count] == null) {
/* 186 */         this.outputs[this.count] = new CharsRef();
/*     */       }
/* 188 */       this.outputs[this.count].copy(output, offset, len);
/* 189 */       this.count += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class PendingInput
/*     */   {
/* 132 */     final CharsRef term = new CharsRef();
/*     */     AttributeSource.State state;
/*     */     boolean keepOrig;
/*     */     boolean matched;
/* 136 */     boolean consumed = true;
/*     */     int startOffset;
/*     */     int endOffset;
/*     */ 
/*     */     public void reset()
/*     */     {
/* 141 */       this.state = null;
/* 142 */       this.consumed = true;
/* 143 */       this.keepOrig = false;
/* 144 */       this.matched = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.synonym.SynonymFilter
 * JD-Core Version:    0.6.2
 */