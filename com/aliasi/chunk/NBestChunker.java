package com.aliasi.chunk;

import com.aliasi.util.ScoredObject;
import java.util.Iterator;

public abstract interface NBestChunker extends Chunker
{
  public abstract Iterator<ScoredObject<Chunking>> nBest(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.NBestChunker
 * JD-Core Version:    0.6.2
 */