/*    */ package com.aliasi.dict;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ class Node<C>
/*    */ {
/* 29 */   static final DictionaryEntry[] EMPTY_ENTRIES = new DictionaryEntry[0];
/* 30 */   static final char[] EMPTY_CHARS = new char[0];
/*    */ 
/* 32 */   static final Node[] EMPTY_NODES = new Node[0];
/*    */ 
/* 48 */   DictionaryEntry<C>[] mEntries = emptyEntries();
/*    */ 
/* 50 */   char[] mDtrChars = EMPTY_CHARS;
/*    */ 
/* 52 */   Node<C>[] mDtrNodes = emptyNodes();
/*    */ 
/*    */   static <D> DictionaryEntry<D>[] emptyEntries()
/*    */   {
/* 37 */     DictionaryEntry[] entries = (DictionaryEntry[])EMPTY_ENTRIES;
/* 38 */     return entries;
/*    */   }
/*    */ 
/*    */   static <D> Node<D>[] emptyNodes()
/*    */   {
/* 44 */     Node[] nodes = (Node[])EMPTY_NODES;
/* 45 */     return nodes;
/*    */   }
/*    */ 
/*    */   Node<C> getDtr(char c)
/*    */   {
/* 55 */     int i = Arrays.binarySearch(this.mDtrChars, c);
/* 56 */     return i < 0 ? null : this.mDtrNodes[i];
/*    */   }
/*    */   Node<C> getOrAddDtr(char c) {
/* 59 */     Node dtr = getDtr(c);
/* 60 */     if (dtr != null) return dtr;
/* 61 */     Node result = new Node();
/* 62 */     char[] oldDtrChars = this.mDtrChars;
/* 63 */     Node[] oldDtrNodes = this.mDtrNodes;
/* 64 */     this.mDtrChars = new char[this.mDtrChars.length + 1];
/*    */ 
/* 67 */     Node[] dtrNodes = (Node[])new Node[this.mDtrNodes.length + 1];
/* 68 */     this.mDtrNodes = dtrNodes;
/* 69 */     for (int i = 0; 
/* 70 */       (i < oldDtrChars.length) && 
/* 71 */       (oldDtrChars[i] <= c); i++)
/*    */     {
/* 72 */       this.mDtrChars[i] = oldDtrChars[i];
/* 73 */       this.mDtrNodes[i] = oldDtrNodes[i];
/*    */     }
/* 75 */     this.mDtrChars[i] = c;
/* 76 */     this.mDtrNodes[i] = result;
/* 77 */     for (; i < oldDtrChars.length; i++) {
/* 78 */       this.mDtrChars[(i + 1)] = oldDtrChars[i];
/* 79 */       this.mDtrNodes[(i + 1)] = oldDtrNodes[i];
/*    */     }
/* 81 */     return result;
/*    */   }
/*    */ 
/*    */   void addEntry(DictionaryEntry<C> entry) {
/* 85 */     DictionaryEntry[] oldEntries = this.mEntries;
/* 86 */     for (int i = 0; i < oldEntries.length; i++) {
/* 87 */       if (oldEntries[i].equals(entry)) return;
/*    */     }
/*    */ 
/* 90 */     DictionaryEntry[] entries = (DictionaryEntry[])new DictionaryEntry[oldEntries.length + 1];
/*    */ 
/* 92 */     this.mEntries = entries;
/* 93 */     this.mEntries[0] = entry;
/* 94 */     System.arraycopy(oldEntries, 0, this.mEntries, 1, oldEntries.length);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.Node
 * JD-Core Version:    0.6.2
 */