package com.aliasi.chunk;

import com.aliasi.tag.StringTagging;
import com.aliasi.tag.TagLattice;
import com.aliasi.tag.Tagging;
import java.util.Iterator;
import java.util.Set;

public abstract interface TagChunkCodec
{
  public abstract Tagging<String> toTagging(Chunking paramChunking);

  public abstract StringTagging toStringTagging(Chunking paramChunking);

  public abstract Chunking toChunking(StringTagging paramStringTagging);

  public abstract Set<String> tagSet(Set<String> paramSet);

  public abstract boolean legalTags(String[] paramArrayOfString);

  public abstract boolean legalTagSubSequence(String[] paramArrayOfString);

  public abstract boolean isEncodable(Chunking paramChunking);

  public abstract boolean isDecodable(StringTagging paramStringTagging);

  public abstract Iterator<Chunk> nBestChunks(TagLattice<String> paramTagLattice, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.TagChunkCodec
 * JD-Core Version:    0.6.2
 */