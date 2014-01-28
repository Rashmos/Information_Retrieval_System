/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ public class FieldScoreQuery extends ValueSourceQuery
/*     */ {
/*     */   public FieldScoreQuery(String field, Type type)
/*     */   {
/* 108 */     super(getValueSource(field, type));
/*     */   }
/*     */ 
/*     */   private static ValueSource getValueSource(String field, Type type)
/*     */   {
/* 113 */     if (type == Type.BYTE) {
/* 114 */       return new ByteFieldSource(field);
/*     */     }
/* 116 */     if (type == Type.SHORT) {
/* 117 */       return new ShortFieldSource(field);
/*     */     }
/* 119 */     if (type == Type.INT) {
/* 120 */       return new IntFieldSource(field);
/*     */     }
/* 122 */     if (type == Type.FLOAT) {
/* 123 */       return new FloatFieldSource(field);
/*     */     }
/* 125 */     throw new IllegalArgumentException(type + " is not a known Field Score Query Type!");
/*     */   }
/*     */ 
/*     */   public static class Type
/*     */   {
/*  77 */     public static final Type BYTE = new Type("byte");
/*     */ 
/*  80 */     public static final Type SHORT = new Type("short");
/*     */ 
/*  83 */     public static final Type INT = new Type("int");
/*     */ 
/*  86 */     public static final Type FLOAT = new Type("float");
/*     */     private String typeName;
/*     */ 
/*     */     private Type(String name)
/*     */     {
/*  90 */       this.typeName = name;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/*  95 */       return getClass().getName() + "::" + this.typeName;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.FieldScoreQuery
 * JD-Core Version:    0.6.2
 */