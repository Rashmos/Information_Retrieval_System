/*     */ package org.apache.lucene.analysis;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CharArraySet extends AbstractSet<Object>
/*     */ {
/*     */   private static final int INIT_SIZE = 8;
/*     */   private char[][] entries;
/*     */   private int count;
/*     */   private final boolean ignoreCase;
/*  51 */   public static final CharArraySet EMPTY_SET = unmodifiableSet(new CharArraySet(0, false));
/*     */ 
/*     */   public CharArraySet(int startSize, boolean ignoreCase)
/*     */   {
/*  56 */     this.ignoreCase = ignoreCase;
/*  57 */     int size = 8;
/*  58 */     while (startSize + (startSize >> 2) > size)
/*  59 */       size <<= 1;
/*  60 */     this.entries = new char[size][];
/*     */   }
/*     */ 
/*     */   public CharArraySet(Collection<? extends Object> c, boolean ignoreCase)
/*     */   {
/*  65 */     this(c.size(), ignoreCase);
/*  66 */     addAll(c);
/*     */   }
/*     */ 
/*     */   private CharArraySet(char[][] entries, boolean ignoreCase, int count)
/*     */   {
/*  71 */     this.entries = entries;
/*  72 */     this.ignoreCase = ignoreCase;
/*  73 */     this.count = count;
/*     */   }
/*     */ 
/*     */   public boolean contains(char[] text, int off, int len)
/*     */   {
/*  79 */     return this.entries[getSlot(text, off, len)] != null;
/*     */   }
/*     */ 
/*     */   public boolean contains(CharSequence cs)
/*     */   {
/*  84 */     return this.entries[getSlot(cs)] != null;
/*     */   }
/*     */ 
/*     */   private int getSlot(char[] text, int off, int len) {
/*  88 */     int code = getHashCode(text, off, len);
/*  89 */     int pos = code & this.entries.length - 1;
/*  90 */     char[] text2 = this.entries[pos];
/*  91 */     if ((text2 != null) && (!equals(text, off, len, text2))) {
/*  92 */       int inc = (code >> 8) + code | 0x1;
/*     */       do {
/*  94 */         code += inc;
/*  95 */         pos = code & this.entries.length - 1;
/*  96 */         text2 = this.entries[pos];
/*  97 */       }while ((text2 != null) && (!equals(text, off, len, text2)));
/*     */     }
/*  99 */     return pos;
/*     */   }
/*     */ 
/*     */   private int getSlot(CharSequence text)
/*     */   {
/* 104 */     int code = getHashCode(text);
/* 105 */     int pos = code & this.entries.length - 1;
/* 106 */     char[] text2 = this.entries[pos];
/* 107 */     if ((text2 != null) && (!equals(text, text2))) {
/* 108 */       int inc = (code >> 8) + code | 0x1;
/*     */       do {
/* 110 */         code += inc;
/* 111 */         pos = code & this.entries.length - 1;
/* 112 */         text2 = this.entries[pos];
/* 113 */       }while ((text2 != null) && (!equals(text, text2)));
/*     */     }
/* 115 */     return pos;
/*     */   }
/*     */ 
/*     */   public boolean add(CharSequence text)
/*     */   {
/* 120 */     return add(text.toString());
/*     */   }
/*     */ 
/*     */   public boolean add(String text)
/*     */   {
/* 125 */     return add(text.toCharArray());
/*     */   }
/*     */ 
/*     */   public boolean add(char[] text)
/*     */   {
/* 133 */     if (this.ignoreCase)
/* 134 */       for (int i = 0; i < text.length; i++)
/* 135 */         text[i] = Character.toLowerCase(text[i]);
/* 136 */     int slot = getSlot(text, 0, text.length);
/* 137 */     if (this.entries[slot] != null) return false;
/* 138 */     this.entries[slot] = text;
/* 139 */     this.count += 1;
/*     */ 
/* 141 */     if (this.count + (this.count >> 2) > this.entries.length) {
/* 142 */       rehash();
/*     */     }
/*     */ 
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean equals(char[] text1, int off, int len, char[] text2) {
/* 149 */     if (len != text2.length)
/* 150 */       return false;
/* 151 */     if (this.ignoreCase) {
/* 152 */       for (int i = 0; i < len; i++)
/* 153 */         if (Character.toLowerCase(text1[(off + i)]) != text2[i])
/* 154 */           return false;
/*     */     }
/*     */     else {
/* 157 */       for (int i = 0; i < len; i++) {
/* 158 */         if (text1[(off + i)] != text2[i])
/* 159 */           return false;
/*     */       }
/*     */     }
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean equals(CharSequence text1, char[] text2) {
/* 166 */     int len = text1.length();
/* 167 */     if (len != text2.length)
/* 168 */       return false;
/* 169 */     if (this.ignoreCase) {
/* 170 */       for (int i = 0; i < len; i++)
/* 171 */         if (Character.toLowerCase(text1.charAt(i)) != text2[i])
/* 172 */           return false;
/*     */     }
/*     */     else {
/* 175 */       for (int i = 0; i < len; i++) {
/* 176 */         if (text1.charAt(i) != text2[i])
/* 177 */           return false;
/*     */       }
/*     */     }
/* 180 */     return true;
/*     */   }
/*     */ 
/*     */   private void rehash() {
/* 184 */     int newSize = 2 * this.entries.length;
/* 185 */     char[][] oldEntries = this.entries;
/* 186 */     this.entries = new char[newSize][];
/*     */ 
/* 188 */     for (int i = 0; i < oldEntries.length; i++) {
/* 189 */       char[] text = oldEntries[i];
/* 190 */       if (text != null)
/*     */       {
/* 192 */         this.entries[getSlot(text, 0, text.length)] = text;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getHashCode(char[] text, int offset, int len) {
/* 198 */     int code = 0;
/* 199 */     int stop = offset + len;
/* 200 */     if (this.ignoreCase) {
/* 201 */       for (int i = offset; i < stop; i++)
/* 202 */         code = code * 31 + Character.toLowerCase(text[i]);
/*     */     }
/*     */     else {
/* 205 */       for (int i = offset; i < stop; i++) {
/* 206 */         code = code * 31 + text[i];
/*     */       }
/*     */     }
/* 209 */     return code;
/*     */   }
/*     */ 
/*     */   private int getHashCode(CharSequence text) {
/* 213 */     int code = 0;
/* 214 */     int len = text.length();
/* 215 */     if (this.ignoreCase) {
/* 216 */       for (int i = 0; i < len; i++)
/* 217 */         code = code * 31 + Character.toLowerCase(text.charAt(i));
/*     */     }
/*     */     else {
/* 220 */       for (int i = 0; i < len; i++) {
/* 221 */         code = code * 31 + text.charAt(i);
/*     */       }
/*     */     }
/* 224 */     return code;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 230 */     return this.count;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 235 */     return this.count == 0;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object o)
/*     */   {
/* 240 */     if ((o instanceof char[])) {
/* 241 */       char[] text = (char[])o;
/* 242 */       return contains(text, 0, text.length);
/*     */     }
/* 244 */     return contains(o.toString());
/*     */   }
/*     */ 
/*     */   public boolean add(Object o)
/*     */   {
/* 249 */     if ((o instanceof char[])) {
/* 250 */       return add((char[])o);
/*     */     }
/* 252 */     return add(o.toString());
/*     */   }
/*     */ 
/*     */   public static CharArraySet unmodifiableSet(CharArraySet set)
/*     */   {
/* 266 */     if (set == null)
/* 267 */       throw new NullPointerException("Given set is null");
/* 268 */     if (set == EMPTY_SET)
/* 269 */       return EMPTY_SET;
/* 270 */     if ((set instanceof UnmodifiableCharArraySet)) {
/* 271 */       return set;
/*     */     }
/*     */ 
/* 277 */     return new UnmodifiableCharArraySet(set.entries, set.ignoreCase, set.count, null);
/*     */   }
/*     */ 
/*     */   public static CharArraySet copy(Set<?> set)
/*     */   {
/* 291 */     if (set == null)
/* 292 */       throw new NullPointerException("Given set is null");
/* 293 */     if (set == EMPTY_SET)
/* 294 */       return EMPTY_SET;
/* 295 */     boolean ignoreCase = (set instanceof CharArraySet) ? ((CharArraySet)set).ignoreCase : false;
/*     */ 
/* 297 */     return new CharArraySet(set, ignoreCase);
/*     */   }
/*     */ 
/*     */   public Iterator<String> stringIterator()
/*     */   {
/* 340 */     return new CharArraySetIterator();
/*     */   }
/*     */ 
/*     */   public Iterator<Object> iterator()
/*     */   {
/* 347 */     return stringIterator();
/*     */   }
/*     */ 
/*     */   private static final class UnmodifiableCharArraySet extends CharArraySet
/*     */   {
/*     */     private UnmodifiableCharArraySet(char[][] entries, boolean ignoreCase, int count)
/*     */     {
/* 361 */       super(ignoreCase, count, null);
/*     */     }
/*     */ 
/*     */     public boolean add(Object o)
/*     */     {
/* 366 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean addAll(Collection<? extends Object> coll)
/*     */     {
/* 371 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean add(char[] text)
/*     */     {
/* 376 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean add(CharSequence text)
/*     */     {
/* 381 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public boolean add(String text)
/*     */     {
/* 386 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   public class CharArraySetIterator
/*     */     implements Iterator<String>
/*     */   {
/* 304 */     int pos = -1;
/*     */     char[] next;
/*     */ 
/*     */     CharArraySetIterator()
/*     */     {
/* 307 */       goNext();
/*     */     }
/*     */ 
/*     */     private void goNext() {
/* 311 */       this.next = null;
/* 312 */       this.pos += 1;
/* 313 */       while ((this.pos < CharArraySet.this.entries.length) && ((this.next = CharArraySet.this.entries[this.pos]) == null)) this.pos += 1; 
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 317 */       return this.next != null;
/*     */     }
/*     */ 
/*     */     public char[] nextCharArray()
/*     */     {
/* 322 */       char[] ret = this.next;
/* 323 */       goNext();
/* 324 */       return ret;
/*     */     }
/*     */ 
/*     */     public String next()
/*     */     {
/* 330 */       return new String(nextCharArray());
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 334 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.CharArraySet
 * JD-Core Version:    0.6.2
 */