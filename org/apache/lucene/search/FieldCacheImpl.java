/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.index.TermDocs;
/*     */ import org.apache.lucene.index.TermEnum;
/*     */ import org.apache.lucene.util.FieldCacheSanityChecker;
/*     */ import org.apache.lucene.util.FieldCacheSanityChecker.Insanity;
/*     */ import org.apache.lucene.util.StringHelper;
/*     */ 
/*     */ class FieldCacheImpl
/*     */   implements FieldCache
/*     */ {
/*     */   private Map<Class<?>, Cache> caches;
/*     */   private volatile PrintStream infoStream;
/*     */ 
/*     */   FieldCacheImpl()
/*     */   {
/*  47 */     init();
/*     */   }
/*     */   private synchronized void init() {
/*  50 */     this.caches = new HashMap(7);
/*  51 */     this.caches.put(Byte.TYPE, new ByteCache(this));
/*  52 */     this.caches.put(Short.TYPE, new ShortCache(this));
/*  53 */     this.caches.put(Integer.TYPE, new IntCache(this));
/*  54 */     this.caches.put(Float.TYPE, new FloatCache(this));
/*  55 */     this.caches.put(Long.TYPE, new LongCache(this));
/*  56 */     this.caches.put(Double.TYPE, new DoubleCache(this));
/*  57 */     this.caches.put(String.class, new StringCache(this));
/*  58 */     this.caches.put(FieldCache.StringIndex.class, new StringIndexCache(this));
/*     */   }
/*     */ 
/*     */   public void purgeAllCaches() {
/*  62 */     init();
/*     */   }
/*     */ 
/*     */   public void purge(IndexReader r) {
/*  66 */     for (Cache c : this.caches.values())
/*  67 */       c.purge(r);
/*     */   }
/*     */ 
/*     */   public FieldCache.CacheEntry[] getCacheEntries()
/*     */   {
/*  72 */     List result = new ArrayList(17);
/*  73 */     for (Iterator i$ = this.caches.keySet().iterator(); i$.hasNext(); ) { cacheType = (Class)i$.next();
/*  74 */       cache = (Cache)this.caches.get(cacheType);
/*  75 */       for (i$ = cache.readerCache.keySet().iterator(); i$.hasNext(); ) { readerKey = i$.next();
/*     */ 
/*  80 */         if ((null != readerKey) && (cache.readerCache.containsKey(readerKey))) {
/*  81 */           Map innerCache = (Map)cache.readerCache.get(readerKey);
/*  82 */           for (Map.Entry mapEntry : innerCache.entrySet()) {
/*  83 */             Entry entry = (Entry)mapEntry.getKey();
/*  84 */             result.add(new CacheEntryImpl(readerKey, entry.field, cacheType, entry.custom, mapEntry.getValue()));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Class cacheType;
/*     */     Cache cache;
/*     */     Iterator i$;
/*     */     Object readerKey;
/*  91 */     return (FieldCache.CacheEntry[])result.toArray(new FieldCache.CacheEntry[result.size()]);
/*     */   }
/*     */ 
/*     */   public byte[] getBytes(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 258 */     return getBytes(reader, field, null);
/*     */   }
/*     */ 
/*     */   public byte[] getBytes(IndexReader reader, String field, FieldCache.ByteParser parser)
/*     */     throws IOException
/*     */   {
/* 264 */     return (byte[])((Cache)this.caches.get(Byte.TYPE)).get(reader, new Entry(field, parser));
/*     */   }
/*     */ 
/*     */   public short[] getShorts(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 304 */     return getShorts(reader, field, null);
/*     */   }
/*     */ 
/*     */   public short[] getShorts(IndexReader reader, String field, FieldCache.ShortParser parser)
/*     */     throws IOException
/*     */   {
/* 310 */     return (short[])((Cache)this.caches.get(Short.TYPE)).get(reader, new Entry(field, parser));
/*     */   }
/*     */ 
/*     */   public int[] getInts(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 351 */     return getInts(reader, field, null);
/*     */   }
/*     */ 
/*     */   public int[] getInts(IndexReader reader, String field, FieldCache.IntParser parser)
/*     */     throws IOException
/*     */   {
/* 357 */     return (int[])((Cache)this.caches.get(Integer.TYPE)).get(reader, new Entry(field, parser));
/*     */   }
/*     */ 
/*     */   public float[] getFloats(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 408 */     return getFloats(reader, field, null);
/*     */   }
/*     */ 
/*     */   public float[] getFloats(IndexReader reader, String field, FieldCache.FloatParser parser)
/*     */     throws IOException
/*     */   {
/* 415 */     return (float[])((Cache)this.caches.get(Float.TYPE)).get(reader, new Entry(field, parser));
/*     */   }
/*     */ 
/*     */   public long[] getLongs(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 464 */     return getLongs(reader, field, null);
/*     */   }
/*     */ 
/*     */   public long[] getLongs(IndexReader reader, String field, FieldCache.LongParser parser)
/*     */     throws IOException
/*     */   {
/* 470 */     return (long[])((Cache)this.caches.get(Long.TYPE)).get(reader, new Entry(field, parser));
/*     */   }
/*     */ 
/*     */   public double[] getDoubles(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 519 */     return getDoubles(reader, field, null);
/*     */   }
/*     */ 
/*     */   public double[] getDoubles(IndexReader reader, String field, FieldCache.DoubleParser parser)
/*     */     throws IOException
/*     */   {
/* 525 */     return (double[])((Cache)this.caches.get(Double.TYPE)).get(reader, new Entry(field, parser));
/*     */   }
/*     */ 
/*     */   public String[] getStrings(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 575 */     return (String[])((Cache)this.caches.get(String.class)).get(reader, new Entry(field, (FieldCache.Parser)null));
/*     */   }
/*     */ 
/*     */   public FieldCache.StringIndex getStringIndex(IndexReader reader, String field)
/*     */     throws IOException
/*     */   {
/* 611 */     return (FieldCache.StringIndex)((Cache)this.caches.get(FieldCache.StringIndex.class)).get(reader, new Entry(field, (FieldCache.Parser)null));
/*     */   }
/*     */ 
/*     */   public void setInfoStream(PrintStream stream)
/*     */   {
/* 675 */     this.infoStream = stream;
/*     */   }
/*     */ 
/*     */   public PrintStream getInfoStream() {
/* 679 */     return this.infoStream;
/*     */   }
/*     */ 
/*     */   static final class StringIndexCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     StringIndexCache(FieldCache wrapper)
/*     */     {
/* 616 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entryKey)
/*     */       throws IOException
/*     */     {
/* 622 */       String field = StringHelper.intern(entryKey.field);
/* 623 */       int[] retArray = new int[reader.maxDoc()];
/* 624 */       String[] mterms = new String[reader.maxDoc() + 1];
/* 625 */       TermDocs termDocs = reader.termDocs();
/* 626 */       TermEnum termEnum = reader.terms(new Term(field));
/* 627 */       int t = 0;
/*     */ 
/* 633 */       mterms[(t++)] = null;
/*     */       try
/*     */       {
/*     */         do {
/* 637 */           Term term = termEnum.term();
/* 638 */           if ((term == null) || (term.field() != field)) {
/*     */             break;
/*     */           }
/* 641 */           mterms[t] = term.text();
/*     */ 
/* 643 */           termDocs.seek(termEnum);
/* 644 */           while (termDocs.next()) {
/* 645 */             retArray[termDocs.doc()] = t;
/*     */           }
/*     */ 
/* 648 */           t++;
/* 649 */         }while (termEnum.next());
/*     */       } finally {
/* 651 */         termDocs.close();
/* 652 */         termEnum.close();
/*     */       }
/*     */ 
/* 655 */       if (t == 0)
/*     */       {
/* 658 */         mterms = new String[1];
/* 659 */       } else if (t < mterms.length)
/*     */       {
/* 662 */         String[] terms = new String[t];
/* 663 */         System.arraycopy(mterms, 0, terms, 0, t);
/* 664 */         mterms = terms;
/*     */       }
/*     */ 
/* 667 */       FieldCache.StringIndex value = new FieldCache.StringIndex(retArray, mterms);
/* 668 */       return value;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class StringCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     StringCache(FieldCache wrapper)
/*     */     {
/* 580 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entryKey)
/*     */       throws IOException
/*     */     {
/* 586 */       String field = StringHelper.intern(entryKey.field);
/* 587 */       String[] retArray = new String[reader.maxDoc()];
/* 588 */       TermDocs termDocs = reader.termDocs();
/* 589 */       TermEnum termEnum = reader.terms(new Term(field));
/*     */       try {
/*     */         do {
/* 592 */           Term term = termEnum.term();
/* 593 */           if ((term == null) || (term.field() != field)) break;
/* 594 */           String termval = term.text();
/* 595 */           termDocs.seek(termEnum);
/* 596 */           while (termDocs.next())
/* 597 */             retArray[termDocs.doc()] = termval;
/*     */         }
/* 599 */         while (termEnum.next());
/*     */       } finally {
/* 601 */         termDocs.close();
/* 602 */         termEnum.close();
/*     */       }
/* 604 */       return retArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class DoubleCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     DoubleCache(FieldCache wrapper)
/*     */     {
/* 530 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entryKey)
/*     */       throws IOException
/*     */     {
/* 536 */       FieldCacheImpl.Entry entry = entryKey;
/* 537 */       String field = entry.field;
/* 538 */       FieldCache.DoubleParser parser = (FieldCache.DoubleParser)entry.custom;
/* 539 */       if (parser == null) {
/*     */         try {
/* 541 */           return this.wrapper.getDoubles(reader, field, FieldCache.DEFAULT_DOUBLE_PARSER);
/*     */         } catch (NumberFormatException ne) {
/* 543 */           return this.wrapper.getDoubles(reader, field, FieldCache.NUMERIC_UTILS_DOUBLE_PARSER);
/*     */         }
/*     */       }
/* 546 */       double[] retArray = null;
/* 547 */       TermDocs termDocs = reader.termDocs();
/* 548 */       TermEnum termEnum = reader.terms(new Term(field));
/*     */       try {
/*     */         do {
/* 551 */           Term term = termEnum.term();
/* 552 */           if ((term == null) || (term.field() != field)) break;
/* 553 */           double termval = parser.parseDouble(term.text());
/* 554 */           if (retArray == null)
/* 555 */             retArray = new double[reader.maxDoc()];
/* 556 */           termDocs.seek(termEnum);
/* 557 */           while (termDocs.next())
/* 558 */             retArray[termDocs.doc()] = termval;
/*     */         }
/* 560 */         while (termEnum.next());
/*     */       } catch (FieldCacheImpl.StopFillCacheException stop) {
/*     */       } finally {
/* 563 */         termDocs.close();
/* 564 */         termEnum.close();
/*     */       }
/* 566 */       if (retArray == null)
/* 567 */         retArray = new double[reader.maxDoc()];
/* 568 */       return retArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class LongCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     LongCache(FieldCache wrapper)
/*     */     {
/* 475 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entry)
/*     */       throws IOException
/*     */     {
/* 481 */       String field = entry.field;
/* 482 */       FieldCache.LongParser parser = (FieldCache.LongParser)entry.custom;
/* 483 */       if (parser == null) {
/*     */         try {
/* 485 */           return this.wrapper.getLongs(reader, field, FieldCache.DEFAULT_LONG_PARSER);
/*     */         } catch (NumberFormatException ne) {
/* 487 */           return this.wrapper.getLongs(reader, field, FieldCache.NUMERIC_UTILS_LONG_PARSER);
/*     */         }
/*     */       }
/* 490 */       long[] retArray = null;
/* 491 */       TermDocs termDocs = reader.termDocs();
/* 492 */       TermEnum termEnum = reader.terms(new Term(field));
/*     */       try {
/*     */         do {
/* 495 */           Term term = termEnum.term();
/* 496 */           if ((term == null) || (term.field() != field)) break;
/* 497 */           long termval = parser.parseLong(term.text());
/* 498 */           if (retArray == null)
/* 499 */             retArray = new long[reader.maxDoc()];
/* 500 */           termDocs.seek(termEnum);
/* 501 */           while (termDocs.next())
/* 502 */             retArray[termDocs.doc()] = termval;
/*     */         }
/* 504 */         while (termEnum.next());
/*     */       } catch (FieldCacheImpl.StopFillCacheException stop) {
/*     */       } finally {
/* 507 */         termDocs.close();
/* 508 */         termEnum.close();
/*     */       }
/* 510 */       if (retArray == null)
/* 511 */         retArray = new long[reader.maxDoc()];
/* 512 */       return retArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class FloatCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     FloatCache(FieldCache wrapper)
/*     */     {
/* 420 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entryKey)
/*     */       throws IOException
/*     */     {
/* 426 */       FieldCacheImpl.Entry entry = entryKey;
/* 427 */       String field = entry.field;
/* 428 */       FieldCache.FloatParser parser = (FieldCache.FloatParser)entry.custom;
/* 429 */       if (parser == null) {
/*     */         try {
/* 431 */           return this.wrapper.getFloats(reader, field, FieldCache.DEFAULT_FLOAT_PARSER);
/*     */         } catch (NumberFormatException ne) {
/* 433 */           return this.wrapper.getFloats(reader, field, FieldCache.NUMERIC_UTILS_FLOAT_PARSER);
/*     */         }
/*     */       }
/* 436 */       float[] retArray = null;
/* 437 */       TermDocs termDocs = reader.termDocs();
/* 438 */       TermEnum termEnum = reader.terms(new Term(field));
/*     */       try {
/*     */         do {
/* 441 */           Term term = termEnum.term();
/* 442 */           if ((term == null) || (term.field() != field)) break;
/* 443 */           float termval = parser.parseFloat(term.text());
/* 444 */           if (retArray == null)
/* 445 */             retArray = new float[reader.maxDoc()];
/* 446 */           termDocs.seek(termEnum);
/* 447 */           while (termDocs.next())
/* 448 */             retArray[termDocs.doc()] = termval;
/*     */         }
/* 450 */         while (termEnum.next());
/*     */       } catch (FieldCacheImpl.StopFillCacheException stop) {
/*     */       } finally {
/* 453 */         termDocs.close();
/* 454 */         termEnum.close();
/*     */       }
/* 456 */       if (retArray == null)
/* 457 */         retArray = new float[reader.maxDoc()];
/* 458 */       return retArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class IntCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     IntCache(FieldCache wrapper)
/*     */     {
/* 362 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entryKey)
/*     */       throws IOException
/*     */     {
/* 368 */       FieldCacheImpl.Entry entry = entryKey;
/* 369 */       String field = entry.field;
/* 370 */       FieldCache.IntParser parser = (FieldCache.IntParser)entry.custom;
/* 371 */       if (parser == null) {
/*     */         try {
/* 373 */           return this.wrapper.getInts(reader, field, FieldCache.DEFAULT_INT_PARSER);
/*     */         } catch (NumberFormatException ne) {
/* 375 */           return this.wrapper.getInts(reader, field, FieldCache.NUMERIC_UTILS_INT_PARSER);
/*     */         }
/*     */       }
/* 378 */       int[] retArray = null;
/* 379 */       TermDocs termDocs = reader.termDocs();
/* 380 */       TermEnum termEnum = reader.terms(new Term(field));
/*     */       try {
/*     */         do {
/* 383 */           Term term = termEnum.term();
/* 384 */           if ((term == null) || (term.field() != field)) break;
/* 385 */           int termval = parser.parseInt(term.text());
/* 386 */           if (retArray == null)
/* 387 */             retArray = new int[reader.maxDoc()];
/* 388 */           termDocs.seek(termEnum);
/* 389 */           while (termDocs.next())
/* 390 */             retArray[termDocs.doc()] = termval;
/*     */         }
/* 392 */         while (termEnum.next());
/*     */       } catch (FieldCacheImpl.StopFillCacheException stop) {
/*     */       } finally {
/* 395 */         termDocs.close();
/* 396 */         termEnum.close();
/*     */       }
/* 398 */       if (retArray == null)
/* 399 */         retArray = new int[reader.maxDoc()];
/* 400 */       return retArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class ShortCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     ShortCache(FieldCache wrapper)
/*     */     {
/* 315 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entryKey)
/*     */       throws IOException
/*     */     {
/* 321 */       FieldCacheImpl.Entry entry = entryKey;
/* 322 */       String field = entry.field;
/* 323 */       FieldCache.ShortParser parser = (FieldCache.ShortParser)entry.custom;
/* 324 */       if (parser == null) {
/* 325 */         return this.wrapper.getShorts(reader, field, FieldCache.DEFAULT_SHORT_PARSER);
/*     */       }
/* 327 */       short[] retArray = new short[reader.maxDoc()];
/* 328 */       TermDocs termDocs = reader.termDocs();
/* 329 */       TermEnum termEnum = reader.terms(new Term(field));
/*     */       try {
/*     */         do {
/* 332 */           Term term = termEnum.term();
/* 333 */           if ((term == null) || (term.field() != field)) break;
/* 334 */           short termval = parser.parseShort(term.text());
/* 335 */           termDocs.seek(termEnum);
/* 336 */           while (termDocs.next())
/* 337 */             retArray[termDocs.doc()] = termval;
/*     */         }
/* 339 */         while (termEnum.next());
/*     */       } catch (FieldCacheImpl.StopFillCacheException stop) {
/*     */       } finally {
/* 342 */         termDocs.close();
/* 343 */         termEnum.close();
/*     */       }
/* 345 */       return retArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class ByteCache extends FieldCacheImpl.Cache
/*     */   {
/*     */     ByteCache(FieldCache wrapper)
/*     */     {
/* 269 */       super();
/*     */     }
/*     */ 
/*     */     protected Object createValue(IndexReader reader, FieldCacheImpl.Entry entryKey) throws IOException
/*     */     {
/* 274 */       FieldCacheImpl.Entry entry = entryKey;
/* 275 */       String field = entry.field;
/* 276 */       FieldCache.ByteParser parser = (FieldCache.ByteParser)entry.custom;
/* 277 */       if (parser == null) {
/* 278 */         return this.wrapper.getBytes(reader, field, FieldCache.DEFAULT_BYTE_PARSER);
/*     */       }
/* 280 */       byte[] retArray = new byte[reader.maxDoc()];
/* 281 */       TermDocs termDocs = reader.termDocs();
/* 282 */       TermEnum termEnum = reader.terms(new Term(field));
/*     */       try {
/*     */         do {
/* 285 */           Term term = termEnum.term();
/* 286 */           if ((term == null) || (term.field() != field)) break;
/* 287 */           byte termval = parser.parseByte(term.text());
/* 288 */           termDocs.seek(termEnum);
/* 289 */           while (termDocs.next())
/* 290 */             retArray[termDocs.doc()] = termval;
/*     */         }
/* 292 */         while (termEnum.next());
/*     */       } catch (FieldCacheImpl.StopFillCacheException stop) {
/*     */       } finally {
/* 295 */         termDocs.close();
/* 296 */         termEnum.close();
/*     */       }
/* 298 */       return retArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Entry
/*     */   {
/*     */     final String field;
/*     */     final Object custom;
/*     */ 
/*     */     Entry(String field, Object custom)
/*     */     {
/* 229 */       this.field = StringHelper.intern(field);
/* 230 */       this.custom = custom;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/* 236 */       if ((o instanceof Entry)) {
/* 237 */         Entry other = (Entry)o;
/* 238 */         if (other.field == this.field) {
/* 239 */           if (other.custom == null) {
/* 240 */             if (this.custom == null) return true; 
/*     */           }
/* 241 */           else if (other.custom.equals(this.custom)) {
/* 242 */             return true;
/*     */           }
/*     */         }
/*     */       }
/* 246 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 252 */       return this.field.hashCode() ^ (this.custom == null ? 0 : this.custom.hashCode());
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class Cache
/*     */   {
/*     */     final FieldCache wrapper;
/* 148 */     final Map<Object, Map<FieldCacheImpl.Entry, Object>> readerCache = new WeakHashMap();
/*     */ 
/*     */     Cache()
/*     */     {
/* 139 */       this.wrapper = null;
/*     */     }
/*     */ 
/*     */     Cache(FieldCache wrapper) {
/* 143 */       this.wrapper = wrapper;
/*     */     }
/*     */ 
/*     */     protected abstract Object createValue(IndexReader paramIndexReader, FieldCacheImpl.Entry paramEntry)
/*     */       throws IOException;
/*     */ 
/*     */     public void purge(IndexReader r)
/*     */     {
/* 155 */       Object readerKey = r.getFieldCacheKey();
/* 156 */       synchronized (this.readerCache) {
/* 157 */         this.readerCache.remove(readerKey);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object get(IndexReader reader, FieldCacheImpl.Entry key)
/*     */       throws IOException
/*     */     {
/* 164 */       Object readerKey = reader.getFieldCacheKey();
/*     */       Map innerCache;
/*     */       Object value;
/* 165 */       synchronized (this.readerCache) {
/* 166 */         innerCache = (Map)this.readerCache.get(readerKey);
/*     */         Object value;
/* 167 */         if (innerCache == null) {
/* 168 */           innerCache = new HashMap();
/* 169 */           this.readerCache.put(readerKey, innerCache);
/* 170 */           value = null;
/*     */         } else {
/* 172 */           value = innerCache.get(key);
/*     */         }
/* 174 */         if (value == null) {
/* 175 */           value = new FieldCache.CreationPlaceholder();
/* 176 */           innerCache.put(key, value);
/*     */         }
/*     */       }
/* 179 */       if ((value instanceof FieldCache.CreationPlaceholder)) {
/* 180 */         synchronized (value) {
/* 181 */           FieldCache.CreationPlaceholder progress = (FieldCache.CreationPlaceholder)value;
/* 182 */           if (progress.value == null) {
/* 183 */             progress.value = createValue(reader, key);
/* 184 */             synchronized (this.readerCache) {
/* 185 */               innerCache.put(key, progress.value);
/*     */             }
/*     */ 
/* 191 */             if ((key.custom != null) && (this.wrapper != null)) {
/* 192 */               PrintStream infoStream = this.wrapper.getInfoStream();
/* 193 */               if (infoStream != null) {
/* 194 */                 printNewInsanity(infoStream, progress.value);
/*     */               }
/*     */             }
/*     */           }
/* 198 */           return progress.value;
/*     */         }
/*     */       }
/* 201 */       return value;
/*     */     }
/*     */ 
/*     */     private void printNewInsanity(PrintStream infoStream, Object value) {
/* 205 */       FieldCacheSanityChecker.Insanity[] insanities = FieldCacheSanityChecker.checkSanity(this.wrapper);
/* 206 */       for (int i = 0; i < insanities.length; i++) {
/* 207 */         FieldCacheSanityChecker.Insanity insanity = insanities[i];
/* 208 */         FieldCache.CacheEntry[] entries = insanity.getCacheEntries();
/* 209 */         for (int j = 0; j < entries.length; j++)
/* 210 */           if (entries[j].getValue() == value)
/*     */           {
/* 212 */             infoStream.println("WARNING: new FieldCache insanity created\nDetails: " + insanity.toString());
/* 213 */             infoStream.println("\nStack:\n");
/* 214 */             new Throwable().printStackTrace(infoStream);
/* 215 */             break;
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class StopFillCacheException extends RuntimeException
/*     */   {
/*     */   }
/*     */ 
/*     */   private static final class CacheEntryImpl extends FieldCache.CacheEntry
/*     */   {
/*     */     private final Object readerKey;
/*     */     private final String fieldName;
/*     */     private final Class<?> cacheType;
/*     */     private final Object custom;
/*     */     private final Object value;
/*     */ 
/*     */     CacheEntryImpl(Object readerKey, String fieldName, Class<?> cacheType, Object custom, Object value)
/*     */     {
/* 104 */       this.readerKey = readerKey;
/* 105 */       this.fieldName = fieldName;
/* 106 */       this.cacheType = cacheType;
/* 107 */       this.custom = custom;
/* 108 */       this.value = value;
/*     */     }
/*     */ 
/*     */     public Object getReaderKey()
/*     */     {
/* 117 */       return this.readerKey;
/*     */     }
/* 119 */     public String getFieldName() { return this.fieldName; } 
/*     */     public Class<?> getCacheType() {
/* 121 */       return this.cacheType;
/*     */     }
/* 123 */     public Object getCustom() { return this.custom; } 
/*     */     public Object getValue() {
/* 125 */       return this.value;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FieldCacheImpl
 * JD-Core Version:    0.6.2
 */