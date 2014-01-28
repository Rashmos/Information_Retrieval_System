/*     */ package com.aliasi.symbol;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class CompiledSymbolTable
/*     */   implements SymbolTable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -8025428413920807070L;
/*     */   private final String[] mSymbols;
/*     */ 
/*     */   public CompiledSymbolTable(String[] symbols)
/*     */   {
/*  81 */     this.mSymbols = new String[symbols.length];
/*  82 */     System.arraycopy(symbols, 0, this.mSymbols, 0, symbols.length);
/*  83 */     Arrays.sort(this.mSymbols);
/*     */   }
/*     */ 
/*     */   private CompiledSymbolTable(String[] symbols, boolean ignore) {
/*  87 */     this.mSymbols = symbols;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/*  91 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 102 */     return Arrays.asList(this.mSymbols).toString();
/*     */   }
/*     */ 
/*     */   public void write(DataOutputStream out)
/*     */     throws IOException
/*     */   {
/* 118 */     out.writeInt(numSymbols());
/* 119 */     for (int i = 0; i < numSymbols(); i++) {
/* 120 */       String symbol = idToSymbol(i);
/* 121 */       out.writeShort(symbol.length());
/* 122 */       out.writeChars(symbol);
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeObj(ObjectOutput out) throws IOException {
/* 127 */     out.writeInt(numSymbols());
/* 128 */     for (int i = 0; i < numSymbols(); i++) {
/* 129 */       String symbol = idToSymbol(i);
/* 130 */       out.writeShort(symbol.length());
/* 131 */       out.writeChars(symbol);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int symbolToID(String symbol)
/*     */   {
/* 144 */     int result = Arrays.binarySearch(this.mSymbols, symbol);
/* 145 */     return result < 0 ? -1 : result;
/*     */   }
/*     */ 
/*     */   public String idToSymbol(int id)
/*     */   {
/* 160 */     return this.mSymbols[id];
/*     */   }
/*     */ 
/*     */   public int numSymbols()
/*     */   {
/* 169 */     return this.mSymbols.length;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 178 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int getOrAddSymbol(String symbol)
/*     */   {
/* 189 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public int removeSymbol(String symbol)
/*     */   {
/* 200 */     throw new UnsupportedOperationException(); } 
/*     */   static class Serializer extends AbstractExternalizable { static final long serialVersionUID = 2115083345444042460L;
/*     */     private final CompiledSymbolTable mSymbolTable;
/*     */     static final boolean IGNORE = true;
/*     */ 
/* 207 */     public Serializer(CompiledSymbolTable symbolTable) { this.mSymbolTable = symbolTable; }
/*     */ 
/*     */     public Serializer() {
/* 210 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 214 */       out.writeInt(this.mSymbolTable.mSymbols.length);
/* 215 */       for (String symbol : this.mSymbolTable.mSymbols)
/* 216 */         out.writeUTF(symbol);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws ClassNotFoundException, IOException {
/* 220 */       int numSymbols = in.readInt();
/* 221 */       String[] symbols = new String[numSymbols];
/* 222 */       for (int i = 0; i < numSymbols; i++)
/* 223 */         symbols[i] = in.readUTF();
/* 224 */       return new CompiledSymbolTable(symbols, true, null);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.symbol.CompiledSymbolTable
 * JD-Core Version:    0.6.2
 */