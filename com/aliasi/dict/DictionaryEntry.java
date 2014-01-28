/*     */ package com.aliasi.dict;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Scored;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public class DictionaryEntry<C>
/*     */   implements Compilable, Scored
/*     */ {
/*     */   private final String mPhrase;
/*     */   private final C mCategory;
/*     */   private final double mScore;
/*     */   private final int mCount;
/*     */ 
/*     */   public DictionaryEntry(String phrase, C category, int count, double score)
/*     */   {
/*  61 */     this.mPhrase = phrase;
/*  62 */     this.mCategory = category;
/*  63 */     this.mCount = count;
/*  64 */     this.mScore = score;
/*     */   }
/*     */ 
/*     */   public DictionaryEntry(String phrase, C category, double score)
/*     */   {
/*  78 */     this(phrase, category, 0, score);
/*     */   }
/*     */ 
/*     */   public DictionaryEntry(String phrase, C category, int count)
/*     */   {
/*  93 */     this(phrase, category, count, count);
/*     */   }
/*     */ 
/*     */   public DictionaryEntry(String phrase, C category)
/*     */   {
/* 106 */     this(phrase, category, 1);
/*     */   }
/*     */ 
/*     */   public String phrase()
/*     */   {
/* 115 */     return this.mPhrase;
/*     */   }
/*     */ 
/*     */   public C category()
/*     */   {
/* 124 */     return this.mCategory;
/*     */   }
/*     */ 
/*     */   public double score()
/*     */   {
/* 133 */     return this.mScore;
/*     */   }
/*     */ 
/*     */   public int count()
/*     */   {
/* 142 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 152 */     return phrase() + ":" + category() + " " + score();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 170 */     if (!(that instanceof DictionaryEntry)) return false;
/* 171 */     DictionaryEntry thatEntry = (DictionaryEntry)that;
/* 172 */     return (this.mPhrase.equals(thatEntry.mPhrase)) && (this.mCategory.equals(thatEntry.mCategory)) && (this.mScore == thatEntry.mScore) && (this.mCount == thatEntry.mCount);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 187 */     return this.mPhrase.hashCode() + 31 * this.mCategory.hashCode();
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 198 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */   private static class Externalizer<C2> extends AbstractExternalizable {
/*     */     private static final long serialVersionUID = -863015530144283246L;
/*     */     private final DictionaryEntry<C2> mEntry;
/*     */ 
/* 205 */     public Externalizer() { this(null); } 
/*     */     public Externalizer(DictionaryEntry<C2> entry) {
/* 207 */       this.mEntry = entry;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 213 */       String phrase = in.readUTF();
/*     */ 
/* 216 */       Object category = in.readObject();
/* 217 */       int count = in.readInt();
/* 218 */       double score = in.readDouble();
/* 219 */       return new DictionaryEntry(phrase, category, count, score);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 223 */       objOut.writeUTF(this.mEntry.phrase());
/* 224 */       Object category = this.mEntry.category();
/*     */ 
/* 226 */       AbstractExternalizable.compileOrSerialize(category, objOut);
/* 227 */       objOut.writeInt(this.mEntry.count());
/* 228 */       objOut.writeDouble(this.mEntry.score());
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.DictionaryEntry
 * JD-Core Version:    0.6.2
 */