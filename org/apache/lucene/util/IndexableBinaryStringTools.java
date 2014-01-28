/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ public class IndexableBinaryStringTools
/*     */ {
/*  52 */   private static final CodingCase[] CODING_CASES = { new CodingCase(7, 1), new CodingCase(14, 6, 2), new CodingCase(13, 5, 3), new CodingCase(12, 4, 4), new CodingCase(11, 3, 5), new CodingCase(10, 2, 6), new CodingCase(9, 1, 7), new CodingCase(8, 0) };
/*     */ 
/*     */   public static int getEncodedLength(ByteBuffer original)
/*     */     throws IllegalArgumentException
/*     */   {
/*  77 */     if (original.hasArray())
/*     */     {
/*  79 */       long length = original.limit() - original.arrayOffset();
/*  80 */       return (int)((length * 8L + 14L) / 15L) + 1;
/*     */     }
/*  82 */     throw new IllegalArgumentException("original argument must have a backing array");
/*     */   }
/*     */ 
/*     */   public static int getDecodedLength(CharBuffer encoded)
/*     */     throws IllegalArgumentException
/*     */   {
/*  95 */     if (encoded.hasArray()) {
/*  96 */       int numChars = encoded.limit() - encoded.arrayOffset() - 1;
/*  97 */       if (numChars <= 0) {
/*  98 */         return 0;
/*     */       }
/* 100 */       int numFullBytesInFinalChar = encoded.charAt(encoded.limit() - 1);
/* 101 */       int numEncodedChars = numChars - 1;
/* 102 */       return (numEncodedChars * 15 + 7) / 8 + numFullBytesInFinalChar;
/*     */     }
/*     */ 
/* 105 */     throw new IllegalArgumentException("encoded argument must have a backing array");
/*     */   }
/*     */ 
/*     */   public static void encode(ByteBuffer input, CharBuffer output)
/*     */   {
/* 121 */     if ((input.hasArray()) && (output.hasArray())) {
/* 122 */       byte[] inputArray = input.array();
/* 123 */       int inputOffset = input.arrayOffset();
/* 124 */       int inputLength = input.limit() - inputOffset;
/* 125 */       char[] outputArray = output.array();
/* 126 */       int outputOffset = output.arrayOffset();
/* 127 */       int outputLength = getEncodedLength(input);
/* 128 */       output.limit(outputOffset + outputLength);
/* 129 */       output.position(0);
/* 130 */       if (inputLength > 0) {
/* 131 */         int inputByteNum = inputOffset;
/* 132 */         int caseNum = 0;
/* 133 */         for (int outputCharNum = outputOffset; 
/* 135 */           inputByteNum + CODING_CASES[caseNum].numBytes <= inputLength; 
/* 136 */           outputCharNum++) {
/* 137 */           CodingCase codingCase = CODING_CASES[caseNum];
/* 138 */           if (2 == codingCase.numBytes) {
/* 139 */             outputArray[outputCharNum] = ((char)(((inputArray[inputByteNum] & 0xFF) << codingCase.initialShift) + ((inputArray[(inputByteNum + 1)] & 0xFF) >>> codingCase.finalShift & codingCase.finalMask) & 0x7FFF));
/*     */           }
/*     */           else
/*     */           {
/* 145 */             outputArray[outputCharNum] = ((char)(((inputArray[inputByteNum] & 0xFF) << codingCase.initialShift) + ((inputArray[(inputByteNum + 1)] & 0xFF) << codingCase.middleShift) + ((inputArray[(inputByteNum + 2)] & 0xFF) >>> codingCase.finalShift & codingCase.finalMask) & 0x7FFF));
/*     */           }
/*     */ 
/* 152 */           inputByteNum += codingCase.advanceBytes;
/* 153 */           caseNum++; if (caseNum == CODING_CASES.length) {
/* 154 */             caseNum = 0;
/*     */           }
/*     */         }
/*     */ 
/* 158 */         CodingCase codingCase = CODING_CASES[caseNum];
/*     */ 
/* 160 */         if (inputByteNum + 1 < inputLength) {
/* 161 */           outputArray[(outputCharNum++)] = ((char)(((inputArray[inputByteNum] & 0xFF) << codingCase.initialShift) + ((inputArray[(inputByteNum + 1)] & 0xFF) << codingCase.middleShift) & 0x7FFF));
/*     */ 
/* 166 */           outputArray[(outputCharNum++)] = '\001';
/* 167 */         } else if (inputByteNum < inputLength) {
/* 168 */           outputArray[(outputCharNum++)] = ((char)((inputArray[inputByteNum] & 0xFF) << codingCase.initialShift & 0x7FFF));
/*     */ 
/* 172 */           outputArray[(outputCharNum++)] = (caseNum == 0 ? 1 : '\000');
/*     */         }
/*     */         else {
/* 175 */           outputArray[(outputCharNum++)] = '\001';
/*     */         }
/*     */       }
/*     */     } else {
/* 179 */       throw new IllegalArgumentException("Arguments must have backing arrays");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void decode(CharBuffer input, ByteBuffer output)
/*     */   {
/* 195 */     if ((input.hasArray()) && (output.hasArray())) {
/* 196 */       int numInputChars = input.limit() - input.arrayOffset() - 1;
/* 197 */       int numOutputBytes = getDecodedLength(input);
/* 198 */       output.limit(numOutputBytes + output.arrayOffset());
/* 199 */       output.position(0);
/* 200 */       byte[] outputArray = output.array();
/* 201 */       char[] inputArray = input.array();
/* 202 */       if (numOutputBytes > 0) {
/* 203 */         int caseNum = 0;
/* 204 */         int outputByteNum = output.arrayOffset();
/* 205 */         for (int inputCharNum = input.arrayOffset(); 
/* 208 */           inputCharNum < numInputChars - 1; inputCharNum++) {
/* 209 */           CodingCase codingCase = CODING_CASES[caseNum];
/* 210 */           short inputChar = (short)inputArray[inputCharNum];
/* 211 */           if (2 == codingCase.numBytes) {
/* 212 */             if (0 == caseNum) {
/* 213 */               outputArray[outputByteNum] = ((byte)(inputChar >>> codingCase.initialShift));
/*     */             }
/*     */             else
/*     */             {
/*     */               int tmp139_137 = outputByteNum;
/*     */               byte[] tmp139_135 = outputArray; tmp139_135[tmp139_137] = ((byte)(tmp139_135[tmp139_137] + (byte)(inputChar >>> codingCase.initialShift)));
/*     */             }
/* 217 */             outputArray[(outputByteNum + 1)] = ((byte)((inputChar & codingCase.finalMask) << codingCase.finalShift));
/*     */           }
/*     */           else
/*     */           {
/*     */             int tmp182_180 = outputByteNum;
/*     */             byte[] tmp182_178 = outputArray; tmp182_178[tmp182_180] = ((byte)(tmp182_178[tmp182_180] + (byte)(inputChar >>> codingCase.initialShift)));
/* 221 */             outputArray[(outputByteNum + 1)] = ((byte)((inputChar & codingCase.middleMask) >>> codingCase.middleShift));
/*     */ 
/* 223 */             outputArray[(outputByteNum + 2)] = ((byte)((inputChar & codingCase.finalMask) << codingCase.finalShift));
/*     */           }
/*     */ 
/* 226 */           outputByteNum += codingCase.advanceBytes;
/* 227 */           caseNum++; if (caseNum == CODING_CASES.length) {
/* 228 */             caseNum = 0;
/*     */           }
/*     */         }
/*     */ 
/* 232 */         short inputChar = (short)inputArray[inputCharNum];
/* 233 */         CodingCase codingCase = CODING_CASES[caseNum];
/* 234 */         if (0 == caseNum)
/* 235 */           outputArray[outputByteNum] = 0;
/*     */         int tmp303_301 = outputByteNum;
/*     */         byte[] tmp303_299 = outputArray; tmp303_299[tmp303_301] = ((byte)(tmp303_299[tmp303_301] + (byte)(inputChar >>> codingCase.initialShift)));
/* 238 */         int bytesLeft = numOutputBytes - outputByteNum;
/* 239 */         if (bytesLeft > 1) {
/* 240 */           if (2 == codingCase.numBytes) {
/* 241 */             outputArray[(outputByteNum + 1)] = ((byte)((inputChar & codingCase.finalMask) >>> codingCase.finalShift));
/*     */           }
/*     */           else {
/* 244 */             outputArray[(outputByteNum + 1)] = ((byte)((inputChar & codingCase.middleMask) >>> codingCase.middleShift));
/*     */ 
/* 246 */             if (bytesLeft > 2)
/* 247 */               outputArray[(outputByteNum + 2)] = ((byte)((inputChar & codingCase.finalMask) << codingCase.finalShift));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 254 */       throw new IllegalArgumentException("Arguments must have backing arrays");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ByteBuffer decode(CharBuffer input)
/*     */   {
/* 270 */     byte[] outputArray = new byte[getDecodedLength(input)];
/* 271 */     ByteBuffer output = ByteBuffer.wrap(outputArray);
/* 272 */     decode(input, output);
/* 273 */     return output;
/*     */   }
/*     */ 
/*     */   public static CharBuffer encode(ByteBuffer input)
/*     */   {
/* 286 */     char[] outputArray = new char[getEncodedLength(input)];
/* 287 */     CharBuffer output = CharBuffer.wrap(outputArray);
/* 288 */     encode(input, output);
/* 289 */     return output; } 
/*     */   static class CodingCase { int numBytes;
/*     */     int initialShift;
/*     */     int middleShift;
/*     */     int finalShift;
/* 293 */     int advanceBytes = 2;
/*     */     short middleMask;
/*     */     short finalMask;
/*     */ 
/* 297 */     CodingCase(int initialShift, int middleShift, int finalShift) { this.numBytes = 3;
/* 298 */       this.initialShift = initialShift;
/* 299 */       this.middleShift = middleShift;
/* 300 */       this.finalShift = finalShift;
/* 301 */       this.finalMask = ((short)(255 >>> finalShift));
/* 302 */       this.middleMask = ((short)(255 << middleShift)); }
/*     */ 
/*     */     CodingCase(int initialShift, int finalShift)
/*     */     {
/* 306 */       this.numBytes = 2;
/* 307 */       this.initialShift = initialShift;
/* 308 */       this.finalShift = finalShift;
/* 309 */       this.finalMask = ((short)(255 >>> finalShift));
/* 310 */       if (finalShift != 0)
/* 311 */         this.advanceBytes = 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.IndexableBinaryStringTools
 * JD-Core Version:    0.6.2
 */