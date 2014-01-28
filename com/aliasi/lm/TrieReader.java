package com.aliasi.lm;

import java.io.IOException;

public abstract interface TrieReader
{
  public abstract long readSymbol()
    throws IOException;

  public abstract long readCount()
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TrieReader
 * JD-Core Version:    0.6.2
 */