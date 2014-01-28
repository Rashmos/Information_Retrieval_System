/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class RamUsageEstimator
/*     */ {
/*     */   private MemoryModel memoryModel;
/*     */   private final Map<Object, Object> seen;
/*     */   private int refSize;
/*     */   private int arraySize;
/*     */   private int classSize;
/*     */   private boolean checkInterned;
/*     */   private static final long ONE_KB = 1024L;
/*     */   private static final long ONE_MB = 1048576L;
/*     */   private static final long ONE_GB = 1073741824L;
/*     */ 
/*     */   public RamUsageEstimator()
/*     */   {
/*  53 */     this(new AverageGuessMemoryModel());
/*     */   }
/*     */ 
/*     */   public RamUsageEstimator(boolean checkInterned)
/*     */   {
/*  63 */     this(new AverageGuessMemoryModel(), checkInterned);
/*     */   }
/*     */ 
/*     */   public RamUsageEstimator(MemoryModel memoryModel)
/*     */   {
/*  70 */     this(memoryModel, true);
/*     */   }
/*     */ 
/*     */   public RamUsageEstimator(MemoryModel memoryModel, boolean checkInterned)
/*     */   {
/*  81 */     this.memoryModel = memoryModel;
/*  82 */     this.checkInterned = checkInterned;
/*     */ 
/*  85 */     this.seen = new IdentityHashMap(64);
/*  86 */     this.refSize = memoryModel.getReferenceSize();
/*  87 */     this.arraySize = memoryModel.getArraySize();
/*  88 */     this.classSize = memoryModel.getClassSize();
/*     */   }
/*     */ 
/*     */   public long estimateRamUsage(Object obj) {
/*  92 */     long size = size(obj);
/*  93 */     this.seen.clear();
/*  94 */     return size;
/*     */   }
/*     */ 
/*     */   private long size(Object obj) {
/*  98 */     if (obj == null) {
/*  99 */       return 0L;
/*     */     }
/*     */ 
/* 102 */     if ((this.checkInterned) && ((obj instanceof String)) && (obj == ((String)obj).intern()))
/*     */     {
/* 106 */       return 0L;
/*     */     }
/*     */ 
/* 110 */     if (this.seen.containsKey(obj)) {
/* 111 */       return 0L;
/*     */     }
/*     */ 
/* 115 */     this.seen.put(obj, null);
/*     */ 
/* 117 */     Class clazz = obj.getClass();
/* 118 */     if (clazz.isArray()) {
/* 119 */       return sizeOfArray(obj);
/*     */     }
/*     */ 
/* 122 */     long size = 0L;
/*     */ 
/* 125 */     while (clazz != null) {
/* 126 */       Field[] fields = clazz.getDeclaredFields();
/* 127 */       for (int i = 0; i < fields.length; i++) {
/* 128 */         if (!Modifier.isStatic(fields[i].getModifiers()))
/*     */         {
/* 132 */           if (fields[i].getType().isPrimitive()) {
/* 133 */             size += this.memoryModel.getPrimitiveSize(fields[i].getType());
/*     */           } else {
/* 135 */             size += this.refSize;
/* 136 */             fields[i].setAccessible(true);
/*     */             try {
/* 138 */               Object value = fields[i].get(obj);
/* 139 */               if (value != null)
/* 140 */                 size += size(value);
/*     */             }
/*     */             catch (IllegalAccessException ex)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 148 */       clazz = clazz.getSuperclass();
/*     */     }
/* 150 */     size += this.classSize;
/* 151 */     return size;
/*     */   }
/*     */ 
/*     */   private long sizeOfArray(Object obj) {
/* 155 */     int len = Array.getLength(obj);
/* 156 */     if (len == 0) {
/* 157 */       return 0L;
/*     */     }
/* 159 */     long size = this.arraySize;
/* 160 */     Class arrayElementClazz = obj.getClass().getComponentType();
/* 161 */     if (arrayElementClazz.isPrimitive())
/* 162 */       size += len * this.memoryModel.getPrimitiveSize(arrayElementClazz);
/*     */     else {
/* 164 */       for (int i = 0; i < len; i++) {
/* 165 */         size += this.refSize + size(Array.get(obj, i));
/*     */       }
/*     */     }
/*     */ 
/* 169 */     return size;
/*     */   }
/*     */ 
/*     */   public static String humanReadableUnits(long bytes, DecimalFormat df)
/*     */   {
/*     */     String newSizeAndUnits;
/*     */     String newSizeAndUnits;
/* 182 */     if (bytes / 1073741824L > 0L) {
/* 183 */       newSizeAndUnits = String.valueOf(df.format((float)bytes / 1.073742E+09F)) + " GB";
/*     */     }
/*     */     else
/*     */     {
/*     */       String newSizeAndUnits;
/* 185 */       if (bytes / 1048576L > 0L) {
/* 186 */         newSizeAndUnits = String.valueOf(df.format((float)bytes / 1048576.0F)) + " MB";
/*     */       }
/*     */       else
/*     */       {
/*     */         String newSizeAndUnits;
/* 188 */         if (bytes / 1024L > 0L) {
/* 189 */           newSizeAndUnits = String.valueOf(df.format((float)bytes / 1024.0F)) + " KB";
/*     */         }
/*     */         else
/* 192 */           newSizeAndUnits = String.valueOf(bytes) + " bytes";
/*     */       }
/*     */     }
/* 195 */     return newSizeAndUnits;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.RamUsageEstimator
 * JD-Core Version:    0.6.2
 */