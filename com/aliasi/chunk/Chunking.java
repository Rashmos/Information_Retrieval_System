package com.aliasi.chunk;

import java.util.Set;

public abstract interface Chunking
{
  public abstract Set<Chunk> chunkSet();

  public abstract CharSequence charSequence();

  public abstract boolean equals(Object paramObject);

  public abstract int hashCode();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.Chunking
 * JD-Core Version:    0.6.2
 */