package com.aliasi.symbol;

public abstract interface SymbolTable
{
  public static final int UNKNOWN_SYMBOL_ID = -1;

  public abstract int symbolToID(String paramString);

  public abstract String idToSymbol(int paramInt);

  public abstract int numSymbols();

  public abstract int getOrAddSymbol(String paramString);

  public abstract int removeSymbol(String paramString);

  public abstract void clear();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.symbol.SymbolTable
 * JD-Core Version:    0.6.2
 */