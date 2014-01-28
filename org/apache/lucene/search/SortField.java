/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
/*     */ import org.apache.lucene.util.StringHelper;
/*     */ 
/*     */ public class SortField
/*     */   implements Serializable
/*     */ {
/*     */   public static final int SCORE = 0;
/*     */   public static final int DOC = 1;
/*     */   public static final int STRING = 3;
/*     */   public static final int INT = 4;
/*     */   public static final int FLOAT = 5;
/*     */   public static final int LONG = 6;
/*     */   public static final int DOUBLE = 7;
/*     */   public static final int SHORT = 8;
/*     */   public static final int CUSTOM = 9;
/*     */   public static final int BYTE = 10;
/*     */   public static final int STRING_VAL = 11;
/*  95 */   public static final SortField FIELD_SCORE = new SortField(null, 0);
/*     */ 
/*  98 */   public static final SortField FIELD_DOC = new SortField(null, 1);
/*     */   private String field;
/*     */   private int type;
/*     */   private Locale locale;
/* 103 */   boolean reverse = false;
/*     */   private FieldCache.Parser parser;
/*     */   private FieldComparatorSource comparatorSource;
/*     */ 
/*     */   public SortField(String field, int type)
/*     */   {
/* 116 */     initFieldType(field, type);
/*     */   }
/*     */ 
/*     */   public SortField(String field, int type, boolean reverse)
/*     */   {
/* 127 */     initFieldType(field, type);
/* 128 */     this.reverse = reverse;
/*     */   }
/*     */ 
/*     */   public SortField(String field, FieldCache.Parser parser)
/*     */   {
/* 142 */     this(field, parser, false);
/*     */   }
/*     */ 
/*     */   public SortField(String field, FieldCache.Parser parser, boolean reverse)
/*     */   {
/* 157 */     if ((parser instanceof FieldCache.IntParser)) initFieldType(field, 4);
/* 158 */     else if ((parser instanceof FieldCache.FloatParser)) initFieldType(field, 5);
/* 159 */     else if ((parser instanceof FieldCache.ShortParser)) initFieldType(field, 8);
/* 160 */     else if ((parser instanceof FieldCache.ByteParser)) initFieldType(field, 10);
/* 161 */     else if ((parser instanceof FieldCache.LongParser)) initFieldType(field, 6);
/* 162 */     else if ((parser instanceof FieldCache.DoubleParser)) initFieldType(field, 7);
/*     */     else {
/* 164 */       throw new IllegalArgumentException("Parser instance does not subclass existing numeric parser from FieldCache (got " + parser + ")");
/*     */     }
/* 166 */     this.reverse = reverse;
/* 167 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public SortField(String field, Locale locale)
/*     */   {
/* 176 */     initFieldType(field, 3);
/* 177 */     this.locale = locale;
/*     */   }
/*     */ 
/*     */   public SortField(String field, Locale locale, boolean reverse)
/*     */   {
/* 186 */     initFieldType(field, 3);
/* 187 */     this.locale = locale;
/* 188 */     this.reverse = reverse;
/*     */   }
/*     */ 
/*     */   public SortField(String field, FieldComparatorSource comparator)
/*     */   {
/* 196 */     initFieldType(field, 9);
/* 197 */     this.comparatorSource = comparator;
/*     */   }
/*     */ 
/*     */   public SortField(String field, FieldComparatorSource comparator, boolean reverse)
/*     */   {
/* 206 */     initFieldType(field, 9);
/* 207 */     this.reverse = reverse;
/* 208 */     this.comparatorSource = comparator;
/*     */   }
/*     */ 
/*     */   private void initFieldType(String field, int type)
/*     */   {
/* 214 */     this.type = type;
/* 215 */     if (field == null) {
/* 216 */       if ((type != 0) && (type != 1))
/* 217 */         throw new IllegalArgumentException("field can only be null when type is SCORE or DOC");
/*     */     }
/* 219 */     else this.field = StringHelper.intern(field);
/*     */   }
/*     */ 
/*     */   public String getField()
/*     */   {
/* 228 */     return this.field;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 235 */     return this.type;
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 243 */     return this.locale;
/*     */   }
/*     */ 
/*     */   public FieldCache.Parser getParser()
/*     */   {
/* 251 */     return this.parser;
/*     */   }
/*     */ 
/*     */   public boolean getReverse()
/*     */   {
/* 258 */     return this.reverse;
/*     */   }
/*     */ 
/*     */   public FieldComparatorSource getComparatorSource()
/*     */   {
/* 265 */     return this.comparatorSource;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 270 */     StringBuilder buffer = new StringBuilder();
/* 271 */     switch (this.type) {
/*     */     case 0:
/* 273 */       buffer.append("<score>");
/* 274 */       break;
/*     */     case 1:
/* 277 */       buffer.append("<doc>");
/* 278 */       break;
/*     */     case 3:
/* 281 */       buffer.append("<string: \"").append(this.field).append("\">");
/* 282 */       break;
/*     */     case 11:
/* 285 */       buffer.append("<string_val: \"").append(this.field).append("\">");
/* 286 */       break;
/*     */     case 10:
/* 289 */       buffer.append("<byte: \"").append(this.field).append("\">");
/* 290 */       break;
/*     */     case 8:
/* 293 */       buffer.append("<short: \"").append(this.field).append("\">");
/* 294 */       break;
/*     */     case 4:
/* 297 */       buffer.append("<int: \"").append(this.field).append("\">");
/* 298 */       break;
/*     */     case 6:
/* 301 */       buffer.append("<long: \"").append(this.field).append("\">");
/* 302 */       break;
/*     */     case 5:
/* 305 */       buffer.append("<float: \"").append(this.field).append("\">");
/* 306 */       break;
/*     */     case 7:
/* 309 */       buffer.append("<double: \"").append(this.field).append("\">");
/* 310 */       break;
/*     */     case 9:
/* 313 */       buffer.append("<custom:\"").append(this.field).append("\": ").append(this.comparatorSource).append('>');
/* 314 */       break;
/*     */     case 2:
/*     */     default:
/* 317 */       buffer.append("<???: \"").append(this.field).append("\">");
/*     */     }
/*     */ 
/* 321 */     if (this.locale != null) buffer.append('(').append(this.locale).append(')');
/* 322 */     if (this.parser != null) buffer.append('(').append(this.parser).append(')');
/* 323 */     if (this.reverse) buffer.append('!');
/*     */ 
/* 325 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 334 */     if (this == o) return true;
/* 335 */     if (!(o instanceof SortField)) return false;
/* 336 */     SortField other = (SortField)o;
/* 337 */     return (other.field == this.field) && (other.type == this.type) && (other.reverse == this.reverse) && (other.locale == null ? this.locale == null : other.locale.equals(this.locale)) && (other.comparatorSource == null ? this.comparatorSource == null : other.comparatorSource.equals(this.comparatorSource)) && (other.parser == null ? this.parser == null : other.parser.equals(this.parser));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 354 */     int hash = this.type ^ 879060445 + Boolean.valueOf(this.reverse).hashCode() ^ 0xAF5998BB;
/* 355 */     if (this.field != null) hash += (this.field.hashCode() ^ 0xFF5685DD);
/* 356 */     if (this.locale != null) hash += (this.locale.hashCode() ^ 0x8150815);
/* 357 */     if (this.comparatorSource != null) hash += this.comparatorSource.hashCode();
/* 358 */     if (this.parser != null) hash += (this.parser.hashCode() ^ 0x3AAF56FF);
/* 359 */     return hash;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
/*     */   {
/* 364 */     in.defaultReadObject();
/* 365 */     if (this.field != null)
/* 366 */       this.field = StringHelper.intern(this.field);
/*     */   }
/*     */ 
/*     */   public FieldComparator getComparator(int numHits, int sortPos)
/*     */     throws IOException
/*     */   {
/* 384 */     if (this.locale != null)
/*     */     {
/* 388 */       return new FieldComparator.StringComparatorLocale(numHits, this.field, this.locale);
/*     */     }
/*     */ 
/* 391 */     switch (this.type) {
/*     */     case 0:
/* 393 */       return new FieldComparator.RelevanceComparator(numHits);
/*     */     case 1:
/* 396 */       return new FieldComparator.DocComparator(numHits);
/*     */     case 4:
/* 399 */       return new FieldComparator.IntComparator(numHits, this.field, this.parser);
/*     */     case 5:
/* 402 */       return new FieldComparator.FloatComparator(numHits, this.field, this.parser);
/*     */     case 6:
/* 405 */       return new FieldComparator.LongComparator(numHits, this.field, this.parser);
/*     */     case 7:
/* 408 */       return new FieldComparator.DoubleComparator(numHits, this.field, this.parser);
/*     */     case 10:
/* 411 */       return new FieldComparator.ByteComparator(numHits, this.field, this.parser);
/*     */     case 8:
/* 414 */       return new FieldComparator.ShortComparator(numHits, this.field, this.parser);
/*     */     case 9:
/* 417 */       assert (this.comparatorSource != null);
/* 418 */       return this.comparatorSource.newComparator(this.field, numHits, sortPos, this.reverse);
/*     */     case 3:
/* 421 */       return new FieldComparator.StringOrdValComparator(numHits, this.field, sortPos, this.reverse);
/*     */     case 11:
/* 424 */       return new FieldComparator.StringValComparator(numHits, this.field);
/*     */     case 2:
/*     */     }
/* 427 */     throw new IllegalStateException("Illegal sort type: " + this.type);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.SortField
 * JD-Core Version:    0.6.2
 */