/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SparseFloatVector extends AbstractVector
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -6258691051932319575L;
/*     */   final int[] mKeys;
/*     */   final float[] mValues;
/*     */   final int mNumDimensions;
/*     */   final double mLength;
/* 521 */   static final Integer[] EMPTY_INTEGER_ARRAY = new Integer[0];
/*     */ 
/*     */   public SparseFloatVector(Map<Integer, ? extends Number> map)
/*     */   {
/*  86 */     this(map, -1, false);
/*     */   }
/*     */ 
/*     */   public SparseFloatVector(Map<Integer, ? extends Number> map, int numDimensions)
/*     */   {
/* 101 */     this(map, numDimensions, true);
/*     */   }
/*     */ 
/*     */   public SparseFloatVector(int[] keys, float[] values, int numDimensions)
/*     */   {
/* 120 */     this(keys, values, numDimensions, constructorLength(values));
/* 121 */     if (keys.length != values.length) {
/* 122 */       String msg = "Keys and values must be same length. Found keys.length=" + keys.length + " values.length=" + values.length;
/*     */ 
/* 125 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 127 */     for (int i = 1; i < keys.length; i++) {
/* 128 */       if (keys[(i - 1)] >= keys[i]) {
/* 129 */         String msg = "Keys must be in strictly ascending order. Found keys[" + (i - 1) + "]=" + keys[(i - 1)] + " keys[" + i + "]=" + keys[i];
/*     */ 
/* 132 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 135 */     if ((keys.length > 0) && (keys[(keys.length - 1)] >= numDimensions)) {
/* 136 */       String msg = "Keys must be less than number of dimensions. Found numDimensions=" + numDimensions + " keys[" + (keys.length - 1) + "]=" + keys[(keys.length - 1)];
/*     */ 
/* 139 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static double constructorLength(float[] vs)
/*     */   {
/* 145 */     double sum = 0.0D;
/* 146 */     for (int i = 0; i < vs.length; i++)
/* 147 */       sum += vs[i] * vs[i];
/* 148 */     return Math.sqrt(sum);
/*     */   }
/*     */ 
/*     */   SparseFloatVector(int[] keys, float[] values, int numDimensions, double length)
/*     */   {
/* 153 */     if (numDimensions < 0) {
/* 154 */       String msg = "Dimensionality must be positive. Found numDimensions=" + numDimensions;
/*     */ 
/* 156 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 158 */     this.mKeys = keys;
/* 159 */     this.mValues = values;
/* 160 */     this.mNumDimensions = numDimensions;
/* 161 */     this.mLength = length;
/*     */   }
/*     */ 
/*     */   private SparseFloatVector(Map<Integer, ? extends Number> map, int numDimensions, boolean useDims)
/*     */   {
/* 166 */     Integer[] keys = (Integer[])map.keySet().toArray(EMPTY_INTEGER_ARRAY);
/* 167 */     Arrays.sort(keys);
/* 168 */     int[] newKeys = new int[keys.length];
/* 169 */     for (int i = 0; i < keys.length; i++)
/* 170 */       newKeys[i] = keys[i].intValue();
/* 171 */     if ((newKeys.length > 0) && (newKeys[0] < 0)) {
/* 172 */       String msg = "All keys must be non-negative. Found key=" + newKeys[0];
/*     */ 
/* 174 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 176 */     float[] values = new float[keys.length];
/* 177 */     for (int i = 0; i < keys.length; i++)
/* 178 */       values[i] = ((Number)map.get(keys[i])).floatValue();
/* 179 */     this.mKeys = newKeys;
/* 180 */     this.mValues = values;
/* 181 */     if ((this.mKeys.length > 0) && (this.mKeys[(this.mKeys.length - 1)] == 2147483647)) {
/* 182 */       String msg = "Maximum dimension is Integer.MAX_VALUE-1 Found dimension=Integer.MAX_VALUE";
/*     */ 
/* 184 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 186 */     int maxFoundDimensions = this.mKeys.length == 0 ? 0 : this.mKeys[(this.mKeys.length - 1)] + 1;
/*     */ 
/* 188 */     if (useDims) {
/* 189 */       if (numDimensions < 0) {
/* 190 */         String msg = "Number of dimensions must be non-negative. Found numDimensions=" + numDimensions;
/*     */ 
/* 192 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 194 */       if (numDimensions < maxFoundDimensions) {
/* 195 */         String msg = "Specified number of dimensions lower than largest index. Num dimensions specified=" + numDimensions + " Largest dimension found=" + this.mKeys[(this.mKeys.length - 1)];
/*     */ 
/* 198 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 200 */       this.mNumDimensions = numDimensions;
/*     */     } else {
/* 202 */       this.mNumDimensions = maxFoundDimensions;
/*     */     }
/* 204 */     this.mLength = computeLength(values);
/*     */   }
/*     */ 
/*     */   public int numDimensions()
/*     */   {
/* 209 */     return this.mNumDimensions;
/*     */   }
/*     */ 
/*     */   public int[] nonZeroDimensions()
/*     */   {
/* 227 */     return this.mKeys;
/*     */   }
/*     */ 
/*     */   public void increment(double scale, Vector v)
/*     */   {
/* 239 */     String msg = "Can not set values in sparse float vectors.";
/* 240 */     throw new UnsupportedOperationException(msg);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 246 */     StringBuilder sb = new StringBuilder();
/* 247 */     for (int i = 0; i < this.mValues.length; i++) {
/* 248 */       if (i > 0) sb.append(' ');
/* 249 */       sb.append(this.mKeys[i] + "=" + this.mValues[i]);
/*     */     }
/* 251 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public double value(int dimension)
/*     */   {
/* 256 */     if ((dimension < 0) || (dimension >= this.mNumDimensions)) {
/* 257 */       String msg = "Dimension out of range. num dimensions in vector=" + this.mNumDimensions + " found dimension=" + dimension;
/*     */ 
/* 260 */       throw new IndexOutOfBoundsException(msg);
/*     */     }
/* 262 */     int index = Arrays.binarySearch(this.mKeys, dimension);
/* 263 */     return index < 0 ? 0.0D : this.mValues[index];
/*     */   }
/*     */ 
/*     */   public double length()
/*     */   {
/* 268 */     return this.mLength;
/*     */   }
/*     */ 
/*     */   static double computeLength(float[] vals) {
/* 272 */     double sum = 0.0D;
/* 273 */     for (int i = 0; i < vals.length; i++) {
/* 274 */       double val = vals[i];
/* 275 */       sum += val * val;
/*     */     }
/* 277 */     return Math.sqrt(sum);
/*     */   }
/*     */ 
/*     */   public Vector add(Vector v)
/*     */   {
/* 282 */     if (!(v instanceof SparseFloatVector))
/* 283 */       return Matrices.add(this, v);
/* 284 */     verifyMatchingDimensions(v);
/* 285 */     SparseFloatVector spv = (SparseFloatVector)v;
/* 286 */     int[] keys1 = this.mKeys;
/* 287 */     int[] keys2 = spv.mKeys;
/*     */ 
/* 289 */     int numMatching = 0;
/* 290 */     int index1 = 0;
/* 291 */     int index2 = 0;
/* 292 */     while ((index1 < keys1.length) && (index2 < keys2.length)) {
/* 293 */       numMatching++;
/* 294 */       int comp = keys1[index1] - keys2[index2];
/* 295 */       if (comp == 0) {
/* 296 */         index1++;
/* 297 */         index2++;
/* 298 */       } else if (comp < 0) {
/* 299 */         index1++;
/*     */       } else {
/* 301 */         index2++;
/*     */       }
/*     */     }
/* 304 */     while (index1 < keys1.length) {
/* 305 */       numMatching++;
/* 306 */       index1++;
/*     */     }
/* 308 */     while (index2 < keys2.length) {
/* 309 */       numMatching++;
/* 310 */       index2++;
/*     */     }
/*     */ 
/* 313 */     float[] vals1 = this.mValues;
/* 314 */     float[] vals2 = spv.mValues;
/*     */ 
/* 316 */     int[] resultKeys = new int[numMatching];
/* 317 */     float[] resultVals = new float[numMatching];
/*     */ 
/* 319 */     int resultIndex = 0;
/* 320 */     index1 = 0;
/* 321 */     index2 = 0;
/* 322 */     while ((index1 < keys1.length) && (index2 < keys2.length)) {
/* 323 */       int comp = keys1[index1] - keys2[index2];
/* 324 */       if (comp == 0) {
/* 325 */         resultKeys[resultIndex] = index1;
/* 326 */         vals1[index1] += vals2[index2];
/* 327 */         index1++;
/* 328 */         index2++;
/* 329 */         resultIndex++;
/* 330 */       } else if (comp < 0) {
/* 331 */         resultKeys[resultIndex] = index1;
/* 332 */         resultVals[resultIndex] = vals1[index1];
/* 333 */         index1++;
/* 334 */         resultIndex++;
/*     */       } else {
/* 336 */         resultKeys[resultIndex] = index2;
/* 337 */         resultVals[resultIndex] = vals2[index2];
/* 338 */         index2++;
/* 339 */         resultIndex++;
/*     */       }
/*     */     }
/* 342 */     while (index1 < keys1.length) {
/* 343 */       resultKeys[resultIndex] = index1;
/* 344 */       resultVals[resultIndex] = vals1[index1];
/* 345 */       index1++;
/* 346 */       resultIndex++;
/*     */     }
/* 348 */     while (index2 < keys2.length) {
/* 349 */       resultKeys[resultIndex] = index2;
/* 350 */       resultVals[resultIndex] = vals2[index2];
/* 351 */       index2++;
/* 352 */       resultIndex++;
/*     */     }
/* 354 */     double lengthSquared = 0.0D;
/* 355 */     for (int i = 0; i < resultVals.length; i++)
/* 356 */       lengthSquared += resultVals[i] * resultVals[i];
/* 357 */     double length = Math.sqrt(lengthSquared);
/* 358 */     return new SparseFloatVector(resultKeys, resultVals, numDimensions(), length);
/*     */   }
/*     */ 
/*     */   public double dotProduct(Vector v)
/*     */   {
/* 366 */     verifyMatchingDimensions(v);
/*     */ 
/* 368 */     if ((v instanceof DenseVector))
/*     */     {
/* 370 */       double[] vValues = ((DenseVector)v).mValues;
/* 371 */       double sum = 0.0D;
/* 372 */       int[] keys1 = this.mKeys;
/* 373 */       float[] vals1 = this.mValues;
/* 374 */       for (int i = 0; i < keys1.length; i++)
/* 375 */         sum += vals1[i] * vValues[keys1[i]];
/* 376 */       return sum;
/*     */     }
/*     */ 
/* 379 */     if ((v instanceof SparseFloatVector)) {
/* 380 */       SparseFloatVector spv = (SparseFloatVector)v;
/* 381 */       int[] keys1 = this.mKeys;
/* 382 */       float[] vals1 = this.mValues;
/* 383 */       int[] keys2 = spv.mKeys;
/* 384 */       float[] vals2 = spv.mValues;
/*     */ 
/* 386 */       double sum = 0.0D;
/* 387 */       int index1 = 0;
/* 388 */       int index2 = 0;
/* 389 */       while ((index1 < keys1.length) && (index2 < keys2.length)) {
/* 390 */         int comp = keys1[index1] - keys2[index2];
/* 391 */         if (comp == 0)
/* 392 */           sum += vals1[(index1++)] * vals2[(index2++)];
/* 393 */         else if (comp < 0)
/* 394 */           index1++;
/*     */         else
/* 396 */           index2++;
/*     */       }
/* 398 */       return sum;
/*     */     }
/*     */ 
/* 402 */     double sum = 0.0D;
/* 403 */     int[] keys1 = this.mKeys;
/* 404 */     float[] vals1 = this.mValues;
/* 405 */     for (int i = 0; i < keys1.length; i++)
/* 406 */       sum += vals1[i] * v.value(keys1[i]);
/* 407 */     return sum;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 420 */     if ((that instanceof SparseFloatVector)) {
/* 421 */       SparseFloatVector thatVector = (SparseFloatVector)that;
/* 422 */       if (this.mKeys.length != thatVector.mKeys.length)
/* 423 */         return false;
/* 424 */       if (this.mNumDimensions != thatVector.mNumDimensions)
/* 425 */         return false;
/* 426 */       if (this.mLength != thatVector.mLength)
/* 427 */         return false;
/* 428 */       for (int i = 0; i < this.mKeys.length; i++)
/* 429 */         if (this.mKeys[i] != thatVector.mKeys[i])
/* 430 */           return false;
/* 431 */       for (int i = 0; i < this.mValues.length; i++)
/* 432 */         if (this.mValues[i] != thatVector.mValues[i])
/* 433 */           return false;
/* 434 */       return true;
/* 435 */     }if ((that instanceof Vector)) {
/* 436 */       Vector thatVector = (Vector)that;
/* 437 */       if (this.mNumDimensions != thatVector.numDimensions())
/* 438 */         return false;
/* 439 */       if (this.mLength != thatVector.length())
/* 440 */         return false;
/* 441 */       for (int i = 0; i < this.mKeys.length; i++)
/* 442 */         if (this.mValues[i] != thatVector.value(this.mKeys[i]))
/* 443 */           return false;
/* 444 */       return true;
/*     */     }
/* 446 */     return super.equals(that);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 462 */     int code = 1;
/* 463 */     for (int i = 0; i < this.mValues.length; i++) {
/* 464 */       long v = Double.doubleToLongBits(this.mValues[i]);
/* 465 */       int valHash = (int)(v ^ v >>> 32);
/* 466 */       code = 31 * code + valHash;
/*     */     }
/* 468 */     return code;
/*     */   }
/*     */ 
/*     */   public double cosine(Vector v)
/*     */   {
/* 474 */     double cosine = dotProduct(v) / (v.length() * length());
/* 475 */     return cosine > 1.0D ? 1.0D : cosine < -1.0D ? -1.0D : cosine;
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 483 */     return new Externalizer(this);
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -7216149275959287094L;
/*     */     final SparseFloatVector mVector;
/*     */ 
/*     */     public Externalizer() {
/* 491 */       this(null);
/*     */     }
/*     */     public Externalizer(SparseFloatVector vector) {
/* 494 */       this.mVector = vector;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 498 */       int len = in.readInt();
/* 499 */       int numDimensions = in.readInt();
/* 500 */       double length = in.readDouble();
/* 501 */       int[] keys = new int[len];
/* 502 */       for (int i = 0; i < keys.length; i++)
/* 503 */         keys[i] = in.readInt();
/* 504 */       float[] values = new float[len];
/* 505 */       for (int i = 0; i < len; i++)
/* 506 */         values[i] = in.readFloat();
/* 507 */       return new SparseFloatVector(keys, values, numDimensions, length);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 511 */       out.writeInt(this.mVector.mKeys.length);
/* 512 */       out.writeInt(this.mVector.mNumDimensions);
/* 513 */       out.writeDouble(this.mVector.mLength);
/* 514 */       for (int i = 0; i < this.mVector.mKeys.length; i++)
/* 515 */         out.writeInt(this.mVector.mKeys[i]);
/* 516 */       for (int i = 0; i < this.mVector.mValues.length; i++)
/* 517 */         out.writeFloat(this.mVector.mValues[i]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.SparseFloatVector
 * JD-Core Version:    0.6.2
 */