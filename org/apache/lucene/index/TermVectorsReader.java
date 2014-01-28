/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexInput;
/*     */ 
/*     */ class TermVectorsReader
/*     */   implements Cloneable
/*     */ {
/*     */   static final int FORMAT_VERSION = 2;
/*     */   static final int FORMAT_VERSION2 = 3;
/*     */   static final int FORMAT_UTF8_LENGTH_IN_BYTES = 4;
/*     */   static final int FORMAT_CURRENT = 4;
/*     */   static final int FORMAT_SIZE = 4;
/*     */   static final byte STORE_POSITIONS_WITH_TERMVECTOR = 1;
/*     */   static final byte STORE_OFFSET_WITH_TERMVECTOR = 2;
/*     */   private FieldInfos fieldInfos;
/*     */   private IndexInput tvx;
/*     */   private IndexInput tvd;
/*     */   private IndexInput tvf;
/*     */   private int size;
/*     */   private int numTotalDocs;
/*     */   private int docStoreOffset;
/*     */   private final int format;
/*     */ 
/*     */   TermVectorsReader(Directory d, String segment, FieldInfos fieldInfos)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/*  64 */     this(d, segment, fieldInfos, 1024);
/*     */   }
/*     */ 
/*     */   TermVectorsReader(Directory d, String segment, FieldInfos fieldInfos, int readBufferSize) throws CorruptIndexException, IOException
/*     */   {
/*  69 */     this(d, segment, fieldInfos, readBufferSize, -1, 0);
/*     */   }
/*     */ 
/*     */   TermVectorsReader(Directory d, String segment, FieldInfos fieldInfos, int readBufferSize, int docStoreOffset, int size) throws CorruptIndexException, IOException
/*     */   {
/*  74 */     boolean success = false;
/*     */     try
/*     */     {
/*  77 */       if (d.fileExists(segment + "." + "tvx")) {
/*  78 */         this.tvx = d.openInput(segment + "." + "tvx", readBufferSize);
/*  79 */         this.format = checkValidFormat(this.tvx);
/*  80 */         this.tvd = d.openInput(segment + "." + "tvd", readBufferSize);
/*  81 */         int tvdFormat = checkValidFormat(this.tvd);
/*  82 */         this.tvf = d.openInput(segment + "." + "tvf", readBufferSize);
/*  83 */         int tvfFormat = checkValidFormat(this.tvf);
/*     */ 
/*  85 */         assert (this.format == tvdFormat);
/*  86 */         assert (this.format == tvfFormat);
/*     */ 
/*  88 */         if (this.format >= 3) {
/*  89 */           assert ((this.tvx.length() - 4L) % 16L == 0L);
/*  90 */           this.numTotalDocs = ((int)(this.tvx.length() >> 4));
/*     */         } else {
/*  92 */           assert ((this.tvx.length() - 4L) % 8L == 0L);
/*  93 */           this.numTotalDocs = ((int)(this.tvx.length() >> 3));
/*     */         }
/*     */ 
/*  96 */         if (-1 == docStoreOffset) {
/*  97 */           this.docStoreOffset = 0;
/*  98 */           this.size = this.numTotalDocs;
/*  99 */           if ((!$assertionsDisabled) && (size != 0) && (this.numTotalDocs != size)) throw new AssertionError(); 
/*     */         }
/* 101 */         else { this.docStoreOffset = docStoreOffset;
/* 102 */           this.size = size;
/*     */ 
/* 105 */           assert (this.numTotalDocs >= size + docStoreOffset) : ("numTotalDocs=" + this.numTotalDocs + " size=" + size + " docStoreOffset=" + docStoreOffset); }
/*     */       }
/*     */       else {
/* 108 */         this.format = 0;
/*     */       }
/* 110 */       this.fieldInfos = fieldInfos;
/* 111 */       success = true;
/*     */     }
/*     */     finally
/*     */     {
/* 118 */       if (!success)
/* 119 */         close();
/*     */     }
/*     */   }
/*     */ 
/*     */   IndexInput getTvdStream()
/*     */   {
/* 126 */     return this.tvd;
/*     */   }
/*     */ 
/*     */   IndexInput getTvfStream()
/*     */   {
/* 131 */     return this.tvf;
/*     */   }
/*     */ 
/*     */   private final void seekTvx(int docNum) throws IOException {
/* 135 */     if (this.format < 3)
/* 136 */       this.tvx.seek((docNum + this.docStoreOffset) * 8L + 4L);
/*     */     else
/* 138 */       this.tvx.seek((docNum + this.docStoreOffset) * 16L + 4L);
/*     */   }
/*     */ 
/*     */   boolean canReadRawDocs() {
/* 142 */     return this.format >= 4;
/*     */   }
/*     */ 
/*     */   final void rawDocs(int[] tvdLengths, int[] tvfLengths, int startDocID, int numDocs)
/*     */     throws IOException
/*     */   {
/* 153 */     if (this.tvx == null) {
/* 154 */       Arrays.fill(tvdLengths, 0);
/* 155 */       Arrays.fill(tvfLengths, 0);
/* 156 */       return;
/*     */     }
/*     */ 
/* 161 */     if (this.format < 3) {
/* 162 */       throw new IllegalStateException("cannot read raw docs with older term vector formats");
/*     */     }
/* 164 */     seekTvx(startDocID);
/*     */ 
/* 166 */     long tvdPosition = this.tvx.readLong();
/* 167 */     this.tvd.seek(tvdPosition);
/*     */ 
/* 169 */     long tvfPosition = this.tvx.readLong();
/* 170 */     this.tvf.seek(tvfPosition);
/*     */ 
/* 172 */     long lastTvdPosition = tvdPosition;
/* 173 */     long lastTvfPosition = tvfPosition;
/*     */ 
/* 175 */     int count = 0;
/* 176 */     while (count < numDocs) {
/* 177 */       int docID = this.docStoreOffset + startDocID + count + 1;
/* 178 */       assert (docID <= this.numTotalDocs);
/* 179 */       if (docID < this.numTotalDocs) {
/* 180 */         tvdPosition = this.tvx.readLong();
/* 181 */         tvfPosition = this.tvx.readLong();
/*     */       } else {
/* 183 */         tvdPosition = this.tvd.length();
/* 184 */         tvfPosition = this.tvf.length();
/* 185 */         assert (count == numDocs - 1);
/*     */       }
/* 187 */       tvdLengths[count] = ((int)(tvdPosition - lastTvdPosition));
/* 188 */       tvfLengths[count] = ((int)(tvfPosition - lastTvfPosition));
/* 189 */       count++;
/* 190 */       lastTvdPosition = tvdPosition;
/* 191 */       lastTvfPosition = tvfPosition;
/*     */     }
/*     */   }
/*     */ 
/*     */   private int checkValidFormat(IndexInput in) throws CorruptIndexException, IOException
/*     */   {
/* 197 */     int format = in.readInt();
/* 198 */     if (format > 4) {
/* 199 */       throw new CorruptIndexException("Incompatible format version: " + format + " expected " + 4 + " or less");
/*     */     }
/*     */ 
/* 202 */     return format;
/*     */   }
/*     */ 
/*     */   void close()
/*     */     throws IOException
/*     */   {
/* 208 */     IOException keep = null;
/* 209 */     if (this.tvx != null) try { this.tvx.close(); } catch (IOException e) { if (keep == null) keep = e; 
/*     */       }
/* 210 */     if (this.tvd != null) try { this.tvd.close(); } catch (IOException e) { if (keep == null) keep = e; 
/*     */       }
/* 211 */     if (this.tvf != null) try { this.tvf.close(); } catch (IOException e) { if (keep == null) keep = e; 
/*     */       }
/* 212 */     if (keep != null) throw ((IOException)keep.fillInStackTrace());
/*     */   }
/*     */ 
/*     */   int size()
/*     */   {
/* 220 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void get(int docNum, String field, TermVectorMapper mapper) throws IOException {
/* 224 */     if (this.tvx != null) {
/* 225 */       int fieldNumber = this.fieldInfos.fieldNumber(field);
/*     */ 
/* 230 */       seekTvx(docNum);
/*     */ 
/* 232 */       long tvdPosition = this.tvx.readLong();
/*     */ 
/* 234 */       this.tvd.seek(tvdPosition);
/* 235 */       int fieldCount = this.tvd.readVInt();
/*     */ 
/* 240 */       int number = 0;
/* 241 */       int found = -1;
/* 242 */       for (int i = 0; i < fieldCount; i++) {
/* 243 */         if (this.format >= 2)
/* 244 */           number = this.tvd.readVInt();
/*     */         else {
/* 246 */           number += this.tvd.readVInt();
/*     */         }
/* 248 */         if (number == fieldNumber) {
/* 249 */           found = i;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 254 */       if (found != -1)
/*     */       {
/*     */         long position;
/*     */         long position;
/* 257 */         if (this.format >= 3)
/* 258 */           position = this.tvx.readLong();
/*     */         else
/* 260 */           position = this.tvd.readVLong();
/* 261 */         for (int i = 1; i <= found; i++) {
/* 262 */           position += this.tvd.readVLong();
/*     */         }
/* 264 */         mapper.setDocumentNumber(docNum);
/* 265 */         readTermVector(field, position, mapper);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   TermFreqVector get(int docNum, String field)
/*     */     throws IOException
/*     */   {
/* 285 */     ParallelArrayTermVectorMapper mapper = new ParallelArrayTermVectorMapper();
/* 286 */     get(docNum, field, mapper);
/*     */ 
/* 288 */     return mapper.materializeVector();
/*     */   }
/*     */ 
/*     */   private final String[] readFields(int fieldCount)
/*     */     throws IOException
/*     */   {
/* 294 */     int number = 0;
/* 295 */     String[] fields = new String[fieldCount];
/*     */ 
/* 297 */     for (int i = 0; i < fieldCount; i++) {
/* 298 */       if (this.format >= 2)
/* 299 */         number = this.tvd.readVInt();
/*     */       else {
/* 301 */         number += this.tvd.readVInt();
/*     */       }
/* 303 */       fields[i] = this.fieldInfos.fieldName(number);
/*     */     }
/*     */ 
/* 306 */     return fields;
/*     */   }
/*     */ 
/*     */   private final long[] readTvfPointers(int fieldCount)
/*     */     throws IOException
/*     */   {
/*     */     long position;
/*     */     long position;
/* 314 */     if (this.format >= 3)
/* 315 */       position = this.tvx.readLong();
/*     */     else {
/* 317 */       position = this.tvd.readVLong();
/*     */     }
/* 319 */     long[] tvfPointers = new long[fieldCount];
/* 320 */     tvfPointers[0] = position;
/*     */ 
/* 322 */     for (int i = 1; i < fieldCount; i++) {
/* 323 */       position += this.tvd.readVLong();
/* 324 */       tvfPointers[i] = position;
/*     */     }
/*     */ 
/* 327 */     return tvfPointers;
/*     */   }
/*     */ 
/*     */   TermFreqVector[] get(int docNum)
/*     */     throws IOException
/*     */   {
/* 338 */     TermFreqVector[] result = null;
/* 339 */     if (this.tvx != null)
/*     */     {
/* 341 */       seekTvx(docNum);
/* 342 */       long tvdPosition = this.tvx.readLong();
/*     */ 
/* 344 */       this.tvd.seek(tvdPosition);
/* 345 */       int fieldCount = this.tvd.readVInt();
/*     */ 
/* 348 */       if (fieldCount != 0) {
/* 349 */         String[] fields = readFields(fieldCount);
/* 350 */         long[] tvfPointers = readTvfPointers(fieldCount);
/* 351 */         result = readTermVectors(docNum, fields, tvfPointers);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 356 */     return result;
/*     */   }
/*     */ 
/*     */   public void get(int docNumber, TermVectorMapper mapper) throws IOException
/*     */   {
/* 361 */     if (this.tvx != null)
/*     */     {
/* 364 */       seekTvx(docNumber);
/* 365 */       long tvdPosition = this.tvx.readLong();
/*     */ 
/* 367 */       this.tvd.seek(tvdPosition);
/* 368 */       int fieldCount = this.tvd.readVInt();
/*     */ 
/* 371 */       if (fieldCount != 0) {
/* 372 */         String[] fields = readFields(fieldCount);
/* 373 */         long[] tvfPointers = readTvfPointers(fieldCount);
/* 374 */         mapper.setDocumentNumber(docNumber);
/* 375 */         readTermVectors(fields, tvfPointers, mapper);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private SegmentTermVector[] readTermVectors(int docNum, String[] fields, long[] tvfPointers)
/*     */     throws IOException
/*     */   {
/* 385 */     SegmentTermVector[] res = new SegmentTermVector[fields.length];
/* 386 */     for (int i = 0; i < fields.length; i++) {
/* 387 */       ParallelArrayTermVectorMapper mapper = new ParallelArrayTermVectorMapper();
/* 388 */       mapper.setDocumentNumber(docNum);
/* 389 */       readTermVector(fields[i], tvfPointers[i], mapper);
/* 390 */       res[i] = ((SegmentTermVector)mapper.materializeVector());
/*     */     }
/* 392 */     return res;
/*     */   }
/*     */ 
/*     */   private void readTermVectors(String[] fields, long[] tvfPointers, TermVectorMapper mapper) throws IOException
/*     */   {
/* 397 */     for (int i = 0; i < fields.length; i++)
/* 398 */       readTermVector(fields[i], tvfPointers[i], mapper);
/*     */   }
/*     */ 
/*     */   private void readTermVector(String field, long tvfPointer, TermVectorMapper mapper)
/*     */     throws IOException
/*     */   {
/* 415 */     this.tvf.seek(tvfPointer);
/*     */ 
/* 417 */     int numTerms = this.tvf.readVInt();
/*     */ 
/* 420 */     if (numTerms == 0)
/*     */       return;
/*     */     boolean storeOffsets;
/*     */     boolean storePositions;
/*     */     boolean storeOffsets;
/* 426 */     if (this.format >= 2) {
/* 427 */       byte bits = this.tvf.readByte();
/* 428 */       boolean storePositions = (bits & 0x1) != 0;
/* 429 */       storeOffsets = (bits & 0x2) != 0;
/*     */     }
/*     */     else {
/* 432 */       this.tvf.readVInt();
/* 433 */       storePositions = false;
/* 434 */       storeOffsets = false;
/*     */     }
/* 436 */     mapper.setExpectations(field, numTerms, storeOffsets, storePositions);
/* 437 */     int start = 0;
/* 438 */     int deltaLength = 0;
/* 439 */     int totalLength = 0;
/*     */ 
/* 442 */     boolean preUTF8 = this.format < 4;
/*     */     byte[] byteBuffer;
/*     */     char[] charBuffer;
/*     */     byte[] byteBuffer;
/* 445 */     if (preUTF8) {
/* 446 */       char[] charBuffer = new char[10];
/* 447 */       byteBuffer = null;
/*     */     } else {
/* 449 */       charBuffer = null;
/* 450 */       byteBuffer = new byte[20];
/*     */     }
/*     */ 
/* 453 */     for (int i = 0; i < numTerms; i++) {
/* 454 */       start = this.tvf.readVInt();
/* 455 */       deltaLength = this.tvf.readVInt();
/* 456 */       totalLength = start + deltaLength;
/*     */       String term;
/*     */       String term;
/* 460 */       if (preUTF8)
/*     */       {
/* 462 */         if (charBuffer.length < totalLength) {
/* 463 */           char[] newCharBuffer = new char[(int)(1.5D * totalLength)];
/* 464 */           System.arraycopy(charBuffer, 0, newCharBuffer, 0, start);
/* 465 */           charBuffer = newCharBuffer;
/*     */         }
/* 467 */         this.tvf.readChars(charBuffer, start, deltaLength);
/* 468 */         term = new String(charBuffer, 0, totalLength);
/*     */       }
/*     */       else {
/* 471 */         if (byteBuffer.length < totalLength) {
/* 472 */           byte[] newByteBuffer = new byte[(int)(1.5D * totalLength)];
/* 473 */           System.arraycopy(byteBuffer, 0, newByteBuffer, 0, start);
/* 474 */           byteBuffer = newByteBuffer;
/*     */         }
/* 476 */         this.tvf.readBytes(byteBuffer, start, deltaLength);
/* 477 */         term = new String(byteBuffer, 0, totalLength, "UTF-8");
/*     */       }
/* 479 */       int freq = this.tvf.readVInt();
/* 480 */       int[] positions = null;
/* 481 */       if (storePositions)
/*     */       {
/* 483 */         if (!mapper.isIgnoringPositions()) {
/* 484 */           positions = new int[freq];
/* 485 */           int prevPosition = 0;
/* 486 */           for (int j = 0; j < freq; j++)
/*     */           {
/* 488 */             positions[j] = (prevPosition + this.tvf.readVInt());
/* 489 */             prevPosition = positions[j];
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 494 */           for (int j = 0; j < freq; j++)
/*     */           {
/* 496 */             this.tvf.readVInt();
/*     */           }
/*     */         }
/*     */       }
/* 500 */       TermVectorOffsetInfo[] offsets = null;
/* 501 */       if (storeOffsets)
/*     */       {
/* 503 */         if (!mapper.isIgnoringOffsets()) {
/* 504 */           offsets = new TermVectorOffsetInfo[freq];
/* 505 */           int prevOffset = 0;
/* 506 */           for (int j = 0; j < freq; j++) {
/* 507 */             int startOffset = prevOffset + this.tvf.readVInt();
/* 508 */             int endOffset = startOffset + this.tvf.readVInt();
/* 509 */             offsets[j] = new TermVectorOffsetInfo(startOffset, endOffset);
/* 510 */             prevOffset = endOffset;
/*     */           }
/*     */         } else {
/* 513 */           for (int j = 0; j < freq; j++) {
/* 514 */             this.tvf.readVInt();
/* 515 */             this.tvf.readVInt();
/*     */           }
/*     */         }
/*     */       }
/* 519 */       mapper.map(term, freq, offsets, positions);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 526 */     TermVectorsReader clone = (TermVectorsReader)super.clone();
/*     */ 
/* 530 */     if ((this.tvx != null) && (this.tvd != null) && (this.tvf != null)) {
/* 531 */       clone.tvx = ((IndexInput)this.tvx.clone());
/* 532 */       clone.tvd = ((IndexInput)this.tvd.clone());
/* 533 */       clone.tvf = ((IndexInput)this.tvf.clone());
/*     */     }
/*     */ 
/* 536 */     return clone;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermVectorsReader
 * JD-Core Version:    0.6.2
 */