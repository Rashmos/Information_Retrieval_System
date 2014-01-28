/*     */ package com.aliasi.symbol;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SymbolTableCompiler
/*     */   implements Compilable, SymbolTable
/*     */ {
/*  54 */   private final Set<String> mSymbolSet = new HashSet();
/*     */   private CompiledSymbolTable mSymbolTable;
/*     */ 
/*     */   public static SymbolTable asSymbolTable(String[] symbols)
/*     */   {
/*  86 */     Set symbolSet = new HashSet();
/*  87 */     for (String symbol : symbols) {
/*  88 */       if (!symbolSet.add(symbol)) {
/*  89 */         String msg = "Duplicate symbol=" + symbol + " Symbols=" + Arrays.asList(symbols);
/*     */ 
/*  91 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/*  94 */     MapSymbolTable table = new MapSymbolTable(0);
/*  95 */     for (String symbol : symbols)
/*  96 */       table.getOrAddSymbolInteger(symbol);
/*  97 */     return MapSymbolTable.unmodifiableView(table);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 108 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public int symbolToID(String symbol)
/*     */   {
/* 125 */     if (this.mSymbolTable == null) return -1;
/* 126 */     return this.mSymbolTable.symbolToID(symbol);
/*     */   }
/*     */ 
/*     */   public String[] symbols()
/*     */   {
/* 135 */     return (String[])this.mSymbolSet.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */   }
/*     */ 
/*     */   public String idToSymbol(int id)
/*     */   {
/* 154 */     if (this.mSymbolTable == null)
/* 155 */       throw new IndexOutOfBoundsException("Symbol table not compiled");
/* 156 */     return this.mSymbolTable.idToSymbol(id);
/*     */   }
/*     */ 
/*     */   public int numSymbols()
/*     */   {
/* 167 */     if (this.mSymbolTable == null) return -1;
/* 168 */     return this.mSymbolTable.numSymbols();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 177 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int getOrAddSymbol(String symbol)
/*     */   {
/* 188 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean addSymbol(String symbol)
/*     */   {
/* 204 */     if (symbol.length() > 32767) {
/* 205 */       String msg = "Symbol=" + symbol + " too long; max length=" + 32767;
/*     */ 
/* 208 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 210 */     return this.mSymbolSet.add(symbol);
/*     */   }
/*     */ 
/*     */   public int removeSymbol(String symbol)
/*     */   {
/* 221 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 233 */     return compile().toString();
/*     */   }
/*     */ 
/*     */   private CompiledSymbolTable compile() {
/* 237 */     if (this.mSymbolTable != null) return this.mSymbolTable;
/* 238 */     String[] symbols = new String[this.mSymbolSet.size()];
/* 239 */     Iterator symbolIterator = this.mSymbolSet.iterator();
/* 240 */     for (int id = 0; symbolIterator.hasNext(); id++)
/* 241 */       symbols[id] = ((String)symbolIterator.next());
/* 242 */     this.mSymbolTable = new CompiledSymbolTable(symbols);
/* 243 */     return this.mSymbolTable;
/*     */   }
/*     */   private static class Externalizer extends AbstractExternalizable {
/*     */     private static final long serialVersionUID = 1065202374901852230L;
/*     */     final SymbolTableCompiler mCompiler;
/*     */ 
/* 250 */     public Externalizer() { this(null); }
/*     */ 
/*     */     public Externalizer(SymbolTableCompiler compiler) {
/* 253 */       this.mCompiler = compiler;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 257 */       this.mCompiler.compile().writeObj(objOut);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws IOException {
/* 261 */       String[] symbols = new String[objIn.readInt()];
/* 262 */       for (int i = 0; i < symbols.length; i++) {
/* 263 */         StringBuilder sb = new StringBuilder();
/* 264 */         int length = objIn.readShort();
/* 265 */         for (int j = 0; j < length; j++)
/* 266 */           sb.append(objIn.readChar());
/* 267 */         symbols[i] = sb.toString();
/*     */       }
/* 269 */       return new CompiledSymbolTable(symbols);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.symbol.SymbolTableCompiler
 * JD-Core Version:    0.6.2
 */