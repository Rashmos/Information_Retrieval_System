package org.apache.lucene.index;

import org.apache.lucene.store.Directory;

final class SegmentInfo
{
  public String name;
  public int docCount;
  public Directory dir;

  public SegmentInfo(String paramString, int paramInt, Directory paramDirectory)
  {
    this.name = paramString;
    this.docCount = paramInt;
    this.dir = paramDirectory;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentInfo
 * JD-Core Version:    0.6.2
 */