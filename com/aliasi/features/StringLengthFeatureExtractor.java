/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.BinaryMap;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class StringLengthFeatureExtractor
/*     */   implements FeatureExtractor<CharSequence>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 4390057742097519384L;
/*     */   private final int[] mLengths;
/*     */   private final String[] mFeatureNames;
/*     */ 
/*     */   public StringLengthFeatureExtractor(int[] lengths)
/*     */   {
/*  81 */     if (lengths.length < 1) {
/*  82 */       String msg = "Require non-empty array of lengths.";
/*  83 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  85 */     for (int i = 0; i < lengths.length; i++) {
/*  86 */       if (lengths[i] < 0) {
/*  87 */         String msg = "Lengths must be non-negative. Found lengths[" + i + "]=" + lengths[i];
/*     */ 
/*  89 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/*  92 */     TreeSet lengthSet = new TreeSet();
/*  93 */     for (int length : lengths)
/*  94 */       lengthSet.add(Integer.valueOf(length));
/*  95 */     this.mLengths = new int[lengthSet.size()];
/*  96 */     int pos = 0;
/*  97 */     for (Integer length : lengthSet) {
/*  98 */       this.mLengths[pos] = length.intValue();
/*  99 */       pos++;
/*     */     }
/* 101 */     this.mFeatureNames = new String[this.mLengths.length];
/* 102 */     for (int i = 0; i < this.mLengths.length; i++)
/* 103 */       this.mFeatureNames[i] = ("LEN>=" + this.mLengths[i]);
/*     */   }
/*     */ 
/*     */   public Map<String, ? extends Number> features(CharSequence in) {
/* 107 */     int len = in.length();
/* 108 */     int end = 0;
/* 109 */     while ((end < this.mLengths.length) && (len >= this.mLengths[end]))
/* 110 */       end++;
/* 111 */     if (end == 0)
/* 112 */       return Collections.emptyMap();
/* 113 */     BinaryMap features = new BinaryMap();
/* 114 */     for (int i = 0; i < end; i++)
/* 115 */       features.add(this.mFeatureNames[i]);
/* 116 */     return features;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 120 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 5726292832004631457L;
/*     */     StringLengthFeatureExtractor mExtractor;
/*     */ 
/* 127 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(StringLengthFeatureExtractor extractor) {
/* 130 */       this.mExtractor = extractor;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 134 */       writeInts(this.mExtractor.mLengths, out);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 138 */       int[] lengths = readInts(in);
/* 139 */       return new StringLengthFeatureExtractor(lengths);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.StringLengthFeatureExtractor
 * JD-Core Version:    0.6.2
 */