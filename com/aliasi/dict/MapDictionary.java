/*     */ package com.aliasi.dict;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.ObjectToSet;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class MapDictionary<C> extends AbstractDictionary<C>
/*     */   implements Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3296124888445414454L;
/*     */   final ObjectToSet<String, DictionaryEntry<C>> mPhraseToEntrySet;
/*     */ 
/*     */   public MapDictionary()
/*     */   {
/*  67 */     this(new ObjectToSet());
/*     */   }
/*     */ 
/*     */   private MapDictionary(ObjectToSet<String, DictionaryEntry<C>> phraseToEntrySet) {
/*  71 */     this.mPhraseToEntrySet = phraseToEntrySet;
/*     */   }
/*     */ 
/*     */   public void addEntry(DictionaryEntry<C> entry)
/*     */   {
/*  76 */     this.mPhraseToEntrySet.addMember(entry.phrase(), entry);
/*     */   }
/*     */ 
/*     */   public Iterator<DictionaryEntry<C>> iterator()
/*     */   {
/*  86 */     return this.mPhraseToEntrySet.memberIterator();
/*     */   }
/*     */ 
/*     */   public Iterator<DictionaryEntry<C>> phraseEntryIt(String phrase)
/*     */   {
/*  91 */     return this.mPhraseToEntrySet.getSet(phrase).iterator();
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/*  99 */     out.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 103 */     return new Externalizer(this);
/*     */   }
/*     */   private static class Externalizer<D> extends AbstractExternalizable { private static final long serialVersionUID = -9136273040574611243L;
/*     */     final MapDictionary<D> mDictionary;
/*     */ 
/* 109 */     public Externalizer() { this(null); } 
/*     */     public Externalizer(MapDictionary<D> dictionary) {
/* 111 */       this.mDictionary = dictionary;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 117 */       MapDictionary dict = new MapDictionary();
/* 118 */       int numEntries = in.readInt();
/* 119 */       for (int i = 0; i < numEntries; i++)
/*     */       {
/* 122 */         DictionaryEntry entry = (DictionaryEntry)in.readObject();
/* 123 */         dict.addEntry(entry);
/*     */       }
/* 125 */       return dict;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 129 */       objOut.writeInt(this.mDictionary.size());
/* 130 */       for (DictionaryEntry entry : this.mDictionary)
/* 131 */         entry.compileTo(objOut);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.MapDictionary
 * JD-Core Version:    0.6.2
 */