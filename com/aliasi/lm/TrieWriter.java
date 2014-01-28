package com.aliasi.lm;

import java.io.IOException;

public abstract interface TrieWriter
{
  public abstract void writeCount(long paramLong)
    throws IOException;

  public abstract void writeSymbol(long paramLong)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TrieWriter
 * JD-Core Version:    0.6.2
 */