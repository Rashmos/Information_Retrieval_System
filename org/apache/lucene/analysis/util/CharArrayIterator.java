/*     */ package org.apache.lucene.analysis.util;
/*     */ 
/*     */ import java.text.BreakIterator;
/*     */ import java.text.CharacterIterator;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class CharArrayIterator
/*     */   implements CharacterIterator
/*     */ {
/*     */   private char[] array;
/*     */   private int start;
/*     */   private int index;
/*     */   private int length;
/*     */   private int limit;
/* 173 */   public static final boolean HAS_BUGGY_BREAKITERATORS = v;
/*     */ 
/*     */   public char[] getText()
/*     */   {
/*  19 */     return this.array;
/*     */   }
/*     */ 
/*     */   public int getStart() {
/*  23 */     return this.start;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/*  27 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setText(char[] array, int start, int length)
/*     */   {
/*  38 */     this.array = array;
/*  39 */     this.start = start;
/*  40 */     this.index = start;
/*  41 */     this.length = length;
/*  42 */     this.limit = (start + length);
/*     */   }
/*     */ 
/*     */   public char current() {
/*  46 */     return this.index == this.limit ? 65535 : jreBugWorkaround(this.array[this.index]);
/*     */   }
/*     */ 
/*     */   protected abstract char jreBugWorkaround(char paramChar);
/*     */ 
/*     */   public char first() {
/*  52 */     this.index = this.start;
/*  53 */     return current();
/*     */   }
/*     */ 
/*     */   public int getBeginIndex() {
/*  57 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getEndIndex() {
/*  61 */     return this.length;
/*     */   }
/*     */ 
/*     */   public int getIndex() {
/*  65 */     return this.index - this.start;
/*     */   }
/*     */ 
/*     */   public char last() {
/*  69 */     this.index = (this.limit == this.start ? this.limit : this.limit - 1);
/*  70 */     return current();
/*     */   }
/*     */ 
/*     */   public char next() {
/*  74 */     if (++this.index >= this.limit) {
/*  75 */       this.index = this.limit;
/*  76 */       return 65535;
/*     */     }
/*  78 */     return current();
/*     */   }
/*     */ 
/*     */   public char previous()
/*     */   {
/*  83 */     if (--this.index < this.start) {
/*  84 */       this.index = this.start;
/*  85 */       return 65535;
/*     */     }
/*  87 */     return current();
/*     */   }
/*     */ 
/*     */   public char setIndex(int position)
/*     */   {
/*  92 */     if ((position < getBeginIndex()) || (position > getEndIndex()))
/*  93 */       throw new IllegalArgumentException("Illegal Position: " + position);
/*  94 */     this.index = (this.start + position);
/*  95 */     return current();
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try {
/* 101 */       return super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException e) {
/* 104 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static CharArrayIterator newSentenceInstance()
/*     */   {
/* 113 */     if (HAS_BUGGY_BREAKITERATORS) {
/* 114 */       return new CharArrayIterator()
/*     */       {
/*     */         protected char jreBugWorkaround(char ch)
/*     */         {
/* 120 */           return (ch >= 55296) && (ch <= 57343) ? ',' : ch;
/*     */         }
/*     */       };
/*     */     }
/* 124 */     return new CharArrayIterator()
/*     */     {
/*     */       protected char jreBugWorkaround(char ch)
/*     */       {
/* 128 */         return ch;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static CharArrayIterator newWordInstance()
/*     */   {
/* 139 */     if (HAS_BUGGY_BREAKITERATORS) {
/* 140 */       return new CharArrayIterator()
/*     */       {
/*     */         protected char jreBugWorkaround(char ch)
/*     */         {
/* 145 */           return (ch >= 55296) && (ch <= 57343) ? 'A' : ch;
/*     */         }
/*     */       };
/*     */     }
/* 149 */     return new CharArrayIterator()
/*     */     {
/*     */       protected char jreBugWorkaround(char ch)
/*     */       {
/* 153 */         return ch;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     boolean v;
/*     */     try
/*     */     {
/* 166 */       BreakIterator bi = BreakIterator.getSentenceInstance(Locale.US);
/* 167 */       bi.setText("í­€í±“");
/* 168 */       bi.next();
/* 169 */       v = false;
/*     */     } catch (Exception e) {
/* 171 */       v = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.util.CharArrayIterator
 * JD-Core Version:    0.6.2
 */