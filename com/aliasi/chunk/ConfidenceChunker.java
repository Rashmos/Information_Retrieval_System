package com.aliasi.chunk;

import java.util.Iterator;

public abstract interface ConfidenceChunker
{
  public abstract Iterator<Chunk> nBestChunks(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.ConfidenceChunker
 * JD-Core Version:    0.6.2
 */