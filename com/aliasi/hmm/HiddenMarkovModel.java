package com.aliasi.hmm;

import com.aliasi.symbol.SymbolTable;

public abstract interface HiddenMarkovModel
{
  public abstract SymbolTable stateSymbolTable();

  public abstract double startProb(int paramInt);

  public abstract double startProb(String paramString);

  public abstract double startLog2Prob(int paramInt);

  public abstract double startLog2Prob(String paramString);

  public abstract double endProb(int paramInt);

  public abstract double endProb(String paramString);

  public abstract double endLog2Prob(int paramInt);

  public abstract double endLog2Prob(String paramString);

  public abstract double transitProb(int paramInt1, int paramInt2);

  public abstract double transitLog2Prob(int paramInt1, int paramInt2);

  public abstract double emitProb(int paramInt, CharSequence paramCharSequence);

  public abstract double emitLog2Prob(int paramInt, CharSequence paramCharSequence);

  public abstract double transitProb(String paramString1, String paramString2);

  public abstract double transitLog2Prob(String paramString1, String paramString2);

  public abstract double emitProb(String paramString, CharSequence paramCharSequence);

  public abstract double emitLog2Prob(String paramString, CharSequence paramCharSequence);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.hmm.HiddenMarkovModel
 * JD-Core Version:    0.6.2
 */