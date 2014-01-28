/*     */ package com.aliasi.dict;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Iterators;
/*     */ import com.aliasi.util.Iterators.Buffered;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public class TrieDictionary<C> extends AbstractDictionary<C>
/*     */   implements Serializable, Compilable
/*     */ {
/*     */   static final long serialVersionUID = -6772406715071883449L;
/*  71 */   Node<C> mRootNode = new Node();
/*     */ 
/*     */   DictionaryEntry<C>[] phraseEntries(String phrase)
/*     */   {
/*  82 */     Node node = this.mRootNode;
/*  83 */     for (int i = 0; i < phrase.length(); i++) {
/*  84 */       node = node.getDtr(phrase.charAt(i));
/*  85 */       if (node == null) return Node.emptyEntries();
/*     */     }
/*  87 */     return node.mEntries;
/*     */   }
/*     */ 
/*     */   public Iterator<DictionaryEntry<C>> phraseEntryIt(String phrase)
/*     */   {
/*  92 */     return Iterators.array(phraseEntries(phrase));
/*     */   }
/*     */ 
/*     */   public void addEntry(DictionaryEntry<C> entry)
/*     */   {
/* 100 */     String phrase = entry.phrase();
/* 101 */     Node node = this.mRootNode;
/* 102 */     for (int i = 0; i < phrase.length(); i++)
/* 103 */       node = node.getOrAddDtr(phrase.charAt(i));
/* 104 */     node.addEntry(entry);
/*     */   }
/*     */ 
/*     */   public Iterator<DictionaryEntry<C>> iterator()
/*     */   {
/* 117 */     return new TrieIterator(this.mRootNode);
/*     */   }
/*     */ 
/*     */   private Object writeReplace() {
/* 121 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 133 */     out.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   private static class TrieIterator<D> extends Iterators.Buffered<DictionaryEntry<D>>
/*     */   {
/* 168 */     LinkedList<Node<D>> mQueue = new LinkedList();
/*     */     DictionaryEntry<D>[] mEntries;
/* 170 */     int mNextEntry = -1;
/*     */ 
/* 172 */     TrieIterator(Node<D> root) { this.mQueue.add(root); }
/*     */ 
/*     */     protected DictionaryEntry<D> bufferNext()
/*     */     {
/* 176 */       while ((this.mEntries == null) && (!this.mQueue.isEmpty())) {
/* 177 */         Node node = (Node)this.mQueue.removeFirst();
/* 178 */         addDtrs(node.mDtrNodes);
/* 179 */         if (node.mEntries.length > 0) {
/* 180 */           this.mEntries = node.mEntries;
/* 181 */           this.mNextEntry = 0;
/*     */         }
/*     */       }
/* 184 */       if (this.mEntries == null) return null;
/* 185 */       DictionaryEntry result = this.mEntries[(this.mNextEntry++)];
/* 186 */       if (this.mNextEntry >= this.mEntries.length) this.mEntries = null;
/* 187 */       return result;
/*     */     }
/*     */     void addDtrs(Node<D>[] dtrs) {
/* 190 */       int i = dtrs.length;
/*     */       while (true) { i--; if (i < 0) break;
/* 191 */         if (dtrs[i] == null) System.out.println("ADDING=" + i);
/* 192 */         this.mQueue.addFirst(dtrs[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Externalizer<F> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -6351978792499636468L;
/*     */     private final TrieDictionary<F> mDictionary;
/*     */ 
/*     */     public Externalizer(TrieDictionary<F> dict)
/*     */     {
/* 140 */       this.mDictionary = dict;
/*     */     }
/*     */     public Externalizer() {
/* 143 */       this(null);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 147 */       TrieDictionary dict = new TrieDictionary();
/* 148 */       int numEntries = in.readInt();
/* 149 */       for (int i = 0; i < numEntries; i++)
/*     */       {
/* 152 */         DictionaryEntry entry = (DictionaryEntry)in.readObject();
/* 153 */         dict.addEntry(entry);
/*     */       }
/* 155 */       return dict;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 159 */       int count = this.mDictionary.size();
/* 160 */       out.writeInt(count);
/* 161 */       for (DictionaryEntry entry : this.mDictionary)
/* 162 */         entry.compileTo(out);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.TrieDictionary
 * JD-Core Version:    0.6.2
 */