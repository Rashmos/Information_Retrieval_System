/*     */ package com.aliasi.dict;
/*     */ 
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Iterators.Filter;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class AbstractDictionary<C> extends AbstractSet<DictionaryEntry<C>>
/*     */   implements Dictionary<C>, Compilable
/*     */ {
/*     */   public Iterator<DictionaryEntry<C>> phraseEntryIt(String phrase)
/*     */   {
/*  72 */     return new PhraseIterator(phrase);
/*     */   }
/*     */ 
/*     */   DictionaryEntry<C>[] phraseEntries(String phrase)
/*     */   {
/*  86 */     return itToEntries(phraseEntryIt(phrase));
/*     */   }
/*     */ 
/*     */   public List<DictionaryEntry<C>> phraseEntryList(String phrase)
/*     */   {
/*  99 */     return itToEntryList(phraseEntryIt(phrase));
/*     */   }
/*     */ 
/*     */   public Iterator<DictionaryEntry<C>> categoryEntryIt(C category)
/*     */   {
/* 114 */     return new CategoryIterator(category);
/*     */   }
/*     */ 
/*     */   DictionaryEntry<C>[] categoryEntries(C category)
/*     */   {
/* 128 */     return itToEntries(categoryEntryIt(category));
/*     */   }
/*     */ 
/*     */   public List<DictionaryEntry<C>> categoryEntryList(C category)
/*     */   {
/* 141 */     return itToEntryList(categoryEntryIt(category));
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 164 */     return entries().length;
/*     */   }
/*     */ 
/*     */   DictionaryEntry<C>[] entries()
/*     */   {
/* 177 */     return itToEntries(iterator());
/*     */   }
/*     */ 
/*     */   public List<DictionaryEntry<C>> entryList()
/*     */   {
/* 190 */     return itToEntryList(iterator());
/*     */   }
/*     */ 
/*     */   public void addEntry(DictionaryEntry<C> entry)
/*     */   {
/* 205 */     unsupported("addEntry(DictionaryEntry)");
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 221 */     unsupported("compileTo(ObjectOut)");
/*     */   }
/*     */ 
/*     */   private void unsupported(String op) {
/* 225 */     String msg = "Unsupported operation=" + op + " Class=" + getClass();
/*     */ 
/* 227 */     throw new UnsupportedOperationException(msg);
/*     */   }
/*     */ 
/*     */   private List<DictionaryEntry<C>> itToEntryList(Iterator<DictionaryEntry<C>> it) {
/* 231 */     List entryList = new ArrayList();
/*     */ 
/* 233 */     while (it.hasNext())
/* 234 */       entryList.add(it.next());
/* 235 */     return entryList;
/*     */   }
/*     */ 
/*     */   private DictionaryEntry<C>[] itToEntries(Iterator<DictionaryEntry<C>> it) {
/* 239 */     List entryList = itToEntryList(it);
/*     */ 
/* 241 */     DictionaryEntry[] entries = (DictionaryEntry[])new DictionaryEntry[entryList.size()];
/*     */ 
/* 243 */     entryList.toArray(entries);
/* 244 */     return entries;
/*     */   }
/*     */ 
/*     */   private class CategoryIterator extends Iterators.Filter<DictionaryEntry<C>>
/*     */   {
/*     */     private final C mCategory;
/*     */ 
/*     */     public CategoryIterator()
/*     */     {
/* 266 */       super();
/* 267 */       this.mCategory = category;
/*     */     }
/*     */ 
/*     */     public boolean accept(DictionaryEntry<C> entry) {
/* 271 */       return entry.category().equals(this.mCategory);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class PhraseIterator extends Iterators.Filter<DictionaryEntry<C>>
/*     */   {
/*     */     private final String mPhrase;
/*     */ 
/*     */     public PhraseIterator(String phrase)
/*     */     {
/* 252 */       super();
/* 253 */       this.mPhrase = phrase;
/*     */     }
/*     */ 
/*     */     public boolean accept(DictionaryEntry<C> entry) {
/* 257 */       return entry.phrase().equals(this.mPhrase);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.AbstractDictionary
 * JD-Core Version:    0.6.2
 */