/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexOutput;
/*     */ import org.apache.lucene.util.StringHelper;
/*     */ import org.apache.lucene.util.UnicodeUtil;
/*     */ import org.apache.lucene.util.UnicodeUtil.UTF8Result;
/*     */ 
/*     */ final class TermVectorsWriter
/*     */ {
/*  29 */   private IndexOutput tvx = null; private IndexOutput tvd = null; private IndexOutput tvf = null;
/*     */   private FieldInfos fieldInfos;
/*  31 */   final UnicodeUtil.UTF8Result[] utf8Results = { new UnicodeUtil.UTF8Result(), new UnicodeUtil.UTF8Result() };
/*     */ 
/*     */   public TermVectorsWriter(Directory directory, String segment, FieldInfos fieldInfos)
/*     */     throws IOException
/*     */   {
/*  38 */     this.tvx = directory.createOutput(segment + "." + "tvx");
/*  39 */     this.tvx.writeInt(4);
/*  40 */     this.tvd = directory.createOutput(segment + "." + "tvd");
/*  41 */     this.tvd.writeInt(4);
/*  42 */     this.tvf = directory.createOutput(segment + "." + "tvf");
/*  43 */     this.tvf.writeInt(4);
/*     */ 
/*  45 */     this.fieldInfos = fieldInfos;
/*     */   }
/*     */ 
/*     */   public final void addAllDocVectors(TermFreqVector[] vectors)
/*     */     throws IOException
/*     */   {
/*  58 */     this.tvx.writeLong(this.tvd.getFilePointer());
/*  59 */     this.tvx.writeLong(this.tvf.getFilePointer());
/*     */ 
/*  61 */     if (vectors != null) {
/*  62 */       int numFields = vectors.length;
/*  63 */       this.tvd.writeVInt(numFields);
/*     */ 
/*  65 */       long[] fieldPointers = new long[numFields];
/*     */ 
/*  67 */       for (int i = 0; i < numFields; i++) {
/*  68 */         fieldPointers[i] = this.tvf.getFilePointer();
/*     */ 
/*  70 */         int fieldNumber = this.fieldInfos.fieldNumber(vectors[i].getField());
/*     */ 
/*  73 */         this.tvd.writeVInt(fieldNumber);
/*     */ 
/*  75 */         int numTerms = vectors[i].size();
/*  76 */         this.tvf.writeVInt(numTerms);
/*     */         byte bits;
/*     */         TermPositionVector tpVector;
/*     */         byte bits;
/*     */         boolean storePositions;
/*     */         boolean storeOffsets;
/*  84 */         if ((vectors[i] instanceof TermPositionVector))
/*     */         {
/*  86 */           TermPositionVector tpVector = (TermPositionVector)vectors[i];
/*  87 */           boolean storePositions = (tpVector.size() > 0) && (tpVector.getTermPositions(0) != null);
/*  88 */           boolean storeOffsets = (tpVector.size() > 0) && (tpVector.getOffsets(0) != null);
/*  89 */           bits = (byte)((storePositions ? 1 : 0) + (storeOffsets ? 2 : 0));
/*     */         }
/*     */         else {
/*  92 */           tpVector = null;
/*  93 */           bits = 0;
/*  94 */           storePositions = false;
/*  95 */           storeOffsets = false;
/*     */         }
/*     */ 
/*  98 */         this.tvf.writeVInt(bits);
/*     */ 
/* 100 */         String[] terms = vectors[i].getTerms();
/* 101 */         int[] freqs = vectors[i].getTermFrequencies();
/*     */ 
/* 103 */         int utf8Upto = 0;
/* 104 */         this.utf8Results[1].length = 0;
/*     */ 
/* 106 */         for (int j = 0; j < numTerms; j++)
/*     */         {
/* 108 */           UnicodeUtil.UTF16toUTF8(terms[j], 0, terms[j].length(), this.utf8Results[utf8Upto]);
/*     */ 
/* 110 */           int start = StringHelper.bytesDifference(this.utf8Results[(1 - utf8Upto)].result, this.utf8Results[(1 - utf8Upto)].length, this.utf8Results[utf8Upto].result, this.utf8Results[utf8Upto].length);
/*     */ 
/* 114 */           int length = this.utf8Results[utf8Upto].length - start;
/* 115 */           this.tvf.writeVInt(start);
/* 116 */           this.tvf.writeVInt(length);
/* 117 */           this.tvf.writeBytes(this.utf8Results[utf8Upto].result, start, length);
/* 118 */           utf8Upto = 1 - utf8Upto;
/*     */ 
/* 120 */           int termFreq = freqs[j];
/*     */ 
/* 122 */           this.tvf.writeVInt(termFreq);
/*     */ 
/* 124 */           if (storePositions) {
/* 125 */             int[] positions = tpVector.getTermPositions(j);
/* 126 */             if (positions == null)
/* 127 */               throw new IllegalStateException("Trying to write positions that are null!");
/* 128 */             assert (positions.length == termFreq);
/*     */ 
/* 131 */             int lastPosition = 0;
/* 132 */             for (int k = 0; k < positions.length; k++) {
/* 133 */               int position = positions[k];
/* 134 */               this.tvf.writeVInt(position - lastPosition);
/* 135 */               lastPosition = position;
/*     */             }
/*     */           }
/*     */ 
/* 139 */           if (storeOffsets) {
/* 140 */             TermVectorOffsetInfo[] offsets = tpVector.getOffsets(j);
/* 141 */             if (offsets == null)
/* 142 */               throw new IllegalStateException("Trying to write offsets that are null!");
/* 143 */             assert (offsets.length == termFreq);
/*     */ 
/* 146 */             int lastEndOffset = 0;
/* 147 */             for (int k = 0; k < offsets.length; k++) {
/* 148 */               int startOffset = offsets[k].getStartOffset();
/* 149 */               int endOffset = offsets[k].getEndOffset();
/* 150 */               this.tvf.writeVInt(startOffset - lastEndOffset);
/* 151 */               this.tvf.writeVInt(endOffset - startOffset);
/* 152 */               lastEndOffset = endOffset;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 159 */       if (numFields > 1) {
/* 160 */         long lastFieldPointer = fieldPointers[0];
/* 161 */         for (int i = 1; i < numFields; i++) {
/* 162 */           long fieldPointer = fieldPointers[i];
/* 163 */           this.tvd.writeVLong(fieldPointer - lastFieldPointer);
/* 164 */           lastFieldPointer = fieldPointer;
/*     */         }
/*     */       }
/*     */     } else {
/* 168 */       this.tvd.writeVInt(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   final void addRawDocuments(TermVectorsReader reader, int[] tvdLengths, int[] tvfLengths, int numDocs)
/*     */     throws IOException
/*     */   {
/* 177 */     long tvdPosition = this.tvd.getFilePointer();
/* 178 */     long tvfPosition = this.tvf.getFilePointer();
/* 179 */     long tvdStart = tvdPosition;
/* 180 */     long tvfStart = tvfPosition;
/* 181 */     for (int i = 0; i < numDocs; i++) {
/* 182 */       this.tvx.writeLong(tvdPosition);
/* 183 */       tvdPosition += tvdLengths[i];
/* 184 */       this.tvx.writeLong(tvfPosition);
/* 185 */       tvfPosition += tvfLengths[i];
/*     */     }
/* 187 */     this.tvd.copyBytes(reader.getTvdStream(), tvdPosition - tvdStart);
/* 188 */     this.tvf.copyBytes(reader.getTvfStream(), tvfPosition - tvfStart);
/* 189 */     assert (this.tvd.getFilePointer() == tvdPosition);
/* 190 */     assert (this.tvf.getFilePointer() == tvfPosition);
/*     */   }
/*     */ 
/*     */   final void close()
/*     */     throws IOException
/*     */   {
/* 197 */     IOException keep = null;
/* 198 */     if (this.tvx != null)
/*     */       try {
/* 200 */         this.tvx.close();
/*     */       } catch (IOException e) {
/* 202 */         if (keep == null) keep = e;
/*     */       }
/* 204 */     if (this.tvd != null)
/*     */       try {
/* 206 */         this.tvd.close();
/*     */       } catch (IOException e) {
/* 208 */         if (keep == null) keep = e;
/*     */       }
/* 210 */     if (this.tvf != null)
/*     */       try {
/* 212 */         this.tvf.close();
/*     */       } catch (IOException e) {
/* 214 */         if (keep == null) keep = e;
/*     */       }
/* 216 */     if (keep != null) throw ((IOException)keep.fillInStackTrace());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermVectorsWriter
 * JD-Core Version:    0.6.2
 */