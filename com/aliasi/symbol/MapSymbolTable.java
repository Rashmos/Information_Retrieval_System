/*     */ package com.aliasi.symbol;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class MapSymbolTable
/*     */   implements Serializable, SymbolTable
/*     */ {
/*     */   static final long serialVersionUID = 3515814090489781415L;
/*  57 */   final HashMap<String, Integer> mSymbolToId = new HashMap();
/*  58 */   final HashMap<Integer, String> mIdToSymbol = new HashMap();
/*     */   private int mNextSymbol;
/*     */ 
/*     */   public MapSymbolTable()
/*     */   {
/*  67 */     this(0);
/*     */   }
/*     */ 
/*     */   public MapSymbolTable(int firstId)
/*     */   {
/*  78 */     this.mNextSymbol = firstId;
/*     */   }
/*     */ 
/*     */   public MapSymbolTable(Map<String, Integer> symbolToIdMap)
/*     */   {
/*  91 */     int maxSymbol = -1;
/*  92 */     for (Map.Entry entry : symbolToIdMap.entrySet()) {
/*  93 */       String symbol = (String)entry.getKey();
/*  94 */       Integer id = (Integer)entry.getValue();
/*  95 */       if (id.intValue() == -1) {
/*  96 */         String msg = "Symbols cannot be equal to the unknown symbol ID. MapSymbolTable.UNKNOWN_SYMBOL_ID=-1 found id=" + id;
/*     */ 
/*  99 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 101 */       if (this.mIdToSymbol.put(id, symbol) != null) {
/* 102 */         String msg = "Identifiers must be unique. Found duplicate identifiers. Identifier=" + id;
/*     */ 
/* 105 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 107 */       maxSymbol = Math.max(maxSymbol, ((Integer)entry.getValue()).intValue());
/*     */     }
/* 109 */     this.mNextSymbol = (maxSymbol + 1);
/* 110 */     this.mSymbolToId.putAll(symbolToIdMap);
/*     */   }
/*     */ 
/*     */   public Set<Integer> idSet()
/*     */   {
/* 119 */     return Collections.unmodifiableSet(this.mIdToSymbol.keySet());
/*     */   }
/*     */ 
/*     */   public Set<String> symbolSet()
/*     */   {
/* 128 */     return Collections.unmodifiableSet(this.mSymbolToId.keySet());
/*     */   }
/*     */ 
/*     */   private MapSymbolTable(ObjectInput objIn) throws IOException {
/* 132 */     int numEntries = objIn.readInt();
/* 133 */     int max = 0;
/* 134 */     for (int i = 0; i < numEntries; i++) {
/* 135 */       String symbol = objIn.readUTF();
/* 136 */       Integer id = Integer.valueOf(objIn.readInt());
/* 137 */       max = Math.max(max, id.intValue());
/* 138 */       this.mSymbolToId.put(symbol, id);
/* 139 */       this.mIdToSymbol.put(id, symbol);
/*     */     }
/* 141 */     this.mNextSymbol = (max + 1);
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 147 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public int numSymbols() {
/* 151 */     return this.mSymbolToId.size();
/*     */   }
/*     */ 
/*     */   public int symbolToID(String symbol) {
/* 155 */     Integer result = symbolToIDInteger(symbol);
/* 156 */     return result == null ? -1 : result.intValue();
/*     */   }
/*     */ 
/*     */   public Integer symbolToIDInteger(String symbol)
/*     */   {
/* 168 */     return (Integer)this.mSymbolToId.get(symbol);
/*     */   }
/*     */ 
/*     */   public String idToSymbol(Integer id)
/*     */   {
/* 182 */     String symbol = (String)this.mIdToSymbol.get(id);
/* 183 */     if (symbol == null) {
/* 184 */       String msg = "Could not find id=" + id;
/* 185 */       throw new IndexOutOfBoundsException(msg);
/*     */     }
/* 187 */     return symbol;
/*     */   }
/*     */ 
/*     */   public String idToSymbol(int id) {
/* 191 */     return idToSymbol(Integer.valueOf(id));
/*     */   }
/*     */ 
/*     */   public int removeSymbol(String symbol)
/*     */   {
/* 204 */     int id = symbolToID(symbol);
/* 205 */     if (id >= 0) {
/* 206 */       this.mSymbolToId.remove(symbol);
/* 207 */       this.mIdToSymbol.remove(Integer.valueOf(id));
/*     */     }
/* 209 */     return id;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 218 */     this.mSymbolToId.clear();
/* 219 */     this.mIdToSymbol.clear();
/*     */   }
/*     */ 
/*     */   public int getOrAddSymbol(String symbol)
/*     */   {
/* 230 */     return getOrAddSymbolInteger(symbol).intValue();
/*     */   }
/*     */ 
/*     */   public Integer getOrAddSymbolInteger(String symbol)
/*     */   {
/* 241 */     Integer id = (Integer)this.mSymbolToId.get(symbol);
/* 242 */     if (id != null) return id;
/* 243 */     Integer freshId = Integer.valueOf(this.mNextSymbol++);
/* 244 */     this.mSymbolToId.put(symbol, freshId);
/* 245 */     this.mIdToSymbol.put(freshId, symbol);
/* 246 */     return freshId;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 257 */     return this.mIdToSymbol.toString();
/*     */   }
/*     */ 
/*     */   public static SymbolTable unmodifiableView(SymbolTable table)
/*     */   {
/* 275 */     return new UnmodifiableViewTable(table);
/*     */   }
/*     */ 
/*     */   private static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = -6040616216389802649L;
/*     */     final MapSymbolTable mSymbolTable;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 337 */       this.mSymbolTable = null;
/*     */     }
/* 339 */     public Externalizer(MapSymbolTable symbolTable) { this.mSymbolTable = symbolTable; }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException
/*     */     {
/* 343 */       return new MapSymbolTable(in, null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 347 */       objOut.writeInt(this.mSymbolTable.mSymbolToId.size());
/* 348 */       for (Map.Entry entry : this.mSymbolTable.mSymbolToId.entrySet()) {
/* 349 */         objOut.writeUTF((String)entry.getKey());
/* 350 */         objOut.writeInt(((Integer)entry.getValue()).intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UnmodifiableViewTable
/*     */     implements SymbolTable, Serializable
/*     */   {
/*     */     static final long serialVersionUID = 3326236896411055713L;
/*     */     private final SymbolTable mSymbolTable;
/*     */     static final String UNSUPPORTED_MSG = "Cannot modify the underlying symbol table from this view.";
/*     */ 
/*     */     UnmodifiableViewTable(SymbolTable symbolTable)
/*     */     {
/* 284 */       this.mSymbolTable = symbolTable;
/*     */     }
/*     */     public void clear() {
/* 287 */       throw new UnsupportedOperationException("Cannot modify the underlying symbol table from this view.");
/*     */     }
/*     */     public int getOrAddSymbol(String symbol) {
/* 290 */       throw new UnsupportedOperationException("Cannot modify the underlying symbol table from this view.");
/*     */     }
/*     */     public int removeSymbol(String symbol) {
/* 293 */       throw new UnsupportedOperationException("Cannot modify the underlying symbol table from this view.");
/*     */     }
/*     */     public String idToSymbol(int id) {
/* 296 */       return this.mSymbolTable.idToSymbol(id);
/*     */     }
/*     */     public int numSymbols() {
/* 299 */       return this.mSymbolTable.numSymbols();
/*     */     }
/*     */     public int symbolToID(String symbol) {
/* 302 */       return this.mSymbolTable.symbolToID(symbol);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 306 */       return this.mSymbolTable.toString();
/*     */     }
/*     */     Object writeReplace() {
/* 309 */       return new Serializer(this);
/*     */     }
/*     */     static class Serializer extends AbstractExternalizable { static final long serialVersionUID = -5293452773208612837L;
/*     */       final MapSymbolTable.UnmodifiableViewTable mSymbolTable;
/*     */ 
/* 315 */       public Serializer() { this(null); }
/*     */ 
/*     */       public Serializer(MapSymbolTable.UnmodifiableViewTable symbolTable) {
/* 318 */         this.mSymbolTable = symbolTable;
/*     */       }
/*     */ 
/*     */       public void writeExternal(ObjectOutput objOut) throws IOException {
/* 322 */         objOut.writeObject(this.mSymbolTable.mSymbolTable);
/*     */       }
/*     */ 
/*     */       public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 326 */         SymbolTable symbolTable = (SymbolTable)in.readObject();
/* 327 */         return new MapSymbolTable.UnmodifiableViewTable(symbolTable);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.symbol.MapSymbolTable
 * JD-Core Version:    0.6.2
 */